package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SeckillOrder;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeckillOrderMapper {

    /**
     * 插入秒杀订单
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(SeckillOrder order);

    /**
     * 根据ID查询订单
     */
    SeckillOrder getById(@Param("id") Long id);

    /**
     * 更新订单状态
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据用户ID查询订单列表
     */
    List<SeckillOrder> getByUserId(@Param("userId") Long userId);

    /**
     * 根据活动ID查询订单列表
     */
    List<SeckillOrder> getByActivityId(@Param("activityId") Long activityId);
}
