package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.SeckillActivityDTO;
import com.sky.dto.SeckillActivityPageQueryDTO;
import com.sky.entity.SeckillActivity;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SeckillActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/seckill")
@Api(tags = "秒杀活动API")
@Slf4j
public class SeckillController {

    @Autowired
    private SeckillActivityService seckillActivityService;

    @GetMapping("/page")
    @ApiOperation("分页查询秒杀活动")
    public Result<PageResult> page(SeckillActivityPageQueryDTO seckillActivityPageQueryDTO) {
        log.info("分页查询秒杀活动：{}", seckillActivityPageQueryDTO);
        Page<SeckillActivity> page = seckillActivityService.pageQuery(seckillActivityPageQueryDTO);
        return Result.success(new PageResult(page.getTotal(), page.getResult()));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询秒杀活动")
    public Result<SeckillActivity> getById(@PathVariable Long id) {
        log.info("根据id查询秒杀活动：{}", id);
        SeckillActivity seckillActivity = seckillActivityService.getById(id);
        return Result.success(seckillActivity);
    }

    @PostMapping
    @ApiOperation("新增秒杀活动")
    public Result<Void> save(@RequestBody SeckillActivityDTO seckillActivityDTO) {
        log.info("新增秒杀活动：{}", seckillActivityDTO);
        seckillActivityService.save(seckillActivityDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改秒杀活动")
    public Result<Void> update(@RequestBody SeckillActivityDTO seckillActivityDTO) {
        log.info("修改秒杀活动：{}", seckillActivityDTO);
        seckillActivityService.update(seckillActivityDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除秒杀活动")
    public Result<Void> deleteById(@PathVariable Long id) {
        log.info("根据id删除秒杀活动：{}", id);
        seckillActivityService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}/{status}")
    @ApiOperation("更新秒杀活动状态")
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        log.info("更新秒杀活动状态：id={}, status={}", id, status);
        seckillActivityService.updateStatus(id, status);
        return Result.success();
    }

    @GetMapping("/current")
    @ApiOperation("获取当前进行中的秒杀活动")
    public Result<List<SeckillActivity>> getCurrentActivities() {
        log.info("获取当前进行中的秒杀活动");
        List<SeckillActivity> activities = seckillActivityService.getCurrentActivities();
        return Result.success(activities);
    }

    @PutMapping("/reduce-stock/{id}")
    @ApiOperation("扣减秒杀库存")
    public Result<Boolean> reduceStock(@PathVariable Long id, @RequestParam Integer quantity) {
        log.info("扣减秒杀库存：id={}, quantity={}", id, quantity);
        boolean success = seckillActivityService.reduceStock(id, quantity);
        return Result.success(success);
    }

    @PutMapping("/increase-sold/{id}")
    @ApiOperation("增加已售数量")
    public Result<Void> increaseSoldCount(@PathVariable Long id, @RequestParam Integer quantity) {
        log.info("增加已售数量：id={}, quantity={}", id, quantity);
        seckillActivityService.increaseSoldCount(id, quantity);
        return Result.success();
    }

}
