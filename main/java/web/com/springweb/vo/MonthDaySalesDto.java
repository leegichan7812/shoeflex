package web.com.springweb.vo;

public class MonthDaySalesDto {
	private String month;     // "2024-06"
    private String dow;       // "1" ~ "7" (일~토)
    private String dowName;   // "월", "화", ...
    private int count;        // 판매건수
    private long sales;       // 판매금액
	public MonthDaySalesDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MonthDaySalesDto(String month, String dow, String dowName, int count, long sales) {
		super();
		this.month = month;
		this.dow = dow;
		this.dowName = dowName;
		this.count = count;
		this.sales = sales;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDow() {
		return dow;
	}
	public void setDow(String dow) {
		this.dow = dow;
	}
	public String getDowName() {
		return dowName;
	}
	public void setDowName(String dowName) {
		this.dowName = dowName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long getSales() {
		return sales;
	}
	public void setSales(long sales) {
		this.sales = sales;
	}
}
