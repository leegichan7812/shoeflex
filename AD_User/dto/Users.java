package web.com.springweb.AD_User.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Users {

// userId, email, password, name, phone, address, accountType, createAt, updatedAt, age, gender, 
//accountStatus, withdrawDate, originEmail, userNote
	
	
	 private String userId; 
	 private String email; 
	 private String password; 
	 private String name;
	 private String phone;
	 private String address; 
	 private String accountType;
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 private Date createdAt;
	 @DateTimeFormat(pattern = "yyyy-MM-dd") 
	 private Date updatedAt; 
	 private int age;
	 private String gender; 
	 private String accountStatus;
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	 private Date withdrawDate; 
	 private String originEmail; 
	 private String userNote;
	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Date getWithdrawDate() {
		return withdrawDate;
	}
	public void setWithdrawDate(Date withdrawDate) {
		this.withdrawDate = withdrawDate;
	}
	public String getOriginEmail() {
		return originEmail;
	}
	public void setOriginEmail(String originEmail) {
		this.originEmail = originEmail;
	}
	public String getUserNote() {
		return userNote;
	}
	public void setUserNote(String userNote) {
		this.userNote = userNote;
	}
	 
	

}
