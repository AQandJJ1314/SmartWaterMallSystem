package com.atcode.watermall.watermallsearch.controller;

import com.atcode.common.exception.BizCodeEnum;
import com.atcode.common.to.SkuEsModel;
import com.atcode.common.utils.R;
import com.atcode.watermall.watermallsearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSearchSaveController {

    @Autowired
    ProductSaveService productSaveService;
    /**
     * 上架商品
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b = false;  //true代表有错误，false代表没错误
        try{
            b =  productSaveService.productStatusUp(skuEsModels);
        }catch (Exception e){
            log.error("ElasticSearchSaveController商品上架错误:{}",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if(!b){return R.ok();}
        else {return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());}

    }
}
