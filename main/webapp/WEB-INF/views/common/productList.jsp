<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
    window.shopMode = "${mode}";
</script>

<input type="hidden" id="currentBrandId" value="${brandId != null ? brandId : ''}"/>
<input type="hidden" id="currentCategoryId" value="${categoryId != null ? categoryId : ''}"/>
<input type="hidden" id="currentMode" value="${mode != null ? mode : ''}"/>

	<div class="row mb-5">
	    <c:forEach var="product" items="${pListBrand}">
	        <div class="col-sm-6 col-lg-4 mb-4" data-aos="fade-up">
	            <div class="block-4 text-center border">
	                <figure class="block-4-image" >
	                    <a href="#" onclick="loadPage('productDetail?productId=${product.productId}')">
	                    	<img src="${product.imageUrl}" alt="${product.name}" class="img-fluid" width="250px" height="250px">
	                    </a>
	                    
	                    <c:if test="${not empty sessionScope.loginUser}">
						    <c:set var="isWished" value="false" />
							<c:forEach var="wId" items="${wishList}">
							  <c:if test="${wId == product.productId}">
							    <c:set var="isWished" value="true" />
							  </c:if>
							</c:forEach>
							<button class="wishlist-btn wishlist-shop"
							        data-product-id="${product.productId}"
							        onclick="toggleWishlist(this)">
							  <i class="${isWished ? 'fa-solid' : 'fa-regular'} fa-heart"></i>
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

    <div class="row" data-aos="fade-up">
        <div class="col-md-12 text-center">
            <div class="site-block-27">
                <ul>
                    <c:forEach var="i" begin="1" end="${totalPage}">
                        <li class="<c:if test='${i == currentPage}'>active</c:if>">
                            <a href="#" onclick="loadProductPage(${i}); return false;">${i}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
