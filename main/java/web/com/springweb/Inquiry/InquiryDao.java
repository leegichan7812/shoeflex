package web.com.springweb.Inquiry;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

import web.com.springweb.vo.ProductInquiryView;

@Mapper
public interface InquiryDao {

    // 상태/페이징 조회 (XML에서 구현, 파라미터명 반드시 일치!)
    List<web.com.springweb.vo.ProductInquiryView> getInquiryStatuses(
        @Param("status") String status,
        @Param("startRow") int startRow,
        @Param("endRow") int endRow
    );

    // 총건수 (XML에서 구현, 파라미터명 일치!)
    int getInquiryTotalCount(@Param("status") String status);

    // 답변 단일 조회 (어노테이션)
    @Select("SELECT ANSWER_ID FROM PRODUCT_ANSWER WHERE INQUIRY_ID = #{inquiryId}")
    Integer getAnswerIdByInquiryId(@Param("inquiryId") int inquiryId);

    // 답변 등록 (어노테이션)
    @Insert("""
        INSERT INTO PRODUCT_ANSWER (ANSWER_ID, INQUIRY_ID, ANSWER_CONTENT, ANSWER_DATE, ADMIN_ID)
        VALUES (PRODUCT_ANSWER_SEQ.NEXTVAL, #{inquiryId}, #{answerContent}, SYSDATE, #{adminId})
    """)
    int insertAnswer(@Param("inquiryId") int inquiryId,
                     @Param("answerContent") String answerContent,
                     @Param("adminId") int adminId);

    // 답변 수정 (어노테이션)
    @Update("""
        UPDATE PRODUCT_ANSWER
        SET ANSWER_CONTENT = #{answerContent}, ANSWER_DATE = SYSDATE, ADMIN_ID = #{adminId}
        WHERE ANSWER_ID = #{answerId}
    """)
    int updateAnswer(@Param("answerId") int answerId,
                     @Param("answerContent") String answerContent,
                     @Param("adminId") int adminId);

    // 답변 삭제 (어노테이션)
    @Delete("DELETE FROM PRODUCT_ANSWER WHERE INQUIRY_ID = #{inquiryId}")
    int deleteAnswerByInquiryId(@Param("inquiryId") int inquiryId);

    // 문의 상태: 답변완료 (어노테이션)
    @Update("UPDATE PRODUCT_INQUIRY SET STATUS = '답변완료' WHERE INQUIRY_ID = #{inquiryId}")
    int updateInquiryStatusAnswered(@Param("inquiryId") int inquiryId);

    // 문의 상태: 미확인 (어노테이션)
    @Update("UPDATE PRODUCT_INQUIRY SET STATUS = '미확인' WHERE INQUIRY_ID = #{inquiryId}")
    int updateInquiryStatusWaiting(@Param("inquiryId") int inquiryId);

    // 관리자 뷰어 업데이트 (XML에서 구현, 파라미터명 일치!)
    int updateViewerAdmin(@Param("inquiryId") Long inquiryId, @Param("adminId") Long adminId);
    
    ProductInquiryView getInquiryDetail(@Param("inquiryId") int inquiryId);
    
    List<ProductInquiryView> getRecentUnchecked(@Param("count") int count);
}
