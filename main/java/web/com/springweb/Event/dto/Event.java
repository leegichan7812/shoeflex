package web.com.springweb.Event.dto;

import java.util.Date;

public class Event {
	private Integer calendarId;     // PK, FK → CALENDAR.ID
    private String title;
    private String start1;
    private String end1;
    private Integer brandId;
    private String brandName;
    private String slideImage;
    private String slideTitle;
    private Date createdAt;
    private Date updatedAt;

    public Integer getCalendarId() { return calendarId; }
    public void setCalendarId(Integer calendarId) { this.calendarId = calendarId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStart1() { return start1; }
    public void setStart1(String start1) { this.start1 = start1; }
    public String getEnd1() { return end1; }
    public void setEnd1(String end1) { this.end1 = end1; }
    public Integer getBrandId() { return brandId; }
    public void setBrandId(Integer brandId) { this.brandId = brandId; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public String getSlideImage() { return slideImage; }
    public void setSlideImage(String slideImage) { this.slideImage = slideImage; }
    public String getSlideTitle() { return slideTitle; }
    public void setSlideTitle(String slideTitle) { this.slideTitle = slideTitle; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
