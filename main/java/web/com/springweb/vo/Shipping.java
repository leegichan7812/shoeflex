package web.com.springweb.vo;

import java.util.Date;

public class Shipping {
	private int shippingId;
    private String trackingNumber;
    private int shippingFee;
    private String shippingStatus; // ex: "배송준비중", "배송중", ...
    private Date createdAt;
    private int shippingAddressId;
	public Shipping() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Shipping(int shippingId, String trackingNumber, int shippingFee, String shippingStatus, Date createdAt,
			int shippingAddressId) {
		super();
		this.shippingId = shippingId;
		this.trackingNumber = trackingNumber;
		this.shippingFee = shippingFee;
		this.shippingStatus = shippingStatus;
		this.createdAt = createdAt;
		this.shippingAddressId = shippingAddressId;
	}
	public int getShippingId() {
		return shippingId;
	}
	public void setShippingId(int shippingId) {
		this.shippingId = shippingId;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public int getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(int shippingFee) {
		this.shippingFee = shippingFee;
	}
	public String getShippingStatus() {
		return shippingStatus;
	}
	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public int getShippingAddressId() {
		return shippingAddressId;
	}
	public void setShippingAddressId(int shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}
    
}
