package web.com.springweb.AD_Update;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import web.com.springweb.AD_Update.dto.UpdateStocksRequest;

@RestController
@RequestMapping("/product")
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    // 1. 상품 전체 상태 변경
    @PostMapping("/updateStatus")
    public Map<String, Object> updateProductStatus(
        @RequestParam int productId,
        @RequestParam String status
    ) {
        boolean ok = updateService.updateProductStatus(productId, status);
        return Map.of("msg", ok ? "상태가 변경되었습니다." : "실패", "success", ok);
    }

    // 색상 일시중단
    @PostMapping("/pauseColor")
    public Map<String, Object> pauseColor(@RequestParam int productId,
            @RequestParam int colorId) {
		System.out.println(">>> pauseColor 호출됨: productId=" + productId + ", colorId=" + colorId);
		boolean ok = updateService.updateColorStatus(productId, colorId, "일시중단");
		return Map.of("success", ok);
	}

    // 색상 재판매(= 일시중단 해제 → 판매중)
    @PostMapping("/resumeColor")
    public Map<String, Object> resumeColor(@RequestParam int productId,
                                           @RequestParam int colorId) {
        System.out.println(">>> resumeColor 호출됨: productId=" + productId + ", colorId=" + colorId);
        boolean ok = updateService.updateColorStatus(productId, colorId, "판매중");
        return Map.of("success", ok);
    }
    
    // 색상 판매중지
    @PostMapping("/updateColorStatus")
    public Map<String, Object> updateColorStatus(
            @RequestParam int productId,
            @RequestParam int colorId) {
        System.out.println(">>> updateColorStatus(판매중지): productId=" + productId + ", colorId=" + colorId);
        boolean ok = updateService.updateColorStatus(productId, colorId, "판매중지");
        return Map.of("success", ok);
    }
    
    // 색상별 재고 수정
    @PostMapping("/updateStocks")
    public Map<String,Object> updateStocks(@RequestBody UpdateStocksRequest body) {
        boolean ok = updateService.updateStocks(body.getUpdates());
        return Map.of("success", ok, "msg", ok ? "재고가 저장되었습니다." : "일부 저장 실패");
    }

}
