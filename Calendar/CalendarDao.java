package web.com.springweb.Calendar;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import web.com.springweb.vo.Calendar;

@Mapper
public interface CalendarDao {
	// 개인 일정 보기
	@Select("""
	    SELECT ID, TITLE, WRITER, 
	           START1 AS "START", 
	           END1 AS "END", 
	           CONTENT,
	           BACKGROUND_COLOR, TEXT_COLOR, ALL_DAY, STATUS,
	           IS_PUBLIC, USER_ID
	    FROM CALENDAR
	    WHERE 
		       IS_PUBLIC = 'Y'
		       OR USER_ID = #{userId}
	""")
	List<Calendar> getVisibleCalendarList(@Param("userId") int userId);
	// 전체 일정 리스트
	@Select("SELECT ID, TITLE, WRITER, START1 \"START\", END1 \"END\", CONTENT, \r\n"
			+ "BACKGROUND_COLOR, TEXT_COLOR, ALL_DAY, STATUS, IS_PUBLIC, USER_ID\r\n"
			+ "FROM CALENDAR")
	List<Calendar> getCalendarList();
	// Black 등급 가지고 오기
	@Select("SELECT GRADE FROM ADMINS WHERE ADMIN_ID = #{userId}")
	String getAdminGrade(@Param("userId") int userId);
	// 등록
	@Insert("INSERT INTO CALENDAR (\r\n"
			+ "        ID, TITLE, WRITER, START1, END1, CONTENT,\r\n"
			+ "        BACKGROUND_COLOR, TEXT_COLOR, ALL_DAY,\r\n"
			+ "        STATUS, IS_PUBLIC, USER_ID\r\n"
			+ "    ) VALUES (\r\n"
			+ "        CALENDAR_SEQ.NEXTVAL, #{title}, #{writer}, #{start}, #{end}, #{content},\r\n"
			+ "        #{backgroundColor}, #{textColor}, #{allDay},\r\n"
			+ "        #{status}, #{isPublic}, #{userId}\r\n"
			+ "    )")
	int calInsert(Calendar ins);
	// 수정
	@Update("	UPDATE CALENDAR\r\n"
			+ "	  SET TITLE = #{title},\r\n"
			+ "	      WRITER = #{writer},\r\n"
			+ "	      START1 = #{start},\r\n"
			+ "	      END1 = #{end},\r\n"
			+ "	      CONTENT = #{content},\r\n"
			+ "	      BACKGROUND_COLOR = #{backgroundColor},\r\n"
			+ "	      TEXT_COLOR = #{textColor},\r\n"
			+ "	      ALL_DAY = #{allDay},\r\n"
			+ "	      STATUS = #{status},\r\n"
			+ "	      IS_PUBLIC = #{isPublic},\r\n"
			+ "	      USER_ID = #{userId, jdbcType=INTEGER}\r\n"
			+ "	 WHERE ID = #{id}")
	int calUpdate(Calendar upt);
	// 삭제
	@Delete("DELETE FROM CALENDAR WHERE ID = #{id}")
	int calDelete(@Param("id") int id);
	// 기존 일정 조회
	@Select("SELECT * FROM calendar WHERE id = #{id}")
	Calendar getCalendarById(@Param("id") int id);
	// 승인 대기 일정 조회
	@Select("""
	    SELECT ID, TITLE, WRITER,
	           START1 AS "START",
	           END1 AS "END",
	           CONTENT, BACKGROUND_COLOR, TEXT_COLOR,
	           ALL_DAY, STATUS, IS_PUBLIC, USER_ID
	    FROM CALENDAR
	    WHERE IS_PUBLIC = 'N'
	""")
	List<Calendar> getPendingApprovals();
	// 승인 처리 API
	@Update("UPDATE calendar SET IS_PUBLIC = 'Y' WHERE id = #{id}")
	int approveCalendar(@Param("id") int id);
}
