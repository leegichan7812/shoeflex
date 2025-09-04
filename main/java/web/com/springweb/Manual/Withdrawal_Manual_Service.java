package web.com.springweb.Manual;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.Manual.dto.WithdrawalManual;


@Service
public class Withdrawal_Manual_Service {
	
	@Autowired(required = false)
	private Withdrawal_Manual_Dao dao;
	   
	public List<WithdrawalManual> getWithdrawalManualList(WithdrawalManual sch){
	// #{reason}
	if(sch.getReason()==null) sch.setReason("");
    sch.setReason("%"+sch.getReason()+"%");
    	return dao.getWithdrawalManualList(sch);
	   }
	public String insertWithdrawal_Manual(WithdrawalManual ins) {
		return dao.insertWithdrawal_Manual(ins)>0?"등록성공":"등록실패";
	}
	public WithdrawalManual getWithdrawalManual(@Param("id") int id) {
		return dao.getWithdrawalManual(id);
	}
	public String updatetWithdrawal_Manual(WithdrawalManual upt) {
		return dao.updateWithdrawalManual(upt)>0?"수정성공":"수정실패";
	}
	
	// 수정 안내~후속조치
	public String updateWithdrawalManual2(WithdrawalManual upt) {	
        return dao.updateWithdrawalManual2(upt)>0?"수정성공":"수정실패";
	}    

	public String deleteWithdrawal_Manual(int id) {
		return dao.deleteWithdrawal_Manual(id)>0?"삭제성공":"삭제실패";
	}
	
	
}

	

