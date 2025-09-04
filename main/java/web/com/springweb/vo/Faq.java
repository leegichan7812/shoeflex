package web.com.springweb.vo;

import java.util.Date;

public class Faq {
	
	private Long faqId;
    private String category;
    private String question;
    private String answer;
    private Date createdAt;
    private int viewCount;
	public Faq() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Faq(Long faqId, String category, String question, String answer, Date createdAt, int viewCount) {
		super();
		this.faqId = faqId;
		this.category = category;
		this.question = question;
		this.answer = answer;
		this.createdAt = createdAt;
		this.viewCount = viewCount;
	}
	public Long getFaqId() {
		return faqId;
	}
	public void setFaqId(Long faqId) {
		this.faqId = faqId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
    
	
	
    

}
