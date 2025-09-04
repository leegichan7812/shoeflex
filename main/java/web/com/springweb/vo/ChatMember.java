package web.com.springweb.vo;

import java.util.Date;

public class ChatMember {
	private int memberId;
	private int roomId;
	private int userId;
	private Date joinedAt;
	public ChatMember() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChatMember(int memberId, int roomId, int userId, Date joinedAt) {
		super();
		this.memberId = memberId;
		this.roomId = roomId;
		this.userId = userId;
		this.joinedAt = joinedAt;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getJoinedAt() {
		return joinedAt;
	}
	public void setJoinedAt(Date joinedAt) {
		this.joinedAt = joinedAt;
	}
	

}
