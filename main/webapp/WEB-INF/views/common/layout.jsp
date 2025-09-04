<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="path" value="${pageContext.request.contextPath }"/>
<fmt:requestEncoding value="UTF-8"/>
<fmt:setLocale value="ko" scope="session"/>

<!DOCTYPE html>
<%--


 --%>
<html>
<head>
<meta charset="UTF-8">
<title>SHOE-FLEX</title>


<link rel="stylesheet" href="${path}/css/shop.css">
<link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
<link rel="stylesheet" type="text/css" href="${path}/css/util.css">
<link rel="stylesheet" type="text/css" href="${path}/css/bootstrap.min.css" >
<link rel="stylesheet" type="text/css" href="${path}/css/slick.css">
<link rel="stylesheet" type="text/css" href="${path}/css/main.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" href="fonts/iconic/css/material-design-iconic-font.min.css">
<link rel="stylesheet" type="text/css" href="fonts/linearicons-v1.0.0/icon-font.min.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/material-design-iconic-font@2.2.0/dist/css/material-design-iconic-font.min.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="${path}/css/jb.css">
<link rel="stylesheet" type="text/css" href="${path}/css/gc.css">
<link rel="stylesheet" type="text/css" href="${path}/css/mc.css">
<link rel="stylesheet" href="${path}/css/inquiry.css">


<style>

pre-wrap{white-space:pre-wrap}

   #chatToggleBtn {
  position: fixed !important;
  bottom: 20px !important;
  right: 20px !important;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  z-index: 20000;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color:#212529;
  border:1px #212529;
}

#chatToggleBtn,
.lang-switch {
  display: none; 
}

.bg-primary {
  background-color: #212529 !important; /* 검정색 */
}
#chatWidget {
  border: none !important;
  box-shadow: none !important; /* 그림자도 없애고 싶다면 */
}

#chatSendBtn{
	background-color:#fe4c50;
	border:none;
}
#jhTotalAmount {
  font-size: 22px;       /* 글자 크기 키우기 */
  font-weight: normal;     /* 보통 글씨체 */
  color: #000000;        /* 강조 색상 (빨간 계열) */
  background-color: #f8f9fa; /* 연한 배경색 (선택사항) */
  padding: 10px 15px;    /* 안쪽 여백 */
  border-radius: 8px;    /* 둥근 테두리 */
  box-shadow: 0 2px 6px rgba(0,0,0,0.15); /* 약한 그림자 */
  display: inline-block; /* 크기 딱 맞게 */
}

#recommend-section .card-img-top { height: 240px; object-fit: cover; }
#recommend-section .rec-card { width: 260px; } /* 슬릭 한 칸 너비 */

</style>

<script>


</script>
</head>

<body>

	<header class="header">
	
		<!-- Main Navigation -->

		<div class="main_nav_container">
			<div class="container">
				<div class="row">
					<div class="col-lg-12 text-right">
						<div class="logo_container">
							<a href="${path}/index">Shoe<span>Flex</span></a>
						
						</div>
						
					<div class="lang-switch my-2 text-right">
					  <button type="button" style="font-size: 0.85rem;" class="langBtn" onclick="changeLang('ko')">
					    한국어
					  </button>
					  <button type="button" style="font-size: 0.85rem; margin-left: 5px;" class="langBtn" onclick="changeLang('en')">
					    English
					  </button>
					  <button type="button" style="font-size: 0.85rem; margin-left: 5px;" class="langBtn" onclick="changeLang('ja')">
					    日本語
					  </button>
					</div>
						<c:if test="${not empty sessionScope.loginUser}">
							<div class="user_infor">
							
								${sessionScope.loginUser.name}님 방문을 환영합니다.
							</div>
						</c:if>
						
						<nav class="navbar">
						
							<ul class="navbar_menu">
								<li class="dropdown">
									<a href="#" class="dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
										<i class="fa fa-bars"></i>
									</a>
									<ul class="dropdown-menu p-3 dropdown-menu-end d-menu-size1">
										<li class="fw-bold pb-2 text-margin">『 브랜드 』</li>
										<li>
											<div class="row row-cols-5 gy-1 text-center">
												<c:forEach var="brand" items="${brandList}">
													<div class="col" style="box-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);">
														<a class="dropdown-item d-flex flex-column align-items-center" href="#" onclick="loadPage('shop/brand?brandId=${brand.brandId}')">
															<img src="${brand.brandImg}" alt="${brand.brandName} 로고" width="70" height="70">
															<span>${brand.brandName}</span>
													    </a>
													</div>
												</c:forEach>
											</div>
										</li>
										
										
										<li class="fw-bold pb-2 text-margin" style="margin-top: 40px;">『 카테고리 』</li>
										<li>
											<div class="row row-cols-5 gy-1 text-center">
												<c:forEach var="category" items="${categoryList}">
													<div class="col" style="box-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);">
														<a class="dropdown-item d-flex flex-column align-items-center" href="#" onclick="loadPage('shop/category?categoryId=${category.categoryId}')">
															<img src="${category.categoryUrl}" alt="${category.categoryName} 이미지" width="70" height="70">
															<span>${category.categoryName}</span>
													    </a>
													</div>
												</c:forEach>	
											</div>
										</li>
									</ul>
								</li>
								<li><a href="#" onclick="loadPage('shop/brand?brandId=1')">브랜드</a></li>
								<li><a href="#" onclick="loadPage('shop/category?categoryId=1')">카테고리</a></li>
							</ul>
							<ul class="navbar_user">
							
								
								<li class="search-container" style="display: none;">
									<a href="#"><i class="fa fa-search search-switch" aria-hidden="true"></i></a>
									<form action="/search" method="get">
				                        <input type="search" class="search-model" id="search-input"/>
				                    </form>
								</li>													
																					
								
								<c:if test="${not empty sessionScope.loginUser}">
									<li class="checkout dropdown" style="display: none;">
										<a href="#" class="dropdown-toggle" data-bs-toggle="dropdown">
											<i class="fa fa-bell"></i>
										</a>
										<ul class="dropdown-menu p-3 dropdown-menu-end d-menu-size2">
											<li>없음</li>
										</ul>
									</li>
								
									<li class="checkout js-show-cart">
										<a href="#" onclick="loadPage('cart')">
											<i class="fa fa-shopping-cart" aria-hidden="true"></i>
											<span class="checkout_items checkout_cart">3</span>
										</a>
									</li>
								
									<li><a href="#" onclick="loadPage('myPage')"><i class="bi bi-person-fill-gear f-size"></i></a></li>
									<li><a href="#" onclick="loadPage('support')"><i class="fa-solid fa-headset"></i></a></li>
									<li><a href="#" id="logoutBtn"><i class="bi bi-box-arrow-right f-size"></i></a></li>
								</c:if>
								<c:if test="${empty sessionScope.loginUser}">
									<li><a href="#" onclick="loadPage('glogin')"><i class="fa fa-user" aria-hidden="true"></i></a></li>	
								</c:if>							
							</ul>
							
						</nav>
					</div>
				</div>
			</div>
		</div>

	</header>
	
	<main>
	<!-- Slider -->
	<section class="section-slide">
	  <div class="wrap-slick1">
	    <div class="slick1">
	
	      <c:forEach var="ev" items="${eventSlideList}">
	        <!-- 배경 이미지 폴백 -->
	        <c:set var="bg" value="${ev.slideImage}" />
	        <c:if test="${empty bg}">
	          <c:set var="bg" value="/img/slide/img_slide01.jpg" />
	        </c:if>
	
	        <div class="item-slick1"
	             data-thumb="<c:url value='${bg}'/>"
	             data-caption="${ev.title}"
	             style="background-image:url('<c:url value='${bg}'/>');">
	          <div class="container h-full">
	            <div class="flex-col-l-m h-full p-t-100 p-b-30 respon5">
	
	              <!-- 브랜드 로고(있으면 로고, 없으면 텍스트) -->
	              <div class="layer-slick1 animated visible-false" data-appear="fadeInDown" data-delay="0">
	                <c:choose>
	                  <c:when test="${not empty ev.brandImg}">
	                  	<span class="ltext-101 cl2 respon2">
	                    	<img src="<c:url value='${ev.brandImg}'/>" alt="${ev.brandName}" width="70" height="70" />
	                    </span>
	                  </c:when>
	                  <c:otherwise>
	                    <span class="ltext-101 cl2 respon2"
	                          style="display:inline-block;padding:.25rem .5rem;border:1px solid #fff;border-radius:.5rem;">
	                      ${ev.brandName}
	                    </span>
	                  </c:otherwise>
	                </c:choose>
	              </div>
	
	              <!-- 슬라이드 타이틀 -->
	              <div class="layer-slick1 animated visible-false" data-appear="fadeInUp" data-delay="800">
	                <h2 class="ltext-201 cl2 p-t-19 p-b-43 respon1">
	                  『${ev.brandName}』 - ${ev.slideTitle}
	                </h2>
	              </div>
	
	              <!-- 링크 -->
	              <div class="layer-slick1 animated visible-false" data-appear="zoomIn" data-delay="1600">
	                <a href="#"
	                   onclick="loadPage('shop/brand?brandId=${ev.brandId}')"
	                   class="flex-c-m cl0 size-101 bg1 bor1 hov-btn1 p-lr-15 trans-04 stext-101 ltext-1012">
	                  구경하기
	                </a>
	              </div>
	
	            </div>
	          </div>
	        </div>
	      </c:forEach>
	
	    </div>
	  </div>
	</section>
	<!-- 🔽 추천 섹션 -->
	<c:if test="${not empty recommendList}">
	<section class="container my-5" id="recommend-section">
	  <h3 class="text-center mb-4">
	    <c:choose>
	      <c:when test="${not empty sessionScope.loginUser}">
	        ${sessionScope.loginUser.name}님이 좋아할 만한
	      </c:when>
	      <c:otherwise>이 상품은 어때요?</c:otherwise>
	    </c:choose>
	  </h3>
	
	  <div class="recommend-slick">
	    <c:forEach var="p" items="${recommendList}">
	      <div class="rec-card px-2">
	        <div class="card h-100 shadow-sm">
	          <a href="#" onclick="loadPage('productDetail?productId=${p.productId}')">
	            <img class="card-img-top" src="${p.imageUrl}" alt="${p.name}"
	                 onerror="this.src='/img/noimg.png'">
	          </a>
	          <!-- 찜하기 하트 -->
			    <c:if test="${not empty sessionScope.loginUser}">
				    <c:set var="isWished" value="false" />
					<c:forEach var="wId" items="${wishList}">
					  <c:if test="${wId == p.productId}">
					    <c:set var="isWished" value="true" />
					  </c:if>
					</c:forEach>
					<button class="wishlist-btn1 wishlist-shop1"
					        data-product-id="${p.productId}"
					        onclick="toggleWishlist(this)">
					  <i class="${isWished ? 'fa-solid' : 'fa-regular'} fa-heart"></i>
					</button>
				</c:if>			     
	          <div class="card-body">
	            <div class="small text-muted">${p.brandName} · ${p.categoryName}</div>
	            <div class="fw-bold text-truncate" title="${p.name}">${p.name}</div>
	            <div class="mt-1">
	              <span class="h6 mb-0">
	                <fmt:formatNumber value="${p.price}" type="number"/>원
	              </span>
	            </div>
	          </div>
	          <div class="card-footer bg-white border-0 d-flex justify-content-between">
	            <button class="btn btn-sm btn-outline-dark"
	                    onclick="loadPage('productDetail?productId=${p.productId}')" style="width:100%;">구매하기</button>
	          </div>
	        </div>
	      </div>
	    </c:forEach>
	  </div>
	</section>
	</c:if>
	</main>
	
	
	
	
	
	
	
	
	<!-- 사이드메뉴 장바구니 -->
	<div class="wrap-header-cart js-panel-cart">
		<div class="s-full js-hide-cart"></div>
	
		<div class="header-cart">
			<div class="header-cart-title">
				<div class="title-box">
					<span class="mtext-103 cl2">
						장바구니
					</span>
				</div>
	
				<div class="js-hide-cart pointer hov-cl1">
					<i class="zmdi zmdi-close"></i>
				</div>
			</div>
			
			<div class="header-cart-content" style="overflow-y: auto; scrollbar-width: none;">
				<ul class="header-cart-wrapitem">
					<!-- start -->
				 	<c:forEach var="cart" items="${list}">
 					<li class="header-cart-item row">
						<div class="header-cart-item-img">
							<img src="${cart.imageUrl}" alt="IMG">
						</div>
	
						<div class="header-cart-item-txt">
							<a href="#" class="header-cart-item-name hov-cl1"
							   title="${cart.productName}">
							   <c:choose>
							      <c:when test="${fn:length(cart.productName) > 12}">
							         ${fn:substring(cart.productName, 0, 12)}...
							      </c:when>
							      <c:otherwise>
							         ${cart.productName}
							      </c:otherwise>
							   </c:choose>
							</a>
	
							<span class="header-cart-item-info">
								${cart.quantity} x ${cart.price}
								<c:if test="${cart.quantity == 0}">
								    <!-- 수량이 0일 때 보여줄 내용 -->
								    <br>
								    <span style="color:red;">※ 품절된 상품입니다</span>
								</c:if>
							</span>
						</div>
					</li>
					</c:forEach>
					
					<!-- end -->
				</ul>			
				
			</div>
			<c:set var="total" value="0" />
				<c:forEach var="item" items="${list}">
   					<c:set var="itemTotal" value="${item.price * item.quantity}" />
    					<c:set var="total" value="${total + itemTotal}" />
			</c:forEach>

			<div class="header-bottom">
  				<div class="header-cart-total">
  					<div id="jhTotalAmount">
    				총합계 : <fmt:formatNumber value="${total}" type="number" />원
			  	</div>
			</div>			
<%--			<div class="header-bottom">
				<div class="header-cart-total">
					Total: $75.00
				</div>

 			<div class="header-cart-buttons">
					<a href="shoping-cart.html" class="flex-c-m bg3 hov-btn3">
						View Cart
					</a>

					<a href="shoping-cart.html" class="flex-c-m bg3 hov-btn3">
						Check Out
					</a>
				</div> 
				
--%>	
			</div>
		</div>
	</div>
	<!-- 채팅 열기 버튼 -->
	<button id="chatToggleBtn" class="btn btn-primary rounded-circle shadow"
	style="position: fixed; top: 700px; right: 20px; width:60px; height:60px;
            z-index: 9999; border-radius: 12px;">
	  <i class="fa fa-comment"></i>
	</button>
	
	<footer class="site-footer">
  <div class="footer-container">
    <!-- 사업자 정보 -->
    <div class="footer-section">
      <h4>사업자 정보</h4>
      <ul>
        <li>대표자: 홍길동</li>
        <li>사업자등록번호: 123-45-67890</li>
        <li>통신판매업신고번호: 제2025-서울강남-0000호</li>
        <li><a href="#">이용약관</a></li>
        <li><a href="#">개인정보처리방침</a></li>
      </ul>
    </div>

    <!-- HELP -->
    <div class="footer-section">
      <h4>HELP</h4>
      <ul>
		<li><a href="#" onclick="loadPage('support')"><i class="fa-solid fa-headset"></i>고객센터</a></li>
        <li><a href="#" onclick="loadPage('/support/notice')"> <i class="fas fa-bullhorn"></i>공지사항</a></li>
        <li><a href="#" onclick="loadPage('/faq')"><i class="fa-solid fa-clipboard-question"></i>자주묻는질문</a></li>
      </ul>
    </div>

    <!-- BRAND -->
    <div class="footer-section">
      <h4>BRAND</h4>
      <div class="brand-logos">
      	<c:forEach var="brand" items="${brandList}">
        	<a href="${brand.brandUrl}" target="_blank">
        		<img src="${brand.brandImg}" alt="${brand.brandName}">
        	</a>
        </c:forEach>
      </div>
    </div>

    <!-- PAYMENT -->
    <div class="footer-section">
      <h4>PAYMENT</h4>
      <ul class="payment-icons">
      	<c:forEach var="payment" items="${paymentList}">
        	<li><img src="${payment.methodImg}" alt="${payment.methodName}"></li>
        </c:forEach>
      </ul>
    </div>
  </div>
 
<!-- 채팅 위젯 (쇼핑몰 공통 레이아웃에 1회만 포함) -->
<div id="chatWidget" class="card shadow" 
     style="position: fixed; bottom: 20px; right: 20px; width: 400px; height: 500px;
            z-index: 9999; border-radius: 12px; overflow: hidden; display: none;">
  <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
    <span><i class="fa fa-comments"></i> 1:1 고객센터</span>
    <button type="button" class="btn btn-sm btn-light" id="chatCloseBtn">&times;</button>
  </div>
  <div class="card-body p-2" id="chatMessages" 
       style="height: 300px; overflow-y: auto; background-color: #fdfdfd;">
  </div>
  <div class="card-footer p-2">
    <div class="input-group input-group-sm">
      <input type="text" id="chatInput" class="form-control" placeholder="메시지를 입력하세요">
      <div class="input-group-append">
        <button class="btn btn-success" id="chatSendBtn">전송</button>
      </div>
    </div>
  </div>
</div>



</footer>
	
		
<div class="modal fade" id="noticePopupModal" tabindex="-1" role="dialog" aria-hidden="true">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content bg-light border-dark">
	      <div class="modal-header bg-dark text-white">
	        <h5 class="modal-title" id="popupTitle">공지</h5>
	      </div>
	
	      <!-- 내용 -->
	      <div class="modal-body" style="max-height:100vh; overflow:auto;">
	        <div id="popupContent" class="pre-wrap" style="color:#000;"></div>
	        <small class="text-muted d-block mt-3" id="popupDate"></small>
	      </div>
		 <!-- ✅ 여기 붙이세요: 푸터(체크박스 + 수동 닫기 버튼) -->
		<div class="modal-footer d-flex justify-content-end">
		  <button type="button" id="btnPopupCloseManual" class="btn btn-secondary">닫기</button>
		</div>
	    </div>
	  </div>
	</div>
		
	
	
	
	
	


	
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!--===============================================================================================-->

<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script src="js/slick.min.js"></script>
<script src="js/slick-custom.js"></script>
<script src="js/CER.js"></script>
<script src="js/jb.js"></script>
<script src="js/gc.js"></script>
<script src="js/sw.js"></script>
<script src="js/main.js"></script>

<script type="text/javascript">
window.contextPath = "${pageContext.request.contextPath}";

$(function () {
	  var $modal = $('#noticePopupModal');

	  // 닫기 버튼(수동) 핸들러
	  $('#btnPopupCloseManual').off('.popup').on('click.popup', function (e) {
	    e.preventDefault();
	    // ✅ 포커스 먼저 해제
	    if (document.activeElement) document.activeElement.blur();
	    $modal.modal('hide');
	  });

	  // X/배경으로 닫을 때도 안전장치
	  $modal.off('hide.bs.modal.ariafix').on('hide.bs.modal.ariafix', function(){
	    if (document.activeElement && this.contains(document.activeElement)) {
	      document.activeElement.blur();
	    }
	  });
	});

(function () {
	  var CTX = "${pageContext.request.contextPath}";
	  var ENDPOINT = (CTX ? CTX : "") + "/support/notice/popup";

	  function normalize(res) {
	    if (!res) return null;
	    if (res.hasPopup === false) return null;
	    var n = res.noticeId ? res : (res.data || res);
	    if (!n || (!n.noticeId && !n.id)) return null;
	    return {
	      noticeId: n.noticeId || n.id,
	      title: n.title || "",
	      content: n.content || "",
	      createdAt: n.createdAt || n.created_at || null
	    };
	  }

	  function openPopup(n) {
	    if (!n) return;
	    document.getElementById("popupTitle").textContent = n.title || "공지";
	    var contentEl = document.getElementById("popupContent");
	    contentEl.textContent = n.content || "";
	    document.getElementById("popupDate").textContent =
	      n.createdAt ? ("작성일: " + new Date(n.createdAt).toLocaleString()) : "";

	    var el = document.getElementById("noticePopupModal");
	    if (window.bootstrap && bootstrap.Modal) {
	      bootstrap.Modal.getOrCreateInstance(el).show();
	    } else if (window.jQuery && jQuery.fn && typeof jQuery.fn.modal === "function") {
	      jQuery(el).modal("show");
	    } else {
	      el.style.display = "block";
	      el.classList.add("show");
	    }
	    attachPopupControls(n);
	  }

	  function fetchAndOpen() {
	    fetch(ENDPOINT, { credentials: "same-origin" })
	      .then(function (r) { return r.ok ? r.json() : null; })
	      .then(function (json) { openPopup(normalize(json)); })
	      .catch(function () {});
	  }

	  document.addEventListener("DOMContentLoaded", fetchAndOpen);
	})();

	(function(){
	  var HIDE_KEY_PREFIX = "hideNoticePopupUntil_";
	  function hideKey(id){ return HIDE_KEY_PREFIX + String(id||"0"); }
	  function suppressOneDay(id){
	    var d = new Date(); d.setDate(d.getDate()+1);
	    localStorage.setItem(hideKey(id), String(d.getTime()));
	  }

	  window.attachPopupControls = function(notice){
	    var $modal = $("#noticePopupModal");
	    $("#dontShowTodayChk").prop("checked", false);
	    $("#btnPopupCloseManual").off("click.popup").on("click.popup", function(){
	      if ($("#dontShowTodayChk").is(":checked")) suppressOneDay(notice.noticeId);
	      if (window.bootstrap && bootstrap.Modal) {
	        bootstrap.Modal.getOrCreateInstance($modal[0]).hide();
	      } else if (window.jQuery && $.fn.modal) {
	        $modal.modal("hide");
	      } else {
	        $modal.removeClass("show").hide();
	      }
	    });
	  };
	})();
	

function loadPage(url) {
	$.ajax({
		url: url,
		type: "get",
		dataType: "html", // HTML 조각을 받아올 거니까
		success: function(response) {
			$("main").html(response); // 받아온 HTML을 삽입

			// 페이지에 따라 특정 초기화 함수 실행
			if (url === 'insertJoin' || url === 'joinUpdate') {
				initJoinFormValidation();  // ✅ 회원가입용 JS 실행
			}
			if (url === 'main') {
				initSlick1(); // 예시: main이면 슬릭 초기화
			}
			if (url === 'login' || url === 'glogin') {
				initLoginFormHandler(); // ✅ 로그인 폼 이벤트 바인딩 함수 호출
			}
			// ✅ 브랜드/카테고리 상품 필터 페이지일 때
			if (url.startsWith("shop/brand")) {
				const params = new URLSearchParams(url.split('?')[1]);
	            const brandId = params.get('brandId');
	            const categoryId = params.get('categoryId'); // ✅ 추가

	            // 현재 상태 세팅
	            currentBrandId = brandId ? Number(brandId) : null;
	            currentCategoryId = categoryId ? Number(categoryId) : null;

	            initFilterUI();
	            // 브랜드 슬라이드는 브랜드 진입시에만 센터링
	            if (brandId) initBrandSlide(brandId);

	            bindFilterEvents();
	          
			}
			// ✅ 브랜드/카테고리 상품 필터 페이지일 때
			if (url.startsWith("shop/category")) {
				const params = new URLSearchParams(url.split('?')[1]);
	            const brandId = params.get('brandId');
	            const categoryId = params.get('categoryId'); // ✅ 추가

	            // 현재 상태 세팅
	            currentBrandId = brandId ? Number(brandId) : null;
	            currentCategoryId = categoryId ? Number(categoryId) : null;

	            initFilterUI();
	            // 브랜드 슬라이드는 브랜드 진입시에만 센터링
	            if (categoryId) initCategorySlide(categoryId);

	            bindFilterEvents();
	          
			}
			if (url === 'cart') {
				checkEmptyCart(); // ✅ 장바구니 비었는지 체크
			}
			if (url.startsWith('productDetail')) {
			    setTimeout(() => {
			        updateTotalPrice();
			    }, 100);
			}
			// ✅ FAQ, Notice, Support 페이지일 때 버튼 보이기
			if (url === '/faq' || url === '/support/notice' || url === 'support') {
			    $("#chatToggleBtn").show();
			    $(".lang-switch").show();
			} else {
			    $("#chatToggleBtn").hide();
			    $(".lang-switch").hide();
			}
			if (url === 'review') {
				$(".modal-backdrop").remove();
			  	$("body").removeClass("modal-open").css("padding-right", "");
			  
			  	// ✅ review 페이지 로딩 후 init 실행
			  	reviewInit();
			}
			// 모달 백드롭 강제 제거
		      
			
		},
		error: function(xhr, status, error) {
			console.error("로딩 실패:", error);
		}
	});	
}
$(document).ready(function(){
    $.get("/getCartCount", function(count){
        if(count > 0){
            $(".checkout_cart").text(count);
            $(".checkout_items").show();
        } else {
            $(".checkout_items").hide();
        }
    });
});

console.log("socketServer = ", "${socketServer}");


</script>

<script>
    window.chatConfig = {
        socketServer: "${socketServer}",
        currentUser: "${sessionScope.loginUser != null ? sessionScope.loginUser.name : '고객님'}"
    };
</script>

<!-- 반드시 chatConfig 정의 후에 mc.js 불러오기 -->
<script src="${path}/js/mc.js"></script>
<script>
  function initRecommendSlick(){
    const $el = $('.recommend-slick');
    if (!$el.length || $el.hasClass('slick-initialized')) return;
    $el.slick({
      slidesToShow: 5, slidesToScroll: 5, infinite: false, arrows: true, dots: false,
      responsive: [
        { breakpoint: 1200, settings: { slidesToShow: 4, slidesToScroll: 4 }},
        { breakpoint: 992,  settings: { slidesToShow: 3, slidesToScroll: 3 }},
        { breakpoint: 768,  settings: { slidesToShow: 2, slidesToScroll: 2 }},
        { breakpoint: 576,  settings: { slidesToShow: 1, slidesToScroll: 1 }}
      ]
    });
  }

  // loadPage 래핑이 중복되지 않게
  if (!window.__loadPageWrapped) {
    window.__loadPageWrapped = true;
    const __orig = window.loadPage;
    window.loadPage = function(url){
      __orig(url);
      setTimeout(initRecommendSlick, 200);
    }
  }
  // 최초 1회
  document.addEventListener('DOMContentLoaded', function(){ setTimeout(initRecommendSlick, 100); });
</script>


</body>
</html>