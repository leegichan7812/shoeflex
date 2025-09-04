<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    <style>
        .error-container {
            margin-top: 5%;
            text-align: center;
        }
        .error-image {
            max-width: 300px;
            margin-bottom: 0px;
        }
    </style>

<div class="container error-container" style="margin-bottom:30px;">
    <h1 class="display-5 text-danger">문제가 발생했습니다. 😢</h1>
    <img src="img/err.png" alt="에러 이미지" class="error-image"/>

    <p class="lead text-muted" >${errorMessage}</p>
    <p class="text-muted">담담자 : 신길동</p>
    <p class="text-muted">연락처 : 010-4673-7422</p>
    <a href="/" class="btn btn-primary mt-3">홈으로 돌아가기</a>
</div>

