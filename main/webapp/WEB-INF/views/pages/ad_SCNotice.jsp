<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
function openNoticeModal(noticeId){ 
$("#newNoticeModal"+noticeId).modal("show"); 
}
$(document).on('show.bs.modal', '#newNoticeModal', function () {
	  $(this).appendTo('body');   // 모달을 항상 body로 이동
	  resetNewNoticeForm();       // 열릴 때마다 폼 초기화
	});
$(document).on('keydown keypress', '#editNoticeModals input[id^="editTitle"]', function (e) {
	  // 한글 입력 중(IME 조합)일 때는 방해하지 않음
	  if (e.isComposing || e.keyCode === 229) return;

	  if (e.key === 'Enter' || e.keyCode === 13) {
	    e.preventDefault();
	    // 선택: 엔터 누르면 내용 textarea로 포커스 이동
	    const suffix = this.id.replace('editTitle', '');
	    const next = document.getElementById('editContent' + suffix);
	    if (next) next.focus();
	    return false;
	  }
	});
//새 HTML 조각에서 totalPage를 뽑는 함수 (메타 우선, 없으면 링크로 계산)
function _extractTotalPage($ctx) {
  const $meta = $ctx.find('#paginationMeta');
  if ($meta.length) {
    const v = parseInt($meta.data('total-page'), 10);
    if (!isNaN(v) && v > 0) return v;
  }
  // fallback: 페이지네이션 링크의 data-page 중 최대값
  const nums = $ctx.find('#noticePagination a[data-page]').map(function(){ 
    return parseInt(this.dataset.page, 10);
  }).get().filter(n => !isNaN(n));
  return nums.length ? Math.max.apply(null, nums) : 1;
}

//✅ 목록 조각 스왑 (tbody + pagination + 총 페이지 + 편집 모달까지 교체)
function _swapNotice(html) {
  const $html = $(html);

  // 1) tbody 교체
  const $newTbody = $html.find('#noticeTableBody');
  if ($newTbody.length) {
    $('#noticeTableBody').html($newTbody.html());
  }

  // 2) 페이지네이션 교체
  const $newPagi = $html.find('#noticePagination');
  if ($newPagi.length) {
    $('#noticePagination').html($newPagi.html());
  }

  // 3) 총/현재 페이지 메타 갱신
  const $meta = $html.find('#paginationMeta');
  if ($meta.length) {
    const total = parseInt($meta.data('total-page'), 10) || 1;
    $('#span01 strong').text(total);
    $('#noticePaginationWrap').toggleClass('d-none', total <= 1);
  } else {
    const total = _extractTotalPage($html);
    $('#span01 strong').text(total);
    $('#noticePaginationWrap').toggleClass('d-none', total <= 1);
  }

  // 4) (관리자) 편집 모달 덩어리도 조각에 있으면 교체
  const $newEditWrap = $html.find('#editNoticeModals');
  if ($newEditWrap.length) {
    const $wrap = $('#editNoticeModals');
    if ($wrap.length) $wrap.replaceWith($newEditWrap);
    else $('body').append($newEditWrap);
  }

  // 5) 페이지 전환 시 포커스/스크롤 UX
  window.scrollTo({ top: 0, behavior: 'smooth' });

  // 6) 새로 들어온 요소에 대한 초기화 훅
  if (typeof initNoticeEditModals === 'function') {
    initNoticeEditModals();
  }
}
// 예) 새 공지 등록 성공 콜백에서 목록 다시 로드 후 교체
function submitNewNotice() {
  $.post('/notice/create', $('#newNoticeForm').serialize())
   .done(function(){
      // 보통 첫 페이지로 다시 조회
      $.get('/notice/list?page=1', function(fragmentHtml){
        _swapNotice(fragmentHtml);
      });
   });
}

// 페이지네이션 클릭(위임) → 해당 페이지 로드 후 교체
$(document).on('click', '#noticePagination a[data-page]', function(e){
  e.preventDefault();
  const page = this.dataset.page || 1;
  $.get('/notice/list?page=' + page, function(fragmentHtml){
    _swapNotice(fragmentHtml);
  });
});

function initAfterAdminPageLoad(url) {
	  try {
	    // ✅ 공지 영역 존재 판단: /support/noticeA 또는 DOM 존재
	    const hasNoticeDOM =
	      (url && url.includes('/support/noticeA')) ||           // ← 경로 수정
	      document.getElementById('noticeTableBody') ||
	      document.getElementById('editNoticeModals');

	    if (hasNoticeDOM) {
	      const $wrap = $('#editNoticeModals');
	      if ($wrap.length && !$wrap.parent().is('body')) {
	        $('body').append($wrap); // 선택: 모달 래퍼를 body 바로 아래로
	      }
	      if (typeof initNoticeEditModals === 'function') {
	        initNoticeEditModals();
	      }
	    }

	    // 공지 목록 스왑 후 재초기화 훅(1회 설치)
	    if (typeof _swapNotice === 'function' && !window.__noticeSwapHooked) {
	      window.__noticeSwapHooked = true;
	      const origSwap = _swapNotice;
	      window._swapNotice = function(html) {
	        origSwap(html);
	        if (typeof initNoticeEditModals === 'function') initNoticeEditModals();
	      };
	    }
	  } catch (e) {
	    console.error('initAfterAdminPageLoad error:', e);
	  }
	}
	
function openEditNoticeModal(id, e){
	  if (e) {
	    e.preventDefault();
	    e.stopPropagation();
	    if (e.stopImmediatePropagation) e.stopImmediatePropagation();
	  }
	  var $m = $("#editNoticeModal" + id);
	  if ($m.length) {
	    // 백드롭/z-index 이슈 예방: body로 이동
	    if (!$m.parent().is('body')) $('body').append($m);
	    showModalById('editNoticeModal' + id);   // ★ 여기만 변경
	  }
	  return false;
	}
function showModalById(id) {
	  var el = document.getElementById(id);
	  if (!el) return false;

	  // BS5: vanilla API
	  if (window.bootstrap && bootstrap.Modal) {
	    bootstrap.Modal.getOrCreateInstance(el).show();
	    return true;
	  }
	  // BS4: jQuery plugin
	  if (window.jQuery && jQuery.fn && jQuery.fn.modal) {
	    jQuery(el).modal('show');
	    return true;
	  }

	  console.error('Bootstrap Modal API not found (BS4/BS5 둘 다 감지 실패)');
	  return false;
	}

	function hideModalById(id) {
	  var el = document.getElementById(id);
	  if (!el) return false;

	  if (window.bootstrap && bootstrap.Modal) {
	    var inst = bootstrap.Modal.getOrCreateInstance(el);
	    inst.hide();
	    return true;
	  }
	  if (window.jQuery && jQuery.fn && jQuery.fn.modal) {
	    jQuery(el).modal('hide');
	    return true;
	  }
	  console.error('Bootstrap Modal API not found (hide)');
	  return false;
	}
</script>
<style>
#span01 {
  color: black !important;
}
/* 전체 section 배경 */
#noticelistbody {
    background-color: #ffffff !important; /* 흰색 배경 */
    color: black !important;              /* 검은 글씨 */
    border-radius: 8px;
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
    background-color: #343a40 !important; /* 헤더 검정색 */
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
    background-color: #343a40 !important; /* 검정색 헤더/푸터 */
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

<section id="noticelistbody">

<%-- 세션에서 loginUser 객체를 가져옵니다. --%>
<c:set var="loginUser" value="${sessionScope.loginUser}" />
  <div class="container my-5 container-custom bg-white">



    <!-- 검색 폼 (GET) -->
    <div class="row mt-2 pt-2" style="display: flex; justify-content: flex-end;">
	    
			
	        <div class="row">
	            <form class="form-inline d-flex" onsubmit="return searchNotice()">
	             <label for="noticeKeyword" class="sr-only">공지 검색어</label>
				<input type="text" name="keyword" id="noticeKeyword" 
				       class="form-control mr-2" style="max-width:500px;" 
				       placeholder="<fmt:message key='notice.search.placeholder'/>">
	       			  <button type="submit" class="btn btn-outline-secondary">
				    <i class="fa fa-search"></i>
				  </button>
				</form>
				
				<%-- loginUser가 존재하고 accountType이 'ADMIN'일 경우에만 버튼을 표시합니다. --%>
					<button type="button" id="regNoticeBtn" class="btn btn-dark" style="margin-left: 18px; margin-right: 24px;" data-toggle="modal" data-target="#newNoticeModal">
					  + 새 공지 등록
					</button>
	        </div>

		 
		    
		  
	</div>
<div id="noticeTableWrap"
     data-list-url="<c:url value='/support/noticeA'/>">  <!-- ✅ 관리자용 목록 URL -->
 <table id="noticeTable" class="table table-bordered table-notice-hover mt-2">
  <thead class="thead-dark text-center">
	 <tr>
  <th style="width:10%"><fmt:message key="notice.no"/></th>
  <th style="width:60%"><fmt:message key="notice.title"/></th>
  <th style="width:15%"><fmt:message key="notice.date"/></th>  
  <th style="width:15%">관리</th>
	</tr>
  </thead>
  <tbody id="noticeTableBody" data-is-admin="true">
    <c:forEach var="nt" items="${noticeList}" varStatus="st">
      <tr id="noticeRow${nt.noticeId}"
      	  style="cursor:pointer;"
          data-toggle="modal"
          data-target="#noticeModal"
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
          <td class="text-center">
				<button type="button"
				        class="btn btn-sm btn-outline-secondary js-no-detail"
				        onclick="return openEditNoticeModal(${nt.noticeId}, event);">
				  수정
				</button>
            <button class="btn btn-sm btn-outline-danger"
                    onclick="event.stopPropagation(); deleteNotice(${nt.noticeId});">
              삭제
            </button>
          </td>
      </tr>
    </c:forEach>
    <c:if test="${empty noticeList}">
      <tr>
        <%-- colspan 값 조정: 관리 열이 추가될 경우 4, 없을 경우 3 --%>
        <td colspan="<c:choose><c:when test="${not empty loginUser and loginUser.accountType eq 'ADMIN'}">4</c:when><c:otherwise>3</c:otherwise></c:choose>" class="text-center text-muted">등록된 공지사항이 없습니다.</td>
      </tr>
    </c:if>
  </tbody>
</table>
</div>

<!-- 페이지 네비게이션 (1페이지 뿐이면 전체 숨김) -->
<!-- 항상 존재하는 래퍼 -->
<div id="noticePaginationWrap" class="${totalPage <= 1 ? 'd-none' : ''}">
  <ul id="noticePagination" class="pagination justify-content-center">
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

    <!-- 번호 -->
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
</div>
    <div class="text-center text-secondary" style="margin-bottom:10px;">
   <span id="span01"> 총 <strong>${totalPage}</strong> 페이지</span>
</div>
<!-- 목록 조각의 아무 곳이나: 총/현재 페이지 메타 -->
<div id="paginationMeta"
     data-total-page="${totalPage}"
     data-cur-page="${curPage}"
     style="display:none"></div>





<!-- 상세보기 모달 -->
<div class="modal fade" id="noticeModal" tabindex="-1" role="dialog">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header bg-dark text-white">
        <h5 class="modal-title" id="noticeModalTitle"></h5>
      </div>
      <div class="modal-body">
        <div id="noticeModalContent" class="mb-3"></div>
      </div>
      <div class="modal-footer text-muted" id="noticeModalInfo"></div>
    </div> <!-- /modal-content -->
  </div>   <!-- /modal-dialog -->
</div>     <!-- /modal -->  



 <!-- 2) 새 공지 등록 모달 -->
<div class="modal fade" id="newNoticeModal" tabindex="-1" role="dialog" aria-labelledby="newNoticeModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content bg-light border-dark">
      <form id="noticeForm" action="${pageContext.request.contextPath}/support/notice/insert" method="post">
        <div class="modal-header bg-dark text-white">
          <h5 class="modal-title" id="newNoticeModalLabel">새 공지 등록</h5>
          <!-- 닫기 버튼: BS4 -->
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group mb-3">
 		 <label for="notice-title-new">제목</label>
		  <input id="notice-title-new" type="text" name="title" class="form-control" required/>
          </div>
          <div class="form-group mb-3">
		  <label for="notice-content-new">내용</label>
		  <textarea id="notice-content-new" name="content" rows="6" class="form-control" required></textarea>
          </div>
          <div class="form-check form-check-inline">
            <input type="hidden" name="isPopup" value="N"/>
		 <input id="notice-popup-new" class="form-check-input" type="checkbox" name="isPopup" value="Y"/>
 		<label class="form-check-label" for="notice-popup-new">팝업</label>

            <input type="hidden" name="isPinned" value="N"/>
 		<input id="notice-pinned-new" class="form-check-input" type="checkbox" name="isPinned" value="Y"/>
 		<label class="form-check-label" for="notice-pinned-new">고정</label>
          </div>
        </div>
		<div class="modal-footer">
		  <button type="button" class="btn btn-dark" onclick="submitNotice()">등록</button>
		</div>
      </form>
    </div>
  </div>
</div>
    
  
<!-- 3) 공지 수정 모달 -->
<div id="editNoticeModals"><!-- ✅ 추가: 모달 래퍼 -->
  <c:forEach var="nt" items="${noticeList}">
    <div class="modal fade" id="editNoticeModal${nt.noticeId}" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content bg-light border-dark">
          <form id="editNoticeForm${nt.noticeId}">
            <input type="hidden" name="noticeId" value="${nt.noticeId}"/>
            <div class="modal-header bg-dark text-white">
              <h5 class="modal-title">공지 수정</h5>
            </div>
            <div class="modal-body">
  <div class="form-group mb-3">
    <label for="editTitle${nt.noticeId}">제목</label>
    <input id="editTitle${nt.noticeId}" type="text" name="title"
           class="form-control" value="${nt.title}" required/>
  </div>

  <div class="form-group mb-3">
    <label for="editContent${nt.noticeId}">내용</label>
    <textarea id="editContent${nt.noticeId}" name="content" rows="6"
              class="form-control" required>${nt.content}</textarea>
  </div>

  <!-- 체크박스: 미체크 시에도 N 전송되도록 hidden 기본값 추가 -->
  <div class="form-check form-check-inline">
    <input type="hidden" name="isPopup" value="N"/>
    <input class="form-check-input" type="checkbox" name="isPopup"
           id="editIsPopup${nt.noticeId}" value="Y"
           <c:if test="${nt.isPopup=='Y'}">checked</c:if> />
    <label class="form-check-label" for="editIsPopup${nt.noticeId}">팝업</label>
  </div>

  <div class="form-check form-check-inline">
    <input type="hidden" name="isPinned" value="N"/>
    <input class="form-check-input" type="checkbox" name="isPinned"
           id="editIsPinned${nt.noticeId}" value="Y"
           <c:if test="${nt.isPinned=='Y'}">checked</c:if> />
    <label class="form-check-label" for="editIsPinned${nt.noticeId}">고정</label>
  </div>
</div>
		<div class="modal-footer">
		  <button type="button" class="btn btn-dark"
		          onclick="event.stopPropagation(); updateNotice(${nt.noticeId});">
		    수정
		  </button>
		</div>
        </form>
      </div>
    </div>
  </div>
</c:forEach>
  </div>
  
</section>