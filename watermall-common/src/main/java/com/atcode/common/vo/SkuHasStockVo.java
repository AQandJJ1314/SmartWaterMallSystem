package com.atcode.common.vo;

import lombok.Data;

@Data
public class SkuHasStockVo {
    private Long skuId;
    private Boolean hasStock;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Boolean getHasStock() {
        return hasStock;
    }

    public void setHasStock(Boolean hasStock) {
        this.hasStock = hasStock;
    }
}
