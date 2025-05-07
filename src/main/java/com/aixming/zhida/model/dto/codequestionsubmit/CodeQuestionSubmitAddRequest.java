package com.aixming.zhida.model.dto.codequestionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建题目提交请求
 *
 * @author AixMing
 */
@Data
public class CodeQuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;

}
