package web.com.springweb.AD_Update.dto;

public class UpdateReq {
	private int productId;
    private int colorId;
    private int sizeValue;
    private int stock;
	public UpdateReq() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UpdateReq(int productId, int colorId, int sizeValue, int stock) {
		super();
		this.productId = productId;
		this.colorId = colorId;
		this.sizeValue = sizeValue;
		this.stock = stock;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getColorId() {
		return colorId;
	}
	public void setColorId(int colorId) {
		this.colorId = colorId;
	}
	public int getSizeValue() {
		return sizeValue;
	}
	public void setSizeValue(int sizeValue) {
		this.sizeValue = sizeValue;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
}
