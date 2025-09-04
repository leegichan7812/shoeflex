<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
function _swapFaq(html) {
	  const $html = $(html);

	  // tbody
	  const $tbody = $html.find("#faqTableBody");
	  if ($tbody.length) $("#faqTableBody").html($tbody.html());

	  // (선택) thead
	  const $thead = $html.find("#faqTable thead");
	  if ($thead.length) $("#faqTable thead").html($thead.html());

	  // pagination: ul 내용만 교체
	  const $newPagi = $html.find("#noticePagination");
	  if ($newPagi.length) {
	    $("#noticePagination").html($newPagi.html());
	    $("#noticePaginationWrap").removeClass("d-none");
	  } else {
	    $("#noticePagination").empty();
	    $("#noticePaginationWrap").addClass("d-none");
	  }

	  // 총 페이지 문구 교체/제거
	  const $total = $html.find("#totalPageInfo");
	  if ($total.length) {
	    const $existing = $("#totalPageInfo");
	    if ($existing.length) $existing.replaceWith($total);
	    else $("#noticePaginationWrap").after($total);
	  } else {
	    $("#totalPageInfo").remove();
	  }

	  // (선택) 모달 교체
	  const $modals = $html.find(".faq-modal");
	  if ($modals.length) { $(".faq-modal").remove(); $("body").append($modals); }
	}

/** 3) AJAX GET 공통 로더 */
function loadAdminPage(url) {
  $.ajax({
    url,
    type: "GET",
    dataType: "html",
    success: _swapFaq,
    error: () => alert("FAQ 목록을 불러오지 못했습니다.")
  });
}

/** 4) FAQ 목록 갱신 (notice 패턴과 동일) */
function loadFaqList(opts = {}) {
  const base = _faqBaseUrl();
  const qs   = new URLSearchParams();

  // keyword: opts 우선, 없으면 입력창 값
  const keyword = (opts.keyword ?? ($("#faqKeyword").val() || "")).toString().trim();
  if (keyword) qs.set("keyword", keyword);

  // category: opts 우선, 없으면 활성 버튼에서 추출
  const category = (opts.category ?? (function () {
    const $btn = $("#faqCategoryGroup .btn.active");
    if ($btn.length && $btn.attr("id") !== "faqCatAll") {
      return ($btn.data("category") || $btn.text()).toString().trim();
    }
    return "";
  })());
  if (category) qs.set("category", category);

  // pageSize (선택)
  if (opts.pageSize && Number.isFinite(Number(opts.pageSize))) {
    qs.set("pageSize", String(Number(opts.pageSize)));
  }

  // curPage: opts.page 우선, 없으면 noticePagination의 active에서 추출
  let page = Number(opts.page);
  if (!page) {
    const $active = $("#noticePagination .page-item.active .page-link");
    page = Number($active.data("page") || $active.text() || 1);
  }
  if (!Number.isFinite(page) || page < 1) page = 1;
  qs.set("curPage", String(page));

  // 요청
  const url = base + (qs.toString() ? ("?" + qs.toString()) : "");
  loadAdminPage(url);
}

/** 5) 페이징 클릭 위임 (한 개만!) */
$(document).on("click", "#noticePagination .page-link", function (e) {
  e.preventDefault();

  let page = Number($(this).data("page"));
  if (Number.isNaN(page)) {
    const t = ($(this).text() || "").trim();
    if (/^\d+$/.test(t)) page = Number(t);
  }

  if (!Number.isNaN(page)) {
    loadFaqList({ page });
  }
});
</script>

<style>
#faqlistbody {
  background-color: #ffffff !important; /* 흰색 배경 */
  color: #000 !important;               /* 검정 글자 */
  padding: 20px;
  border-radius: 8px;                   /* 둥근 모서리 */
}

/* ===== 탭 ===== */
.nav-tabs .nav-link {
  background-color: #f8f9fa !important; /* 밝은 회색 */
  color: #000 !important;
  border: 1px solid #dee2e6 !important;
}
.nav-tabs .nav-link.active {
  background-color: #343a40 !important; /* 다크 */
  color: #fff !important;
  border-color: #343a40 !important;
}

/* ===== 검색 박스 ===== */
.faq-search-container {
  background-color: #fff !important;
  color: #000 !important;
  border: 1px solid #dee2e6 !important;
  border-radius: 6px;
}
.faq-search-container label,
.faq-search-container div,
.faq-search-container span {
  color: #000 !important;
}

/* ===== 입력창 ===== */
.form-control {
  background-color: #fff !important;
  color: #000 !important;
  border: 1px solid #ced4da !important;
}
#faqKeyword,
#noticeKeyword { max-width: 500px; } /* 입력창 약간 확대 */

/* ===== 테이블 ===== */
.table {
  background-color: #fff !important;
  color: #000 !important;
}
.table thead th {
  background-color: #000 !important;    /* 헤더 검정 */
  color: #fff !important;
  border-color: #454d55 !important;
}
.table td {
  background-color: #fff !important;
  color: #000 !important;
  border-color: #dee2e6 !important;
}
/* 행 hover 강조 (옵션: table-notice-hover 클래스 사용 시) */
.table-notice-hover tbody tr:hover td {
  background-color: #f8f9fa !important;
}

/* ===== 버튼 ===== */
.btn-dark {
  background-color: #343a40 !important;
  color: #fff !important;
  border: none !important;
  border-radius: .5rem;
}
.btn-outline-secondary {
  color: #000 !important;
  border-color: #6c757d !important;
}
.btn-outline-secondary:hover {
  background-color: #6c757d !important;
  color: #fff !important;
}
.btn-outline-danger {
  color: #dc3545 !important;
  border-color: #dc3545 !important;
}
.btn-outline-danger:hover {
  background-color: #dc3545 !important;
  color: #fff !important;
}

/* ===== 모달 ===== */
.modal-content {
  background-color: #fff !important;
  color: #000 !important;
  border-radius: 8px;
}
.modal-header {
  background-color: #000 !important;
  color: #fff !important;
}
.modal-body {
  background-color: #fff !important;
  color: #000 !important;
}

#noticelistbody {
    background-color: #ffffff !important; /* 흰색 배경 */
    color: black !important;              /* 검은 글씨 */
    padding: 20px;
}

/* 네비게이션 탭 */
.nav-tabs .nav-link {
    background-color: #f8f9fa !important; /* 밝은 회색 */
    color: black !important;
    border: 1px solid #dee2e6 !important;
}
.nav-tabs .nav-link.active {
    background-color: #343a40 !important; /* 검정색 */
    color: white !important;
    border-color: #343a40 !important;
}

/* 검색 영역 */
.faq-search-container {
    background-color: #ffffff !important;
    color: black !important;
    border: 1px solid #dee2e6;
    border-radius: 6px;
}
.faq-search-container label,
.faq-search-container div,
.faq-search-container span {
    color: black !important;
}

/* 테이블 */
.table {
    color: black !important;
    background-color: #ffffff !important;
}
.table th {
    background-color: #000000 !important; /* 헤더 검정색 */
    color: white !important;
    border-color: #454d55 !important;
}
.table td {
    background-color: #ffffff !important; /* 본문 흰색 */
    color: black !important;
    border-color: #dee2e6 !important;
}

/* 버튼 */
.btn-dark {
    background-color: #343a40 !important;
    color: white !important;
    border: none !important;
}
.btn-outline-secondary {
    color: black !important;
    border-color: #6c757d !important;
}
.btn-outline-secondary:hover {
    background-color: #6c757d !important;
    color: white !important;
}
.btn-outline-danger {
    color: #dc3545 !important;
    border-color: #dc3545 !important;
}
.btn-outline-danger:hover {
    background-color: #dc3545 !important;
    color: white !important;
}

/* 모달 */
.modal-content {
    background-color: #ffffff !important; /* 흰색 배경 */
    color: black !important;
    border-radius: 8px;
}
.modal-header {
    background-color: #000000 !important; /* 검정색 헤더/푸터 */
    color: white !important;
}
.modal-body {
    background-color: #ffffff !important; /* 본문 흰색 */
    color: black !important;
}

/* 입력창 */
.form-control {
    background-color: #ffffff !important;
    color: black !important;
    border: 1px solid #ced4da !important;
}
</style>
<section id="faqlistbody" style="background-color:white;">
<div class="container my-5 container-custom bg-white">

<c:set var="loginUser" value="${sessionScope.loginUser}" />

  <!-- 상단 탭 네비게이션 -->
  <div class="container-fluid mt-3">
    <h3 class="mb-4">
      <i class="fa-solid fa-clipboard-question"></i>&nbsp;
      <span id="faqTitle"><fmt:message key="faq.title"/></span>
    </h3>
    <ul class="nav nav-tabs mb-4" role="tablist">
      <li class="nav-item">
        <a id="faqTab" class="nav-link h6 active" href="#" onclick="loadPage('/faq')">
          <fmt:message key="notice.tab.faq"/>
        </a>
      </li>
	  <li class="nav-item">
	    <a id="qnaTab" class="nav-link h6" href="#" onclick="ChatWidget.toggle(); return false;">	      
	      <fmt:message key="notice.tab.qna"/>
	    </a>
	  </li>
      <li class="nav-item">
        <a id="noticeTab" class="nav-link h6" href="#" onclick="loadPage('/support/notice')">
          <fmt:message key="notice.tab.notice"/>
        </a>
      </li>
    </ul>
  </div>
	 <!-- 카테고리 버튼 그룹 -->
	<div class="row">
  <div class="btn-group col-6 mt-2" role="group" id="faqCategoryGroup">
    <button class="btn btn-outline-secondary table-notice-hover" id="faqCatAll"
            onclick="filterFaqByCategory('', false)">
      <fmt:message key="faq.category.all"/>
    </button>
    <button class="btn btn-outline-secondary" id="faqCatOrder"
            onclick="filterFaqByCategory('주문', false)">
      <fmt:message key="faq.category.order"/>
    </button>
    <button class="btn btn-outline-secondary" id="faqCatDelivery"
            onclick="filterFaqByCategory('배송', false)">
      <fmt:message key="faq.category.delivery"/>
    </button>
    <button class="btn btn-outline-secondary" id="faqCatMember"
            onclick="filterFaqByCategory('회원', false)">
      <fmt:message key="faq.category.member"/>
    </button>
    <button class="btn btn-outline-secondary" id="faqCatProduct"
            onclick="filterFaqByCategory('상품', false)">
      <fmt:message key="faq.category.product"/>
    </button>
    <button class="btn btn-outline-secondary" id="faqCatPayment"
            onclick="filterFaqByCategory('주문/결제', false)">
      <fmt:message key="faq.category.payment"/>
    </button>
    <button class="btn btn-outline-secondary" id="faqCatRefund"
            onclick="filterFaqByCategory('교환/반품/취소', false)">
      <fmt:message key="faq.category.refund"/>
    </button>
    <button class="btn btn-outline-secondary" id="faqCatCoupon"
            onclick="filterFaqByCategory('쿠폰', false)">
      <fmt:message key="faq.category.coupon"/>
    </button>
  </div>
</div>
  


  <!-- 검색 -->
  <div class="container faq-search-container">
    <div class="row align-items-center mb-3">
	<div class="col-md-2 font-weight-bold" id="faqSearchLabel">
	  <fmt:message key="faq.search"/>
	</div>
      <div class="col-md-10">
		<form class="form-inline d-flex" onsubmit="return searchFaq()">
		  <input type="text" id="faqKeyword" class="form-control mr-2"
		         placeholder="<fmt:message key='faq.search.placeholder'/>"
		         value="${keyword}" />
		  <button type="submit" class="btn btn-outline-secondary">
		    <i class="fa fa-search"></i>
		  </button>
		</form>
      </div>
    </div>
  </div>
<%-- 세션에서 loginUser 객체를 가져옵니다. --%>
<c:set var="loginUser" value="${sessionScope.loginUser}" />


  <!-- FAQ 목록 테이블 -->
  <table class="table table-bordered table-notice-hover ">
    <thead class="thead-dark text-center">
		<tr>
		  <th id="faqNo"><fmt:message key="faq.no"/></th>
		  <th id="faqCategory"><fmt:message key="faq.category"/></th>
		  <th id="faqQuestion"><fmt:message key="faq.question"/></th>
		  <th id="faqDate"><fmt:message key="faq.date"/></th>

		</tr>
</thead>
<tbody id="faqTableBody">
  <c:forEach var="f" items="${faqList}" varStatus="st">
    <tr id="faqRow${f.faqId}" onclick="openFaqModal(${f.faqId})" style="cursor:pointer;">
	  <td class="text-center">
		  ${totalCount - ((curPage - 1) * pageSize + st.index)}
	  </td>
      <td class="text-center">${f.category}</td>
      <td>${f.question}</td>
      <td class="text-center"><fmt:formatDate value="${f.createdAt}" pattern="yyyy-MM-dd"/></td>

    </tr>
  </c:forEach>
</tbody>
  </table>
  
<!-- 페이지 네비게이션 -->
<c:if test="${totalPage > 1}">
  <div id="noticePaginationWrap">
    <nav class="mt-3">
      <ul id="noticePagination" class="pagination justify-content-center">
        <c:if test="${curPage > 1}">
          <li class="page-item">
            <a class="page-link" href="#" data-page="1"><fmt:message key="처음"/></a>
          </li>
        </c:if>

        <c:if test="${startPage > 1}">
          <li class="page-item">
            <a class="page-link" href="#" data-page="${startPage - 1}"><fmt:message key="이전"/></a>
          </li>
        </c:if>

        <c:forEach begin="${startPage}" end="${endPage}" var="p">
          <li class="page-item ${p == curPage ? 'active' : ''}">
            <a class="page-link" href="#" data-page="${p}">${p}</a>
          </li>
        </c:forEach>

        <c:if test="${endPage < totalPage}">
          <li class="page-item">
            <a class="page-link" href="#" data-page="${endPage + 1}"><fmt:message key="다음"/></a>
          </li>
        </c:if>

        <c:if test="${curPage < totalPage}">
          <li class="page-item">
            <a class="page-link" href="#" data-page="${totalPage}"><fmt:message key="마지막"/></a>
          </li>
        </c:if>
      </ul>
    </nav>
  </div>

  <!-- 총 페이지 문구도 블럭이 있을 때만 노출 -->
  <div id="totalPageInfo" class="text-center text-secondary" style="margin-bottom:10px;">
    총 <strong>${totalPage}</strong> 페이지
  </div>
</c:if>


<!-- FAQ 상세조회 모달 -->
  <c:forEach var="f" items="${faqList}">
  <div class="modal fade faq-modal" id="faqModal${f.faqId}" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content bg-light border-dark">
        <div class="modal-header bg-dark text-white">
          <h5 class="modal-title">${f.question}</h5>
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
</section>
