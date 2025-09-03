package web.com.springweb.AD_Brand;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import web.com.springweb.AD_Brand.dto.Brand;

@Mapper
public interface BrandDao {
	@Select("""
        SELECT 
          BRAND_ID        AS brandId,
          BRAND_NAME      AS brandName,
          BRAND_NAME_ENG  AS brandNameEng,
          BRAND_IMG       AS brandImg,
          BRAND_URL       AS brandUrl,
          NVL(STATUS,'판매중') AS status
        FROM BRANDS
        ORDER BY BRAND_NAME
    """)
    List<Brand> getBrandList();

    // 상태 변경 (판매중 / 판매중지)
    @Update("""
        UPDATE BRANDS
           SET STATUS = #{status}
         WHERE BRAND_ID = #{brandId}
    """)
    int updateStatus(@Param("brandId") int brandId, @Param("status") String status);

    // 논리 삭제(=판매중지로 상태 전환)
    @Update("UPDATE BRANDS SET STATUS='판매중지' WHERE BRAND_ID=#{brandId}")
    int logicalDelete(@Param("brandId") int brandId);
}
