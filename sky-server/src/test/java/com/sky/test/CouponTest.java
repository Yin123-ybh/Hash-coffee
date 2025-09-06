package com.sky.test;

import com.sky.dto.CouponTemplateDTO;
import com.sky.service.CouponTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
public class CouponTest {

    @Autowired
    private CouponTemplateService couponTemplateService;

    @Test
    public void testCreateCouponTemplate() {
        CouponTemplateDTO dto = new CouponTemplateDTO();
        dto.setName("测试优惠券");
        dto.setType(1); // 满减券
        dto.setDiscountType(1); // 固定金额
        dto.setDiscountValue(new BigDecimal("10.00"));
        dto.setMinAmount(new BigDecimal("50.00"));
        dto.setTotalCount(100);
        dto.setPerUserLimit(1);
        dto.setValidDays(30);
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusDays(30));
        dto.setStatus(1);

        couponTemplateService.save(dto);
        System.out.println("优惠券模板创建成功");
    }
}
