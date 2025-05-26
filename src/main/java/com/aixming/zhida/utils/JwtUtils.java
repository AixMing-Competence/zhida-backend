package com.aixming.zhida.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * jwt 生成与解析
 *
 * @author AixMing
 * @since 2025-05-23 15:34:02
 */
@Slf4j
public class JwtUtils {

    private static final String secret = "askjdklashjflsajdfka";

    public static String createToken(Map<String, Object> claims, long expireTime) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, secret).addClaims(claims).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expireTime)).compact();
    }

    public static Map<String, Object> checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        if (token.contains("Bearer")) {
            token = token.replace("Bearer ", "");
        }
        try {
            JwtParser parser = Jwts.parser();
            Jwt parse = parser.setSigningKey(secret).parse(token);
            return (Map<String, Object>) parse.getBody();
        } catch (Exception e) {
            log.info("check token failed, token: {}", token);
        }
        return null;
    }

}
