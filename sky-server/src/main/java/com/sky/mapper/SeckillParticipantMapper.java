package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SeckillParticipant;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SeckillParticipantMapper {

    /**
     * 插入秒杀参与记录
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(SeckillParticipant participant);

    /**
     * 根据用户和活动查询参与记录
     */
    SeckillParticipant getByUserAndActivity(@Param("userId") Long userId,
                                            @Param("activityId") Long activityId);

    /**
     * 更新参与状态
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 检查用户是否已参与
     */
    int checkUserParticipated(@Param("userId") Long userId,
                              @Param("activityId") Long activityId);
}
