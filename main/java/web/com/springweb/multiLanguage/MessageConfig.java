package web.com.springweb.multiLanguage;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MessageConfig implements WebMvcConfigurer {

	  // ✅ 세션 로케일 & lang 파라미터
	  @Bean public SessionLocaleResolver localeResolver() {
	    var r = new SessionLocaleResolver();
	    r.setDefaultLocale(Locale.KOREAN);
	    return r;
	  }
	  @Bean public LocaleChangeInterceptor localeChangeInterceptor() {
	    var i = new LocaleChangeInterceptor();
	    i.setParamName("lang");
	    return i;
	  }
	  @Override public void addInterceptors(InterceptorRegistry reg) {
	    reg.addInterceptor(localeChangeInterceptor());
	  }
	  // 풀백 메시지 소스
	  @Bean("fileMessageSource")   // ← 이름 고정
	  public ReloadableResourceBundleMessageSource fileMessageSource() {
	    var ms = new ReloadableResourceBundleMessageSource();
	    ms.setBasenames("classpath:/i18n/messages", "classpath:/messages");
	    ms.setDefaultEncoding("UTF-8");
	    return ms;
	  }
}
