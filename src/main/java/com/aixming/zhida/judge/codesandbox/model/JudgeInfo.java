package com.aixming.zhida.judge.codesandbox.model;

import lombok.Data;

/**
 * @author AixMing
 * @since 2025-03-17 17:28:16
 */
@Data
public class JudgeInfo {

    /**
     * 判题信息
     */
    private String message;

    /**
     * 执行时间
     */
    private Long time;

    /**
     * 内存
     */
    private Long memory;

}
