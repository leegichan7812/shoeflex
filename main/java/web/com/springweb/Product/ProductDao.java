package web.com.springweb.Product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import web.com.springweb.vo.Brand;
import web.com.springweb.vo.Category;
import web.com.springweb.vo.Color;
import web.com.springweb.vo.Payment;
import web.com.springweb.vo.Products;
import web.com.springweb.vo.Size;

@Mapper
public interface ProductDao {
	
	// 제품을 가져오는 영역
    @Select("SELECT * FROM PRODUCTS WHERE BRAND_ID = #{brandId} AND STATUS = '판매중'")
    List<Products> getProducts(@Param("brandId") int brandId);

    @Select("SELECT * FROM PRODUCTS WHERE CATEGORY_ID = #{categoryId} AND STATUS = '판매중'")
    List<Products> getCategory(@Param("categoryId") int categoryId);

    @Select("SELECT * FROM PRODUCTS WHERE STATUS = '판매중'")
    List<Products> getProduct();

    @Select("SELECT * FROM PRODUCTS WHERE PRODUCT_ID = #{productId} AND STATUS = '판매중'")
    Products getProductById(@Param("productId") int productId);
	
	
	// 제품의 정보들을 가져오는 영역
	@Select("SELECT * FROM BRANDS WHERE STATUS = '판매중'")
	List<Brand> getBrands();
	
	@Select("SELECT * FROM CATEGORIES")
	List<Category> getCategories();
	
	// 1. 브랜드 기준 → 카테고리 목록
    @Select("""
        SELECT DISTINCT c.CATEGORY_ID, c.CATEGORY_NAME, c.CATEGORY_URL
        FROM PRODUCTS p
        JOIN CATEGORIES c ON p.CATEGORY_ID = c.CATEGORY_ID
        WHERE p.BRAND_ID = #{brandId} AND p.STATUS = '판매중'
        ORDER BY c.CATEGORY_NAME
    """)
    List<Category> getCategoriesByBrand(int brandId);

    // 2. 카테고리 기준 → 브랜드 목록
    @Select("""
        SELECT DISTINCT b.BRAND_ID, b.BRAND_NAME, b.BRAND_IMG
        FROM PRODUCTS p
        JOIN BRANDS b ON p.BRAND_ID = b.BRAND_ID
        WHERE p.CATEGORY_ID = #{categoryId} AND p.STATUS = '판매중'
        ORDER BY b.BRAND_NAME
    """)
    List<Brand> getBrandsByCategory(int categoryId);
	
	@Select("SELECT * FROM SIZES")
	List<Size> getSizes();
	
	@Select("SELECT * FROM COLORS")
	List<Color> getColors();
	
	
	// 결제수단 정보를 가져오는 영역
	@Select("SELECT * FROM PAYMENT_METHODS")
	List<Payment> getPayment();
	
	
	List<Products> getFilteredProducts(
            @Param("brandId") Integer brandId,
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("sizeList") List<Integer> sizeList,
            @Param("colorList") List<Integer> colorList,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow
    );

    int getFilteredProductsCount(
            @Param("brandId") Integer brandId,
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("sizeList") List<Integer> sizeList,
            @Param("colorList") List<Integer> colorList
    );
    
    
    // 전체 개수
    int countByBrand(@Param("brandId") int brandId);

    // 페이징 상품 리스트
    List<Products> getBrandProductPage(@Param("brandId") int brandId,
                                      @Param("offset") int offset,
                                      @Param("pageSize") int pageSize);
    
    List<Color> getColorsByProductId(int productId);

    List<Size> getSizesByProductId(int productId);
    
    List<Products> getProductsByIds(List<Integer> productIds);
    
    // 추천 5개 읽어오기
    List<Map<String,Object>> selectRecommendationsByUser(int userId);
    
    List<Map<String,Object>> selectPopularFallback();
    // ✅ 새로 추가: 부족분만 채우는 인기상품
    List<Map<String,Object>> selectPopularFallbackFill(@Param("limit") int limit,
                                                       @Param("excludeIds") List<Integer> excludeIds);
    
    
    // 전체 개수(카테고리)
    int countByCategory(@Param("categoryId") int categoryId);
}
