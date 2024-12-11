package com.atcode.watermall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.atcode.watermall.ware.VO.MergerVo;
import com.atcode.watermall.ware.VO.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atcode.watermall.ware.entity.PurchaseEntity;
import com.atcode.watermall.ware.service.PurchaseService;
import com.atcode.common.utils.PageUtils;
import com.atcode.common.utils.R;



/**
 * 采购信息
 *
 * @author JiangCheng
 * @email JiangCheng@watermail.com
 * @date 2024-11-26 13:39:03
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 采购需求->批量操作->合并整单
     * 采购需求中查询未领取的采购单
     * @param params
     * @return
     */
    @RequestMapping("/unreceive/list")
    //@RequiresPermissions("ware:purchase:list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 分配，就是修改【采购需求】里对应的【采购单id、采购需求状态】，即purchase_detail表
     * 并且不能重复分配采购需求给不同的采购单,如果还没去采购，或者采购失败，就可以修改
     * @param mergerVo
     * @return
     */
    @PostMapping("/merge")
    //@RequiresPermissions("ware:purchase:list")
    public R merge(@RequestBody MergerVo mergerVo){
        purchaseService.mergePurchase(mergerVo);
        return R.ok();
    }

    /**
     * 领取采购单/ware/purchase/received
     * 采购单分配给了采购人员，采购人员在手机端领取采购单，
     * 此时的采购单应该为新建或已分配状态，在采购人员领取后采购单的状态变为已领取，
     * 采购需求的状态变为正在采购
     */
    //TODO 该接口未测试
    @PostMapping("/received")
    //@RequiresPermissions("ware:purchase:list")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }

    /**
     * 完成采购
     * 完成采购的步骤：
     * 判断所有采购需求的状态，采购需求全部完成时，采购单状态才为完成
     * 采购项完成的时候，增加库存（调用远程获取skuName）
     * 加上分页插件
     */
    @PostMapping("/done")
//@RequiresPermissions("ware:purchase:list")
    public R received(@RequestBody PurchaseDoneVo vo){
        purchaseService.done(vo);
        return R.ok();
    }

}
