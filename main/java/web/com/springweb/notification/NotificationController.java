package web.com.springweb.notification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // 프론트에서 /notifications/summary 호출
    @GetMapping("/notifications/summary")
    public Map<String, Object> summary() {
        Map<String, Object> res = new HashMap<>();
        res.put("inquiryUnresolved",   service.countUnresolvedInquiries());
        res.put("calPendingApprovals", service.countCalendarApprovals());
        res.put("calDueSoon",          service.countCalendarDueSoon());
        // 후순위
        res.put("returnsCount", service.countReturns());
        //res.put("reviewsNoReply",      service.countReviewsNoReply());
        return res;
    }
}