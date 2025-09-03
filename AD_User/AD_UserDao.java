package web.com.springweb.AD_User;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import web.com.springweb.AD_User.dto.Chart;
import web.com.springweb.AD_User.dto.PurchaseHistoryFullChart;
import web.com.springweb.AD_User.dto.Qnaview;
import web.com.springweb.AD_User.dto.ReviewWithFiles;
import web.com.springweb.AD_User.dto.Users;

@Mapper
public interface AD_UserDao {

    /* 회원 정보 조회 */
    @Select("SELECT u.* " +
            "FROM USERS u " +
            "WHERE u.EMAIL = #{email}")
    Users getUsersByEmail(@Param("email") String email);

    /* 리뷰 조회 (이메일 기반) */
    @Select("SELECT r.* " +
            "FROM REVIEW_WITH_FILES r " +
            "JOIN USERS u ON r.USER_ID = u.USER_ID " +
            "WHERE u.EMAIL = #{email}")
    List<ReviewWithFiles> getReviewWithFilesList(@Param("email") String email);

    /* QnA 조회 (이메일 기반) */
    @Select("SELECT q.* " +
            "FROM QNA_VIEW q " +
            "JOIN USERS u ON q.USER_ID = u.USER_ID " +
            "WHERE u.EMAIL = #{email}")
    List<Qnaview> getQnaviewList(@Param("email") String email);

    /* QnA 등록 (이건 이메일이 아니라 USER_ID 가 있어야 가능 → 그대로 두는 게 안전) */
    @Insert("INSERT INTO QNA_VIEW (INQUIRY_ID, PRODUCT_NAME, INQUIRY_TITLE, INQUIRY_CONTENT, INQUIRY_DATE, STATUS, USER_ID) " +
            "VALUES (QNA_VIEW_seq.nextval, " +
            "#{productName}, #{inquiryTitle}, #{inquiryContent}, SYSDATE, #{status}, " +
            "(SELECT USER_ID FROM USERS WHERE EMAIL = #{email}))")
    int insertQnaview(Qnaview ins);

    /* 구매 이력 차트 리스트 */
    @Select("SELECT TO_CHAR(p.purchased_at, 'YYYY-MM') AS mon, " +
            "       SUM(p.quantity) AS sales_count, " +
            "       SUM(p.product_total_price) AS month_tot " +
            "FROM PURCHASE_HISTORY_FULL p " +
            "JOIN USERS u ON p.USER_ID = u.USER_ID " +
            "WHERE u.EMAIL = #{email} " +
            "  AND p.purchased_at >= DATE '2025-02-01' " +
            "  AND p.purchased_at < DATE '2025-08-01' " +
            "GROUP BY TO_CHAR(p.purchased_at, 'YYYY-MM') " +
            "ORDER BY mon")
    List<PurchaseHistoryFullChart> getPurchaseHistoryFullChartList(@Param("email") String email);

    /* 구매 이력 차트 (단일) */
    @Select("SELECT TO_CHAR(p.purchased_at, 'YYYY-MM') AS mon, " +
            "       SUM(p.quantity) AS sales_count, " +
            "       SUM(p.product_total_price) AS month_tot " +
            "FROM PURCHASE_HISTORY_FULL p " +
            "JOIN USERS u ON p.USER_ID = u.USER_ID " +
            "WHERE u.EMAIL = #{email} " +
            "  AND p.purchased_at >= DATE '2025-02-01' " +
            "  AND p.purchased_at < DATE '2025-08-01' " +
            "GROUP BY TO_CHAR(p.purchased_at, 'YYYY-MM') " +
            "ORDER BY mon")
    List<Chart> getPurchaseHistoryFullChart(@Param("email") String email);

    /* 구매 요약 (이메일 기반) */
    @Select("SELECT SUM(p.quantity) AS total_sales_count, " +
            "       SUM(p.product_total_price) AS total_amount " +
            "FROM PURCHASE_HISTORY_FULL p " +
            "JOIN USERS u ON p.USER_ID = u.USER_ID " +
            "WHERE u.EMAIL = #{email} " +
            "  AND p.purchased_at >= DATE '2025-02-01' " +
            "  AND p.purchased_at < DATE '2025-08-01'")
    List<PurchaseHistoryFullChart> getPurchaseHistorySummary(@Param("email") String email);

}
