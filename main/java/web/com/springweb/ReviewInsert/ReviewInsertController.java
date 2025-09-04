package web.com.springweb.ReviewInsert;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import web.com.springweb.vo.Review;

@Controller
@RequestMapping("/review")
public class ReviewInsertController {
	
	@Autowired
    private ReviewInsertService reviewService;

    @PostMapping("/insert")
    @ResponseBody
    public String insertReview(
            @RequestParam("userId") int userId,
            @RequestParam("rating") int rating,
            @RequestParam("content") String content,
            @RequestParam("pcsi") int pcsi,
            @RequestParam(value = "reports", required = false) List<MultipartFile> reports
    ) {
        try {
            Review review = new Review();
            review.setUserId(userId);
            review.setRating(rating);
            review.setContent(content);
            review.setpcsi(pcsi);

            reviewService.insertReviewWithFiles(review, reports);
            return "리뷰가 등록되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "리뷰 등록 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
	
}
