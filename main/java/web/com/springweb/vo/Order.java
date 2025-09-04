package web.com.springweb.vo;

import java.util.Date;

public class Order {
	private int orderId;
    private int userId;
    private Date orderDate;
    private int paymentMethodId;
    private String orderStatus;
    private int shippingId;
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Order(int orderId, int userId, Date orderDate, int paymentMethodId, String orderStatus, int shippingId) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.orderDate = orderDate;
		this.paymentMethodId = paymentMethodId;
		this.orderStatus = orderStatus;
		this.shippingId = shippingId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public int getPaymentMethodId() {
		return paymentMethodId;
	}
	public void setPaymentMethodId(int paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getShippingId() {
		return shippingId;
	}
	public void setShippingId(int shippingId) {
		this.shippingId = shippingId;
	}
    
}
