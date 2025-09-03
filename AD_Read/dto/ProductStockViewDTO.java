package web.com.springweb.AD_Read.dto;


public class ProductStockViewDTO {
	private int productId;
    private String productName;
    private String brandName;
    private String categoryName;
    private int colorId;
    private String colorName;
    private String colorCode;
    private int sizeValue;
    private int stock;
    private int colorTotalStock;
    private int productTotalStock;
    private String colorStatus;
	public ProductStockViewDTO() {
		super();
		// TODO Auto-generated constructor stub
	}		
	public ProductStockViewDTO(int productId, String productName, String brandName, String categoryName, int colorId,
			String colorName, String colorCode, int sizeValue, int stock, int colorTotalStock, int productTotalStock,
			String colorStatus) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.brandName = brandName;
		this.categoryName = categoryName;
		this.colorId = colorId;
		this.colorName = colorName;
		this.colorCode = colorCode;
		this.sizeValue = sizeValue;
		this.stock = stock;
		this.colorTotalStock = colorTotalStock;
		this.productTotalStock = productTotalStock;
		this.colorStatus = colorStatus;
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
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
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
	public int getColorTotalStock() {
		return colorTotalStock;
	}
	public void setColorTotalStock(int colorTotalStock) {
		this.colorTotalStock = colorTotalStock;
	}
	public int getProductTotalStock() {
		return productTotalStock;
	}
	public void setProductTotalStock(int productTotalStock) {
		this.productTotalStock = productTotalStock;
	}
	public int getColorId() {
		return colorId;
	}
	public void setColorId(int colorId) {
		this.colorId = colorId;
	}
	public String getColorStatus() {
		return colorStatus;
	}
	public void setColorStatus(String colorStatus) {
		this.colorStatus = colorStatus;
	}		
}
