package web.com.springweb.vo;

import java.util.List;

public class OrderRequestDto {
	private int shippingAddressId;
    private int shippingFee;
    private int paymentMethodId;
    private List<OrderItemDto> items;
	public OrderRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderRequestDto(int shippingAddressId, int shippingFee, int paymentMethodId) {
		super();
		this.shippingAddressId = shippingAddressId;
		this.shippingFee = shippingFee;
		this.paymentMethodId = paymentMethodId;
	}
	public int getShippingAddressId() {
		return shippingAddressId;
	}
	public void setShippingAddressId(int shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}
	public int getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(int shippingFee) {
		this.shippingFee = shippingFee;
	}
	public int getPaymentMethodId() {
		return paymentMethodId;
	}
	public void setPaymentMethodId(int paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}
	public List<OrderItemDto> getItems() {
		return items;
	}
	public void setItems(List<OrderItemDto> items) {
		this.items = items;
	}
    
}
