package com.sky.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 秒杀活动状态变更消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillStatusMessage implements Serializable {
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
     * 状态变更类型：1-启用，2-禁用，3-结束
     */
    private Integer statusType;
    
    /**
     * 新状态：0-禁用，1-启用，2-已结束
     */
    private Integer newStatus;
    
    /**
     * 变更时间
     */
    private LocalDateTime changeTime;
    
    /**
     * 操作人ID
     */
    private Long operatorId;
    
    /**
     * 操作人姓名
     */
    private String operatorName;
    
    /**
     * 变更原因
     */
    private String reason;
}
