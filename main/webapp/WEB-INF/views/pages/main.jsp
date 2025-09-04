<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

	

	<!-- Slider -->
	<section class="section-slide">
	  <div class="wrap-slick1">
	    <div class="slick1">
	      <c:forEach var="ev" items="${eventSlideList}">
	        <c:set var="bg" value="${empty ev.slideImage ? 'img/slide/img_slide01.jpg' : ev.slideImage}" />
	        <div class="item-slick1"
	             data-thumb="${bg}"
	             data-caption="${fn:escapeXml(ev.title)}"
	             style="background-image: url(${bg});">
	          <div class="container h-full">
	            <div class="flex-col-l-m h-full p-t-100 p-b-30 respon5">
	
	              <!-- 브랜드명 배지 -->
	              <div class="layer-slick1 animated visible-false" data-appear="fadeInDown" data-delay="0">
	                <span class="ltext-101 cl2 respon2"
	                      style="display:inline-block;padding:.25rem .5rem;border:1px solid #fff;border-radius:.5rem;">
	                  ${ev.brandName}
	                </span>
	              </div>
	
	              <!-- 메인 타이틀 -->
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
	


