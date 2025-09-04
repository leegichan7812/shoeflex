package web.com.springweb.Chatting;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChattingHandler chatHandler;

    public WebSocketConfig(ChattingHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/chat")
        		.addInterceptors(new CustomHandshakeInterceptor()) // ← 추가
                .setAllowedOrigins("*");
    }

    /*
    /
chat 엔드포인트를 WebSocket 핸들러와 연결

클라이언트가 ws://.../chat 으로 접속하면 ChattingHandler가 처리
     */
}
