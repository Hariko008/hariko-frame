package com.hariko.boot.configure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.hariko.boot.configure.redis.RedisClusterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnClass(RedisClusterProperties.class)
@EnableConfigurationProperties(RedisClusterProperties.class)
public class RedisConfiguration {

    @Autowired
    RedisClusterProperties redisClusterProperties;

    @Bean("clusterConnectionFactoryByJedis")
    RedisConnectionFactory clusterConnectionFactoryByJedis() {

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisClusterProperties.getNodes());
        redisClusterConfiguration.setPassword(redisClusterProperties.getPassword());

        return new JedisConnectionFactory(redisClusterConfiguration);
    }

    @Bean("redisClusterTemplateByJedis")
    @ConditionalOnMissingBean(name = "redisClusterTemplateByJedis")
    public RedisTemplate<Object, Object> redisClusterTemplateByJedis(RedisConnectionFactory clusterConnectionFactoryByJedis) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(clusterConnectionFactoryByJedis);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(getJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer getJackson2JsonRedisSerializer() {
        // 设置序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(
                Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        /**
         * 这一句必须要，作用是序列化时将对象全类名一起保存下来
         * 设置之后的序列化结果如下：
         *  [
         *   "com.dxy.cache.pojo.Dept",
         *   {
         *     "pid": 1,
         *     "code": "11",
         *     "name": "财务部1"
         *   }
         * ]
         *
         * 不设置的话，序列化结果如下，将无法反序列化
         *
         *  {
         *     "pid": 1,
         *     "code": "11",
         *     "name": "财务部1"
         *   }
         */
        //objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //因为上面那句代码已经被标记成作废，因此用下面这个方法代替，仅仅测试了一下，不知道是否完全正确
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    @Bean("stringRedisClusterTemplateByJedis")
    @ConditionalOnMissingBean(name = "stringRedisClusterTemplateByJedis")
    public StringRedisTemplate stringRedisClusterTemplateByJedis(RedisConnectionFactory clusterConnectionFactoryByJedis) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(clusterConnectionFactoryByJedis);
        return redisTemplate;
    }

    @Bean("stringRedisClusterTemplateByLettuce")
    @ConditionalOnMissingBean(name = "stringRedisClusterTemplateByLettuce")
    public StringRedisTemplate stringRedisClusterTemplateByLettuce(){
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisClusterProperties.getNodes());
        redisClusterConfiguration.setPassword(redisClusterProperties.getPassword());
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration);

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return redisTemplate;
    }
}
