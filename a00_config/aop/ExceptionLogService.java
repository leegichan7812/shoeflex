package web.com.springweb.a00_config.aop;

import java.util.List;

import web.com.springweb.vo.ExceptionLog;
import web.com.springweb.vo.ExceptionLogSearchDto;

public interface ExceptionLogService {
	List<ExceptionLog> getExceptionLogList(ExceptionLogSearchDto sch);
    int getTotalCount(ExceptionLogSearchDto sch);
}
