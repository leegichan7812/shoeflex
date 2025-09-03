package web.com.springweb.AD_Read;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import web.com.springweb.AD_Read.dto.ProductColorStockDTO;
import web.com.springweb.AD_Read.dto.ProductSimpleStockDTO;
import web.com.springweb.AD_Read.dto.ProductStockViewDTO;
import web.com.springweb.Product.ProductService;

@Controller
@RequestMapping("/product")
public class ReadController {

    @Autowired
    private ReadService service;
    @Autowired
	private ProductService productService;

    
    // 상품 상세 재고 Ajax 반환
    @GetMapping("/detailStock")
    @ResponseBody
    public List<ProductStockViewDTO> getProductStock(@RequestParam int productId) {
        return service.getStockDetail(productId);
    }

    // 컬러별 재고 (필요 시)
    @GetMapping("/colorStock")
    @ResponseBody
    public List<ProductColorStockDTO> getColorStock() {
        return service.getColorTotalStock();
    }
    
    @GetMapping("/ad_Read")
    public String productRead(
        @RequestParam(defaultValue="") String name,
        @RequestParam(defaultValue="") String brand,
        @RequestParam(defaultValue="") String category,
        @RequestParam(defaultValue="1") int page,
        Model model
    ) {
        int pageSize = 5;
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;

        List<ProductSimpleStockDTO> list = service.getProductPaging(name, brand, category, startRow, endRow);
        int totalCount = service.getProductCount(name, brand, category);
        int totalPage = (int)Math.ceil(totalCount / (double)pageSize);

        model.addAttribute("productList", list);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("brandList", productService.getAllBrands());
        model.addAttribute("categoryList", productService.getAllCategories());

        model.addAttribute("filterName", name);
        model.addAttribute("filterBrand", brand);
        model.addAttribute("filterCategory", category);

        return "/pages/ad_Read";
    }
    @GetMapping("/ad_ReadList")
    @ResponseBody
    public Map<String, Object> productReadAjax(
        @RequestParam(defaultValue="") String name,
        @RequestParam(defaultValue="") String brand,
        @RequestParam(defaultValue="") String category,
        @RequestParam(defaultValue="1") int page
    ) {
        int pageSize = 5;
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;

        List<ProductSimpleStockDTO> list = service.getProductPaging(name, brand, category, startRow, endRow);
        int totalCount = service.getProductCount(name, brand, category);
        int totalPage = (int)Math.ceil(totalCount / (double)pageSize);

        return Map.of(
            "productList", list,
            "currentPage", page,
            "totalPage", totalPage
            // 필요시 brandList, categoryList도 추가 가능
        );
    }
}
