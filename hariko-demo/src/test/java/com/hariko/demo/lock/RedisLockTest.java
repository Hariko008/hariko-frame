package com.hariko.demo.lock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.cluster.lock.DistributedLock;
import org.springframework.cloud.cluster.redis.lock.RedisLockService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisLockTest {

    @Autowired
    private StringRedisTemplate stringRedisClusterTemplateByJedis;
    @Autowired
    private RedisConnectionFactory clusterConnectionFactoryByJedis;

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

