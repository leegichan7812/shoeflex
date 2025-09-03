package web.com.springweb.AD_Read.dto;

public class ProductSimpleStockDTO {
    private int productId;
    private String productName;
    private String imageUrl;
    private String brandName;
    private String categoryName;
    private int productTotalStock;
    private String productStatus;
    private String colorStatus;      // ← 추가

    public ProductSimpleStockDTO() {}

    public ProductSimpleStockDTO(
        int productId, String productName, String imageUrl,
        String brandName, String categoryName, int productTotalStock,
        String productStatus, String colorStatus
    ) {
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.productTotalStock = productTotalStock;
        this.productStatus = productStatus;
        this.colorStatus = colorStatus;
    }

    // --- Getter/Setter ---
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public int getProductTotalStock() { return productTotalStock; }
    public void setProductTotalStock(int productTotalStock) { this.productTotalStock = productTotalStock; }

    public String getProductStatus() { return productStatus; }
    public void setProductStatus(String productStatus) { this.productStatus = productStatus; }

    public String getColorStatus() { return colorStatus; }
    public void setColorStatus(String colorStatus) { this.colorStatus = colorStatus; }
}
