package web.com.springweb.CER;

public class CerItemView {
    private Long orderItemId;
    private Long productColorSizeId;
    private String name;
    private String colorNameKor;
    private String sizeValue;
    private Integer quantity;
	public CerItemView() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CerItemView(Long orderItemId, Long productColorSizeId, String name, String colorNameKor, String sizeValue,
			Integer quantity) {
		super();
		this.orderItemId = orderItemId;
		this.productColorSizeId = productColorSizeId;
		this.name = name;
		this.colorNameKor = colorNameKor;
		this.sizeValue = sizeValue;
		this.quantity = quantity;
	}
	public Long getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	public Long getProductColorSizeId() {
		return productColorSizeId;
	}
	public void setProductColorSizeId(Long productColorSizeId) {
		this.productColorSizeId = productColorSizeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColorNameKor() {
		return colorNameKor;
	}
	public void setColorNameKor(String colorNameKor) {
		this.colorNameKor = colorNameKor;
	}
	public String getSizeValue() {
		return sizeValue;
	}
	public void setSizeValue(String sizeValue) {
		this.sizeValue = sizeValue;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
    
    

}
