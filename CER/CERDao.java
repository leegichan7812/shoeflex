package web.com.springweb.CER;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import web.com.springweb.vo.AfterSalesRequest;

@Mapper
public interface CERDao {

    AfterSalesRequest selectById(@Param("reqId") int reqId);

    // 총 개수 (view/status/type 반영)
    int countList(@Param("reqStatus") String reqStatus,
                  @Param("reqType") String reqType,
                  @Param("view") String view);

    // 페이징 목록 (view/status/type + offset/limit)
    List<AfterSalesRequest> selectListPaged(@Param("reqStatus") String reqStatus,
                                            @Param("reqType") String reqType,
                                            @Param("view") String view,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    int countAll();

    int insert(AfterSalesRequest req);

    int update(AfterSalesRequest req);

    CerItemView findCerItemViewByOrderItemId(@Param("orderItemId") long orderItemId);

    

    List<AfterSalesRequest> selectListByUser(@Param("userId") int userId,
                                             @Param("reqStatus") String reqStatus,
                                             @Param("reqType") String reqType,
                                             @Param("view") String view);

    int countListByUser(@Param("userId") int userId,
                        @Param("reqStatus") String reqStatus,
                        @Param("reqType") String reqType,
                        @Param("view") String view);

    List<AfterSalesRequest> selectListByUserPaged(@Param("userId") int userId,
                                                  @Param("reqStatus") String reqStatus,
                                                  @Param("reqType") String reqType,
                                                  @Param("view") String view,
                                                  @Param("offset") int offset,
                                                  @Param("limit") int limit);
	
    int countOpenRequests(@Param("orderItemId") long orderItemId);
    
 // 진행중인 신청(신청/승인 상태) 단건 조회
    AfterSalesRequest findOpenRequestByOrderItemId(@Param("orderItemId") long orderItemId);
}
