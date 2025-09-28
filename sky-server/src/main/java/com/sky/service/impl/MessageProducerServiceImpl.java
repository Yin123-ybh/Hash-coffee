package com.sky.service.impl;

import com.sky.entity.message.OrderPayMessage;
import com.sky.entity.message.OrderTimeoutMessage;
import com.sky.entity.message.PointsEarnMessage;
import com.sky.entity.message.SeckillStatusMessage;
import com.sky.entity.message.SeckillStockMessage;
import com.sky.entity.message.SeckillParticipateMessage;
import com.sky.entity.message.SeckillEndMessage;
import com.sky.service.MessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息生产者服务实现类
 */
@Service
@Slf4j
public class MessageProducerServiceImpl implements MessageProducerService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 发送订单支付消息
     */
    @Override
    public void sendOrderPayMessage(OrderPayMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                "order.exchange",
                "order.pay",
                message,
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return msg;
                }
            );
            log.info("订单支付消息发送成功：orderId={}, userId={}", message.getOrderId(), message.getUserId());
        } catch (Exception e) {
            log.error("发送订单支付消息失败：{}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送积分获得消息
     */
    @Override
    public void sendPointsEarnMessage(PointsEarnMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                "points.exchange",
                "points.earn",
                message,
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return msg;
                }
            );
            log.info("积分获得消息发送成功：userId={}, points={}", message.getUserId(), message.getPoints());
        } catch (Exception e) {
            log.error("发送积分获得消息失败：{}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送订单超时消息
     */
    @Override
    public void sendOrderTimeoutMessage(OrderTimeoutMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                "order.timeout.exchange",
                "order.timeout",
                message,
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    msg.getMessageProperties().setExpiration("900000"); // 15分钟
                    return msg;
                }
            );
            log.info("订单超时消息发送成功：orderId={}, userId={}", message.getOrderId(), message.getUserId());
        } catch (Exception e) {
            log.error("发送订单超时消息失败：{}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送秒杀活动状态变更消息
     */
    @Override
    public void sendSeckillStatusMessage(SeckillStatusMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                "seckill.status.exchange",
                "seckill.status",
                message,
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return msg;
                }
            );
            log.info("秒杀活动状态变更消息发送成功：activityId={}, statusType={}", 
                    message.getActivityId(), message.getStatusType());
        } catch (Exception e) {
            log.error("发送秒杀活动状态变更消息失败：{}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送秒杀活动库存变更消息
     */
    @Override
    public void sendSeckillStockMessage(SeckillStockMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                "seckill.stock.exchange",
                "seckill.stock",
                message,
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return msg;
                }
            );
            log.info("秒杀活动库存变更消息发送成功：activityId={}, changeType={}, changeQuantity={}", 
                    message.getActivityId(), message.getChangeType(), message.getChangeQuantity());
        } catch (Exception e) {
            log.error("发送秒杀活动库存变更消息失败：{}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送秒杀活动参与消息
     */
    @Override
    public void sendSeckillParticipateMessage(SeckillParticipateMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                "seckill.activity.exchange",
                "seckill.activity",
                message,
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return msg;
                }
            );
            log.info("秒杀活动参与消息发送成功：activityId={}, userId={}, result={}", 
                    message.getActivityId(), message.getUserId(), message.getResult());
        } catch (Exception e) {
            log.error("发送秒杀活动参与消息失败：{}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送秒杀活动结束消息
     */
    @Override
    public void sendSeckillEndMessage(SeckillEndMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                "seckill.activity.exchange",
                "seckill.activity",
                message,
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return msg;
                }
            );
            log.info("秒杀活动结束消息发送成功：activityId={}, endType={}, totalSales={}", 
                    message.getActivityId(), message.getEndType(), message.getTotalSales());
        } catch (Exception e) {
            log.error("发送秒杀活动结束消息失败：{}", e.getMessage(), e);
        }
    }
}


