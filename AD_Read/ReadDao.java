package web.com.springweb.AD_Read;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import web.com.springweb.AD_Read.dto.ProductStockViewDTO;
import web.com.springweb.AD_Read.dto.ProductSimpleStockDTO;
import web.com.springweb.AD_Read.dto.ProductColorStockDTO;

@Mapper
public interface ReadDao {

    // 1. 상품 상세 재고 (상품ID + 상태 조건)
	@Select("""
	    SELECT v.*
	    FROM PRODUCT_STOCK_VIEW v
	    WHERE v.PRODUCT_ID = #{productId}
	      AND v.PRODUCT_STATUS IN ('판매중', '일시중단')
	      AND v.COLOR_STATUS   IN ('판매중', '일시중단')   -- ★ 컬러 상태 필터 추가
	    ORDER BY v.COLOR_NAME, v.SIZE_VALUE
	""")
	List<ProductStockViewDTO> selectProductDetailStock(@Param("productId") int productId);

    // 2. 전체 상품 목록 (대표정보, 설명 포함)
    @Select("""
        SELECT DISTINCT
            PRODUCT_ID, PRODUCT_NAME, IMAGE_URL,
            BRAND_NAME, CATEGORY_NAME, PRODUCT_TOTAL_STOCK, PRODUCT_STATUS
        FROM PRODUCT_STOCK_VIEW
        WHERE PRODUCT_STATUS IN ('판매중', '일시중단')
        ORDER BY PRODUCT_ID DESC
    """)
    List<ProductSimpleStockDTO> selectAllProductSimple();

    // 3. 컬러별 재고 (상품/컬러/컬러이름/코드/컬러별재고/컬러상태)
    @Select("""
        SELECT DISTINCT
            PRODUCT_ID, PRODUCT_NAME, COLOR_NAME, COLOR_ID, COLOR_CODE, COLOR_TOTAL_STOCK, COLOR_STATUS
        FROM PRODUCT_STOCK_VIEW
        WHERE PRODUCT_STATUS IN ('판매중', '일시중단')
          AND COLOR_STATUS IN ('판매중', '일시중단')
        ORDER BY PRODUCT_ID DESC, COLOR_NAME
    """)
    List<ProductColorStockDTO> selectColorTotalStock();

    // 4. 상품 개수 (필터)
    @Select("""
        SELECT COUNT(*)
        FROM (
            SELECT DISTINCT PRODUCT_ID
            FROM PRODUCT_STOCK_VIEW
            WHERE PRODUCT_NAME LIKE '%' || #{keyword} || '%'
              AND BRAND_NAME LIKE '%' || #{brand} || '%'
              AND CATEGORY_NAME LIKE '%' || #{category} || '%'
              AND PRODUCT_STATUS IN ('판매중', '일시중단')
        )
    """)
    int selectProductCount(
        @Param("keyword") String keyword,
        @Param("brand") String brand,
        @Param("category") String category
    );

    // 5. 페이징 + 필터 (상품 목록)
    @Select("""
        SELECT * FROM (
            SELECT ROWNUM rn, t.*
            FROM (
                SELECT DISTINCT
                    PRODUCT_ID, PRODUCT_NAME, IMAGE_URL,
                    BRAND_NAME, CATEGORY_NAME, PRODUCT_TOTAL_STOCK, PRODUCT_STATUS
                FROM PRODUCT_STOCK_VIEW
                WHERE PRODUCT_NAME LIKE '%' || #{keyword} || '%'
                  AND BRAND_NAME LIKE '%' || #{brand} || '%'
                  AND CATEGORY_NAME LIKE '%' || #{category} || '%'
                  AND PRODUCT_STATUS IN ('판매중', '일시중단')
                ORDER BY PRODUCT_ID DESC
            ) t
        ) WHERE rn BETWEEN #{startRow} AND #{endRow}
    """)
    List<ProductSimpleStockDTO> selectProductPaging(
        @Param("keyword") String keyword,
        @Param("brand") String brand,
        @Param("category") String category,
        @Param("startRow") int startRow,
        @Param("endRow") int endRow
    );
}
