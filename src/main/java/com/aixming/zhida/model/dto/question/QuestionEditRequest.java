package com.aixming.zhida.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑应用题目请求
 *
 * @author AixMing
 */
@Data
public class QuestionEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目内容（json格式）
     */
    private List<QuestionContentDTO> questionContent;

    /**
     * 应用 id
     */
    private Long appId;

    private static final long serialVersionUID = 1L;
}