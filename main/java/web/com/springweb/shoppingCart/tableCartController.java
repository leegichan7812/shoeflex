package web.com.springweb.shoppingCart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import web.com.springweb.vo.tableCarts;

@Controller
public class tableCartController {

   @Autowired(required = false)
   private TableCartService service;
   
   /* 장바구니 리스트 */
   
   // http://localhost:7075/tableCartsList
   @RequestMapping("tableCartsList")
   public String tableCartsList(tableCarts sch, Model d) {
//   d.addAttribute("list",service.gettableCartsList());
   // /WEB-INF/views/
   
   // common/layout.jsp
   // .jsp
      return "\\common\\layout";
      
   }
}
