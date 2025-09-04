package web.com.springweb.ReviewEdit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewDeleteService {

    private final ReviewDeleteDAO reviewDAO;

    public ReviewDeleteService(ReviewDeleteDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Transactional
    public boolean deleteReview(int reviewId) {
        // 비즈니스 로직, 여러 DAO 호출 가능
        return reviewDAO.deleteReview(reviewId) > 0;
    }
}
