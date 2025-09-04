package web.com.springweb.MyPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.vo.CartProduct;
import web.com.springweb.vo.PurchaseHistory;
import web.com.springweb.vo.ReviewWithFile;
import web.com.springweb.vo.Users;
import web.com.springweb.vo.WithdrawalReason;

@Service
public class MyPageService {

    @Autowired(required = false)
    private MyPageDao dao;

    private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder =
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    /* =========================
       주문 현황 (기존: 6개월치 전체)
       ========================= */
    public List<PurchaseHistory> getHistory(int userId) {
        List<PurchaseHistory> result = dao.getOrderList(userId);
        return result != null ? result : new ArrayList<>();
    }

    /* =========================
       주문 현황 - 6개월 내 "주문단위 페이징" 추가
       ========================= */
    /** 6개월 내 주문 총 건수(주문ID 기준) */
    public int getOrderTotalCount6M(int userId) {
        return dao.countOrdersIn6Months(userId);
    }

    /** 6개월 내 주문 페이징: curPage/pageSize로 호출 (주문ID 단위로 끊어서 해당 주문들의 아이템 반환) */
    public List<PurchaseHistory> getOrderPage6M(int userId, int curPage, int pageSize) {
        int ps = pageSize > 0 ? pageSize : 10;
        int cp = curPage  > 0 ? curPage  : 1;
        int offset = (cp - 1) * ps;
        List<PurchaseHistory> result = dao.findOrderItemsForPageIn6Months(userId, offset, ps);
        return result != null ? result : new ArrayList<>();
    }

    /** 필요 시: offset/limit 직접형 */
    public List<PurchaseHistory> getOrderPage6MByOffset(int userId, int offset, int limit) {
        int off = Math.max(0, offset);
        int lim = Math.max(1, limit);
        List<PurchaseHistory> result = dao.findOrderItemsForPageIn6Months(userId, off, lim);
        return result != null ? result : new ArrayList<>();
    }

    /* =========================
       회원 정보 수정
       ========================= */
    public String joinUpdate(Users upt) {
        // 비번 입력이 있으면 인코딩, 없으면 null/빈문자 → DAO 동적 SQL에서 PASSWORD 제외
        if (upt.getPassword() != null && !upt.getPassword().trim().isEmpty()) {
            upt.setPassword(encoder.encode(upt.getPassword().trim()));
        } else {
            upt.setPassword(null);
        }
        return dao.joinUpdate(upt) > 0 ? "수정성공" : "수정실패";
    }

    public Users userById(int userId) {
        return dao.userById(userId);
    }

    // Bcrypt (필요 시 사용)
    private boolean isBcrypt(String v){
        return v != null && v.length() >= 60 && v.startsWith("$2");
    }

    /* =========================
       회원 탈퇴
       ========================= */
    public String withdrawal(int userId, int reasonId, String etcReason) {
        int uptStatus = dao.updateAccountStatus(userId, "탈퇴");
        int logResult = dao.insertWithdrawalLog(userId, reasonId, etcReason);
        return (uptStatus > 0 && logResult > 0) ? "탈퇴성공" : "탈퇴실패";
    }

    public List<WithdrawalReason> getWithdrawalReasons() {
        return dao.getWithdrawalReasons();
    }

    /* =========================
       장바구니
       ========================= */
    public List<CartProduct> getCartByuser(int userId){
        return dao.getCartByuser(userId);
    }

    // 상품 수량 업데이트 (재고 검증 포함)
    public Map<String, Object> updateCartQuantity(int cartId, int quantity) {
        int stock = dao.getStockByCartId(cartId);
        if (quantity > stock) {
            throw new IllegalArgumentException("재고를 초과할 수 없습니다.");
        }

        dao.updateCartQuantity(cartId, quantity);

        int userId = dao.getUserIdByCartId(cartId);
        List<CartProduct> cartList = dao.getCartByuser(userId);

        int totalCount = 0;
        int totalPrice = 0;

        for (CartProduct cp : cartList) {
            if (cp.getStock() == 0) {
                cp.setQuantity(0);
                cp.setTotalPrice(0);
                dao.updateCartQuantity(cp.getCartId(), 0); // DB 반영
            }
            totalCount += cp.getQuantity();
            totalPrice += cp.getTotalPrice();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", totalCount);
        result.put("totalPrice", totalPrice);
        return result;
    }

    public Map<String, Object> deleteCartItem(int cartId) {
        int userId = dao.getUserIdByCartId(cartId); // 삭제 전 조회
        dao.deleteCartItem(cartId);

        List<CartProduct> cartList = dao.getCartByuser(userId);
        int totalCount = 0;
        int totalPrice = 0;
        for (CartProduct cp : cartList) {
            totalCount += cp.getQuantity();
            totalPrice += cp.getTotalPrice();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", totalCount);
        result.put("totalPrice", totalPrice);
        return result;
    }

    public int getCartItemCount(int userId) {
        return dao.getCartItemCount(userId);
    }

    /* =========================
       리뷰
       ========================= */
    public List<ReviewWithFile> getReviewList(int userId) {
        List<ReviewWithFile> result = dao.getReviewList(userId);
        return result != null ? result : new ArrayList<>();
    }
}
