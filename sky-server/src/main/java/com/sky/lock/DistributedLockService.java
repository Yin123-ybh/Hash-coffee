package com.sky.lock;

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

/**
 * 分布式锁服务
 */
@Service
public class DistributedLockService {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Value("${sky.redis.seckill.prefix:seckill:}")
    private String seckillPrefix;

    // 锁缓存，避免重复获取锁对象
    private final ConcurrentHashMap<String, RLock> lockCache = new ConcurrentHashMap<>();


    //秒杀参与脚本
    private final DefaultRedisScript<List> seckillParticipantScript;

    public DistributedLockService() {
        //初始化秒杀参与脚本
        this.seckillParticipantScript = new DefaultRedisScript<>();
        this.seckillParticipantScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("scripts/seckill_participant.lua"))
        );
        this.seckillParticipantScript.setResultType(List.class);
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
    public List<Object> seckillParticipate(Long activityId,Long userId,Integer quantity,Integer perUserLimit){
        String stockKey = seckillPrefix + "stock:" + activityId;
        String participantsKey = seckillPrefix + "participants:" + activityId;
        List<String> keys = Arrays.asList(stockKey, participantsKey);
        Object[] args = {userId.toString(), quantity.toString(), perUserLimit.toString()};

        return redisTemplate.execute(seckillParticipantScript, keys, args);
    }
    }

