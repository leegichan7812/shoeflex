package web.com.springweb.a00_config.aop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.vo.ExceptionLog;
import web.com.springweb.vo.ExceptionLogSearchDto;

@Service
public class ExceptionLogServiceImpl implements ExceptionLogService {
	@Autowired(required = false)
    private ExceptionLogDao dao;
	
	// 목록 출력용
    @Override
    public List<ExceptionLog> getExceptionLogList(ExceptionLogSearchDto sch) {
        return dao.getExceptionLogList(sch);
    }
    // 페이징 계산
    @Override
    public int getTotalCount(ExceptionLogSearchDto sch) {
        return dao.getTotalCount(sch);
    }
    
    // 🔽 단건 조회 메서드 추가
    public ExceptionLog getLogById(int logId) {
        return dao.getLogById(logId);
    }
}
