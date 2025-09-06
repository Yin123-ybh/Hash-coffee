package com.sky.service;

import com.sky.vo.CouponStatisticsVO;
import com.sky.vo.SeckillStatisticsVO;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponStatisticsService {

    /**
     * 获取优惠券统计信息
     */
    CouponStatisticsVO getCouponStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取秒杀活动统计信息
     */
    List<SeckillStatisticsVO> getSeckillStatistics(LocalDateTime startTime, LocalDateTime endTime);
}
