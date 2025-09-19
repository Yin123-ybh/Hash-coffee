package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存扣减记录表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillStockLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 秒杀活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 扣减数量
     */
    private Integer quantity;

    /**
     * 扣减前库存
     */
    private Integer beforeStock;

    /**
     * 扣减后库存
     */
    private Integer afterStock;

    /**
     * 状态：1-成功，0-失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

