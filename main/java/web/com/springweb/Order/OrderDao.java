package web.com.springweb.Order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import web.com.springweb.vo.OrderItem;
import web.com.springweb.vo.OrderItemEntity;
import web.com.springweb.vo.Payment;
import web.com.springweb.vo.ShippingAddress;

@Mapper
public interface OrderDao {
	// 장바구니 → 선택박스 상세 정보
	List<OrderItem> getOrderItemsByCartIds(@Param("cartIds") List<Integer> cartIds);
	// 장바구니 → 주문용 상세 정보
	@Select("""
	    SELECT 
		    c.CART_ID,
		    pc.PRODUCT_ID,
		    p.NAME AS PRODUCT_NAME,
		    co.COLOR_NAME_KOR AS COLOR_NAME,
		    s.SIZE_VALUE,
		    c.QUANTITY,
		    p.PRICE AS UNIT_PRICE,
		    p.IMAGE_URL,
		    pcs.PRODUCT_COLOR_SIZE_ID
		FROM CART c
		JOIN PRODUCT_COLOR_SIZES pcs ON c.PRODUCT_COLOR_SIZE_ID = pcs.PRODUCT_COLOR_SIZE_ID
		JOIN PRODUCT_COLORS pc ON pcs.PRODUCT_COLOR_ID = pc.PRODUCT_COLOR_ID
		JOIN PRODUCTS p ON pc.PRODUCT_ID = p.PRODUCT_ID
		JOIN COLORS co ON pc.COLOR_ID = co.COLOR_ID
		JOIN SIZES s ON pcs.SIZE_ID = s.SIZE_ID
		WHERE c.USER_ID = #{userId}
	""")
	List<OrderItem> getOrderItems(@Param("userId") int userId);
	// 상품 상세 → 바로 구매
	@Select("""
	    SELECT 
	        pc.PRODUCT_ID,
	        p.NAME AS PRODUCT_NAME,
	        co.COLOR_NAME_KOR AS COLOR_NAME,
	        s.SIZE_VALUE,
	        p.PRICE AS UNIT_PRICE,
	        p.IMAGE_URL,
	        pcs.PRODUCT_COLOR_SIZE_ID
	    FROM PRODUCT_COLOR_SIZES pcs
	    JOIN PRODUCT_COLORS pc ON pcs.PRODUCT_COLOR_ID = pc.PRODUCT_COLOR_ID
	    JOIN PRODUCTS p ON pc.PRODUCT_ID = p.PRODUCT_ID
	    JOIN COLORS co ON pc.COLOR_ID = co.COLOR_ID
	    JOIN SIZES s ON pcs.SIZE_ID = s.SIZE_ID
	    WHERE pcs.PRODUCT_COLOR_SIZE_ID = #{pcsId}
	""")
	OrderItem getOrderItemByPcsId(@Param("pcsId") int pcsId);

	@Select("""
	    SELECT pcs.PRODUCT_COLOR_SIZE_ID
	    FROM PRODUCT_COLOR_SIZES pcs
	    JOIN PRODUCT_COLORS pc ON pcs.PRODUCT_COLOR_ID = pc.PRODUCT_COLOR_ID
	    WHERE pc.PRODUCT_ID = #{productId}
	      AND pc.COLOR_ID = #{colorId}
	      AND pcs.SIZE_ID = #{sizeId}
	""")
	int getProductColorSizeId(@Param("productId") int productId,
	                          @Param("colorId") int colorId,
	                          @Param("sizeId") int sizeId);
	// 배송지 목록
	@Select("SELECT * FROM SHIPPING_ADDRESS WHERE USER_ID = #{userId}")
	List<ShippingAddress> getShippingAddresses(@Param("userId") int userId);

	// 기본 배송지 1건만
	@Select("SELECT * FROM SHIPPING_ADDRESS WHERE USER_ID = #{userId} AND IS_DEFAULT = 'Y'")
	ShippingAddress getDefaultShippingAddress(@Param("userId") int userId);
	
	// 배송지 정보 추가
	@Insert("""
	    INSERT INTO SHIPPING_ADDRESS (
	        ADDRESS_ID, USER_ID, ADDRESS_NAME, RECEIVER_NAME,
	        RECEIVER_PHONE, ZIPCODE, ADDRESS1, ADDRESS2,
	        DELIVERY_MEMO, IS_DEFAULT, LAST_USED_AT
	    ) VALUES (
	        seq_shipping_address_id.NEXTVAL, #{userId}, #{addressName}, #{receiverName},
	        #{receiverPhone}, #{zipcode}, #{address1}, #{address2},
	        #{deliveryMemo}, #{isDefault}, SYSDATE
	    )
	""")
	int insertShippingAddress(ShippingAddress address);
	// 결제 수단
	@Select("SELECT METHOD_ID, METHOD_NAME, METHOD_IMG FROM PAYMENT_METHODS ORDER BY METHOD_ID")
    List<Payment> selectAllMethods();
	
	// 1. 배송 정보 저장
	int insertShipping(web.com.springweb.vo.Shipping shipping);

	// 2. 주문 정보 저장
	int insertOrder(web.com.springweb.vo.Order order);

	// 3. 주문 상품 저장
	int insertOrderItemEntity(OrderItemEntity entity);

	// 4. 재고 감소
	int updateStock(@Param("pcsId") int productColorSizeId, @Param("qty") int quantity);

	// 5. 장바구니 삭제
	int deleteCart(@Param("cartId") int cartId);
	
	// 기존 기본 배송지를 'N'으로 업데이트
	@Update("""
	    UPDATE SHIPPING_ADDRESS
	    SET IS_DEFAULT = 'N'
	    WHERE USER_ID = #{userId}
	      AND IS_DEFAULT = 'Y'
	""")
	int resetDefaultShippingAddress(@Param("userId") int userId);
}
