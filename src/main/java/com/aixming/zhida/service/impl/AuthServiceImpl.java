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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author AixMing
 * @since 2025-05-23 20:04:11
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserService userService;

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
        int expireTime = 24 * 60 * 60 * 1000;
        userLoginResponse.setAccessToken(JwtUtils.createToken(claims, expireTime));
        userLoginResponse.setRefreshToken(JwtUtils.createToken(claims, 2 * expireTime));
        return userLoginResponse;
    }
}
