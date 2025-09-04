package web.com.springweb.WishList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import web.com.springweb.Product.ProductService;
import web.com.springweb.vo.Products;
import web.com.springweb.vo.Users;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishCotroller {

    @Autowired
    private WishService wishService;
    // 1. 찜 추가
    @PostMapping("/insert")
    public String insertWishlist(HttpSession session, @RequestParam int productId) {
        Users users = (Users) session.getAttribute("loginUser");
        if (users == null) return "unauthorized";
        wishService.addWishlist(users.getUserId(), productId);
        return "ok";
    }
    // 2. 찜 해제
    @PostMapping("/delete")
    public String deleteWishlist(HttpSession session, @RequestParam int productId) {
        Users users = (Users) session.getAttribute("loginUser");
        if (users == null) return "unauthorized";
        wishService.removeWishlist(users.getUserId(), productId);
        return "ok";
    }

    // 3. (AJAX) 특정 상품에 대해 현재 찜 여부 반환 (하트 동기화용)
    @GetMapping("/status")
    public boolean isWishlisted(HttpSession session, @RequestParam int productId) {
        Users users = (Users) session.getAttribute("loginUser");
        if (users == null) return false;
        return wishService.isWishlisted(users.getUserId(), productId);
    }

    // 4. (AJAX) 로그인 유저의 전체 찜한 상품 id 목록 (목록 뿌릴 때 사용)
    @GetMapping("/myList")
    public List<Integer> getWishProductIds(HttpSession session) {
        Users users = (Users) session.getAttribute("loginUser");
        if (users == null) return java.util.Collections.emptyList();
        return wishService.getWishProductIds(users.getUserId());
    }
    
    
    
}
