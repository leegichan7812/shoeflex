<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<section>
<div class="container py-5 px-2">
	<div class="row justify-content-center"> 
		<div class="col-12 mypage-content"> <!-- 중앙 배치 -->
			<c:forEach var="review" items="${reviewList}">
			    <div class="card mb-4 shadow-sm d-flex flex-row align-items-stretch">
			        <!-- ① 이미지 영역: 이미지가 있을 때만 출력 -->
			        <c:if test="${not empty review.imageNames}">
			            <div class="review-image-slider d-flex flex-column align-items-center justify-content-center" style="width:170px; min-width:170px;">
			                <img class="review-main-img img-thumbnail mb-2"
			                     src="${pageContext.request.contextPath}/images/review/${review.imageNames[0]}"
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
			        <!-- ② 카드 바디: 이미지 유무에 따라 width 다르게 -->
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
			        </div>
			    </div>
			</c:forEach>

		</div> <!-- col -->
	</div> <!-- row -->
</div> <!-- container -->
</section>

