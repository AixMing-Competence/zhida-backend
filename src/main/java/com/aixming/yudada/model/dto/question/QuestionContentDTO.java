package com.aixming.yudada.model.dto.question;

import lombok.Data;

import java.util.List;

/**
 * @author Duzeming
 * @since 2024-07-22 14:23:45
 */
@Data
public class QuestionContentDTO {

    /**
     * 题目标题
     */
    private String title;

    /**
     * 选项列表
     */
    private List<Option> options;

    /**
     * 题目选项
     */
    @Data
    public static class Option{
        private String key;
        private String value;
        private Integer score;
        private String result;
    }
    
    private static final long serialVersionUID = 1L;
}
