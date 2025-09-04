<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="path" value="${pageContext.request.contextPath }" />
<fmt:requestEncoding value="UTF-8" />
<!DOCTYPE html>
<%--


 --%>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${path}/com/bootstrap.min.css">
<style>
	.input-group-text{width:100%;background-color:linen;
		color:black;font-weight:bolder;}
	.input-group-prepend{width:20%;}
	#chatArea{
		width:80%;height:200px;overflow-y:auto;text-align:left;
		border:1px solid green;
	}
</style>
<script src="${path}/com/jquery-3.7.1.js"></script>
<script src="${path}/com/bootstrap.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#selectLan").val("${param.lang}").change(function(){
			var chVal = $(this).val()
			if(chVal!=''){
				location.href="multilang?lang="+chVal
			}
		})
	});
</script>
</head>

<body>
	<div class="jumbotron text-center">
		<h2>
			<spring:message code="multilang" />
		</h2>
		<h2>
			<spring:message code="welcome" />
		</h2>
		<h2>
			<spring:message code="greet" />
		</h2>
		<h2>
			<spring:message code="regmem" />
		</h2>
	</div>
	<%-- 
multilang=mutilangual
welcome=welcome !
id=identification
reg=register
pwd=password
greet=hi!!!
regmem=register member!
chlang=choice language
ko=korean
en=english		
--%>
	<div class="container">
		<div>
			<select class="form-control" id="selectLan" style="width:20%">
				<option value=""><spring:message code="chlang" /></option>
				<option value="ko"><spring:message code="ko" /></option>
				<option value="en"><spring:message code="en" /></option>
			</select>
		</div>
		<form method="post">
			<div class="input-group mb-3">
				<div class="input-group-prepend ">
					<span class="input-group-text  justify-content-center">
						<spring:message code="id" />
					</span>
				</div>
				<input name="id" class="form-control" value="" />
			</div>
			<div class="input-group mb-3">
				<div class="input-group-prepend ">
					<span class="input-group-text  justify-content-center">
						<spring:message code="pwd" />
					</span>
				</div>
				<input name="pwd" type="password" class="form-control" value="" />
			</div>			
			<div style="text-align: right;">
				<input type="button" class="btn btn-primary" value='<spring:message code="reg" />' id="regBtn" />
			</div>
		</form>
	</div>	
</body>
</html>