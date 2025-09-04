package web.com.springweb.Chatting;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import reactor.core.scheduler.Schedulers;


@Component("chatHandler")
public class ChattingHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> admins = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> users  = new ConcurrentHashMap<>();
    private final Set<String> humanConnected = java.util.Collections.newSetFromMap(new ConcurrentHashMap<>());

    private enum ChatMode { BOT, HUMAN }
    private final Map<String, ChatMode> modes = new ConcurrentHashMap<>();

    private final BotClient botClient;

    public ChattingHandler(BotClient botClient) {
        this.botClient = botClient;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("[소켓 서버단] 접속: " + session.getId());
        String accountType = (String) session.getAttributes().get("accountType");
        String userId      = (String) session.getAttributes().get("userId");

        if ("ADMIN".equalsIgnoreCase(accountType)) {
            admins.put(session.getId(), session);
            System.out.println("[ADMIN 접속] " + userId);
            send(session, "[SYSTEM] 관리자 연결됨");

            // ADMIN이 늦게 들어온 경우: 대기중(HUMAN) 유저들 알림
            for (Map.Entry<String, WebSocketSession> e : users.entrySet()) {
                String uid = e.getKey();
                if (modes.getOrDefault(uid, ChatMode.BOT) == ChatMode.HUMAN && !humanConnected.contains(uid)) {
                    String name = (String) e.getValue().getAttributes().get("userId");
                    if (name == null || name.isBlank()) name = uid;
                    send(session, "[ALERT] CONNECT_REQUEST|" + name + "|" + uid);
                }
            }
        } else {
            users.put(session.getId(), session);
            modes.put(session.getId(), ChatMode.BOT); // 기본 BOT
            System.out.println("[USER 접속] " + userId);
            send(session, "[BOT] 안녕하세요! 무엇을 도와드릴까요? '도움말' 입력시 명령어를 확인하실 수 있습니다."
            		+ " ('상담원'을 입력하면 사람 상담으로 전환됩니다)");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String text        = message.getPayload().trim();
        String sid         = session.getId();
        String accountType = (String) session.getAttributes().get("accountType");
        boolean isAdmin    = "ADMIN".equalsIgnoreCase(accountType);

        String uname = (String) session.getAttributes().get("userId");
        if (uname == null || uname.isBlank()) uname = sid;
        System.out.println("[소켓 메시지] " + uname + " : " + text);

        if (isAdmin) {
            // 🔴 종료 명령 처리
            String norm = text.replaceAll("\\s+", "");
            if ("/end".equalsIgnoreCase(text) || "종료".equals(norm) || "상담종료".equals(norm)) {
                for (Map.Entry<String, WebSocketSession> e : users.entrySet()) {
                    String uid = e.getKey();
                    if (modes.getOrDefault(uid, ChatMode.BOT) == ChatMode.HUMAN) {
                        send(e.getValue(), "[SYSTEM] 상담이 종료되었습니다.");
                        modes.put(uid, ChatMode.BOT);
                        humanConnected.remove(uid);
                    }
                }
                send(session, "[SYSTEM] 모든 고객과의 상담을 종료했습니다.");
                return;
            }

            // (선택) 특정 고객만 종료: "/end <세션ID>"
            if (text.startsWith("/end ")) {
                String targetSid = text.substring(5).trim();
                WebSocketSession target = users.get(targetSid);
                if (target != null && modes.getOrDefault(targetSid, ChatMode.BOT) == ChatMode.HUMAN) {
                    send(target, "[SYSTEM] 상담이 종료되었습니다.");
                    modes.put(targetSid, ChatMode.BOT);
                    humanConnected.remove(targetSid);
                    send(session, "[SYSTEM] " + targetSid + " 상담을 종료했습니다.");
                } else {
                    send(session, "[SYSTEM] 대상이 없거나 사람 상담 상태가 아닙니다.");
                }
                return;
            }

            // 관리자 메시지: HUMAN 유저에게만 전달
            for (Map.Entry<String, WebSocketSession> e : users.entrySet()) {
                String uid = e.getKey();
                if (modes.getOrDefault(uid, ChatMode.BOT) == ChatMode.HUMAN && !humanConnected.contains(uid)) {
                    humanConnected.add(uid);
                    send(e.getValue(), "[SYSTEM] 상담원과 연결되었습니다.");
                }
            }
            for (WebSocketSession u : users.values()) {
                if (modes.getOrDefault(u.getId(), ChatMode.BOT) == ChatMode.HUMAN) {
                    send(u, "[ADMIN] " + text);
                }
            }
            return;
        }

        // 사용자 키워드: 모드 전환
        if ("상담원".equals(text)) {
            modes.put(sid, ChatMode.HUMAN);
            String name = (String) session.getAttributes().get("userId");
            if (name == null || name.isBlank()) name = sid;

            if (admins.isEmpty()) {
                send(session, "[SYSTEM] 상담원을 연결 중입니다. 잠시만 기다려 주세요.");
            } else {
                for (WebSocketSession a : admins.values()) {
                    send(a, "[ALERT] CONNECT_REQUEST|" + name + "|" + sid);
                }
                send(session, "[SYSTEM] 상담원과 연결 중입니다. 잠시만 기다려 주세요.");
            }
            return;
        }

        // ✅ '봇'으로 전환
        if (text.matches("(?i)^(봇|bot|챗봇)$")) {
            modes.put(sid, ChatMode.BOT);
            humanConnected.remove(sid);
            send(session, "[SYSTEM] 봇 모드로 전환되었습니다.");
            send(session, "[BOT] 무엇을 도와드릴까요?");
            return; // ← 챗봇 호출 금지
        }

        // --- BOT/HUMAN 분기 ---
        ChatMode mode = modes.getOrDefault(sid, ChatMode.BOT);

        if (mode == ChatMode.BOT) {
            botClient.infer(text, sid)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                    res -> {
                        String reply = (res != null && res.getReply() != null)
                                ? res.getReply()
                                : "답변을 생성하지 못했어요.";
                        send(session, "[BOT] " + reply);

                        if (res != null) {
                            String route = res.getRoute();
                            if (route != null && !route.isBlank()) {
                                send(session, "[ROUTE] " + route); // CTA/라우팅 신호
                            }
                            java.util.List<String> sugg = res.getSuggestions();
                            if (sugg != null && !sugg.isEmpty()) {
                                send(session, "[SUGGEST] " + String.join("|", sugg)); // 퀵리플라이
                            }
                        }
                    },
                    err -> {
                        err.printStackTrace();
                        send(session, "[BOT] 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                    }
                );
            return; // ← HUMAN 분기로 내려가지 않음
        }

        // ---- HUMAN 모드: 고객 → 관리자 전달 ----
        String senderName = (String) session.getAttributes().get("userId");
        if (senderName == null || senderName.isBlank()) senderName = sid;

        if (admins.isEmpty()) {
            send(session, "[SYSTEM] 현재 상담원이 없습니다. '봇' 을 입력해 봇 모드로 전환할 수 있어요.");
        } else {
            for (WebSocketSession a : admins.values()) {
                send(a, "[USER " + senderName + "] " + text);
            }
        }
    } // ← 메서드 닫기(중요)


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sid   = session.getId();
        String role  = (String) session.getAttributes().get("accountType");
        String uname = (String) session.getAttributes().get("userId");
        if (uname == null || uname.isBlank()) uname = sid;

        admins.remove(sid);
        users.remove(sid);
        modes.remove(sid);
        humanConnected.remove(sid);

        if ("ADMIN".equalsIgnoreCase(role)) {
            // 관리자가 끊으면 HUMAN 유저에게 종료 안내
            for (WebSocketSession u : users.values()) {
                if (modes.getOrDefault(u.getId(), ChatMode.BOT) == ChatMode.HUMAN) {
                    send(u, "[SYSTEM] 상담이 종료되었습니다.");
                }
            }
        } else {
            // 유저가 끊으면 관리자에게 알림
            for (WebSocketSession a : admins.values()) {
                send(a, "[SYSTEM] " + uname + " 고객님이 상담을 종료했습니다.");
            }
        }

        System.out.println("[소켓 접속 종료] " + uname + " (" + sid + ")");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("[에러 발생]" + session.getId() + ", 예외발생:" + exception.getMessage());
    }

    private void send(WebSocketSession s, String text) {
        try {
            if (s != null && s.isOpen()) s.sendMessage(new TextMessage(text));
        } catch (Exception ignore) {}
    }
}