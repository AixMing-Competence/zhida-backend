package com.aixming.zhida.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行代码响应
 * @author AixMing
 * @since 2025-03-19 19:34:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeResponse {
    
    private List<String> outputList;
    
    /**
     * 接口信息
     */
    private String message;
    
    /**
     * 执行状态
     */
    private Integer status;
    private JudgeInfo judgeInfo;
}
