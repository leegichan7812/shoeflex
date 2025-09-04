package web.com.springweb.vo;

public class ProductColorSize {
	private int productColorSizeId; // PK (자동증가)
    private int productColorId;     // PRODUCT_COLORS의 PK (FK)
    private int sizeId;             // 사이즈 ID (FK)
    private int stock;              // 재고수량
    
    


    public ProductColorSize() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public ProductColorSize(int productColorSizeId, int productColorId, int sizeId, int stock) {
		super();
		this.productColorSizeId = productColorSizeId;
		this.productColorId = productColorId;
		this.sizeId = sizeId;
		this.stock = stock;
	}

	// Getter / Setter
    public int getProductColorSizeId() { return productColorSizeId; }
    public void setProductColorSizeId(int productColorSizeId) { this.productColorSizeId = productColorSizeId; }

    public int getProductColorId() { return productColorId; }
    public void setProductColorId(int productColorId) { this.productColorId = productColorId; }

    public int getSizeId() { return sizeId; }
    public void setSizeId(int sizeId) { this.sizeId = sizeId; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    

}
