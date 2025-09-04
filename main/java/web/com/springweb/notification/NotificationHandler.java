package web.com.springweb.notification;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class NotificationHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> blacks = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> normals = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String grade = getAttr(session, "accountType", "USER"); // BLACK/USER
        if ("BLACK".equalsIgnoreCase(grade) || "ADMIN".equalsIgnoreCase(grade)) {
            blacks.put(session.getId(), session);
        } else {
            normals.put(session.getId(), session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        blacks.remove(session.getId());
        normals.remove(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        blacks.remove(session.getId());
        normals.remove(session.getId());
        try { session.close(CloseStatus.SERVER_ERROR); } catch (IOException ignore) {}
    }

    private String getAttr(WebSocketSession s, String key, String def) {
        Object v = s.getAttributes() == null ? null : s.getAttributes().get(key);
        return v == null ? def : v.toString();
    }

    /* ====== 공개 API (다른 Service에서 주입받아 호출) ====== */
    public void sendToAll(String json) { send(json, blacks.values(), normals.values()); }
    public void sendToBlacks(String json) { send(json, blacks.values()); }
    public void sendToNormals(String json) { send(json, normals.values()); }

    private void send(String json, Collection<WebSocketSession>... targets) {
        TextMessage msg = new TextMessage(json);
        Stream.of(targets).flatMap(Collection::stream).forEach(ws -> {
            if (ws.isOpen()) {
                try { ws.sendMessage(msg); } catch (Exception ignore) {}
            }
        });
    }
}