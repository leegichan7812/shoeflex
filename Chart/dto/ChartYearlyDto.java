package web.com.springweb.Chart.dto;

public class ChartYearlyDto {
	private String brandName;    // 브랜드명
    private String year;         // YYYY
    private double salesAmount;  // 판매 금액
	public ChartYearlyDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChartYearlyDto(String brandName, String year, double salesAmount) {
		super();
		this.brandName = brandName;
		this.year = year;
		this.salesAmount = salesAmount;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public double getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(double salesAmount) {
		this.salesAmount = salesAmount;
	}    
}
