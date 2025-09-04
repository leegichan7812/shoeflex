package web.com.springweb.vo;

public class OrderItemEntity {
	private int orderItemId;             // 주문 상세 PK
    private int orderId;                 // 주문 ID (FK)
    private int productColorSizeId;      // 상품 색상/사이즈 ID (FK)
    private int quantity;                // 수량
	public OrderItemEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderItemEntity(int orderItemId, int orderId, int productColorSizeId, int quantity) {
		super();
		this.orderItemId = orderItemId;
		this.orderId = orderId;
		this.productColorSizeId = productColorSizeId;
		this.quantity = quantity;
	}
	public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getProductColorSizeId() {
		return productColorSizeId;
	}
	public void setProductColorSizeId(int productColorSizeId) {
		this.productColorSizeId = productColorSizeId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
}
