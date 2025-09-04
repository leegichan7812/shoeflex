<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>



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
/* 전체 section 배경 */
#noticelistbody {
    background-color: #ffffff !important; /* 흰색 배경 */
    color: black !important;              /* 검은 글씨 */
    padding: 20px;
    border-radius: 8px;
}

</style>


<section id="noticelistbody">
  <div class="container my-5 container-custom bg-white">

    <!-- 상단 타이틀/탭 -->
    <div class="container-fluid mt-3">
      <h3 class="mb-4"><i class="fas fa-bullhorn"></i>&nbsp;
        <span id="noticePageTitle"><fmt:message key="notice.title"/></span>
      </h3>
      <ul class="nav nav-tabs mb-4" role="tablist">
        <li class="nav-item">
          <a id="faqTab" class="nav-link h6" href="#" onclick="loadPage('/faq')">
            <fmt:message key="notice.tab.faq"/>
          </a>
        </li>
        <li class="nav-item">
          <a id="qnaTab" class="nav-link h6" href="#" onclick="ChatWidget.toggle(); return false;">
            <fmt:message key="notice.tab.qna"/>
          </a>
        </li>
        <li class="nav-item">
          <a id="noticeTab" class="nav-link h6 active" href="#" onclick="loadPage('/support/notice')">
            <fmt:message key="notice.tab.notice"/>
          </a>
        </li>
      </ul>
    </div>
    <!-- ↑ 여기서 컨테이너를 닫지 않습니다. (기존에는 </div></div> 로 과도하게 닫힘) -->

    <!-- 검색 폼 -->
    <div class="container faq-search-container mb-3">
      <div class="row align-items-center">
        <div id="noticeSearchLabel2" class="col-md-2 font-weight-bold">
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

    <!-- 공지 목록 테이블 -->
    <table class="table table-bordered table-notice-hover">
      <thead class="thead-dark text-center">
        <tr>
          <th style="width:10%"><fmt:message key="notice.no"/></th>
          <th style="width:60%"><fmt:message key="notice.title"/></th>
          <th style="width:15%"><fmt:message key="notice.date"/></th>
        </tr>
      </thead>
      <tbody id="noticeTableBody">
        <c:forEach var="nt" items="${noticeList}" varStatus="st">
          <tr id="noticeRow${nt.noticeId}" style="cursor:pointer;"
              data-toggle="modal" data-target="#noticeModal"
              onclick="loadNoticeDetail(${nt.noticeId})">
	        <td class="text-center noticePin">
	          <c:choose>
	            <c:when test="${nt.isPinned=='Y'}">
	              <span class="pinText"><fmt:message key="notice.pin"/></span>
	              <sup class="text-danger font-weight-bold">〃</sup>
	            </c:when>
	            <c:otherwise>
	              ${totalCount - ((curPage - 1) * pageSize + st.index)}
	            </c:otherwise>
	          </c:choose>
	        </td>
            <td class="text-left font-weight-bold">${nt.title}</td>
            <td class="text-center">
              <fmt:formatDate value="${nt.createdAt}" pattern="yyyy.MM.dd"/>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty noticeList}">
          <tr>
            <td colspan="3" class="text-center text-muted">등록된 공지사항이 없습니다.</td>
          </tr>
        </c:if>
      </tbody>
    </table>

<!-- 페이지 네비게이션 + 총 페이지 표기 (totalPage > 1 일 때만 노출) -->
<c:if test="${totalPage > 1}">
  <div id="noticePaginationWrap">
    <nav class="mt-3">
      <ul id="noticePagination" class="pagination justify-content-center">

        <!-- 처음 -->
        <li class="page-item ${curPage == 1 ? 'disabled' : ''}">
          <a class="page-link" href="#" data-page="1"><fmt:message key="처음"/></a>
        </li>
        <!-- 번호 -->
        <c:forEach begin="${startPage}" end="${endPage}" var="p">
          <li class="page-item ${p == curPage ? 'active' : ''}">
            <a class="page-link" href="#" data-page="${p}">${p}</a>
          </li>
        </c:forEach>
        <!-- 마지막 -->
        <li class="page-item ${curPage >= totalPage ? 'disabled' : ''}">
          <a class="page-link" href="#" data-page="${totalPage}"><fmt:message key="마지막"/></a>
        </li>

      </ul>
    </nav>
  </div>

  <!-- 총 페이지 문구도 블럭 있을 때만 노출 -->
  <div id="totalPageInfo" class="text-center text-secondary mb-2">
    총 <strong>${totalPage}</strong> 페이지
  </div>
</c:if>

    <!-- 상세보기 모달 -->
    <div class="modal fade" id="noticeModal" tabindex="-1" role="dialog" aria-hidden="true">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header bg-dark text-white">
            <h5 class="modal-title" id="noticeModalTitle"></h5>

          </div>
          <div class="modal-body">
            <div id="noticeModalContent" class="mb-3"></div>
          </div>
          <div class="modal-footer text-muted" id="noticeModalInfo"></div>
        </div> <!-- /.modal-content -->
      </div>   <!-- /.modal-dialog -->
    </div>     <!-- /.modal -->

  </div> <!-- /.container -->
</section>

