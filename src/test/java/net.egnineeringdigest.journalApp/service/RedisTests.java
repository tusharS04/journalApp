package net.egnineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.JournalApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = JournalApplication.class)
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("email", "tusharpsomvanshi@gmail.com");

        Object email = redisTemplate.opsForValue().get("email");
        Object salary = redisTemplate.opsForValue().get("salary");
        //not getting email - need to set redis config

        System.out.println(email);
        System.out.println(salary);
    }
}
