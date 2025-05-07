package com.aixming.zhida.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行代码请求
 * @author AixMing
 * @since 2025-03-19 19:34:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeRequest {
    private List<String> inputList;
    private String code;
    private String language;
}
