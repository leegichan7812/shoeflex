package web.com.springweb.vo;

import java.util.Date;

public class ProductInquiryView {
	// 문의 정보
    private Integer inquiryId;
    private Integer productId;
    private String userId;
    private String inquiryTitle;
    private String inquiryContent;
    private Date inquiryDate;
    private String status;
    private Integer isPublic; // 뷰에서는 int, DB에선 NUMBER(1)
    private String viewerAdminId;

    // 상품 정보
    private String productName;
    private String imageUrl;

    // 답변 정보
    private Integer answerId;
    private String answerContent;
    private Date answerDate;
    
	public ProductInquiryView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductInquiryView(Integer inquiryId, Integer productId, String userId, String inquiryTitle,
			String inquiryContent, Date inquiryDate, String status, Integer isPublic, String viewerAdminId,
			String productName, String imageUrl, Integer answerId, String answerContent, Date answerDate) {
		super();
		this.inquiryId = inquiryId;
		this.productId = productId;
		this.userId = userId;
		this.inquiryTitle = inquiryTitle;
		this.inquiryContent = inquiryContent;
		this.inquiryDate = inquiryDate;
		this.status = status;
		this.isPublic = isPublic;
		this.viewerAdminId = viewerAdminId;
		this.productName = productName;
		this.imageUrl = imageUrl;
		this.answerId = answerId;
		this.answerContent = answerContent;
		this.answerDate = answerDate;
	}

	public Integer getInquiryId() {
		return inquiryId;
	}

	public void setInquiryId(Integer inquiryId) {
		this.inquiryId = inquiryId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
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

	public Date getInquiryDate() {
		return inquiryDate;
	}

	public void setInquiryDate(Date inquiryDate) {
		this.inquiryDate = inquiryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public String getViewerAdminId() {
		return viewerAdminId;
	}

	public void setViewerAdminId(String viewerAdminId) {
		this.viewerAdminId = viewerAdminId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}

	public Date getAnswerDate() {
		return answerDate;
	}

	public void setAnswerDate(Date answerDate) {
		this.answerDate = answerDate;
	}        
}
