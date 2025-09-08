package com.sky.task;

import com.sky.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CouponTask {

    @Autowired
    private CouponService couponService;

    /**
     * 每分钟检查一次过期优惠券
     */
    @Scheduled(cron = "0 * * * * ?")
    public void updateExpiredCoupons() {
        log.info("开始更新过期优惠券状态");
        try {
            couponService.updateExpiredCoupons();
            log.info("更新过期优惠券状态完成");
        } catch (Exception e) {
            log.error("更新过期优惠券状态失败", e);
        }
    }
}
