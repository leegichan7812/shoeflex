package web.com.springweb.vo;

import java.util.Date;

public class Users {
	private int userId;
	private String email;
	private String password;
	private String name;
	private String phone;
	private String address;
	private String accountType;
	private Date createdAt;
	private Date updatedAt;
	private int age;
	private String gender;
	private String accountStatus;
	private Date withdrawDate;
	private String originEmail;
	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Users(int userId, String email, String password, String name, String phone, String address,
			String accountType, Date createdAt, Date updatedAt, int age, String gender, String accountStatus,
			Date withdrawDate, String originEmail) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.accountType = accountType;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.age = age;
		this.gender = gender;
		this.accountStatus = accountStatus;
		this.withdrawDate = withdrawDate;
		this.originEmail = originEmail;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
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
	
	
}
