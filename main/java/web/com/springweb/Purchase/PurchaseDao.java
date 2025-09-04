package web.com.springweb.Purchase;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import web.com.springweb.vo.PurchaseHistory;

@Mapper
public interface PurchaseDao {
	
	@Select("SELECT * FROM PURCHASE_HISTORY_FULL WHERE USER_ID = {userId} ORDER BY PURCHASED_AT DESC;")
	List<PurchaseHistory> ListContent(@Param("userId")String userId);
	
}
