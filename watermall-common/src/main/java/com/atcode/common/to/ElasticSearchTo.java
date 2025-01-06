package com.atcode.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public class ElasticSearchTo {
    @Data
    public static class SkuEsModel {
        private Long skuId;
        private Long spuId;
        private String skuTitle;
        private BigDecimal skuPrice;
        private String skuImg;
        private Long saleCount;
        private boolean hasStock;
        private Long hotScore;
        private Long brandId;
        private Long catalogId;
        private String brandName;
        private String brandImg;
        private String catalogName;
        private List<Attr> attrs;

        //TODO 后续可能会留坑，注意static
        @Data
        public static class Attr{
//        public  class Attr{
            private Long attrId;
            private String attrName;
            private String attrValue;
        }

}}
