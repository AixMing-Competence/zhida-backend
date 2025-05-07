package com.aixming.zhida.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Duzeming
 * @since 2024-07-23 18:17:58
 */
@Data
public class AppReviewRequest implements Serializable {

    /**
     * 审核应用 id
     */
    private Long id;

    /**
     * 审核状态
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    private static final long serialVersionUID = 1L;
}
