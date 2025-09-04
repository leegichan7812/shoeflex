<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 관리</title>

<style>
    body { font-family: Arial, sans-serif; }
    .tab-menu { margin-bottom: 20px; text-align: right; }
    .tab-btn { padding: 10px 20px; margin-left: 5px; border: 1px solid #ccc; background: #f5f5f5; cursor: pointer; }
    .tab-btn.active { background: #333; color: #fff; }
    .content-area { padding: 20px; border: 1px solid #ddd; min-height: 500px; }
</style>
</head>
<body>
<div class="row">
<div class="col-xl-6">
	<h2>상품 페이지</h2>
</div>

<div class="tab-menu col-xl-6">
    <button class="tab-btn" data-url="/product/ad_Read">상품 조회 / 수정 / 삭제</button>
    <button class="tab-btn" data-url="/product/ad_Insert">상품 등록</button>
</div>
</div>

<div id="content-area" class="content-area">
    <!-- 여기에 Ajax로 JSP 로드됨 -->
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${path}/js/ad_Read.js"></script> 
<script src="${path}/js/ad_Insert.js"></script> 

<script>
$(document).ready(function(){
    // 최초 로딩 (조회)
    loadPage('/product/ad_Read');

    $('.tab-btn').click(function(){
        $('.tab-btn').removeClass('active');
        $(this).addClass('active');

        const url = $(this).data('url');
        loadPage(url);
    });

    function loadPage(url){
        $("#content-area").html("<p>로딩중...</p>");
        $.get(url, function(res){
            // <script> 태그 제거 (실행 방지)
            const html = res.replace(/<script[\s\S]*?>[\s\S]*?<\/script>/gi, "");
            $("#content-area").html(html);

            // JSP별 init 함수 호출 (있으면 실행)
            if (url.includes("ad_Read") && typeof initReadPage === "function") {
            	initReadPage();
			}
            if (url.includes("ad_Insert") && typeof initInsertPage === "function") {
                initInsertPage();
            }
        });
    }
});
</script>

</body>
</html>
