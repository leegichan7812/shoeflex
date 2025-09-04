package web.com.springweb.Inquiry;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import web.com.springweb.vo.ProductInquiryView;

@RestController
@RequestMapping("/api/inquiries")
public class InquiryController {
    
    @Autowired
    private InquiryService inquiryService;

    // 상태별 문의 목록(페이징)
    @GetMapping("/statuses")
    public Map<String, Object> getInquiryStatuses(
    		@RequestParam(name="page", defaultValue = "1") int page,
    	    @RequestParam(name="pageSize", defaultValue = "10") int pageSize,
    	    @RequestParam(name="status", required = false) String status
    ) {
        // 빈 스트링 또는 "전체" 등은 null로 치환
        if (status != null && (status.trim().isEmpty() || "전체".equals(status.trim()))) status = null;
        return inquiryService.getPagedInquiryStatuses(page, pageSize, status);
    }

    // 답변 등록/수정
    @PostMapping("/answer")
    public Map<String, Object> insertAnswer(
        @RequestParam int inquiryId,
        @RequestParam String answerContent
    ) {
        int adminId = 1; // 실제 구현시 세션에서 꺼내세요
        inquiryService.addOrUpdateAnswer(inquiryId, answerContent, adminId);
        // Map으로 반환하면 프론트 연동이 더 편함
        return Map.of("success", true, "message", "답변 저장 완료");
    }
    
    @GetMapping("/detail")
    public ProductInquiryView getInquiryDetail(@RequestParam int inquiryId) {
        return inquiryService.getInquiryDetail(inquiryId);
    }

    // 답변 삭제
    @PostMapping("/answerDelete")
    public Map<String, Object> deleteAnswer(@RequestParam int inquiryId) {
        inquiryService.deleteAnswerByInquiryId(inquiryId);
        return Map.of("success", true, "message", "답변 삭제 완료");
    }

    // 조회 관리자(admin) 변경
    @PostMapping("/updateViewerAdmin")
    public Map<String, Object> updateViewerAdmin(
        @RequestParam Long inquiryId,
        @RequestParam(required = false) Long adminId
    ) {    
        inquiryService.updateViewerAdmin(inquiryId, adminId);
        return Map.of("success", true, "message", "확인 관리자 변경 완료");
    }
    
    @GetMapping("/recent-unchecked")
    public List<ProductInquiryView> getRecentUnchecked() {
        return inquiryService.getRecentUnchecked(5); // 5건만 반환
    }
}
