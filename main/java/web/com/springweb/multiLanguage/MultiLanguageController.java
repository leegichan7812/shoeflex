package web.com.springweb.multiLanguage;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MultiLanguageController {


    private final LocaleResolver localeResolver;
    private final I18nMessageMapper mapper;

    public MultiLanguageController(LocaleResolver lr, I18nMessageMapper m) {
      this.localeResolver = lr;
      this.mapper = m;
    }

    @PostMapping("/changeLang")
    @ResponseBody
    public Map<String, String> changeLang(@RequestParam String lang,
                                          @RequestParam(required=false) String prefix,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
      Locale locale = Locale.forLanguageTag(lang);
      localeResolver.setLocale(request, response, locale);

      String tag = locale.toLanguageTag().toLowerCase();

      Map<String, I18nMessageVO> voMap = (prefix == null || prefix.isBlank())
          ? mapper.selectByLocaleAsVOMap(tag)
          : mapper.selectByLocaleAndPrefixAsVOMap(tag, prefix);

      // VO → Map<String, String> (code → message)
      Map<String, String> out = new LinkedHashMap<>();
      for (Map.Entry<String, I18nMessageVO> e : voMap.entrySet()) {
        out.put(e.getKey(), e.getValue().getMessage());
      }
      return out;
    }
    
    
    
    
    
    
    
}