package com.atcode.watermall.product.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.atcode.watermall.product.vo.AttrRespVo;
import com.atcode.watermall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atcode.watermall.product.service.AttrService;
import com.atcode.common.utils.PageUtils;
import com.atcode.common.utils.R;



/**
 * 商品属性
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-25 19:53:42
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

//    /**
//     * 列表
//     */
//    @RequestMapping("/list")
//    //@RequiresPermissions("product:attr:list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = attrService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }


    /**
     * 通过添加路径变量{attrType}同时用一个方法查询销售属性和规格参数。并且attr表的attr_type字段是属性类型[0-销售属性，1-基本属性]，新增时只能选择0和1。
     * 注意： 仅查询规格参数时设置分组名，销售属性无分组。在设置分组id时，要判断查到分组是不是null，防止空指针异常
     * @param params
     * @param catelogId
     * @param attrType
     * @return
     */
    @RequestMapping("/{attrType}/list/{catelogId}")
    //路径参数和形参名同名时，@PathVariable的值可以省略。
    public R baseList(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId,
                      @PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryBaseAttrPage(params, catelogId, attrType);
        return R.ok().put("page", page);
    }


//    @RequestMapping("/base/list/{catelogId}")
//    public R baseList(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId){
//        PageUtils page = attrService.queryBaseAttrPage(params, catelogId);
//        return R.ok().put("page", page);
//    }

//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{attrId}")
//    //@RequiresPermissions("product:attr:info")
//    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);
//
//        return R.ok().put("attr", attr);
//    }

    /**
     * 信息  这里传入类别的路径
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     *  使用传入的VO对象
     */
    @RequestMapping("/save")
   // @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
//    @RequestMapping("/update")
//    //@RequiresPermissions("product:attr:update")
//    public R update(@RequestBody AttrEntity attr){
//		attrService.updateById(attr);
//
//        return R.ok();
//    }
    /**
     * 修改
     * 修改属性名时，同步修改属性和属性分组的关联关系
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
        attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
