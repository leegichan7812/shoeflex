package web.com.springweb.Order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.com.springweb.vo.Order;
import web.com.springweb.vo.OrderItem;
import web.com.springweb.vo.OrderItemDto;
import web.com.springweb.vo.OrderItemEntity;
import web.com.springweb.vo.OrderRequestDto;
import web.com.springweb.vo.Payment;
import web.com.springweb.vo.Shipping;
import web.com.springweb.vo.ShippingAddress;
import web.com.springweb.vo.Users;

@Service
public class OrderService {
	@Autowired(required=false)
	private OrderDao dao;
	
	// 장바구니 → 주문용 상세 정보
	public List<OrderItem> getOrderItems(int userId) {
	    return dao.getOrderItems(userId);
	}
	// 장바구니 → 선택박스 주문용 상세 정보
	public List<OrderItem> getOrderItemsByCartIds(List<Integer> cartIds) {
	    return dao.getOrderItemsByCartIds(cartIds);
	}
	// 상품 상세 → 바로 구매
	public OrderItem getOrderItemDirect(int productColorSizeId, int quantity) {
	    OrderItem item = dao.getOrderItemByPcsId(productColorSizeId);
	    if (item != null) item.setQuantity(quantity);
	    return item;
	}

	public int getProductColorSizeId(int productId, int colorId, int sizeId) {
	    return dao.getProductColorSizeId(productId, colorId, sizeId);
	}
	// 배송지 목록
	public List<ShippingAddress> getShippingAddresses(int userId) {
	    return dao.getShippingAddresses(userId);
	}
	// 기본 배송지 1건만
	public ShippingAddress getDefaultShippingAddress(int userId) {
	    return dao.getDefaultShippingAddress(userId);
	}
	// 배송지 정보 추가
	// 기존 기본 배송지를 'N'으로 업데이트
	@Transactional
	public int insertShippingAddress(ShippingAddress address) {
	    if ("Y".equals(address.getIsDefault())) {
	        dao.resetDefaultShippingAddress(address.getUserId()); // 기존 기본 배송지 초기화
	    }
	    return dao.insertShippingAddress(address);
	}
	// 결제 수단
	public List<Payment> getAllPaymentMethods() {
        return dao.selectAllMethods();
    }
	@Transactional
	public int saveOrder(OrderRequestDto dto, Users loginUser) {
		
	    // 1. 배송 저장
	    Shipping shipping = new Shipping();
	    shipping.setShippingAddressId(dto.getShippingAddressId());
	    shipping.setShippingFee(dto.getShippingFee());
	    
	    dao.insertShipping(shipping);

	    // 2. 주문 저장
	    Order order = new Order();
	    order.setUserId(loginUser.getUserId());
	    order.setPaymentMethodId(dto.getPaymentMethodId());
	    order.setShippingId(shipping.getShippingId());
	    dao.insertOrder(order);

	    // 3. 주문상품 저장
	    for (OrderItemDto item : dto.getItems()) {
	        OrderItemEntity orderItem = new OrderItemEntity();
	        orderItem.setOrderId(order.getOrderId());
	        orderItem.setProductColorSizeId(item.getProductColorSizeId());
	        orderItem.setQuantity(item.getQuantity());
	        dao.insertOrderItemEntity(orderItem);

	        dao.updateStock(item.getProductColorSizeId(), item.getQuantity());

	        if (item.getCartId() != null) {
	            dao.deleteCart(item.getCartId());
	        }
	    }

	    return order.getOrderId(); // 반환 시 주문 완료 페이지로 이동
	}
	
}
