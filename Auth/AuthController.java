package web.com.springweb.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.com.springweb.vo.Users;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    // 아이디 찾기: name + phone => email 반환
    @PostMapping("findId")
    public ResponseEntity<?> findId(@RequestParam String name,
                                    @RequestParam String phone) {
        String email = authService.findEmailByNamePhone(name, phone);
        Map<String,Object> resp = new HashMap<>();
        if (email != null) {
            resp.put("success", true);
            resp.put("email", email);
        } else {
            resp.put("success", false);
            resp.put("msg", "일치하는 정보가 없습니다.");
        }
        return ResponseEntity.ok(resp);
    }

    // 비밀번호 초기화(임시 비번 메일 발송)
    @PostMapping("resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String name,
                                           @RequestParam String phone) {
        Map<String,Object> resp = new HashMap<>();
        try {
            boolean ok = authService.resetPasswordAndSendMail(email, name, phone);
            resp.put("success", ok);
            resp.put("msg", ok ? "임시 비밀번호가 발송되었습니다." : "일치하는 정보가 없습니다.");
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("msg", "처리 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(resp);
        }
    }
}