package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.CouponBatchIssueDTO;
import com.sky.dto.CouponPageQueryDTO;
import com.sky.entity.Coupon;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/coupon")
@Api(tags = "优惠券管理")
@Slf4j
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/page")
    @ApiOperation("分页查询优惠券")
    public Result<PageResult> page(CouponPageQueryDTO couponPageQueryDTO) {
        log.info("分页查询优惠券：{}", couponPageQueryDTO);
        Page<Coupon> page = couponService.pageQuery(couponPageQueryDTO);
        return Result.success(new PageResult(page.getTotal(), page.getResult()));
    }

    @PostMapping("/batch-issue")
    @ApiOperation("批量发放优惠券")
    public Result<Void> batchIssue(@RequestBody CouponBatchIssueDTO couponBatchIssueDTO) {
        log.info("批量发放优惠券：{}", couponBatchIssueDTO);
        couponService.batchIssue(couponBatchIssueDTO);
        return Result.success();
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("根据用户ID查询优惠券")
    public Result<List<Coupon>> getByUserId(@PathVariable Long userId, 
                                           @RequestParam(required = false) Integer status) {
        log.info("根据用户ID查询优惠券：userId={}, status={}", userId, status);
        List<Coupon> coupons = couponService.getByUserId(userId, status);
        return Result.success(coupons);
    }

    @PutMapping("/use/{couponId}")
    @ApiOperation("使用优惠券")
    public Result<Void> useCoupon(@PathVariable Long couponId, @RequestParam Long orderId) {
        log.info("使用优惠券：couponId={}, orderId={}", couponId, orderId);
        couponService.useCoupon(couponId, orderId);
        return Result.success();
    }

    @GetMapping("/check/{userId}/{templateId}")
    @ApiOperation("检查用户是否已领取该模板的优惠券")
    public Result<Boolean> hasReceived(@PathVariable Long userId, @PathVariable Long templateId) {
        log.info("检查用户是否已领取该模板的优惠券：userId={}, templateId={}", userId, templateId);
        boolean hasReceived = couponService.hasReceived(userId, templateId);
        return Result.success(hasReceived);
    }

    @PutMapping("/update-expired")
    @ApiOperation("更新过期优惠券状态")
    public Result<Void> updateExpiredCoupons() {
        log.info("更新过期优惠券状态");
        couponService.updateExpiredCoupons();
        return Result.success();
    }
}
