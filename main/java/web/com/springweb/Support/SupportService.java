package web.com.springweb.Support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.com.springweb.vo.Faq;
import web.com.springweb.vo.Notice;

@Service
public class SupportService {

    @Autowired(required = false)
    private SupportDao dao;

    // ===== 유틸 =====
    private String nvl(String s) { return (s == null ? "" : s.trim()); }

    // ===== 공지(Notice) =====
    public List<Notice> getTop5Notice() {
        return dao.getTop5Notice();
    }

    public List<Notice> searchNotices(String keyword) {
        return dao.searchNotices(nvl(keyword));
    }

    @Transactional
    public void insertNotice(Notice notice) { dao.insertNotice(notice); }

    @Transactional
    public void updateNotice(Notice notice) { dao.updateNotice(notice); }

    @Transactional
    public void deleteNotice(Long noticeId) { dao.deleteNotice(noticeId); }

    public Notice getNoticeDetail(int noticeId) {
        dao.increaseViewCount(noticeId);
        return dao.getNoticeById(noticeId);
    }

    // --- Notice 페이징 ---
    public int getNoticeTotalCount(String keyword) {
        return dao.countNotices(nvl(keyword));
    }

    public List<Notice> getNoticePageByCur(String keyword, int curPage, int pageSize) {
        int ps = pageSize > 0 ? pageSize : 10;
        int cp = curPage  > 0 ? curPage  : 1;
        int offset = (cp - 1) * ps;          // DAO가 ROWNUM 방식이든 OFFSET/FETCH든 동일하게 동작
        return dao.findNoticePage(nvl(keyword), offset, ps);
    }

    // ===== FAQ =====
    public List<Faq> getTop5ViewCountFaq() {
        return dao.getTop5ViewCountFaq();
    }

    public List<Faq> getAllFaq() {
        return dao.getAllFaq();
    }

    public List<Faq> searchFaqByKeyword(String keyword) {
        return dao.searchFaqByKeyword(nvl(keyword));
    }

    public List<Faq> searchFaqByCategory(String category) {
        return dao.searchFaqByCategory(nvl(category));
    }

    public Faq getFaqById(Long faqId) {
        return dao.getFaqById(faqId);
    }

    @Transactional
    public void insertFaq(Faq faq) { dao.insertFaq(faq); }

    @Transactional
    public void updateFaq(Faq faq) { dao.updateFaq(faq); }

    @Transactional
    public void deleteFaq(Long faqId) { dao.deleteFaq(faqId); }
    
    private static void normalizeYN(Notice n){
        n.setIsPopup("Y".equalsIgnoreCase(n.getIsPopup()) ? "Y" : "N");
        n.setIsPinned("Y".equalsIgnoreCase(n.getIsPinned()) ? "Y" : "N");
    }
    
    public Notice getPopupNotice() {
        return dao.selectLatestPopup();  // is_popup='Y' 중 최신 1건 반환
    }

    // --- FAQ 페이징 ---
    public int getFaqTotalCount(String keyword, String category) {
        return dao.countFaq(nvl(keyword), nvl(category));
    }

    public List<Faq> getFaqPageByCur(String keyword, String category, int curPage, int pageSize) {
        int ps = pageSize > 0 ? pageSize : 10;
        int cp = curPage  > 0 ? curPage  : 1;
        int offset = (cp - 1) * ps;
        return dao.findFaqPage(nvl(keyword), nvl(category), offset, ps);
    }
}
