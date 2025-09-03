package web.com.springweb.AD_Insert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.com.springweb.Product.ProductService;
import web.com.springweb.Util.ExcelParser;
import web.com.springweb.vo.ProductExcelDto;
import web.com.springweb.vo.Products;
import web.com.springweb.vo.Size;

@Controller
@RequestMapping("/product")
public class InsertController {
	
	@Autowired
	private InsertService service;
	@Autowired
	private ProductService productService;
	
	
	
	/**
     * 상품 등록 페이지 진입
     */
	// http://localhost:7075/product/ad_Insert   
    @GetMapping("/ad_Insert")
    public String adInsert(Model model) {
        model.addAttribute("brandList", productService.getAllBrands());
        model.addAttribute("categoryList", productService.getAllCategories());
        model.addAttribute("colorList", productService.getAllColors());
        model.addAttribute("sizeList", productService.getAllSizes());
        return "/pages/ad_Insert";
    }

    /**
     * 엑셀 양식 다운로드
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        String fileName = "product_template.xlsx";

        InputStream is = getClass().getClassLoader().getResourceAsStream("template/product_template.xlsx");

        if (is == null) {
            throw new FileNotFoundException("엑셀 템플릿 파일이 없습니다: template/product_template.xlsx");
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        ServletOutputStream out = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = is.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        out.flush();
        out.close();
        is.close();
    }

    /**
     * 상품 다중 등록 (직접 입력) Ajax
     */
    @PostMapping("/ajaxMultiInsert")
    @ResponseBody
    public Map<String, Object> ajaxMultiInsert(HttpServletRequest request) {
        Map<String, Object> res = new HashMap<>();

        try {
            String[] names = request.getParameterValues("name");
            String[] descriptions = request.getParameterValues("description");
            String[] prices = request.getParameterValues("price");
            String[] brandIds = request.getParameterValues("brandId");
            String[] categoryIds = request.getParameterValues("categoryId");
            String[] imageUrls = request.getParameterValues("imageUrl");

            List<Products> productList = new ArrayList<>();

            for (int i = 0; i < names.length; i++) {
                Products p = new Products();
                p.setName(names[i]);
                p.setDescription(descriptions[i]);
                p.setPrice(Double.parseDouble(prices[i]));
                p.setBrandId(Integer.parseInt(brandIds[i]));
                p.setCategoryId(categoryIds[i].isEmpty() ? null : Integer.parseInt(categoryIds[i]));
                p.setImageUrl(imageUrls[i]);

                productList.add(p);
            }

            service.insertMultiProducts(productList);

            List<Integer> productIds = productList.stream().map(Products::getProductId).toList();

            res.put("status", "success");
            res.put("productIds", productIds);
            res.put("productList", productList);
        } catch (Exception e) {
            res.put("status", "fail");
            res.put("message", e.getMessage());
        }

        return res;
    }

    /**
     * 색상/사이즈 재고 등록 (Ajax)
     */
    @PostMapping("/insertColorSizeMultiAjax")
    @ResponseBody
    public String insertColorSizeMultiAjax(HttpServletRequest request) {
        try {
            String[] productIds = request.getParameter("productIds").split(",");

            for (String pid : productIds) {
                int productId = Integer.parseInt(pid);
                int colorId = Integer.parseInt(request.getParameter("color_" + productId));

                // product_colors insert 후 productColorId 얻기
                int productColorId = service.insertProductColor(productId, colorId);

                // 사이즈별 재고
                List<Size> sizeList = productService.getAllSizes();

                for (Size size : sizeList) {
                    String paramName = "stock_" + productId + "_" + size.getSizeId();
                    String stockStr = request.getParameter(paramName);

                    // 재고를 입력한 경우만 insert
                    if (stockStr != null && !stockStr.isEmpty()) {
                        int stock = Integer.parseInt(stockStr);
                        if (stock > 0) {
                            service.insertProductColorSize(productColorId, size.getSizeId(), stock);
                        }
                    }
                }
            }

            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 엑셀 업로드 등록 (상품+색상+사이즈)
     */
    @PostMapping("/excelUpload")
    public String excelUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            List<ProductExcelDto> dtoList = ExcelParser.parseProductColorSizeExcel(file);
            service.insertProductWithColorSize(dtoList);
            return "redirect:/product/ad_Insert";

        } catch (Exception e) {
            model.addAttribute("error", "엑셀 업로드 실패: " + e.getMessage());
            return "/pages/ad_Insert";
        }
    }


}
