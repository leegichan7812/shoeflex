<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<section class="sec-product-detail bg0 p-t-65 p-b-60">
<div class="container">
	<div class="row align-items-start">
		<c:set var="product" value="${proDet}" />
		<div class="col-lg-1 mb-4"></div>	
		<div class="col-lg-5 mb-4">		
			<img src="${product.imageUrl}" 
                 alt="${product.name}" 
                 class="img-fluid w-100" 
                 style="max-width: 100%; height: auto; border-radius: 4px; border: 4px solid #D3D3D3;" />						
		</div>
		<div class="col-lg-5">
			<div class="p-t-5 p-lr-0-lg">
				<h2 class="mtext-105 cl2" style="font-size:30px; padding-left: 12px;">
					${product.name}
				</h2>
				<hr style="border: 0; height: 4px; background-color: #D3D3D3;" />
				 <!-- 단가 -->
			    <div class="d-flex mb-2">
			        <div class="col-4 fw-bold" style="font-size:18px;">단가</div>
			        <div class="col-8 fw-bold" style="padding-left:30px;font-size:18px;">
			            <fmt:formatNumber value="${product.price}" type="number" /> 원
			        </div>
			    </div>
			    <div class="d-flex mb-2">
			        <div class="col-4 fw-bold" style="font-size:18px;">브랜드</div>
			        <div class="col-8 fw-bold" style="padding-left:30px;font-size:18px;">
			            ${product.brandName}
			        </div>
			    </div>
			    <div class="d-flex mb-2">
			        <div class="col-4 fw-bold" style="font-size:18px;">카테고리</div>
			        <div class="col-8 fw-bold" style="padding-left:30px;font-size:18px;">
			            ${product.categoryName}
			        </div>
			    </div>
			    <hr style="border: 0; height: 2px; background-color: #D3D3D3;" />
			    <div class="d-flex mb-2">
			        <div class="col-4 fw-bold mt-3" style="font-size:18px;padding-top:8px;">색상</div>
			        <div class="col-8 fw-bold" style="padding-left:30px;font-size:18px;">
			            <div class="product-colors d-flex flex-wrap mt-3" id="colorGroup" data-product-id="${product.productId}">
						    <c:forEach var="color" items="${product.colorList}">
						        <div class="color-option me-2 mb-2" 
						        	 data-color-id="${color.colorId}" 
						       		 data-color-name="${color.colorNameKor}"
						             title="${color.colorNameKor}"	 
						             style="
						                 width: 35px;
						                 height: 35px;
						                 border-radius: 50%;
						                 border: 2px solid #ccc;
						                 background-color: ${color.colorCode};
						                 cursor: pointer;
						                 margin-right: 15px;  /* 오른쪽 간격 증가 */"> 
						        </div>
						    </c:forEach>
						</div>
			        </div>
			    </div>
				
				<div id="colorNameBox" class="mb-2" style="display:none;">
			        <div class="col-4 fw-bold" style="font-size:18px;"></div>
			        <div class="col-8 fw-bold" style="padding-left:30px;font-size:18px;">
			            <span id="selectedColorName" class="fw-bold"></span>
			        </div>
			    </div>
				<hr style="border: 0; height: 2px; background-color: #D3D3D3;" />
				<input type="hidden" id="colorSelect" />
				<div class="d-flex mb-2">
			        <div class="col-4 fw-bold mt-3" style="font-size:18px;padding-top:8px;">사이즈</div>
			        <div class="col-8 fw-bold" style="padding-left:30px;font-size:18px;">
			            <select id="sizeSelect" class="form-select" style="max-width: 300px;">
						    <option selected disabled>사이즈를 선택하세요</option>
						</select>
			        </div>
			    </div>
				
				<div class="d-flex mb-2">
					<div class="col-4 fw-bold mt-3" style="font-size:18px;padding-top:8px;">수량</div>
				    <!-- 수량 조절 -->
				    <div class="col-8 wrap-pd-num-product flex-w m-r-20 mt-3" style="padding-left:30px;">
				        <div class="pd-btn-down cl8 hov-btn3 trans-04 flex-c-m" style="cursor:pointer;">
				            <i class="fs-16 zmdi zmdi-minus"></i>
				        </div>

				        <input class="mtext-104 cl3 txt-center num-product" 
				               type="number" 
				               id="quantity" 
				               name="quantity" 
				               value="1" 
				               min="1"
				               style="width: 60px;"/>
				               <input type="hidden" id="unitPrice" value="<c:out value='${product.price}' default='0'/>" />
				               <!-- 단가 전달 -->

				        <div class="pd-btn-up cl8 hov-btn3 trans-04 flex-c-m" style="cursor:pointer;">
				            <i class="fs-16 zmdi zmdi-plus"></i>
				        </div>
				    </div>		
				</div>
				<hr style="border: 0; height: 4px; background-color: #D3D3D3;" />
				<!-- 총 금액 표시 영역 -->
				<div class="d-flex mb-2">
			        <div class="col-4 fw-bold" style="font-size:18px;">총 금액</div>
			        <div class="col-8 fw-bold" style="padding-left:30px;font-size:18px;text-align:right;">
			            <span id="totalPrice" class="fw-bold" style="color:red;font-size:30px;"></span> 원
			        </div>
			    </div>
			    <div class="d-flex mb-2">
				    
					<div class="m-t-20 col-6">
					    <input type="hidden" id="loginUserId" value="${loginUser.userId}" />
					    <button id="addToCartBtn"
					            class="flex-c-m stext-101 cl10 size-101 bg2 hov-btn3 trans-04"
					            style="height: 45px;width:95%;">
					        장바구니
					    </button>
					</div>
					<div class="m-t-20 col-6">
					    
					    <button id="buyNowBtn"
					            class="flex-c-m stext-101 cl0 size-101 bg3 hov-btn3 trans-04"
					            style="height: 45px;width:95%;">
					        바로구매
					        <input type="hidden" id="productId" value="${product.productId}" />
					        
					    </button>
					</div>
					
				</div>
			</div>								
		</div>
		<div class="col-lg-1 col-md-12 mb-4"></div>	
	</div>
</div>


<div class="container mt-5">
	<div class="row">
	<div class="col-1"></div>
	<div class="col-10">
	    <ul class="nav nav-tabs" id="productTab" role="tablist">        
	        <li class="nav-item" role="presentation">
	            <button class="nav-link active" id="review-tab" data-bs-toggle="tab" data-bs-target="#review" type="button" role="tab" aria-selected="true">
	                리뷰
	            </button>
	        </li>
	        <li class="nav-item" role="presentation">
	            <button class="nav-link" id="qna-tab" data-bs-toggle="tab" data-bs-target="#qna" type="button" role="tab" aria-selected="false">
	                문의
	            </button>
	        </li>
	    </ul>
	
	    <div class="tab-content border border-top-0" id="productTabContent" style="min-height: 300px;">        
	        <div class="tab-pane fade show active" id="review" role="tabpanel">
	            <p></p>
	            <jsp:include page="productDetailReview.jsp" />
	        </div>
	        <div class="tab-pane fade" id="qna" role="tabpanel">
	            <p></p>
	            <jsp:include page="productDetailInquiry.jsp" />
	        </div>
	    </div>
    </div>    
	<div class="col-1"></div>
	</div>
</div>

</section>
      

