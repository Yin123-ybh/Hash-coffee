package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.CouponBatchIssueDTO;
import com.sky.dto.CouponPageQueryDTO;
import com.sky.entity.Coupon;

import java.math.BigDecimal;
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
    
    /**
     * 领取优惠券
     * @param templateId 优惠券模板ID
     * @param userId 用户ID
     * @return 领取结果
     */
    String claimCoupon(Long templateId, Long userId);
    
    /**
     * 获取用户可用优惠券
     * @param userId 用户ID
     * @return 用户可用优惠券列表
     */
    List<Coupon> getUserAvailableCoupons(Long userId);
    
    /**
     * 检查优惠券是否可用
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @param orderAmount 订单金额
     * @return 检查结果
     */
    String checkCouponAvailable(Long couponId, Long userId, BigDecimal orderAmount);
}
