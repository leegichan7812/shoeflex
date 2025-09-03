package web.com.springweb.AD_Update;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UpdateDao {
    // 상품 상태(STATUS) 변경 (일시중단/해제/판매중지)
    @Update("UPDATE PRODUCTS SET STATUS = #{status} WHERE PRODUCT_ID = #{productId}")
    int updateProductStatus(@Param("productId") int productId, @Param("status") String status);

    // 색상(컬러)별 상태 변경 (일시중단/해제/판매중지)
    @Update("UPDATE PRODUCT_COLORS SET STATUS = #{status} WHERE PRODUCT_ID = #{productId} AND COLOR_ID = #{colorId}")
    int updateColorStatus(@Param("productId") int productId, @Param("colorId") int colorId, @Param("status") String status);

    // (선택) 사이즈별 재고 수정
    @Update("UPDATE PRODUCT_COLOR_SIZES SET STOCK = #{stock} WHERE PRODUCT_COLOR_ID = #{productColorId} AND SIZE_ID = #{sizeId}")
    int updateColorSizeStock(@Param("productColorId") int productColorId, @Param("sizeId") int sizeId, @Param("stock") int stock);
    
    @Select("SELECT PRODUCT_COLOR_ID FROM PRODUCT_COLORS WHERE PRODUCT_ID = #{productId} AND COLOR_ID = #{colorId}")
    Integer findProductColorId(@Param("productId") int productId, @Param("colorId") int colorId);

    @Select("SELECT SIZE_ID FROM SIZES WHERE SIZE_VALUE = #{sizeValue}")
    Integer findSizeId(@Param("sizeValue") int sizeValue);
}
