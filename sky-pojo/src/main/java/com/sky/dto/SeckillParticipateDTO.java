package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 秒杀参与DTO
 */
@Data
public class SeckillParticipateDTO implements Serializable {
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
     * 参与数量
     */
    private Integer quantity;
}
