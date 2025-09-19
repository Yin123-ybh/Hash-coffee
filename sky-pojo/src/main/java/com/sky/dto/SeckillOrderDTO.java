package com.sky.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 秒杀订单DTO
 */
@Data
public class SeckillOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long dishId;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;
}
