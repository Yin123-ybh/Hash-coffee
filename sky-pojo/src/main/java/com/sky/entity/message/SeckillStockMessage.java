package com.sky.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 秒杀活动库存变更消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillStockMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 秒杀活动ID
     */
    private Long activityId;
    
    /**
     * 活动名称
     */
    private String activityName;
    
    /**
     * 变更类型：1-扣减库存，2-增加库存，3-库存不足，4-活动结束
     */
    private Integer changeType;
    
    /**
     * 变更数量
     */
    private Integer changeQuantity;
    
    /**
     * 变更前库存
     */
    private Integer beforeStock;
    
    /**
     * 变更后库存
     */
    private Integer afterStock;
    
    /**
     * 变更时间
     */
    private LocalDateTime changeTime;
    
    /**
     * 用户ID（如果是用户参与导致的库存变更）
     */
    private Long userId;
    
    /**
     * 订单ID（如果是订单导致的库存变更）
     */
    private Long orderId;
    
    /**
     * 变更原因
     */
    private String reason;
}
