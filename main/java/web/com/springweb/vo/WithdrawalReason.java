package web.com.springweb.vo;

public class WithdrawalReason {
	private int reasonId;
	private String reasonText;
	public WithdrawalReason() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WithdrawalReason(int reasonId, String reasonText) {
		super();
		this.reasonId = reasonId;
		this.reasonText = reasonText;
	}
	public int getReasonId() {
		return reasonId;
	}
	public void setReasonId(int reasonId) {
		this.reasonId = reasonId;
	}
	public String getReasonText() {
		return reasonText;
	}
	public void setReasonText(String reasonText) {
		this.reasonText = reasonText;
	}
	
}
