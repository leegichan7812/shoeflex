package web.com.springweb.vo;

import java.util.Date;

public class CustomerServiceAnswer {
	
	private int answerId;
	private int qnaId;
	private int userId;
	private String content;
	private Date createdAt;
	public CustomerServiceAnswer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerServiceAnswer(int answerId, int qnaId, int userId, String content, Date createdAt) {
		super();
		this.answerId = answerId;
		this.qnaId = qnaId;
		this.userId = userId;
		this.content = content;
		this.createdAt = createdAt;
	}
	public int getAnswerId() {
		return answerId;
	}
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
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
	
	

}
