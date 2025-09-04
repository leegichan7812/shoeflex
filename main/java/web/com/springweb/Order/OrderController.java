package web.com.springweb.Order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import web.com.springweb.vo.OrderItem;
import web.com.springweb.vo.OrderRequestDto;
import web.com.springweb.vo.Payment;
import web.com.springweb.vo.ShippingAddress;
import web.com.springweb.vo.Users;

@Controller
public class OrderController {
	@Autowired(required=false)
	private OrderService service;
	
	@GetMapping("orderPage")
	public String showOrderPage(
	        @RequestParam(name = "cartIds", required = false) String cartIdsJson,
	        @RequestParam(name = "productColorSizeId", required = false) Integer productColorSizeId,
	        @RequestParam(name = "quantity", required = false) Integer quantity,
	        HttpSession session, Model model) {

	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (loginUser == null) return "index";

	    List<OrderItem> orderList = new ArrayList<>();

	    // [1] 장바구니 선택 주문
	    if (cartIdsJson != null && !cartIdsJson.isEmpty()) {
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            List<Integer> cartIds = mapper.readValue(cartIdsJson, new TypeReference<List<Integer>>() {});
	            orderList = service.getOrderItemsByCartIds(cartIds);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	    // [2] 상세페이지 바로구매
	    } else if (productColorSizeId != null && quantity != null) {
	        OrderItem item = service.getOrderItemDirect(productColorSizeId, quantity);
	        if (item != null) {
	            orderList.add(item);
	        }

	    // [3] 장바구니 전체 주문
	    } else {
	        orderList = service.getOrderItems(loginUser.getUserId());
	    }

	    // 배송지
	    ShippingAddress defaultAddress = service.getDefaultShippingAddress(loginUser.getUserId());
	    // 결제 수단
	    List<Payment> paymentMethods = service.getAllPaymentMethods();
	    model.addAttribute("paymentMethods", paymentMethods);

	    model.addAttribute("orderList", orderList);
	    model.addAttribute("defaultAddress", defaultAddress);
	    
	    // 🔽 총 상품 금액 및 배송비 계산
	    int totalPrice = 0;
	    int totalQuantity = 0;

	    for (OrderItem item : orderList) {
	        totalPrice += item.getUnitPrice() * item.getQuantity();
	        totalQuantity += item.getQuantity();
	    }

	    int shippingFee;
	    if (totalQuantity == 1) {
	        shippingFee = 3000;
	    } else if (totalQuantity == 2) {
	        shippingFee = 2500;
	    } else {
	        shippingFee = 0;
	    }

	    // 🔽 JSP에서 사용할 값들 전달
	    model.addAttribute("totalPrice", totalPrice);
	    model.addAttribute("shippingFee", shippingFee);

	    return "pages/order";
	}
	
	@GetMapping("getProductColorSizeId")
	@ResponseBody
	public Integer getProductColorSizeId(@RequestParam int productId,
	                                     @RequestParam int colorId,
	                                     @RequestParam int sizeId) {
	    return service.getProductColorSizeId(productId, colorId, sizeId);
	}
	// 배송지 추가 요청 처리 컨트롤러
	@PostMapping("addShippingAddress")
	@ResponseBody
	public String addShippingAddress(ShippingAddress address, HttpSession session) {
	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (loginUser == null) return "fail";
	    address.setUserId(loginUser.getUserId());
	    if (address.getIsDefault() == null) {
	        address.setIsDefault("N");
	    }
	    boolean isSuccess = service.insertShippingAddress(address) > 0;
	    return isSuccess ? "success" : "fail";
	}
	// 배송지 목록을 JSON으로 리턴하는 컨트롤러
	@GetMapping("getShippingAddresses")
	@ResponseBody
	public List<ShippingAddress> getShippingAddresses(HttpSession session) {
	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (loginUser == null) return new ArrayList<>();
	    return service.getShippingAddresses(loginUser.getUserId());
	}
	// 주문 저장용
	@PostMapping("submitOrder")
	@ResponseBody
	public String submitOrder(@RequestBody OrderRequestDto dto, HttpSession session) {
	    Users loginUser = (Users) session.getAttribute("loginUser");
	    if (loginUser == null) return "index";

	    int result = service.saveOrder(dto, loginUser);
	    return result > 0 ? "success" : "fail";
	}
	
	
}
