package web.com.springweb.Admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import web.com.springweb.AD_Insert.InsertService;
import web.com.springweb.AD_Read.ReadService;
import web.com.springweb.Calendar.CalendarService;
import web.com.springweb.Chart.ChartService;
import web.com.springweb.Product.ProductService;
import web.com.springweb.Support.SupportService;
import web.com.springweb.vo.Users;

@Controller
public class AdminController {
	
	@Autowired(required=false)
	private SupportService Supportservice;	
	@Autowired
	private ReadService readService;
	@Autowired
	private InsertService insertService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ChartService chartService;
	@Autowired(required=false)
	private CalendarService calendarService;
	
	// http://localhost:7075/admin
	@GetMapping("/admin/dashboard")
    public String mainPage(HttpSession session, Model d) {
		d.addAttribute("noticeList", Supportservice.getTop5Notice());
		
		Users loginUser = (Users) session.getAttribute("loginUser");

	    int userId = loginUser.getUserId();
	    String grade = calendarService.getAdminGrade(userId); // 따로 DAO에서 grade 조회
	    System.out.println(grade);
	    d.addAttribute("grade", grade);
		
        return "/pages/dashboard"; // 레이아웃 페이지
    }
	
	// http://localhost:7075/admin/ad_product
	@GetMapping("/admin/ad_product")
    public String ad_product() {
        return "/common/ad_product"; // 레이아웃 페이지
    }
	
	// http://localhost:7075/admin/inquiry
	@GetMapping("/admin/inquiry")
    public String showInquiryStatuses() {
        return "/pages/inquiry"; 
    }
	
	
	// http://localhost:7075/admin/ad_product
	@GetMapping("/admin/ad_DeleteUser")
    public String ad_DeleteUser() {
        return "/common/ad_DeleteUser"; // 레이아웃 페이지
    }
	
	

    

    @GetMapping("/admin/ad_Update")
    public String adUpdate(Model model) {
        // 수정용 데이터 있으면 전달
        return "/pages/ad_Update";
    }

    @GetMapping("/admin/ad_Delete")
    public String adDelete(Model model) {
        // 삭제용 데이터 있으면 전달
        return "/pages/ad_Delete";
    }
    
    @GetMapping("/logout2")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 전체 삭제
        return "redirect:/index"; // 로그인 페이지로 이동
    }

	
}
