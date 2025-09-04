package web.com.springweb.ReviewInsert;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import web.com.springweb.vo.Review;
import web.com.springweb.vo.ReviewFileDto;


@Mapper
public interface ReviewInsertDao {
	@Insert("INSERT INTO REVIEW VALUES(SEQ_REVIEW.NEXTVAL, #{userId}, #{rating}, #{content}, SYSDATE, SYSDATE, #{pcsi})")
    int insertReview(Review ins);

    @Insert("INSERT INTO REVIEW_FILE VALUES(SEQ_REVIEW_FILE.NEXTVAL, SEQ_REVIEW.CURRVAL, #{fname}, #{etc}, SYSDATE, SYSDATE)")
    int insertFile(ReviewFileDto ins);
    
    @Delete("DELETE FROM REVIEW_FILE WHERE FILE_ID = #{fileId}")
    int deleteFileById(@Param("fileId") long fileId);
	
}
