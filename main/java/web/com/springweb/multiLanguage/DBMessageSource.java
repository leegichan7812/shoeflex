package web.com.springweb.multiLanguage;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component("messageSource")
public class DBMessageSource extends AbstractMessageSource {
	
	@Autowired(required=false)
	  private final I18nMessageMapper mapper;
	  private final MessageSource parent; // 파일 폴백
	  private final java.util.concurrent.ConcurrentMap<String, Map<String,String>> cache = new java.util.concurrent.ConcurrentHashMap<>();

	  public DBMessageSource(I18nMessageMapper mapper,
              @Qualifier("fileMessageSource") ReloadableResourceBundleMessageSource parent) {
			this.mapper = mapper;
			this.parent = parent;
			setParentMessageSource(parent);
			}
	  

	  private Map<String,String> load(String tagLower) {
		    Map<String, I18nMessageVO> voMap = mapper.selectByLocaleAsVOMap(tagLower);
		    Map<String,String> out = new java.util.HashMap<>(Math.max(16, voMap.size()*2));
		    for (Map.Entry<String, I18nMessageVO> e : voMap.entrySet()) {
		      out.put(e.getKey(), e.getValue().getMessage()); // code → message
		    }
		    return out;
		  }

		  @Override
		  protected MessageFormat resolveCode(String code, Locale locale) {
		    String tag  = locale.toLanguageTag().toLowerCase();
		    String base = locale.getLanguage().toLowerCase();

		    String msg = cache.computeIfAbsent(tag,  this::load).get(code);
		    if (msg == null && !tag.equals(base)) {
		      msg = cache.computeIfAbsent(base, this::load).get(code);
		    }
		    if (msg == null) {
		      try { msg = parent.getMessage(code, null, locale); } catch (Exception ignore) {}
		    }
		    if (msg == null) msg = code;
		    return createMessageFormat(msg, locale);
		  }
		  
		  

		  

	  
}
