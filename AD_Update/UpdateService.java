package web.com.springweb.AD_Update;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.com.springweb.AD_Update.dto.UpdateReq;

@Service
public class UpdateService {

    @Autowired
    private UpdateDao updateDao;

    // 상품 상태(STATUS) 변경 (일시중단/판매중지)
    @Transactional
    public boolean updateProductStatus(int productId, String status) {
        return updateDao.updateProductStatus(productId, status) > 0;
    }

    // 컬러별 상태 변경 (일시중단/해제/판매중지)
    @Transactional
    public boolean updateColorStatus(int productId, int colorId, String status) {
        return updateDao.updateColorStatus(productId, colorId, status) > 0;
    }

    // 사이즈별 재고 수정 (단일)
    @Transactional
    public boolean updateColorSizeStock(int productColorId, int sizeId, int stock) {
        return updateDao.updateColorSizeStock(productColorId, sizeId, stock) > 0;
    }
    
    @Transactional
    public boolean updateStocks(List<UpdateReq> updates) {
        boolean ok = true;
        for (UpdateReq u : updates) {
            Integer pcid = updateDao.findProductColorId(u.getProductId(), u.getColorId());
            Integer sid  = updateDao.findSizeId(u.getSizeValue());
            if (pcid == null || sid == null) { ok = false; continue; }
            int c = updateDao.updateColorSizeStock(pcid, sid, u.getStock());
            if (c == 0) ok = false;
        }
        return ok;
    }
}
