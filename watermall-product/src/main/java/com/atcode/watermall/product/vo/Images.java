package com.atcode.watermall.product.vo;

import lombok.Data;

@Data
public class Images {

    private String imgUrl;

    private int defaultImg;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    @Override
    public String toString() {
        return "Images{" +
                "imgUrl='" + imgUrl + '\'' +
                ", defaultImg=" + defaultImg +
                '}';
    }
}
