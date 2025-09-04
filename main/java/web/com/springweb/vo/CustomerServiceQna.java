package web.com.springweb.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CustomerServiceQna {
	private int qnaId;
	private int userId;
	private String category;
	private String title;
	private String content;
	private String status;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdAt;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updatedAt;
	public CustomerServiceQna() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerServiceQna(int qnaId, int userId, String category, String title, String content, String status,
			Date createdAt, Date updatedAt) {
		super();
		this.qnaId = qnaId;
		this.userId = userId;
		this.category = category;
		this.title = title;
		this.content = content;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public int getQnaId() {
		return qnaId;
	}
	public void setQnaId(int qnaId) {
		this.qnaId = qnaId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	
	

}
