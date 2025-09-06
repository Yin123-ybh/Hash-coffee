package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 优惠券模板分页查询DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplatePageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private int page;

    /**
     * 每页记录数
     */
    private int size;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券类型：1-满减券，2-折扣券，3-代金券
     */
    private Integer type;

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;
}
