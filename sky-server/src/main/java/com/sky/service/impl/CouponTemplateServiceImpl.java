package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.sky.dto.CouponTemplateDTO;
import com.sky.dto.CouponTemplatePageQueryDTO;
import com.sky.entity.CouponTemplate;
import com.sky.mapper.CouponTemplateMapper;
import com.sky.service.CouponTemplateService;
import com.sky.vo.CouponStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CouponTemplateServiceImpl implements CouponTemplateService {

    @Autowired
    private CouponTemplateMapper couponTemplateMapper;

    @Override
    public Page<CouponTemplate> pageQuery(CouponTemplatePageQueryDTO couponTemplatePageQueryDTO) {
        return couponTemplateMapper.pageQuery(couponTemplatePageQueryDTO);
    }

    @Override
    public CouponTemplate getById(Long id) {
        return couponTemplateMapper.getById(id);
    }

    @Override
    public void save(CouponTemplateDTO couponTemplateDTO) {
        CouponTemplate couponTemplate = new CouponTemplate();
        BeanUtils.copyProperties(couponTemplateDTO, couponTemplate);
        couponTemplate.setUsedCount(0);
        couponTemplateMapper.insert(couponTemplate);
    }

    @Override
    public void update(CouponTemplateDTO couponTemplateDTO) {
        CouponTemplate couponTemplate = new CouponTemplate();
        BeanUtils.copyProperties(couponTemplateDTO, couponTemplate);
        couponTemplate.setUpdateTime(LocalDateTime.now());
        couponTemplateMapper.update(couponTemplate);
    }

    @Override
    public void deleteById(Long id) {
        couponTemplateMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        couponTemplateMapper.updateStatus(id, status);
    }

    @Override
    public CouponStatisticsVO getStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return couponTemplateMapper.getStatistics(startTime, endTime);
    }

    @Override
    public List<CouponTemplate> listEnabled() {
        return couponTemplateMapper.listEnabled();
    }
}
