package web.com.springweb.vo;

public class tableCarts {
	
	private int tableId;
	private String tableImg;
	private String tableItem;
	private int tableCnt;
	private int tablePrice;
	public tableCarts() {
		super();
		// TODO Auto-generated constructor stub
	}
	public tableCarts(int tableId, String tableImg, String tableItem, int tableCnt, int tablePrice) {
		super();
		this.tableId = tableId;
		this.tableImg = tableImg;
		this.tableItem = tableItem;
		this.tableCnt = tableCnt;
		this.tablePrice = tablePrice;
	}
	public int getTableId() {
		return tableId;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	public String getTableImg() {
		return tableImg;
	}
	public void setTableImg(String tableImg) {
		this.tableImg = tableImg;
	}
	public String getTableItem() {
		return tableItem;
	}
	public void setTableItem(String tableItem) {
		this.tableItem = tableItem;
	}
	public int getTableCnt() {
		return tableCnt;
	}
	public void setTableCnt(int tableCnt) {
		this.tableCnt = tableCnt;
	}
	public int getTablePrice() {
		return tablePrice;
	}
	public void setTablePrice(int tablePrice) {
		this.tablePrice = tablePrice;
	}
	
	

}
