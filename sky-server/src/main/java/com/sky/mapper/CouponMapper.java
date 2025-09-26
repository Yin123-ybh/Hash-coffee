package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CouponPageQueryDTO;
import com.sky.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CouponMapper {

    /**
     * 分页查询优惠券
     */
    Page<Coupon> pageQuery(CouponPageQueryDTO couponPageQueryDTO);

    /**
     * 插入优惠券
     */
    void insert(Coupon coupon);
    
    /**
     * 批量插入优惠券
     */
    int insertBatch(@Param("coupons") List<Coupon> coupons);

    /**
     * 根据用户ID查询优惠券
     */
    List<Coupon> getByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 根据ID查询优惠券
     */
    Coupon getById(Long id);

    /**
     * 更新优惠券状态
     */
    void updateStatus(@Param("id") Long id, 
                     @Param("status") Integer status, 
                     @Param("orderId") Long orderId, 
                     @Param("usedTime") LocalDateTime usedTime);

    /**
     * 检查用户是否已领取该模板的优惠券
     */
    int countByUserIdAndTemplateId(@Param("userId") Long userId, @Param("templateId") Long templateId);

    /**
     * 获取过期的优惠券
     */
    List<Coupon> getExpiredCoupons(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 批量更新过期优惠券状态
     */
    void updateExpiredCoupons(@Param("ids") List<Long> ids, @Param("currentTime") LocalDateTime currentTime);
}
