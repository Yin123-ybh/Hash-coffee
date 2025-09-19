package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SeckillStockLog;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeckillStockLogMapper {

    /**
     * 插入库存扣减记录
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(SeckillStockLog stockLog);

    /**
     * 根据活动ID查询库存记录
     */
    List<SeckillStockLog> getByActivityId(@Param("activityId") Long activityId);

    /**
     * 根据用户ID查询库存记录
     */
    List<SeckillStockLog> getByUserId(@Param("userId") Long userId);
}
