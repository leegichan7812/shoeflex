package web.com.springweb.vo;

import java.sql.Timestamp;

public class PurchaseHistory {

    // 주문 정보
    private Long orderId;
    private Long userId;
    private String orderStatus;
    private String paymentMethod;
    private Timestamp purchasedAt;
    

    // 상품 정보
    private Long productId;
    private String productName;
    private String imageUrl;
    private String brandName;
    private String categoryName;
    private String colorName; // COLOR_NAME_KOR → VIEW 컬럼에서 COLOR_NAME으로 들어옴
    private String productSize; // 사이즈 추가
    private String productColorSizeId;
    private Long orderItemId;

    // 주문 상세
    private int quantity;
    private int unitPrice;
    private int productTotalPrice;

    // 배송 정보
    private Long finalShippingId;
    private String trackingNumber;
    private int shippingFee;
    private String shippingStatus;
    private Long shippingAddressId;

    // 총 결제 금액
    private int totalWithShipping;

    public PurchaseHistory() {
        super();
    }

    public PurchaseHistory(Long orderId, Long userId, String orderStatus, String paymentMethod, Timestamp purchasedAt,
                           Long productId, String productName, String imageUrl, String brandName, String categoryName,
                           String colorName, String productSize, int quantity, int unitPrice, int productTotalPrice,
                           Long finalShippingId, String trackingNumber, int shippingFee, String shippingStatus,
                           Long shippingAddressId, int totalWithShipping) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.purchasedAt = purchasedAt;
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
    }

    // Getter / Setter
    
    

    public Long getOrderId() {
        return orderId;
    }

    public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Timestamp getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(Timestamp purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
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

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
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

    public Long getFinalShippingId() {
        return finalShippingId;
    }

    public void setFinalShippingId(Long finalShippingId) {
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

    public Long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public int getTotalWithShipping() {
        return totalWithShipping;
    }

    public void setTotalWithShipping(int totalWithShipping) {
        this.totalWithShipping = totalWithShipping;
    }

	public String getProductColorSizeId() {
		return productColorSizeId;
	}

	public void setProductColorSizeId(String productColorSizeId) {
		this.productColorSizeId = productColorSizeId;
	}
    
    

}
