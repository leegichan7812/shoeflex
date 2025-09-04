package web.com.springweb.MyPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import web.com.springweb.CER.CERService;
import web.com.springweb.Product.ProductService;
import web.com.springweb.WishList.WishService;
import web.com.springweb.vo.AfterSalesRequest;
import web.com.springweb.vo.CartProduct;
import web.com.springweb.vo.Products;
import web.com.springweb.vo.PurchaseHistory;
import web.com.springweb.vo.Users;
import web.com.springweb.vo.WithdrawalReason;

@Controller
public class MyPageController {

    @Autowired(required = false)
    private MyPageService service;
    @Autowired
    private WishService wishService;
    @Autowired
    private ProductService productService;
    @Autowired(required=false)
	public CERService cerService;
    
    

    /* =========================
       마이페이지 (주문/배송 조회) - 페이징 적용
       ========================= */
    // 예: /myPage?curPage=1&pageSize=10
    @GetMapping("myPage")
    public String gmyPage(@RequestParam(value = "curPage",  defaultValue = "1") int curPage,
                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                          HttpSession session, Model d) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        int userId = loginUser.getUserId();

        // 총 주문 건수(6개월 내, 주문ID 기준)
        int totalCount = service.getOrderTotalCount6M(userId);

        int ps = Math.max(1, pageSize);
        int totalPage = Math.max(1, (int) Math.ceil(totalCount / (double) ps));
        int cp = Math.max(1, Math.min(curPage, totalPage));

        // 블록 페이징 계산 (10개 단위 예시)
        int blockSize = 5;
        int startPage = ((cp - 1) / blockSize) * blockSize + 1;
        int endPage   = Math.min(startPage + blockSize - 1, totalPage);

        // 해당 페이지 주문의 아이템 목록 (정렬: 주문일 desc, 주문ID desc, 아이템ID)
        List<PurchaseHistory> list = service.getOrderPage6M(userId, cp, ps);
        
        // ✅ 각 orderItemId 기준으로 진행중 신청 조회
        Map<Long, AfterSalesRequest> openReqMap = new HashMap<>();
        if (list != null) {
            for (PurchaseHistory ph : list) {
                long orderItemId = ph.getOrderItemId();
                AfterSalesRequest openReq = cerService.findOpenRequestByOrderItemId(orderItemId);
                if (openReq != null) {
                    openReqMap.put(orderItemId, openReq);
                }
            }
        }        
        System.out.println(openReqMap);

        d.addAttribute("orderList", list != null ? list : new ArrayList<>());
        d.addAttribute("curPage",   cp);
        d.addAttribute("pageSize",  ps);
        d.addAttribute("totalPage", totalPage);
        d.addAttribute("startPage", startPage);
        d.addAttribute("endPage",   endPage);
        d.addAttribute("totalCount", totalCount);
        
        d.addAttribute("openReqMap", openReqMap); // ✅ 이거 꼭 필요

        return "pages/mypage";
    }

    /* =========================
       회원 정보 수정/조회
       ========================= */
    @GetMapping("joinUpdate")
    public String joinUpdate(HttpSession session, Model d) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser != null) {
            int userId = loginUser.getUserId();
            d.addAttribute("ulist", service.userById(userId));
        }
        return "pages/joinUpdate";
    }

    @PostMapping("checkPwd")
    @ResponseBody
    public String checkPwd(@RequestParam String password, HttpSession session) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "fail";

        Users dbUser = service.userById(loginUser.getUserId());
        String dbPw = dbUser.getPassword();

        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder enc =
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

        boolean ok;
        if (dbPw != null && dbPw.startsWith("$2")) {
            ok = enc.matches(password, dbPw); // BCrypt
        } else {
            ok = password.equals(dbPw);       // 레거시 평문 호환
            // 필요 시: enc.encode(password)로 업그레이드 가능
        }
        return ok ? "ok" : "fail";
    }

    @PutMapping("ajaxJoinUpdate")
    public ResponseEntity<?> ajaxJoinUpdate(@RequestBody Users upt, HttpSession session) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");

        upt.setUserId(loginUser.getUserId());
        String result = service.joinUpdate(upt);

        if ("수정성공".equals(result)) {
            Users newUser = service.userById(loginUser.getUserId());
            session.setAttribute("loginUser", newUser);
        }
        return ResponseEntity.ok(result);
    }

    /* =========================
       회원 탈퇴 / 사유
       ========================= */
    @PutMapping("ajaxWithdrawal")
    public ResponseEntity<?> withdrawal(@RequestBody Map<String, Object> reqData,
                                        HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Users loginUser = (session != null) ? (Users) session.getAttribute("loginUser") : null;
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        int reasonId = Integer.parseInt(String.valueOf(reqData.get("reasonId")));
        String etcReason = reqData.get("etcReason") != null ? String.valueOf(reqData.get("etcReason")) : "";

        service.withdrawal(loginUser.getUserId(), reasonId, etcReason);

        // ✅ 이미 무효화된 세션이면 NPE/IllegalStateException 방지
        if (session != null) {
            try { session.invalidate(); } catch (IllegalStateException ignore) {}
        }
        return ResponseEntity.ok("탈퇴완료");
    }

    @GetMapping("getWithdrawalReasons")
    @ResponseBody
    public List<WithdrawalReason> getWithdrawalReasons() {
        return service.getWithdrawalReasons();
    }

    /* =========================
       장바구니
       ========================= */
    @GetMapping("cart")
    public String cart(HttpSession session, Model d) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser != null) {
            int userId = loginUser.getUserId();
            List<CartProduct> clist = service.getCartByuser(userId);
            d.addAttribute("clist", clist);
        } else {
            d.addAttribute("clist", new ArrayList<CartProduct>());
        }
        return "pages/cart";
    }

    @PostMapping("/updateCartQuantity")
    @ResponseBody
    public Map<String, Object> updateCartQuantity(int cartId, int quantity) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = service.updateCartQuantity(cartId, quantity);
            result.put("success", true);
        } catch (IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/deleteCartItem")
    @ResponseBody
    public Map<String, Object> deleteCartItem(int cartId) {
        return service.deleteCartItem(cartId);
    }

    @GetMapping("/getCartCount")
    @ResponseBody
    public int getCartCount(HttpSession session) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        return (loginUser != null) ? service.getCartItemCount(loginUser.getUserId()) : 0;
    }

    /* =========================
       찜 목록
       ========================= */
    @GetMapping("/like")
    public String myWishListPage(HttpSession session, Model model) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        List<Integer> wishList = wishService.getWishProductIds(loginUser.getUserId());
        List<Products> myWishList = wishList.isEmpty() ? Collections.emptyList()
                : productService.getProductsByIds(wishList);

        model.addAttribute("myWishList", myWishList);
        return "pages/wishlist";
    }

    // (리뷰 페이지 필요 시 주석 해제)
    /*
    @GetMapping("review")
    public String review(HttpSession session, Model d) {
        Users loginUser = (Users)session.getAttribute("loginUser");
        if (loginUser != null) {
            int userId = loginUser.getUserId();
            List<ReviewWithFile> list = service.getReviewList(userId);
            d.addAttribute("reviewList", list);
        }
        return "pages/review";
    }
    */
}
