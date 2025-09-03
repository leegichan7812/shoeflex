package web.com.springweb.AD_Insert;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.vo.ProductColorSize;
import web.com.springweb.vo.ProductExcelDto;
import web.com.springweb.vo.Products;
import web.com.springweb.vo.ProductsColor;

@Service
public class InsertService {

    @Autowired
    private InsertDao dao;

    public void insertProduct(Products product) {
        dao.insertProduct(product);
    }

    public void insertMultiProducts(List<Products> productList) {
        for (Products p : productList) {
            dao.insertProduct(p);
        }
    }

    public int insertProductColor(int productId, int colorId) {
        ProductsColor pc = new ProductsColor();
        pc.setProductId(productId);
        pc.setColorId(colorId);
        dao.insertProductColor(pc);
        return pc.getProductColorId();
    }

    public void insertProductColorSize(int productColorId, int sizeId, int stock) {
        ProductColorSize pcs = new ProductColorSize();
        pcs.setProductColorId(productColorId);
        pcs.setSizeId(sizeId);
        pcs.setStock(stock);
        dao.insertProductColorSize(pcs);
    }

    public void insertProductWithColorSize(List<ProductExcelDto> dtoList) {
        for (ProductExcelDto dto : dtoList) {

            Products product = dto.getProduct();
            dao.insertProduct(product);

            ProductsColor pc = new ProductsColor();
            pc.setProductId(product.getProductId());
            pc.setColorId(dto.getColorId());
            dao.insertProductColor(pc);

            for (Map.Entry<Integer, Integer> entry : dto.getSizeStockMap().entrySet()) {
                int sizeId = entry.getKey();
                int stock = entry.getValue();

                if (stock > 0) {
                    ProductColorSize pcs = new ProductColorSize();
                    pcs.setProductColorId(pc.getProductColorId());
                    pcs.setSizeId(sizeId);
                    pcs.setStock(stock);
                    dao.insertProductColorSize(pcs);
                }
            }
        }
    }
}
