package web.com.springweb.Chatting;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class BotClient {
	
	 private final WebClient webClient;
	    @Value("${chatbot.api-key}") private String apiKey;

	    public BotClient(@Qualifier("botWebClient") WebClient webClient) {
	        this.webClient = webClient;
	    }

	    public Mono<InferRes> infer(String text, String sessionId) {
	        InferReq req = new InferReq();
	        req.setText(text);
	        req.setSessionId(sessionId);

	        return webClient.post()
	                .uri("/infer")                           // ✅ baseUrl은 Bean에서
	                .header("x-api-key", apiKey)
	                .bodyValue(req)
	                .retrieve()
	                .onStatus(HttpStatusCode::isError, resp -> // ✅ HTTP 오류를 원문으로
	                        resp.bodyToMono(String.class)
	                            .defaultIfEmpty("(no body)")
	                            .flatMap(body -> Mono.error(new RuntimeException(
	                                    "Bot infer HTTP " + resp.statusCode() + " : " + body)))
	                )
	                .bodyToMono(InferRes.class)
	                .timeout(Duration.ofSeconds(6)); // ✅ 상위 타임아웃(방어)
	    }

	    // DTO
	    public static class InferReq {
	        private String text;
	        private String sessionId;
	        public String getText(){return text;}
	        public void setText(String t){this.text=t;}
	        public String getSessionId(){return sessionId;}
	        public void setSessionId(String s){this.sessionId=s;}
	    }
	    public static class InferRes {
	        private String label, reply, sessionId;
	        private double confidence;
	        // ▼ 신규 (없어도 역직렬화는 되도록 null 허용)
	        private String route;
	        private java.util.List<String> suggestions;
	        
	        public String getRoute() { return route; }
	        public void setRoute(String route) { this.route = route; }

	        public List<String> getSuggestions() { return suggestions; }
	        public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }        
	        public String getLabel(){return label;}
	        public void setLabel(String v){this.label=v;}	        
	        public String getReply(){return reply;}
	        public void setReply(String v){this.reply=v;}
	        public String getSessionId(){return sessionId;}
	        public void setSessionId(String v){this.sessionId=v;}
	        public double getConfidence(){return confidence;}
	        public void setConfidence(double v){this.confidence=v;}
	    }
	    
	    
	    
	    
	    
	}
	