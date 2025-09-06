package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 优惠券批量发放DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponBatchIssueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 发放数量
     */
    private Integer count;

    /**
     * 用户ID列表（指定用户发放时使用）
     */
    private List<Long> userIds;
}
