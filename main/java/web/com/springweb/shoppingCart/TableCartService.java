package web.com.springweb.shoppingCart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.com.springweb.MyPage.MyPageDao;
import web.com.springweb.vo.CartProduct;
import web.com.springweb.vo.tableCarts;

@Service
public class TableCartService {
   @Autowired(required = false)
   private MyPageDao dao; // ==> MyPageDao
   
   
   public List<CartProduct> gettableCartsList(int userId){
       
      //if(sch.getTableItem()==null)   sch.setTableItem("");
      //sch.setTableItem("%"+sch.getTableItem()+"%");
         return dao.getCartByuser(userId);
      }

}
