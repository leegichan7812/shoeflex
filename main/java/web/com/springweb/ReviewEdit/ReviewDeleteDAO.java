package web.com.springweb.ReviewEdit;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewDeleteDAO {

    @Delete("DELETE FROM REVIEW WHERE REVIEW_ID = #{reviewId}")
    int deleteReview(int reviewId);
}

