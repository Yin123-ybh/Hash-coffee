package com.sky.service;

import com.sky.entity.message.OrderPayMessage;
import com.sky.entity.message.OrderTimeoutMessage;
import com.sky.entity.message.PointsEarnMessage;
import com.sky.entity.message.SeckillStatusMessage;
import com.sky.entity.message.SeckillStockMessage;
import com.sky.entity.message.SeckillParticipateMessage;
import com.sky.entity.message.SeckillEndMessage;

/**
 * 消息生产者服务接口
 */
public interface MessageProducerService {
    
    /**
     * 发送订单支付消息
     * @param message 订单支付消息
     */
    void sendOrderPayMessage(OrderPayMessage message);
    
    /**
     * 发送积分获得消息
     * @param message 积分获得消息
     */
    void sendPointsEarnMessage(PointsEarnMessage message);
    
    /**
     * 发送订单超时消息
     * @param message 订单超时消息
     */
    void sendOrderTimeoutMessage(OrderTimeoutMessage message);
    
    /**
     * 发送秒杀活动状态变更消息
     * @param message 秒杀活动状态变更消息
     */
    void sendSeckillStatusMessage(SeckillStatusMessage message);
    
    /**
     * 发送秒杀活动库存变更消息
     * @param message 秒杀活动库存变更消息
     */
    void sendSeckillStockMessage(SeckillStockMessage message);
    
    /**
     * 发送秒杀活动参与消息
     * @param message 秒杀活动参与消息
     */
    void sendSeckillParticipateMessage(SeckillParticipateMessage message);
    
    /**
     * 发送秒杀活动结束消息
     * @param message 秒杀活动结束消息
     */
    void sendSeckillEndMessage(SeckillEndMessage message);
}


