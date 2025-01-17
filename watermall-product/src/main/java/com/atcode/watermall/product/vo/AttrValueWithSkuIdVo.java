package com.atcode.watermall.product.vo;

import lombok.Data;

@Data
public class AttrValueWithSkuIdVo {

    private String attrValue;

    private String skuIds;

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(String skuIds) {
        this.skuIds = skuIds;
    }
}
