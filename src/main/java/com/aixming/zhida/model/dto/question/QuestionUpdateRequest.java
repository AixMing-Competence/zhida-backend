package com.aixming.zhida.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新应用题目请求
 *
 * @author AixMing
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目内容（json格式）
     */
    private List<QuestionContentDTO> questionContent;

    private static final long serialVersionUID = 1L;
}