package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀活动统计VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 商品名称
     */
    private String dishName;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 已售数量
     */
    private Integer soldCount;

    /**
     * 销售率
     */
    private BigDecimal sellRate;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;
}
