package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillParticipant {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 秒杀活动ID
     */
    private Long seckillId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 参与数量
     */
    private Integer quantity;

    /**
     * 状态 0 待处理 1-成功 2-失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;



}
