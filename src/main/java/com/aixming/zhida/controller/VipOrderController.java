package com.aixming.zhida.controller;

import com.aixming.zhida.common.BaseResponse;
import com.aixming.zhida.common.ResultUtils;
import com.aixming.zhida.constant.TokenConstant;
import com.aixming.zhida.model.entity.VipOrder;
import com.aixming.zhida.model.enums.VipOrderStatusEnum;
import com.aixming.zhida.service.VipOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * vip 订单接口
 *
 * @author AixMing
 * @since 2025-05-31 21:25:43
 */
@RestController
@RequestMapping("/vip_order")
public class VipOrderController {

    @Resource
    private VipOrderService vipOrderService;

    @PostMapping("/add")
    public BaseResponse<Long> addVipOrder(HttpServletRequest request) {
        Map<String, Object> claims = (Map<String, Object>) request.getAttribute("loginUser");
        long userId = ((Number) claims.get(TokenConstant.UID)).longValue();
        VipOrder vipOrder = new VipOrder();
        vipOrder.setUserId(userId);
        vipOrder.setStatus(VipOrderStatusEnum.PAYING.getValue());
        vipOrderService.save(vipOrder);
        return ResultUtils.success(vipOrder.getId());
    }

}
