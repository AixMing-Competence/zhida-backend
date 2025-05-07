package com.aixming.zhida.model.entity;

import lombok.Data;

/**
 * 题目配置
 *
 * @author AixMing
 * @since 2025-03-17 17:30:01
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制
     */
    private Long timeLimit;

    /**
     * 内存限制
     */
    private Long memoryLimit;

    /**
     * 栈内存限制
     */
    private Long stackLimit;

}
