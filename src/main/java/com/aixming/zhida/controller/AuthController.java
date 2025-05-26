package com.aixming.zhida.controller;

import com.aixming.zhida.common.BaseResponse;
import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.common.ResultUtils;
import com.aixming.zhida.exception.ThrowUtils;
import com.aixming.zhida.model.dto.user.UserLoginRequest;
import com.aixming.zhida.model.dto.user.UserLoginResponse;
import com.aixming.zhida.service.AuthService;
import com.aixming.zhida.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限接口
 *
 * @author AixMing
 * @since 2025-05-23 19:53:10
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public BaseResponse<UserLoginResponse> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        UserLoginResponse userLoginResponse = authService.userLogin(userLoginRequest);
        return ResultUtils.success(userLoginResponse);
    }

    @GetMapping("/refresh_token")
    public BaseResponse<UserLoginResponse> refreshToken(@RequestHeader("Authorization") String token) {
        Map<String, Object> claims = JwtUtils.checkToken(token);
        ThrowUtils.throwIf(claims == null, ErrorCode.TOKEN_ERROR);
        Object uid = claims.get("uid");
        Object role = claims.get("role");
        HashMap<String, Object> newClaims = new HashMap<String, Object>() {{
            put("uid", uid);
            put("role", role);
        }};
        int expireTime = 24 * 60 * 60 * 1000;
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setAccessToken(JwtUtils.createToken(claims, expireTime));
        userLoginResponse.setRefreshToken(JwtUtils.createToken(claims, 2 * expireTime));
        return ResultUtils.success(userLoginResponse);
    }

}
