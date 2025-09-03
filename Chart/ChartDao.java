package web.com.springweb.Chart;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import web.com.springweb.Chart.dto.ChartMonthlyDto;
import web.com.springweb.Chart.dto.ChartYearlyDto;


@Mapper
public interface ChartDao {

    // 1. 최근 3개월 요일별 판매건수 및 매출금액
	@Select("""
	        SELECT
	            TO_CHAR(o.ORDER_DATE, 'YYYY-MM') AS month,
	            TO_CHAR(o.ORDER_DATE, 'D') AS dow,
	            TO_CHAR(o.ORDER_DATE, 'DY', 'NLS_DATE_LANGUAGE=KOREAN') AS dow_name,
	            SUM(oi.QUANTITY) AS count,
	            SUM(oi.QUANTITY * p.PRICE) AS sales
	        FROM ORDERS o
	        JOIN ORDER_ITEMS oi ON o.ORDER_ID = oi.ORDER_ID
	        JOIN PRODUCT_COLOR_SIZES pcs ON oi.PRODUCT_COLOR_SIZE_ID = pcs.PRODUCT_COLOR_SIZE_ID
	        JOIN PRODUCT_COLORS pc ON pcs.PRODUCT_COLOR_ID = pc.PRODUCT_COLOR_ID
	        JOIN PRODUCTS p ON pc.PRODUCT_ID = p.PRODUCT_ID
	        WHERE o.ORDER_DATE >= TRUNC(ADD_MONTHS(SYSDATE, -2), 'MM')
	          AND o.ORDER_DATE < TRUNC(ADD_MONTHS(SYSDATE, 1), 'MM')
	          AND o.ORDER_STATUS = '주문완료'
	        GROUP BY
	            TO_CHAR(o.ORDER_DATE, 'YYYY-MM'),
	            TO_CHAR(o.ORDER_DATE, 'D'),
	            TO_CHAR(o.ORDER_DATE, 'DY', 'NLS_DATE_LANGUAGE=KOREAN')
	        ORDER BY
	            month, TO_NUMBER(TO_CHAR(o.ORDER_DATE, 'D'))
	    """)
	    List<Map<String, Object>> getRecent3MonthDaySales();
	
	// 2. 분기별 브랜드 매출액 차트
    @Select("""
    	    SELECT 
			    b.BRAND_NAME AS brandName,
			    b.BRAND_IMG AS brandImg,  -- << 추가!
			    SUM(oi.QUANTITY * p.PRICE) AS salesTotal,
			    SUM(oi.QUANTITY) AS itemCount
			FROM ORDER_ITEMS oi
			JOIN PRODUCT_COLOR_SIZES pcs ON oi.PRODUCT_COLOR_SIZE_ID = pcs.PRODUCT_COLOR_SIZE_ID
			JOIN PRODUCT_COLORS pc ON pcs.PRODUCT_COLOR_ID = pc.PRODUCT_COLOR_ID
			JOIN PRODUCTS p ON pc.PRODUCT_ID = p.PRODUCT_ID
			JOIN BRANDS b ON p.BRAND_ID = b.BRAND_ID
			JOIN ORDERS o ON oi.ORDER_ID = o.ORDER_ID
			WHERE o.ORDER_DATE >= #{startDate}
			  AND o.ORDER_DATE <= #{endDate}
			  AND o.ORDER_STATUS = '주문완료'
			GROUP BY b.BRAND_NAME, b.BRAND_IMG   -- << GROUP BY에 추가!
			ORDER BY salesTotal DESC
    	""")
        List<Map<String, Object>> getBrandSalesPie(
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate
        );
    
    // 전체 탈퇴 이유 차트
    @Select("""
    		SELECT
			  r.reason_id     AS reason_id,
			  r.reason_text   AS reason_text,
			  COUNT(*)        AS cnt
			FROM withdrawal_log l
			JOIN withdrawal_reason_master r
			  ON r.reason_id = l.reason_id
			WHERE r.use_yn = 'Y'
			GROUP BY r.reason_id, r.reason_text
			ORDER BY cnt DESC
    		""")
    List<Map<String, Object>> getReasonCountAll();
    // 최근 N주 주간 집계
    @Select("""
		WITH weeks AS (
		  SELECT TRUNC(SYSDATE,'IW') - 7*(LEVEL-1) AS week_start
		  FROM dual CONNECT BY LEVEL <= #{weeks}
		)
		SELECT
		  TO_CHAR(w.week_start,'YYYY-MM-DD') AS week,
		  r.reason_text                       AS reason,
		  NVL(COUNT(l.log_id),0)              AS cnt
		FROM weeks w
		JOIN withdrawal_reason_master r ON r.use_yn='Y'
		LEFT JOIN withdrawal_log l
		  ON l.reason_id = r.reason_id
		 AND TRUNC(l.withdrawal_date,'IW') = w.week_start
		GROUP BY w.week_start, r.reason_text
		ORDER BY w.week_start, r.reason_text
		""")
	List<Map<String,Object>> getWithdrawalWeekly(@Param("weeks") int weeks);
    /* 전체 기간(올타임) 기타 상세사유 TOP N */
    @Select("""
	    SELECT * FROM (
	      SELECT
	        REGEXP_REPLACE(UPPER(TRIM(CAST(l.etc_reason AS VARCHAR2(4000)))), '[[:space:]]+', ' ') AS etc_text,
	        COUNT(*) AS cnt
	      FROM TWO.WITHDRAWAL_LOG l
	      WHERE l.reason_id = 5
	        AND l.etc_reason IS NOT NULL
	        AND LENGTH(TRIM(CAST(l.etc_reason AS VARCHAR2(4000)))) > 0
	      GROUP BY REGEXP_REPLACE(UPPER(TRIM(CAST(l.etc_reason AS VARCHAR2(4000)))), '[[:space:]]+', ' ')
	      ORDER BY cnt DESC
	    )
	    WHERE ROWNUM <= #{limit}
	  """)
	  List<Map<String,Object>> getEtcReasonsTopAll(@Param("limit") int limit);
    
    
    /* 브랜드별 최근 12개월 판매 건수 */
    List<ChartMonthlyDto> getMonthlySalesCount(List<String> brandNames);
    
    
    
    /* 브랜드 연도별 판매금액 */
    List<ChartYearlyDto> getYearlySalesAmount(List<String> brandNames);
    
    
}
