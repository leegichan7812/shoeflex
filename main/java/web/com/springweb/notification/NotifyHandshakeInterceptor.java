package web.com.springweb.notification;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.*;
import web.com.springweb.vo.Users;

@Component
public class NotifyHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse res,
                                   WebSocketHandler h, Map<String,Object> attrs) {
        if (req instanceof ServletServerHttpRequest) {
            HttpSession hs = ((ServletServerHttpRequest) req).getServletRequest().getSession(false);
            if (hs != null) {
                Users u = (Users) hs.getAttribute("loginUser");
                attrs.put("userId",  u != null ? u.getUserId() : "guest");
                attrs.put("userName", u != null ? u.getName() : "고객");
                String grade = (String) hs.getAttribute("accountType");
                attrs.put("accountType", grade != null ? grade : "USER"); // BLACK / USER
            }
        }
        return true;
    }
    @Override public void afterHandshake(ServerHttpRequest a, ServerHttpResponse b, WebSocketHandler c, Exception d) {}
}