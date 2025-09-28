package com.sky.controller.user;

import com.sky.entity.SeckillActivity;
import com.sky.result.Result;
import com.sky.service.SeckillActivityService;
import com.sky.vo.SeckillActivityDetailVO;
import com.sky.vo.SeckillActivityListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端秒杀活动控制器
 */
@RestController
@RequestMapping("/user/seckill/activity")
@Api(tags = "用户端秒杀活动")
@Slf4j
public class UserSeckillActivityController {

    @Autowired
    private SeckillActivityService seckillActivityService;

    @GetMapping("/current")
    @ApiOperation("获取当前进行中的秒杀活动")
    public Result<List<SeckillActivityListVO>> getCurrentActivities() {
        log.info("获取当前进行中的秒杀活动");
        List<SeckillActivityListVO> activities = seckillActivityService.getCurrentActivitiesVO();
        return Result.success(activities);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询秒杀活动详情")
    public Result<SeckillActivity> getById(@PathVariable Long id) {
        log.info("根据id查询秒杀活动详情：{}", id);
        SeckillActivity seckillActivity = seckillActivityService.getById(id);
        return Result.success(seckillActivity);
    }

    @PostMapping("/participate/{id}")
    @ApiOperation("参与秒杀活动")
    public Result<String> participateSeckill(@PathVariable Long id, 
                                           @RequestParam Integer quantity,
                                           @RequestHeader("userId") Long userId) {
        log.info("用户参与秒杀活动：id={}, quantity={}, userId={}", id, quantity, userId);
        String result = seckillActivityService.participateSeckill(id, userId, quantity);
        return Result.success(result);
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("获取秒杀活动详情（包含菜品信息）")
    public Result<SeckillActivityDetailVO> getActivityDetail(@PathVariable Long id) {
        log.info("获取秒杀活动详情：{}", id);
        SeckillActivityDetailVO detail = seckillActivityService.getActivityDetail(id);
        return Result.success(detail);
    }

    @GetMapping("/dish/{dishId}")
    @ApiOperation("获取活动商品信息")
    public Result<SeckillActivityDetailVO> getActivityDish(@PathVariable Long dishId) {
        log.info("获取活动商品信息：{}", dishId);
        SeckillActivityDetailVO dish = seckillActivityService.getActivityDish(dishId);
        return Result.success(dish);
    }
}
