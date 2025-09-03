package web.com.springweb.AD_Update.dto;

import java.util.List;

public class UpdateStocksRequest {
	private List<UpdateReq> updates;

	public UpdateStocksRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UpdateStocksRequest(List<UpdateReq> updates) {
		super();
		this.updates = updates;
	}
	public List<UpdateReq> getUpdates() {
		return updates;
	}
	public void setUpdates(List<UpdateReq> updates) {
		this.updates = updates;
	}
}
