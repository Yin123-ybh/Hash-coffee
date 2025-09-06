package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CouponPageQueryDTO implements Serializable {

    //页码
    private int page;

    //每页记录数
    private int pageSize;

    //优惠券码
    private String code;

    //用户ID
    private Long userId;

    //模板ID
    private Long templateId;

    //状态：0-未使用，1-已使用，2-已过期
    private Integer status;

    //开始时间
    private LocalDateTime startTime;

    //结束时间
    private LocalDateTime endTime;

}
