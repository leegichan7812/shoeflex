<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
	.fc .fc-button {
		display: inline-block;
		color: #000 !important;
		background: #f0f0f0 !important;
		border: 1px solid #ccc !important;
		padding: 5px 10px !important;
		font-size: 14px !important;
	}
 	/* 캘린더 전용 모달 스타일 */
	#calendarModal .modal-content {
	  background-color: #ffffff;
	  color: #000;
	  border-radius: 8px;
	  border: 1px solid #ccc;
	}
	
	#calendarModal .form-control,
	#calendarModal .form-select,
	#calendarModal textarea {
	  background-color: #fff !important;
	  color: #000 !important;
	  border: 1px solid #ccc !important;
	}
	
	#calendarModal input[type="color"] {
	  padding: 0 !important;
	  height: 2.2rem;
	}
	
	#calendarModal label,
	#calendarModal option {
	  color: #000 !important;
	}
	
	#calendarModal .modal-header {
	  background-color: #f8f9fa;
	  border-bottom: 1px solid #ddd;
	}
	
	#calendarModal .modal-footer {
	  border-top: 1px solid #ddd;
	  background-color: #f8f9fa;
	}
	
	#calendarModal .btn {
	  font-weight: 500;
	}
	#calendarModal select.form-select {
	    height: calc(2.25rem + 2px);  /* Bootstrap input 높이와 동일 */
	    padding: 0.375rem 0.75rem;
	    font-size: 1rem;
	    line-height: 1.5;
	    border-radius: 0.25rem;
	    width:100%;
	}
</style>
<section>
<div class="row">
  <div class="col-12 grid-margin stretch-card">
    <div class="card">
      <div class="card-body">
        <div id="calendar"></div>
      </div>
    </div>
  </div>
</div>

<!-- 일정 모달 (숨겨진 트리거 포함) -->
<button id="showModal" data-bs-toggle="modal" data-bs-target="#calendarModal" style="display:none;"></button>

<div class="modal fade" id="calendarModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 id="modalTitle" class="modal-title">일정 등록</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <form id="frm" method="post">
          <input type="hidden" name="id" />
          
          <input type="hidden" name="userId" value="${sessionScope.loginUser.userId}"/>
          <!-- 백업용 -->
		  <input type="hidden" id="userIdVal" value="${sessionScope.loginUser.userId}" />
		  <input type="hidden" id="loginUserNameVal" value="${sessionScope.loginUser.name}">
          <div class="row mb-2">
            <div class="col"><input name="title" class="form-control" placeholder="제목" /></div>
            <div class="col"><input name="writer" class="form-control" value="${sessionScope.loginUser.name}" readonly /></div>
          </div>
          <div class="row mb-2">
            <div class="col"><input name="start" class="form-control" readonly /></div>
            <div class="col"><input name="end" class="form-control" readonly /></div>
          </div>
          <div class="row mb-2">
            <div class="col"><input name="backgroundColor" class="form-control" type="color" value="#0099cc" /></div>
            <div class="col"><input name="textColor" class="form-control" type="color" value="#ccffff" /></div>
          </div>
          <div class="row mb-2">
            <div class="col">
              <select name="allDay" class="form-select">
                <option value="1">종일</option>
                <option value="0">시간</option>
              </select>
            </div>
            <div class="col">
              <select name="status" class="form-select">
                <option value="이벤트">이벤트</option>
                <option value="공지">공지</option>
                <option value="휴가">휴가</option>
              </select>
            </div>
          </div>
          <div class="row mb-2" id="isPublicRow" style="display:none;">
			  <div class="col">
			    <select name="isPublic" class="form-select">
			      <option value="Y">공개</option>
			      <option value="N">비공개</option>
			    </select>
			  </div>
			</div>
          <textarea name="content" class="form-control mb-2" placeholder="내용" rows="4"></textarea>
        </form>
      </div>
      <div class="modal-footer">
        <button id="regBtn" class="btn btn-success">등록</button>
        <button id="uptBtn" class="btn btn-primary">수정</button>
        <button id="delBtn" class="btn btn-danger">삭제</button>
        <button class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
      </div>
    </div>
  </div>
</div>
</section>
      

