package web.com.springweb.CER;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import web.com.springweb.vo.AfterSalesRequest;
import web.com.springweb.vo.PageResult;
import web.com.springweb.vo.Users;

@Controller
@RequestMapping("/after-sales")
public class CERController {
	@Autowired(required=false)
	public CERService service;
	
	@GetMapping("/list")
	public String list(@RequestParam(required = false) String status,
	                   @RequestParam(required = false) String type,
	                   @RequestParam(required = false, defaultValue = "active") String view,
	                   @RequestParam(required = false, defaultValue = "1") int page,
	                   @RequestParam(required = false, defaultValue = "10") int size,
	                   Model model) {

	    PageResult<AfterSalesRequest> pr =
	        service.getPagedList(page, size, status, type, view);

	    model.addAttribute("list", pr.getContent());
	    model.addAttribute("totalCount", pr.getTotalCount());
	    model.addAttribute("status", status);
	    model.addAttribute("type", type);
	    model.addAttribute("view", view);

	    // 페이징 정보
	    model.addAttribute("page", pr.getPage());
	    model.addAttribute("size", pr.getSize());
	    model.addAttribute("totalPages", pr.getTotalPages());
	    model.addAttribute("hasPrev", pr.isHasPrev());
	    model.addAttribute("hasNext", pr.isHasNext());

	    return "pages/CER";
	}
	    @GetMapping("/new")
	    public String form(Model model) {
	        model.addAttribute("form", new AfterSalesRequest());
	        return "pages/afterSales/form"; // /WEB-INF/views/pages/afterSales/form.jsp
	    }

	    @PostMapping("")
	    public String create(@ModelAttribute("form") AfterSalesRequest form) {
	        service.create(form);
	        return "pages\\CER";
	    }
	    
	 // 단건 JSON 조회
	    @GetMapping("/api/{reqId}")
	    @ResponseBody
	    public AfterSalesRequest getOne(@PathVariable int reqId) {
	        return service.getById(reqId); // => dao.selectById(reqId)
	    }

	    // 수정 저장 (간단히 POST)
	    // 날짜 바인딩은 "yyyy-MM-dd HH:mm" 형태면 기본 컨버터로도 처리되는 경우가 많음
	    @PostMapping("/update")
	    @ResponseBody
	    public int update(AfterSalesRequest req, HttpSession session) {
	        // 세션에서 로그인 사용자 정보 꺼내기
	        Object userObj = session.getAttribute("loginUser"); // 세션 키는 환경에 맞게
	        if (userObj != null) {
	            // 예: Users 도메인에 userId가 관리자 ID라면
	            Users loginUser = (Users) userObj;
	            req.setProcessedBy(loginUser.getUserId()); // ✅ 처리자 ID 강제 주입
	        }

	        // createdAt은 업데이트 대상 아님(DDL/mapper 참고)
	        return service.modify(req); // => dao.update(req)
	    }
	    
	    
	    /** 주문항목 조회(모달 오픈 시 요약표시/pcsId 보강) */
	    @GetMapping(value = "/api/item", produces = MediaType.APPLICATION_JSON_VALUE)
	    @ResponseBody
	    public ResponseEntity<?> getOrderItem(@RequestParam("orderItemId") long orderItemId, HttpSession session) {
	        // (선택) 소유권 검증이 필요하면 여기서 userId와 함께 검사하도록 확장
	        CerItemView view = service.findCerItemViewByOrderItemId(orderItemId);
	        if (view == null) return badText("해당 주문항목을 찾을 수 없습니다.");
	        return ResponseEntity.ok(view);
	    }
	    

	    /** 신청 등록(모달 ‘신청하기’) – x-www-form-urlencoded */
	    @PostMapping(value = "/submit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	    @ResponseBody
	    public ResponseEntity<String> submit(
	            @RequestParam("orderItemId") Long orderItemId,
	            @RequestParam("productColorSizeId") Long productColorSizeId,
	            @RequestParam("reqType") String reqType,
	            @RequestParam(value = "reasonCode", required = false) String reasonCode,
	            @RequestParam(value = "reasonDetail", required = false) String reasonDetail,
	            @RequestParam(required=false) String pickupAddress,  // ✅ 추가
	            HttpSession session) {

	        Users loginUser = (Users) session.getAttribute("loginUser");
	        if (loginUser == null) return okText("NEED_LOGIN");

	        if (orderItemId == null || orderItemId <= 0)         return okText("INVALID_ORDER_ITEM");
	        if (productColorSizeId == null || productColorSizeId <= 0) return okText("INVALID_PCS_ID");
	        if (reqType == null || reqType.trim().isEmpty())     return okText("INVALID_REQ_TYPE");

	        AfterSalesRequest req = new AfterSalesRequest();
	        req.setUserId(loginUser.getUserId());
	        req.setOrderItemId(orderItemId.intValue());
	        req.setProductColorSizeId(productColorSizeId.intValue());
	        req.setReqType(reqType);
	        req.setReasonCode(reasonCode);
	        req.setReasonDetail(reasonDetail);
	        req.setPickupAddress(pickupAddress);   

	        int rows = service.create(req);
	        if (rows == 1) {
	            return okText("OK");
	        } else if (rows == -1) {
	            return okText("ALREADY_OPEN");
	        } else {
	            return okText("ERROR");
	        }
	    }

	    // helpers
	    private ResponseEntity<String> okText(String body) {
	        return ResponseEntity.ok()
	                .contentType(new MediaType("text", "plain", StandardCharsets.UTF_8))
	                .body(body);
	    }
	    private ResponseEntity<String> badText(String body) {
	        return ResponseEntity.badRequest()
	                .contentType(new MediaType("text", "plain", StandardCharsets.UTF_8))
	                .body(body);
	    }
	    
	    /** 내가 등록한 신청 목록 (페이징) */
	    @GetMapping("/Custlist")
	    public String myList(@RequestParam(name="reqStatus", required=false) String reqStatus,
	                         @RequestParam(name="reqType",   required=false) String reqType,
	                         @RequestParam(name="page",      defaultValue="1") int page,
	                         @RequestParam(name="size",      defaultValue="10") int size,
	                         Model model,
	                         HttpSession session) {
	        Users loginUser = (Users) session.getAttribute("loginUser");
	        if (loginUser == null) return "redirect:/login";

	        PageResult<AfterSalesRequest> result =
	            service.getPagedListByUser(loginUser.getUserId(), page, size, reqStatus, reqType, "active");

	        model.addAttribute("list",        result.getContent());
	        model.addAttribute("page",        result.getPage());
	        model.addAttribute("size",        result.getSize());
	        model.addAttribute("totalPages",  result.getTotalPages());
	        model.addAttribute("totalCount",  result.getTotalCount());
	        model.addAttribute("hasPrev",     result.isHasPrev());
	        model.addAttribute("hasNext",     result.isHasNext());

	        // 필터값 유지
	        model.addAttribute("reqStatus", reqStatus);
	        model.addAttribute("reqType",   reqType);

	        return "pages\\CER_Cust"; // 네가 올린 JSP
	    }
	    
	    
	    

}
