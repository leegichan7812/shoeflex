package web.com.springweb.AD_Brand.dto;

public class Brand {
    private Integer brandId;
    private String brandName;
    private String brandNameEng;
    private String brandImg;
    private String brandUrl;
    private String status; // '판매중' | '판매중지'

    public Integer getBrandId() { return brandId; }
    public void setBrandId(Integer brandId) { this.brandId = brandId; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public String getBrandNameEng() { return brandNameEng; }
    public void setBrandNameEng(String brandNameEng) { this.brandNameEng = brandNameEng; }
    public String getBrandImg() { return brandImg; }
    public void setBrandImg(String brandImg) { this.brandImg = brandImg; }
    public String getBrandUrl() { return brandUrl; }
    public void setBrandUrl(String brandUrl) { this.brandUrl = brandUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
