package web.com.springweb.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.vo.Users;


@Service
public class userService {
	@Autowired(required = false)
	private userDao dao;
	
	private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder =
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    public Users selectUser(Users login){
        Users dbUser = dao.findByEmail(login.getEmail());
        if (dbUser == null) return null;

        String dbPw = dbUser.getPassword();
        String raw  = login.getPassword();

        if (isBcrypt(dbPw)) {
            return encoder.matches(raw, dbPw) ? dbUser : null;
        } else {
            // 레거시(평문) 호환
            if (safeEquals(raw, dbPw)) {
                // ✅ 로그인 성공으로 간주하고 즉시 업그레이드
                String newHash = encoder.encode(raw);
                dao.updatePasswordByUserId(dbUser.getUserId(), newHash);
                dbUser.setPassword(newHash);
                return dbUser;
            } else {
                return null;
            }
        }
    }

    public String insertJoin(Users ins) {
        ins.setPassword(encoder.encode(ins.getPassword())); // 신규가입은 무조건 BCrypt
        return dao.insertJoin(ins)>0 ? "등록성공" : "등록실페";
    }

    private boolean isBcrypt(String v){
        return v != null && v.length() >= 60 && v.startsWith("$2");
    }
    private boolean safeEquals(String a, String b){
        return a != null && a.equals(b);
    }
    
	// 이메일 중복체크
	public boolean checkId(String email) {
	    return dao.checkId(email) == 0;  // 중복이 없으면 true (가입 가능)
	}
	
}
