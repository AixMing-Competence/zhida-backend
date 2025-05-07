package com.aixming.zhida.model.dto.codequestionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑题目提交请求
 *
 * @author AixMing
 */
@Data
public class CodeQuestionSubmitEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    private static final long serialVersionUID = 1L;

}
