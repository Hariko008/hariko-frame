package com.hariko.boot.configure.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Component(value = "redisClusterProperties")
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisClusterProperties {

    //@Value("${cluster.nodes}")
    List<String> nodes;

    //@Value("${cluster.password")
    String password;

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
