package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.sky.dto.CouponBatchIssueDTO;
import com.sky.dto.CouponPageQueryDTO;
import com.sky.entity.Coupon;
import com.sky.entity.CouponTemplate;
import com.sky.mapper.CouponMapper;
import com.sky.mapper.CouponTemplateMapper;
import com.sky.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponTemplateMapper couponTemplateMapper;

    @Override
    public Page<Coupon> pageQuery(CouponPageQueryDTO couponPageQueryDTO) {
        return couponMapper.pageQuery(couponPageQueryDTO);
    }

    @Override
    @Transactional
    public void batchIssue(CouponBatchIssueDTO couponBatchIssueDTO) {
        CouponTemplate template = couponTemplateMapper.getById(couponBatchIssueDTO.getTemplateId());
        if (template == null) {
            throw new RuntimeException("优惠券模板不存在");
        }

        List<Coupon> coupons = new ArrayList<>();
        List<Long> userIds = couponBatchIssueDTO.getUserIds();

        // 如果指定了用户ID列表，则只给指定用户发放
        if (userIds != null && !userIds.isEmpty()) {
            for (Long userId : userIds) {
                // 检查用户是否已领取过该模板的优惠券
                if (hasReceived(userId, template.getId())) {
                    continue;
                }
                
                for (int i = 0; i < couponBatchIssueDTO.getCount(); i++) {
                    Coupon coupon = Coupon.builder()
                            .templateId(template.getId())
                            .userId(userId)
                            .code(generateCouponCode())
                            .status(0)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build();
                    coupons.add(coupon);
                }
            }
        } else {
            // 给所有用户发放（这里简化处理，实际应该查询所有用户）
            // 为了演示，这里只给前100个用户发放
            for (int i = 1; i <= 100; i++) {
                for (int j = 0; j < couponBatchIssueDTO.getCount(); j++) {
                    Coupon coupon = Coupon.builder()
                            .templateId(template.getId())
                            .userId((long) i)
                            .code(generateCouponCode())
                            .status(0)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build();
                    coupons.add(coupon);
                }
            }
        }

        if (!coupons.isEmpty()) {
            couponMapper.insertBatch(coupons);
        }
    }

    @Override
    public List<Coupon> getByUserId(Long userId, Integer status) {
        return couponMapper.getByUserId(userId, status);
    }

    @Override
    @Transactional
    public void useCoupon(Long couponId, Long orderId) {
        Coupon coupon = couponMapper.getById(couponId);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        if (coupon.getStatus() != 0) {
            throw new RuntimeException("优惠券已使用或已过期");
        }

        couponMapper.updateStatus(couponId, 1, orderId, LocalDateTime.now());
    }

    @Override
    public boolean hasReceived(Long userId, Long templateId) {
        return couponMapper.countByUserIdAndTemplateId(userId, templateId) > 0;
    }

    @Override
    @Transactional
    public void updateExpiredCoupons() {
        List<Coupon> expiredCoupons = couponMapper.getExpiredCoupons(LocalDateTime.now());
        if (!expiredCoupons.isEmpty()) {
            List<Long> ids = expiredCoupons.stream().map(Coupon::getId).collect(java.util.stream.Collectors.toList());
            couponMapper.updateExpiredCoupons(ids, LocalDateTime.now());
        }
    }

    /**
     * 生成优惠券码
     */
    private String generateCouponCode() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
