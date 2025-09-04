package web.com.springweb.vo;

import java.util.Date;

public class ChatMessage {
	private int messageId;
	private int roomId;
	private int senderId;
	private String messageType;
	private String content;
	private Date sendAt;
	public ChatMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChatMessage(int messageId, int roomId, int senderId, String messageType, String content, Date sendAt) {
		super();
		this.messageId = messageId;
		this.roomId = roomId;
		this.senderId = senderId;
		this.messageType = messageType;
		this.content = content;
		this.sendAt = sendAt;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSendAt() {
		return sendAt;
	}
	public void setSendAt(Date sendAt) {
		this.sendAt = sendAt;
	}  
	

}
