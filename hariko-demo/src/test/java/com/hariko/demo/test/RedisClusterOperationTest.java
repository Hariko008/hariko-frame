package com.hariko.demo.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.cluster.lock.DistributedLock;
import org.springframework.cloud.cluster.redis.lock.RedisLockService;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisClusterOperationTest {

    @Autowired
    private StringRedisTemplate stringRedisClusterTemplateByJedis;
    @Autowired
    private RedisTemplate redisClusterTemplateByJedis;
    @Autowired
    private RedisConnectionFactory clusterConnectionFactoryByJedis;
    @Autowired
    private StringRedisTemplate stringRedisClusterTemplateByLettuce;

    @Test // 查证集群中结点信息
    public void testStringRedisClusterTemplateByJedis01(){
        RedisClusterNode redisClusterNode = new RedisClusterNode("192.168.0.103", 7021);

        Set<RedisClusterNode> redisClusterNodeSet = (Set<RedisClusterNode>) stringRedisClusterTemplateByJedis.opsForCluster().getSlaves(redisClusterNode);

        Assert.assertEquals(redisClusterNodeSet.size(), 2, 0);
    }

    @Test // 插入数据
    public void testStringRedisClusterTemplateByJedis02(){
        stringRedisClusterTemplateByJedis.opsForValue().set("hariko_cluster_key_01", "testStringRedisClusterTemplateByJedis02");
    }

    @Test // 插入数据
    public void testStringRedisClusterTemplateByLettuce02(){
        stringRedisClusterTemplateByLettuce.opsForValue().set("hariko_cluster_key_03", "testStringRedisClusterTemplateByLettuce02");
    }

    @Test
    public void testRedisClusterTemplateByJedis(){
        Student student = new Student();
        student.setName("hariko");
        student.setAge(29);
        redisClusterTemplateByJedis.opsForList().rightPush("hariko_cluster_list_01", student);
    }

    static class Student{
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

    @Test
    public void testSimpleLock() {
        RedisLockService lockService = new RedisLockService(clusterConnectionFactoryByJedis);
        DistributedLock lock = lockService.obtain("lock");
        lock.lock();

        Set<String> keys = stringRedisClusterTemplateByJedis.keys(RedisLockService.DEFAULT_REGISTRY_KEY + ":*");
        assertThat(keys.size(), is(1));
        assertThat(keys.iterator().next(), is(RedisLockService.DEFAULT_REGISTRY_KEY + ":lock"));

        lock.unlock();
    }

    @Test
    public void testSecondLockSucceed() {
        RedisLockService lockService = new RedisLockService(clusterConnectionFactoryByJedis);
        DistributedLock lock1 = lockService.obtain("lock");
        DistributedLock lock2 = lockService.obtain("lock");
        lock1.lock();
        // same thread so try/lock doesn't fail
        assertThat(lock2.tryLock(), is(true));
        lock2.lock();

        Set<String> keys = stringRedisClusterTemplateByJedis.keys(RedisLockService.DEFAULT_REGISTRY_KEY + ":*");
        assertThat(keys.size(), is(1));
        assertThat(keys.iterator().next(), is(RedisLockService.DEFAULT_REGISTRY_KEY + ":lock"));

        lock1.unlock();
        lock2.unlock();
    }
}

