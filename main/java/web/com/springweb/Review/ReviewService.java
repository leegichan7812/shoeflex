package web.com.springweb.Review;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.Review.dto.ReviewDto;
import web.com.springweb.Review.dto.ReviewFlatDto;
import web.com.springweb.vo.ReviewFileDto;

@Service
public class ReviewService {
	
	@Autowired
    private ReviewDao reviewDao;
	
	// [1] 유저별 리뷰 전체 조회 (그룹핑)
    public List<ReviewDto> getReviewListByUser(int userId) {
        List<ReviewFlatDto> flatList = reviewDao.selectReviewListByUser(userId);
        return groupReviews(flatList);
    }

    // [2] 상품별 리뷰 (필요하면)
    public List<ReviewDto> getReviewListByProduct(int productId) {
        List<ReviewFlatDto> flatList = reviewDao.selectReviewListByProduct(productId);
        return groupReviews(flatList);
    }

    // [3] 그룹핑 로직 (flatList → reviewId 기준 ReviewDto로 변환)
    private List<ReviewDto> groupReviews(List<ReviewFlatDto> flatList) {
        Map<Integer, ReviewDto> map = new LinkedHashMap<>();
        for (ReviewFlatDto row : flatList) {
            ReviewDto dto = map.computeIfAbsent(row.getReviewId(), id -> {
                ReviewDto r = new ReviewDto();
                r.setReviewId(row.getReviewId());
                r.setUserId(row.getUserId());
                r.setProductId(row.getProductId());
                r.setRating(row.getRating());
                r.setContent(row.getContent());
                r.setCreatedAt(row.getCreatedAt());
                r.setUpdatedAt(row.getUpdatedAt());
                r.setSizeValue(row.getSizeValue());
                r.setColorNameKor(row.getColorNameKor());
                r.setColorNameEng(row.getColorNameEng());
                r.setProductName(row.getProductName());
                r.setBrandName(row.getBrandName());
                r.setCategoryName(row.getCategoryName());
                r.setUserName(row.getUserName());
                r.setImageNames(new ArrayList<>());
                r.setImageFiles(new ArrayList<>());
                return r;
            });
            // 이미지 파일명 리스트에 중복 없이 추가 (null 체크)
            if (row.getFname() != null && !dto.getImageNames().contains(row.getFname())) {
                dto.getImageNames().add(row.getFname());
            }
            
            if (row.getFileId() != null) {
                ReviewFileDto f = new ReviewFileDto();
                f.setFileId(String.valueOf(row.getFileId()));     // DB가 숫자면 String 변환
                f.setReviewId(String.valueOf(row.getReviewId()));
                f.setFname(row.getFname());                       // 저장 파일명
                f.setEtc(row.getEtc());                           // 원본 파일명
                dto.getImageFiles().add(f);
            }
        }
        return new ArrayList<>(map.values());
    }

}
