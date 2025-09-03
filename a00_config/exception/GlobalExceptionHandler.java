package web.com.springweb.a00_config.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private boolean isAjax(HttpServletRequest req) {
        String xhr = req.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(xhr);
    }
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        // FK 위반(ORA-02292) 케이스
        if (e.getCause() != null && e.getCause().getMessage().contains("FK_EVENTS_CALENDAR")) {
            return ResponseEntity.badRequest().body("이벤트는 삭제가 불가능합니다. 부득이 하게 삭제 하실려면 공지나 휴가로 수정 후 삭제해주세요.");
        }
        return ResponseEntity.badRequest().body("데이터 무결성 제약 위반: " + e.getMessage());
    }

    // 404 계열도 잡고 싶으면 필요시 추가: NoHandlerFoundException 등
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, Model d, HttpServletRequest req) {
        d.addAttribute("errorMessage", e.getMessage());

        if (isAjax(req)) {
            // AJAX에는 조각(프래그먼트)만 반환
            return "common/errorFragment"; // <div>만 있는 JSP
            // 또는 JSON 원하면 아래처럼:
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        // 전체 페이지 네비/헤더 포함해서 보여줄 때
        return "common/errorPage";
    }
}
	
