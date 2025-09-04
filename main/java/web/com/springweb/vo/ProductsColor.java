package web.com.springweb.vo;

public class ProductsColor {
	private Integer productColorId; // 자동생성
    private Integer productId;
    private Integer colorId;
	public ProductsColor() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProductsColor(Integer productColorId, Integer productId, Integer colorId) {
		super();
		this.productColorId = productColorId;
		this.productId = productId;
		this.colorId = colorId;
	}
	public Integer getProductColorId() {
		return productColorId;
	}
	public void setProductColorId(Integer productColorId) {
		this.productColorId = productColorId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getColorId() {
		return colorId;
	}
	public void setColorId(Integer colorId) {
		this.colorId = colorId;
	}
    
}
