package com.atcode.watermall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.atcode.watermall.product.entity.BrandEntity;
import com.atcode.watermall.product.vo.BrandVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atcode.watermall.product.entity.CategoryBrandRelationEntity;
import com.atcode.watermall.product.service.CategoryBrandRelationService;
import com.atcode.common.utils.PageUtils;
import com.atcode.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-25 19:53:42
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }
    /**
     * 获取当前品牌关联的所有分类列表
     */
    @GetMapping("/catelog/list")
    public R catelogList(@RequestParam("brandId") Long brandId){
        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId)
        );

        return R.ok().put("data", data);
    }

    /**
     * 获取当前分类关联的所有品牌
     * 1、 Controller: 处理请求，接受和校验数据
     * 2、Service接受controller传来的数据，进行业务处理
     * 3、Controller接受Service处理完的数据，封装成页面指定的vo
     */
    @GetMapping("/brands/list")
    public R relationBrandsList(@RequestParam(value = "catId", required = true) Long catId){
        List<BrandEntity> vos = categoryBrandRelationService.getBrandsByCatId(catId);
//遍历拷贝vos到brandVo
        List<BrandVo> collect = vos.stream().map(item -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());

            return brandVo;
        }).collect(Collectors.toList());

        return R.ok().put("data", collect);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//   // @RequiresPermissions("product:categorybrandrelation:save")
//    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
//		categoryBrandRelationService.save(categoryBrandRelation);
//
//        return R.ok();
//    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
        categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
