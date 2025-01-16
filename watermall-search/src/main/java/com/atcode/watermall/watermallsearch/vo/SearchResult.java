package com.atcode.watermall.watermallsearch.vo;

import com.atcode.common.to.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * 检索之后的返回结果
 */
@Data
public class SearchResult {
    /**
     * 查询到的商品信息
     */
    private List<SkuEsModel> products;

    private Integer pageNum;//当前页码

    private Long total;//总记录数

    private Integer totalPages;//总页码

    private List<BrandVo> brands;//当前查询到的结果，所有涉及到的品牌

    private List<CatalogVo> catalogs;//当前查询到的结果，所有涉及到的分类

    private List<AttrVo> attrs;//当前查询到的结果，所有涉及到的属性


    //=====================以上是返给页面的信息==========================

    @Data
    public static class BrandVo{
        private Long brandId;

        private String brandName;

        private String brandImg;

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getBrandImg() {
            return brandImg;
        }

        public void setBrandImg(String brandImg) {
            this.brandImg = brandImg;
        }

    }

    @Data
    public static class CatalogVo{
        private Long catalogId;

        private String catalogName;

        private String brandImg;

        public Long getCatalogId() {
            return catalogId;
        }

        public void setCatalogId(Long catalogId) {
            this.catalogId = catalogId;
        }

        public String getCatalogName() {
            return catalogName;
        }

        public void setCatalogName(String catalogName) {
            this.catalogName = catalogName;
        }

        public String getBrandImg() {
            return brandImg;
        }

        public void setBrandImg(String brandImg) {
            this.brandImg = brandImg;
        }
    }

    @Data
    public static class AttrVo {
        private Long attrId;

        private String attrName;

        private List<String> attrValue;

        public Long getAttrId() {
            return attrId;
        }

        public void setAttrId(Long attrId) {
            this.attrId = attrId;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public List<String> getAttrValue() {
            return attrValue;
        }

        public void setAttrValue(List<String> attrValue) {
            this.attrValue = attrValue;
        }
    }

    public List<SkuEsModel> getProducts() {
        return products;
    }

    public void setProducts(List<SkuEsModel> products) {
        this.products = products;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<BrandVo> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandVo> brands) {
        this.brands = brands;
    }

    public List<CatalogVo> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<CatalogVo> catalogs) {
        this.catalogs = catalogs;
    }

    public List<AttrVo> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<AttrVo> attrs) {
        this.attrs = attrs;
    }
}
