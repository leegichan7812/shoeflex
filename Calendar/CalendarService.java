package web.com.springweb.Calendar;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.vo.CalDTO;
import web.com.springweb.vo.Calendar;
@Service
public class CalendarService {
	@Autowired(required=false)
	private CalendarDao dao;
	
	public List<Calendar> getVisibleCalendarList(int userId) {
	    return dao.getVisibleCalendarList(userId);
	}
	public List<Calendar> getCalendarList() {
		return dao.getCalendarList();
	}
	public CalDTO calInsert(Calendar ins) {
		return new CalDTO(dao.calInsert(ins)>0?"등록성공":"등록실패", getCalendarList()) ;
	}
	public CalDTO calUpdate(Calendar upt) {
		return new CalDTO(dao.calUpdate(upt)>0?"수정성공":"수정실패", getCalendarList());
	}
	public CalDTO calDelete(int id) {
		return new CalDTO(dao.calDelete(id)>0?"삭제성공":"삭제실패", getCalendarList());
	}
	// Black 등급 가지고 오기
	public String getAdminGrade(int userId) {
		return dao.getAdminGrade(userId);
	}
	// 기존 일정 조회
	public Calendar getCalendarById(int id) {
	    return dao.getCalendarById(id);
	}
	// 승인 대기 일정 조회
	public List<Calendar> getPendingApprovals() {
	    return dao.getPendingApprovals();
	}
	// 승인 처리 API
	public CalDTO approveCalendar(int id) {
	    int result = dao.approveCalendar(id);
	    return new CalDTO(result > 0 ? "승인 완료" : "승인 실패", getCalendarList());
	}
}
