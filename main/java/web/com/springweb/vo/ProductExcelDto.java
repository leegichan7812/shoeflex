package web.com.springweb.vo;

import java.util.Map;

public class ProductExcelDto {

    private Products product;
    private int colorId;
    private Map<Integer, Integer> sizeStockMap; // sizeId -> stock

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public Map<Integer, Integer> getSizeStockMap() {
        return sizeStockMap;
    }

    public void setSizeStockMap(Map<Integer, Integer> sizeStockMap) {
        this.sizeStockMap = sizeStockMap;
    }

}