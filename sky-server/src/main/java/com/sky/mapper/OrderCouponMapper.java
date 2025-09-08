package com.sky.mapper;

import com.sky.entity.OrderCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderCouponMapper {

    /**
     * 批量插入订单优惠券关联
     */
    void insertBatch(@Param("orderCoupons") List<OrderCoupon> orderCoupons);

    /**
     * 根据订单ID查询优惠券关联
     */
    List<OrderCoupon> getByOrderId(Long orderId);

    /**
     * 根据优惠券ID查询订单关联
     */
    OrderCoupon getByCouponId(Long couponId);
}
