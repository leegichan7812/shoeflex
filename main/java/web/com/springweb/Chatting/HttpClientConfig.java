package web.com.springweb.Chatting;

import reactor.netty.http.client.HttpClient;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HttpClientConfig {
	
    @Bean
    public WebClient botWebClient(@Value("${chatbot.base-url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)   // ✅ baseUrl을 한 번만 고정
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                  .responseTimeout(Duration.ofSeconds(5)) // 타임아웃 설정
                ))
                .build();
    }

}
