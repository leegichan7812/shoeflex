package web.com.springweb.Slide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import web.com.springweb.Product.ProductService;
import web.com.springweb.Slide.dto.Slide;

@Controller
public class SlideController {
	
	@Autowired
	private SlideService slideService;
	@Autowired
	private ProductService productService;
	
	
	@GetMapping("main")
	public String index(Model model) {
		model.addAttribute("brandList", productService.getAllBrands());
		model.addAttribute("categoryList", productService.getAllCategories());
		model.addAttribute("paymentList", productService.getAllPayment());
		List<Slide> eventSlideList = slideService.getSlidesForHome();
        model.addAttribute("eventSlideList", eventSlideList);
		return "pages/main";
	}

}
