package com.aixming.yudada.model.dto.statistic;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Duzeming
 * @since 2024-08-10 17:39:23
 */
@Data
public class AppAnswerCountDTO implements Serializable {

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 用户提交回答数
     */
    private Long answerCount;
    
    private static final long serialVersionUID = 1L;
}
