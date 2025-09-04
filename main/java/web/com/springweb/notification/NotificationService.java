package web.com.springweb.notification;

import org.springframework.stereotype.Service;
import web.com.springweb.vo.Users;

import jakarta.servlet.http.HttpSession;

@Service
public class NotificationService {
    
    private final NotificationDao dao;
    private final HttpSession session;

    public NotificationService(NotificationDao dao, HttpSession session) {
        this.dao = dao;
        this.session = session;
    }

    public int countUnresolvedInquiries() {
        return dao.countUnresolvedInquiries();
    }

    public int countCalendarApprovals() {
        return dao.countCalendarApprovals();
    }

    public int countCalendarDueSoon() {
        // 세션에서 로그인 사용자 id
        Users u = (Users) session.getAttribute("loginUser");
        int userId = (u != null) ? u.getUserId() : -1; // 비로그인 시 -1
        return dao.countCalendarDueSoon(userId);
    }
    
    // 후순위: 일단 0 리턴
    public int countReturns() { 
    	return dao.countReturns(); 
    }
    /*
    public int countReviewsNoReply() { return dao.countReviewsNoReply(); }
    */
}