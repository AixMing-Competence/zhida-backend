package com.aixming.zhida.aop;

import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.exception.BusinessException;
import com.aixming.zhida.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author AixMing
 * @since 2025-05-26 20:58:47
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/swagger") || requestURI.contains("/user/login") || requestURI.contains("/logout") || requestURI.contains("/register")) {
            return true;
        }
        String token = request.getHeader("Authorization");
        Map<String, Object> claims = JwtUtils.checkToken(token);
        if (claims == null) {
            log.info("拦截请求：{}", requestURI);
            throw new BusinessException(ErrorCode.TOKEN_ERROR);
        }
        request.setAttribute("loginUser", claims);
        return true;
    }

}
