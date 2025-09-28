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
import com.sky.service.DishService;
import com.sky.service.SeckillActivityService;
import com.sky.service.MessageProducerService;
import com.sky.entity.message.SeckillStatusMessage;
import com.sky.entity.message.SeckillStockMessage;
import com.sky.entity.message.SeckillParticipateMessage;
import com.sky.entity.message.SeckillEndMessage;
import com.sky.vo.DishVO;
import com.sky.vo.SeckillActivityDetailVO;
import com.sky.vo.SeckillActivityListVO;
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
    @Autowired
    private DishService dishService;
    @Autowired
    private MessageProducerService messageProducerService;


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
        
        //发送秒杀活动状态变更消息
        SeckillStatusMessage statusMessage = SeckillStatusMessage.builder()
                .activityId(seckillActivity.getId())
                .activityName(seckillActivity.getName())
                .statusType(1) // 1-启用
                .newStatus(seckillActivity.getStatus())
                .changeTime(LocalDateTime.now())
                .operatorId(1L) // 系统操作
                .operatorName("系统")
                .reason("创建秒杀活动")
                .build();
        messageProducerService.sendSeckillStatusMessage(statusMessage);
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
        
        //获取活动信息
        SeckillActivity activity = seckillActivityMapper.getById(id);
        if (activity != null) {
            //发送秒杀活动状态变更消息
            SeckillStatusMessage statusMessage = SeckillStatusMessage.builder()
                    .activityId(id)
                    .activityName(activity.getName())
                    .statusType(status == 1 ? 1 : 2) // 1-启用，2-禁用
                    .newStatus(status)
                    .changeTime(LocalDateTime.now())
                    .operatorId(1L) // 系统操作
                    .operatorName("系统")
                    .reason(status == 1 ? "启用秒杀活动" : "禁用秒杀活动")
                    .build();
            messageProducerService.sendSeckillStatusMessage(statusMessage);
        }
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
                    
                    //7.发送秒杀参与成功消息
                    SeckillParticipateMessage participateMessage = SeckillParticipateMessage.builder()
                            .activityId(activityId)
                            .activityName(activity.getName())
                            .userId(userId)
                            .quantity(quantity)
                            .seckillPrice(activity.getSeckillPrice())
                            .participateTime(now)
                            .result(1) // 1-成功
                            .failReason(null)
                            .orderId(null) // 订单ID稍后设置
                            .remainingStock((Integer) result.get(2)) // 剩余库存
                            .userIp("127.0.0.1") // 实际项目中从请求中获取
                            .userAgent("WeChat Mini Program")
                            .build();
                    messageProducerService.sendSeckillParticipateMessage(participateMessage);
                    
                    //8.发送库存变更消息
                    SeckillStockMessage stockMessage = SeckillStockMessage.builder()
                            .activityId(activityId)
                            .activityName(activity.getName())
                            .changeType(1) // 1-扣减库存
                            .changeQuantity(quantity)
                            .beforeStock(activity.getStock())
                            .afterStock((Integer) result.get(2))
                            .changeTime(now)
                            .userId(userId)
                            .orderId(null) // 订单ID稍后设置
                            .reason("用户参与秒杀")
                            .build();
                    messageProducerService.sendSeckillStockMessage(stockMessage);
                    
                    return "参与成功";
                }else {
                    //发送秒杀参与失败消息
                    SeckillParticipateMessage participateMessage = SeckillParticipateMessage.builder()
                            .activityId(activityId)
                            .activityName(activity.getName())
                            .userId(userId)
                            .quantity(quantity)
                            .seckillPrice(activity.getSeckillPrice())
                            .participateTime(now)
                            .result(2) // 2-失败
                            .failReason((String) result.get(1))
                            .orderId(null)
                            .remainingStock(activity.getStock())
                            .userIp("127.0.0.1")
                            .userAgent("WeChat Mini Program")
                            .build();
                    messageProducerService.sendSeckillParticipateMessage(participateMessage);
                    
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

    @Override
    public SeckillActivityDetailVO getActivityDetail(Long id) {
        SeckillActivity activity = getById(id);
        if (activity == null) {
            return null;
        }
        
        // 获取菜品信息
        DishVO dishVO = dishService.getByIdWithFlavor(activity.getDishId());
        if (dishVO == null) {
            return null;
        }
        
        // 计算剩余时间
        LocalDateTime now = LocalDateTime.now();
        long remainingSeconds = 0;
        boolean isActive = false;
        
        if (now.isBefore(activity.getEndTime()) && now.isAfter(activity.getStartTime()) && activity.getStatus() == 1) {
            remainingSeconds = java.time.Duration.between(now, activity.getEndTime()).getSeconds();
            isActive = true;
        }
        
        // 计算折扣率
        BigDecimal discountRate = activity.getSeckillPrice()
                .divide(activity.getOriginalPrice(), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        
        return SeckillActivityDetailVO.builder()
                .id(activity.getId())
                .name(activity.getName())
                .description(activity.getDescription())
                .dishId(activity.getDishId())
                .dishName(dishVO.getName())
                .dishImage(dishVO.getImage())
                .seckillPrice(activity.getSeckillPrice())
                .originalPrice(activity.getOriginalPrice())
                .stock(activity.getStock())
                .soldCount(activity.getSoldCount())
                .perUserLimit(activity.getPerUserLimit())
                .startTime(activity.getStartTime())
                .endTime(activity.getEndTime())
                .status(activity.getStatus())
                .remainingTime(remainingSeconds)
                .isActive(isActive)
                .discountRate(discountRate)
                .build();
    }

    @Override
    public SeckillActivityDetailVO getActivityDish(Long dishId) {
        // 查找该菜品是否有进行中的秒杀活动
        List<SeckillActivity> activities = seckillActivityMapper.getCurrentActivities(LocalDateTime.now());
        SeckillActivity activity = activities.stream()
                .filter(a -> a.getDishId().equals(dishId))
                .findFirst()
                .orElse(null);
        
        if (activity == null) {
            return null;
        }
        
        return getActivityDetail(activity.getId());
    }

    @Override
    public List<SeckillActivityListVO> getCurrentActivitiesVO() {
        List<SeckillActivity> activities = getCurrentActivities();
        return activities.stream().map(activity -> {
            // 获取菜品信息
            DishVO dishVO = dishService.getByIdWithFlavor(activity.getDishId());
            if (dishVO == null) {
                return null;
            }
            
            // 计算剩余时间
            LocalDateTime now = LocalDateTime.now();
            long remainingSeconds = 0;
            boolean isActive = false;
            
            if (now.isBefore(activity.getEndTime()) && now.isAfter(activity.getStartTime()) && activity.getStatus() == 1) {
                remainingSeconds = java.time.Duration.between(now, activity.getEndTime()).getSeconds();
                isActive = true;
            }
            
            // 计算折扣率
            BigDecimal discountRate = activity.getSeckillPrice()
                    .divide(activity.getOriginalPrice(), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            
            return SeckillActivityListVO.builder()
                    .id(activity.getId())
                    .name(activity.getName())
                    .dishId(activity.getDishId())
                    .dishName(dishVO.getName())
                    .dishImage(dishVO.getImage())
                    .seckillPrice(activity.getSeckillPrice())
                    .originalPrice(activity.getOriginalPrice())
                    .stock(activity.getStock())
                    .soldCount(activity.getSoldCount())
                    .perUserLimit(activity.getPerUserLimit())
                    .startTime(activity.getStartTime())
                    .endTime(activity.getEndTime())
                    .status(activity.getStatus())
                    .remainingTime(remainingSeconds)
                    .isActive(isActive)
                    .discountRate(discountRate)
                    .build();
        }).filter(vo -> vo != null).collect(java.util.stream.Collectors.toList());
    }
}

