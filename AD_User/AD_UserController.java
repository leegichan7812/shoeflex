package web.com.springweb.AD_User;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import web.com.springweb.AD_User.dto.Users;

@Controller
public class AD_UserController {

    @Autowired
    private AD_UserService service;

    // 페이지 껍데기 반환
    @GetMapping("/admin/ad_Users")
    public String AdminPage() {
        return "pages/ad_Users"; 
    }

    // ✅ 이메일로 조회 (데이터만 반환, Ajax에서 사용)
    @GetMapping("/admin/ad_Users/data")
    @ResponseBody
    public Map<String, Object> searchUserData(@RequestParam("email") String email) {
        Map<String, Object> result = new HashMap<>();

        if (email != null && !email.isEmpty()) {
            result.put("member", service.getUsersByEmail(email));              // 회원 정보
            result.put("qnalist", service.getQnaviewList(email));              // QnA
            result.put("reviewlist", service.getReviewWithFilesList(email));   // 리뷰
            result.put("chartlist", service.getPurchaseHistoryFullChartList(email)); // 구매내역
        }

        return result; // JSON 응답
    }

    // ✅ 차트만 별도로 조회
    @GetMapping("/chartJson")
    public ResponseEntity<?> chartJson(@RequestParam("email") String email) {
        return ResponseEntity.ok(service.getPurchaseHistoryFullChart(email));
    }
}
