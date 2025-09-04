package web.com.springweb.Chatting;

import java.util.Optional;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	@ModelAttribute("socketServer")
	public String addSocketServer(HttpServletRequest req) {
	    String host = req.getServerName();
	    int port = req.getServerPort();
	    String scheme = req.isSecure() ? "wss" : "ws";
	    String ctx = Optional.ofNullable(req.getContextPath()).orElse("");
	    return scheme + "://" + host + (port==80||port==443 ? "" : ":"+port) + ctx + "/chat";
	}
	 
	 @ModelAttribute("notifyServer")
	 public String addNotifyServer(HttpServletRequest req){
	     String scheme = req.isSecure() ? "wss" : "ws";
	     String host = req.getServerName();
	     int port = req.getServerPort();
	     String ctx = Optional.ofNullable(req.getContextPath()).orElse("");
	     return scheme + "://" + host + (port==80||port==443 ? "" : ":"+port) + ctx + "/notify";
	 }
//역할

//모든 컨트롤러에서 JSP로 공통 속성(socketServer)을 전달해 줌

//뷰에서 <c:out value="${socketServer}"/> 같은 방식으로 WebSocket 주소를 바로 사용 가능
}
