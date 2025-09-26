package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
    
    /**
     * 添加用户积分
     * @param userId 用户ID
     * @param points 积分数量
     * @param orderId 订单ID
     */
    void addUserPoints(Long userId, Integer points, Long orderId);
    
    /**
     * 获取用户积分
     * @param userId 用户ID
     * @return 用户积分
     */
    Integer getUserPoints(Long userId);
}
