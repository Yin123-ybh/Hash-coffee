package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单优惠券关联表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 订单ID（关联现有orders表）
     */
    private Long orderId;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
