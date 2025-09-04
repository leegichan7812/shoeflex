package web.com.springweb.Slide.dto;

public class Slide {
	private Integer calendarId;
    private String title;
    private String start1;
    private String end1;
    private Integer brandId;
    private String brandName;
    private String slideImage;   // 배경 이미지(URL)
    private String slideTitle;
    private String brandImg;     // BRANDS.BRAND_IMG (로고)
	public Slide() {
		super();
		// TODO Auto-generated constructor stub
	}	
	public Slide(Integer calendarId, String title, String start1, String end1, Integer brandId, String brandName,
			String slideImage, String slideTitle, String brandImg) {
		super();
		this.calendarId = calendarId;
		this.title = title;
		this.start1 = start1;
		this.end1 = end1;
		this.brandId = brandId;
		this.brandName = brandName;
		this.slideImage = slideImage;
		this.slideTitle = slideTitle;
		this.brandImg = brandImg;
	}
	public Integer getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(Integer calendarId) {
		this.calendarId = calendarId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStart1() {
		return start1;
	}
	public void setStart1(String start1) {
		this.start1 = start1;
	}
	public String getEnd1() {
		return end1;
	}
	public void setEnd1(String end1) {
		this.end1 = end1;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getSlideImage() {
		return slideImage;
	}
	public void setSlideImage(String slideImage) {
		this.slideImage = slideImage;
	}
	public String getSlideTitle() {
		return slideTitle;
	}
	public void setSlideTitle(String slideTitle) {
		this.slideTitle = slideTitle;
	}
	public String getBrandImg() {
		return brandImg;
	}
	public void setBrandImg(String brandImg) {
		this.brandImg = brandImg;
	}	
}
