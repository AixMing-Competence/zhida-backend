package com.aixming.zhida.model.dto.userAnswer;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建用户答题请求
 *
 * @author AixMing
 */
@Data
public class UserAnswerAddRequest implements Serializable {

    /**
     * 唯一 id
     */
    private Long id;

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 用户答案（JSON 数组）
     */
    private List<String> choices;

    private static final long serialVersionUID = 1L;
}