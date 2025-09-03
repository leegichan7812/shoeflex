package web.com.springweb.Auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AuthDao {
    @Select("SELECT EMAIL FROM USERS WHERE NAME = #{name} AND REPLACE(PHONE,'-','') = REPLACE(#{phone},'-','')")
    String findEmailByNamePhone(@Param("name") String name, @Param("phone") String phone);

    @Select("SELECT USER_ID FROM USERS WHERE EMAIL = #{email} AND NAME = #{name} AND REPLACE(PHONE,'-','') = REPLACE(#{phone},'-','')")
    Integer findUserIdByEmailNamePhone(@Param("email") String email, @Param("name") String name, @Param("phone") String phone);

    @Update("UPDATE USERS SET PASSWORD = #{encodedPw} WHERE USER_ID = #{userId}")
    int updatePassword(@Param("userId") int userId, @Param("encodedPw") String encodedPw);
}