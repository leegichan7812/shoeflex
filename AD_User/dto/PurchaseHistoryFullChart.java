package web.com.springweb.AD_User.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class PurchaseHistoryFullChart {
	
	private int orderId;   			
	private String userId;			
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date purchasedAt;		
	private String orderStatus	;	
	private String paymentMethod;	
	private int productId;			
	private String productName;		
	private String imageUrl;			
	private String brandName;		
	private String categoryName;		
	private String colorName;		
	private int productSize;			
	private int quantity;			
	private int unitPrice;			
	private int productTotalPrice;	
	private int finalShippingId;		
	private String trackingNumber;	
	private int shippingFee;			
	private String shippingStatus;	
	private int shippingAddressId;	
	private int totalWithShipping;
	
	private String mon;           // 월 (ex. "2025-02")
    private int salesCount;       // 판매 건수
    private long monthTot;
	public PurchaseHistoryFullChart() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PurchaseHistoryFullChart(int orderId, String userId, Date purchasedAt, String orderStatus,
			String paymentMethod, int productId, String productName, String imageUrl, String brandName,
			String categoryName, String colorName, int productSize, int quantity, int unitPrice, int productTotalPrice,
			int finalShippingId, String trackingNumber, int shippingFee, String shippingStatus, int shippingAddressId,
			int totalWithShipping, String mon, int salesCount, long monthTot) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.purchasedAt = purchasedAt;
		this.orderStatus = orderStatus;
		this.paymentMethod = paymentMethod;
		this.productId = productId;
		this.productName = productName;
		this.imageUrl = imageUrl;
		this.brandName = brandName;
		this.categoryName = categoryName;
		this.colorName = colorName;
		this.productSize = productSize;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.productTotalPrice = productTotalPrice;
		this.finalShippingId = finalShippingId;
		this.trackingNumber = trackingNumber;
		this.shippingFee = shippingFee;
		this.shippingStatus = shippingStatus;
		this.shippingAddressId = shippingAddressId;
		this.totalWithShipping = totalWithShipping;
		this.mon = mon;
		this.salesCount = salesCount;
		this.monthTot = monthTot;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getPurchasedAt() {
		return purchasedAt;
	}
	public void setPurchasedAt(Date purchasedAt) {
		this.purchasedAt = purchasedAt;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public int getProductSize() {
		return productSize;
	}
	public void setProductSize(int productSize) {
		this.productSize = productSize;
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
	public int getProductTotalPrice() {
		return productTotalPrice;
	}
	public void setProductTotalPrice(int productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}
	public int getFinalShippingId() {
		return finalShippingId;
	}
	public void setFinalShippingId(int finalShippingId) {
		this.finalShippingId = finalShippingId;
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
	public int getShippingAddressId() {
		return shippingAddressId;
	}
	public void setShippingAddressId(int shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}
	public int getTotalWithShipping() {
		return totalWithShipping;
	}
	public void setTotalWithShipping(int totalWithShipping) {
		this.totalWithShipping = totalWithShipping;
	}
	public String getMon() {
		return mon;
	}
	public void setMon(String mon) {
		this.mon = mon;
	}
	public int getSalesCount() {
		return salesCount;
	}
	public void setSalesCount(int salesCount) {
		this.salesCount = salesCount;
	}
	public long getMonthTot() {
		return monthTot;
	}
	public void setMonthTot(long monthTot) {
		this.monthTot = monthTot;
	}
    
}