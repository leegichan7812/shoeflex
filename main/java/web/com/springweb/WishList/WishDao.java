package web.com.springweb.WishList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WishDao {

    // 1. 찜 추가 (Insert)
    @Insert("INSERT INTO TWO.WISHLIST (WISHLIST_ID, USER_ID, PRODUCT_ID, CREATED_AT) " +
            "VALUES (TWO.SEQ_WISHLIST_ID.NEXTVAL, #{userId}, #{productId}, SYSDATE)")
    int insertWishlist(@Param("userId") int userId, @Param("productId") int productId);

    // 2. 찜 해제 (Delete)
    @Delete("DELETE FROM TWO.WISHLIST WHERE USER_ID = #{userId} AND PRODUCT_ID = #{productId}")
    int deleteWishlist(@Param("userId") int userId, @Param("productId") int productId);

    // 3. 해당 상품에 대해 이미 찜했는지 여부 (Count)
    @Select("SELECT COUNT(*) FROM TWO.WISHLIST WHERE USER_ID = #{userId} AND PRODUCT_ID = #{productId}")
    int isWishlisted(@Param("userId") int userId, @Param("productId") int productId);

    // 4. (옵션) 로그인 유저의 전체 찜 목록 조회
    @Select("SELECT PRODUCT_ID FROM TWO.WISHLIST WHERE USER_ID = #{userId}")
    java.util.List<Integer> getWishProductIds(@Param("userId") int userId);
}

