package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.CouponTemplateDTO;
import com.sky.dto.CouponTemplatePageQueryDTO;
import com.sky.entity.CouponTemplate;
import com.sky.vo.CouponStatisticsVO;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponTemplateService {

    /**
     * 分页查询优惠券模板
     */
    Page<CouponTemplate> pageQuery(CouponTemplatePageQueryDTO couponTemplatePageQueryDTO);

    /**
     * 根据id查询优惠券模板
     */
    CouponTemplate getById(Long id);

    /**
     * 新增优惠券模板
     */
    void save(CouponTemplateDTO couponTemplateDTO);

    /**
     * 修改优惠券模板
     */
    void update(CouponTemplateDTO couponTemplateDTO);

    /**
     * 根据id删除优惠券模板
     */
    void deleteById(Long id);

    /**
     * 更新优惠券模板状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 获取优惠券统计信息
     */
    CouponStatisticsVO getStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取所有启用的优惠券模板
     */
    List<CouponTemplate> listEnabled();
    
    /**
     * 获取可领取的优惠券模板
     * @return 可领取的优惠券模板列表
     */
    List<CouponTemplate> getAvailableTemplates();
}
