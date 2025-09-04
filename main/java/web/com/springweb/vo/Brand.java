package web.com.springweb.vo;

public class Brand {
	private int brandId;
    private String brandName;
    private String brandImg;
    private String brandUrl;
    private String brandImgSlide;
    private String brandSlideTitle;
	public Brand() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	public Brand(int brandId, String brandName, String brandImg, String brandUrl, String brandImgSlide,
			String brandSlideTitle) {
		super();
		this.brandId = brandId;
		this.brandName = brandName;
		this.brandImg = brandImg;
		this.brandUrl = brandUrl;
		this.brandImgSlide = brandImgSlide;
		this.brandSlideTitle = brandSlideTitle;
	}

	public String getBrandImgSlide() {
		return brandImgSlide;
	}

	public void setBrandImgSlide(String brandImgSlide) {
		this.brandImgSlide = brandImgSlide;
	}

	public String getBrandSlideTitle() {
		return brandSlideTitle;
	}

	public void setBrandSlideTitle(String brandSlideTitle) {
		this.brandSlideTitle = brandSlideTitle;
	}

	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandUrl() {
		return brandUrl;
	}
	public void setBrandUrl(String brandUrl) {
		this.brandUrl = brandUrl;
	}
	public String getBrandImg() {
		return brandImg;
	}
	public void setBrandImg(String brandImg) {
		this.brandImg = brandImg;
	}   
	
}
