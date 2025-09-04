package web.com.springweb.ProDetial;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import web.com.springweb.Review.ReviewService;
import web.com.springweb.Review.dto.ReviewDto;
import web.com.springweb.vo.ProductInquiry;
import web.com.springweb.vo.SizeOption;
import web.com.springweb.vo.Users;

@Controller
public class ProDetailController {
	@Autowired(required=false)
	private ProDetailService service;
	
	@Autowired
    private ReviewService reviewService;
	
	// 상세정보 색상 가지고오기
	@GetMapping("productDetail")
	public String getProductDetail(@RequestParam("productId") int productId, Model d) {
		d.addAttribute("proDet",service.getProductDetail(productId));
		d.addAttribute("pInquiryList", service.getProductInquiryList2(productId));
		List<ReviewDto> reviewList = reviewService.getReviewListByProduct(productId);
        d.addAttribute("reviewList", reviewList);
		
		return "pages/productDetail";
	}
	// 색상별 사이즈 리스트 조회
	@GetMapping("getSizesByColor")
	@ResponseBody
	public List<SizeOption> getSizesByColor(
	        @RequestParam int productId,
	        @RequestParam int colorId) {
	    return service.getSizesByColor(productId, colorId);
	}
	@PostMapping("addToCart")
	@ResponseBody
	public String addToCart(@RequestParam int userId,
	                        @RequestParam int productColorSizeId,
	                        @RequestParam int quantity) {
	    boolean isInserted = service.addToCart(userId, productColorSizeId, quantity);
	    return isInserted ? "success" : "exists";
	}
	
	
	@PostMapping("/inquiry/submit")
	public String submitInquiry(ProductInquiry ins, Model d) {
	    String resultMsg = service.insertInquiry(ins);
	    d.addAttribute("msg", resultMsg);
	    
	    // 등록 후 다시 상세페이지로 이동 (productId 포함해서)
	    return "redirect:/index";
	}
	
	
}
