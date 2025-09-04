package web.com.springweb.Users;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import web.com.springweb.vo.Users;

@Mapper
public interface userDao {
	// 로그인
	//@Select("SELECT * FROM USERS WHERE EMAIL = #{email} AND PASSWORD = #{password} AND ACCOUNT_STATUS = '정상'")
	//Users selectUser(Users login);
	// ✅ 변경: 이메일로만 조회
    @Select("SELECT * FROM USERS WHERE EMAIL = #{email} AND ACCOUNT_STATUS = '정상'")
    Users findByEmail(String email);
    // 비밀번호 자동으로 BCrypt로 승격
    @Update("UPDATE USERS SET PASSWORD = #{encodedPw} WHERE USER_ID = #{userId}")
    int updatePasswordByUserId(@Param("userId") int userId, @Param("encodedPw") String encodedPw);
	
	// 회원가입
	@Insert("INSERT INTO USERS (USER_ID, EMAIL, PASSWORD, NAME, PHONE, ADDRESS, "
			+ "ACCOUNT_TYPE, created_at, updated_at, AGE, GENDER, ACCOUNT_STATUS) \r\n"
			+ "VALUES (SEQ_USER_ID.NEXTVAL, #{email}, #{password}, #{name}, #{phone}, #{address}, "
			+ "#{accountType}, SYSDATE, SYSDATE, #{age}, #{gender}, #{accountStatus})")
	int insertJoin(Users ins);
	// 아이디 중복체크
	@Select("SELECT COUNT(*) FROM USERS WHERE EMAIL = #{email}")
	int checkId(String email);
	

}
