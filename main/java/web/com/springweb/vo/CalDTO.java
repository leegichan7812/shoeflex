package web.com.springweb.vo;

import java.util.List;

import web.com.springweb.vo.Calendar;

public class CalDTO {
	private String msg;
	private List<Calendar> list;
	public CalDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CalDTO(String msg, List<Calendar> list) {
		super();
		this.msg = msg;
		this.list = list;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<Calendar> getList() {
		return list;
	}
	public void setList(List<Calendar> list) {
		this.list = list;
	}
	
}
