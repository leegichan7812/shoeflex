<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

	<section class="pb-11 pt-7 bg-600">
	  <div class="container d-flex justify-content-center">
	    <div class="p-5 bg-white rounded-3 shadow" style="max-width: 500px; width: 100%;">
	      <h1 class="mb-4 text-center">로그인</h1>
     		<form id="loginForm">
	        <div class="mb-3">
	          	<label class="form-label">아이디</label>
          		<input class="form-control" type="text" name="email" id="email" required />
	        </div>
	        <div class="mb-3">
	          	<label class="form-label">비밀번호</label>
	         	<input class="form-control" type="password" name="password" id="password" required />
	        </div>
	       
	        <div class="text-center">
	          <button class="btn btn-primary w-100" type="submit">로그인</button>
	        </div>
	      </form>
			<div class="mt-3 text-center">
			  <a class="btn btn-primary w-100" href="#" onclick="loadPage('insertJoin')">회원가입</a>
			</div>
			<div id="login-error-msg" class="text-center mt-3"></div>
			<div class="mt-3 text-center">
			    <a href="#" data-bs-toggle="modal" data-bs-target="#findIdModal" class="text-decoration-none text-muted me-2">
			        아이디 찾기
			    </a>
			    <span class="text-muted">|</span>
			    <a href="#" data-bs-toggle="modal" data-bs-target="#resetPwModal" class="text-decoration-none text-muted ms-2">
			        비밀번호 찾기
			    </a>
			</div>
	        
	    </div>
	  </div>
	  
	  <!-- 아이디 찾기 모달 -->
	  <div class="modal fade" id="findIdModal" tabindex="-1" aria-hidden="true">
	    <div class="modal-dialog">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title">아이디 찾기</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
	        </div>
	        <div class="modal-body">
	          <form id="findIdForm">
	            <div class="mb-3">
	              <label class="form-label">이름</label>
	              <input type="text" class="form-control" name="name" required>
	            </div>
	            <div class="mb-3">
	              <label class="form-label">휴대폰 번호</label>
	              <input type="text" class="form-control" name="phone" placeholder="010-1234-5678" required>
	            </div>
	            <div id="findIdResult" class="text-center small text-muted"></div>
	          </form>
	        </div>
	        <div class="modal-footer">
	          <button class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
	          <button class="btn btn-primary" id="btnFindId">아이디 찾기</button>
	        </div>
	      </div>
	    </div>
	  </div>

	  <!-- 비밀번호 찾기 모달 -->
	  <div class="modal fade" id="resetPwModal" tabindex="-1" aria-hidden="true">
	    <div class="modal-dialog">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title">비밀번호 찾기</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
	        </div>
	        <div class="modal-body">
	          <form id="resetPwForm">
	            <div class="mb-3">
	              <label class="form-label">아이디(이메일)</label>
	              <input type="email" class="form-control" name="email" required>
	            </div>
	            <div class="mb-3">
	              <label class="form-label">이름</label>
	              <input type="text" class="form-control" name="name" required>
	            </div>
	            <div class="mb-3">
	              <label class="form-label">휴대폰 번호</label>
	              <input type="text" class="form-control" name="phone" placeholder="010-1234-5678" required>
	            </div>
	            <div id="resetPwResult" class="text-center small text-muted"></div>
	          </form>
	        </div>
	        <div class="modal-footer">
	          <button class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
	          <button class="btn btn-primary" id="btnResetPw">임시 비밀번호 발송</button>
	        </div>
	      </div>
	    </div>
	  </div>

        <!-- end of .container-->

      </section>
