package web.com.springweb.AD_User.dto;
// id; reason;  situation; checkAction;  guidance; followUp;
public class WithdrawalManual {
	 	private int id;
	    private String reason;
	    private String situation;
	    private String checkAction;
	    private String guidance;
	    private String followUp;
		public WithdrawalManual() {
			super();
			// TODO Auto-generated constructor stub
		}
		public WithdrawalManual(int id, String reason, String situation, String checkAction, String guidance,
				String followUp) {
			super();
			this.id = id;
			this.reason = reason;
			this.situation = situation;
			this.checkAction = checkAction;
			this.guidance = guidance;
			this.followUp = followUp;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public String getSituation() {
			return situation;
		}
		public void setSituation(String situation) {
			this.situation = situation;
		}
		public String getCheckAction() {
			return checkAction;
		}
		public void setCheckAction(String checkAction) {
			this.checkAction = checkAction;
		}
		public String getGuidance() {
			return guidance;
		}
		public void setGuidance(String guidance) {
			this.guidance = guidance;
		}
		public String getFollowUp() {
			return followUp;
		}
		public void setFollowUp(String followUp) {
			this.followUp = followUp;
		}
		
}

	    
		