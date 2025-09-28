package com.sky.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积分获得消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsEarnMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 获得积分
     */
    private Integer points;
    
    /**
     * 获得时间
     */
    private LocalDateTime earnTime;
}


