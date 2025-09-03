package web.com.springweb.a00_config.aop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import web.com.springweb.vo.ExceptionLog;
import web.com.springweb.vo.ExceptionLogSearchDto;

@Controller
public class ExceptionLogController {
	@Autowired
    private ExceptionLogServiceImpl service;

    @RequestMapping("exceptionLogs")
    public String showExceptionLogs(ExceptionLogSearchDto sch, Model d) {
        if (sch.getKeyword() == null) sch.setKeyword("");
        if (sch.getCurPage() <= 0) sch.setCurPage(1);
        sch.setPaging(sch.getCurPage(), sch.getPageSize());

        List<ExceptionLog> logList = service.getExceptionLogList(sch);
        int totalCount = service.getTotalCount(sch);
        int totalPage = (int) Math.ceil((double) totalCount / sch.getPageSize());
        
        // 블록 계산 추가
        sch.setPageBlock(totalCount);

        d.addAttribute("logList", logList);
        d.addAttribute("totalCount", totalCount);
        d.addAttribute("totalPage", totalPage);
        d.addAttribute("curPage", sch.getCurPage());
        d.addAttribute("keyword", sch.getKeyword());
        d.addAttribute("startPage", sch.getStartPage());
        d.addAttribute("endPage", sch.getEndPage());

        return "/pages/exceptionLogList"; // JSP 위치
    }
    
    // 로그 상세 모달창 단건 조회
    @GetMapping("/getLogDetail")
    @ResponseBody
    public ExceptionLog getLogDetail(@RequestParam("logId") int logId) {
        return service.getLogById(logId);
    }
}
