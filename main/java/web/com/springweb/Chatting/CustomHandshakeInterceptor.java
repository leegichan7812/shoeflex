package web.com.springweb.Chatting;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;

public class CustomHandshakeInterceptor implements HandshakeInterceptor{
	  @Override
	    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
	                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
	        if (request instanceof ServletServerHttpRequest sri) {
	            HttpServletRequest req = sri.getServletRequest();
	            String accountType = req.getParameter("accountType"); // ADMIN / USER
	            String userId = req.getParameter("userId");           // 임의 ID
	            if (accountType == null || accountType.isBlank()) accountType = "USER";
	            if (userId == null || userId.isBlank()) userId = "guest-" + System.currentTimeMillis();
	            attributes.put("accountType", accountType);
	            attributes.put("userId", userId);
	        }
	        return true;
	    }
	    @Override public void afterHandshake(ServerHttpRequest req, ServerHttpResponse res,
	                                         WebSocketHandler wsHandler, Exception ex) {}
	}