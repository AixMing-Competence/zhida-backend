package com.aixming.zhida.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题消息枚举
 *
 * @author AixMing
 * @since 2025-03-18 14:58:33
 */
@Getter
public enum JudgeMessageEnum {
    /**
     * 判题时会出现的各种情况
     */
    ACCEPT("成功", "Accepted"),
    WRONG_ANSWER("答案错误", "Wrong Answer"),
    COMPILE_ERROR("编译错误", "Compile Error"),
    MEMORY_LIMIT_EXCEEDED("内存溢出", "Memory Limit Exceeded"),
    TIME_LIMIT_EXCEEDED("超时", "Time Limit Exceeded"),
    PRESENTATION_ERROR("展示错误", "Presentation Error"),
    WAITING("等待中", "Waiting"),
    OUTPUT_LIMIT_EXCEEDED("输出溢出", "Output Limit Exceeded"),
    DANGEROUS_OPERATION("危险操作", "Dangerous Operation"),
    RUNTIME_ERROR("运行错误", "Runtime Error"),
    SYSTEM_ERROR("系统错误", "System Error");

    private String text;

    private String value;

    JudgeMessageEnum(String text, String message) {
        this.text = text;
        this.value = message;
    }

    /**
     * 获取所有值
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(JudgeMessageEnum::getValue).collect(Collectors.toList());
    }

    /**
     * 根据值获取枚举对象
     *
     * @param value
     * @return
     */
    public static JudgeMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeMessageEnum anEnum : values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
