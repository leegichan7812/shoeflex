package web.com.springweb.MyPage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import web.com.springweb.vo.CartProduct;
import web.com.springweb.vo.PurchaseHistory;
import web.com.springweb.vo.ReviewWithFile;
import web.com.springweb.vo.Users;
import web.com.springweb.vo.WithdrawalReason;

@Mapper
public interface MyPageDao {

    /* =========================
       주문 현황 (기존: 전체 6개월치)
       ========================= */
    @Select("""
        SELECT
            o.ORDER_ID,
            o.USER_ID,
            o.ORDER_DATE AS PURCHASED_AT,
            o.ORDER_STATUS,
            pm.METHOD_NAME AS PAYMENT_METHOD,
            pc.PRODUCT_ID,
            p.NAME AS PRODUCT_NAME,
            p.IMAGE_URL,
            b.BRAND_NAME,
            c.CATEGORY_NAME,
            co.COLOR_NAME_KOR AS COLOR_NAME,
            s.SIZE_VALUE AS PRODUCT_SIZE,
            oi.QUANTITY,
            p.PRICE AS UNIT_PRICE,
            (p.PRICE * oi.QUANTITY) AS PRODUCT_TOTAL_PRICE,
            o.SHIPPING_ID AS FINAL_SHIPPING_ID,
            shi.TRACKING_NUMBER,
            shi.SHIPPING_FEE,
            shi.SHIPPING_STATUS,
            shi.SHIPPING_ADDRESS_ID,
            oi.PRODUCT_COLOR_SIZE_ID,
            oi.ORDER_ITEM_ID,
            SUM(p.PRICE * oi.QUANTITY) OVER (PARTITION BY o.ORDER_ID) + shi.SHIPPING_FEE AS TOTAL_WITH_SHIPPING
        FROM ORDERS o
        JOIN ORDER_ITEMS oi ON o.ORDER_ID = oi.ORDER_ID
        JOIN PRODUCT_COLOR_SIZES pcs ON oi.PRODUCT_COLOR_SIZE_ID = pcs.PRODUCT_COLOR_SIZE_ID
        JOIN PRODUCT_COLORS pc ON pcs.PRODUCT_COLOR_ID = pc.PRODUCT_COLOR_ID
        JOIN PRODUCTS p ON pc.PRODUCT_ID = p.PRODUCT_ID
        LEFT JOIN COLORS co ON pc.COLOR_ID = co.COLOR_ID
        LEFT JOIN SIZES s ON pcs.SIZE_ID = s.SIZE_ID
        LEFT JOIN PAYMENT_METHODS pm ON o.PAYMENT_METHOD_ID = pm.METHOD_ID
        LEFT JOIN BRANDS b ON p.BRAND_ID = b.BRAND_ID
        LEFT JOIN CATEGORIES c ON p.CATEGORY_ID = c.CATEGORY_ID
        LEFT JOIN SHIPPINGS shi ON o.SHIPPING_ID = shi.SHIPPING_ID
        WHERE o.USER_ID = #{userId}
          AND o.ORDER_DATE >= ADD_MONTHS(SYSDATE, -6)
        ORDER BY o.ORDER_DATE DESC, o.ORDER_ID DESC, oi.ORDER_ITEM_ID
        """)
    List<PurchaseHistory> getOrderList(@Param("userId") int userId);

    /* =========================
       주문 현황 - 6개월 내 "주문단위" 페이징 추가
       ========================= */
    // 6개월 내 주문 "건수"(주문ID 개수)
    @Select("""
        SELECT COUNT(*)
        FROM ORDERS o
        WHERE o.USER_ID = #{userId}
          AND o.ORDER_DATE >= ADD_MONTHS(SYSDATE, -6)
        """)
    int countOrdersIn6Months(@Param("userId") int userId);

    // 페이징: 해당 페이지의 주문ID들에 속한 아이템만 내려줌 (정렬/alias는 기존 JSP와 동일)
    @Select("""
        SELECT
            o.ORDER_ID,
            o.USER_ID,
            o.ORDER_DATE AS PURCHASED_AT,
            o.ORDER_STATUS,
            pm.METHOD_NAME AS PAYMENT_METHOD,
            pc.PRODUCT_ID,
            p.NAME AS PRODUCT_NAME,
            p.IMAGE_URL,
            b.BRAND_NAME,
            c.CATEGORY_NAME,
            co.COLOR_NAME_KOR AS COLOR_NAME,
            s.SIZE_VALUE AS PRODUCT_SIZE,
            oi.QUANTITY,
            p.PRICE AS UNIT_PRICE,
            (p.PRICE * oi.QUANTITY) AS PRODUCT_TOTAL_PRICE,
            o.SHIPPING_ID AS FINAL_SHIPPING_ID,
            shi.TRACKING_NUMBER,
            shi.SHIPPING_FEE,
            shi.SHIPPING_STATUS,
            shi.SHIPPING_ADDRESS_ID,
            oi.PRODUCT_COLOR_SIZE_ID,
            oi.ORDER_ITEM_ID,
            SUM(p.PRICE * oi.QUANTITY) OVER (PARTITION BY o.ORDER_ID) + shi.SHIPPING_FEE AS TOTAL_WITH_SHIPPING
        FROM ORDERS o
        JOIN ORDER_ITEMS oi ON o.ORDER_ID = oi.ORDER_ID
        JOIN PRODUCT_COLOR_SIZES pcs ON oi.PRODUCT_COLOR_SIZE_ID = pcs.PRODUCT_COLOR_SIZE_ID
        JOIN PRODUCT_COLORS pc ON pcs.PRODUCT_COLOR_ID = pc.PRODUCT_COLOR_ID
        JOIN PRODUCTS p ON pc.PRODUCT_ID = p.PRODUCT_ID
        LEFT JOIN COLORS co ON pc.COLOR_ID = co.COLOR_ID
        LEFT JOIN SIZES s ON pcs.SIZE_ID = s.SIZE_ID
        LEFT JOIN PAYMENT_METHODS pm ON o.PAYMENT_METHOD_ID = pm.METHOD_ID
        LEFT JOIN BRANDS b ON p.BRAND_ID = b.BRAND_ID
        LEFT JOIN CATEGORIES c ON p.CATEGORY_ID = c.CATEGORY_ID
        LEFT JOIN SHIPPINGS shi ON o.SHIPPING_ID = shi.SHIPPING_ID
        WHERE o.USER_ID = #{userId}
          AND o.ORDER_DATE >= ADD_MONTHS(SYSDATE, -6)
          AND o.ORDER_ID IN (
                WITH R AS (
                  SELECT o2.ORDER_ID,
                         ROW_NUMBER() OVER (ORDER BY o2.ORDER_DATE DESC, o2.ORDER_ID DESC) rn
                  FROM ORDERS o2
                  WHERE o2.USER_ID = #{userId}
                    AND o2.ORDER_DATE >= ADD_MONTHS(SYSDATE, -6)
                )
                SELECT ORDER_ID
                FROM R
                WHERE rn BETWEEN (#{offset} + 1) AND (#{offset} + #{limit})
          )
        ORDER BY o.ORDER_DATE DESC, o.ORDER_ID DESC, oi.ORDER_ITEM_ID
        """)
    List<PurchaseHistory> findOrderItemsForPageIn6Months(
        @Param("userId") int userId,
        @Param("offset") int offset,
        @Param("limit")  int limit
    );

    /* =========================
       회원정보/비밀번호/탈퇴
       ========================= */
    @Update({
      "<script>",
      "UPDATE USERS",
      "<set>",
      "  <if test='password != null and password != \"\"'>",
      "    PASSWORD = #{password},",
      "  </if>",
      "  NAME = #{name},",
      "  PHONE = #{phone},",
      "  ADDRESS = #{address}",
      "</set>",
      "WHERE USER_ID = #{userId}",
      "</script>"
    })
    int joinUpdate(Users upt);

    @Update("UPDATE USERS SET PASSWORD = #{encodedPw} WHERE USER_ID = #{userId}")
    int updatePasswordByUserId(@Param("userId") int userId, @Param("encodedPw") String encodedPw);

    @Select("SELECT * FROM USERS WHERE EMAIL = #{email} AND ACCOUNT_STATUS = '정상'")
    Users findByEmail(String email);

    @Select("SELECT * FROM USERS WHERE USER_ID = #{userId}")
    Users userById(@Param("userId") int userId);

    @Update("""
        UPDATE USERS
           SET ACCOUNT_STATUS = #{status},
               WITHDRAW_DATE  = SYSDATE,
               ORIGIN_EMAIL   = EMAIL,
               EMAIL          = '탈퇴회원' || #{userId} || '@deleted.com'
         WHERE USER_ID = #{userId}
        """)
    int updateAccountStatus(@Param("userId") int userId, @Param("status") String status);

    @Insert("""
        INSERT INTO WITHDRAWAL_LOG
          (LOG_ID, USER_ID, REASON_ID, ETC_REASON, WITHDRAWAL_DATE)
        VALUES
          (SEQ_WITHDRAWAL_LOG.NEXTVAL, #{userId}, #{reasonId}, #{etcReason}, SYSDATE)
        """)
    int insertWithdrawalLog(@Param("userId") int userId, @Param("reasonId") int reasonId, @Param("etcReason") String etcReason);

    @Select("""
        SELECT REASON_ID AS reasonId,
               REASON_TEXT AS reasonText
        FROM WITHDRAWAL_REASON_MASTER
        WHERE USE_YN = 'Y'
        """)
    List<WithdrawalReason> getWithdrawalReasons();

    /* =========================
       장바구니
       ========================= */
    @Select("""
        SELECT
            c.CART_ID,
            c.USER_ID,
            CASE WHEN pcs.STOCK = 0 THEN 0 ELSE c.QUANTITY END AS QUANTITY,
            p.NAME AS PRODUCT_NAME,
            p.IMAGE_URL,
            p.PRICE,
            clr.COLOR_NAME_KOR,
            s.SIZE_VALUE,
            pcs.STOCK,
            (p.PRICE * CASE WHEN pcs.STOCK = 0 THEN 0 ELSE c.QUANTITY END) AS TOTAL_PRICE
        FROM CART c
        JOIN PRODUCT_COLOR_SIZES pcs ON c.PRODUCT_COLOR_SIZE_ID = pcs.PRODUCT_COLOR_SIZE_ID
        JOIN PRODUCT_COLORS pc ON pcs.PRODUCT_COLOR_ID = pc.PRODUCT_COLOR_ID
        JOIN PRODUCTS p ON pc.PRODUCT_ID = p.PRODUCT_ID
        JOIN COLORS clr ON pc.COLOR_ID = clr.COLOR_ID
        JOIN SIZES s ON pcs.SIZE_ID = s.SIZE_ID
        WHERE c.USER_ID = #{userId}
        """)
    List<CartProduct> getCartByuser(@Param("userId") int userId);

    @Update("UPDATE CART SET QUANTITY = #{quantity} WHERE CART_ID = #{cartId}")
    void updateCartQuantity(@Param("cartId") int cartId, @Param("quantity") int quantity);

    @Select("SELECT USER_ID FROM CART WHERE CART_ID = #{cartId}")
    int getUserIdByCartId(@Param("cartId") int cartId);

    @Delete("DELETE FROM CART WHERE CART_ID = #{cartId}")
    void deleteCartItem(@Param("cartId") int cartId);

    @Select("SELECT COUNT(*) FROM CART WHERE USER_ID = #{userId}")
    int getCartItemCount(@Param("userId") int userId);

    @Select("""
        SELECT pcs.STOCK
        FROM CART c
        JOIN PRODUCT_COLOR_SIZES pcs ON c.PRODUCT_COLOR_SIZE_ID = pcs.PRODUCT_COLOR_SIZE_ID
        WHERE c.CART_ID = #{cartId}
        """)
    int getStockByCartId(@Param("cartId") int cartId);

    /* =========================
       리뷰
       ========================= */
    @Select("SELECT * FROM REVIEW_WITH_FILES WHERE USER_ID = #{userId} ORDER BY REVIEW_ID DESC")
    List<ReviewWithFile> getReviewList(@Param("userId") int userId);
}
