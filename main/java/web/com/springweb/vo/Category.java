package web.com.springweb.vo;

public class Category {
	
	private int categoryId;
    private String categoryName;
    private String categoryUrl;
	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Category(int categoryId, String categoryName, String categoryUrl) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryUrl = categoryUrl;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryUrl() {
		return categoryUrl;
	}
	public void setCategoryUrl(String categoryUrl) {
		this.categoryUrl = categoryUrl;
	}    
}
