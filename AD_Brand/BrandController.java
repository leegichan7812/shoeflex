package web.com.springweb.AD_Brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin/brands")
public class BrandController {

	@Autowired
    private BrandService brandService;
    
    

    // 페이지 진입 (Ajax 로딩 구조면 이 JSP를 반환)
    @GetMapping
    public String page(Model d){
        d.addAttribute("brandList", brandService.list());
        return "/pages/ad_brand"; // JSP 경로는 프로젝트에 맞게 수정
    }

    // 상태 변경: '판매중' | '판매중지'
    @PostMapping("/status")
    @ResponseBody
    public String status(@RequestParam int brandId, @RequestParam String status){
        if(!"판매중".equals(status) && !"판매중지".equals(status)) return "BAD_STATUS";
        return brandService.updateStatus(brandId, status) ? "OK" : "FAIL";
    }

    // 논리삭제: 판매중지로 전환
    @PostMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam int brandId){
        return brandService.logicalDelete(brandId) ? "OK" : "FAIL";
    }

    
}
