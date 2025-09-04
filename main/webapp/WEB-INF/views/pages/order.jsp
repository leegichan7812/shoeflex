<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- 추가 CSS (선택사항) -->
<style>
  #addAddressModal .modal-content {
    max-height: 90vh; /* 전체 모달이 화면 90% 높이까지만 */
  }
  #addAddressModal .modal-body {
    overflow-y: auto; /* 내용이 길면 스크롤 */
  }
</style>
<section>
	<div class="container mt-5">
		<div class="col-md-12">
	    <h2>주문 / 결제</h2>
	    
	    <!-- [1] 주문 상품 목록 -->
	    <h5 class="mt-4">상품 정보</h5>
	    <table class="table table-bordered">
	        <thead>
	            <tr class="table-secondary text-center">
	            	<th>이미지</th>
	                <th>상품명</th>
	                <th>색상</th>
	                <th>사이즈</th>
	                <th>수량</th>
	                <th>단가</th>
	                <th>합계</th>
	            </tr>
	        </thead>
	        <tbody>
	        <c:forEach var="item" items="${orderList}">
	            <tr class="text-center"
					data-pcs-id="${item.productColorSizeId}"
				    data-cart-id="${item.cartId}">
		            <td>
		                <img src="${item.imageUrl}" alt="${item.productName}" style="width:60px; height:60px; object-fit:cover; border-radius:4px; border:1px solid #ddd;" />
		            </td>
	                <td style="vertical-align: middle;">${item.productName}</td>
	                <td style="vertical-align: middle;">${item.colorName}</td>
	                <td style="vertical-align: middle;">${item.sizeValue}</td>
	                <td class="qty" style="vertical-align: middle;">${item.quantity}</td>
	                <td class="unit-price" data-price="${item.unitPrice}" style="vertical-align: middle;"><fmt:formatNumber value="${item.unitPrice}" type="number" /> 원</td>
	                <td style="vertical-align: middle;">
	                    <fmt:formatNumber value="${item.unitPrice * item.quantity}" type="number" /> 원
	                </td>
	            </tr>
	        </c:forEach>
	        </tbody>
	    </table>
		</div>
		<div class="container mt-3 mb-3 row" style="margin-right:0px;padding-right:0px;">
		
		  <!-- [좌측] 배송지 / 메모 / 결제수단 -->
		  <div class="col-md-8">
		
		    <!-- 배송지 카드 -->
		    <div class="card mb-4 p-3">
		      <div class="d-flex justify-content-between align-items-center mb-2">
		        <h5 class="mb-0">배송지</h5>
		        <button class="btn btn-outline-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addressModal">배송지 변경</button>
		      </div>
		      <div>
		        <div id="selectedAddress" class="text-muted">
		          	<strong>${defaultAddress.receiverName}</strong> (${defaultAddress.receiverPhone})<br/>
        			${defaultAddress.address1} ${defaultAddress.address2} (${defaultAddress.zipcode})
		        </div>
		      </div>
		    </div>
		
		    <!-- 배송 메모 카드 -->
		    <div class="card mb-4 p-3">
		      <div class="d-flex justify-content-between align-items-center mb-2">
		        <h5 class="mb-0">배송 요청사항</h5>
		      </div>
		      <input type="text" class="form-control" id="deliveryMemo" name="deliveryMemo"
		             value="${defaultAddress.deliveryMemo}" maxlength="100" placeholder="배송 요청사항을 입력해 주세요" />
		    </div>
		
		   <!-- 결제수단 카드 -->
			<div class="card p-3">
			  <h5 class="mb-3">결제수단</h5>
			  <c:forEach var="method" items="${paymentMethods}">
			    <div class="form-check mb-2">
				    <input class="form-check-input d-none" type="radio" name="paymentMethodId"
				           id="method_${method.methodId}" value="${method.methodId}"
				           ${method.methodId == 1 ? 'checked' : ''} />			  
				    <label class="form-check-label payment-option" for="method_${method.methodId}" style="width:210px;">
				      <img src="${pageContext.request.contextPath}/${method.methodImg}"
				           alt="${method.methodName}"
				           style="width: 40px; height: 40px; object-fit: contain; margin-right: 10px;" />
				      <span>${method.methodName}</span>
				    </label>			    
			    </div>
			  </c:forEach>
			</div>
		
		  </div>
		
		  <!-- [우측] 결제 요약 -->
		  <div class="col-md-4" style="padding-right:0px;">
		    <div class="card p-3">
		      <h5>최종 결제 금액</h5>
		      <div class="d-flex justify-content-between mt-3">
		        <span>총 상품 가격</span>
		        <strong><fmt:formatNumber value="${totalPrice}" type="number" />원</strong>
		      </div>
		      <div class="d-flex justify-content-between">
		        <span>배송비</span>
		        <strong id="shippingFee" data-fee="${shippingFee}">
					<fmt:formatNumber value="${shippingFee}" type="number" />원
				</strong>
		      </div>
		      <hr/>
		      <div class="d-flex justify-content-between">
		        <span style="font-size:20px;">총 결제 금액</span>
		        <strong class="text-danger" style="font-size:30px;">
		          <fmt:formatNumber value="${totalPrice + shippingFee}" type="number" />원
		        </strong>
		      </div>
			  <!-- 배송비 정책 안내 텍스트 -->
			  <div class="mt-3 text-muted" style="font-size: 14px;">
			    ※ 배송비 정책: 1개 3,000원 / 2개 2,500원 / 3개 이상 무료배송
			  </div>
		
		      <!-- 주문 제출 폼 -->
				<input type="hidden" name="shippingAddressId" id="shippingAddressId" value="${defaultAddress.addressId}" />
				<input type="hidden" name="memo" id="hiddenMemo" />
				<input type="hidden" name="paymentMethodId" id="hiddenPayMethod" />   
		      <button type="button" class="btn btn-primary w-100" id="placeOrderBtn">결제하기</button>
		    </div>
		  </div>
		
		</div>

	</div>
	
	<!-- 배송지 선택 모달 -->
	<div class="modal fade" id="addressModal" tabindex="-1" aria-labelledby="addressModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-lg">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">배송지 선택</h5>
	        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
	      </div>
	      <div class="modal-body">
	        <table class="table table-bordered text-center">
	          <thead>
	            <tr class="table-secondary">
	              <th>배송지명</th>
	              <th>수령인</th>
	              <th>연락처</th>
	              <th>주소</th>
	              <th>선택</th>
	            </tr>
	          </thead>
	          <tbody id="addressTableBody">
	            <!-- JS로 목록 동적 추가 -->
	          </tbody>
	        </table>
	        <!-- 배송지 추가 버튼 -->
			<button class="btn btn-outline-secondary mt-3" data-bs-toggle="modal" data-bs-target="#addAddressModal">
			    + 새 배송지 등록
			</button>
	      </div>
	    </div>
	  </div>
	</div>

	<!-- 배송지 추가 모달 -->
	<div class="modal fade" id="addAddressModal" tabindex="-1" aria-labelledby="addAddressModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-dialog-centered modal-lg"> <!-- 크기 옵션은 필요시 조절 -->
	    <div class="modal-content" style="max-height:90vh; overflow-y:auto;"> <!-- 전체 높이 제한 + 스크롤 -->
	      <form id="addAddressForm">
	        <div class="modal-header">
	          <h5 class="modal-title" id="addAddressModalLabel">새 배송지 등록</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	        </div>
	
	        <div class="modal-body">
	          <div class="mb-2">
	            <label for="addressName">배송지 이름</label>
	            <input type="text" class="form-control" name="addressName" required />
	          </div>
	          <div class="mb-2">
	            <label for="receiverName">받는 분 이름</label>
	            <input type="text" class="form-control" name="receiverName" required />
	          </div>
	          <div class="mb-2">
	            <label for="receiverPhone">받는 분 전화번호</label>
	            <input type="text" class="form-control" name="receiverPhone" required />
	          </div>
	          <div class="mb-2">
	            <label for="zipcode">우편번호</label>
	            <input type="text" class="form-control" name="zipcode" />
	          </div>
	          <div class="mb-2">
	            <label for="address1">주소1</label>
	            <input type="text" class="form-control" name="address1" required />
	          </div>
	          <div class="mb-2">
	            <label for="address2">주소2</label>
	            <input type="text" class="form-control" name="address2" />
	          </div>
	          <div class="mb-2">
	            <label for="deliveryMemo">배송 메모</label>
	            <input type="text" class="form-control" name="deliveryMemo" />
	          </div>
	          <div class="form-check mt-2" style="padding-left:20px;">
	            <input class="form-check-input" type="checkbox" name="isDefault" value="Y" id="isDefault">
	            <label class="form-check-label" for="isDefault">
	              기본 배송지로 설정
	            </label>
	          </div>
	        </div>
	
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-primary">등록</button>
	          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
	        </div>
	      </form>
	    </div>
	  </div>
	</div>


</section>
      

