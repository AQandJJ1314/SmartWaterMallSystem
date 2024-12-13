package com.atcode.watermall.product.vo;

import lombok.Data;

@Data
public class BaseAttrs {

    private Long attrId;

    private String attrValues;

    private int showDesc;

    public Long getAttrId() {
        return attrId;
    }

    public void setAttrId(Long attrId) {
        this.attrId = attrId;
    }

    public String getAttrValues() {
        return attrValues;
    }

    public void setAttrValues(String attrValues) {
        this.attrValues = attrValues;
    }

    public int getShowDesc() {
        return showDesc;
    }

    public void setShowDesc(int showDesc) {
        this.showDesc = showDesc;
    }

    @Override
    public String toString() {
        return "BaseAttrs{" +
                "attrId=" + attrId +
                ", attrValues='" + attrValues + '\'' +
                ", showDesc=" + showDesc +
                '}';
    }
}
