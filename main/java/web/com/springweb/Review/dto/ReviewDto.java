package web.com.springweb.Review.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import web.com.springweb.vo.ReviewFileDto;

public class ReviewDto {
    private int reviewId;
    private int userId;
    private int productId;
    private double rating;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private String sizeValue;
    private String colorNameKor;
    private String colorNameEng;
    private String productName;
    private String brandName;
    private String categoryName;
    private String userName;
    private List<String> imageNames; // 이미지 파일명들(슬라이드용)
    private List<ReviewFileDto> imageFiles = new ArrayList<>();
    
	public ReviewDto() {
		super();
		// TODO Auto-generated constructor stub
	}    
	public ReviewDto(int reviewId, int userId, int productId, double rating, String content, Date createdAt,
			Date updatedAt, String sizeValue, String colorNameKor, String colorNameEng, String productName,
			String brandName, String categoryName, String userName, List<String> imageNames) {
		super();
		this.reviewId = reviewId;
		this.userId = userId;
		this.productId = productId;
		this.rating = rating;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.sizeValue = sizeValue;
		this.colorNameKor = colorNameKor;
		this.colorNameEng = colorNameEng;
		this.productName = productName;
		this.brandName = brandName;
		this.categoryName = categoryName;
		this.userName = userName;
		this.imageNames = imageNames;
	}

	// --- Getter/Setter ---
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public String getSizeValue() { return sizeValue; }
    public void setSizeValue(String sizeValue) { this.sizeValue = sizeValue; }

    public String getColorNameKor() { return colorNameKor; }
    public void setColorNameKor(String colorNameKor) { this.colorNameKor = colorNameKor; }

    public String getColorNameEng() { return colorNameEng; }
    public void setColorNameEng(String colorNameEng) { this.colorNameEng = colorNameEng; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public List<String> getImageNames() { return imageNames; }
    public void setImageNames(List<String> imageNames) { this.imageNames = imageNames; }
    
    public List<ReviewFileDto> getImageFiles() {
		return imageFiles;
	}
	public void setImageFiles(List<ReviewFileDto> imageFiles) {
		this.imageFiles = imageFiles;
	}
}

