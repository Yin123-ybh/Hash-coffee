package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.SeckillActivityDTO;
import com.sky.dto.SeckillActivityPageQueryDTO;
import com.sky.entity.SeckillActivity;
import com.sky.vo.SeckillStatisticsVO;

import java.time.LocalDateTime;
import java.util.List;

public interface SeckillActivityService {

    /**
     * 分页查询秒杀活动
     */
    Page<SeckillActivity> pageQuery(SeckillActivityPageQueryDTO seckillActivityPageQueryDTO);

    /**
     * 根据id查询秒杀活动
     */
    SeckillActivity getById(Long id);

    /**
     * 新增秒杀活动
     */
    void save(SeckillActivityDTO seckillActivityDTO);

    /**
     * 修改秒杀活动
     */
    void update(SeckillActivityDTO seckillActivityDTO);

    /**
     * 根据id删除秒杀活动
     */
    void deleteById(Long id);

    /**
     * 更新秒杀活动状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 获取秒杀活动统计信息
     */
    List<SeckillStatisticsVO> getStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取当前进行中的秒杀活动
     */
    List<SeckillActivity> getCurrentActivities();

    /**
     * 扣减秒杀库存
     */
    boolean reduceStock(Long id, Integer quantity);

    /**
     * 增加已售数量
     */
    void increaseSoldCount(Long id, Integer quantity);
}
