package web.com.springweb.vo;

public class OrderItem {
	private int cartId;
    private int productId;
    private int productColorSizeId;
    private String productName;
    private String colorName;
    private String sizeValue;
    private int quantity;
    private int unitPrice;
    private String imageUrl;
	public OrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderItem(int cartId, int productId, int productColorSizeId, String productName, String colorName,
			String sizeValue, int quantity, int unitPrice, String imageUrl) {
		super();
		this.cartId = cartId;
		this.productId = productId;
		this.productColorSizeId = productColorSizeId;
		this.productName = productName;
		this.colorName = colorName;
		this.sizeValue = sizeValue;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.imageUrl = imageUrl;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductColorSizeId() {
		return productColorSizeId;
	}
	public void setProductColorSizeId(int productColorSizeId) {
		this.productColorSizeId = productColorSizeId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getSizeValue() {
		return sizeValue;
	}
	public void setSizeValue(String sizeValue) {
		this.sizeValue = sizeValue;
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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
    
}
