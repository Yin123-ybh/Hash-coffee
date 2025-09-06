package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.CouponBatchIssueDTO;
import com.sky.dto.CouponPageQueryDTO;
import com.sky.entity.Coupon;

import java.util.List;

public interface CouponService {

    /**
     * 分页查询优惠券
     */
    Page<Coupon> pageQuery(CouponPageQueryDTO couponPageQueryDTO);

    /**
     * 批量发放优惠券
     */
    void batchIssue(CouponBatchIssueDTO couponBatchIssueDTO);

    /**
     * 根据用户ID查询优惠券
     */
    List<Coupon> getByUserId(Long userId, Integer status);

    /**
     * 使用优惠券
     */
    void useCoupon(Long couponId, Long orderId);

    /**
     * 检查用户是否已领取该模板的优惠券
     */
    boolean hasReceived(Long userId, Long templateId);

    /**
     * 更新过期优惠券状态
     */
    void updateExpiredCoupons();
}
