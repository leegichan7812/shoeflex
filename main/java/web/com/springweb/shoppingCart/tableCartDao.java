package web.com.springweb.shoppingCart;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import web.com.springweb.vo.CartProduct;



@Mapper
public interface tableCartDao {
   @Select("SELECT \r\n"   
         + "    p.IMAGE_URL,\r\n"
         + "    p.PRICE,\r\n"
         + "    c.QUANTITY,\r\n")
   List<CartProduct> getCartByuser(@Param("userId") int userId); 
    

    
}
