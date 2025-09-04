package web.com.springweb.vo;

public class SizeOption {
	private int productColorSizeId; // 장바구니 insert용 PK
    private int sizeId;
    private String sizeValue;
    private int stock;
	public SizeOption() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SizeOption(int productColorSizeId, int sizeId, String sizeValue, int stock) {
		super();
		this.productColorSizeId = productColorSizeId;
		this.sizeId = sizeId;
		this.sizeValue = sizeValue;
		this.stock = stock;
	}
	public int getProductColorSizeId() {
		return productColorSizeId;
	}
	public void setProductColorSizeId(int productColorSizeId) {
		this.productColorSizeId = productColorSizeId;
	}
	public int getSizeId() {
		return sizeId;
	}
	public void setSizeId(int sizeId) {
		this.sizeId = sizeId;
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
	
}
