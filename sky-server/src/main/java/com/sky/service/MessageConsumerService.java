package com.sky.service;

import com.sky.entity.message.OrderPayMessage;
import com.sky.entity.message.OrderTimeoutMessage;
import com.sky.entity.message.PointsEarnMessage;
import com.sky.entity.message.SeckillStatusMessage;
import com.sky.entity.message.SeckillStockMessage;
import com.sky.entity.message.SeckillParticipateMessage;
import com.sky.entity.message.SeckillEndMessage;

/**
 * 消息消费者服务接口
 */
public interface MessageConsumerService {
    
    /**
     * 处理订单支付消息
     * @param message 订单支付消息
     */
    void handleOrderPayMessage(OrderPayMessage message);
    
    /**
     * 处理积分获得消息
     * @param message 积分获得消息
     */
    void handlePointsEarnMessage(PointsEarnMessage message);
    
    /**
     * 处理订单超时消息
     * @param message 订单超时消息
     */
    void handleOrderTimeoutMessage(OrderTimeoutMessage message);
    
    /**
     * 处理秒杀活动状态变更消息
     * @param message 秒杀活动状态变更消息
     */
    void handleSeckillStatusMessage(SeckillStatusMessage message);
    
    /**
     * 处理秒杀活动库存变更消息
     * @param message 秒杀活动库存变更消息
     */
    void handleSeckillStockMessage(SeckillStockMessage message);
    
    /**
     * 处理秒杀活动参与消息
     * @param message 秒杀活动参与消息
     */
    void handleSeckillParticipateMessage(SeckillParticipateMessage message);
    
    /**
     * 处理秒杀活动结束消息
     * @param message 秒杀活动结束消息
     */
    void handleSeckillEndMessage(SeckillEndMessage message);
}


