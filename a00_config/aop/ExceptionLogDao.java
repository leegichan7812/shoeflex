package web.com.springweb.a00_config.aop;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import web.com.springweb.vo.ExceptionLog;
import web.com.springweb.vo.ExceptionLogSearchDto;

@Mapper
public interface ExceptionLogDao {
	@Insert("""
	    INSERT INTO EXCEPTION_LOG 
	    (LOG_ID, OCCURRED_AT, CLASS_NAME, METHOD_NAME, EXCEPTION_TYPE, EXCEPTION_MESSAGE,
	     ERROR_TYPE, EXECUTION_TIME_MS, USER_EMAIL, USER_NAME)
	    VALUES (
	        EXCEPTION_LOG_SEQ.NEXTVAL, SYSDATE, #{className, jdbcType=VARCHAR}, #{methodName, jdbcType=VARCHAR},
	        #{exceptionType, jdbcType=VARCHAR}, #{exceptionMessage, jdbcType=VARCHAR},
	        #{errorType, jdbcType=VARCHAR}, #{executionTimeMs, jdbcType=INTEGER},
	        #{userEmail, jdbcType=VARCHAR}, #{userName, jdbcType=VARCHAR}
	    )
	""")
	void insertLog(ExceptionLog log);
	
	// 예외 로그 목록 조회 (페이징 + 검색)
    List<ExceptionLog> getExceptionLogList(ExceptionLogSearchDto sch);

    // 총 건수 조회
    int getTotalCount(ExceptionLogSearchDto sch);
    
    
    @Select("SELECT * FROM EXCEPTION_LOG WHERE LOG_ID = #{logId}")
    ExceptionLog getLogById(int logId);
}