package web.com.springweb.Inquiry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.com.springweb.vo.ProductInquiryView;  // 반드시 import!

@Service
public class InquiryService {

    @Autowired
    private InquiryDao inquiryDao;

    // 페이징 + 상태 필터 적용된 문의 현황 조회
    public Map<String, Object> getPagedInquiryStatuses(int page, int pageSize, String status) {
        // startRow, endRow: XML에서 요구하는 이름!
        int startRow = (page - 1) * pageSize;
        int endRow = page * pageSize;

        List<ProductInquiryView> list = inquiryDao.getInquiryStatuses(status, startRow, endRow);
        int totalCount = inquiryDao.getInquiryTotalCount(status);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        return result;
    }

    // 상태별 전체 개수 반환
    public int getInquiryTotalCount(String status) {
        return inquiryDao.getInquiryTotalCount(status);
    }

    // 답변 등록 또는 수정 (답변 있으면 update, 없으면 insert)
    public void addOrUpdateAnswer(int inquiryId, String answerContent, int adminId) {
        Integer answerId = inquiryDao.getAnswerIdByInquiryId(inquiryId);
        if (answerId == null) {
            inquiryDao.insertAnswer(inquiryId, answerContent, adminId);
        } else {
            inquiryDao.updateAnswer(answerId, answerContent, adminId);
        }
        inquiryDao.updateInquiryStatusAnswered(inquiryId);
    }

    // 답변 삭제 (문의ID로)
    public void deleteAnswerByInquiryId(int inquiryId) {
        inquiryDao.deleteAnswerByInquiryId(inquiryId);
        inquiryDao.updateInquiryStatusWaiting(inquiryId);
    }

    // 조회 관리자(admin) 변경
    public void updateViewerAdmin(Long inquiryId, Long adminId) {
        inquiryDao.updateViewerAdmin(inquiryId, adminId);
    }
    
    public ProductInquiryView getInquiryDetail(int inquiryId) {
        return inquiryDao.getInquiryDetail(inquiryId);
    }
    
    public List<ProductInquiryView> getRecentUnchecked(int count) {
        return inquiryDao.getRecentUnchecked(count);
    }
}
