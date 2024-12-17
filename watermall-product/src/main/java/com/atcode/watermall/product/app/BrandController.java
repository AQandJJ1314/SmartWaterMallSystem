package com.atcode.watermall.product.app;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.atcode.common.validator.group.AddGroup;
import com.atcode.common.validator.group.UpdateGroup;
import com.atcode.common.validator.group.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atcode.watermall.product.entity.BrandEntity;
import com.atcode.watermall.product.service.BrandService;
import com.atcode.common.utils.PageUtils;
import com.atcode.common.utils.R;


/**
 * 品牌
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-25 19:53:42
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     * 在需要校验的controller参数添加@Valid注解，并返回提示信息
     * 规范校验错误时返回结果，BindingResult 参数 此参数能够得到异常的信息
     */
    @RequestMapping("/save")
   // @RequiresPermissions("product:brand:save")
    public R save(@RequestBody @Validated({AddGroup.class}) BrandEntity brand){
		brandService.save(brand);

        return R.ok();
    }

//    @RequestMapping("/save")
//    //@RequiresPermissions("product:brand:save")
//    public R save(@Valid @RequestBody BrandEntity brand, BindingResult result){
//        if (result.hasErrors()){
//            Map<String, String> map = new HashMap<>();
//            //1、获取校验的结果
//            result.getFieldErrors().forEach((item)->{
//                //获取到错误提示
//                String message = item.getDefaultMessage();
//                //获取到错误属性的名字(校验错误的字段)
//                String field = item.getField();
//                map.put(field, message);
//            });
//            return R.error(400,"提交的数据不合法").put("data", map);
//        }else{
//            brandService.save(brand);
//        }
//        return R.ok();
//    }

//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    //@RequiresPermissions("product:brand:update")
//    public R update(@RequestBody @Validated({UpdateGroup.class}) BrandEntity brand){
//		brandService.updateById(brand);
//
//        return R.ok();
//    }

    /**
     * 修改品牌名时，同步修改关联表的品牌名
     * @param brand
     * @return
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated(value = {UpdateGroup.class})@RequestBody BrandEntity brand){
        brandService.updateByIdDetail(brand);

        return R.ok();
    }

    /**
     * 仅修改状态
     * @param brand
     * @return
     */
    @RequestMapping("/update/status")
    //校验传来对象的status属性，只能是0或者1。
    public R updateStatus(@RequestBody @Validated({UpdateStatusGroup.class}) BrandEntity brand) {   //只校验状态
        brandService.updateById(brand);

        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
