package web.com.springweb.Event;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import web.com.springweb.Event.dto.Event;

@Mapper
public interface EventDao {

	/** 미진행(오늘 이후 시작) 전체 이벤트 목록 */
    @Select("""
        SELECT
            CALENDAR_ID  AS calendarId,
            TITLE        AS title,
            START1       AS start1,
            END1         AS end1,
            BRAND_ID     AS brandId,
            BRAND_NAME   AS brandName,
            SLIDE_IMAGE  AS slideImage,
            SLIDE_TITLE  AS slideTitle,
            CREATED_AT   AS createdAt,
            UPDATED_AT   AS updatedAt
        FROM EVENTS
        WHERE TO_DATE(END1, 'YYYY-MM-DD') > TRUNC(SYSDATE)
        ORDER BY TO_DATE(START1, 'YYYY-MM-DD')
    """)
    List<Event> selectUpcomingAll();

    /** 현재 이미지 경로 */
    @Select("""
        SELECT SLIDE_IMAGE
        FROM EVENTS
        WHERE CALENDAR_ID = #{calendarId}
    """)
    String selectSlideImage(@Param("calendarId") int calendarId);

    /**
     * 브랜드/타이틀/이미지 동시 업데이트
     * - slideImagePath, brandId, brandName이 NULL이면 기존 값 유지
     */
    @Update("""
    		UPDATE EVENTS
    		SET
    		  SLIDE_TITLE = NVL(#{slideTitle,jdbcType=VARCHAR}, SLIDE_TITLE),
    		  SLIDE_IMAGE = NVL(#{slideImagePath,jdbcType=VARCHAR}, SLIDE_IMAGE),
    		  BRAND_ID    = NVL(#{brandId,jdbcType=NUMERIC}, BRAND_ID),
    		  BRAND_NAME  = NVL(#{brandName,jdbcType=VARCHAR}, BRAND_NAME),
    		  UPDATED_AT  = SYSDATE
    		WHERE CALENDAR_ID = #{calendarId,jdbcType=NUMERIC}
    		""")
    		int updateBrandAndSlide(
    		    @Param("calendarId") int calendarId,
    		    @Param("brandId") Integer brandId,
    		    @Param("brandName") String brandName,
    		    @Param("slideTitle") String slideTitle,
    		    @Param("slideImagePath") String slideImagePath
    		);

    /** 이미지만 삭제(타이틀/브랜드 유지) */
    @Update("""
        UPDATE EVENTS
           SET SLIDE_IMAGE = NULL,
               UPDATED_AT  = SYSDATE
         WHERE CALENDAR_ID = #{calendarId}
    """)
    int clearSlide(@Param("calendarId") int calendarId);
    
    /** 이벤트 단건 조회 */
    @Select("""
        SELECT
            CALENDAR_ID  AS calendarId,
            TITLE        AS title,
            START1       AS start1,
            END1         AS end1,
            BRAND_ID     AS brandId,
            BRAND_NAME   AS brandName,
            SLIDE_IMAGE  AS slideImage,
            SLIDE_TITLE  AS slideTitle,
            CREATED_AT   AS createdAt,
            UPDATED_AT   AS updatedAt
        FROM EVENTS
        WHERE CALENDAR_ID = #{calendarId}
    """)
    Event selectById(@Param("calendarId") int calendarId);
}