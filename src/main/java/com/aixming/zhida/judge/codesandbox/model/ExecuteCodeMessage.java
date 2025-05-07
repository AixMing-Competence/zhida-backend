package com.aixming.zhida.judge.codesandbox.model;

import lombok.Data;

/**
 * 执行代码结果信息
 *
 * @author AixMing
 * @since 2025-03-26 17:27:56
 */
@Data
public class ExecuteCodeMessage {
    
    private String message;

    private String errorMessage;

    private Long time;

    private Long memory;
}
