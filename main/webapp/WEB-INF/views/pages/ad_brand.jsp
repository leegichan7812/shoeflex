<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<section>
<div class="row">
  <div class="table-responsive col-6" style="margin: 0; padding: 2px; background: white; border-radius: 5px;">
  	<div style="height: 40px; overflow:hidden;">
	    <table class="table table-bordered align-middle text-center brand_table" style="width: 100%; padding: 5px;">
	      <thead class="table-light">
	        <tr>
	          <th style="min-width:50px; max-width:50px;"><input type="checkbox" id="chkAll"></th>          
	          <th style="min-width:80px; max-width:80px;">로고</th>
	          <th style="min-width:100px; max-width:100px;">브랜드 명</th>
	          <th style="min-width:250px; max-width:250px">공식사이트 URL</th>
	          <th style="min-width:100px; max-width:100px;">이벤트업로드</th>
	          <th style="min-width:100px; max-width:100px;">삭제</th>
	        </tr>
	      </thead>
		</table>
	</div>
	
	<div class="brand-body-scroll">
		<table class="table table-bordered align-middle text-center brand_table" style="width:100%; padding: 5px;">
	      <tbody id="brandTbody">
	        <c:forEach var="b" items="${brandList}">
	          <tr data-id="${b.brandId}">
	            <td class="text-center" style="min-width: 50px;"><input type="checkbox" class="row-check" name="brand" value="${b.brandName}"></td>            
	            <td class="text-center" style="min-width: 80px;">
	              <c:choose>
	                <c:when test="${not empty b.brandImg}">
	                  <img src="${pageContext.request.contextPath}${b.brandImg}"
	                       alt="logo" style="width:60px; height:60px; border-radius:0px; ">
	                </c:when>
	                <c:otherwise><span class="text-muted">-</span></c:otherwise>
	              </c:choose>
	            </td>
	            <td class="brand-name" style="min-width: 100px;" data-name="${b.brandName}">${b.brandName}</td>
	            <td style="overflow: hidden; max-width:250px;">
	              <c:choose>
	                <c:when test="${not empty b.brandUrl}">
	                  <a href="${b.brandUrl}" target="_blank">${b.brandUrl}</a>
	                </c:when>
	                <c:otherwise><span class="text-muted">-</span></c:otherwise>
	              </c:choose>
	            </td>            
	            <td class="text-center" style="min-width: 100px;">
	              <button type="button" class="btn btn-sm btn-outline-primary btn-upload"
	                      data-brand='${b.brandId}'
	                      data-bs-toggle="modal" data-bs-target="#eventModal">+</button>
	            </td>
	            <td class="text-center" style="min-width: 100px;">
	              <button type="button" class="btn btn-sm btn-outline-danger btn-del"
	                      data-brand='${b.brandId}'>&times;</button>
	            </td>
	          </tr>
	        </c:forEach>
	      </tbody>
	    </table>
    </div>
  </div>
  <div class="col-6" style="margin: 0; padding: 0px 0px 0px 10px;">
	  <div id="monthlyChart" style="width:100%; height:350px; padding: 10px 2px 10px 2px; background: white; border-radius: 5px;"></div>
	  <div id="yearlyChart" style="width:100%; height:350px; margin-top:10px; padding:2px; background: white; border-radius: 5px;"></div>
  </div>
</div>

<!-- 이벤트 모달 -->
<div class="modal fade" id="eventModal" tabindex="-1" aria-hidden="true" 
	data-list-url="/admin/events/upcoming" 
	data-save-url="/admin/events/{id}/slide"  
	data-delete-url="/admin/events/{id}/slide">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content" style="width: 1300px;">
      <div class="modal-header bg-dark text-white">
        <h5 class="modal-title">
          이벤트 업로드 <span id="ev-brand-title" class="fw-normal small ms-2 text-white-50"></span>
        </h5>
        <button class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close" 
        style="background: white; width: 25px; height: 25px; color:red; font-weight: bold;">X</button>
      </div>

      <div class="modal-body">
        <input type="hidden" id="ev-brand-id">  <!-- 클릭한 브랜드ID 보관 -->
        <div class="d-flex justify-content-between align-items-center mb-2">
          <strong>이벤트 목록</strong>
          <div class="text-muted small">체크한 행만 저장/삭제 대상</div>
        </div>

        <div class="table-responsive" style="max-height: 480px; overflow:auto;">
          <table class="table table-sm table-notice-hover align-middle text-center" id="eventTable">
            <thead class="table-light position-sticky top-0">
              <tr>
                <th class="align-items-center" style="width:40px;"><input type="checkbox" id="ev-check-all"></th>
                <th style="min-width:150px;">이벤트명</th>
                <th style="min-width:120px;">시작일</th>
                <th style="min-width:120px;">종료일</th>
                <th style="min-width:150px;">타이틀</th>
                <th style="min-width:150px;">이미지 업로드 / 확인</th>
              </tr>
            </thead>
            <tbody id="eventTbody" style="color:white;"><!-- JS로 렌더링 --></tbody>
          </table>
        </div>
      </div>

      <div class="modal-footer bg-white d-flex justify-content-end gap-2">
        <button type="button" class="btn btn-outline-danger" id="btnEvImgDelete">이미지 삭제</button>
        <button type="button" class="btn btn-primary" id="btnEvSave">등록</button>
      </div>
    </div>
  </div>
</div>



</section>

<script>
  window.APP_CTX = '${pageContext.request.contextPath}'; // 외부 JS에서 컨텍스트 경로 사용할 때
</script>
<script src="${pageContext.request.contextPath}/js/admin_brand.js"></script>

