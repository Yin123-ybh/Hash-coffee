package com.sky.constant;

/**
 * Redis键常量类
 */
public class RedisKeyConstants {

    // 秒杀相关前缀
    public static final String SECKILL_PREFIX = "seckill:";

    // 秒杀子键
    public static final String STOCK_KEY = "stock:";
    public static final String PARTICIPANTS_KEY = "participants:";
    public static final String LOCK_KEY = "lock:";
    public static final String ORDER_KEY = "order:";

    /**
     * 生成库存键
     */
    public static String getStockKey(Long activityId) {
        return SECKILL_PREFIX + STOCK_KEY + activityId;
    }

    /**
     * 生成参与者键
     */
    public static String getParticipantsKey(Long activityId) {
        return SECKILL_PREFIX + PARTICIPANTS_KEY + activityId;
    }

    /**
     * 生成锁键
     */
    public static String getLockKey(String businessKey) {
        return SECKILL_PREFIX + LOCK_KEY + businessKey;
    }

    /**
     * 生成订单键
     */
    public static String getOrderKey(Long orderId) {
        return SECKILL_PREFIX + ORDER_KEY + orderId;
    }
}