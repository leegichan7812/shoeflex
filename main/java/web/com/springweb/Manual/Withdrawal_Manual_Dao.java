package web.com.springweb.Manual;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import web.com.springweb.Manual.dto.WithdrawalManual;


@Mapper
public interface Withdrawal_Manual_Dao {
	
	@Select("SELECT * FROM Withdrawal_Manual WHERE reason LIKE #{reason}")
	List<WithdrawalManual> getWithdrawalManualList(WithdrawalManual sch); 
	
	@Insert("INSERT INTO Withdrawal_Manual " +
	        "(id, reason, situation, check_action, guidance, follow_up) " +
	        "VALUES (Withdrawal_Manual_seq.nextval, " +
	        "#{reason,jdbcType=VARCHAR}, " +
	        "#{situation,jdbcType=VARCHAR}, " +
	        "#{checkAction,jdbcType=VARCHAR}, " +
	        "#{guidance,jdbcType=VARCHAR}, " +
	        "#{followUp,jdbcType=VARCHAR})")
	int insertWithdrawal_Manual(WithdrawalManual ins);
	

	@Select("SELECT  * FROM Withdrawal_Manual WHERE ID = #{id}")
	WithdrawalManual getWithdrawalManual(@Param("id") int id);
	
	
	@Update("UPDATE Withdrawal_Manual "
	        + "SET reason       = #{reason,jdbcType=VARCHAR}, "
	        + "    situation    = #{situation,jdbcType=VARCHAR}, "
	        + "    check_action = #{checkAction,jdbcType=VARCHAR}, "
	        + "    guidance     = #{guidance,jdbcType=VARCHAR}, "
	        + "    follow_up    = #{followUp,jdbcType=VARCHAR} "
	        + "WHERE id = #{id,jdbcType=NUMERIC}")
	int updateWithdrawalManual(WithdrawalManual upt);
	// 안내, 후속조치
	@Update("UPDATE Withdrawal_Manual "
	        + "SET guidance     = #{guidance,jdbcType=VARCHAR}, "
	        + "    follow_up    = #{followUp,jdbcType=VARCHAR} "
	        + "WHERE id = #{id,jdbcType=NUMERIC}")
	int updateWithdrawalManual2(WithdrawalManual upt);	
	
	@Delete("DELETE FROM Withdrawal_Manual WHERE id = #{id}")
	int deleteWithdrawal_Manual(@Param("id") int id);


	
}


	
	
	
	
	
	
	


	
	



