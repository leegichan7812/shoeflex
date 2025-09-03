package web.com.springweb.Calendar;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import web.com.springweb.vo.CalDTO;
import web.com.springweb.vo.Calendar;
import web.com.springweb.vo.Users;

@Controller
public class CalendarController {
	@Autowired(required=false)
	private CalendarService service;
	
	@GetMapping("calendar")
	public String getCalendar() {
		return "/pages/calendar";
	}
	// 개인 일정 및 공지
	@GetMapping("calListVisible")
	public ResponseEntity<?> calListVisible(HttpSession session) {
	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (loginUser == null) return ResponseEntity.badRequest().body("로그인 필요");

	    return ResponseEntity.ok(service.getVisibleCalendarList(loginUser.getUserId()));
	}
	// 전체 일정 가지고 오기
	@GetMapping("/calListAdmin")
	public ResponseEntity<?> calListAdmin(HttpSession session) {
	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (loginUser == null) return ResponseEntity.status(401).body("로그인 필요");

	    String grade = service.getAdminGrade(loginUser.getUserId());
	    if ("BLACK".equals(grade)) {
	        return ResponseEntity.ok(service.getCalendarList());
	    }
	    return ResponseEntity.status(403).body("권한 없음"); // ← 중요
	}
	
	@PostMapping("calInsert")
	public ResponseEntity<?> calInsert(Calendar ins){
		return ResponseEntity.ok(service.calInsert(ins));
	}
	@PostMapping("calUpdate")
	public ResponseEntity<?> calUpdate(Calendar upt, HttpSession session) {
	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (loginUser == null) return ResponseEntity.badRequest().body("로그인 필요");

	    String grade = service.getAdminGrade(loginUser.getUserId());
	    Calendar existing = service.getCalendarById(upt.getId());

	    // GREEN 등급인데 작성자 다르면 거절
	    if ("GREEN".equals(grade) && !loginUser.getName().equals(existing.getWriter())) {
	        return ResponseEntity.badRequest().body(new CalDTO("본인 일정만 수정 가능합니다", service.getVisibleCalendarList(loginUser.getUserId())));
	    }

	    return ResponseEntity.ok(service.calUpdate(upt));
	}
	@PostMapping("calDelete")
	public ResponseEntity<?> calDelete(@RequestParam int id, HttpSession session) {
	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (loginUser == null) return ResponseEntity.badRequest().body(Map.of("msg","로그인 필요"));

	    String grade = service.getAdminGrade(loginUser.getUserId());
	    Calendar existing = service.getCalendarById(id);
	    if (existing == null) {
	        return ResponseEntity.badRequest().body(Map.of("msg","존재하지 않는 일정입니다."));
	    }
	    if (!"BLACK".equals(grade) && loginUser.getUserId() != existing.getUserId()) {
	        return ResponseEntity.badRequest().body(
	            new CalDTO("본인 일정만 삭제 가능합니다", service.getVisibleCalendarList(loginUser.getUserId()))
	        );
	    }

	    try {
	        // 성공도 CalDTO로 내려주면 프론트에서 동일 로직으로 처리 가능
	        return ResponseEntity.ok(service.calDelete(id));
	    } catch (DataIntegrityViolationException ex) {
	        // FK 제약(이벤트가 참조 중) 사용자 메시지
	        return ResponseEntity.badRequest().body(
	            Map.of("msg", "이벤트는 삭제가 불가능합니다. 부득이 하게 삭제 하실려면 공지나 휴가로 수정 후 삭제해주세요.")
	        );
	    }
	}
	// 승인 대기 일정 조회
	@GetMapping("/pendingApprovals")
	@ResponseBody
	public ResponseEntity<?> pendingApprovals(HttpSession session) {
	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        return ResponseEntity.status(401).body("로그인 필요");
	    }
	    String grade = service.getAdminGrade(loginUser.getUserId());
	    if (!"BLACK".equals(grade)) {
	        return ResponseEntity.status(403).body("권한 없음");
	    }
	    return ResponseEntity.ok(service.getPendingApprovals()); // List<Calendar>
	}
	// 승인 처리 API
	@PostMapping("/approveCalendar")
	@ResponseBody
	public ResponseEntity<?> approveCalendar(@RequestParam("id") int id, HttpSession session) {
	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (!"BLACK".equals(service.getAdminGrade(loginUser.getUserId()))) {
	        return ResponseEntity.status(403).body("권한 없음");
	    }
	    return ResponseEntity.ok(service.approveCalendar(id));
	}
}
