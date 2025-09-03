package web.com.springweb.a00_config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/slide/**")
                .addResourceLocations("file:///C:/spring_uploads/slide/")
                .setCachePeriod(0); // 캐시 끄기
        
        // 리뷰 이미지
        registry.addResourceHandler("/img/review/**")
                .addResourceLocations("file:///C:/spring_uploads/review/")
                .setCachePeriod(0); // 캐시 끄기
    }
}
