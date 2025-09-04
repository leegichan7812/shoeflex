<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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

        <!-- 메인 콘텐츠 -->
        <div class="col-lg-10 col-md-8 mypage-content">
            <h3 class="fw-bold mb-4" style="text-align:center;">주문/배송 조회</h3>

        <div class="container">
        <c:if test="${empty orderList}">
		    <div class="alert text-center mt-4" role="alert">
		        조회된 주문 내역이 없습니다.
		    </div>
		</c:if>
		
		<c:set var="prevOrderId" value="-1" /> 		
		<c:set var="orderTotal" value="0" />		
		<c:set var="orderShippingFee" value="0" /> 	
		
		<c:forEach var="item" items="${orderList}" varStatus="loop">
		
		    <c:if test="${item.orderId ne prevOrderId}">
		        <c:if test="${prevOrderId ne -1}">
		            </div> <!-- .order-items-group -->
		            <div class="d-flex justify-content-end text-end mt-3 border-top pt-3" style="padding-right:10px; margin-bottom:10px;">
		                <strong class="text-dark" style="font-size:20px;">합계</strong>
		                <span class="fw-bold" style="font-size:20px;">
		                    (<fmt:formatNumber value="${orderTotal}" type="number" groupingUsed="true" maxFractionDigits="0" /> 원) 
		                </span>
		                <strong class="text-dark" style="font-size:20px;">&nbsp;+ 배송비</strong>
		                <span class="fw-bold" style="font-size:20px;">
		                    (<fmt:formatNumber value="${orderShippingFee}" type="number" groupingUsed="true" maxFractionDigits="0" /> 원)
		                </span>
		                <strong class="text-dark" style="font-size:20px;">&nbsp;= 총 금액</strong>
		                <span class="fw-bold" style="font-size:20px;">
		                    (<fmt:formatNumber value="${orderTotal + orderShippingFee}" type="number" groupingUsed="true" maxFractionDigits="0" /> 원)
		                </span> 
		            </div>
		            </div> <!-- .card-body -->
		        </div> <!-- .card -->
		        </c:if>
		
		        <!-- 새 카드 시작 -->
		        <c:set var="orderTotal" value="0" />
		        <c:set var="orderShippingFee" value="${item.shippingFee}" />
		        <c:set var="prevOrderId" value="${item.orderId}" />
		
		        <div class="card mb-4 shadow-sm">
		            <div class="card-header bg-white border-bottom d-flex justify-content-between align-items-center">
		                <h5 class="mb-0 text-dark">
		                    주문 일자: <fmt:formatDate value="${item.purchasedAt}" pattern="yyyy-MM-dd HH:mm" />
		                </h5>
		                <span class="badge 
		                    <c:choose>
		                        <c:when test="${item.orderStatus eq '주문완료'}">badge-success</c:when>
		                        <c:when test="${item.orderStatus eq '주문취소'}">badge-danger</c:when>
		                        <c:when test="${item.orderStatus eq '결제대기'}">badge-warning</c:when>
		                        <c:when test="${item.orderStatus eq '반품처리중'}">badge-info</c:when>
		                        <c:otherwise>badge-secondary</c:otherwise>
		                    </c:choose>
		                p-2" style="padding-right:50px;"> ${item.orderStatus}</span>
		            </div>
		
		            <div class="card-body">
		                <div class="order-items-group">
		    </c:if>
		
		    <!-- 상품 출력 -->
		    <div class="d-flex justify-content-between border-bottom py-3 flex-nowrap" style="gap:10px; overflow:hidden;">
		        <div class="d-flex flex-nowrap" style="gap:10px;">
		            <div class="me-2" style="border: 4px solid #D3D3D3; border-radius: 2px; max-height: 128px;">
		                <img src="${item.imageUrl}" alt="${item.productName}" width="120" height="120" >
		            </div>
		            <div class="text-start" style="padding-top:10px; min-width:180px; max-width: 440px;">
		                <h4 class="fw-bold text-dark mb-2">${item.productName}</h4>
		                <div class="text-muted">
		                    브랜드: ${item.brandName} |
		                    카테고리: ${item.categoryName}
		                </div>
		                <div class="text-muted">
		                    색상: ${item.colorName} |
		                    사이즈: ${item.productSize}
		                </div>
		                <div class="text-muted">
		                    수량: ${item.quantity}개 |
		                    단가: <fmt:formatNumber value="${item.unitPrice}" type="number" groupingUsed="true" maxFractionDigits="0" /> 원
		                </div>
		            </div>
		        </div>
		
		        <div class="d-flex flex-nowrap" style="width:500px; min-width:500px; justify-content: space-between; align-items: center;">
		            <div style="text-align:center; min-width:130px;">
		                <p>송장 번호</p>
		                <p class="text-secondary">
		                    <c:choose>
		                        <c:when test="${empty item.trackingNumber}">-</c:when>
		                        <c:otherwise>${item.trackingNumber}</c:otherwise>
		                    </c:choose>
		                </p>
		            </div>
		
		            <div style="text-align:center; min-width:130px;">
		                <p>배송 상태</p> 
		                <p class="badge
		                    <c:choose>
		                        <c:when test="${item.shippingStatus eq '배송완료'}">badge-success</c:when>
		                        <c:when test="${item.shippingStatus eq '배송취소'}">badge-danger</c:when>
		                        <c:when test="${item.shippingStatus eq '배송준비중'}">badge-warning</c:when>
		                        <c:when test="${item.shippingStatus eq '배송중'}">badge-info</c:when>
		                        <c:otherwise>badge-secondary</c:otherwise>
		                    </c:choose>
		                " style="font-size:15px; padding:8px 14px;">${item.shippingStatus} </p>
		            </div>
		            <div class="mp-btn-box" >

						
						<c:if test="${item.shippingStatus eq '배송완료'}">
					        <button
					          type="button"
					          class="btn btn-outline-primary btn-open-review"
					          data-user-id="${loginUser.userId}"
					          data-product-size="${item.productSize}"
					          data-pcs-id="${item.orderItemId}"  <%-- ★ 반드시 실어주기 --%>
					        >
					          리뷰 작성하기
					        </button>
					    </c:if>
											
		            	
						    <!-- orderItemId 기반으로 진행중 신청 객체 가져오기 -->
						    <c:set var="openReq" value="${openReqMap[item.orderItemId]}" />
						
						    <button type="button"
						    	<c:choose>
						    		<c:when test="${openReq != null}">
						           		class="btn-outline-danger btn-open-cer"
						            </c:when>
						            <c:otherwise>
						            	class="btn btn-outline-danger btn-open-cer"
						            </c:otherwise>
						        </c:choose>
						            data-order-item-id="${item.orderItemId}"
						            data-product-color-size-id="${item.productColorSizeId}"
						            data-req-type="${openReq != null ? openReq.reqType : ''}"
						            data-req-status="${openReq != null ? openReq.reqStatus : ''}"
						            ${openReq != null ? 'disabled' : ''}>
						        <c:choose>
						            <c:when test="${openReq != null}">
						                ${openReq.reqType} 진행중...
						            </c:when>
						            <c:otherwise>
						                취소/교환/반품
						            </c:otherwise>
						        </c:choose>
						    </button>
							
		            </div>
		        </div>
		    </div>
		
		    <!-- 상품별 금액 누적 -->
		    <c:set var="orderTotal" value="${orderTotal + item.productTotalPrice}" />
		
		    <!-- 마지막 주문일 때 카드 닫기 -->
		    <c:if test="${loop.last}">
		        </div> <!-- .order-items-group -->
		        <div class="d-flex justify-content-end text-end mt-3 border-top pt-3" style="padding-right:10px; margin-bottom:10px;">
		            <strong class="text-dark" style="font-size:20px;">합계</strong>
		            <span class="fw-bold" style="font-size:20px;">
		                (<fmt:formatNumber value="${orderTotal}" type="number" groupingUsed="true" maxFractionDigits="0" /> 원) 
		            </span>
		            <strong class="text-dark" style="font-size:20px;">&nbsp;+ 배송비</strong>
		            <span class="fw-bold" style="font-size:20px;">
		                (<fmt:formatNumber value="${orderShippingFee}" type="number" groupingUsed="true" maxFractionDigits="0" /> 원)
		            </span>
		            <strong class="text-dark" style="font-size:20px;">&nbsp;= 총 금액</strong>
		            <span class="fw-bold" style="font-size:20px;">
		                (<fmt:formatNumber value="${orderTotal + orderShippingFee}" type="number" groupingUsed="true" maxFractionDigits="0" /> 원)
		            </span> 
		        </div> <!-- .order-sum -->
		        </div> <!-- .card-body -->
		    </div> <!-- .card -->
		    </c:if>
		</c:forEach>
</div>

<nav aria-label="Page navigation">
  <ul class="pagination justify-content-center">

    <!-- 처음 -->
    <li class="page-item" 
        style="<c:if test='${curPage == 1}'>display:none;</c:if>">
      <a class="page-link" href="javascript:void(0);" 
         onclick="loadPage('myPage?curPage=1')">처음</a>
    </li>

    <!-- 이전 -->
    <li class="page-item" 
        style="<c:if test='${startPage == 1}'>display:none;</c:if>">
      <a class="page-link" href="javascript:void(0);" 
         onclick="loadPage('myPage?curPage=${startPage - 1}')">이전</a>
    </li>

    <!-- 페이지 번호 -->
    <c:forEach var="p" begin="${startPage}" end="${endPage}">
      <li class="page-item ${p == curPage ? 'active' : ''}">
        <a class="page-link" href="javascript:void(0);" 
           onclick="loadPage('myPage?curPage=${p}')">${p}</a>
      </li>
    </c:forEach>

    <!-- 다음 -->
    <li class="page-item" 
        style="<c:if test='${endPage == totalPage}'>display:none;</c:if>">
      <a class="page-link" href="javascript:void(0);" 
         onclick="loadPage('myPage?curPage=${endPage + 1}')">다음</a>
    </li>

    <!-- 마지막 -->
    <li class="page-item" 
        style="<c:if test='${curPage == totalPage}'>display:none;</c:if>">
      <a class="page-link" href="javascript:void(0);" 
         onclick="loadPage('myPage?curPage=${totalPage}')">마지막</a>
    </li>

  </ul>
</nav>
<div class="text-center text-secondary" style="margin-bottom:10px;">
    총 <strong>${totalPage}</strong> 페이지
</div>

</section>
<!-- 교환/반품/취소 신청 모달 -->
<div class="modal fade" id="cerModal" tabindex="-1"
     role="dialog" aria-modal="true" aria-labelledby="cerModalLabel"
     data-default-address="${fn:escapeXml(loginUser.address)}">
  <div class="modal-dialog modal-dialog-centered" style="max-width: 720px;">
    <div class="modal-content">
      <div class="modal-header bg-dark text-white">
        <h5 class="modal-title" id="cerModalLabel">교환/반품/취소 신청</h5>
        <button type="button" class="btn-close btn-close-white"
                data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <div class="modal-body" id="cerModalBody">
        <!-- 숨김값 -->
        <input type="hidden" id="cer-orderItemId" name="orderItemId">
        <input type="hidden" id="cer-productColorSizeId" name="productColorSizeId">

        <!-- 조회 결과 요약 -->
        <div class="mb-3">
          <label class="form-label" for="cer-productSummary">상품 정보</label>
          <input type="text" class="form-control" id="cer-productSummary"
                 placeholder="주문항목 조회 후 표시됩니다." readonly>
        </div>

        <div class="row g-2">
          <div class="col-md-4">
            <label class="form-label" for="cer-reqType">요청유형</label>
            <select class="form-select" id="cer-reqType" name="reqType" required>
              <option value="" selected disabled>선택</option>
              <option value="취소">취소</option>
              <option value="반품">반품</option>
              <option value="교환">교환</option>
            </select>
          </div>

          <div class="col-md-4">
            <label class="form-label" for="cer-reasonCode">사유</label>
            <select class="form-select" id="cer-reasonCode" name="reasonCode" required>
              <option value="" selected disabled>선택</option>
              <option value="단순변심">단순변심</option>
              <option value="파손">파손</option>
              <option value="오배송">오배송</option>
              <option value="불량">불량</option>
              <option value="색상/사이즈 불만">색상/사이즈 불만</option>
              <option value="기타">기타</option>
            </select>
          </div>

          <div class="col-md-12">
            <label class="form-label" for="cer-reasonDetail">상세사유</label>
            <textarea class="form-control" rows="3" id="cer-reasonDetail"
                      name="reasonDetail" placeholder="상세 사유를 입력하세요."></textarea>
          </div>
             <!-- ✅ 수거주소: 반품/교환 선택시에만 표시 -->
		<div class="col-md-12 d-none" id="cer-pickupAddress-group">
		  <label class="form-label" for="cer-pickupAddress">수거주소</label>
		  <input type="text" class="form-control" id="cer-pickupAddress" name="pickupAddress" placeholder="수거 주소를 입력하세요.">
		</div>
        </div>
      </div>

      <div class="modal-footer bg-light">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
        <button type="button" class="btn btn-dark" id="cer-submitBtn">신청하기</button>
      </div>
    </div>
  </div>
</div>

      
<!-- 리뷰 작성 모달 -->
<div class="modal fade" id="reviewWriteModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" style="max-width: 700px;">
    <div class="modal-content">
      <div class="modal-header bg-dark text-white">
        <h5 class="modal-title">리뷰 작성</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <form id="reviewForm" method="post" enctype="multipart/form-data">
        <div class="modal-body">
          <!-- 보여주기용 -->
          <div class="mb-3">
            <label class="form-label">상품</label>
            <input type="text" class="form-control" id="rv-product-title" readonly>
          </div>

          <!-- 히든예정 -->
          <input type="hidden" name="userId" id="rv-user-id">
          <input type="hidden" name="pcsi" id="rv-pcs-id">

          <div class="mb-3">
            <label class="form-label">별점</label>
            <select class="form-select" name="rating" required>
              <option value="" selected disabled>선택하세요</option>
              <option value="5">★★★★★ (5)</option>
              <option value="4">★★★★☆ (4)</option>
              <option value="3">★★★☆☆ (3)</option>
              <option value="2">★★☆☆☆ (2)</option>
              <option value="1">★☆☆☆☆ (1)</option>
            </select>
          </div>

          <div class="mb-3">
            <label class="form-label">내용</label>
            <textarea class="form-control" name="content" rows="5" placeholder="상품 사용 후기를 작성해주세요." required></textarea>
          </div>

          <!-- 이미지 업로드(선택) -->
          <div class="mb-2">
            <label class="form-label">사진 첨부 (선택)</label>
            <input type="file" name="reports" id="rv-files" class="form-control" multiple accept="image/*">
            <div class="form-text">여러 장 가능</div>
          </div>
        </div>

        <div class="modal-footer bg-light">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
          <button type="submit" class="btn btn-primary">등록</button>
        </div>
      </form>
    </div>
  </div>
</div>



      


