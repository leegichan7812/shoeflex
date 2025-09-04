package web.com.springweb.vo;

import java.sql.Timestamp;

public class ExceptionLog {
	private int logId;
    private Timestamp occurredAt;
    private String className;
    private String methodName;
    private String exceptionType;
    private String exceptionMessage;
    private String errorType;       // "Exception" 또는 "Timeout"
    private Integer executionTimeMs;
    private String userEmail;
    private String userName;
	public ExceptionLog() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ExceptionLog(int logId, Timestamp occurredAt, String className, String methodName, String exceptionType,
			String exceptionMessage, String errorType, Integer executionTimeMs, String userEmail, String userName) {
		super();
		this.logId = logId;
		this.occurredAt = occurredAt;
		this.className = className;
		this.methodName = methodName;
		this.exceptionType = exceptionType;
		this.exceptionMessage = exceptionMessage;
		this.errorType = errorType;
		this.executionTimeMs = executionTimeMs;
		this.userEmail = userEmail;
		this.userName = userName;
	}

	public int getLogId() {
		return logId;
	}
	public void setLogId(int logId) {
		this.logId = logId;
	}
	public Timestamp getOccurredAt() {
		return occurredAt;
	}
	public void setOccurredAt(Timestamp occurredAt) {
		this.occurredAt = occurredAt;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public Integer getExecutionTimeMs() {
		return executionTimeMs;
	}

	public void setExecutionTimeMs(Integer executionTimeMs) {
		this.executionTimeMs = executionTimeMs;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
}
