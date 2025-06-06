package com.aixming.zhida.model.dto.alipay;

import lombok.Data;

/**
 * @author AixMing
 * @since 2025-06-06 21:06:47
 */
@Data
public class AliPay {

    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;

}
