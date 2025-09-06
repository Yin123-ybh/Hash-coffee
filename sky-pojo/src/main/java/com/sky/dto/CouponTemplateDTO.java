package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券模板DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    private Long id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券类型：1-满减券，2-折扣券，3-代金券
     */
    private Integer type;

    /**
     * 折扣类型：1-固定金额，2-百分比
     */
    private Integer discountType;

    /**
     * 折扣值
     */
    private BigDecimal discountValue;

    /**
     * 最低消费金额
     */
    private BigDecimal minAmount;

    /**
     * 最大折扣金额
     */
    private BigDecimal maxDiscount;

    /**
     * 发放总数量
     */
    private Integer totalCount;

    /**
     * 已使用数量
     */
    private Integer usedCount;

    /**
     * 每人限领数量
     */
    private Integer perUserLimit;

    /**
     * 有效天数
     */
    private Integer validDays;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态：1-启用，0-禁用
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

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改人
     */
    private Long updateUser;
}
