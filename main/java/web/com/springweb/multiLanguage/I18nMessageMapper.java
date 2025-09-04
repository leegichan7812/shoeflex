package web.com.springweb.multiLanguage;

import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface I18nMessageMapper {
	
	  // ✅ 언어별 번들만 맵으로 바로 받기
	  @Select("""
		      SELECT CODE   AS code,
		             LOCALE AS locale,
		             MESSAGE AS message
		      FROM I18N_MESSAGE
		      WHERE LOWER(LOCALE) = LOWER(#{locale})
		      """)
		  @MapKey("code")
		  Map<String, I18nMessageVO> selectByLocaleAsVOMap(@Param("locale") String locale);

		  @Select("""
		      SELECT CODE   AS code,
		             LOCALE AS locale,
		             MESSAGE AS message
		      FROM I18N_MESSAGE
		      WHERE LOWER(LOCALE) = LOWER(#{locale})
		        AND (#{prefix} IS NULL OR CODE LIKE #{prefix} || '%')
		      """)
		  @MapKey("code")
		  Map<String, I18nMessageVO> selectByLocaleAndPrefixAsVOMap(@Param("locale") String locale,
		                                                            @Param("prefix") String prefix);
		

}


