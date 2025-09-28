package com.sky.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 */
@Configuration
public class RabbitMQConfig {
    
    // 订单相关队列
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.pay";
    
    // 积分相关队列
    public static final String POINTS_QUEUE = "points.queue";
    public static final String POINTS_EXCHANGE = "points.exchange";
    public static final String POINTS_ROUTING_KEY = "points.earn";
    
    // 订单超时队列
    public static final String ORDER_TIMEOUT_QUEUE = "order.timeout.queue";
    public static final String ORDER_TIMEOUT_EXCHANGE = "order.timeout.exchange";
    public static final String ORDER_TIMEOUT_ROUTING_KEY = "order.timeout";
    
    // 死信队列
    public static final String ORDER_DLX_QUEUE = "order.dlx.queue";
    public static final String ORDER_DLX_EXCHANGE = "order.dlx.exchange";
    
    // 秒杀活动相关队列
    public static final String SECKILL_ACTIVITY_QUEUE = "seckill.activity.queue";
    public static final String SECKILL_ACTIVITY_EXCHANGE = "seckill.activity.exchange";
    public static final String SECKILL_ACTIVITY_ROUTING_KEY = "seckill.activity";
    
    // 秒杀活动状态变更队列
    public static final String SECKILL_STATUS_QUEUE = "seckill.status.queue";
    public static final String SECKILL_STATUS_EXCHANGE = "seckill.status.exchange";
    public static final String SECKILL_STATUS_ROUTING_KEY = "seckill.status";
    
    // 秒杀活动库存变更队列
    public static final String SECKILL_STOCK_QUEUE = "seckill.stock.queue";
    public static final String SECKILL_STOCK_EXCHANGE = "seckill.stock.exchange";
    public static final String SECKILL_STOCK_ROUTING_KEY = "seckill.stock";
    
    /**
     * 订单交换机
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE, true, false);
    }
    
    /**
     * 订单队列
     */
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "order.dlx")
                .build();
    }
    
    /**
     * 订单队列绑定
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDER_ROUTING_KEY);
    }
    
    /**
     * 积分交换机
     */
    @Bean
    public DirectExchange pointsExchange() {
        return new DirectExchange(POINTS_EXCHANGE, true, false);
    }
    
    /**
     * 积分队列
     */
    @Bean
    public Queue pointsQueue() {
        return QueueBuilder.durable(POINTS_QUEUE).build();
    }
    
    /**
     * 积分队列绑定
     */
    @Bean
    public Binding pointsBinding() {
        return BindingBuilder.bind(pointsQueue()).to(pointsExchange()).with(POINTS_ROUTING_KEY);
    }
    
    /**
     * 订单超时交换机
     */
    @Bean
    public DirectExchange orderTimeoutExchange() {
        return new DirectExchange(ORDER_TIMEOUT_EXCHANGE, true, false);
    }
    
    /**
     * 订单超时队列
     */
    @Bean
    public Queue orderTimeoutQueue() {
        return QueueBuilder.durable(ORDER_TIMEOUT_QUEUE)
                .withArgument("x-message-ttl", 15 * 60 * 1000) // 15分钟TTL
                .withArgument("x-dead-letter-exchange", ORDER_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "order.timeout")
                .build();
    }
    
    /**
     * 订单超时队列绑定
     */
    @Bean
    public Binding orderTimeoutBinding() {
        return BindingBuilder.bind(orderTimeoutQueue()).to(orderTimeoutExchange()).with(ORDER_TIMEOUT_ROUTING_KEY);
    }
    
    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange orderDlxExchange() {
        return new DirectExchange(ORDER_DLX_EXCHANGE, true, false);
    }
    
    /**
     * 死信队列
     */
    @Bean
    public Queue orderDlxQueue() {
        return QueueBuilder.durable(ORDER_DLX_QUEUE).build();
    }
    
    /**
     * 死信队列绑定
     */
    @Bean
    public Binding orderDlxBinding() {
        return BindingBuilder.bind(orderDlxQueue()).to(orderDlxExchange()).with("order.dlx");
    }
    
    /**
     * 秒杀活动交换机
     */
    @Bean
    public DirectExchange seckillActivityExchange() {
        return new DirectExchange(SECKILL_ACTIVITY_EXCHANGE, true, false);
    }
    
    /**
     * 秒杀活动队列
     */
    @Bean
    public Queue seckillActivityQueue() {
        return QueueBuilder.durable(SECKILL_ACTIVITY_QUEUE).build();
    }
    
    /**
     * 秒杀活动队列绑定
     */
    @Bean
    public Binding seckillActivityBinding() {
        return BindingBuilder.bind(seckillActivityQueue()).to(seckillActivityExchange()).with(SECKILL_ACTIVITY_ROUTING_KEY);
    }
    
    /**
     * 秒杀活动状态交换机
     */
    @Bean
    public DirectExchange seckillStatusExchange() {
        return new DirectExchange(SECKILL_STATUS_EXCHANGE, true, false);
    }
    
    /**
     * 秒杀活动状态队列
     */
    @Bean
    public Queue seckillStatusQueue() {
        return QueueBuilder.durable(SECKILL_STATUS_QUEUE).build();
    }
    
    /**
     * 秒杀活动状态队列绑定
     */
    @Bean
    public Binding seckillStatusBinding() {
        return BindingBuilder.bind(seckillStatusQueue()).to(seckillStatusExchange()).with(SECKILL_STATUS_ROUTING_KEY);
    }
    
    /**
     * 秒杀活动库存交换机
     */
    @Bean
    public DirectExchange seckillStockExchange() {
        return new DirectExchange(SECKILL_STOCK_EXCHANGE, true, false);
    }
    
    /**
     * 秒杀活动库存队列
     */
    @Bean
    public Queue seckillStockQueue() {
        return QueueBuilder.durable(SECKILL_STOCK_QUEUE).build();
    }
    
    /**
     * 秒杀活动库存队列绑定
     */
    @Bean
    public Binding seckillStockBinding() {
        return BindingBuilder.bind(seckillStockQueue()).to(seckillStockExchange()).with(SECKILL_STOCK_ROUTING_KEY);
    }
    
    /**
     * JSON消息转换器
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    /**
     * RabbitTemplate配置
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
    
    /**
     * 消费者容器工厂配置
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        
        // 设置并发消费者数量
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        
        // 设置预取数量
        factory.setPrefetchCount(1);
        
        // 设置手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        
        return factory;
    }
}


