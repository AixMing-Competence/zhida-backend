package com.aixming.zhida.model.dto.appthumb;

import lombok.Data;

import java.io.Serializable;

/**
 * @author AixMing
 * @since 2025-05-27 21:40:35
 */
@Data
public class AppThumbAddRequest implements Serializable {

    private static final long serialVersionUID = -3000768087157690446L;

    private Long appId;

}
