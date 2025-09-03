package web.com.springweb.AD_User.dto;

import java.util.List;

public class Chart {
	private String userId;
	private String mon;
	private int salesCount;
	private long monthTot;
	
	private List<String> mons;
	private List<Integer> salesCounts;
	private List<Long> monthTots;
	
	
	

	public Chart(List<String> mons, List<Integer> salesCounts, List<Long> monthTots) {
		super();
		this.mons = mons;
		this.salesCounts = salesCounts;
		this.monthTots = monthTots;
	}
	public Chart() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMon() {
		return mon;
	}
	public void setMon(String mon) {
		this.mon = mon;
	}
	public int getSalesCount() {
		return salesCount;
	}
	public void setSalesCount(int salesCount) {
		this.salesCount = salesCount;
	}
	public long getMonthTot() {
		return monthTot;
	}
	public void setMonthTot(long monthTot) {
		this.monthTot = monthTot;
	}
	public List<String> getMons() {
		return mons;
	}
	public void setMons(List<String> mons) {
		this.mons = mons;
	}
	public List<Integer> getSalesCounts() {
		return salesCounts;
	}
	public void setSalesCounts(List<Integer> salesCounts) {
		this.salesCounts = salesCounts;
	}
	public List<Long> getMonthTots() {
		return monthTots;
	}
	public void setMonthTots(List<Long> monthTots) {
		this.monthTots = monthTots;
	}

	
	
}
