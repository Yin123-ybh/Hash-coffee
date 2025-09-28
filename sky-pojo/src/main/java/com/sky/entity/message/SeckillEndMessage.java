package com.sky.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀活动结束消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillEndMessage implements Serializable {
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
     * 结束类型：1-正常结束，2-提前结束，3-库存售罄结束
     */
    private Integer endType;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 总库存
     */
    private Integer totalStock;
    
    /**
     * 已售数量
     */
    private Integer soldCount;
    
    /**
     * 剩余库存
     */
    private Integer remainingStock;
    
    /**
     * 参与用户数
     */
    private Integer participantCount;
    
    /**
     * 总销售额
     */
    private BigDecimal totalSales;
    
    /**
     * 平均参与时间（秒）
     */
    private Double avgParticipateTime;
    
    /**
     * 结束原因
     */
    private String endReason;
    
    /**
     * 操作人ID
     */
    private Long operatorId;
}
