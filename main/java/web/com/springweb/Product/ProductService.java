package web.com.springweb.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.vo.Brand;
import web.com.springweb.vo.Category;
import web.com.springweb.vo.Color;
import web.com.springweb.vo.Payment;
import web.com.springweb.vo.Products;
import web.com.springweb.vo.Size;

@Service
public class ProductService {
	
	@Autowired
	private ProductDao dao;
	
	
	public int countProductsByBrand(int brandId) {
		return dao.countByBrand(brandId);
	}
	public List<Products> getProductsByBrandPage(int brandId, int page, int pageSize) {
		int offset = (page - 1) * pageSize;
		List<Products> productList = dao.getBrandProductPage(brandId, offset, pageSize);
		
		for (Products p : productList) {
			p.setColorList(dao.getColorsByProductId(p.getProductId()));
			p.setSizeList(dao.getSizesByProductId(p.getProductId()));
		}
		
		return productList;
	}
	public List<Products> getProductsByCategory(int categoryId) {
        return dao.getCategory(categoryId);
    }
	public Products getProductById(int productId) {
	    return dao.getProductById(productId);
	}
	public List<Products> getAllProducts() {
        return dao.getProduct();
    }
	
	public List<Brand> getAllBrands() {
        return dao.getBrands();
    }
	public List<Category> getAllCategories() {
        return dao.getCategories();
    }
	public List<Size> getAllSizes() {
        return dao.getSizes();
    }
	public List<Color> getAllColors() {
        return dao.getColors();
    }
	public List<Payment> getAllPayment() {
        return dao.getPayment();
    }
	
	
	// ✅ 페이징이 적용된 필터 검색
	public List<Products> getFilteredProducts(Integer brandId, Integer categoryId, Integer minPrice, Integer maxPrice,
            List<Integer> colorList, List<Integer> sizeList, int startRow, int endRow) {

		List<Products> productList = dao.getFilteredProducts(brandId, categoryId, minPrice, maxPrice,
		                                   colorList, sizeList, startRow, endRow);
		
		for (Products p : productList) {
			p.setColorList(dao.getColorsByProductId(p.getProductId()));
			p.setSizeList(dao.getSizesByProductId(p.getProductId()));
	}
	
	return productList;
	}

	// ✅ 전체 개수 조회 (페이징용)
	public int getFilteredProductsCount(Integer brandId, Integer categoryId, Integer minPrice, Integer maxPrice,
	                                    List<Integer> sizeList, List<Integer> colorList) {
        return dao.getFilteredProductsCount(brandId, categoryId, minPrice, maxPrice, sizeList, colorList);
    }
	
	
	
	public List<Products> getProductsByIds(List<Integer> productIds) {
	    if (productIds == null || productIds.isEmpty()) {
	        return java.util.Collections.emptyList();
	    }
	    List<Products> productList = dao.getProductsByIds(productIds);

	    // (옵션) 상품별 색상, 사이즈 같이 채우는 코드
	    for (Products p : productList) {
	        p.setColorList(dao.getColorsByProductId(p.getProductId()));
	        p.setSizeList(dao.getSizesByProductId(p.getProductId()));
	    }

	    return productList;
	}
	
	// 추천 5개 읽어오기
	public List<Map<String,Object>> getRecommendationsOrPopular(Integer userId) {
	    // 1. 로그인 안 한 경우 → 인기 Top5
	    if (userId == null) {
	        return dao.selectPopularFallback();
	    }

	    // 2. 로그인 한 경우 → 추천 조회
	    List<Map<String,Object>> recs = dao.selectRecommendationsByUser(userId);

	    if (recs == null) recs = new ArrayList<>();

	    // 3. 추천이 5개 미만이면 인기 상품으로 채워서 항상 5개 보장
	    if (recs.size() < 5) {
	        int need = 5 - recs.size();
	        List<Integer> excludeIds = recs.stream()
	                                       .map(m -> (Integer)m.get("productId"))
	                                       .collect(Collectors.toList());
	        List<Map<String,Object>> fill = dao.selectPopularFallbackFill(need, excludeIds);
	        recs.addAll(fill);
	    }

	    return recs;
	}
	
	/** 브랜드 기준 → 카테고리 목록 */
    public List<Category> getCategoriesByBrand(int brandId) {
        return dao.getCategoriesByBrand(brandId);
    }

    /** 카테고리 기준 → 브랜드 목록 */
    public List<Brand> getBrandsByCategory(int categoryId) {
        return dao.getBrandsByCategory(categoryId);
    }
}
