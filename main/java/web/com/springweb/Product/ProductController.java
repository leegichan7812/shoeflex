package web.com.springweb.Product;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import web.com.springweb.WishList.WishService;
import web.com.springweb.vo.Products;



@Controller
@RequestMapping("/shop")
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private WishService wishService;
	
	
	// shop 기본페이지 선언
	// http://localhost:7075/shop
	@GetMapping("")
	public String getAllProducts(HttpServletRequest request, Model d) {
	    d.addAttribute("brandList", service.getAllBrands());
	    d.addAttribute("categoryList", service.getAllCategories());
	    d.addAttribute("sizeList", service.getAllSizes());
	    d.addAttribute("colorList", service.getAllColors());
	    d.addAttribute("pListBrand", service.getAllProducts());
	    d.addAttribute("mode", "brand"); // ⭐ 기본 모드 지정 (예: brand/all)

	    // ⭐️ 찜목록 추가 (로그인유저 있을 때만)
	    List<Integer> wishList = java.util.Collections.emptyList();
	    Object loginUser = request.getSession().getAttribute("loginUser");
	    if (loginUser != null) {
	        int userId = ((web.com.springweb.vo.Users) loginUser).getUserId();
	        wishList = wishService.getWishProductIds(userId);
	    }
	    d.addAttribute("wishList", wishList);

	    boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	    if (isAjax) {
	        return "pages/shop";
	    }
	    d.addAttribute("view", "pages/shop");
	    return "common/layout";
	}
	
	@GetMapping("/brand")
	public String getProductsByBrand(
	    @RequestParam("brandId") int brandId,
	    @RequestParam(value = "page", defaultValue = "1") int page,
	    HttpServletRequest request,
	    Model d) {

	    int pageSize = 15;
	    int totalProduct = service.countProductsByBrand(brandId);
	    int totalPage = (int) Math.ceil((double) totalProduct / pageSize);

	    List<Products> pListBrand = service.getProductsByBrandPage(brandId, page, pageSize);

	    d.addAttribute("mode", "brand"); // ⭐️ 모드 추가
	    d.addAttribute("brandList", service.getAllBrands());
	    d.addAttribute("categoryList", service.getAllCategories());
	    d.addAttribute("sizeList", service.getAllSizes());
	    d.addAttribute("colorList", service.getAllColors());
	    d.addAttribute("categoryFilterList", service.getCategoriesByBrand(brandId));

	    d.addAttribute("pListBrand", pListBrand);
	    d.addAttribute("currentPage", page);
	    d.addAttribute("totalPage", totalPage);
	    d.addAttribute("brandId", brandId);

	    // ⭐️ 찜목록 추가 (로그인유저 있을 때만)
	    List<Integer> wishList = java.util.Collections.emptyList();
	    Object loginUser = request.getSession().getAttribute("loginUser");
	    if (loginUser != null) {
	        int userId = ((web.com.springweb.vo.Users) loginUser).getUserId();
	        wishList = wishService.getWishProductIds(userId);
	    }
	    d.addAttribute("wishList", wishList);

	    boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	    if (isAjax) {
	        return "pages/shop";
	    }
	    d.addAttribute("view", "pages/shop");
	    return "common/layout";
	}
	
	// 브랜드 전용 Ajax 상품 리스트 (fragment 전용)
	@GetMapping("/brandProductList")
	public String getBrandProductListAjax(@RequestParam("brandId") int brandId,
	                                      @RequestParam(value = "page", defaultValue = "1") int page,
	                                      HttpServletRequest request,
	                                      Model model) {

	    int pageSize = 15;
	    int startRow = (page - 1) * pageSize + 1;
	    int endRow = page * pageSize;

	    List<Products> products = service.getFilteredProducts(brandId, null, null, null, null, null, startRow, endRow);
	    int totalCount = service.getFilteredProductsCount(brandId, null, null, null, null, null);
	    int totalPage = (int) Math.ceil((double) totalCount / pageSize);

	    model.addAttribute("pListBrand", products);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPage", totalPage);

	    // 💡 로그인 유저의 찜목록 추가
	    List<Integer> wishList = java.util.Collections.emptyList();
	    Object loginUser = request.getSession().getAttribute("loginUser");
	    if (loginUser != null) {
	        int userId = ((web.com.springweb.vo.Users) loginUser).getUserId();
	        wishList = wishService.getWishProductIds(userId);
	    }
	    model.addAttribute("wishList", wishList);

	    return "common/productList"; // 항상 fragment 반환
	}

	// 필터 검색 Ajax
	@GetMapping("/filterProductList")
	public String getFilteredProducts(
	    @RequestParam(value = "brandId", required = false) Integer brandId,
	    @RequestParam(value = "categoryId", required = false) Integer categoryId,
	    @RequestParam(value = "minPrice", required = false) Integer minPrice,
	    @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
	    @RequestParam(value = "sizeList", required = false) List<Integer> sizeList,
	    @RequestParam(value = "colorList", required = false) List<Integer> colorList,
	    @RequestParam(value = "page", defaultValue = "1") Integer page,
	    HttpServletRequest request,
	    Model model) {

	    int pageSize = 15;
	    int startRow = (page - 1) * pageSize + 1;
	    int endRow = page * pageSize;

	    List<Products> products = service.getFilteredProducts(brandId, categoryId, minPrice, maxPrice, sizeList, colorList, startRow, endRow);
	    int totalCount = service.getFilteredProductsCount(brandId, categoryId, minPrice, maxPrice, sizeList, colorList);
	    int totalPage = (int) Math.ceil((double) totalCount / pageSize);

	    model.addAttribute("pListBrand", products);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPage", totalPage);
	    
	    model.addAttribute("brandId", brandId);
	    model.addAttribute("categoryId", categoryId);	    
	    
	    // ✅ mode를 일관되게 보장
	    if (brandId != null) {
	        model.addAttribute("mode", "brand");
	    } else if (categoryId != null) {
	        model.addAttribute("mode", "category");
	    } else {
	        // 둘 다 null인 경우 → 이전 상태 유지 (hidden input에서 전달)
	        String prevMode = request.getParameter("mode");
	        model.addAttribute("mode", (prevMode != null ? prevMode : "brand"));
	    }
	    
	    // 💡 로그인 유저의 찜목록 추가
	    List<Integer> wishList = java.util.Collections.emptyList();
	    Object loginUser = request.getSession().getAttribute("loginUser");
	    if (loginUser != null) {
	        int userId = ((web.com.springweb.vo.Users) loginUser).getUserId();
	        wishList = wishService.getWishProductIds(userId);
	    }
	    model.addAttribute("wishList", wishList);

	    return "common/productList"; // 항상 fragment 반환
	}
	
	@GetMapping("/category")
	public String getProductsByCategory(
	        @RequestParam("categoryId") int categoryId,
	        @RequestParam(value = "page", defaultValue = "1") int page,
	        HttpServletRequest request,
	        Model d) {

	    // 공통 데이터
		d.addAttribute("mode", "category"); // ⭐️ 모드 추가
	    d.addAttribute("brandList", service.getAllBrands());
	    d.addAttribute("categoryList", service.getAllCategories());
	    d.addAttribute("sizeList", service.getAllSizes());
	    d.addAttribute("colorList", service.getAllColors());
	    d.addAttribute("brandFilterList", service.getBrandsByCategory(categoryId));
	    

	    // 페이징 계산
	    int pageSize = 15;
	    int startRow = (page - 1) * pageSize + 1;
	    int endRow   = page * pageSize;

	    // 목록/개수 (brandId=null, categoryId=값)
	    List<Products> pListCategory =
	            service.getFilteredProducts(null, categoryId, null, null, null, null, startRow, endRow);
	    int totalCount =
	            service.getFilteredProductsCount(null, categoryId, null, null, null, null);
	    int totalPage = (int) Math.ceil((double) totalCount / pageSize);

	    d.addAttribute("pListBrand", pListCategory); // ← shop.jsp가 이 변수명을 쓰고 있음
	    d.addAttribute("currentPage", page);
	    d.addAttribute("totalPage", totalPage);
	    d.addAttribute("categoryId", categoryId);

	    // 찜목록
	    List<Integer> wishList = java.util.Collections.emptyList();
	    Object loginUser = request.getSession().getAttribute("loginUser");
	    if (loginUser != null) {
	        int userId = ((web.com.springweb.vo.Users) loginUser).getUserId();
	        wishList = wishService.getWishProductIds(userId);
	    }
	    d.addAttribute("wishList", wishList);

	    boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	    if (isAjax) return "pages/shop";
	    d.addAttribute("view", "pages/shop");
	    return "common/layout";
	}

	/** 카테고리 전용 Ajax(상품 리스트 fragment만) */
	@GetMapping("/categoryProductList")
	public String getCategoryProductListAjax(
	        @RequestParam("categoryId") int categoryId,
	        @RequestParam(value = "page", defaultValue = "1") int page,
	        HttpServletRequest request,
	        Model model) {

	    int pageSize = 15;
	    int startRow = (page - 1) * pageSize + 1;
	    int endRow   = page * pageSize;

	    List<Products> products =
	            service.getFilteredProducts(null, categoryId, null, null, null, null, startRow, endRow);
	    int totalCount =
	            service.getFilteredProductsCount(null, categoryId, null, null, null, null);
	    int totalPage = (int) Math.ceil((double) totalCount / pageSize);

	    model.addAttribute("pListBrand", products);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPage", totalPage);

	    // 찜목록
	    List<Integer> wishList = java.util.Collections.emptyList();
	    Object loginUser = request.getSession().getAttribute("loginUser");
	    if (loginUser != null) {
	        int userId = ((web.com.springweb.vo.Users) loginUser).getUserId();
	        wishList = wishService.getWishProductIds(userId);
	    }
	    model.addAttribute("wishList", wishList);

	    return "common/productList"; // fragment
	}
	@GetMapping("/categoriesByBrandFragment")
	public String getCategoriesByBrandFragment(
	        @RequestParam("brandId") int brandId,
	        Model model) {
	    model.addAttribute("categoryFilterList", service.getCategoriesByBrand(brandId));
	    return "common/categoryFilters";
	}

	@GetMapping("/brandsByCategoryFragment")
	public String getBrandsByCategoryFragment(
	        @RequestParam("categoryId") int categoryId,
	        Model model) {
	    model.addAttribute("brandFilterList", service.getBrandsByCategory(categoryId));
	    return "common/brandFilters";
	}

}
