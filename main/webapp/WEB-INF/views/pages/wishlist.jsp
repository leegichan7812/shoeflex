<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<section>
<div id="like-page" class="container py-5 px-2" >
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
		
		<div class="row mb-5">
		    <c:forEach var="product" items="${myWishList}">
		        <div class="col-sm-6 col-lg-4 mb-4" data-aos="fade-up" style="min-width: 390px;">
		            <div class="block-4 text-center border" >
		                <figure class="block-4-image">
		                    <a href="#" onclick="loadPage('productDetail?productId=${product.productId}')">
		                        <img src="${product.imageUrl}" alt="${product.name}" class="img-fluid" width="250px" height="250px">
		                    </a>
		                    <c:if test="${not empty sessionScope.loginUser}">
		                        <button class="wishlist-btn wishlist-like"
		                                data-product-id="${product.productId}"
		                                onclick="toggleWishlist(this)">
		                            <i class="fa-solid fa-heart"></i> <!-- 항상 채운 하트 -->
		                        </button>
		                    </c:if>
		                </figure>
		                <div class="block-4-text p-4">
		                    <!-- 상품명 -->
		                    <h3><a href="#">${product.name}</a></h3>
		                    <!-- 상품설명 -->
		                    <p class="mb-0">${product.description}</p>
		                    <!-- 사이즈 -->
		                    <div class="product-sizes mt-2 mb-2">
		                        <c:forEach var="size" items="${product.sizeList}">
		                            <span class="size-label" style="margin-right:5px; margin-bottom: 5px;">
		                                ${size.sizeValue}
		                            </span>
		                        </c:forEach>
		                    </div>
		                    <div class="d-flex justify-content-between align-items-center mb-2" style="width:100%;">
		                        <!-- 색상 -->
		                        <div class="product-colors d-flex align-items-center" style="flex-wrap: wrap;">
		                            <c:forEach var="color" items="${product.colorList}">
		                                <span class="color-square" style="
		                                    display:inline-block; 
		                                    width:20px; 
		                                    height:20px; 
		                                    background-color:${color.colorCode};
		                                    margin-right:5px;
		                                    border:1px solid #ccc;">
		                                </span>
		                            </c:forEach>
		                        </div>
		                        <!-- 가격 -->
		                        <div class="product-price" style="font-weight: bold; color: black; font-size:20px; line-height:1;">
		                            <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> 원
		                        </div>
		                    </div>
		                </div>
		            </div>
		        </div>
		    </c:forEach>
		</div>
		
		<!-- 페이징(옵션) -->
		<c:if test="${not empty totalPage}">
		    <div class="row" data-aos="fade-up">
		        <div class="col-md-12 text-center">
		            <div class="site-block-27">
		                <ul>
		                    <c:forEach var="i" begin="1" end="${totalPage}">
		                        <li class="<c:if test='${i == currentPage}'>active</c:if>">
		                            <a href="#" onclick="loadMyWishPage(${i}); return false;">${i}</a>
		                        </li>
		                    </c:forEach>
		                </ul>
		            </div>
		        </div>
		    </div>
		</c:if>
		        
		
	</div>
</div>
</section>
      

