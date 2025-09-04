package web.com.springweb.notification;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class NotifyWebSocketConfig implements WebSocketConfigurer {

    private final NotificationHandler notificationHandler;
    private final NotifyHandshakeInterceptor interceptor;

    public NotifyWebSocketConfig(NotificationHandler h, NotifyHandshakeInterceptor i) {
        this.notificationHandler = h;
        this.interceptor = i;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notificationHandler, "/notify")
                .addInterceptors(interceptor)
                .setAllowedOriginPatterns("http://localhost:*","http://127.0.0.1:*");
    }
}
