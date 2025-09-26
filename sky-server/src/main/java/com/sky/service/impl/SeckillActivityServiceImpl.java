package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.sky.constant.RedisKeyConstants;
import com.sky.dto.SeckillActivityDTO;
import com.sky.dto.SeckillActivityPageQueryDTO;
import com.sky.entity.SeckillActivity;
import com.sky.entity.SeckillOrder;
import com.sky.entity.SeckillParticipant;
import com.sky.entity.SeckillStockLog;
import com.sky.lock.DistributedLockService;
import com.sky.mapper.SeckillActivityMapper;
import com.sky.mapper.SeckillOrderMapper;
import com.sky.mapper.SeckillParticipantMapper;
import com.sky.mapper.SeckillStockLogMapper;
import com.sky.service.SeckillActivityService;
import com.sky.vo.SeckillStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SeckillActivityServiceImpl implements SeckillActivityService {

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;
    @Autowired
    private SeckillParticipantMapper seckillParticipantMapper;
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private SeckillStockLogMapper seckillStockLogMapper;
    @Autowired
    private DistributedLockService distributedLockService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public Page<SeckillActivity> pageQuery(SeckillActivityPageQueryDTO seckillActivityPageQueryDTO) {
        return seckillActivityMapper.pageQuery(seckillActivityPageQueryDTO);
    }

    @Override
    public SeckillActivity getById(Long id) {
        return seckillActivityMapper.getById(id);
    }

    @Override
    @Transactional
    public void save(SeckillActivityDTO seckillActivityDTO) {
        SeckillActivity seckillActivity = new SeckillActivity();
        BeanUtils.copyProperties(seckillActivityDTO, seckillActivity);
        seckillActivity.setSoldCount(0);
        seckillActivityMapper.insert(seckillActivity);

        //初始化redis库存
        String stockKey = RedisKeyConstants.getStockKey(seckillActivity.getId());
        redisTemplate.opsForValue().set(stockKey, seckillActivity.getStock());
    }

    @Override
    @Transactional
    public void update(SeckillActivityDTO seckillActivityDTO) {
        SeckillActivity seckillActivity = new SeckillActivity();
        BeanUtils.copyProperties(seckillActivityDTO, seckillActivity);
        seckillActivityMapper.update(seckillActivity);
    }

    @Override
    public void deleteById(Long id) {
        seckillActivityMapper.deleteById(id);
        //删除redis库存
        String stockKey = RedisKeyConstants.getStockKey(id);
        redisTemplate.delete(stockKey);
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

    /**
     * 参与秒杀活动优化版
     */
    @Transactional
    @Override
    public String participateSeckill(Long activityId, Long userId, Integer quantity) {
        //1.获取活动信息
        SeckillActivity activity = getById(activityId);
        if(activity == null){
            return"活动不存在";
        }
        //2.检查活动状态
        if(activity.getStatus()!=1){
            return "活动已禁用";
        }
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(activity.getStartTime())){
            return "活动未开始";
        }
        if(now.isAfter(activity.getEndTime())){
            return "活动已结束";
        }
        //3.使用工具方法生成锁键
        String lockKey = RedisKeyConstants.getLockKey("participate:" +userId+ activityId);
        return distributedLockService.executeWithLock(lockKey, 10, 10, TimeUnit.SECONDS,()->{
            // 4.执行秒杀参与
            List<Object> result = distributedLockService.seckillParticipate(activityId, userId, quantity, activity.getPerUserLimit());
            if(result != null && result.size() > 0){
                Integer success = (Integer) result.get(0);
                if(success == 1){
                    //5.记录参与记录
                    SeckillParticipant participant = SeckillParticipant.builder()
                            .activityId(activityId)
                            .userId(userId)
                            .quantity(quantity)
                            .status(1)
                            .createTime(now)
                            .updateTime(now)
                            .build();
                    seckillParticipantMapper.insert(participant);
                    //6.异步处理订单
                    processSeckillOrderAsync(activity,userId,quantity);
                    return "参与成功";
                }else {
                    return (String) result.get(1);
                }
            }


            return "参与失败";
        });

    }
    //7.异步处理订单
    private void processSeckillOrderAsync(SeckillActivity activity, Long userId, Integer quantity) {
        //创建秒杀订单
        try {
            SeckillOrder order= SeckillOrder.builder()
                    .activityId(activity.getId())
                    .userId(userId)
                    .dishId(activity.getDishId())
                    .quantity(quantity)
                    .seckillPrice(activity.getSeckillPrice())
                    .totalAmount(activity.getSeckillPrice().multiply(BigDecimal.valueOf(quantity)))
                    .status(1)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            seckillOrderMapper.insert(order);
            //更新数据库库存
            updateDatabaseStock(activity.getId(),quantity);
            log.info("秒杀订单创建成功：orderId={},userId={},activityId={}",
                    order.getId(),userId,activity.getId());
        } catch (Exception e) {
            log.error("秒杀订单创建失败",e);
        }
    }

    /**
     * 更新数据库库存
     */
    @Async
    public void updateDatabaseStock(Long activityId, Integer quantity) {
        try {
            // 记录库存扣减日志
            SeckillActivity activity = getById(activityId);
            SeckillStockLog stockLog = SeckillStockLog.builder()
                    .activityId(activityId)
                    .userId(0L) // 系统扣减
                    .quantity(quantity)
                    .beforeStock(activity.getStock())
                    .afterStock(activity.getStock() - quantity)
                    .status(1)
                    .createTime(LocalDateTime.now())
                    .build();
            seckillStockLogMapper.insert(stockLog);

            // 更新数据库库存
            seckillActivityMapper.reduceStock(activityId, quantity);
            seckillActivityMapper.increaseSoldCount(activityId, quantity);
        } catch (Exception e) {
            log.error("更新数据库库存异常", e);
        }
    }
}

