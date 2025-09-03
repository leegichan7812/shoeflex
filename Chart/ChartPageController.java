package web.com.springweb.Chart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChartPageController {
	@GetMapping("/anaChart")
    public String anaChart() {
    	return "pages/anaChart";
    }
}
