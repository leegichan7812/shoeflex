package web.com.springweb.Support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import web.com.springweb.vo.Faq;
import web.com.springweb.vo.Notice;

@Controller
public class SupportController {

    @Autowired(required = false)
    private SupportService service;

    /* =========================
       고객센터 메인 (/support)
       ========================= */
    @GetMapping("support")
    public String supportPage(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                              Model model) {

        // 검색어가 있을 경우 FAQ 검색으로 리디렉션
        if (!keyword.trim().isEmpty()) {
            return "redirect:/faq?keyword=" + keyword.trim();
        }

        // 공지사항 5개 + 카테고리별 상위 FAQ 5개
        model.addAttribute("noticeList", service.getTop5Notice());
        model.addAttribute("faqList", service.getTop5ViewCountFaq());
        model.addAttribute("keyword", ""); // 검색어 초기화

        return "pages\\SCmain";
    }

    /* =========================
       FAQ 목록 (사용자/관리자) + 페이징
       ========================= */

    // 사용자 FAQ 목록
    @GetMapping("/faq")
    public String faqList(@RequestParam(value = "keyword",  defaultValue = "") String keyword,
                          @RequestParam(value = "category", defaultValue = "") String category,
                          @RequestParam(value = "curPage",  defaultValue = "1")  int curPage,
                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                          Model model) {

        int totalCount = service.getFaqTotalCount(keyword.trim(), category.trim());
        int totalPage  = (int) Math.ceil((double) Math.max(totalCount, 0) / Math.max(pageSize, 1));
        curPage = Math.max(1, Math.min(curPage, Math.max(totalPage, 1)));

        List<Faq> faqList = service.getFaqPageByCur(keyword.trim(), category.trim(), curPage, pageSize);

        int blockSize = 5;
        int startPage = ((curPage - 1) / blockSize) * blockSize + 1;
        int endPage   = Math.min(startPage + blockSize - 1, totalPage);

        model.addAttribute("faq", new Faq());
        model.addAttribute("faqList", faqList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);

        model.addAttribute("curPage", curPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalCount", totalCount);

        return "pages\\SCFaqlist";
    }



    // 관리자 FAQ 목록
    @GetMapping("/faqA")
    public String faqListA(@RequestParam(value = "keyword",  defaultValue = "") String keyword,
                           @RequestParam(value = "category", defaultValue = "") String category,
                           @RequestParam(value = "curPage",  defaultValue = "1")  int curPage,
                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                           Model model) {

        int totalCount = service.getFaqTotalCount(keyword.trim(), category.trim());
        int totalPage  = (int) Math.ceil((double) Math.max(totalCount, 0) / Math.max(pageSize, 1));
        curPage = Math.max(1, Math.min(curPage, Math.max(totalPage, 1)));

        List<Faq> faqList = service.getFaqPageByCur(keyword.trim(), category.trim(), curPage, pageSize);

        int blockSize = 5;
        int startPage = ((curPage - 1) / blockSize) * blockSize + 1;
        int endPage   = Math.min(startPage + blockSize - 1, totalPage);

        model.addAttribute("faq", new Faq());
        model.addAttribute("faqList", faqList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);

        model.addAttribute("curPage", curPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalCount", totalCount);

        return "pages\\ad_SCFaqlist";
    }

    /* =========================
       FAQ CRUD (AJAX)
       ========================= */
    @PostMapping("/faq")
    @ResponseBody
    public ResponseEntity<String> insertFaq(Faq faq) {
        service.insertFaq(faq);
        return ResponseEntity.ok("등록 성공");
    }

    @PostMapping("/faq/update")
    @ResponseBody
    public ResponseEntity<String> updateFaq(@ModelAttribute Faq faq) {
        service.updateFaq(faq);
        return ResponseEntity.ok("수정 성공");
    }

    @PostMapping("/faq/delete")
    @ResponseBody
    public ResponseEntity<String> deleteFaq(@RequestParam("faqId") Long faqId) {
        service.deleteFaq(faqId);
        return ResponseEntity.ok("삭제 성공");
    }

    /* =========================
       공지 목록 (사용자/관리자) + 페이징
       ========================= */

    // 사용자 공지 목록
    @GetMapping("/support/notice")
    public String noticeSearchPage(@RequestParam(name = "keyword",  defaultValue = "") String keyword,
                                   @RequestParam(name = "curPage",  defaultValue = "1")  int curPage,
                                   @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                   Model model) {

        int totalCount = service.getNoticeTotalCount(keyword.trim());
        int pageSZ     = Math.max(pageSize, 1);
        int totalPage  = (int) Math.ceil((double) Math.max(totalCount, 0) / pageSZ);
        curPage = Math.max(1, Math.min(curPage, Math.max(totalPage, 1)));

        List<Notice> noticeList = service.getNoticePageByCur(keyword.trim(), curPage, pageSZ);

        int blockSize = 5;
        int startPage = ((curPage - 1) / blockSize) * blockSize + 1;
        int endPage   = Math.min(startPage + blockSize - 1, totalPage);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("keyword", keyword);

        model.addAttribute("curPage", curPage);
        model.addAttribute("pageSize", pageSZ);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalCount", totalCount);

        return "pages\\SCNotice";
    }

    // 관리자 공지 목록
    @GetMapping("/support/noticeA")
    public String noticeSearchPageA(@RequestParam(name = "keyword",  defaultValue = "") String keyword,
                                    @RequestParam(name = "curPage",  defaultValue = "1")  int curPage,
                                    @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                    Model model) {

        int totalCount = service.getNoticeTotalCount(keyword.trim());
        int pageSZ     = Math.max(pageSize, 1);
        int totalPage  = (int) Math.ceil((double) Math.max(totalCount, 0) / pageSZ);
        curPage = Math.max(1, Math.min(curPage, Math.max(totalPage, 1)));

        List<Notice> noticeList = service.getNoticePageByCur(keyword.trim(), curPage, pageSZ);

        int blockSize = 5;
        int startPage = ((curPage - 1) / blockSize) * blockSize + 1;
        int endPage   = Math.min(startPage + blockSize - 1, totalPage);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("keyword", keyword);

        model.addAttribute("curPage", curPage);
        model.addAttribute("pageSize", pageSZ);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalCount", totalCount);

        return "pages\\ad_SCNotice";
    }

    /* =========================
       공지 CRUD + 상세 (AJAX)
       ========================= */
    @PostMapping("/support/notice/insert")
    @ResponseBody
    public ResponseEntity<String> create(@ModelAttribute Notice notice) {
        service.insertNotice(notice);
        return ResponseEntity.ok("등록성공");
    }

    @PostMapping("/support/notice/update")
    @ResponseBody
    public ResponseEntity<String> edit(@ModelAttribute Notice notice) {
        service.updateNotice(notice);
        return ResponseEntity.ok("수정성공");
    }
    
 // Controller
    @GetMapping("/support/notice/popup")
    @ResponseBody
    public Notice popup() {
        return service.getPopupNotice();
    }

    @PostMapping("/support/notice/delete")
    @ResponseBody
    public ResponseEntity<String> remove(@RequestParam Long noticeId) {
        service.deleteNotice(noticeId);
        return ResponseEntity.ok("삭제성공");
    }

    @GetMapping("/support/notice/detail")
    @ResponseBody
    public Notice getNoticeDetail(@RequestParam("id") int id) {
        return service.getNoticeDetail(id);
    }
}
