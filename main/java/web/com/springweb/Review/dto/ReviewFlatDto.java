package web.com.springweb.Review.dto;

import java.util.Date;

public class ReviewFlatDto {
	private int reviewId;
    private int userId;
    private int productId;
    private double rating;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private Integer fileId;
    private String fname;
    private String etc;
    private String sizeValue;
    private String colorNameEng;
    private String colorNameKor;
    private String productName;
    private String brandName;
    private String categoryName;
    private String userName;
    
	public ReviewFlatDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReviewFlatDto(int reviewId, int userId, int productId, double rating, String content, Date createdAt,
			Date updatedAt, Integer fileId, String fname, String etc, String sizeValue, String colorNameEng,
			String colorNameKor, String productName, String brandName, String categoryName, String userName) {
		super();
		this.reviewId = reviewId;
		this.userId = userId;
		this.productId = productId;
		this.rating = rating;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.fileId = fileId;
		this.fname = fname;
		this.etc = etc;
		this.sizeValue = sizeValue;
		this.colorNameEng = colorNameEng;
		this.colorNameKor = colorNameKor;
		this.productName = productName;
		this.brandName = brandName;
		this.categoryName = categoryName;
		this.userName = userName;
	}
	public int getReviewId() {
		return reviewId;
	}
	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	public String getSizeValue() {
		return sizeValue;
	}
	public void setSizeValue(String sizeValue) {
		this.sizeValue = sizeValue;
	}
	public String getColorNameEng() {
		return colorNameEng;
	}
	public void setColorNameEng(String colorNameEng) {
		this.colorNameEng = colorNameEng;
	}
	public String getColorNameKor() {
		return colorNameKor;
	}
	public void setColorNameKor(String colorNameKor) {
		this.colorNameKor = colorNameKor;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}    
}
