package com.whu.checky;


import com.whu.checky.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        // set
        stringRedisTemplate.opsForValue().set("key", "value");
        // get
        String value = stringRedisTemplate.opsForValue().get("key");
        System.out.println(value);
        Assert.assertEquals("value", value);
    }

    @Test
    public void testObj() throws Exception {
        User user = new User();
        user.setSessionId("asfasfgagsa");
        user.setUserName("test");
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        // set
        operations.set("obj.user.key", user);
        // get
        User user2 = operations.get("obj.user.key");
    }

}
