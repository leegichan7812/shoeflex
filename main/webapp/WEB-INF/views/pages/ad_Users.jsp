<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>

<title>회원 조회 및 통계</title>
<style>


.top-bar {
	background-color: white;
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding:20px;
	margin:0px 10px;
	border-bottom: 2px solid;
	font-size: 20px;
	font-weight: bold;
	border-radius:10px;
}

.top-title {
	color: #333;
}

.top-search {
	display: flex;
	gap: 4px;
}

.top-search input {
	padding: 12px 14px;
	border: 1px solid #888;
	border-radius: 4px;
	font-size: 16px;
	width: 400px;  
}

.top-search button {
	background-color: #5878b8;
	border: none;
	color: white;
	padding: 12px 18px;
	border-radius: 4px;
	cursor: pointer;
	font-weight: bold;
	font-size: 16px;  
}

.grid-container {
	display: grid;
	grid-template-columns:  1fr 1fr 1fr; /*1fr 1fr ;*/
	grid-template-rows: 450px; /* 흰색바탕 높이 조절 */
	gap: 10px;
	padding: 10px;
	max-width: 1800px; /* ★ 이걸 늘리면 전체 박스 너비 증가 */
	margin: auto;
	overflow-y: auto;  /* ✅내부 내용이 넘치면 스크롤처리 */
  	
}
.grid-container2{
	display: grid;
	grid-template-columns:  1fr 1fr; /*1fr 1fr ;*/
	grid-template-rows: 450px; /* 흰색바탕 높이 조절 */
	gap: 10px;
	padding: 10px;
	padding-top:0px;
	max-width: 1800px; /* ★ 이걸 늘리면 전체 박스 너비 증가 */
	margin: auto;
  	overflow-y: auto;  /* ✅내부 내용이 넘치면 스크롤처리 */
}

.box {
	background: #fff;
	border-radius: 15px;
	box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
	padding: 40px;
	overflow: hidden;
}

.section-title {
	font-weight: bold;
	font-size: 18px;
	color: #764ba2;
	margin-bottom: 15px;
}



/* 회원조회 폼 */
.box form label {
	display: block;
	margin: 1px 0 1px; /* ✅ 위아래 간격 줄이기 (기존: margin: 4px 0 2px) */
}

.box input {
	width: 100%;
	padding: 8px;
	border-radius: 6px;
	border: 1px solid #ccc;
	margin-bottom: 5px;
	font-size: 14px;   /* ✅ 글자 크기 키우기 */
}

.box button {
	width: 100%;
	padding: 40px; 
	background: #764ba2;
	color: #fff;
	font-weight: bold;
	border: none;
	border-radius: 8px;
	cursor: pointer;
}

table {
	width: 100%;
	border-collapse: collapse;
	font-size: 16px;
}

th, td {
	padding: 10px;
	text-align: center;
	border-bottom: 1px solid #ddd;	
}

th {
	background: #f3e9ff;
	color: #5a3686;
}

canvas {
	height: 350px !important; /*그래프 높이조절*/
}

.form-row {
	width:95%;
	display: flex;
	gap: 15px;
	margin-bottom: 10px; /* ✅ 이게 아래 폼과의 간격 */
}

.form-item {
	flex: 1; /* 동일한 너비로 나눔 */
	display: flex;
	flex-direction: column;
}
.searchForm{
	width: 100%;
}

<!-- 비고란 -->
.box textarea {
	width: 100%;
	padding: 10px;
	border-radius: 6px;
	border: 1px solid #ccc;
	resize: vertical; /* 사용자가 크기 조절 가능 */
	min-height: 30px; /* ✅ 기본 높이 지정 */
	font-size: 14px;
	margin-bottom: 15px;
	font-size: 14px;   /* ✅ textarea도 동일하게 적용 */
}

#memo{
	width: 100%;
	height: 80px;
}

.review-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 15px;
}

.review-table th, .review-table td {
  padding: 10px;
  text-align: center;
  border-bottom: 1px solid #ddd;
  vertical-align: middle;
}

/* 컬럼별 고정 폭 */
.review-table .col-product { width: 50px; }
.review-table .col-content { width: 35%; }
.review-table .col-rating { width: 10%; }
.review-table .col-date   { width: 15%; }

/* 긴 텍스트 ... 처리 */





</style>
</head>
<body>



	<!-- 상단 헤더 바 -->
	<div class="top-bar">
	  <div class="top-title">회원 관리자 페이지</div>
	  <div class="top-search">
	    <form id="searchForm" onsubmit="return false;">
		  <input type="text" id="emailInput" name="email" placeholder="이메일 입력">
		  <button type="button" onclick="searchUser()">검색</button>
		</form>

	  </div>
	</div>
	
	
	<!-- 회원 정보 조회 -->
	<div class="grid-container">
	  <div class="box">
	    <div class="section-title">회원 정보 조회</div>
	    <form style="color:black;">
	      <div class="form-row">
	        <div class="form-item"><label>이름</label><input type="text" id="name"></div>
	        <div class="form-item"><label>성별</label><input type="text" id="gender"></div>
	        <div class="form-item"><label>나이</label><input type="text" id="age"></div>
	      </div>
	      <div class="form-row">
	        <div class="form-item"><label>이메일</label><input type="text" id="email"></div>
	        <div class="form-item"><label>전화번호</label><input type="text" id="phone"></div>
	      </div>
	      <div class="form-row">
	        <div class="form-item"><label>가입일자</label><input type="date" id="joinDate"></div>
	        <div class="form-item"><label>최근 활동일자</label><input type="date" id="lastActiveDate"></div>
	      </div>
	      <label for="memo">비고</label>
	      <textarea id="memo"></textarea>
	    </form>
	  </div>
	
     	

	<!-- 2. 오른쪽 상단: 그래프 --><!-- 2,3번 차트 통합 -->
	<div class="box">
		<div class="section-title">6개월 구매 금액 및 월별 판매 건수</div>

		<div class="chart-container" style="height: 200px;">
			<canvas id="purchaseChart"></canvas>
		</div>


	</div>
		
		
	
	<!-- 3. 오른쪽 상단 옆 도넛 차트 -->
	<div class="box">
  		<div class="section-title">월별 판매 건수</div>
  		<div class="chart-container" style="height: 300px;">
    	 <canvas id="doughnutChart"></canvas>
  		 </div>
	</div>
	
	</div>
	
	
	<!-- 리뷰 목록 -->
	<div class="grid-container2">
	  <div class="box">
	    <div class="section-title">리뷰 목록</div>
	    <table class="review-table" style="color:black;">
	      <thead><tr><th>상품</th><th>내용</th><th>평점</th><th>작성일</th></tr></thead>
	      <tbody></tbody>
	    </table>
	  </div>
	
	  <!-- QnA 목록 -->
	  <div class="box">
	    <div class="section-title">QnA 목록</div>
	    <table class="qna-table" style="color:black;">
	      <thead><tr><th>카테고리</th><th>제목</th><th>작성일</th><th>상태</th></tr></thead>
	      <tbody></tbody>
	    </table>
	  </div>
	</div>
			
    <script src="vendor/jquery/jquery-3.2.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	<script src="${pageContext.request.contextPath}/js/admin_user.js"></script>
	
	
</body>
</html>
