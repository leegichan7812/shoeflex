<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<section>
<div class="container py-5 px-2" >
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
        <!-- Shopping Cart -->
        <!-- 장바구니 비었을 때 메시지 -->
		<div id="emptyCartMessage" style="display: flex; flex-direction: column; justify-content: center; align-items: center; margin-left:400px;margin-top:50px; font-size:20px;height: 200px; /* 필요시 높이 조절 */">
			<i class="fa fa-shopping-cart" style="font-size:30px; color:#999;width:300px;text-align:center;"></i><br/>
			장바구니가 비어있습니다.
		</div>
		<form class="bg0 p-t-30 p-b-40 col-lg-10 col-md-8">
			<div class="container" id="cartTableArea">
			<h3 class="fw-bold mb-4" style="text-align:center;font-size:30px;">장바구니</h3>
				<div class="row">
					<div class="col-lg-9 col-xl-9 m-lr-auto m-b-50">
						<div class="m-l-25 m-lr-auto m-lr-0-xl">
							<div class="wrap-table-shopping-cart">
								<table class="table-shopping-cart">
									<tr class="table_head" style="text-align: center;">
										<th class="column-0" style="width:3%;">
											<div style="display: flex; justify-content: center; align-items: center; height: 100%;">
												<input type="checkbox" id="selectAllCart">
											</div>
										</th>
										<th class="column-1" style="width:45%;">Product</th>
										<th class="column-2" style="width:15%;">Price</th>
										<th class="column-3" style="width:12%;">Quantity</th>
										<th class="column-4" style="width:15%;">Total</th>
										<th class="column-5" style="width:10%;">Remove</th>
									</tr>

								<c:forEach var="item" items="${clist}">
								
									<tr class="table_row">
										<td class="column-0" style="width:3%;">
										    <div style="display: flex; justify-content: center; align-items: center; height: 100%;">
										        <input type="checkbox" class="cart-select" name="selectedCartId" value="${item.cartId}" ${item.stock == 0 ? 'disabled' : ''}>
										    </div>
										</td>
										<td class="column-1" style="width:45%;">
											<div style="display: flex; align-items: center;">
												<img src="${item.imageUrl}" alt="IMG" class="cart_img">
										
												<div class="text-start" style="padding-top:30px; padding-left:10px;min-width:200px;">
										            <h4 class="fw-bold text-dark mb-2">${item.productName}</h4>										        
										            <div class="text-muted">
										                색상: ${item.colorNameKor} |
										                사이즈: ${item.sizeValue}
										            </div>
										            <c:if test="${item.stock == 0}">
												      <div class="text-danger mt-2">※ 품절된 상품입니다</div>
												    </c:if>										            
										        </div>												
											</div>
										</td>
										<td class="column-2" style="width:15%; text-align: center;"><fmt:formatNumber value="${item.price}" pattern="#,###"/>원</td>
										<td class="column-3" style="width:12%; text-align: center;">
											<div class="wrap-num-product d-flex justify-content-center align-items-center">
												<div class="btn-num-product-down cl8 hov-btn3 trans-04 flex-c-m">
													<i class="fs-16 zmdi zmdi-minus"></i>
												</div>
								
												<input 
												    type="number" 
												    class="mtext-104 cl3 txt-center num-product" 
												    style="width:50px;" 
												    data-cart-id="${item.cartId}" 
												    data-price="${item.price}" 
												    value="${item.quantity}" 
												    min="1"
												    max="${item.stock}"
												    ${item.stock == 0 ? 'disabled' : ''}
												/>
								
												<div class="btn-num-product-up cl8 hov-btn3 trans-04 flex-c-m">
													<i class="fs-16 zmdi zmdi-plus"></i>
												</div>
											</div>
										</td>
										<td class="column-4" style="width:15%; text-align: center;">
											<span class="item-total"><fmt:formatNumber value="${item.totalPrice}" pattern="#,###"/>원</span>
										</td>
										<td class="column-5" style="width:10%; text-align: center;">
											<button class="btn btn-sm btn-danger btn-delete-cart" data-cart-id="${item.cartId}">
												<i class="fa fa-times"></i>
											</button>
										</td>
									</tr>
								</c:forEach>
								</table>
							</div>

							
						</div>
					</div>

					<c:set var="totalCount" value="0" />
					<c:set var="totalPrice" value="0" />
					
					<c:forEach var="item" items="${clist}">
					    <c:set var="totalCount" value="${totalCount + item.quantity}" />
					    <c:set var="totalPrice" value="${totalPrice + item.totalPrice}" />
					</c:forEach>
					
					<div id="cartTotalArea" class="col-sm-10 col-lg-3 col-xl-3 m-lr-auto m-b-50">
					    <div class="bor10 p-lr-10 p-t-30 p-b-40 m-l-10 m-r-5 m-lr-0-xl">
					        <h4 class="mtext-109 cl2 p-b-30">
					            Cart Totals
					        </h4>
					
					        <div class="flex-w flex-t bor12 p-b-13" style="justify-content: space-between; align-items: center;">
							    <span class="stext-110 cl2">총 갯수:</span>
							    <span class="stext-110 cl2"><strong id="totalCount">${totalCount} 개</strong></span>
							</div>
							
							<div class="flex-w flex-t p-t-27 p-b-33" style="justify-content: space-between; align-items: center;">
							    <span class="mtext-101 cl2">총 금액:</span>
							    <span class="mtext-101 cl2"><strong id="totalPrice"><fmt:formatNumber value="${totalPrice}" pattern="#,###" />원</strong></span>
							</div>
					
					        <button type="button" id="goOrderBtn" class="flex-c-m stext-101 cl0 size-116 bg3 bor14 hov-btn3 p-lr-15 trans-04 pointer">
					            결제
					        </button>
					    </div>
					</div>
					
					
					
				</div>
			</div>
		</form>
	</div>
</div>
</section>
      

