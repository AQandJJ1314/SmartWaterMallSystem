package com.atcode.watermall.product.vo;


import com.atcode.watermall.product.entity.SkuImagesEntity;
import com.atcode.watermall.product.entity.SkuInfoEntity;
import com.atcode.watermall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {

    //1、sku基本信息的获取  pms_sku_info
    private SkuInfoEntity info;

    private boolean hasStock = true;

    //2、sku的图片信息    pms_sku_images
    private List<SkuImagesEntity> images;

    //3、获取spu的销售属性组合
    private List<SkuItemSaleAttrVo> saleAttr;

    //4、获取spu的介绍
    private SpuInfoDescEntity desc;

    //5、获取spu的规格参数信息
    private List<SpuItemAttrGroupVo> groupAttrs;

    //6、秒杀商品的优惠信息
    private SeckillSkuVo seckillSkuVo;

    public SkuInfoEntity getInfo() {
        return info;
    }

    public void setInfo(SkuInfoEntity info) {
        this.info = info;
    }

    public boolean isHasStock() {
        return hasStock;
    }

    public void setHasStock(boolean hasStock) {
        this.hasStock = hasStock;
    }

    public List<SkuImagesEntity> getImages() {
        return images;
    }

    public void setImages(List<SkuImagesEntity> images) {
        this.images = images;
    }

    public List<SkuItemSaleAttrVo> getSaleAttr() {
        return saleAttr;
    }

    public void setSaleAttr(List<SkuItemSaleAttrVo> saleAttr) {
        this.saleAttr = saleAttr;
    }

    public SpuInfoDescEntity getDesc() {
        return desc;
    }

    public void setDesc(SpuInfoDescEntity desc) {
        this.desc = desc;
    }

    public List<SpuItemAttrGroupVo> getGroupAttrs() {
        return groupAttrs;
    }

    public void setGroupAttrs(List<SpuItemAttrGroupVo> groupAttrs) {
        this.groupAttrs = groupAttrs;
    }

    public SeckillSkuVo getSeckillSkuVo() {
        return seckillSkuVo;
    }

    public void setSeckillSkuVo(SeckillSkuVo seckillSkuVo) {
        this.seckillSkuVo = seckillSkuVo;
    }
}
