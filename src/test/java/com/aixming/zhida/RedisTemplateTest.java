package com.aixming.zhida;

import cn.hutool.json.JSONUtil;
import com.aixming.zhida.constant.RedisKey;
import com.aixming.zhida.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author AixMing
 * @since 2025-05-23 15:38:09
 */
@SpringBootTest
public class RedisTemplateTest {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedisKey() {
        User user = new User();
        user.setId(1L);
        user.setUserAccount("ksjdklasjlda");
        user.setUserPassword("12345678");
        user.setUnionId("11111");
        user.setUserName("sadasdad");
        user.setUserAvatar("你好啊");

        redisTemplate.opsForValue().set(RedisKey.getKey(RedisKey.TOKEN_KEY, "askldjlasjkdlaaaaaa1111"), JSONUtil.toJsonStr(user), 10, TimeUnit.MINUTES);
        User newUser = (User) redisTemplate.opsForValue().get(RedisKey.getKey(RedisKey.TOKEN_KEY, "askldjlasjkdlaaaaaa1111"));
        System.out.println(newUser);
    }

}
