package com.aixming.zhida.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建应用题目请求
 *
 * @author AixMing
 */
@Data
public class QuestionAddRequest implements Serializable {
    
    /**
     * 应用 id
     */
    private Long appId;
    
    /**
     * 题目内容（json格式）
     */
    private List<QuestionContentDTO> questionContent;
    
    private static final long serialVersionUID = 1L;
}