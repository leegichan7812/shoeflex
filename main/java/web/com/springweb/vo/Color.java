package web.com.springweb.vo;

public class Color {
	
	private int colorId;
	private String colorNameEng;
	private String colorNameKor;
	private String colorCode;
	public Color() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Color(int colorId, String colorNameEng, String colorNameKor, String colorCode) {
		super();
		this.colorId = colorId;
		this.colorNameEng = colorNameEng;
		this.colorNameKor = colorNameKor;
		this.colorCode = colorCode;
	}
	public int getColorId() {
		return colorId;
	}
	public void setColorId(int colorId) {
		this.colorId = colorId;
	}
	public String getColorNameEng() {
		return colorNameEng;
	}
	public void setColorNameEng(String colorNameEng) {
		this.colorNameEng = colorNameEng;
	}
	public String getColorNameKor() {
		return colorNameKor;
	}
	public void setColorNameKor(String colorNameKor) {
		this.colorNameKor = colorNameKor;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}	
}
