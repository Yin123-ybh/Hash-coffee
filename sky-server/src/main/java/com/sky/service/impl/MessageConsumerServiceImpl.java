package com.sky.service.impl;

import com.rabbitmq.client.Channel;
import com.sky.entity.message.OrderPayMessage;
import com.sky.entity.message.OrderTimeoutMessage;
import com.sky.entity.message.PointsEarnMessage;
import com.sky.entity.message.SeckillStatusMessage;
import com.sky.entity.message.SeckillStockMessage;
import com.sky.entity.message.SeckillParticipateMessage;
import com.sky.entity.message.SeckillEndMessage;
import com.sky.service.MessageConsumerService;
import com.sky.service.MessageProducerService;
import com.sky.service.OrderService;
import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 消息消费者服务实现类
 */
@Service
@Slf4j
public class MessageConsumerServiceImpl implements MessageConsumerService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private MessageProducerService messageProducerService;
    
    /**
     * 处理订单支付消息（RabbitMQ监听器）
     */
    @RabbitListener(queues = "order.queue", containerFactory = "rabbitListenerContainerFactory")
    public void handleOrderPayMessage(OrderPayMessage message, Channel channel, 
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            // 调用接口方法处理业务逻辑
            handleOrderPayMessage(message);
            
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            log.info("订单支付消息处理成功：orderId={}", message.getOrderId());
            
        } catch (Exception e) {
            log.error("处理订单支付消息失败：orderId={}, error={}", message.getOrderId(), e.getMessage(), e);
            try {
                // 拒绝消息并重新入队
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ioException) {
                log.error("拒绝消息失败：{}", ioException.getMessage(), ioException);
            }
        }
    }
    
    /**
     * 处理积分获得消息（RabbitMQ监听器）
     */
    @RabbitListener(queues = "points.queue", containerFactory = "rabbitListenerContainerFactory")
    public void handlePointsEarnMessage(PointsEarnMessage message, Channel channel, 
                                      @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            // 调用接口方法处理业务逻辑
            handlePointsEarnMessage(message);
            
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            log.info("积分获得消息处理成功：userId={}, points={}", message.getUserId(), message.getPoints());
            
        } catch (Exception e) {
            log.error("处理积分获得消息失败：userId={}, error={}", message.getUserId(), e.getMessage(), e);
            try {
                // 拒绝消息并重新入队
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ioException) {
                log.error("拒绝消息失败：{}", ioException.getMessage(), ioException);
            }
        }
    }
    
    /**
     * 处理订单超时消息（RabbitMQ监听器）
     */
    @RabbitListener(queues = "order.dlx.queue", containerFactory = "rabbitListenerContainerFactory")
    public void handleOrderTimeoutMessage(OrderTimeoutMessage message, Channel channel, 
                                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            // 调用接口方法处理业务逻辑
            handleOrderTimeoutMessage(message);
            
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            log.info("订单超时消息处理成功：orderId={}", message.getOrderId());
            
        } catch (Exception e) {
            log.error("处理订单超时消息失败：orderId={}, error={}", message.getOrderId(), e.getMessage(), e);
            try {
                // 拒绝消息并重新入队
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ioException) {
                log.error("拒绝消息失败：{}", ioException.getMessage(), ioException);
            }
        }
    }
    
    /**
     * 处理订单支付消息（业务逻辑）
     */
    @Override
    public void handleOrderPayMessage(OrderPayMessage message) {
        log.info("开始处理订单支付消息：orderId={}, userId={}", message.getOrderId(), message.getUserId());
        
        // 处理订单支付逻辑
        orderService.processOrderPayment(message.getOrderId(), message.getAmount());
        
        // 发送积分获得消息
        PointsEarnMessage pointsMessage = PointsEarnMessage.builder()
                .userId(message.getUserId())
                .orderId(message.getOrderId())
                .points((int) (message.getAmount().doubleValue() * 0.01)) // 1%积分
                .earnTime(message.getPayTime())
                .build();
        
        messageProducerService.sendPointsEarnMessage(pointsMessage);
    }
    
    /**
     * 处理积分获得消息（业务逻辑）
     */
    @Override
    public void handlePointsEarnMessage(PointsEarnMessage message) {
        log.info("开始处理积分获得消息：userId={}, points={}", message.getUserId(), message.getPoints());
        
        // 处理积分获得逻辑
        userService.addUserPoints(message.getUserId(), message.getPoints(), message.getOrderId());
    }
    
    /**
     * 处理订单超时消息（业务逻辑）
     */
    @Override
    public void handleOrderTimeoutMessage(OrderTimeoutMessage message) {
        log.info("开始处理订单超时消息：orderId={}, userId={}", message.getOrderId(), message.getUserId());
        
        // 处理订单超时逻辑
        orderService.cancelTimeoutOrder(message.getOrderId());
    }
    
    /**
     * 处理秒杀活动状态变更消息（RabbitMQ监听器）
     */
    @RabbitListener(queues = "seckill.status.queue", containerFactory = "rabbitListenerContainerFactory")
    public void handleSeckillStatusMessage(SeckillStatusMessage message, Channel channel, 
                                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            // 调用接口方法处理业务逻辑
            handleSeckillStatusMessage(message);
            
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            log.info("秒杀活动状态变更消息处理成功：activityId={}", message.getActivityId());
            
        } catch (Exception e) {
            log.error("处理秒杀活动状态变更消息失败：activityId={}, error={}", message.getActivityId(), e.getMessage(), e);
            try {
                // 拒绝消息并重新入队
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ioException) {
                log.error("拒绝消息失败：{}", ioException.getMessage(), ioException);
            }
        }
    }
    
    /**
     * 处理秒杀活动库存变更消息（RabbitMQ监听器）
     */
    @RabbitListener(queues = "seckill.stock.queue", containerFactory = "rabbitListenerContainerFactory")
    public void handleSeckillStockMessage(SeckillStockMessage message, Channel channel, 
                                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            // 调用接口方法处理业务逻辑
            handleSeckillStockMessage(message);
            
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            log.info("秒杀活动库存变更消息处理成功：activityId={}", message.getActivityId());
            
        } catch (Exception e) {
            log.error("处理秒杀活动库存变更消息失败：activityId={}, error={}", message.getActivityId(), e.getMessage(), e);
            try {
                // 拒绝消息并重新入队
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ioException) {
                log.error("拒绝消息失败：{}", ioException.getMessage(), ioException);
            }
        }
    }
    
    /**
     * 处理秒杀活动参与消息（RabbitMQ监听器）
     */
    @RabbitListener(queues = "seckill.activity.queue", containerFactory = "rabbitListenerContainerFactory")
    public void handleSeckillParticipateMessage(SeckillParticipateMessage message, Channel channel, 
                                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            // 调用接口方法处理业务逻辑
            handleSeckillParticipateMessage(message);
            
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            log.info("秒杀活动参与消息处理成功：activityId={}, userId={}", message.getActivityId(), message.getUserId());
            
        } catch (Exception e) {
            log.error("处理秒杀活动参与消息失败：activityId={}, userId={}, error={}", 
                    message.getActivityId(), message.getUserId(), e.getMessage(), e);
            try {
                // 拒绝消息并重新入队
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ioException) {
                log.error("拒绝消息失败：{}", ioException.getMessage(), ioException);
            }
        }
    }
    
    /**
     * 处理秒杀活动结束消息（RabbitMQ监听器）
     */
    @RabbitListener(queues = "seckill.activity.queue", containerFactory = "rabbitListenerContainerFactory")
    public void handleSeckillEndMessage(SeckillEndMessage message, Channel channel, 
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            // 调用接口方法处理业务逻辑
            handleSeckillEndMessage(message);
            
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            log.info("秒杀活动结束消息处理成功：activityId={}", message.getActivityId());
            
        } catch (Exception e) {
            log.error("处理秒杀活动结束消息失败：activityId={}, error={}", message.getActivityId(), e.getMessage(), e);
            try {
                // 拒绝消息并重新入队
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ioException) {
                log.error("拒绝消息失败：{}", ioException.getMessage(), ioException);
            }
        }
    }
    
    /**
     * 处理秒杀活动状态变更消息（业务逻辑）
     */
    @Override
    public void handleSeckillStatusMessage(SeckillStatusMessage message) {
        log.info("开始处理秒杀活动状态变更消息：activityId={}, statusType={}", 
                message.getActivityId(), message.getStatusType());
        
        // 这里可以添加具体的业务逻辑，比如：
        // 1. 更新前端管理端的活动状态显示
        // 2. 发送WebSocket通知给前端
        // 3. 记录状态变更日志
        // 4. 触发其他相关业务逻辑
        
        log.info("秒杀活动状态变更处理完成：activityId={}, 新状态={}", 
                message.getActivityId(), message.getNewStatus());
    }
    
    /**
     * 处理秒杀活动库存变更消息（业务逻辑）
     */
    @Override
    public void handleSeckillStockMessage(SeckillStockMessage message) {
        log.info("开始处理秒杀活动库存变更消息：activityId={}, changeType={}, changeQuantity={}", 
                message.getActivityId(), message.getChangeType(), message.getChangeQuantity());
        
        // 这里可以添加具体的业务逻辑，比如：
        // 1. 更新前端管理端的库存显示
        // 2. 发送WebSocket通知给小程序实时更新库存
        // 3. 记录库存变更日志
        // 4. 检查是否需要触发活动结束逻辑
        
        log.info("秒杀活动库存变更处理完成：activityId={}, 剩余库存={}", 
                message.getActivityId(), message.getAfterStock());
    }
    
    /**
     * 处理秒杀活动参与消息（业务逻辑）
     */
    @Override
    public void handleSeckillParticipateMessage(SeckillParticipateMessage message) {
        log.info("开始处理秒杀活动参与消息：activityId={}, userId={}, result={}", 
                message.getActivityId(), message.getUserId(), message.getResult());
        
        // 这里可以添加具体的业务逻辑，比如：
        // 1. 记录用户参与日志
        // 2. 发送参与结果通知给用户
        // 3. 更新统计数据
        // 4. 触发营销活动（如推荐相关商品）
        
        if (message.getResult() == 1) {
            log.info("用户参与秒杀成功：activityId={}, userId={}, 剩余库存={}", 
                    message.getActivityId(), message.getUserId(), message.getRemainingStock());
        } else {
            log.info("用户参与秒杀失败：activityId={}, userId={}, 失败原因={}", 
                    message.getActivityId(), message.getUserId(), message.getFailReason());
        }
    }
    
    /**
     * 处理秒杀活动结束消息（业务逻辑）
     */
    @Override
    public void handleSeckillEndMessage(SeckillEndMessage message) {
        log.info("开始处理秒杀活动结束消息：activityId={}, endType={}, totalSales={}", 
                message.getActivityId(), message.getEndType(), message.getTotalSales());
        
        // 这里可以添加具体的业务逻辑，比如：
        // 1. 生成活动结束报告
        // 2. 发送活动结束通知
        // 3. 更新统计数据
        // 4. 清理相关缓存
        // 5. 触发后续营销活动
        
        log.info("秒杀活动结束处理完成：activityId={}, 参与用户数={}, 总销售额={}", 
                message.getActivityId(), message.getParticipantCount(), message.getTotalSales());
    }
}


