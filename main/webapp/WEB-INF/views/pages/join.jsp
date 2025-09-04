<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

	<section class="pb-11 pt-7 bg-600">
	  <div class="container d-flex justify-content-center">
	    <div class="p-5 bg-white rounded-3 shadow" style="max-width: 500px; width: 100%;">
	      <h1 class="mb-4 text-center">회원 가입</h1>
     		<form id="joinForm" method="post" action="insertJoin">
	        <div class="mb-3">
	          	<label class="form-label">이메일</label>
          		<input class="form-control" type="text" name="email" id="email" required 
          		pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,}"/>
          		<span id="email-check-msg"></span>
	        </div>
	        <div class="mb-3">
	          	<label class="form-label">비밀번호</label>
	         	<input class="form-control" type="password" name="password" id="password" required />
	         	<span id="pwdValid" class="error-msg"></span>
	        </div>
	        <div class="mb-3">
	          	<label class="form-label">비밀번호 확인</label>
	          	<input class="form-control" type="password" id="confirmPassword" required />
         		<span id="pwdMatch" class="error-msg"></span>
	        </div>
			<div class="mb-3">
			    <label class="form-label">이름</label>
			    <input class="form-control" type="text" name="name" required/>
			</div>
	        <div class="mb-3">
          		<label class="form-label">핸드폰</label>
          		<input class="form-control" type="tel" name="phone" required />
	        </div>
	        <div class="mb-3">
          		<label class="form-label">주소</label>
          		<input class="form-control" type="text" name="address" required />
	        </div>
	        <div class="mb-3">
		        <input class="form-control" type="hidden" value="USER" name="accountType" readonly required/>
	        </div>
	        <div class="mb-3">
	          	<label class="form-label">나이</label>
	         	<input class="form-control" type="number" name="age" required/>
	        </div>
     		<div class="mb-3">
			    <label class="form-label">성별</label>
			    <div class="d-flex gap-2">
			        <input type="radio" class="btn-check" name="gender" id="male" value="남" autocomplete="off" checked>
			        <label class="form-control text-center flex-fill" style="cursor:pointer;" for="male">남자</label>
			
			        <input type="radio" class="btn-check" name="gender" id="female" value="여" autocomplete="off">
			        <label class="form-control text-center flex-fill" style="cursor:pointer;" for="female">여자</label>
			    </div>
			</div>
	        <div class="mb-3">
		        <input class="form-control" type="hidden" value="정상" name="accountStatus" readonly required/>
	        </div>
	        <div class="text-center">
	          <button class="btn btn-primary w-100" type="submit">가입하기</button>
	        </div>
	      </form>
	      <div id="resultMsg" class="mt-3 text-center"></div>
	    </div>
	  </div>

        <!-- end of .container-->

      </section>
      

