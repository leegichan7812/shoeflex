package web.com.springweb.Users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import web.com.springweb.vo.Users;

@Controller
public class userController {
	@Autowired(required = false)
	private userService service;
	
	// 로그인
	// http://localhost:7075/glogin
	@GetMapping("glogin")
	public String loginPage() {
	    return "pages/login"; // 로그인 폼으로 이동
	}
	@PostMapping("glogin")
	@ResponseBody
	public Map<String, String> loginProc(Users loginReq, HttpSession session) {
	    Map<String, String> result = new HashMap<>();

	    Users user = service.selectUser(loginReq);
	    
	    if (user != null) {
	        // ✅ accountType 로그 출력
	        System.out.println(">>> 로그인한 사용자: " + user.getEmail());
	        System.out.println(">>> accountType: " + user.getAccountType());

	        session.setAttribute("loginUser", user);

	        if("ADMIN".equals(user.getAccountType())) {
	            result.put("result", "admin");
	        } else {
	            result.put("result", "user");
	        }
	    } else {
	        result.put("result", "fail");
	    }
	    return result;
	}
	

	
	// 회원가입
	// http://localhost:7075/insertJoin
	@GetMapping("insertJoin")
	public String joinForm() {
		return "pages/join";
	}
	@PostMapping("insertJoin")
	@ResponseBody
	public Map<String, Object> insertJoinAjax(Users ins) {
	    Map<String, Object> resp = new HashMap<>();
	    try {
	        String r = service.insertJoin(ins); // DB에 바로 INSERT 시도
	        boolean ok = "등록성공".equals(r);
	        resp.put("success", ok);
	        if (!ok) resp.put("msg", "회원가입 실패");
	    } catch (org.springframework.dao.DuplicateKeyException e) {
	        // ORA-00001 등 유니크 제약 위반 시
	        resp.put("success", false);
	        resp.put("msg", "이미 가입된 이메일입니다.");
	    }
	    return resp;
	}
	// 이메일 중복체크
	@RequestMapping("checkId")
	@ResponseBody
	public boolean checkId(@RequestParam("email") String email) {
		if (email == null || email.trim().isEmpty()) return false;
	    return service.checkId(email.trim());
	}
	
	
	
	
	
}
