package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.CouponStatisticsService;
import com.sky.vo.CouponStatisticsVO;
import com.sky.vo.SeckillStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/coupon/template")
@Api(tags = "优惠券统计管理")
@Slf4j
public class CouponStatisticsController {

    @Autowired
    private CouponStatisticsService couponStatisticsService;

    @GetMapping("/statistics")
    @ApiOperation("获取优惠券统计信息")
    public Result<CouponStatisticsVO> getStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        log.info("获取优惠券统计信息：startTime={}, endTime={}", startTime, endTime);
        LocalDateTime start = startTime.atStartOfDay();
        LocalDateTime end = endTime.atTime(23, 59, 59);
        CouponStatisticsVO statistics = couponStatisticsService.getCouponStatistics(start, end);
        return Result.success(statistics);
    }

    @GetMapping("/usage-trend")
    @ApiOperation("获取优惠券使用趋势")
    public Result<List<Object>> getUsageTrend(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        log.info("获取优惠券使用趋势：startTime={}, endTime={}", startTime, endTime);
        LocalDateTime start = startTime.atStartOfDay();
        LocalDateTime end = endTime.atTime(23, 59, 59);
        // 这里可以返回按日期分组的优惠券使用数据
        // 为了简化，这里返回空列表
        return Result.success(new java.util.ArrayList<>());
    }
}

@RestController
@RequestMapping("/admin/seckill/statistics")
@Api(tags = "秒杀活动统计管理")
@Slf4j
class SeckillStatisticsController {

    @Autowired
    private CouponStatisticsService couponStatisticsService;

    @GetMapping
    @ApiOperation("获取秒杀活动统计信息")
    public Result<List<SeckillStatisticsVO>> getStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        log.info("获取秒杀活动统计信息：startTime={}, endTime={}", startTime, endTime);
        LocalDateTime start = startTime.atStartOfDay();
        LocalDateTime end = endTime.atTime(23, 59, 59);
        List<SeckillStatisticsVO> statistics = couponStatisticsService.getSeckillStatistics(start, end);
        return Result.success(statistics);
    }
}
