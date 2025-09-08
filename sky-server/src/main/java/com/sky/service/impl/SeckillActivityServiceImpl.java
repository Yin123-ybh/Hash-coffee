package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.sky.dto.SeckillActivityDTO;
import com.sky.dto.SeckillActivityPageQueryDTO;
import com.sky.entity.SeckillActivity;
import com.sky.mapper.SeckillActivityMapper;
import com.sky.service.SeckillActivityService;
import com.sky.vo.SeckillStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class SeckillActivityServiceImpl implements SeckillActivityService {

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Override
    public Page<SeckillActivity> pageQuery(SeckillActivityPageQueryDTO seckillActivityPageQueryDTO) {
        return seckillActivityMapper.pageQuery(seckillActivityPageQueryDTO);
    }

    @Override
    public SeckillActivity getById(Long id) {
        return seckillActivityMapper.getById(id);
    }

    @Override
    public void save(SeckillActivityDTO seckillActivityDTO) {
        SeckillActivity seckillActivity = new SeckillActivity();
        BeanUtils.copyProperties(seckillActivityDTO, seckillActivity);
        seckillActivity.setSoldCount(0);
        seckillActivityMapper.insert(seckillActivity);
    }

    @Override
    public void update(SeckillActivityDTO seckillActivityDTO) {
        SeckillActivity seckillActivity = new SeckillActivity();
        BeanUtils.copyProperties(seckillActivityDTO, seckillActivity);
        seckillActivityMapper.update(seckillActivity);
    }

    @Override
    public void deleteById(Long id) {
        seckillActivityMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        seckillActivityMapper.updateStatus(id, status);
    }

    @Override
    public List<SeckillStatisticsVO> getStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return seckillActivityMapper.getStatistics(startTime, endTime);
    }

    @Override
    public List<SeckillActivity> getCurrentActivities() {
        return seckillActivityMapper.getCurrentActivities(LocalDateTime.now());
    }

    @Override
    public boolean reduceStock(Long id, Integer quantity) {
        return seckillActivityMapper.reduceStock(id, quantity) > 0;
    }

    @Override
    public void increaseSoldCount(Long id, Integer quantity) {
        seckillActivityMapper.increaseSoldCount(id, quantity);
    }
}
