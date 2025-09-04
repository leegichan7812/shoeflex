package web.com.springweb.vo;

import java.util.Date;

public class Notice {
	
	private int noticeId;
	private String title;
	private String content;
	private String isPopup;
	private String isPinned;
	private int viewCount;
	private Date createdAt;
	public Notice() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Notice(int noticeId, String title, String content, String isPopup, String isPinned, int viewCount,
			Date createdAt) {
		super();
		this.noticeId = noticeId;
		this.title = title;
		this.content = content;
		this.isPopup = isPopup;
		this.isPinned = isPinned;
		this.viewCount = viewCount;
		this.createdAt = createdAt;
	}
	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
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
	public String getIsPopup() {
		return isPopup;
	}
	public void setIsPopup(String isPopup) {
		this.isPopup = isPopup;
	}
	public String getIsPinned() {
		return isPinned;
	}
	public void setIsPinned(String isPinned) {
		this.isPinned = isPinned;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	

}
