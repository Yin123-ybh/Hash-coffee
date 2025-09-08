package com.sky.service.impl;

import com.sky.service.CouponStatisticsService;
import com.sky.service.CouponTemplateService;
import com.sky.service.SeckillActivityService;
import com.sky.vo.CouponStatisticsVO;
import com.sky.vo.SeckillStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CouponStatisticsServiceImpl implements CouponStatisticsService {

    @Autowired
    private CouponTemplateService couponTemplateService;

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Override
    public CouponStatisticsVO getCouponStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return couponTemplateService.getStatistics(startTime, endTime);
    }

    @Override
    public List<SeckillStatisticsVO> getSeckillStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return seckillActivityService.getStatistics(startTime, endTime);
    }
}
