package web.com.springweb.ProDetial;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import web.com.springweb.vo.ProductDetail;
import web.com.springweb.vo.ProductInquiry;
import web.com.springweb.vo.ReviewWithFile;
import web.com.springweb.vo.SizeOption;

@Mapper
public interface ProDetailDao {
	// 상세정보 색상 가지고오기
	ProductDetail getProductDetail(@Param("productId") int productId); 
	// 색상별 사이즈 리스트 조회
	List<SizeOption> getSizesByColor(@Param("productId") int productId, @Param("colorId") int colorId);
	// 해당 유저가 해당 상품(색상+사이즈 조합)을 장바구니에 이미 담았는지
	@Select("SELECT COUNT(*) FROM CART WHERE USER_ID = #{userId} AND PRODUCT_COLOR_SIZE_ID = #{pcsId}")
	int checkCartItemExists(@Param("userId") int userId, @Param("pcsId") int pcsId);
	// 장바구니에 추가
	@Insert("INSERT INTO CART (CART_ID, USER_ID, PRODUCT_COLOR_SIZE_ID, QUANTITY) " +
	        "VALUES (cart_seq.NEXTVAL, #{userId}, #{pcsId}, #{quantity})")
	int insertCart(@Param("userId") int userId, @Param("pcsId") int pcsId, @Param("quantity") int quantity);
	//
	@Select("SELECT * FROM PRODUCT_INQUIRY_VIEW WHERE PRODUCT_ID = #{productId} "
			+ "ORDER BY INQUIRY_DATE DESC, ANSWER_date ASC ")
    List<ProductInquiry> getProductInquiryList2(@Param("productId") int productId);
	
	@Select("SELECT * FROM REVIEW_WITH_FILES WHERE PRODUCT_ID = #{productId} ORDER BY REVIEW_ID DESC")
    List<ReviewWithFile> getReviewList(@Param("productId") int productId);
	
	@Insert("INSERT INTO PRODUCT_INQUIRY (PRODUCT_ID, USER_ID, INQUIRY_TITLE, INQUIRY_CONTENT, INQUIRY_DATE, IS_PUBLIC, STATUS) " +
	        "VALUES (#{productId}, #{userId}, #{inquiryTitle}, #{inquiryContent}, SYSDATE, #{isPublic}, '미확인')")
	int insertInquiry(ProductInquiry ins);
	
	
}



