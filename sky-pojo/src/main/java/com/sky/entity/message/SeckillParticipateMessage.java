package com.sky.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀活动参与消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillParticipateMessage implements Serializable {
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
     * 用户ID
     */
    private Long userId;
    
    /**
     * 参与数量
     */
    private Integer quantity;
    
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    
    /**
     * 参与时间
     */
    private LocalDateTime participateTime;
    
    /**
     * 参与结果：1-成功，2-失败
     */
    private Integer result;
    
    /**
     * 失败原因
     */
    private String failReason;
    
    /**
     * 订单ID（如果参与成功）
     */
    private Long orderId;
    
    /**
     * 剩余库存
     */
    private Integer remainingStock;
    
    /**
     * 用户IP
     */
    private String userIp;
    
    /**
     * 用户设备信息
     */
    private String userAgent;
}
