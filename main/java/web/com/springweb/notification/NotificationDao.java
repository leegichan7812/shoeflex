package web.com.springweb.notification;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NotificationDao {

    // 1) 미처리 문의: 답변이 없는 문의 건수
    @Select("""
        SELECT COUNT(*) FROM PRODUCT_INQUIRY 
        WHERE STATUS = '미확인'
    """)
    int countUnresolvedInquiries();

    // 2) 캘린더 승인 대기: isPublic = 'N'
    @Select("""
        SELECT COUNT(*)
        FROM CALENDAR
        WHERE IS_PUBLIC = 'N'
    """)
    int countCalendarApprovals();

    // 3) 캘린더 임박: 시작/종료가 24시간 이내
    //    (공개이거나 본인 일정만 카운트)
    @Select("""
        SELECT COUNT(*)
        FROM CALENDAR
        WHERE (
               (START1 BETWEEN SYSDATE AND SYSDATE + 1)
            OR (END1   BETWEEN SYSDATE AND SYSDATE + 1)
        )
        AND ( IS_PUBLIC = 'Y' OR USER_ID = #{userId} )
    """)
    int countCalendarDueSoon(@Param("userId") int userId);
    
    // (후순위) 반품/취소/교환
    @Select(""" 
    	SELECT COUNT(*)
		FROM AFTER_SALES_REQUEST
		WHERE REQ_STATUS <> '완료'
		""")
    int countReturns();
	/*
    // (후순위) 리뷰 미답변
    @Select(""" SELECT 0 FROM DUAL """)
    int countReviewsNoReply();
    */
}