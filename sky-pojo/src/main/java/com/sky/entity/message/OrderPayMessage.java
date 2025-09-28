package com.sky.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单支付消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 支付金额
     */
    private BigDecimal amount;
    
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    
    /**
     * 支付方式
     */
    private Integer payMethod;
}


