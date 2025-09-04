package web.com.springweb.Slide;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import web.com.springweb.Slide.dto.Slide;

@Mapper
public interface SlideDao {
	
	@Select("""
        SELECT
            e.CALENDAR_ID                               AS calendarId,
            e.TITLE                                     AS title,
            e.START1                                    AS start1,
            e.END1                                      AS end1,
            e.BRAND_ID                                  AS brandId,
            NVL(e.BRAND_NAME, b.BRAND_NAME)             AS brandName,
            NVL(e.SLIDE_IMAGE, b.BRAND_IMG_SLIDE)       AS slideImage,
            NVL(e.SLIDE_TITLE, b.BRAND_SLIDE_TITLE)     AS slideTitle,
            b.BRAND_IMG                                 AS brandImg
        FROM EVENTS e
        LEFT JOIN BRANDS b
               ON b.BRAND_ID = e.BRAND_ID
        WHERE TRUNC(SYSDATE) BETWEEN TO_DATE(e.START1, 'YYYY-MM-DD')
                         AND TO_DATE(e.END1,   'YYYY-MM-DD')
        ORDER BY TO_DATE(e.START1, 'YYYY-MM-DD'), e.CALENDAR_ID
    """)
    List<Slide> selectSlidesForHomeWithBrand();
}
