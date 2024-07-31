package com.aixming.yudada.model.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评分策略枚举
 *
 * @author Duzeming
 * @since 2024-07-22 15:47:12
 */
@Getter
public enum ScoringStrategyEnum {
    custom("自定义", 0),
    AI("AI", 1);

    private final String text;
    private final int value;

    ScoringStrategyEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static ScoringStrategyEnum getEnumByValue(Integer value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (ScoringStrategyEnum anEnum : values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }
}
