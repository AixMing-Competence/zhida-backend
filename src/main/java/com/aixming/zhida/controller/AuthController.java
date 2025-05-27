package com.aixming.zhida.controller;

import com.aixming.zhida.common.BaseResponse;
import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.common.ResultUtils;
import com.aixming.zhida.exception.ThrowUtils;
import com.aixming.zhida.model.dto.user.UserLoginRequest;
import com.aixming.zhida.model.dto.user.UserLoginResponse;
import com.aixming.zhida.service.AuthService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        return ResultUtils.success(true);
    }

    @GetMapping("/refresh_token")
    public BaseResponse<UserLoginResponse> refreshToken(HttpServletRequest request) {
//        Map<String, Object> claims = JwtUtils.checkToken(token);
//        ThrowUtils.throwIf(claims == null, ErrorCode.TOKEN_ERROR);
        Map<String, Object> claims = (Map<String, Object>) request.getAttribute("loginUser");
        String refreshToken = request.getHeader("Authorization");
        UserLoginResponse userLoginResponse = authService.refreshToken(refreshToken, claims);
        ThrowUtils.throwIf(userLoginResponse == null, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(userLoginResponse);
    }

}
