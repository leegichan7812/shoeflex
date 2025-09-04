package web.com.springweb.vo;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class Review {
    private int reviewId;
    private int userId;
    private int pcsi;
    private int rating;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    
    private List<MultipartFile> reports;
    
    
    

	public Review() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Review(int reviewId, int userId, int pcsi, int rating, String content, Date createdAt, Date updatedAt) {
		super();
		this.reviewId = reviewId;
		this.userId = userId;
		this.pcsi = pcsi;
		this.rating = rating;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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

	public int getpcsi() {
		return pcsi;
	}

	public void setpcsi(int pcsi) {
		this.pcsi = pcsi;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
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

	public List<MultipartFile> getReports() {
		return reports;
	}

	public void setReports(List<MultipartFile> reports) {
		this.reports = reports;
	}
    
	
}