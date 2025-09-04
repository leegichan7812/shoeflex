package web.com.springweb.vo;

public class OrderItemDto {
	private Integer cartId; // 장바구니 주문 시 사용
    private int productColorSizeId;
    private int quantity;
    private int unitPrice;
	public OrderItemDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderItemDto(Integer cartId, int productColorSizeId, int quantity, int unitPrice) {
		super();
		this.cartId = cartId;
		this.productColorSizeId = productColorSizeId;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}
	public Integer getCartId() {
		return cartId;
	}
	public void setCartId(Integer cartId) {
		this.cartId = cartId;
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
	public int getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}
}
