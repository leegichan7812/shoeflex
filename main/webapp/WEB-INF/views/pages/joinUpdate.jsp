<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<section>
<div class="container py-5 px-2">
    <div class="row flex-nowrap mypage">

        <!-- 사이드 메뉴 -->
        <div class="col-lg-2 col-md-4 mypage-sidebar">
            <h5 style="padding-left:15px; font-size:25px;">마이페이지</h5>
            <ul class="nav flex-column" style="font-size:18px;">
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="loadPage('myPage')">주문/배송 조회</a>
                </li>
				<li class="nav-item">
				  <a class="nav-link" href="#" onclick="loadPage('${ctx}/after-sales/Custlist')">취소/반품/교환</a>
				</li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="loadPage('joinUpdate')">회원정보 수정/탈퇴</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="loadPage('cart')">장바 구니</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="loadPage('review')">리뷰 관리</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="loadPage('like')">찜 목록</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="loadPage('faq')">FAQ</a>
                </li>
				<li class="nav-item">
				<a class="nav-link" href="#" onclick="ChatWidget.toggle(); return false;">1:1 문의</a>
				</li>
                
            </ul>
        </div>
       <div class="col">
	       <!-- 회원 비밀번호 입력시 보이게 -->
			<div id="pwdCheckSection" class="container" style="align-items:center;justify-content:center;">
				<div class="p-5 bg-white rounded-3 shadow" style="max-width:500px; width:100%; margin:0 auto;">
				    <h3 class="fw-bold mb-4 text-center">비밀번호 확인</h3>
				    <form id="pwdCheckForm">
				        <div class="mb-3">
				            <input type="password" id="checkPwd" class="form-control" placeholder="비밀번호를 입력하세요" />
				        </div>
				        <div class="text-center">
				            <button type="button" id="pwdCheckBtn" class="btn btn-primary w-100">확인</button>
				        </div>
				    </form>
				</div>
			</div>
	       <!-- 회원 수정 업데이트 -->
	       <div id="userUpdateSection" style="display:none;">
		       <div class="container d-flex justify-content-center">
			    <div class="p-5 bg-white rounded-3 shadow" style="max-width: 500px; width: 100%;">
			      <h3 class="fw-bold mb-4" style="text-align:center;">회원 정보 수정/탈퇴</h3>
		     		<form id="joinUpt" method="post">
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
					    <input class="form-control" type="text" name="name" value="${ulist.name}" required/>
					</div>
			        <div class="mb-3">
		          		<label class="form-label">핸드폰</label>
		          		<input class="form-control" type="tel" name="phone" value="${ulist.phone}" required />
			        </div>
			        <div class="mb-3">
		          		<label class="form-label">주소</label>
		          		<input class="form-control" type="text" name="address" value="${ulist.address}" required />
			        </div>      
			        <div class="text-center">
			          <button id="joinUptBtn" class="btn btn-primary w-100" type="button">수정</button>
			        </div>
			        <div class="text-center" style="padding-top:10px;">
			          <button id="joinDelBtn" class="btn btn-primary w-100" style="background-color:red;border:none;" type="button">탈퇴</button>
			        </div> 
			      </form>	      	
			    </div>
			  </div>
		  </div>
	  </div>
	  <!-- 모달 창 -->
	  <div class="modal fade" id="withdrawModal" tabindex="-1" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content" >
		      <div class="modal-header">
		        <h5 class="modal-title">탈퇴 사유를 선택해주세요</h5>
		        <button type="button" class="joinupt-close" data-bs-dismiss="modal">x</button>
		      </div>
		      <div class="modal-body">
		        <select id="withdrawReason" class="form-select w-100" style="height: 50px;font-size: 16px;">
		          <!-- Ajax로 reasonList를 채움 -->
		        </select>
		        <textarea id="etcReason" class="form-control w-100 mt-3" style="display:none;height: 100px; font-size: 15px;" placeholder="기타 사유를 입력하세요"></textarea>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
		        <button id="withdrawSubmit" class="btn btn-danger">탈퇴하기</button>
		      </div>
		    </div>
		  </div>
		</div>
	  <!-- 회원 수정/탈퇴 끝 -->	  
	</div>  
</div>



</section>
      

