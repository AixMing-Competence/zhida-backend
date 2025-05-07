package com.aixming.zhida.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目提交编程语言枚举
 *
 * @author AixMing
 * @since 2025-03-17 17:16:59
 */
public enum CodeQuestionSubmitLanguageEnum {

    /**
     * 编程语言
     */
    JAVA("java", "java"),
    CPLUSPLUS("cpp", "cpp"),
    GOLANG("go", "go");

    private final String text;
    private final String value;

    CodeQuestionSubmitLanguageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static CodeQuestionSubmitLanguageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (CodeQuestionSubmitLanguageEnum anEnum : values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
