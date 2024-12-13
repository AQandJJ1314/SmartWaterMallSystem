package com.atcode.watermall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SpuSaveVo {

    private String spuName;

    private String spuDescription;

    private Long catalogId;

    private Long brandId;

    private BigDecimal weight;

    private int publishStatus;

    /**
     * 表述图片
     */
    private List<String> decript;

    private List<String> images;

    private Bounds bounds;

    private List<BaseAttrs> baseAttrs;

    private List<Skus> skus;

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSpuDescription() {
        return spuDescription;
    }

    public void setSpuDescription(String spuDescription) {
        this.spuDescription = spuDescription;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public int getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(int publishStatus) {
        this.publishStatus = publishStatus;
    }

    public List<String> getDecript() {
        return decript;
    }

    public void setDecript(List<String> decript) {
        this.decript = decript;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public List<BaseAttrs> getBaseAttrs() {
        return baseAttrs;
    }

    public void setBaseAttrs(List<BaseAttrs> baseAttrs) {
        this.baseAttrs = baseAttrs;
    }

    public List<Skus> getSkus() {
        return skus;
    }

    public void setSkus(List<Skus> skus) {
        this.skus = skus;
    }

    @Override
    public String toString() {
        return "SpuSaveVo{" +
                "spuName='" + spuName + '\'' +
                ", spuDescription='" + spuDescription + '\'' +
                ", catalogId=" + catalogId +
                ", brandId=" + brandId +
                ", weight=" + weight +
                ", publishStatus=" + publishStatus +
                ", decript=" + decript +
                ", images=" + images +
                ", bounds=" + bounds +
                ", baseAttrs=" + baseAttrs +
                ", skus=" + skus +
                '}';
    }
}
