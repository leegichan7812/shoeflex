package web.com.springweb.AD_Read.dto;

public class ProductColorStockDTO {
	private int productId;
    private String productName;
    private int colorId;
    private String colorName;
    private String colorCode;
    private int colorTotalStock;
    private String colorStatus;
	public ProductColorStockDTO() {
		super();
		// TODO Auto-generated constructor stub
	}		
	public ProductColorStockDTO(int productId, String productName, int colorId, String colorName, String colorCode,
			int colorTotalStock, String colorStatus) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.colorId = colorId;
		this.colorName = colorName;
		this.colorCode = colorCode;
		this.colorTotalStock = colorTotalStock;
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
	public int getColorTotalStock() {
		return colorTotalStock;
	}
	public void setColorTotalStock(int colorTotalStock) {
		this.colorTotalStock = colorTotalStock;
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
