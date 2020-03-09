package com.hariko.demo.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LettuceOperationTest {

    @Autowired
    private StringRedisTemplate stringRedisClusterTemplateByLettuce;


    @Test // 插入数据
    public void testStringRedisClusterTemplateByLettuce02() {
        stringRedisClusterTemplateByLettuce.opsForValue().set("hariko_cluster_key_03", "testStringRedisClusterTemplateByLettuce02");
    }

    @Test
    public void testStringRedisCluterTemplateByLettuce03() {
        stringRedisClusterTemplateByLettuce.opsForList().leftPush("hariko_cluster_lettuce_list_key_01", "hahahahha");
    }
}

