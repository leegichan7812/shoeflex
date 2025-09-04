<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<style>

</style>
<section>
<div class="container py-5 px-2" style="margin: 0 auto;">
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

    <!-- 우측 콘텐츠: 내 취소/반품/교환 신청 -->
    <div class="col ps-lg-4">
      <div class="d-flex justify-content-between align-items-center">
        <h5 class="mb-2">내 취소/반품/교환 신청</h5>

<!-- 필터 -->
<form id="cerCustFilterForm" method="get" class="d-flex gap-2" action="${ctx}/after-sales/Custlist">
  <input type="hidden" name="page" value="${page != null ? page : 1}"/>
  <input type="hidden" name="size" value="${size != null ? size : 10}"/>

  <select id="cerCustReqType" name="reqType" class="form-select form-select-sm" style="width:120px">
    <option value="">전체유형</option>
    <option value="취소"  ${reqType=='취소' ? 'selected':''}>취소</option>
    <option value="반품"  ${reqType=='반품' ? 'selected':''}>반품</option>
    <option value="교환"  ${reqType=='교환' ? 'selected':''}>교환</option>
  </select>

  <select id="cerCustReqStatus" name="reqStatus" class="form-select form-select-sm" style="width:120px">
    <option value="">전체상태</option>
    <option value="신청"  ${reqStatus=='신청' ? 'selected':''}>신청</option>
    <option value="승인"  ${reqStatus=='승인' ? 'selected':''}>승인</option>
    <option value="거절"  ${reqStatus=='거절' ? 'selected':''}>거절</option>
    <option value="완료"  ${reqStatus=='완료' ? 'selected':''}>완료</option>
  </select>

  <button class="btn btn-dark btn-sm">검색</button>
</form>

</div>
<!-- ▼ 목록 + 페이징 래퍼: data-list-url 꼭 설정 -->
<div id="cerCustListWrap" data-list-url="${ctx}/after-sales/Custlist">

  <div class="table-responsive mt-2">
    <table class="table table-bordered table-hover">
      <thead class="thead-dark text-center">
        <tr>
          <th style="width:10%">요청번호</th>
          <th style="width:10%">유형</th>
          <th style="width:10%">상태</th>
          <th>사유</th>
          <th style="width:16%">신청일</th>
          <th style="width:16%">완료일</th>
        </tr>
      </thead>
      <tbody id="cerCustTbody"><!-- ← tbody에 ID -->
        <c:forEach var="it" items="${list}">
          <tr>
            <td class="text-center">${it.reqId}</td>
            <td class="text-center">${it.reqType}</td>
            <td class="text-center">${it.reqStatus}</td>
            <td class="text-start">${it.reasonCode} - ${it.reasonDetail}</td>
            <td class="text-center"><fmt:formatDate value="${it.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td class="text-center">
              <c:choose>
                <c:when test="${not empty it.completedAt}">
                  <fmt:formatDate value="${it.completedAt}" pattern="yyyy-MM-dd HH:mm"/>
                </c:when>
                <c:otherwise>-</c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty list}">
          <tr><td colspan="6" class="text-center text-muted">신청 내역이 없습니다.</td></tr>
        </c:if>
      </tbody>
    </table>

    <!-- 페이징 -->
    <c:if test="${totalPages > 1}">
      <nav class="mt-3">
        <ul id="cerCustPagination" class="pagination pagination-sm justify-content-center">
          <!-- 이전 -->
          <li class="page-item ${page <= 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${page-1}">이전</a>
          </li>

          <!-- 번호 -->
          <c:forEach var="p" begin="1" end="${totalPages}">
            <li class="page-item ${p == page ? 'active' : ''}">
              <a class="page-link" href="#" data-page="${p}">${p}</a>
            </li>
          </c:forEach>

          <!-- 다음 -->
          <li class="page-item ${page >= totalPages ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${page+1}">다음</a>
          </li>
        </ul>
      </nav>
    </c:if>
  </div>

</div><!-- /#cerCustListWrap -->

    </div>
  </div>
</div>
</section>

<script>
//목록 URL (data-list-url 우선)
function _cerCustUrl(){
  return $("#cerCustListWrap").data("list-url") || (window.ctx || "") + "/after-sales/Custlist";
}

// 서버 HTML에서 필요한 부분만 교체
function _swapCerCust(html){
  const $html = $(html);

  const $tb = $html.find("#cerCustTbody");
  if ($tb.length) $("#cerCustTbody").html($tb.html());

  const $pg = $html.find("#cerCustPagination");
  if ($pg.length) $("#cerCustPagination").html($pg.html());
  else $("#cerCustPagination").empty(); // 페이지 1개 이하일 때
}

// 현재 필터(폼) + 추가 파라미터로 GET
function reloadCerCust(extra = {}){
  const $form = $("#cerCustFilterForm");
  const params = new URLSearchParams($form.serialize());

  Object.entries(extra).forEach(([k,v])=>{
    if (v !== undefined && v !== null) params.set(k, v);
  });

  $.ajax({
    url: _cerCustUrl(),
    type: "GET",
    data: params.toString(),
    dataType: "html",
    success: _swapCerCust,
    error: () => alert("목록을 불러오지 못했습니다.")
  });
}

// 필터 submit → 1페이지부터
$(document).on("submit", "#cerCustFilterForm", function(e){
  e.preventDefault();
  // hidden page를 1로
  $(this).find("input[name='page']").val(1);
  reloadCerCust({ page: 1 });
});

 $(document).on("change", "#cerCustReqType, #cerCustReqStatus", function(){
   $("#cerCustFilterForm input[name='page']").val(1);
   reloadCerCust({ page: 1 });
 });

// 페이징 클릭 (위임)
$(document).on("click", "#cerCustPagination .page-link", function(e){
  e.preventDefault();
  const $li = $(this).closest(".page-item");
  if ($li.hasClass("disabled") || $li.hasClass("active")) return;

  const page = parseInt($(this).data("page") || $(this).text(), 10);
  if (!isNaN(page)) {
    $("#cerCustFilterForm input[name='page']").val(page);
    reloadCerCust({ page });
  }
});
</script>
