package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SeckillActivityPageQueryDTO;
import com.sky.entity.SeckillActivity;
import com.sky.enumeration.OperationType;
import com.sky.vo.SeckillStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SeckillActivityMapper {

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
    @AutoFill(value = OperationType.INSERT)
    void insert(SeckillActivity seckillActivity);

    /**
     * 修改秒杀活动
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(SeckillActivity seckillActivity);

    /**
     * 根据id删除秒杀活动
     */
    void deleteById(Long id);

    /**
     * 更新秒杀活动状态
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 获取秒杀活动统计信息
     */
    List<SeckillStatisticsVO> getStatistics(@Param("startTime") LocalDateTime startTime, 
                                            @Param("endTime") LocalDateTime endTime);

    /**
     * 获取当前进行中的秒杀活动
     */
    List<SeckillActivity> getCurrentActivities(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 扣减秒杀库存
     */
    int reduceStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 增加已售数量
     */
    int increaseSoldCount(@Param("id") Long id, @Param("quantity") Integer quantity);
}
