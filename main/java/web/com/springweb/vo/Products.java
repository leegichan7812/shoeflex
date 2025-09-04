package web.com.springweb.vo;

import java.util.Date;
import java.util.List;

public class Products {
	
	private int productId;
    private String name;
    private String description;
    private double price;
    private int brandId;
    private int categoryId;
    private String imageUrl;
    private Date createdAt;
    
    // 추가: 컬러/사이즈
    private List<Color> colorList;
    private List<Size> sizeList;   
    
    // ⭐ 추가 : 찜 여부
    private boolean wished;
	

	public Products() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Products(int productId, String name, String description, double price, int brandId,
			int categoryId, String imageUrl, Date createdAt) {
		super();
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.brandId = brandId;
		this.categoryId = categoryId;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
	}
	
	public Products(int productId, String name, String description, double price, int brandId, int categoryId,
			String imageUrl, Date createdAt, List<Color> colorList, List<Size> sizeList) {
		super();
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.brandId = brandId;
		this.categoryId = categoryId;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
		this.colorList = colorList;
		this.sizeList = sizeList;
	}
	

	public Products(int productId, String name, String description, double price, int brandId, int categoryId,
			String imageUrl, Date createdAt, List<Color> colorList, List<Size> sizeList, boolean wished) {
		super();
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.brandId = brandId;
		this.categoryId = categoryId;
		this.imageUrl = imageUrl;
		this.createdAt = createdAt;
		this.colorList = colorList;
		this.sizeList = sizeList;
		this.wished = wished;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public List<Color> getColorList() {
		return colorList;
	}

	public void setColorList(List<Color> colorList) {
		this.colorList = colorList;
	}

	public List<Size> getSizeList() {
		return sizeList;
	}

	public void setSizeList(List<Size> sizeList) {
		this.sizeList = sizeList;
	}

	public boolean isWished() {
		return wished;
	}

	public void setWished(boolean wished) {
		this.wished = wished;
	}
	
}
