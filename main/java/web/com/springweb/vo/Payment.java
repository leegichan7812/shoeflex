package web.com.springweb.vo;

public class Payment {
	private int methodId;
	private String methodName;
	private String methodImg;
	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Payment(int methodId, String methodName, String methodImg) {
		super();
		this.methodId = methodId;
		this.methodName = methodName;
		this.methodImg = methodImg;
	}
	public int getMethodId() {
		return methodId;
	}
	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodImg() {
		return methodImg;
	}
	public void setMethodImg(String methodImg) {
		this.methodImg = methodImg;
	}	
}
