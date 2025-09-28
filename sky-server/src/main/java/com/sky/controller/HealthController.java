package com.sky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @GetMapping("/check")
    public Map<String, Object> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        
        // 检查Redis连接
        try {
            redisTemplate.opsForValue().get("health:check");
            result.put("redis", "UP");
        } catch (Exception e) {
            result.put("redis", "DOWN");
        }
        
        result.put("status", "UP");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
}


