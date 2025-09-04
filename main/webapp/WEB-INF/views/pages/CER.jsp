<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<style>
#filter{
color: #fff !important;
}
/* 네비게이션 탭 */
.nav-tabs .nav-link { background-color: #f8f9fa !important; color: black !important; border: 1px solid #dee2e6 !important; }
.nav-tabs .nav-link.active { background-color: #343a40 !important; color: white !important; border-color: #343a40 !important; }

/* 테이블 */
.table { color: black !important; background-color: #ffffff !important; }
.table th { background-color: #343a40 !important; color: white !important; border-color: #454d55 !important; }
.table td { background-color: #ffffff !important; color: black !important; border-color: #dee2e6 !important; }

/* 버튼 */
.btn-dark { background-color: #343a40 !important; color: white !important; border: none !important; }
.btn-outline-secondary { color: black !important; border-color: #6c757d !important; }
.btn-outline-secondary:hover { background-color: #6c757d !important; color: white !important; }
.btn-outline-danger { color: #dc3545 !important; border-color: #dc3545 !important; }
.btn-outline-danger:hover { background-color: #dc3545 !important; color: white !important; }

/* 모달 */
.modal-content { background-color: #ffffff !important; color: black !important; border-radius: 8px; }
.modal-header { background-color: #343a40 !important; color: white !important; }
.modal-body { background-color: #ffffff !important; color: black !important; }

/* 입력창(라이트) */
.form-control {
  background-color: #ffffff !important;
  color: black !important;
  border: 1px solid #ced4da !important;
  min-height: 40px;
  padding: 0.5rem 0.75rem;
  font-size: 1rem;
}

/* 다크 인풋(페이지 테마에 맞춘 경우 유지가 필요하면 아래 유지) */
input.form-control, textarea.form-control, select.form-control {
  background-color: #212529;
  color: #fff;
  border: 1px solid #444;
}
input.form-control:focus, textarea.form-control:focus, select.form-control:focus {
  background-color: #212529;
  color: #fff;
  border-color: #444;
  box-shadow: none;
}
input.form-control[readonly], textarea.form-control[readonly] {
  background-color: #212529;
  color: #fff;
  opacity: 1;
}
input.form-control:disabled, textarea.form-control:disabled, select.form-control:disabled {
  background-color: #212529;
  color: #fff;
  opacity: 1;
}
.btn-gray-light {
  background-color: #e9ecef;   /* 밝은 회색 */
  border: 1px solid #ced4da;
  color: #000;
}
.btn-gray-light:hover { background-color: #dee2e6; }
.btn-gray-light:active,
.btn-gray-light:focus  { background-color: #ced4da; }

/* 라벨 색상 (상단 필터는 검정, 모달은 기본 색상) */
.form-label { color: #000 !important; }
</style>

<section class="pb-5 pt-4">
  <div class="container">

    <!-- ========== 필터 바 ========== -->
   <form id="asrFilterForm" class="row g-2 align-items-end mb-3 d-flex justify-content-between">

  <div class="d-flex flex-wrap align-items-end col-md-8">
    <div class="me-3">
      <label class="form-label mb-1" for="asr-type" id="filter">요청유형</label>
      <select id="asr-type" name="type" class="form-control form-control-sm">
        <option value="">전체</option>
        <option value="취소"  ${type == '취소'  ? 'selected' : ''}>취소</option>
        <option value="반품"  ${type == '반품'  ? 'selected' : ''}>반품</option>
        <option value="교환"  ${type == '교환'  ? 'selected' : ''}>교환</option>
      </select>
    </div>

    <div class="me-3">
      <label class="form-label mb-1" for="asr-status" id="filter">진행상태</label>
      <select id="asr-status" name="status" class="form-control form-control-sm">
        <option value="">전체</option>
        <option value="신청"  ${status == '신청'  ? 'selected' : ''}>신청</option>
        <option value="승인"  ${status == '승인'  ? 'selected' : ''}>승인</option>
        <option value="거절"  ${status == '거절'  ? 'selected' : ''}>거절</option>
        <option value="완료"  ${status == '완료'  ? 'selected' : ''}>완료</option>
      </select>
    </div>

    <div class="me-3" style="margin:0px 4px; ">
      <button type="submit" class="btn-gray-light px-4 py-2">검색</button>
    </div>
  </div>

  <!-- 관리자 전용: 숨겨진 데이터 보기 탭 -->
  <c:if test="${not empty loginUser and loginUser.accountType eq 'ADMIN'}">
    <div class="d-flex align-items-end col-md-4 justify-content-end">
      <ul class="nav nav-pills mb-0" id="asrHiddenTabs">
        <li class="nav-item"><a href="#" class="nav-link ${empty view || view=='active' ? 'active' : ''}" data-view="active">최근</a></li>
        <li class="nav-item"><a href="#" class="nav-link ${view=='deleted' ? 'active' : ''}" data-view="deleted">6개월이후</a></li>
        <li class="nav-item"><a href="#" class="nav-link ${view=='all' ? 'active' : ''}" data-view="all">전체</a></li>
      </ul>
      <input type="hidden" name="view" id="asrViewInput" value="${empty view ? 'active' : view}">
    </div>
  </c:if>
</form>

    <!-- ========== 헤더 ========== -->
    <div class="modal-header">
      <h5 class="modal-title" id="asrPageTitle">교환·반품·취소 요청 답변</h5>
      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
    </div>

    <!-- ========== 테이블 ========== -->
    <div class="table-responsive">
      <table class="table table-bordered table-hover mt-2">
        <thead class="thead-dark text-center">
          <tr>
            <th style="width:6%">요청번호</th>
            <th style="width:8%">사용자번호</th>
            <th style="width:8%">요청유형</th>
            <th style="width:8%">진행상태</th>
            <th style="width:10%">사유코드</th>
            <th style="width:15%">상세사유</th>
            <th style="width:12%">수거주소</th>
            <th style="width:8%">상품옵션번호</th>
            <th style="width:8%">주문항목번호</th>
            <th style="width:8%">처리자(ID)</th>
            <th style="width:10%">요청일자</th>
            <th style="width:10%">승인일자</th>
            <th style="width:10%">완료일자</th>
          </tr>
        </thead>
        <tbody id="asrTableBody">
          <c:forEach var="it" items="${list}">
            <tr id="asrRow${it.reqId}"
                style="cursor:pointer;"
                data-bs-toggle="modal"
                data-bs-target="#asrModal"
                onclick="openAsrModal(${it.reqId})">
              <td class="text-center">${it.reqId}</td>
              <td class="text-center">${it.userId}</td>
              <td class="text-center">${empty it.reqType    ? '-' : it.reqType}</td>
			  <td class="text-center">
				 ${it.reqStatus == '완료' ? it.reqType : it.reqStatus}${it.reqStatus == '완료' ? '완료' : ''}
			  </td>
              <td class="text-center">${empty it.reasonCode ? '-' : it.reasonCode}</td>
              <td class="text-left" title="${it.reasonDetail}">
                <c:choose>
                  <c:when test="${not empty it.reasonDetail and fn:length(it.reasonDetail) > 50}">
                    ${fn:substring(it.reasonDetail, 0, 50)}…
                  </c:when>
                  <c:when test="${not empty it.reasonDetail}">
                    ${it.reasonDetail}
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </td>
              <td class="text-left">${empty it.pickupAddress ? '-' : it.pickupAddress}</td>
              <td class="text-center">${it.productColorSizeId}</td>
              <td class="text-center">${it.orderItemId}</td>
              <td class="text-center">${empty it.processedBy ? '-' : it.processedBy}</td>
              <td class="text-center">
                <c:choose>
                  <c:when test="${not empty it.createdAt}">
                    <fmt:formatDate value="${it.createdAt}" pattern="yyyy.MM.dd HH:mm"/>
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </td>
              <td class="text-center">
                <c:choose>
                  <c:when test="${not empty it.approvedAt}">
                    <fmt:formatDate value="${it.approvedAt}" pattern="yyyy.MM.dd HH:mm"/>
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </td>
              <td class="text-center">
                <c:choose>
                  <c:when test="${not empty it.completedAt}">
                    <fmt:formatDate value="${it.completedAt}" pattern="yyyy.MM.dd HH:mm"/>
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty list}">
            <tr>
              <td colspan="13" class="text-center text-muted">데이터가 없습니다.</td>
            </tr>
          </c:if>
        </tbody>
      </table>
    </div>

    <!-- 페이지 네비게이션 -->
    <c:if test="${totalPages > 1}">
      <nav class="mt-3">
        <ul id="asrPagination" class="pagination pagination-sm justify-content-center">
          <li class="page-item ${page <= 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${page - 1}">이전</a>
          </li>
          <c:forEach var="p" begin="1" end="${totalPages}">
            <li class="page-item ${p == page ? 'active' : ''}">
              <a class="page-link" href="#" data-page="${p}">${p}</a>
            </li>
          </c:forEach>
          <li class="page-item ${page >= totalPages ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${page + 1}">다음</a>
          </li>
        </ul>
      </nav>
    </c:if>

  </div> <!-- /.container -->

  <!-- AfterSalesRequest 수정 모달  -->
  <div class="modal fade" id="asrModal" tabindex="-1" role="dialog" aria-labelledby="asrModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content">
        <form id="asrUpdateForm">
          <div class="modal-header">
            <h5 class="modal-title" id="asrModalLabel">교환·반품·취소 요청 답변</h5>
          </div>

          <div class="modal-body">
            <!-- 기본키 -->
            <input type="hidden" name="reqId" id="asr-reqId">

            <div class="row g-2">
              <div class="col-md-3">
                <label class="form-label" for="asr-userId">사용자번호</label>
                <input type="number" class="form-control form-control-sm" name="userId" id="asr-userId" required readonly>
              </div>

			<div class="col-md-3">
			  <label class="form-label" for="asr-reqType">요청유형</label>
			  <input type="text"
			         class="form-control form-control-sm"
			         name="reqType"
			         id="asr-reqType"
			         readonly>
			</div>

              <div class="col-md-3">
                <label class="form-label" for="asr-reqStatus">진행상태</label>
                <select class="form-control form-control-sm" name="reqStatus" id="asr-reqStatus" required>
                  <option value="">선택</option>
                  <option value="신청">신청</option>
                  <option value="승인">승인</option>
                  <option value="거절">거절</option>
                  <option value="완료">완료</option>
                </select>
              </div>

              <div class="col-md-3">
                <label class="form-label" for="asr-processedBy">처리자(ID)</label>
                <input type="number" class="form-control form-control-sm" name="processedBy" id="asr-processedBy" value="${loginUser.userId}" readonly>
              </div>

              <div class="col-md-12">
                <label class="form-label" for="asr-rejectReason">거절사유</label>
                <textarea class="form-control form-control-sm" rows="2" name="rejectReason" id="asr-rejectReason" placeholder="거절 시 사유를 입력하세요"></textarea>
              </div>

              <div class="col-md-3">
                <label class="form-label" for="asr-pcsId">상품옵션번호</label>
                <input type="number" class="form-control form-control-sm" name="productColorSizeId" id="asr-pcsId" required readonly>
              </div>

              <div class="col-md-3">
                <label class="form-label" for="asr-orderItemId">주문항목번호</label>
                <input type="number" class="form-control form-control-sm" name="orderItemId" id="asr-orderItemId" required readonly>
              </div>

              <div class="col-md-3">
                <label class="form-label" for="asr-reasonCode">사유코드</label>
                <input type="text" class="form-control form-control-sm" name="reasonCode" id="asr-reasonCode" placeholder="단순변심/불량/오배송 등" readonly>
              </div>

              <div class="col-md-12">
                <label class="form-label" for="asr-reasonDetail">상세사유</label>
                <textarea class="form-control form-control-sm" rows="3" name="reasonDetail" id="asr-reasonDetail" readonly></textarea>
              </div>

              <div class="col-md-12">
                <label class="form-label" for="asr-pickupAddress">수거주소</label>
                <input type="text" class="form-control form-control-sm" name="pickupAddress" id="asr-pickupAddress" readonly>
              </div>

              <div class="col-md-4">
                <label class="form-label" for="asr-createdAt">요청일자</label>
                <input type="datetime-local" class="form-control form-control-sm" id="asr-createdAt" readonly>
              </div>

              <div class="col-md-4">
                <label class="form-label" for="asr-approvedAt">승인일자</label>
                <input type="datetime-local" class="form-control form-control-sm" name="approvedAt" id="asr-approvedAt" >
              </div>

              <div class="col-md-4">
                <label class="form-label" for="asr-completedAt">완료일자</label>
                <input type="datetime-local" class="form-control form-control-sm" name="completedAt" id="asr-completedAt">
              </div>
            </div>
          </div>

          <div class="modal-footer">
            <button type="button" class="btn btn-dark btn" onclick="submitAsrUpdate()">요청처리</button>
            <button type="button" class="btn btn-secondary btn" data-bs-dismiss="modal">닫기</button>
          </div>
        </form>
      </div>
    </div>
  </div>
<div id="asrLoading"><div class="spinner"></div></div>
</section>

<script>
// -----------------------------
// 경량 목록 로더
// -----------------------------
// ============ ASR 공용 유틸 (중복 선언 방지) ============
window.ASR = window.ASR || {};                  // 네임스페이스
ASR.inFlight     = ASR.inFlight     || false;    // 중복 선언 방지
ASR.listUrl      = ASR.listUrl      || function(){ return '/after-sales/list'; };
ASR.showLoading  = ASR.showLoading  || function(show){
  var el = document.getElementById('asrLoading');
  if (el) el.style.display = show ? 'flex' : 'none';
};

// 부분교체: tbody + pagination
function _swapAsrList(html) {
  const $html = $(html);

  const $newTbody = $html.find('#asrTableBody');
  if ($newTbody.length) {
    $('#asrTableBody').html($newTbody.html());
  }

  const $newPagi = $html.find('#asrPagination');
  if ($newPagi.length) {
    $('#asrPagination').html($newPagi.html());
  }
}

// 현재 필터 상태 → QueryString
function _asrParams(extraParams = {}) {
  const form = document.getElementById('asrFilterForm');
  const viewInput = document.getElementById('asrViewInput');
  const params = new URLSearchParams(form ? new FormData(form) : undefined);
  if (viewInput && viewInput.value) params.set('view', viewInput.value);
  Object.keys(extraParams).forEach(k => params.set(k, extraParams[k]));
  return params.toString();
}

//============ 부분 갱신 로더 ============
window.reloadList = function(extraParams = {}) {
  if (ASR.inFlight) return;          // 중복 요청 방지
  ASR.inFlight = true;
  ASR.showLoading(true);

  const form = document.getElementById('asrFilterForm');
  const viewInput = document.getElementById('asrViewInput');

  if (!form) { ASR.inFlight = false; ASR.showLoading(false); return; }

  const params = new URLSearchParams(new FormData(form));
  if (viewInput && viewInput.value) params.set('view', viewInput.value);

  Object.keys(extraParams).forEach(k => params.set(k, extraParams[k]));

  // 기존 관리자 Ajax 로더 재사용
  loadAdminPage(ASR.listUrl() + '?' + params.toString(), function(){
    ASR.inFlight = false;
    ASR.showLoading(false);
  });
};

// ============ 이벤트 위임 (중복 바인딩 방지) ============
if (!window.__asrBound) {
  window.__asrBound = true;

  // 검색(필터)
  $(document).on('submit', '#asrFilterForm', function(e){
    e.preventDefault();
    reloadList();
  });

  // 탭
  $(document).on('click', '#asrHiddenTabs a[data-view]', function(e){
    e.preventDefault();
    $('#asrHiddenTabs .nav-link').removeClass('active');
    $(this).addClass('active');
    $('#asrViewInput').val($(this).data('view') || 'active');
    reloadList();
  });

  // 페이징
  $(document).on('click', '#asrPagination .page-link', function(e){
    e.preventDefault();
    const page = $(this).data('page');
    if (page) reloadList({ page });
  });
}

// -----------------------------
// 날짜 유틸
// -----------------------------
function toLocalDTValue(str) {
  if (!str) return '';
  // "yyyy-MM-dd HH:mm:ss", "yyyy-MM-ddTHH:mm:ss", "yyyy-MM-ddTHH:mm:ssXXX" 모두 대응
  const s = String(str).replace(' ', 'T');
  // datetime-local은 분까지만 필요
  const i = s.indexOf('T');
  if (i === -1) return '';
  // "YYYY-MM-DDTHH:mm"까지 자름
  return s.slice(0, 16);
}
//✅ 서버로 보낼 때(datetime-local 값을 컨트롤러 바인딩 포맷에 맞추기)
function fromLocalDTValue(value) {
  if (!value) return '';
  // "yyyy-MM-ddTHH:mm" -> "yyyy-MM-dd HH:mm"
  return String(value).replace('T', ' ');
}
// -----------------------------
// 모달 열기 + 단건 조회
// -----------------------------
function openAsrModal(reqId) {
  $.ajax({
    url: '/after-sales/api/' + reqId,
    type: 'GET',
    dataType: 'json',
    success: function (res) {
    	  $('#asr-reqId').val(res.reqId);
    	  $('#asr-userId').val(res.userId);
    	  $('#asr-reqType').val(res.reqType || '');
    	  $('#asr-reqStatus').val(res.reqStatus || '');
    	  $('#asr-reasonCode').val(res.reasonCode || '');
    	  $('#asr-reasonDetail').val(res.reasonDetail || '');
    	  $('#asr-pickupAddress').val(res.pickupAddress || '');

    	  // ✅ JSP에서 채워둔 관리자 ID(기본값) 보존
    	  const $proc = $('#asr-processedBy');
    	  const adminDefault = $proc.val(); // "${loginUser.userId}" 가 들어있음
    	  if (res.processedBy !== null && res.processedBy !== undefined && res.processedBy !== '') {
    	    $proc.val(res.processedBy);
    	  } else {
    	    $proc.val(adminDefault);
    	  }

    	  $('#asr-pcsId').val(res.productColorSizeId);
    	  $('#asr-orderItemId').val(res.orderItemId);
    	  $('#asr-rejectReason').val(res.rejectReason || '');

    	  // ✅ datetime-local 형식으로 모두 변환
    	  $('#asr-createdAt').val(toLocalDTValue(res.createdAt));
    	  $('#asr-approvedAt').val(toLocalDTValue(res.approvedAt));
    	  $('#asr-completedAt').val(toLocalDTValue(res.completedAt));

    	  const modalEl = document.getElementById('asrModal');
    	  const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
    	  modal.show();
    	},
    error: function () {
      alert('상세 조회 중 오류가 발생했습니다.');
    }
  });
}

// -----------------------------
// 수정 저장
// -----------------------------
function submitAsrUpdate() {
  const form = document.getElementById('asrUpdateForm');
  const formData = new FormData(form);

  const approvedAt  = fromLocalDTValue($('#asr-approvedAt').val());
  const completedAt = fromLocalDTValue($('#asr-completedAt').val());
  formData.set('approvedAt', approvedAt);
  formData.set('completedAt', completedAt);

  $.ajax({
    url: '/after-sales/update',
    type: 'POST',
    data: Object.fromEntries(formData.entries()),
    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
    success: function () {
      const modalEl = document.getElementById('asrModal');
      const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
      modal.hide();

      // ✅ 부분 갱신으로 현재 페이지 그대로 리프레시
      reloadList();
    },
    error: function () {
      alert('수정 저장 중 오류가 발생했습니다.');
    }
  });
}

// 전역 노출(기존 호출부 호환)
window.openAsrModal = openAsrModal;
window.submitAsrUpdate = submitAsrUpdate;
</script>
