package web.com.springweb.vo;

import java.util.List;

public class ProductDetail {
	private int productId;
    private String name;
    private int price;
    private String imageUrl;
    private String brandName;
    private String categoryName;
    // 색상 리스트
    private List<proDetailColor> colorList;
	public ProductDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProductDetail(int productId, String name, int price, String imageUrl, String brandName,
			String categoryName) {
		super();
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
		this.brandName = brandName;
		this.categoryName = categoryName;
	}
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
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
	public List<proDetailColor> getColorList() {
		return colorList;
	}
	public void setColorList(List<proDetailColor> colorList) {
		this.colorList = colorList;
	}
    
}
