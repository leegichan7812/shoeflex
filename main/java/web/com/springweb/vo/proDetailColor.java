package web.com.springweb.vo;

public class proDetailColor {
	private int colorId;
    private String colorNameKor;
    private String colorCode;
	public proDetailColor() {
		super();
		// TODO Auto-generated constructor stub
	}
	public proDetailColor(int colorId, String colorNameKor, String colorCode) {
		super();
		this.colorId = colorId;
		this.colorNameKor = colorNameKor;
		this.colorCode = colorCode;
	}
	public int getColorId() {
		return colorId;
	}
	public void setColorId(int colorId) {
		this.colorId = colorId;
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
