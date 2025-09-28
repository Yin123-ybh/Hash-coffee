package com.sky.controller.user;

import com.sky.entity.Coupon;
import com.sky.entity.CouponTemplate;
import com.sky.result.Result;
import com.sky.service.CouponService;
import com.sky.service.CouponTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户端优惠券控制器
 */
@RestController
@RequestMapping("/user/coupon")
@Api(tags = "用户端优惠券")
@Slf4j
public class UserCouponController {

    @Autowired
    private CouponTemplateService couponTemplateService;
    
    @Autowired
    private CouponService couponService;

    @GetMapping("/templates/available")
    @ApiOperation("获取可领取的优惠券模板")
    public Result<List<CouponTemplate>> getAvailableTemplates() {
        log.info("获取可领取的优惠券模板");
        List<CouponTemplate> templates = couponTemplateService.getAvailableTemplates();
        return Result.success(templates);
    }

    @PostMapping("/claim")
    @ApiOperation("领取优惠券")
    public Result<String> claimCoupon(@RequestParam Long templateId,
                                    @RequestHeader("userId") Long userId) {
        log.info("用户领取优惠券：templateId={}, userId={}", templateId, userId);
        String result = couponService.claimCoupon(templateId, userId);
        return Result.success(result);
    }

    @GetMapping("/my")
    @ApiOperation("获取我的优惠券")
    public Result<List<Coupon>> getMyCoupons(@RequestHeader("userId") Long userId) {
        log.info("获取用户优惠券：userId={}", userId);
        List<Coupon> coupons = couponService.getUserAvailableCoupons(userId);
        return Result.success(coupons);
    }

    @PostMapping("/check")
    @ApiOperation("检查优惠券是否可用")
    public Result<String> checkCouponAvailable(@RequestParam Long couponId,
                                             @RequestParam BigDecimal orderAmount,
                                             @RequestHeader("userId") Long userId) {
        log.info("检查优惠券是否可用：couponId={}, orderAmount={}, userId={}", couponId, orderAmount, userId);
        String result = couponService.checkCouponAvailable(couponId, userId, orderAmount);
        return Result.success(result);
    }
}