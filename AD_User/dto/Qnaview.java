package web.com.springweb.AD_User.dto;

import org.springframework.format.annotation.DateTimeFormat;
//categoryName inquiryTitle inquiryDate status
public class Qnaview {
	private String userId;
	private String email;          				//*-- 이메일     	1
	private int inquiryId;						//-- 문의 ID   	2
	private String productName;    			//-- 제품명	    3
	private String brandName;      				//-- 브랜드명    	4
	private String categoryName;    			//-- 카테고리    	5
	private String colorName;    				//-- 컬러      	6
	private String sizeValue;   				//-- 상품사이즈옵션  7
	private String inquiryTitle;     			//*-- 문의 제목    	8
	private String inquiryContent;				//*-- 문의 내용    	9
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String inquiryDate;					//*-- 문의 작성일   	10
	private String status;						//*-- 작성여부    	11
	private String ispublic;					//*-- 공개여부		12
	private String answerContent;				//*-- 답변내용		13
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String answerDate; 					//*-- 답변일자		14
	private String adminId;						//*-- 답변관리자		15
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getInquiryId() {
		return inquiryId;
	}
	public void setInquiryId(int inquiryId) {
		this.inquiryId = inquiryId;
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
	public String getSizeValue() {
		return sizeValue;
	}
	public void setSizeValue(String sizeValue) {
		this.sizeValue = sizeValue;
	}
	public String getInquiryTitle() {
		return inquiryTitle;
	}
	public void setInquiryTitle(String inquiryTitle) {
		this.inquiryTitle = inquiryTitle;
	}
	public String getInquiryContent() {
		return inquiryContent;
	}
	public void setInquiryContent(String inquiryContent) {
		this.inquiryContent = inquiryContent;
	}
	public String getInquiryDate() {
		return inquiryDate;
	}
	public void setInquiryDate(String inquiryDate) {
		this.inquiryDate = inquiryDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIspublic() {
		return ispublic;
	}
	public void setIspublic(String ispublic) {
		this.ispublic = ispublic;
	}
	public String getAnswerContent() {
		return answerContent;
	}
	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}
	public String getAnswerDate() {
		return answerDate;
	}
	public void setAnswerDate(String answerDate) {
		this.answerDate = answerDate;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public Qnaview(String userId, String email, int inquiryId, String productName, String brandName,
			String categoryName, String colorName, String sizeValue, String inquiryTitle, String inquiryContent,
			String inquiryDate, String status, String ispublic, String answerContent, String answerDate,
			String adminId) {
		super();
		this.userId = userId;
		this.email = email;
		this.inquiryId = inquiryId;
		this.productName = productName;
		this.brandName = brandName;
		this.categoryName = categoryName;
		this.colorName = colorName;
		this.sizeValue = sizeValue;
		this.inquiryTitle = inquiryTitle;
		this.inquiryContent = inquiryContent;
		this.inquiryDate = inquiryDate;
		this.status = status;
		this.ispublic = ispublic;
		this.answerContent = answerContent;
		this.answerDate = answerDate;
		this.adminId = adminId;
	}
	public Qnaview() {
		super();
		// TODO Auto-generated constructor stub
	}

}
	