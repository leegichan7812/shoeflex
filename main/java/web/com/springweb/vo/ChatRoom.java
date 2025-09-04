package web.com.springweb.vo;

import java.util.Date;

public class ChatRoom {
	
	private int roomId;
	private String roomName;
	private String roomType;
	private Date createdAt;
	public ChatRoom() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChatRoom(int roomId, String roomName, String roomType, Date createdAt) {
		super();
		this.roomId = roomId;
		this.roomName = roomName;
		this.roomType = roomType;
		this.createdAt = createdAt;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	} 
	
	

}
