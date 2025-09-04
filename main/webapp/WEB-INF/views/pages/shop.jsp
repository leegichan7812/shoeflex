<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<script>
    // 컨트롤러에서 d.addAttribute("mode", "brand" or "category") 내려준 값 사용
    window.shopMode = "${mode}";
</script>
<section>
<div class="site-section">
	<div class="container">
		<div class="brand-slide-space"></div> <!-- 밀림 공간 -->
		<div class="brand-slide-wrapper">
		    
		<c:choose>
				
	    	<c:when test="${mode eq 'brand'}">
	    		<button class="brand-prev">‹</button>
				    <div class="brand-slide-container"> <!-- 슬라이드를 감싸는 container가 중요 -->
				        <div class="brand-slide">		            
		                    <c:forEach var="brand" items="${brandList}">
		                        <div class="brand-item" data-brand-id="${brand.brandId}">
		                            <img src="${pageContext.request.contextPath}/${brand.brandImg}" alt="${brand.brandName}">
		                        </div>
		                    </c:forEach>    
				        </div>
				    </div>		
			    <button class="brand-next">›</button>
	    	</c:when>
	    	
	
			<c:when test="${mode eq 'category'}">
				<button class="category-prev">‹</button>
	               	<div class="category-slide-container"> <!-- 슬라이드를 감싸는 container가 중요 -->
				        <div class="category-slide">
		                    <c:forEach var="category" items="${categoryList}">
		                        <div class="category-item" data-category-id="${category.categoryId}">
		                            <img src="${pageContext.request.contextPath}/${category.categoryUrl}" alt="${category.categoryName}">
		                        </div>
		                    </c:forEach>
	                    </div>
					</div>	               
				<button class="category-next">›</button>
			</c:when>
		</c:choose>
		
		    
		</div>

        <div class="row mb-5 flex-nowrap main-content">
        	     
        
			<div class="col-md-9 order-2 shop_right">
                    
				<div class="product-list">
				    <div class="row mb-5">
					    <c:forEach var="product" items="${pListBrand}">
					        <div class="col-sm-6 col-lg-4 mb-4" data-aos="fade-up">
					            <div class="block-4 text-center border">
					                <figure class="block-4-image" >
					                    <a href="#" onclick="loadPage('productDetail?productId=${product.productId}')">
					                    	<img src="${product.imageUrl}" alt="${product.name}" class="img-fluid" width="250px" height="250px">
					                    </a>
					                    
					                    <!-- 찜하기 하트 -->
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
				</div>
			</div>

			<div class="col-md-3 order-1 shop_left">
	          	
	            <div class="border p-4 rounded mb-4">
	            	<h2 style="font-size: 24px;">검색필터</h2>
	            	<hr>
					<div class="filter-dynamic">
					    <c:choose>
					      <c:when test="${mode eq 'brand'}">
					        <jsp:include page="/WEB-INF/views/common/categoryFilters.jsp"/>
					      </c:when>
					      <c:when test="${mode eq 'category'}">
					        <jsp:include page="/WEB-INF/views/common/brandFilters.jsp"/>
					      </c:when>
					    </c:choose>
					  </div>
				
					<div class="mb-4">
					    <h3 class="mb-3 h6 text-uppercase text-black d-block">『 가격 』</h3>
					    <div id="slider-range" class="border-primary"></div>
					    <input type="text" id="amount" class="form-control border-0 pl-0 bg-white" readonly />
					</div>
		
					<div class="mb-4">
					    <h3 class="mb-3 h6 text-uppercase text-black d-block">『 사이즈 』</h3>
					
					    <div class="size-selector">
					        <c:forEach var="size" items="${sizeList}">
					            <input type="checkbox" id="size_${size.sizeValue}" name="size" value="${size.sizeId}" class="size-check">
					            <label for="size_${size.sizeValue}" class="size-label">${size.sizeValue}</label>
					        </c:forEach>
					    </div>
					</div>
		
					<div class="mb-4">
					    <h3 class="mb-3 h6 text-uppercase text-black d-block">『 색상 』</h3>
					    <div class="d-flex flex-wrap">
					        <c:forEach var="color" items="${colorList}">
					            <div class="color-label-wrapper">
					                <input type="checkbox" id="color_${color.colorNameKor}" name="color" value="${color.colorId}" class="color-checkbox">
					                <label for="color_${color.colorNameKor}" class="color-label">
					                    <c:choose>
							                <c:when test="${color.colorNameKor eq '멀티컬러'}">
							                    <span class="color-circle" style="background: conic-gradient(red 0% 25%, blue 25% 50%, green 50% 75%, yellow 75% 100%);"></span>
							                </c:when>
							                <c:otherwise>
							                    <span class="color-circle" style="background-color:${color.colorCode};"></span>
							                </c:otherwise>
							            </c:choose>
					                    ${color.colorNameKor}
					                </label>
					            </div>
					        </c:forEach>
					    </div>
					</div>
	
	            </div>
			</div>
		</div>

        
        
	</div>
</div>
</section>