package web.com.springweb.Support;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import web.com.springweb.vo.CategoryCount;
import web.com.springweb.vo.Faq;
import web.com.springweb.vo.Notice;

@Mapper
public interface SupportDao {

    /* ===== 공지(Notice) ===== */

    // 공지 Top 5
    @Select("""
    SELECT *
    FROM (
      SELECT *
      FROM NOTICE
      ORDER BY 
        CASE WHEN IS_PINNED = 'Y' THEN 0 ELSE 1 END,  -- 🔹 고정공지 먼저
        CREATED_AT DESC                               -- 🔹 최신순 정렬
    )
    WHERE ROWNUM <= 5
    """)
    List<Notice> getTop5Notice();

    // 공지 키워드 검색 (비페이징)
    @Select("""
        SELECT 
            notice_id,
            title,
            content,
            is_popup,
            is_pinned,
            view_count,
            created_at
        FROM NOTICE
        WHERE title LIKE '%' || #{keyword} || '%'
        ORDER BY 
            CASE WHEN is_pinned = 'Y' THEN 0 ELSE 1 END,
            created_at DESC
    """)
    List<Notice> searchNotices(@Param("keyword") String keyword);

    // 공지 상세
    @Select("SELECT * FROM NOTICE WHERE notice_id = #{noticeId}")
    Notice getNoticeById(@Param("noticeId") int noticeId);

    // 공지 조회수 증가
    @Update("UPDATE NOTICE SET view_count = view_count + 1 WHERE notice_id = #{noticeId}")
    void increaseViewCount(@Param("noticeId") int noticeId);

    // 공지 등록
    @Insert("""
        INSERT INTO NOTICE (
          title,
          content,
          is_popup,
          is_pinned,
          view_count,
          created_at
        ) VALUES (
          #{title},
          #{content},
          #{isPopup},
          #{isPinned},
          0,
          SYSDATE
        )
    """)
    int insertNotice(Notice notice);

    // 공지 수정
    @Update("""
        UPDATE NOTICE
        SET title     = #{title},
            content   = #{content},
            is_popup  = #{isPopup,jdbcType=CHAR},
            is_pinned = #{isPinned,jdbcType=CHAR}
        WHERE notice_id = #{noticeId}
    """)
    int updateNotice(Notice notice);
    
 // 최신 팝업 공지 1건 조회
    @Select("""
    	    SELECT NOTICE_ID, TITLE, CONTENT, IS_POPUP, IS_PINNED, VIEW_COUNT, CREATED_AT
    	    FROM (
    	        SELECT NOTICE_ID, TITLE, CONTENT, IS_POPUP, IS_PINNED, VIEW_COUNT, CREATED_AT
    	        FROM TWO.NOTICE
    	        WHERE IS_POPUP = 'Y'
    	        ORDER BY CREATED_AT DESC, NOTICE_ID DESC
    	    )
    	    WHERE ROWNUM = 1
    	""")
    	Notice selectLatestPopup();

    // 공지 삭제
    @Delete("DELETE FROM NOTICE WHERE notice_id = #{noticeId}")
    int deleteNotice(@Param("noticeId") Long noticeId);

    // 공지 총 건수
    @Select("""
    <script>
    SELECT COUNT(*)
    FROM NOTICE
    WHERE title LIKE '%' || #{keyword} || '%'
    </script>
    """)
    int countNotices(@Param("keyword") String keyword);

    // 공지 페이지 조회 (ROWNUM 페이징)
    @Select("""
    <script>
    SELECT *
    FROM (
      SELECT inner_q.*, ROWNUM AS rnum
      FROM (
        SELECT
          notice_id, title, content, is_popup, is_pinned, view_count, created_at
        FROM NOTICE
        WHERE title LIKE '%' || #{keyword} || '%'
        ORDER BY
          CASE WHEN is_pinned = 'Y' THEN 0 ELSE 1 END,
          created_at DESC
      ) inner_q
      WHERE ROWNUM &lt;= (#{offset} + #{limit})
    )
    WHERE rnum &gt; #{offset}
    </script>
    """)
    List<Notice> findNoticePage(
        @Param("keyword") String keyword,
        @Param("offset")  int offset,
        @Param("limit")   int limit
    );


    /* ===== FAQ ===== */

    // FAQ 전체 조회
    @Select("""
        SELECT faq_id, category, question, answer, created_at
        FROM FAQ
        ORDER BY faq_id DESC
    """)
    List<Faq> getAllFaq();

    // FAQ 키워드 검색
    @Select("""
        SELECT faq_id, category, question, answer, created_at
        FROM FAQ
        WHERE question LIKE '%' || #{keyword} || '%'
           OR answer LIKE '%' || #{keyword} || '%'
        ORDER BY faq_id DESC
    """)
    List<Faq> searchFaqByKeyword(@Param("keyword") String keyword);

    // FAQ 카테고리 검색
    @Select("""
        SELECT * 
        FROM FAQ
        WHERE category = #{category}
        ORDER BY created_at DESC
    """)
    List<Faq> searchFaqByCategory(@Param("category") String category);

    // FAQ 단건 조회
    @Select("SELECT * FROM FAQ WHERE faq_id = #{faqId}")
    Faq getFaqById(Long faqId);

    // FAQ Top5 조회수
    @Select("""
        SELECT * FROM (
            SELECT * FROM FAQ
            ORDER BY VIEW_COUNT DESC
        )
        WHERE ROWNUM <= 5
    """)
    List<Faq> getTop5ViewCountFaq();

    // FAQ 등록
    @Insert("""
        INSERT INTO FAQ (
            faq_id, category, question, answer, created_at
        ) VALUES (
            FAQ_SEQ.NEXTVAL, #{category}, #{question}, #{answer}, SYSDATE
        )
    """)
    int insertFaq(Faq faq);

    // FAQ 수정
    @Update("""
        UPDATE FAQ
        SET category = #{category},
            question = #{question},
            answer = #{answer}
        WHERE faq_id = #{faqId}
    """)
    int updateFaq(Faq faq);

    // FAQ 삭제
    @Delete("DELETE FROM FAQ WHERE faq_id = #{faqId}")
    int deleteFaq(Long faqId);

    // FAQ 총 건수
    @Select("""
    <script>
    SELECT COUNT(*)
    FROM FAQ
    WHERE 1=1
    <if test="keyword != null and keyword != ''">
      AND (question LIKE '%' || #{keyword} || '%'
           OR answer   LIKE '%' || #{keyword} || '%')
    </if>
    <if test="category != null and category != ''">
      AND category = #{category}
    </if>
    </script>
    """)
    int countFaq(
        @Param("keyword")  String keyword,
        @Param("category") String category
    );

    // FAQ 페이지 조회 (ROWNUM 페이징)
    @Select("""
    <script>
    SELECT *
    FROM (
      SELECT inner_q.*, ROWNUM AS rnum
      FROM (
        SELECT faq_id, category, question, answer, created_at
        FROM FAQ
        WHERE 1=1
        <if test="keyword != null and keyword != ''">
          AND (question LIKE '%' || #{keyword} || '%'
               OR answer   LIKE '%' || #{keyword} || '%')
        </if>
        <if test="category != null and category != ''">
          AND category = #{category}
        </if>
        ORDER BY faq_id DESC
      ) inner_q
      WHERE ROWNUM &lt;= (#{offset} + #{limit})
    )
    WHERE rnum &gt; #{offset}
    </script>
    """)
    List<Faq> findFaqPage(
        @Param("keyword")  String keyword,
        @Param("category") String category,
        @Param("offset")   int offset,
        @Param("limit")    int limit
    );
    
    
    
    
}
