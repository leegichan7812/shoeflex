package web.com.springweb.vo;

import java.sql.Date;

public class ProductInquiry {
	
    private int inquiryId;
    private int productId;
    private int userId;
    private String inquiryTitle;
    private String inquiryContent;
    private java.sql.Timestamp inquiryDate;
    private String status;
    private int isPublic;

    private String productName;
    private String productImageUrl;
    
    private String answerId;   
    private String answerContent;
    private java.sql.Timestamp answerDate;
    
    private String OrderBy;
    
    

	public ProductInquiry() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getInquiryId() {
		return inquiryId;
	}

	public void setInquiryId(int inquiryId) {
		this.inquiryId = inquiryId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public java.sql.Timestamp getInquiryDate() {
		return inquiryDate;
	}

	public void setInquiryDate(java.sql.Timestamp inquiryDate) {
		this.inquiryDate = inquiryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(int isPublic) {
		this.isPublic = isPublic;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public String getAnswerId() {
		return answerId;
	}

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}

	public java.sql.Timestamp getAnswerDate() {
		return answerDate;
	}

	public void setAnswerDate(java.sql.Timestamp answerDate) {
		this.answerDate = answerDate;
	}

	public String getOrderBy() {
		return OrderBy;
	}

	public void setOrderBy(String orderBy) {
		OrderBy = orderBy;
	}
    
    
	
    
}