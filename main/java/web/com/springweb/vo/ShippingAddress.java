package web.com.springweb.vo;

import java.sql.Date;

public class ShippingAddress {
	private int addressId;
    private int userId;
    private String addressName;
    private String receiverName;
    private String receiverPhone;
    private String zipcode;
    private String address1;
    private String address2;
    private String deliveryMemo;
    private String isDefault;
    private java.sql.Date lastUsedAt;
	public ShippingAddress() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ShippingAddress(int addressId, int userId, String addressName, String receiverName, String receiverPhone,
			String zipcode, String address1, String address2, String deliveryMemo, String isDefault, Date lastUsedAt) {
		super();
		this.addressId = addressId;
		this.userId = userId;
		this.addressName = addressName;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.zipcode = zipcode;
		this.address1 = address1;
		this.address2 = address2;
		this.deliveryMemo = deliveryMemo;
		this.isDefault = isDefault;
		this.lastUsedAt = lastUsedAt;
	}
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getDeliveryMemo() {
		return deliveryMemo;
	}
	public void setDeliveryMemo(String deliveryMemo) {
		this.deliveryMemo = deliveryMemo;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public java.sql.Date getLastUsedAt() {
		return lastUsedAt;
	}
	public void setLastUsedAt(java.sql.Date lastUsedAt) {
		this.lastUsedAt = lastUsedAt;
	}
    
}
