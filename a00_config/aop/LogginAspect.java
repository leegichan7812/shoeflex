package web.com.springweb.a00_config.aop;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import web.com.springweb.vo.ExceptionLog;
import web.com.springweb.vo.Users;

@Aspect
@Component
public class LogginAspect {
	@Autowired(required = false)
    private ExceptionLogDao dao;
	
	@Around("execution(* web.com.springweb..*Service.*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
	    long start = System.currentTimeMillis();
	    ExceptionLog log = new ExceptionLog();
	    String errorType = "정상";

	    // 🔽 사용자 정보 세팅
	    setSessionUserInfo(log);

	    try {
	        Object result = joinPoint.proceed();
	        return result;
	    } catch (Throwable ex) {
	        errorType = "Exception";
	        log.setExceptionType(ex.getClass().getName());
	        log.setExceptionMessage(ex.getMessage());
	        throw ex;
	    } finally {
	        long executionTime = System.currentTimeMillis() - start;

	        // 🔽 예외 없지만 5초 넘은 경우 "Timeout" 처리
	        if ("정상".equals(errorType) && executionTime > 5000) {
	            errorType = "Timeout";
	        }

	        // 🔽 로그 공통 정보 설정
	        log.setClassName(joinPoint.getSignature().getDeclaringTypeName());
	        log.setMethodName(joinPoint.getSignature().getName());
	        log.setExecutionTimeMs((int) executionTime);
	        log.setErrorType(errorType);

	        try {
	            // 🔽 Exception 또는 Timeout인 경우에만 로그 저장
	            if ("Exception".equals(errorType) || "Timeout".equals(errorType)) {
	                if (dao != null) dao.insertLog(log);
	            }
	        } catch (Exception e) {
	            System.out.println("★ 예외 로그 DB 저장 실패: " + e.getMessage());
	        }
	    }
	}

    private void setSessionUserInfo(ExceptionLog log) {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return;

            HttpServletRequest request = attrs.getRequest();
            HttpSession session = request.getSession(false);

            if (session != null && session.getAttribute("loginUser") != null) {
                Object userObj = session.getAttribute("loginUser");

                if (userObj instanceof Users) {
                    Users user = (Users) userObj;
                    log.setUserEmail(user.getEmail());
                    log.setUserName(user.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("세션 사용자 정보 추출 실패: " + e.getMessage());
        }
    }

	@AfterThrowing(pointcut = "execution(* web.com.springweb..*Service.*(..))", throwing = "ex")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
		// 콘솔 로그
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("예외 발생 시각: " + timestamp);
        System.out.println("클래스/메서드: " + joinPoint.getSignature());
        System.out.println("예외 타입: " + ex.getClass().getName());
        System.out.println("예외 메시지: " + ex.getMessage());
	    
	    // DB 기록용 객체 생성
        ExceptionLog log = new ExceptionLog();
        log.setClassName(joinPoint.getSignature().getDeclaringTypeName());
        log.setMethodName(joinPoint.getSignature().getName());
        log.setExceptionType(ex.getClass().getName());
        log.setExceptionMessage(ex.getMessage());
        
        try {
            if (dao != null) dao.insertLog(log);
        } catch (Exception e) {
            System.out.println("★ 예외 로그 DB 기록 실패: " + e.getMessage());
        }
	}
}
