package com.aixming.zhida.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * vip 订单状态枚举
 *
 * @author AixMing
 * @since 2025-05-31 21:44:55
 */
@Getter
public enum VipOrderStatusEnum {
    /**
     * 订单状态（0：待支付，1：已支付，2：已完成，3：已取消，4：退款中，5：已退款，6：已关闭）
     */
    PAYING("待支付", 0),
    PAYED("已支付", 1),
    COMPLETED("已完成", 2),
    CANCEL("已取消", 3),
    REFUNDING("退款中", 4),
    REFUNDED("已退款", 5),
    CLOSED("已关闭", 6);

    private final String text;

    private final Integer value;

    VipOrderStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有值
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(VipOrderStatusEnum::getValue).collect(Collectors.toList());
    }

    /**
     * 根据值获取枚举
     *
     * @param value
     * @return
     */
    public static VipOrderStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (VipOrderStatusEnum anEnum : values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
