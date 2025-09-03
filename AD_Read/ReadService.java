package web.com.springweb.AD_Read;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.AD_Read.dto.ProductColorStockDTO;
import web.com.springweb.AD_Read.dto.ProductSimpleStockDTO;
import web.com.springweb.AD_Read.dto.ProductStockViewDTO;

@Service
public class ReadService {
	
	@Autowired
    private ReadDao dao;
	
	// 상품 상세 재고
    public List<ProductStockViewDTO> getStockDetail(int productId) {
        return dao.selectProductDetailStock(productId);
    }

    // 전체 상품 개수 (필터 포함)
    public int getProductCount(String keyword, String brand, String category) {
        return dao.selectProductCount(keyword, brand, category);
    }

    // 상품 리스트 페이징 + 필터
    public List<ProductSimpleStockDTO> getProductPaging(String keyword, String brand, String category, int startRow, int endRow) {
        return dao.selectProductPaging(keyword, brand, category, startRow, endRow);
    }

    // 컬러별 종합 재고
    public List<ProductColorStockDTO> getColorTotalStock() {
        return dao.selectColorTotalStock();
    }

}
