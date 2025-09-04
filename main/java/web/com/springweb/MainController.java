package web.com.springweb;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import web.com.springweb.Calendar.CalendarService;
import web.com.springweb.Product.ProductService;
import web.com.springweb.Slide.SlideService;
import web.com.springweb.Slide.dto.Slide;
import web.com.springweb.Support.SupportService;
import web.com.springweb.Users.userService;
import web.com.springweb.WishList.WishService;
import web.com.springweb.shoppingCart.TableCartService;
import web.com.springweb.vo.Users;

@Controller
public class MainController {
	
	@Autowired
	private ProductService productService;

	@Autowired
	private TableCartService cartService;
	@Autowired(required=false)
	private SupportService Supportservice;	
	@Autowired
	private userService service;
	@Autowired(required=false)
	private CalendarService calendarService;
	@Autowired
	private SlideService slideService;
	@Autowired
	private WishService wishService;
	
	// http://localhost:7075/admin
	@GetMapping("/admin")
    public String admin(HttpSession session, Model d) {
		d.addAttribute("noticeList", Supportservice.getTop5Notice());
		
		Users loginUser = (Users) session.getAttribute("loginUser");

	    int userId = loginUser.getUserId();
	    String grade = calendarService.getAdminGrade(userId); // 따로 DAO에서 grade 조회
	    System.out.println(grade);
	    d.addAttribute("grade", grade);
		
        return "common/admin";
    }
	
	
	
	@GetMapping("/")
    public String home(Model model, HttpSession session, HttpServletRequest request) {
		Users user = (Users)session.getAttribute("loginUser");
		List<Slide> eventSlideList = slideService.getSlidesForHome();
        model.addAttribute("eventSlideList", eventSlideList);
		model.addAttribute("brandList", productService.getAllBrands());
		model.addAttribute("categoryList", productService.getAllCategories());
		model.addAttribute("paymentList", productService.getAllPayment());	
		
		Integer uid = (user != null) ? user.getUserId() : null;
	    model.addAttribute("recommendList", productService.getRecommendationsOrPopular(uid));
	    // ⭐️ 찜목록 추가 (로그인유저 있을 때만)
	    List<Integer> wishList = java.util.Collections.emptyList();
	    Object loginUser = request.getSession().getAttribute("loginUser");
	    if (loginUser != null) {
	        int userId = ((web.com.springweb.vo.Users) loginUser).getUserId();
	        wishList = wishService.getWishProductIds(userId);
	    }
	    model.addAttribute("wishList", wishList);
	    
		if(user!=null)
			model.addAttribute("list",cartService.gettableCartsList(user.getUserId()));		
        return "common/layout";
    } 
	
	// http://localhost:7075/index
	@GetMapping("index")
	public String header(Model model, HttpSession session, HttpServletRequest request) {
		Users user = (Users)session.getAttribute("loginUser");
		List<Slide> eventSlideList = slideService.getSlidesForHome();
        model.addAttribute("eventSlideList", eventSlideList);
		model.addAttribute("brandList", productService.getAllBrands());
		model.addAttribute("categoryList", productService.getAllCategories());
		model.addAttribute("paymentList", productService.getAllPayment());	
		Integer uid = (user != null) ? user.getUserId() : null;
	    model.addAttribute("recommendList", productService.getRecommendationsOrPopular(uid));
	    // ⭐️ 찜목록 추가 (로그인유저 있을 때만)
	    List<Integer> wishList = java.util.Collections.emptyList();
	    Object loginUser = request.getSession().getAttribute("loginUser");
	    if (loginUser != null) {
	        int userId = ((web.com.springweb.vo.Users) loginUser).getUserId();
	        wishList = wishService.getWishProductIds(userId);
	    }
	    model.addAttribute("wishList", wishList);
		if(user!=null)
			model.addAttribute("list",cartService.gettableCartsList(user.getUserId()));
		return "common/layout";
	}
	
	
	
	
	@PostMapping("login")
	@ResponseBody
	public Object loginProc(Users loginReq, HttpSession session) {
	    Users user = service.selectUser(loginReq);

	    if (user != null) {
	        session.setAttribute("loginUser", user);
	        session.setAttribute("accountType", user.getAccountType());
	        session.setAttribute("userId", user.getUserId());         // 추가
	        session.setAttribute("userName", user.getName()); // ✅ 사용자 이름도 저장

	        String accountType = user.getAccountType();

	        String redirectTarget = null;
	        if ("ADMIN".equalsIgnoreCase(accountType)) {
	        	redirectTarget = "insertJoin";
	        } else if ("USER".equalsIgnoreCase(accountType)) {
	        	redirectTarget = "header";
	        } 
	        
	        if (redirectTarget != null) {
	            return Map.of("redirect", redirectTarget); // ✅ JSON 응답
	        } else {
	            return Map.of("error", "알 수 없는 계정 유형입니다.");
	        }
	        
	    } else {
	    	 return Map.of("error", "아이디 또는 비밀번호가 틀렸습니다.");
	    }
	}
	
	// ✅ 로그아웃
    @GetMapping("logout")
    @ResponseBody
    public String logout(HttpSession session) {
        session.invalidate();
        return "success";
    }
		
	
}
