package com.aixming.zhida.constant;

/**
 * redis key 常量
 *
 * @author AixMing
 * @since 2025-05-23 15:40:10
 */
public class RedisKey {

    public static final String BASE_KEY = "zhida:";

    public static final String TOKEN_KEY = "access:token:%s";

    public static String getKey(String bizKey, Object... params) {
        return BASE_KEY + String.format(bizKey, params);
    }

}
