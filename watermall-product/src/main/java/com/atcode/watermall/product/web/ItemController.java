package com.atcode.watermall.product.web;

import com.atcode.watermall.product.service.SkuInfoService;
import com.atcode.watermall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController{
    @Autowired
    private SkuInfoService skuInfoService;
    /**
     * 展示当前sku的详情
     * @param skuId
     * @return
     */
//    @GetMapping("/{skuId}.html")
//    public String skuItem(@PathVariable("skuId") Long skuId){
//        System.out.println("准备查询"+skuId+"的详情");
//        return "item";
//    }

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) {
        SkuItemVo skuItemVo=skuInfoService.item(skuId);
        model.addAttribute("item", skuItemVo);
        return "item";
    }


}
