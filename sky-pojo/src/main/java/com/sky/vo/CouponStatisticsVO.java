package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 优惠券统计VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券总数
     */
    private Long totalCoupons;

    /**
     * 已使用数量
     */
    private Long usedCoupons;

    /**
     * 已过期数量
     */
    private Long expiredCoupons;

    /**
     * 优惠金额总计
     */
    private BigDecimal totalDiscount;
}
