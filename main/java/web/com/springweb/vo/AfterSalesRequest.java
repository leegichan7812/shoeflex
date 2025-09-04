package web.com.springweb.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AfterSalesRequest {	
    private int reqId;
    private int userId;
    private String  reqType;       // '취소','반품','교환'
    private String  reqStatus;     // 기본 '신청'
    private String  reasonCode;    // 단순변심/불량/오배송...
    private String  reasonDetail;
    private String  pickupAddress;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private Date approvedAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private Date completedAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private Date createdAt;
    private int processedBy;   // 관리자 ID
    private int productColorSizeId;
    private int orderItemId;
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private Date archivedAt;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private Date deletedAt;
    private String rejectReason;

    /** 'Y' or 'N' */
    private String isDeleted;

    
	public AfterSalesRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AfterSalesRequest(int reqId, int userId, String reqType, String reqStatus, String reasonCode,
			String reasonDetail, String pickupAddress, Date approvedAt, Date completedAt, Date createdAt,
			int processedBy, int productColorSizeId, int orderItemId, Date archivedAt, Date deletedAt,
			String rejectReason, String isDeleted) {
		super();
		this.reqId = reqId;
		this.userId = userId;
		this.reqType = reqType;
		this.reqStatus = reqStatus;
		this.reasonCode = reasonCode;
		this.reasonDetail = reasonDetail;
		this.pickupAddress = pickupAddress;
		this.approvedAt = approvedAt;
		this.completedAt = completedAt;
		this.createdAt = createdAt;
		this.processedBy = processedBy;
		this.productColorSizeId = productColorSizeId;
		this.orderItemId = orderItemId;
		this.archivedAt = archivedAt;
		this.deletedAt = deletedAt;
		this.rejectReason = rejectReason;
		this.isDeleted = isDeleted;
	}
	public int getReqId() {
		return reqId;
	}
	public void setReqId(int reqId) {
		this.reqId = reqId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getReqStatus() {
		return reqStatus;
	}
	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReasonDetail() {
		return reasonDetail;
	}
	public void setReasonDetail(String reasonDetail) {
		this.reasonDetail = reasonDetail;
	}
	public String getPickupAddress() {
		return pickupAddress;
	}
	public void setPickupAddress(String pickupAddress) {
		this.pickupAddress = pickupAddress;
	}
	public Date getApprovedAt() {
		return approvedAt;
	}
	public void setApprovedAt(Date approvedAt) {
		this.approvedAt = approvedAt;
	}
	public Date getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public int getProcessedBy() {
		return processedBy;
	}
	public void setProcessedBy(int processedBy) {
		this.processedBy = processedBy;
	}
	public int getProductColorSizeId() {
		return productColorSizeId;
	}
	public void setProductColorSizeId(int productColorSizeId) {
		this.productColorSizeId = productColorSizeId;
	}
	public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}
	public Date getArchivedAt() {
		return archivedAt;
	}
	public void setArchivedAt(Date archivedAt) {
		this.archivedAt = archivedAt;
	}
	public Date getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}





}
