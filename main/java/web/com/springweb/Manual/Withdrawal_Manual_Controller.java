package web.com.springweb.Manual;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import web.com.springweb.Manual.dto.WithdrawalManual;


@Controller
public class Withdrawal_Manual_Controller {

	@Autowired(required = false)
	private Withdrawal_Manual_Service service;
	
	 // http://localhost:7075/withdrawalManual_List
	 @RequestMapping("/withdrawalManual_List")
	 public String withdrawalManual_List (WithdrawalManual sch,Model d) {
	 d.addAttribute("list",service.getWithdrawalManualList(sch));
		 return "pages/WithdrawalManual_List";
	 }
	// http://localhost:7075/withdrawalManual_Insert
	 @RequestMapping("/withdrawalManual_Insert")
	 public String withdrawalManual_Insert (WithdrawalManual ins,Model d) {
	 d.addAttribute("msg",service.insertWithdrawal_Manual(ins));
		return "jihyun/WithdrawalManual_Insert";
	}
	 //http://localhost:7075/WithdrawalManual_Detail?id=1 
	// http://localhost:7075/withdrawalManual_Detail?id=27
	// http://localhost:7075/WithdrawalManual_Detail?id=27
	@RequestMapping("withdrawalManual_Detail")
	public String withdrawalManual_Detail (@Param("id") int id,Model d) {
	d.addAttribute("manual",service.getWithdrawalManual(id));
		return "jihyun/WithdrawalManual_Detail"; 
	}
	// http://localhost:7075/withdrawalManual_Update
	@RequestMapping("withdrawalManual_Update")
	public String withdrawalManual_Update (WithdrawalManual upt, Model d) {
	d.addAttribute("msg",service.updatetWithdrawal_Manual(upt));
	d.addAttribute("manual",service.getWithdrawalManual(upt.getId()));
		return "jihyun/WithdrawalManual_Detail"; 
	}
	// 수정 안내 후속조치
	// http://localhost:7075/withdrawalManual_Update
	@PostMapping("withdrawalManualList_Update")
	public String withdrawalManualList_Update (WithdrawalManual upt, Model d) {
		d.addAttribute("msg",service.updateWithdrawalManual2(upt));
	//d.addAttribute("manual",service.getWithdrawalManual(upt.getId()));
		return "redirect:/withdrawalManual_List"; 
	}
	
	
	// http://localhost:7075/withdrawalManual_Delete
	@PostMapping("withdrawalManual_Delete")
	public String withdrawalManual_Delete(@RequestParam("id") int id, Model d) {
	d.addAttribute("msg", service.deleteWithdrawal_Manual(id));
		return "jihyun/WithdrawalManual_Detail"; 
	}
	// http://localhost:7075/withdrawalManual_Delete
	@PostMapping("withdrawalManualList_Delete")
	public String withdrawalManualList_Delete(@RequestParam("id") int id, Model d) {
		d.addAttribute("msg", service.deleteWithdrawal_Manual(id));
		
		return "redirect:/withdrawalManual_List";
	}	
	
	// http://localhost:7075/cc
	@RequestMapping("cc")
	public String cc () {
		 return "jihyun/NewFile";
	}
	

	
	
}

		
	


