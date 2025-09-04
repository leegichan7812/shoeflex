package web.com.springweb.ProDetial;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.vo.ProductDetail;
import web.com.springweb.vo.ProductInquiry;
import web.com.springweb.vo.ReviewWithFile;
import web.com.springweb.vo.SizeOption;

@Service
public class ProDetailService {
	@Autowired(required=false)
	private ProDetailDao dao;
	
	// 상세정보 색상 가지고오기
	public ProductDetail getProductDetail(int productId) {
		return dao.getProductDetail(productId);
	}
	// 색상별 사이즈 리스트 조회
	public List<SizeOption> getSizesByColor(int productId, int colorId) {
	    return dao.getSizesByColor(productId, colorId);
	}
	// 장바구니에 추가
	public boolean addToCart(int userId, int pcsId, int quantity) {
	    int exists = dao.checkCartItemExists(userId, pcsId);
	    if (exists > 0) {
	        return false; // 이미 있음
	    }
	    return dao.insertCart(userId, pcsId, quantity) > 0;
	}
	
	
	public List<ProductInquiry> getProductInquiryList2(int productId) {
	    return dao.getProductInquiryList2(productId);
	}
	
	public List<ReviewWithFile> getReviewList(int productId) {
	    return dao.getReviewList(productId);
	}
	
	public String insertInquiry(ProductInquiry ins) {
	    return dao.insertInquiry(ins) > 0 ? "등록 성공" : "등록 실패";
	}
}
