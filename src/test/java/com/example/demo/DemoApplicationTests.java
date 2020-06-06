package com.example.demo;

import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;

@SpringBootTest
class DemoApplicationTests {
    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer<>(new File("src/test/resources/compose-test.yml"))
                    .withExposedService("myredis", 6379,
                            Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    void contextLoads() {
        environment.start();
        Assertions.assertNull(redisTemplate.opsForValue().get("Mykey"));
        redisTemplate.opsForValue().set("Mykey", "Myvalue");
        String myvalue = redisTemplate.opsForValue().get("Mykey");
        System.out.println("value: " + myvalue);
        Assertions.assertEquals("Myvalue",redisTemplate.opsForValue().get("Mykey"));
    }

}
