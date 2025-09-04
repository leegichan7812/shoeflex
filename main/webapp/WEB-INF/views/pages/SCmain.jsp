<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<script>

// JSP → JS로 로그인 여부 전달
var isLoggedIn = ${not empty sessionScope.loginUser};

function openQnaTab() {
  if (!isLoggedIn) {
      alert('로그인이 필요한 서비스입니다.');
    // 로그인 안 한 경우 로그인 페이지로 이동
    loadPage('glogin');   // AJAX 로드 (이미 사용 중이신 방식)
    // window.location.href = '/login'; // 전체 페이지 이동 시
    return false;
  }
  // 로그인 한 경우 → 채팅 위젯 열기
  ChatWidget.toggle();
  return false;
}
//SCmain 전용 검색: 목록 페이지로 이동시킴
function searchFaq() {
  const keyword = (document.getElementById("faqKeyword").value || "").trim();
  const url = "/faq" + (keyword ? ("?keyword=" + encodeURIComponent(keyword)) : "");
  loadPage(url);   // 이미 쓰시는 SPA 네비 함수
  return false;
}

function searchNotice() {
  const el = document.getElementById("noticeKeyword");
  if (!el) return false;
  const keyword = el.value.trim();
  const url = "/support/notice" + (keyword ? ("?keyword=" + encodeURIComponent(keyword)) : "");
  loadPage(url);
  return false;
}
</script>

<section id="SCmain" class="container my-4 container-custom bg-white">
	<h3><i class="fa-solid fa-headset"></i>&nbsp; <span id="pageTitle"><fmt:message key="sc.title"/></span></h3>

  <!-- 탭 네비게이션 -->
<div class="container-fluid mt-3">
  <ul class="nav nav-tabs mb-4" role="tablist">
    <li class="nav-item">
      <a id="faqTab" class="nav-link h6" href="#" onclick="loadPage('/faq')">
        <fmt:message key="sc.tab.faq"/>
      </a>
    </li>
    <li class="nav-item">
      <a id="qnaTab" class="nav-link h6" href="#" onclick="return openQnaTab();">	      
        <fmt:message key="notice.tab.qna"/>
      </a>
    </li>
    <li class="nav-item">
      <a id="noticeTab" class="nav-link h6" href="#" onclick="loadPage('/support/notice')">
        <fmt:message key="sc.tab.notice"/>
      </a>
    </li>
  </ul>
</div>

  <!-- FAQ TOP5 섹션 -->
  <div id="faq">
	<h4 class="fw-bold mb-3" id="faqTop5"><fmt:message key="faq.top5"/></h4>

    <!-- 검색 폼 -->
    <div class="container faq-search-container mb-2" style="padding: 10px 5px;">
      <div class="row align-items-center mb-2">
		<div class="col-md-2 font-weight-bold" id="faqSearchLabel">
		  <fmt:message key="sc.faq.search"/>
		</div>
        <div class="col-md-10">
		<form class="form-inline d-flex" onsubmit="return searchFaq()">
		  <input type="text" name="keyword" id="faqKeyword" value="${keyword}" 
		         class="form-control mr-2" style="max-width:500px;" 
		         placeholder="<fmt:message key='sc.faq.search'/>">
		  <button type="submit" class="btn btn-outline-secondary">
		    <i class="fa fa-search"></i>
		  </button>
		</form>
        </div>
      </div>
    </div>
    <!-- 테이블 -->
    <table class="table table-bordered table-hover">
      <thead class="thead-dark text-center">
		  <tr>
		    <th id="faqNo"><fmt:message key="faq.no"/></th>
		    <th id="faqCategory"><fmt:message key="faq.category"/></th>
		    <th id="faqQuestion"><fmt:message key="faq.question"/></th>
		    <th id="faqDate"><fmt:message key="faq.date"/></th>
		  </tr>
	<tbody>
	  <c:forEach var="f" items="${faqList}" varStatus="st">
	    <!-- Bootstrap 4: data-toggle + data-target 만 사용 -->
	    <tr data-toggle="modal" data-target="#faqModal${f.faqId}" style="cursor:pointer;">
	      <td class="text-center">${st.count}</td>
	      <td class="text-center">${f.category}</td>
	      <td>${f.question}</td>
	      <td class="text-center">
	        <fmt:formatDate value="${f.createdAt}" pattern="yyyy-MM-dd"/>
	      </td>
	    </tr>
	  </c:forEach>
	
	  <c:if test="${empty faqList}">
	    <tr>
	      <td colspan="4" class="text-center text-muted">
	        <fmt:message key="faq.noData"/>
	      </td>
	    </tr>
	  </c:if>
	</tbody>
    </table>

    <!-- FAQ 모달들 -->
  <c:forEach var="f" items="${faqList}">
  <div class="modal fade" id="faqModal${f.faqId}" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content bg-light border-dark">
        <div class="modal-header bg-dark text-white">
          <h5 class="modal-title">${f.question}</h5>
          <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>

        <div class="modal-body">
          <p>${f.answer}</p>
        </div>

        <div class="modal-footer text-muted">
          <fmt:message key="notice.viewcount"/>:
          <fmt:formatDate value="${f.createdAt}" pattern="yyyy-MM-dd"/>
        </div>
      </div>
    </div>
  </div>
</c:forEach>
  </div>

  <!-- NOTICE TOP5 섹션 -->
  <div id="notice" class="mt-5">
	<h4 class="fw-bold mb-3" id="noticeTop5"><fmt:message key="notice.top5"/></h4>

<!-- 공지 검색 폼 -->
<div class="container faq-search-container mb-2" style="padding: 10px 5px;">
  <div class="row align-items-center mb-2">
    <div class="col-md-2 font-weight-bold" id="noticeSearchLabel2">
      <fmt:message key="notice.search2"/>
    </div>
    <div class="col-md-10">
      <form class="form-inline d-flex" onsubmit="return searchNotice()">
        <input type="text" name="keyword" id="noticeKeyword"
               class="form-control mr-2" style="max-width:500px;"
               placeholder="<fmt:message key='notice.search.placeholder'/>">
        <button type="submit" class="btn btn-outline-secondary">
          <i class="fa fa-search"></i>
        </button>
      </form>
    </div>
  </div>
</div>

    <!-- 테이블 -->
    <table class="table table-bordered table-hover">
		<thead class="thead-dark text-center">
		  <tr>
		    <th id="noticeNo"><fmt:message key="notice.no"/></th>
		    <th id="noticeTitle"><fmt:message key="notice.title"/></th>
		    <th id="noticeDate"><fmt:message key="notice.date"/></th>
		  </tr>
		</thead>
  <tbody>
    <!-- 1) 먼저 고정된 공지들만 출력 -->
    <c:forEach var="nt" items="${noticeList}">
      <c:if test="${nt.isPinned == 'Y'}">
        <tr data-toggle="modal"
            data-target="#noticeModal${nt.noticeId}"
            style="cursor:pointer;">
	      <td class="text-center noticePin">
			<span class="pinText"><fmt:message key="notice.pin"/></span>
		    <sup class="text-danger font-weight-bold">〃</sup>
		  </td>
          <td class="text-left font-weight-bold">${nt.title}</td>
          <td class="text-center">
            <fmt:formatDate value="${nt.createdAt}" pattern="yyyy.MM.dd"/>
          </td>
        </tr>
      </c:if>
    </c:forEach>

    <!-- 2) 고정이 아닌 나머지에 번호 매기기 -->
    <c:set var="cnt" value="0"/>
    <c:forEach var="nt" items="${noticeList}">
      <c:if test="${nt.isPinned != 'Y'}">
        <!-- cnt 하나 올리고 출력 -->
        <c:set var="cnt" value="${cnt + 1}" />
        <tr data-toggle="modal"
            data-target="#noticeModal${nt.noticeId}"
            style="cursor:pointer;">
          <td class="text-center">${cnt}</td>
          <td class="text-left font-weight-bold">${nt.title}</td>
          <td class="text-center">
            <fmt:formatDate value="${nt.createdAt}" pattern="yyyy.MM.dd"/>
          </td>
        </tr>
      </c:if>
    </c:forEach>

    <!-- 3) 리스트가 비었을 때 처리 -->
    <c:if test="${empty noticeList}">
      <tr>
         <td colspan="3" class="text-center text-muted">
     	 <fmt:message key="notice.noData"/>
    	</td>
      </tr>
    </c:if>
  </tbody>
</table>

    <!-- NOTICE 모달들 -->
    <c:forEach var="nt" items="${noticeList}">
      <div
        class="modal fade"
        id="noticeModal${nt.noticeId}"
        tabindex="-1"
        role="dialog"
        aria-hidden="true"
      >
        <div class="modal-dialog modal-lg" role="document">
          <div class="modal-content bg-light border-dark">
            <div class="modal-header bg-dark text-white">
              <h5 class="modal-title">${nt.title}</h5>
              <button
                type="button"
                class="close text-white"
                data-dismiss="modal"
                aria-label="Close"
              >
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <p>${nt.content}</p>
            </div>
            <div class="modal-footer text-muted">
               <fmt:message key="notice.viewcount"/>: ${nt.viewCount}
              &nbsp;|&nbsp;
              <fmt:formatDate value="${nt.createdAt}" pattern="yyyy.MM.dd" />
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</section>
