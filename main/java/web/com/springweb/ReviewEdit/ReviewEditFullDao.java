package web.com.springweb.ReviewEdit;

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
public interface ReviewEditFullDao {

    // 소유자 확인
    @Select("SELECT USER_ID FROM REVIEW WHERE REVIEW_ID = #{reviewId}")
    Integer selectOwnerUserId(@Param("reviewId") int reviewId);

    // 리뷰 본문/평점 수정
    @Update("""
            UPDATE REVIEW
               SET RATING = #{rating},
                   CONTENT = #{content},
                   UPDATED_AT = SYSDATE
             WHERE REVIEW_ID = #{reviewId}
            """)
    int updateReview(@Param("reviewId") int reviewId,
                     @Param("rating") int rating,
                     @Param("content") String content);

    // 파일명 조회(물리파일 삭제용)
    @Select("SELECT FNAME FROM REVIEW_FILE WHERE FILE_ID = #{fileId}")
    String selectStoredFileName(@Param("fileId") int fileId);

    // 파일 삭제
    @Delete("DELETE FROM REVIEW_FILE WHERE FILE_ID = #{fileId}")
    int deleteReviewFileById(@Param("fileId") int fileId);

    // 파일 교체(행 업데이트)
    @Update("""
            UPDATE REVIEW_FILE
               SET FNAME = #{saveName},
                   ETC   = #{originalName},
                   UPTDTE = SYSDATE
             WHERE FILE_ID = #{fileId}
            """)
    int updateReviewFile(@Param("fileId") int fileId,
                         @Param("saveName") String saveName,
                         @Param("originalName") String originalName);

    // 새 파일 추가
    @Insert("""
        INSERT INTO REVIEW_FILE(FILE_ID, REVIEW_ID, FNAME, ETC, REGDTE, UPTDTE)
        VALUES(SEQ_REVIEW_FILE.NEXTVAL, #{reviewId}, #{saveName}, #{originalName}, SYSDATE, SYSDATE)
    """)
    int insertReviewFile(@Param("reviewId") int reviewId,
                         @Param("saveName") String saveName,
                         @Param("originalName") String originalName);
    
}
