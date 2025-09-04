package web.com.springweb.vo;

import java.sql.Date;

public class ReviewFileDto {
	
	private String fileId;
    private String reviewId;
    private String fname;
    private String etc;
    private Date regDte;
    private Date uptDte;
    
	public ReviewFileDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReviewFileDto(String fname, String etc) {
		super();
		this.fname = fname;
		this.etc = etc;
	}
	
	

	public ReviewFileDto(String fileId, String reviewId, String fname, String etc, Date regDte, Date uptDte) {
		super();
		this.fileId = fileId;
		this.reviewId = reviewId;
		this.fname = fname;
		this.etc = etc;
		this.regDte = regDte;
		this.uptDte = uptDte;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
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

	public Date getRegDte() {
		return regDte;
	}

	public void setRegDte(Date regDte) {
		this.regDte = regDte;
	}

	public Date getUptDte() {
		return uptDte;
	}

	public void setUptDte(Date uptDte) {
		this.uptDte = uptDte;
	}
    
    
    
}