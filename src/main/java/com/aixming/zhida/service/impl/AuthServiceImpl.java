package com.aixming.zhida.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.exception.ThrowUtils;
import com.aixming.zhida.model.dto.user.UserLoginRequest;
import com.aixming.zhida.model.dto.user.UserLoginResponse;
import com.aixming.zhida.model.entity.User;
import com.aixming.zhida.service.AuthService;
import com.aixming.zhida.service.UserService;
import com.aixming.zhida.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author AixMing
 * @since 2025-05-23 20:04:11
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    private static final String salt = "aixming";


    @Override
    public UserLoginResponse userLogin(UserLoginRequest userLoginRequest) {
        // 参数校验
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount, userPassword), ErrorCode.PARAMS_ERROR);
        userPassword = DigestUtil.md5Hex(salt + userPassword);
        // 是否存在该用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, userPassword)
                .last("limit 1");
        User user = userService.getOne(queryWrapper);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        // 生成 token
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("role", user.getUserRole());
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        long expireTime = 10 * 1000L;
        userLoginResponse.setAccessToken(JwtUtils.createToken(claims, expireTime));
        userLoginResponse.setRefreshToken(JwtUtils.createToken(claims, 2 * expireTime));
        return userLoginResponse;
    }

    @Override
    public UserLoginResponse refreshToken(String refreshToken, Map<String, Object> claims) {
        Long userId = ((Number) claims.get("uid")).longValue();
        // 当前 refresh_token 的锁
        String lockKey = String.format("zhida:auth:%s:%s", refreshToken, userId);
        RLock rLock = redissonClient.getLock(lockKey);
        Object role = claims.get("role");
        HashMap<String, Object> newClaims = new HashMap<String, Object>() {{
            put("uid", userId);
            put("role", role);
        }};
        long expireTime = 10 * 1000L;
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        try {
            boolean locked = rLock.tryLock(2, -1, TimeUnit.SECONDS);
            if (locked) {
                // 检查缓存中是否有由该 token 生成的 access_key 和 refresh_key
                String accessTokenKey = getTokenKey(refreshToken, "access_key", userId);
                String refreshTokenKey = getTokenKey(refreshToken, "refresh_key", userId);
                String accessToken = (String) redisTemplate.opsForValue().get(accessTokenKey);
                String newRefreshToken = (String) redisTemplate.opsForValue().get(refreshTokenKey);
                if (!StringUtils.isAnyBlank(accessToken, newRefreshToken)) {
                    userLoginResponse.setAccessToken(accessToken);
                    userLoginResponse.setRefreshToken(newRefreshToken);
                    return userLoginResponse;
                }
                // 生成 token
                accessToken = JwtUtils.createToken(newClaims, expireTime);
                refreshToken = JwtUtils.createToken(newClaims, 2 * expireTime);
                userLoginResponse.setAccessToken(accessToken);
                userLoginResponse.setRefreshToken(refreshToken);

                // 放入到 redis 当中
                redisTemplate.opsForValue().set(accessTokenKey, accessToken);
                redisTemplate.opsForValue().set(refreshTokenKey, refreshToken);
                return userLoginResponse;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (rLock != null && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
        return null;
    }

    private static String getTokenKey(String refreshToken, String tokenType, long userId) {
        return String.format("zhida:auth:%s:%s:%s", refreshToken, tokenType, userId);
    }
}
