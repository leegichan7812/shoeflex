<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>에러 발생</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <c:set var="path" value="${pageContext.request.contextPath}"/>
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
</head>
<body>
<div class="container error-container">
    <h1 class="display-5 text-danger">문제가 발생했습니다. 😢</h1>
    <img src="${path}/img/err.png" alt="에러 이미지" class="error-image"/>

    <p class="lead text-muted" >${errorMessage}</p>
    <p class="text-muted">담담자 : 신길동</p>
    <p class="text-muted">연락처 : 010-4673-7422</p>
    <a href="/" class="btn btn-primary mt-3">홈으로 돌아가기</a>
</div>
</body>
</html>
