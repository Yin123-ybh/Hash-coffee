package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀活动列表VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillActivityListVO {
    
    /**
     * 活动ID
     */
    private Long id;
    
    /**
     * 活动名称
     */
    private String name;
    
    /**
     * 菜品ID
     */
    private Long dishId;
    
    /**
     * 菜品名称
     */
    private String dishName;
    
    /**
     * 菜品图片
     */
    private String dishImage;
    
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    
    /**
     * 库存
     */
    private Integer stock;
    
    /**
     * 已售数量
     */
    private Integer soldCount;
    
    /**
     * 每人限购数量
     */
    private Integer perUserLimit;
    
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
    
    /**
     * 剩余时间（秒）
     */
    private Long remainingTime;
    
    /**
     * 是否进行中
     */
    private Boolean isActive;
    
    /**
     * 折扣率
     */
    private BigDecimal discountRate;
}


