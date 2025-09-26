package com.sky.lock;

import com.sky.constant.RedisKeyConstants;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁服务（优化版）
 */
@Slf4j
@Service
public class DistributedLockService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${sky.redis.seckill.prefix}")
    private String seckillPrefix;

    // 锁缓存，避免重复获取锁对象
    private final ConcurrentHashMap<String, RLock> lockCache = new ConcurrentHashMap<>();

    // 秒杀参与脚本
    private final DefaultRedisScript<List> seckillParticipateScript;

    public DistributedLockService() {
        // 初始化秒杀参与脚本
        this.seckillParticipateScript = new DefaultRedisScript<>();
        this.seckillParticipateScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("scripts/seckill_participate.lua"))
        );
        this.seckillParticipateScript.setResultType(List.class);
    }

    /**
     * 获取锁对象（带缓存）
     */
    private RLock getLock(String lockKey) {
        return lockCache.computeIfAbsent(lockKey, key -> redissonClient.getLock(key));
    }

    /**
     * 秒杀参与
     */
    public List<Object> seckillParticipate(Long activityId, Long userId, Integer quantity, Integer perUserLimit) {
        // 使用工具方法生成键
        String stockKey = seckillPrefix + RedisKeyConstants.getStockKey(activityId);
        String participantsKey = seckillPrefix + RedisKeyConstants.getParticipantsKey(activityId);

        List<String> keys = Arrays.asList(stockKey, participantsKey);
        Object[] args = {userId.toString(), quantity.toString(), perUserLimit.toString()};

        return redisTemplate.execute(seckillParticipateScript, keys, args);
    }

    /**
     * 尝试获取锁（优化版）
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取锁被中断", e);
        }
    }

    /**
     * 释放锁（优化版）
     */
    public void unlock(String lockKey) {
        RLock lock = getLock(lockKey);
        try {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Exception e) {
            log.warn("释放锁失败: {}", lockKey, e);
        }
    }

    /**
     * 执行带锁的操作（推荐使用）
     */
    public <T> T executeWithLock(String lockKey, long waitTime, long leaseTime,
                                 TimeUnit unit, Supplier<T> supplier) {
        RLock lock = getLock(lockKey);
        try {
            if (lock.tryLock(waitTime, leaseTime, unit)) {
                return supplier.get();
            } else {
                throw new RuntimeException("获取锁失败: " + lockKey);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取锁被中断: " + lockKey, e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 检查锁状态
     */
    public boolean isLocked(String lockKey) {
        RLock lock = getLock(lockKey);
        return lock.isLocked();
    }

    /**
     * 获取锁剩余时间
     */
    public long getLockRemainingTime(String lockKey) {
        RLock lock = getLock(lockKey);
        return lock.remainTimeToLive();
    }
}