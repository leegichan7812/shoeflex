<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*" %>
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
                <li class="nav-item"><a class="nav-link" href="#" onclick="loadPage('myPage')">주문/배송 조회</a></li>
				<li class="nav-item"><a class="nav-link" href="#" onclick="loadPage('${ctx}/after-sales/Custlist')">취소/반품/교환</a></li>                <li class="nav-item"><a class="nav-link" href="#" onclick="loadPage('joinUpdate')">회원정보 수정/탈퇴</a></li>
                <li class="nav-item"><a class="nav-link" href="#" onclick="loadPage('cart')">장바 구니</a></li>
                <li class="nav-item"><a class="nav-link active" href="#" onclick="loadPage('review')">리뷰 관리</a></li>
                <li class="nav-item"><a class="nav-link" href="#" onclick="loadPage('like')">찜 목록</a></li>
                <li class="nav-item"><a class="nav-link" href="#" onclick="loadPage('faq')">FAQ</a></li>
				<li class="nav-item"><a class="nav-link" href="#" onclick="ChatWidget.toggle(); return false;">1:1 문의</a></li>            </ul>
        </div>

        <!-- 메인 콘텐츠 -->
        <div class="col-lg-10 col-md-8 mypage-content">
        	<c:forEach var="review" items="${reviewList}">
			    <div class="card mb-4 shadow-sm d-flex flex-row align-items-stretch">
			        <c:if test="${not empty review.imageNames}">
			            <div class="review-image-slider d-flex flex-column align-items-center justify-content-center" style="width:170px; min-width:170px;">
			                <img class="review-main-img img-thumbnail mb-2"
			                     src="${review.imageNames[0]}"
			                     alt="리뷰 이미지"
			                     data-review-id="${review.reviewId}"
			                     data-images="<c:forEach var='img' items='${review.imageNames}' varStatus='st'>${img}<c:if test='${!st.last}'>,</c:if></c:forEach>"
			                     style="width: 150px; height: auto; object-fit:contain; margin-top: 10px;">
			                <c:if test="${fn:length(review.imageNames) > 1}">
			                    <div class="review-img-dots d-flex justify-content-center gap-1">
			                        <c:forEach var="img" items="${review.imageNames}" varStatus="status">
			                            <span class="dot"
			                                  data-review-id="${review.reviewId}"
			                                  data-img-idx="${status.index}"
			                                  style="width:10px; height:10px; border-radius:50%; background:${status.index == 0 ? '#555' : '#bbb'}; display:inline-block; margin:2px; cursor:pointer;"
			                                  onclick="handleDotClick(this)">
			                            </span>
			                        </c:forEach>
			                    </div>
			                </c:if>
			            </div>
			        </c:if>
			        <div class="flex-grow-1 p-3"
			             style="${empty review.imageNames ? 'width:100%;' : 'width:calc(100% - 170px);'}">
			            <div class="card-header bg-white border-bottom d-flex justify-content-between align-items-center p-0 mb-2">
			                <div class="text-end">
			                    <strong>${review.productName}</strong><br/>
			                    <small class="text-muted">${review.brandName} | ${review.colorNameKor} | ${review.sizeValue}</small>
			                </div>
			                <div style="margin-left: 20px;">
			                    <h5 class="mb-1 text-dark" style="text-align: right;">
			                        <span class="text-warning">
			                            <c:forEach begin="1" end="${review.rating}">&#9733;</c:forEach>
			                        </span>
			                        <span class="text-muted" style="font-size: 0.9rem;">(${review.rating}/5)</span>
			                    </h5>
			                    <p class="text-muted mb-0">
			                        작성일: <fmt:formatDate value="${review.createdAt}" pattern="yyyy-MM-dd HH:mm" />
			                    </p>
			                </div>
			            </div>
			            <p class="text-dark mb-3">${review.content}</p>
			            
			            
			            <div class="d-flex justify-content-end">
			                <button type="button"
						            class="btn btn-sm btn-outline-primary"
						            data-bs-toggle="modal"
						            data-bs-target="#reviewEditModal"
						            data-review-id="${review.reviewId}"
						            data-rating="${review.rating}"
						            data-content="${fn:escapeXml(review.content)}">
						        수정
						    </button>
						
						    <button type="button"
						            class="btn btn-sm btn-outline-danger"
						            onclick="deletePrReview(${review.reviewId})">
						        삭제
						    </button>
    
			            </div>
						<div class="d-none file-data" id="file-data-${review.reviewId}">
						  <c:forEach var="f" items="${review.imageFiles}">
						    <span class="file-item"
						          data-file-id="${f.fileId}"
						          data-fname="${f.fname}"
						          data-original="${fn:escapeXml(f.etc)}"></span>
						  </c:forEach>
						</div>
			        </div>
			    </div>
			</c:forEach>
  
        </div>
		    
    </div>
</div>
</section>



<!-- 통합 수정 모달: 교체 기능 제거 (추가/삭제만) -->
<div class="modal fade" id="reviewEditModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <form id="reviewEditForm" class="modal-content" enctype="multipart/form-data">
      <div class="modal-header">
        <h5 class="modal-title">리뷰 수정</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <div class="modal-body">
        <input type="hidden" name="reviewId" id="editReviewId"/>

        <div class="row g-3">
          <div class="col-md-3">
            <label class="form-label">평점(0~5)</label>
            <input type="number" min="0" max="5" step="1" class="form-control"
                   name="rating" id="editRating"/>
          </div>
          <div class="col-md-9">
            <label class="form-label">내용</label>
            <textarea class="form-control" rows="4" name="content" id="editContent"></textarea>
          </div>
        </div>

        <hr class="my-3"/>

        <!-- 기존 파일 목록 (삭제만) -->
        <div>
          <div class="d-flex justify-content-between align-items-center">
            <h6 class="mb-2">기존 이미지</h6>
            <small class="text-muted">삭제할 이미지만 체크하세요</small>
          </div>
          <div id="existingFileList" class="row g-3">
            <!-- JS로 썸네일 + 삭제 체크박스 렌더링 -->
          </div>
        </div>

        <hr class="my-3"/>

        <!-- 새 파일 추가 -->
        <div>
          <h6 class="mb-2">새 이미지 추가</h6>
          <input type="file" name="newFiles" id="newFiles" multiple class="form-control"/>
          <small class="text-muted">여러 장 선택 가능</small>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">닫기</button>
        <button type="submit" class="btn btn-primary">저장</button>
      </div>
    </form>
  </div>
</div>
<script src="js/review.js"></script>
<script>
var ctx = "${pageContext.request.contextPath}"; // 예: /springweb
function deletePrReview(reviewId) {
    if (!confirm("정말 이 리뷰를 삭제하시겠습니까?")) return;

    $.ajax({
        url: ctx + "/review/delete",
        type: "POST",
        data: { reviewId: reviewId },
        success: function(response) {
            if (response.success) {
                alert("리뷰가 삭제되었습니다.");
                loadPage('review');
            } else {
                alert("삭제 실패: " + response.message);
            }
        },
        error: function(xhr, status, error) {
            alert("서버 오류 발생: " + error);
        }
    });
}
</script>


