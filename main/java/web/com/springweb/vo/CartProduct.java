package web.com.springweb.vo;

public class CartProduct {
	private int cartId;
    private int userId;
    private int quantity;
    private String productName;
    private String imageUrl;
    private int price; // 개별 상품 가격 추가
    private String colorNameKor;
    private String sizeValue;
    private int stock;
    private int totalPrice; // (개별가격 × 수량) 추가
	public CartProduct() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CartProduct(int cartId, int userId, int quantity, String productName, String imageUrl, int price,
			String colorNameKor, String sizeValue, int stock, int totalPrice) {
		super();
		this.cartId = cartId;
		this.userId = userId;
		this.quantity = quantity;
		this.productName = productName;
		this.imageUrl = imageUrl;
		this.price = price;
		this.colorNameKor = colorNameKor;
		this.sizeValue = sizeValue;
		this.stock = stock;
		this.totalPrice = totalPrice;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getColorNameKor() {
		return colorNameKor;
	}
	public void setColorNameKor(String colorNameKor) {
		this.colorNameKor = colorNameKor;
	}
	public String getSizeValue() {
		return sizeValue;
	}
	public void setSizeValue(String sizeValue) {
		this.sizeValue = sizeValue;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
