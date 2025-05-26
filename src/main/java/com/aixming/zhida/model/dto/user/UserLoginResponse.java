package com.aixming.zhida.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录响应
 *
 * @author AixMing
 * @since 2025-05-23 19:56:55
 */
@Data
public class UserLoginResponse implements Serializable {

    private static final long serialVersionUID = 1663130872822168503L;

    private String accessToken;

    private String refreshToken;

}
