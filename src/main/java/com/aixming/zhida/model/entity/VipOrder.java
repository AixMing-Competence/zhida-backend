package com.aixming.zhida.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * vip 订单
 *
 * @TableName vip_order
 */
@TableName(value = "vip_order")
@Data
public class VipOrder {
    /**
     * 订单号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 下单用户 id
     */
    private Long userId;

    /**
     * 订单状态（0：待支付，1：已支付，2：已完成，3：已取消，4：退款中，5：已退款，6：已关闭）
     */
    private Integer status;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}