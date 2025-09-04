package web.com.springweb.ReviewEdit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.com.springweb.Review.ReviewService;

@RestController
@RequestMapping("/review")
public class ReviewDeleteController {

    private final ReviewDeleteService reviewService;

    // 생성자 주입 (권장)
    public ReviewDeleteController(ReviewDeleteService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/delete")
    public Map<String, Object> deleteReview(@RequestParam("reviewId") int reviewId) {
        Map<String, Object> result = new HashMap<>();

        boolean success = reviewService.deleteReview(reviewId);
        System.out.println("삭제 요청 reviewId: " + reviewId);

        result.put("success", success);
        if (!success) {
            result.put("message", "리뷰 삭제 실패");
        }

        return result; // JSON 응답
    }
}


