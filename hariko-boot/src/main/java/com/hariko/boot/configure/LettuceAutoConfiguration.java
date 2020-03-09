package com.hariko.boot.configure;

import com.hariko.boot.configure.redis.RedisClusterProperties;
import io.lettuce.core.resource.ClientResources;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.UnknownHostException;

@Configuration
@ConditionalOnClass(RedisClusterProperties.class)
@ConditionalOnProperty(prefix = "spring.redis.cluster", name = "lettuce", havingValue = "enable")
@EnableConfigurationProperties(RedisClusterProperties.class)
public class LettuceAutoConfiguration {

    @Autowired
    RedisClusterProperties redisClusterProperties;

    @Bean("stringRedisClusterTemplateByLettuce")
    @ConditionalOnMissingBean(name = "stringRedisClusterTemplateByLettuce")
    public StringRedisTemplate stringRedisClusterTemplateByLettuce(LettuceConnectionFactory redisConnectionFactory001) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory001);
        return redisTemplate;
    }

    @Bean("redisConnectionFactory001")
    LettuceConnectionFactory redisConnectionFactory001(
            ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers,
            ClientResources clientResources) throws UnknownHostException {
        LettuceClientConfiguration clientConfig = getLettuceClientConfiguration(builderCustomizers, clientResources);

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisClusterProperties.getNodes());
        redisClusterConfiguration.setPassword(redisClusterProperties.getPassword());
        return new LettuceConnectionFactory(redisClusterConfiguration, clientConfig);
    }

    private LettuceClientConfiguration getLettuceClientConfiguration(
            ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers,
            ClientResources clientResources) {
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = LettuceClientConfiguration.builder();
        builder.clientResources(clientResources);
        builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder.build();
    }

}
