package web.com.springweb.Chart.dto;

public class ChartMonthlyDto {
	private String brandName;   // 브랜드명
    private String month;       // YYYY-MM
    private int salesCount;     // 판매 건수
	public ChartMonthlyDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChartMonthlyDto(String brandName, String month, int salesCount) {
		super();
		this.brandName = brandName;
		this.month = month;
		this.salesCount = salesCount;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getSalesCount() {
		return salesCount;
	}
	public void setSalesCount(int salesCount) {
		this.salesCount = salesCount;
	}
}
