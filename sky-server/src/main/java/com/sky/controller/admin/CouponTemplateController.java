package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.CouponTemplateDTO;
import com.sky.dto.CouponTemplatePageQueryDTO;
import com.sky.entity.CouponTemplate;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CouponTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/coupon/template")
@Api(tags = "优惠券模板管理")
@Slf4j
public class CouponTemplateController {

    @Autowired
    private CouponTemplateService couponTemplateService;

    @GetMapping("/page")
    @ApiOperation("分页查询优惠券模板")
    public Result<PageResult> page(CouponTemplatePageQueryDTO couponTemplatePageQueryDTO) {
        log.info("分页查询优惠券模板：{}", couponTemplatePageQueryDTO);
        Page<CouponTemplate> page = couponTemplateService.pageQuery(couponTemplatePageQueryDTO);
        return Result.success(new PageResult(page.getTotal(), page.getResult()));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询优惠券模板")
    public Result<CouponTemplate> getById(@PathVariable Long id) {
        log.info("根据id查询优惠券模板：{}", id);
        CouponTemplate couponTemplate = couponTemplateService.getById(id);
        return Result.success(couponTemplate);
    }

    @PostMapping
    @ApiOperation("新增优惠券模板")
    public Result save(@RequestBody CouponTemplateDTO couponTemplateDTO) {
        log.info("新增优惠券模板：{}", couponTemplateDTO);
        couponTemplateService.save(couponTemplateDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改优惠券模板")
    public Result update(@RequestBody CouponTemplateDTO couponTemplateDTO) {
        log.info("修改优惠券模板：{}", couponTemplateDTO);
        couponTemplateService.update(couponTemplateDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除优惠券模板")
    public Result deleteById(@PathVariable Long id) {
        log.info("根据id删除优惠券模板：{}", id);
        couponTemplateService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}/{status}")
    @ApiOperation("更新优惠券模板状态")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        log.info("更新优惠券模板状态：id={}, status={}", id, status);
        couponTemplateService.updateStatus(id, status);
        return Result.success();
    }


    @GetMapping("/enabled")
    @ApiOperation("获取所有启用的优惠券模板")
    public Result<List<CouponTemplate>> listEnabled() {
        log.info("获取所有启用的优惠券模板");
        List<CouponTemplate> templates = couponTemplateService.listEnabled();
        return Result.success(templates);
    }
}
