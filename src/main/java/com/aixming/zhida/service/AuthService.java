package com.aixming.zhida.service;

import com.aixming.zhida.model.dto.user.UserLoginRequest;
import com.aixming.zhida.model.dto.user.UserLoginResponse;

/**
 * @author AixMing
 * @since 2025-05-23 20:04:11
 */
public interface AuthService {
    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @return
     */
    UserLoginResponse userLogin(UserLoginRequest userLoginRequest);
}
