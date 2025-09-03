package web.com.springweb.CER;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.com.springweb.vo.AfterSalesRequest;
import web.com.springweb.vo.PageResult;

@Service
public class CERService {
	@Autowired(required=false)
	public CERDao dao;
	
    public AfterSalesRequest getById(int reqId) {
        return dao.selectById(reqId);
    }


    // 총 개수: 기존 countAll()은 Mapper에 없음 → 제거 권장
    public int getTotalCount() {
        // 사용 금지: countAll() Mapper가 없음
        throw new UnsupportedOperationException("Use countList(reqStatus, reqType, view) instead.");
    }

    // ✅ 페이징 서비스 (view/status/type + page/size)
    public PageResult<AfterSalesRequest> getPagedList(int page, int size,
                                                      String reqStatus, String reqType, String view) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        final int offset = (page - 1) * size;

        int totalCount = dao.countList(reqStatus, reqType, view);
        List<AfterSalesRequest> list =
            dao.selectListPaged(reqStatus, reqType, view, offset, size);

        PageResult<AfterSalesRequest> result = new PageResult<>();
        result.setContent(list);
        result.setPage(page);
        result.setSize(size);
        result.setTotalCount(totalCount);

        int totalPages = (int) Math.ceil((double) totalCount / size);
        result.setTotalPages(totalPages);
        result.setHasPrev(page > 1);
        result.setHasNext(page < Math.max(totalPages, 1));

        return result;
    }
    
    /** 신청 등록 (mapper의 <selectKey>로 reqId 채움) */
    @Transactional
    public int create(AfterSalesRequest req) {
    	if (req.getReqStatus() == null || req.getReqStatus().trim().isEmpty()) {
            req.setReqStatus("신청");
        }

        int count = dao.countOpenRequests(req.getOrderItemId());
        if (count > 0) {
            return -1; // 이미 진행 중 신청 있음
        }
        return dao.insert(req);
    }

    @Transactional
    public int modify(AfterSalesRequest req) {
        return dao.update(req);
    }
	
    /** 모달 상단 상품요약 조회 */
    public CerItemView findCerItemViewByOrderItemId(long orderItemId) {
        return dao.findCerItemViewByOrderItemId(orderItemId);
    }
	
    public PageResult<AfterSalesRequest> getPagedListByUser(int userId, int page, int size,
            String reqStatus, String reqType, String view) {
	if (page < 1) page = 1;
	if (size < 1) size = 10;
	
	int offset = (page - 1) * size;
	
	int totalCount = dao.countListByUser(userId, reqStatus, reqType, view);
	List<AfterSalesRequest> list =
	dao.selectListByUserPaged(userId, reqStatus, reqType, view, offset, size);
	
	PageResult<AfterSalesRequest> result = new PageResult<>();
	result.setContent(list);
	result.setPage(page);
	result.setSize(size);
	result.setTotalCount(totalCount);
	
	int totalPages = (int) Math.ceil((double) totalCount / size);
	result.setTotalPages(totalPages);
	result.setHasPrev(page > 1);
	result.setHasNext(page < Math.max(totalPages, 1));
	
	return result;
	}
	    
    
    public AfterSalesRequest findOpenRequestByOrderItemId(long orderItemId) {
        return dao.findOpenRequestByOrderItemId(orderItemId);
    }
    
    
}
