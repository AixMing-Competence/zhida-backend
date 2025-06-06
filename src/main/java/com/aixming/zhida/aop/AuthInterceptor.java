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

//    @Around("@annotation(authCheck)")
//    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        String token = request.getHeader("Authorization");
//        Map<String, Object> claims = JwtUtils.checkToken(token);
//        ThrowUtils.throwIf(claims == null, ErrorCode.NOT_LOGIN_ERROR);
//        UserRoleEnum roleEnum = UserRoleEnum.getEnumByValue((String) claims.get("role"));
//        UserRoleEnum needRoleEnum = UserRoleEnum.getEnumByValue(authCheck.mustRole());
//        request.setAttribute("loginUser", claims);
//        // 不需要权限，直接放行
//        if (needRoleEnum == null) {
//            return joinPoint.proceed();
//        }
//        if (roleEnum == null) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        if (UserRoleEnum.BAN.equals(roleEnum)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        if (UserRoleEnum.ADMIN.equals(needRoleEnum)) {
//            if (!UserRoleEnum.ADMIN.equals(roleEnum)) {
//                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//            }
//        }
//        return joinPoint.proceed();
//    }

    private static final String[] whiteList = {"/alipay", "/webjars", "/login", "/register", "/logout", "/doc.html"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String requestURI = request.getRequestURI();
        if (
                requestURI.contains("/swagger") ||
                        requestURI.contains("/user/login") ||
                        requestURI.contains("/logout") ||
                        requestURI.contains("/register") ||
                        requestURI.contains("/auth/login") ||
//                        requestURI.contains("/auth/refresh_token") ||
                        requestURI.contains("/error") ||
                        requestURI.contains("/alipay")
        ) {
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
