package com.aixming.zhida.filter;

import com.aixming.zhida.common.ErrorCode;
import com.aixming.zhida.exception.BusinessException;
import com.aixming.zhida.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author AixMing
 * @since 2025-05-23 19:14:47
 */
//@WebFilter(urlPatterns = {"/*"})
//@Order(1)
//@Component
@Slf4j
public class AuthFilter implements Filter {

    private static final String[] whiteList = {"/alipay", "/webjars", "/login", "/register", "/logout", "/doc.html"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 白名单直接放行
        String requestURI = request.getRequestURI();
        if (StringUtils.containsAny(requestURI, whiteList)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 从 header 中获取 token，并验证
        String token = request.getHeader("Authorization");
        Map<String, Object> claims = JwtUtils.checkToken(token);
        if (StringUtils.isBlank(token) || claims == null) {
            log.info("拦截请求：{}", requestURI);
            throw new BusinessException(ErrorCode.TOKEN_ERROR);
        }
        // 将用户信息放入 request 域当中并放行
        request.setAttribute("loginUser", claims);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
