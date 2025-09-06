package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CouponTemplatePageQueryDTO;
import com.sky.entity.CouponTemplate;
import com.sky.enumeration.OperationType;
import com.sky.vo.CouponStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CouponTemplateMapper {

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
    @AutoFill(value = OperationType.INSERT)
    void insert(CouponTemplate couponTemplate);

    /**
     * 修改优惠券模板
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(CouponTemplate couponTemplate);

    /**
     * 根据id删除优惠券模板
     */
    void deleteById(Long id);

    /**
     * 更新优惠券模板状态
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 获取优惠券统计信息
     */
    CouponStatisticsVO getStatistics(@Param("startTime") LocalDateTime startTime, 
                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 获取所有启用的优惠券模板
     */
    List<CouponTemplate> listEnabled();
}
