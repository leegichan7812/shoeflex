package web.com.springweb.AD_User.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ReviewWithFiles {
	// productId	rating	content	createdAt
		private String userId;
		private int reviewId;    		
		private int productId;   		
		private double rating;			
		private String content;    		
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date createdAt;			
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date updatedAt;			
		private int fileId;				
		private String fName;			
		private String etc;				
		private int sizeValue;			
		private String colorNameEng; 	
		private String colorNameKor; 	
		private String productName;     	
		private String brandName;	  	
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public int getReviewId() {
			return reviewId;
		}
		public void setReviewId(int reviewId) {
			this.reviewId = reviewId;
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
		public int getFileId() {
			return fileId;
		}
		public void setFileId(int fileId) {
			this.fileId = fileId;
		}
		public String getfName() {
			return fName;
		}
		public void setfName(String fName) {
			this.fName = fName;
		}
		public String getEtc() {
			return etc;
		}
		public void setEtc(String etc) {
			this.etc = etc;
		}
		public int getSizeValue() {
			return sizeValue;
		}
		public void setSizeValue(int sizeValue) {
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
		public ReviewWithFiles(String userId, int reviewId, int productId, double rating, String content,
				Date createdAt, Date updatedAt, int fileId, String fName, String etc, int sizeValue,
				String colorNameEng, String colorNameKor, String productName, String brandName, String categoryName,
				String userName) {
			super();
			this.userId = userId;
			this.reviewId = reviewId;
			this.productId = productId;
			this.rating = rating;
			this.content = content;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
			this.fileId = fileId;
			this.fName = fName;
			this.etc = etc;
			this.sizeValue = sizeValue;
			this.colorNameEng = colorNameEng;
			this.colorNameKor = colorNameKor;
			this.productName = productName;
			this.brandName = brandName;
			this.categoryName = categoryName;
			this.userName = userName;
		}
		public ReviewWithFiles() {
			super();
			// TODO Auto-generated constructor stub
		}
		private String categoryName;    	
		private String userName;
}
