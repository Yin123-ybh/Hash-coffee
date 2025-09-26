package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.sky.dto.CouponBatchIssueDTO;
import com.sky.dto.CouponPageQueryDTO;
import com.sky.entity.Coupon;
import com.sky.entity.CouponTemplate;
import com.sky.mapper.CouponMapper;
import com.sky.mapper.CouponTemplateMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<Coupon> pageQuery(CouponPageQueryDTO couponPageQueryDTO) {
        return couponMapper.pageQuery(couponPageQueryDTO);
    }

    @Override
    @Transactional
    public void batchIssue(CouponBatchIssueDTO couponBatchIssueDTO) {
        // 添加方法开始日志
        log.info("=== 开始批量发放优惠券 ===");
        log.info("请求参数: templateId={}, userIds={}, count={}",
                couponBatchIssueDTO.getTemplateId(),
                couponBatchIssueDTO.getUserIds(),
                couponBatchIssueDTO.getCount());

        CouponTemplate template = couponTemplateMapper.getById(couponBatchIssueDTO.getTemplateId());
        if (template == null) {
            log.error("优惠券模板不存在: templateId={}", couponBatchIssueDTO.getTemplateId());
            throw new RuntimeException("优惠券模板不存在");
        }
        log.info("找到优惠券模板: id={}, name={}", template.getId(), template.getName());

        List<Coupon> coupons = new ArrayList<>();
        List<Long> userIds = couponBatchIssueDTO.getUserIds();

        // 如果指定了用户ID列表，则只给指定用户发放
        if (userIds != null && !userIds.isEmpty()) {
            log.info("指定用户发放模式，用户数量: {}", userIds.size());
            for (Long userId : userIds) {
                // 检查用户是否已领取过该模板的优惠券
                if (hasReceived(userId, template.getId())) {
                    log.info("用户 {} 已领取过该模板优惠券，跳过", userId);
                    continue;
                }

                log.info("为用户 {} 生成 {} 张优惠券", userId, couponBatchIssueDTO.getCount());
                for (int i = 0; i < couponBatchIssueDTO.getCount(); i++) {
                    String code = generateCouponCode();
                    Coupon coupon = Coupon.builder()
                            .templateId(template.getId())
                            .userId(userId)
                            .code(code)
                            .status(0)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build();
                    coupons.add(coupon);
                    log.debug("生成优惠券: userId={}, code={}", userId, code);
                }
            }
        } else {
        // 所有用户发放模式
        log.info("所有用户发放模式，将给所有存在的用户发放");

        // 查询所有存在的用户ID
        List<Long> allUserIds = userMapper.getAllUserIds();
        if (allUserIds.isEmpty()) {
            log.warn("没有找到任何用户");
            return;
        }

        log.info("找到 {} 个用户，将给每个用户发放 {} 张优惠券", allUserIds.size(), couponBatchIssueDTO.getCount());

        for (Long userId : allUserIds) {
            // 检查用户是否已领取过该模板的优惠券
            if (hasReceived(userId, template.getId())) {
                log.info("用户 {} 已领取过该模板优惠券，跳过", userId);
                continue;
            }

            log.info("为用户 {} 生成 {} 张优惠券", userId, couponBatchIssueDTO.getCount());
            for (int i = 0; i < couponBatchIssueDTO.getCount(); i++) {
                String code = generateCouponCode();
                Coupon coupon = Coupon.builder()
                        .templateId(template.getId())
                        .userId(userId)
                        .code(code)
                        .status(0)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                coupons.add(coupon);
                log.debug("生成优惠券: userId={}, code={}", userId, code);
            }
        }
    }

        log.info("总共生成 {} 条优惠券记录", coupons.size());

        if (!coupons.isEmpty()) {
            try {
                log.info("开始执行批量插入操作...");
                int insertedCount = couponMapper.insertBatch(coupons);
                log.info("批量插入完成，实际插入 {} 条记录", insertedCount);

                // 验证插入结果
                if (insertedCount != coupons.size()) {
                    log.error("插入记录数不匹配！预期: {}, 实际: {}", coupons.size(), insertedCount);
                    throw new RuntimeException("优惠券发放失败，预期插入 " + coupons.size() + " 条，实际插入 " + insertedCount + " 条");
                } else {
                    log.info("优惠券发放成功！");
                }
            } catch (Exception e) {
                log.error("批量插入优惠券时发生异常", e);
                throw e;
            }
        } else {
            log.warn("没有生成任何优惠券记录");
        }

        log.info("=== 批量发放优惠券完成 ===");
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
    
    /**
     * 领取优惠券
     * @param templateId 优惠券模板ID
     * @param userId 用户ID
     * @return 领取结果
     */
    @Override
    public String claimCoupon(Long templateId, Long userId) {
        // 检查用户是否已领取该模板的优惠券
        if (hasReceived(userId, templateId)) {
            return "您已经领取过该优惠券了";
        }
        
        // 检查优惠券模板是否存在且可用
        CouponTemplate template = couponTemplateMapper.getById(templateId);
        if (template == null || template.getStatus() != 1) {
            return "优惠券模板不存在或已禁用";
        }
        
        // 检查是否在有效期内
        if (template.getEndTime().isBefore(LocalDateTime.now())) {
            return "优惠券已过期";
        }
        
        // 创建优惠券
        Coupon coupon = Coupon.builder()
                .templateId(templateId)
                .userId(userId)
                .status(0) // 0-未使用
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        
        couponMapper.insert(coupon);
        return "优惠券领取成功";
    }
    
    /**
     * 获取用户可用优惠券
     * @param userId 用户ID
     * @return 用户可用优惠券列表
     */
    @Override
    public List<Coupon> getUserAvailableCoupons(Long userId) {
        return couponMapper.getByUserId(userId, 0); // 0-未使用
    }
    
    /**
     * 检查优惠券是否可用
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @param orderAmount 订单金额
     * @return 检查结果
     */
    @Override
    public String checkCouponAvailable(Long couponId, Long userId, BigDecimal orderAmount) {
        // 这里可以添加优惠券可用性检查逻辑
        return "优惠券可用";
    }
}
