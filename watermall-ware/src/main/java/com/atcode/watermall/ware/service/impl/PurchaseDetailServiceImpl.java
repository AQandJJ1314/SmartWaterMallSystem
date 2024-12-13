package com.atcode.watermall.ware.service.impl;

import com.atcode.common.utils.R;
import com.atcode.watermall.ware.entity.PurchaseEntity;
import com.atcode.watermall.ware.feign.ProductFeignService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atcode.common.utils.PageUtils;
import com.atcode.common.utils.Query;

import com.atcode.watermall.ware.dao.PurchaseDetailDao;
import com.atcode.watermall.ware.entity.PurchaseDetailEntity;
import com.atcode.watermall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        String key  = (String) params.get("key");
        if (!StringUtils.isNullOrEmpty(key)){
            wrapper.and(w -> {
                w.eq("sku_id", key).or().eq("purchase_id", key);
            });
        }

        String status  = (String) params.get("status");
        if (!StringUtils.isNullOrEmpty(status)){
            wrapper.eq("status", status);
        }

        String wareId  = (String) params.get("wareId");
        if (!StringUtils.isNullOrEmpty(wareId)){
            wrapper.eq("ware_id", wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void saveWithProduct(PurchaseDetailEntity purchaseDetail) {

        //TODO 新增时远程调用获取价格
        BigDecimal skuPrice = null;
        Double test = null;
        try {
            R info = productFeignService.info(purchaseDetail.getSkuId());
            if(info.getCode() == 0){
                Map<String,Object> data=(Map<String,Object>)info.get("skuInfo");
                skuPrice = (BigDecimal) data.get("price");
                test = (Double) data.get("price");
            }
        } catch (Exception e) {

        }

        purchaseDetail.setSkuPrice(skuPrice);

        this.save(purchaseDetail);

    }




}



