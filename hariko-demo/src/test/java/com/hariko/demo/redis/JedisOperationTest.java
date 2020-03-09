package com.hariko.demo.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JedisOperationTest {

    @Autowired
    private StringRedisTemplate stringRedisClusterTemplateByJedis;
    @Autowired
    private RedisTemplate redisClusterTemplateByJedis;
    @Autowired
    private RedisConnectionFactory clusterConnectionFactoryByJedis;

    @Test // 查证集群中结点信息
    public void testStringRedisClusterTemplateByJedis01() {
        RedisClusterNode redisClusterNode = new RedisClusterNode("192.168.0.103", 7021);

        Set<RedisClusterNode> redisClusterNodeSet = (Set<RedisClusterNode>) stringRedisClusterTemplateByJedis.opsForCluster().getSlaves(redisClusterNode);

        Assert.assertEquals(redisClusterNodeSet.size(), 2, 0);
    }

    @Test // 插入数据
    public void testStringRedisClusterTemplateByJedis02() {
        stringRedisClusterTemplateByJedis.opsForValue().set("hariko_cluster_key_04", "testStringRedisClusterTemplateByJedis02");
    }

    @Test
    public void testRedisClusterTemplateByJedis() {
        Student student = new Student();
        student.setName("hariko");
        student.setAge(29);
        redisClusterTemplateByJedis.opsForList().rightPush("hariko_cluster_list_01", student);
    }

    static class Student {
        String name;
        int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}

