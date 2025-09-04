<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
function _faqBaseUrl() {
	  const v = String($("#faqTableBody").data("is-admin") || "").toLowerCase();
	  const isAdmin = (v === "true" || v === "y" || v === "1");
	  return isAdmin ? "/faqA" : "/faq";
	}

	function loadFaqList({ page = 1, pageSize = 10, keyword } = {}) {
	  keyword = keyword ?? ($("#faqKeyword").val() || "").trim();

	  $.ajax({
	    url: _faqBaseUrl(),
	    type: "GET",
	    dataType: "html",
	    data: { curPage: page, pageSize, keyword },
	    success: function (html) {
	      const $html = $(html);
	      const $newBody = $html.find("#faqTableBody");
	      const $newPag  = $html.find("#faqPagination").parent(); // <nav> 래퍼까지 갈아끼움
	      if ($newBody.length) $("#faqTableBody").html($newBody.html());
	      if ($newPag.length)  $("#faqPagination").parent().html($newPag.html());
	    },
	    error: function () {
	      alert("FAQ 목록을 불러오지 못했습니다.");
	    }
	  });
	}

</script>

<style>

</style>

<style>

#span01 {
  color: black !important;
}
#faqCategoryGroup .btn {
  min-width: 100px;   /* 버튼 가로 길이 */
  height: 40px;       /* 버튼 높이 */
  font-size: 13px;    /* 글자 크기 살짝 줄임 */
  white-space: nowrap; /* 줄바꿈 방지 */
}
.text-muted {
    color: black !important;
}
/* 전체 section 배경 */
#faqlistbody {
    background-color: #ffffff !important; /* 흰색 배경 */
    color: black !important;              /* 검은 글씨 */
}

/* 네비게이션 탭 */
.nav-tabs .nav-link {
    background-color: #f8f9fa !important; /* 밝은 회색 */
    color: black !important;
    border: 1px solid #dee2e6 !important;
}
.nav-tabs .nav-link.active {
    background-color: #343a40 !important; /* 진한 검정 */
    color: white !important;
}

/* 검색 영역 */
.faq-search-container {
    background-color: #ffffff !important; /* 흰색 */
    color: black !important;
    border: 1px solid #dee2e6;
    border-radius: 6px;
    padding: 10px;
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
    background-color: #343a40 !important; /* 검은색 헤더 */
    color: white !important;              /* 흰 글씨 */
    border-color: #454d55 !important;
}
.table td {
    background-color: #ffffff !important; /* 흰색 본문 */
    color: black !important;              /* 검은 글씨 */
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
#faqDetailModal .modal-content {
    background-color: #ffffff !important;
    color: black !important;
    border: 1px solid #dee2e6;
    border-radius: 10px;
}
#faqDetailModal .modal-header,
#faqDetailModal .modal-footer {
    background-color: #343a40 !important; /* 헤더/푸터 검은색 */
    color: white !important;
}
#faqDetailModal .modal-body {
    background-color: #ffffff !important; /* 본문 흰색 */
    color: black !important;
}

/* 입력창 */
.form-control {
    background-color: #ffffff !important;
    color: black !important;
    border: 1px solid #ced4da !important;
}

/* FAQ 카테고리 버튼 */
#faqCategoryGroup .btn {
    color: black !important;
    background-color: #f8f9fa !important;
    border-color: #dee2e6 !important;
}
#faqCategoryGroup .btn:hover {
    background-color: #e2e6ea !important;
}

/* Hover 시 */
#faqCategoryGroup .btn:hover {
    background-color: #999999 !important;     /* hover 시 더 밝은 회색 */
    color: white !important;
    border-color: #cccccc !important;
}

/* Active (선택된 버튼) */
#faqCategoryGroup .btn.active {
    background-color: #ffe066 !important;     /* 노란색 배경 */
    color: black !important;                  /* 글자 검정 */
    border-color: #e0b700 !important;
}






</style>
<section id="faqlistbody" >
<%-- 세션에서 loginUser 객체를 가져옵니다. --%>
<c:set var="loginUser" value="${sessionScope.loginUser}" />
<div class="container my-5 container-custom bg-white">

  <!-- 상단 바: 왼쪽 카테고리 / 오른쪽 검색+등록 -->
  <div class="row align-items-center mt-2 g-2">

    <!-- 왼쪽: 카테고리 버튼 그룹 -->
    <div class="col-12 col-lg-6">
      <div class="btn-group flex-wrap" role="group" id="faqCategoryGroup">
        <button class="btn btn-outline-secondary table-notice-hover" id="faqCatAll"
                onclick="filterFaqByCategory('', true)">
          <fmt:message key="faq.category.all"/>
        </button>
        <button class="btn btn-outline-secondary" id="faqCatOrder"
                onclick="filterFaqByCategory('주문', true)">
          <fmt:message key="faq.category.order"/>
        </button>
        <button class="btn btn-outline-secondary" id="faqCatDelivery"
                onclick="filterFaqByCategory('배송', true)">
          <fmt:message key="faq.category.delivery"/>
        </button>
        <button class="btn btn-outline-secondary" id="faqCatMember"
                onclick="filterFaqByCategory('회원', true)">
          <fmt:message key="faq.category.member"/>
        </button>
        <button class="btn btn-outline-secondary" id="faqCatProduct"
                onclick="filterFaqByCategory('상품', true)">
          <fmt:message key="faq.category.product"/>
        </button>
        <button class="btn btn-outline-secondary" id="faqCatPayment"
                onclick="filterFaqByCategory('주문/결제', true)">
          <fmt:message key="faq.category.payment"/>
        </button>
        <button class="btn btn-outline-secondary" id="faqCatRefund"
                onclick="filterFaqByCategory('교환/반품/취소', true)">
          <fmt:message key="faq.category.refund"/>
        </button>
        <button class="btn btn-outline-secondary" id="faqCatCoupon"
                onclick="filterFaqByCategory('쿠폰', true)">
          <fmt:message key="faq.category.coupon"/>
        </button>
      </div>
    </div>

    <!-- 오른쪽: 검색 + 등록 버튼 -->
    <div class="col-12 col-lg-6">
      <div class="d-flex justify-content-end align-items-center flex-wrap gap-2">
        <form class="d-flex" onsubmit="return searchFaq()">
          <input type="text" id="faqKeyword" class="form-control"
                 placeholder="<fmt:message key='faq.search.placeholder'/>"
                 value="${keyword}" style="min-width:220px;" />
          <button type="submit" class="btn btn-outline-secondary ms-2">
            <i class="fa fa-search" style="color:black"></i>
          </button>
        </form>

        <c:if test="${not empty loginUser and loginUser.accountType eq 'ADMIN'}">
          <button type="button" id="regBtn" class="btn btn-dark ms-2"
                  data-toggle="modal" data-target="#faqModal">
            + 새 FAQ 등록
          </button>
        </c:if>
      </div>
    </div>
    </div>
    </div>
  <!-- FAQ 목록 테이블 -->
  <table class="table table-bordered table-notice-hover mt-2">
  
    <thead class="thead-dark text-center">
		<tr>
		  <th id="faqNo"><fmt:message key="faq.no"/></th>
		  <th id="faqCategory"><fmt:message key="faq.category"/></th>
		  <th id="faqQuestion"><fmt:message key="faq.question"/></th>
		  <th id="faqDate"><fmt:message key="faq.date"/></th>
		  <%-- loginUser가 존재하고 accountType이 'ADMIN'일 경우에만 '관리' 헤더를 표시합니다. --%>
		    <th id="faqManage">관리</th>
		</tr>
</thead>
<tbody id="faqTableBody" data-is-admin="${not empty loginUser and loginUser.accountType eq 'ADMIN'}">
  <c:forEach var="f" items="${faqList}" varStatus="st">
    <tr id="faqRow${f.faqId}" onclick="openFaqModal(${f.faqId})" style="cursor:pointer;">
        <td class="text-center">
          ${totalCount - ((curPage - 1) * pageSize + st.index)}
        </td>      
        <td class="text-center">${f.category}</td>
      <td>${f.question}</td>
      <td class="text-center"><fmt:formatDate value="${f.createdAt}" pattern="yyyy-MM-dd"/></td>
      <%-- loginUser가 존재하고 accountType이 'ADMIN'일 경우에만 '삭제' 버튼이 있는 셀을 표시합니다. --%>
      <c:if test="${not empty loginUser and loginUser.accountType eq 'ADMIN'}">
        <td class="text-center">
          <form action="/faq/delete" method="post" class="d-inline" onsubmit="return confirm('정말 삭제하시겠습니까?');">
            <input type="hidden" name="faqId" value="${f.faqId}" />
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick="event.stopPropagation(); deleteFaq(${f.faqId});">
              삭제
            </button>
          </form>
        </td>
      </c:if>
    </tr>
  </c:forEach>
    <!-- 검색 결과 없을 시 표시 -->
    <c:if test="${empty faqList}">
      <tr>
        <td colspan="5" class="text-center text-muted">
          검색 결과가 없습니다.
        </td>
      </tr>
    </c:if>
</tbody>
  </table>
  
<!-- 페이지 네비게이션 (1페이지 뿐이면 전체 숨김) -->
<c:if test="${totalPage > 1}">
  <nav class="mt-3">
    <ul id="faqPagination" class="pagination justify-content-center">

      <!-- 처음 -->
      <c:if test="${curPage > 1}">
        <li class="page-item">
          <a class="page-link" href="#" data-page="1"><fmt:message key="처음"/></a>
        </li>
      </c:if>

      <!-- 이전 블록 -->
      <c:if test="${startPage > 1}">
        <li class="page-item">
          <a class="page-link" href="#" data-page="${startPage - 1}"><fmt:message key="이전"/></a>
        </li>
      </c:if>

      <!-- 페이지 번호 -->
      <c:forEach begin="${startPage}" end="${endPage}" var="p">
        <li class="page-item ${p == curPage ? 'active' : ''}">
          <a class="page-link" href="#" data-page="${p}">${p}</a>
        </li>
      </c:forEach>

      <!-- 다음 블록 -->
      <c:if test="${endPage < totalPage}">
        <li class="page-item">
          <a class="page-link" href="#" data-page="${endPage + 1}"><fmt:message key="다음"/></a>
        </li>
      </c:if>

      <!-- 마지막 -->
      <c:if test="${curPage < totalPage}">
        <li class="page-item">
          <a class="page-link" href="#" data-page="${totalPage}"><fmt:message key="마지막"/></a>
        </li>
      </c:if>

    </ul>
  </nav>
</c:if>
<div class="text-center text-secondary" style="margin-bottom:10px;">
   <span id="span01"> 총 <strong>${totalPage}</strong> 페이지</span>
</div>

<!-- FAQ 수정 모달 -->
<c:forEach var="f" items="${faqList}">
  <div class="modal fade"
       id="faqModal${f.faqId}"
       tabindex="-1"
       role="dialog"
       aria-labelledby="faqModalLabel${f.faqId}"
       aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content bg-light border-dark">
        <form id="updateFaqForm${f.faqId}">
          <input type="hidden" name="faqId" value="${f.faqId}" />

          <div class="modal-body">
            <div class="form-group">
              <label for="faq-category-${f.faqId}">카테고리</label>
              <select id="faq-category-${f.faqId}" name="category" class="form-control" required>
                <option value="주문" <c:if test="${f.category eq '주문'}">selected</c:if>>주문</option>
                <option value="배송" <c:if test="${f.category eq '배송'}">selected</c:if>>배송</option>
                <option value="회원" <c:if test="${f.category eq '회원'}">selected</c:if>>회원</option>
                <option value="상품" <c:if test="${f.category eq '상품'}">selected</c:if>>상품</option>
                <option value="주문/결제" <c:if test="${f.category eq '주문/결제'}">selected</c:if>>주문/결제</option>
                <option value="교환/반품/취소" <c:if test="${f.category eq '교환/반품/취소'}">selected</c:if>>교환/반품/취소</option>
                <option value="쿠폰" <c:if test="${f.category eq '쿠폰'}">selected</c:if>>쿠폰</option>
              </select>
            </div>

            <div class="form-group">
              <label for="faq-question-${f.faqId}">질문</label>
              <input id="faq-question-${f.faqId}" type="text" name="question"
                     class="form-control" value="${f.question}" required />
            </div>

            <div class="form-group">
              <label for="faq-answer-${f.faqId}">답변</label>
              <textarea id="faq-answer-${f.faqId}" name="answer" rows="5"
                        class="form-control" required>${f.answer}</textarea>
            </div>

            <p class="text-right text-muted">
              <fmt:formatDate value="${f.createdAt}" pattern="yyyy-MM-dd"/>
            </p>
          </div>

          <div class="modal-footer">
            <c:if test="${not empty loginUser and loginUser.accountType eq 'ADMIN'}">
              <button type="button" class="btn btn-dark" onclick="updateFaq(${f.faqId})">수정</button>
            </c:if>
          </div>
        </form>
      </div>
    </div>
  </div>
</c:forEach>

  <!-- FAQ 등록 모달 -->
  <div class="modal fade" id="faqModal" tabindex="-1" role="dialog" aria-labelledby="faqModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content bg-light border-dark">
        <form method="post" id="faqForm" action="${pageContext.request.contextPath}/faq">
          <div class="modal-header bg-dark text-white">
            <h5 class="modal-title">새 FAQ 등록</h5>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>카테고리</label>
              <select name="category" class="form-control" required>
                <option value="">-- 카테고리 선택 --</option>
                <option value="주문">주문</option>
                <option value="배송">배송</option>
                <option value="회원">회원</option>
                <option value="상품">상품</option>
                <option value="주문/결제">주문/결제</option>
                <option value="교환/반품/취소">교환/반품/취소</option>
                <option value="쿠폰">쿠폰</option>
              </select>
            </div>
            <div class="form-group">
              <label>질문</label>
              <input type="text" name="question" class="form-control" required />
            </div>
            <div class="form-group">
              <label>답변</label>
              <textarea name="answer" rows="5" class="form-control" required></textarea>
            </div>
          </div>
          <div class="modal-footer">
  			<button type="button" class="btn btn-dark" onclick="submitFaq()">등록</button>
            <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
          </div>
        </form>
      </div>
    </div>
  </div>

</section>
