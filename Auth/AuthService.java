package web.com.springweb.Auth;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // 시큐리티 미사용이면 직접 의존성 추가
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class AuthService {

    @Autowired
    private AuthDao authDao;

    @Autowired(required = false)
    private JavaMailSender mailSender; // 메일 설정 안 했으면 null일 수 있음

    private final SecureRandom random = new SecureRandom();

    public String findEmailByNamePhone(String name, String phone) {
        return authDao.findEmailByNamePhone(name, phone);
    }

    public boolean resetPasswordAndSendMail(String email, String name, String phone) {
        // 1) 사용자 검증
        Integer userId = authDao.findUserIdByEmailNamePhone(email, name, phone);
        if (userId == null) return false;

        // 2) 임시 비밀번호 생성
        String tempPw = generateTempPassword(10);

        // 3) 해시 저장(권장: BCrypt)
        String encoded = new BCryptPasswordEncoder().encode(tempPw);
        int updated = authDao.updatePassword(userId, encoded);
        if (updated == 0) return false;

        // 4) 메일 발송
        if (mailSender != null) {
            try {
                MimeMessage mm = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mm, true, "UTF-8");

                helper.setTo(email);

                // 보내는 사람 이메일 + 표시 이름(쇼핑몰명)
                helper.setFrom("noreply@shoeflex.com", "SHOEFLEX");

                helper.setSubject("[SHOEFLEX] 임시 비밀번호 안내");

                String html = """
                    <div style="font-family:Segoe UI,Arial,sans-serif;font-size:14px;line-height:1.6">
                      <p>안녕하세요, <strong>SHOEFLEX</strong>입니다.</p>
                      <p>임시 비밀번호는 <strong>%s</strong> 입니다.</p>
                      <p>보안을 위해 로그인 후 <strong>반드시 비밀번호를 변경</strong>해주세요.</p>
                    </div>
                """.formatted(tempPw);

                helper.setText(html, true); // HTML 본문

                mailSender.send(mm);
            } catch (Exception e) {
                // 전송 실패해도 비번은 이미 변경되었으니, 필요시 롤백/재발송 로직 고려
                e.printStackTrace();
            }
        } else {
            // 메일 설정이 없으면 콘솔 로그로 대체하거나 예외 처리
            System.out.println("TEMP PW for " + email + " = " + tempPw);
        }
        return true;
    }

    private String generateTempPassword(int len) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789!@#$%^&*";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}