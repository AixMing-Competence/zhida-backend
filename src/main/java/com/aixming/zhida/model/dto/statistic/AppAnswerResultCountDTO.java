package com.aixming.zhida.model.dto.statistic;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Duzeming
 * @since 2024-08-10 17:39:23
 */
@Data
public class AppAnswerResultCountDTO implements Serializable {

    /**
     * 结果名称
     */
    private String resultName;

    /**
     * 结果数
     */
    private Long resultCount;
    
    private static final long serialVersionUID = 1L;
}
