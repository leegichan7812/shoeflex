package web.com.springweb.vo;

public class Size {
	
	private int sizeId;
	private int sizeValue;
	public Size() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Size(int sizeId, int sizeValue) {
		super();
		this.sizeId = sizeId;
		this.sizeValue = sizeValue;
	}
	public int getSizeId() {
		return sizeId;
	}
	public void setSizeId(int sizeId) {
		this.sizeId = sizeId;
	}
	public int getSizeValue() {
		return sizeValue;
	}
	public void setSizeValue(int sizeValue) {
		this.sizeValue = sizeValue;
	}
}
