package web.com.springweb.AD_Insert;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import web.com.springweb.vo.ProductColorSize;
import web.com.springweb.vo.Products;
import web.com.springweb.vo.ProductsColor;

@Mapper
public interface InsertDao {
	
	/**
     * PRODUCTS 테이블에 상품 등록
     * @param vo 상품 객체
     */
	@Insert("INSERT INTO PRODUCTS (PRODUCT_ID, NAME, DESCRIPTION, PRICE, BRAND_ID, CATEGORY_ID, IMAGE_URL, CREATED_AT) " +
            "VALUES (SEQ_PRODUCT_ID.NEXTVAL, #{name}, #{description}, #{price}, #{brandId}, #{categoryId}, #{imageUrl}, SYSDATE)")
    @Options(useGeneratedKeys = true, keyProperty = "productId", keyColumn = "PRODUCT_ID")
    void insertProduct(Products vo);

    /**
     * PRODUCT_COLORS 테이블에 상품-색상 매핑 등록
     * @param pc ProductColor 객체
     */
	@Insert("INSERT INTO PRODUCT_COLORS (PRODUCT_COLOR_ID, PRODUCT_ID, COLOR_ID) " +
            "VALUES (SEQ_PRODUCT_COLOR_ID.NEXTVAL, #{productId}, #{colorId})")
    @Options(useGeneratedKeys = true, keyProperty = "productColorId", keyColumn = "PRODUCT_COLOR_ID")
    void insertProductColor(ProductsColor pc);

    /**
     * PRODUCT_COLOR_SIZES 테이블에 상품-색상-사이즈 재고 등록
     * @param pcs ProductColorSize 객체
     */
	@Insert("INSERT INTO PRODUCT_COLOR_SIZES (PRODUCT_COLOR_SIZE_ID, PRODUCT_COLOR_ID, SIZE_ID, STOCK) " +
            "VALUES (SEQ_PRODUCT_COLOR_SIZE_ID.NEXTVAL, #{productColorId}, #{sizeId}, #{stock})")
    @Options(useGeneratedKeys = true, keyProperty = "productColorSizeId", keyColumn = "PRODUCT_COLOR_SIZE_ID")
    void insertProductColorSize(ProductColorSize pcs);

}
