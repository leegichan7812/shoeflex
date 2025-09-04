package web.com.springweb.Review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import web.com.springweb.Review.dto.ReviewDto;
import web.com.springweb.vo.Users;

@Controller
public class ReviewController {
	
	@Autowired
    private ReviewService reviewService;

    // [마이페이지] 내 리뷰 리스트
    @GetMapping("review")
    public String myReviewList(HttpSession session, Model model) {
        // 로그인 유저 정보 꺼내기 (userId 추출)
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) {
            // 로그인 안 된 경우 로그인 페이지 등으로 리다이렉트
            return "redirect:/login";
        }

        int userId = loginUser.getUserId();

        // 서비스에서 리뷰 리스트 가져오기
        List<ReviewDto> reviewList = reviewService.getReviewListByUser(userId);
        model.addAttribute("reviewList", reviewList);

        // JSP 파일명 (예: /pages/my/reviewList.jsp)
        return "pages/review";
    }

    // [확장] 상품별 리뷰 리스트
    @GetMapping("/product/{productId}/reviews")
    public String productReviewList(@PathVariable int productId, Model model) {
         List<ReviewDto> reviewList = reviewService.getReviewListByProduct(productId);
         model.addAttribute("reviewList", reviewList);
         return "/pages/product/reviewList";
   }
}
