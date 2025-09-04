package web.com.springweb.WishList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishService {
	
    @Autowired
    private WishDao wishlistDao;

    // 1. 찜 추가
    public void addWishlist(int userId, int productId) {
        wishlistDao.insertWishlist(userId, productId);
    }

    // 2. 찜 해제
    public void removeWishlist(int userId, int productId) {
        wishlistDao.deleteWishlist(userId, productId);
    }

    // 3. 해당 상품에 대해 이미 찜했는지 여부 (상세/목록에서 하트 표시용)
    public boolean isWishlisted(int userId, int productId) {
        return wishlistDao.isWishlisted(userId, productId) > 0;
    }

    // 4. 유저가 찜한 모든 상품 ID 리스트 (목록/필터에 필요)
    public List<Integer> getWishProductIds(int userId) {
        return wishlistDao.getWishProductIds(userId);
    }
}
