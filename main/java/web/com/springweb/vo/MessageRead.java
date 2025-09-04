package web.com.springweb.vo;

import java.util.Date;

public class MessageRead {
	private int readId;
	private int messageId;
	private int userId;
	private Date readAt;
	public MessageRead() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MessageRead(int readId, int messageId, int userId, Date readAt) {
		super();
		this.readId = readId;
		this.messageId = messageId;
		this.userId = userId;
		this.readAt = readAt;
	}
	public int getReadId() {
		return readId;
	}
	public void setReadId(int readId) {
		this.readId = readId;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getReadAt() {
		return readAt;
	}
	public void setReadAt(Date readAt) {
		this.readAt = readAt;
	}
	

}
