package com.atcode.watermall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
/**
 * Description：成直积分、购物积分
 */
@Data
public class Bounds {

    private BigDecimal buyBounds;
    private BigDecimal growBounds;

    public BigDecimal getBuyBounds() {
        return buyBounds;
    }

    public void setBuyBounds(BigDecimal buyBounds) {
        this.buyBounds = buyBounds;
    }

    public BigDecimal getGrowBounds() {
        return growBounds;
    }

    public void setGrowBounds(BigDecimal growBounds) {
        this.growBounds = growBounds;
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "buyBounds=" + buyBounds +
                ", growBounds=" + growBounds +
                '}';
    }
}
