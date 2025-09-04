<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>상품 등록 및 색상/사이즈 등록</title>
<style>
    table { border-collapse: collapse; width: 100%; margin-bottom: 20px;}
    th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
    input, select { width: 95%; }
    .button-area { text-align: right; margin-top: 10px; position: relative; }
    .disabled { background-color: #eee; pointer-events: none; opacity: 0.6; }
    #excelFile { position: absolute; width: 0; height: 0; opacity: 0; overflow: hidden; }
</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<div class="row">
	<div class="col-6">
		<h2>상품 등록</h2>
	</div>

	<div class="col-6">
		<div style="text-align:right;">
	        <button type="button" onclick="downloadTemplate()" style="padding: 5px 10px;">엑셀 양식 다운로드</button>
	    </div>
	</div>
</div>
<form id="productForm" enctype="multipart/form-data">
    

    <table>
        <thead>
            <tr>
                <th>상품명</th>
                <th>설명</th>
                <th>가격</th>
                <th>브랜드</th>
                <th>카테고리</th>
                <th>이미지 URL</th>
            </tr>
        </thead>
        <tbody id="productTable">
            <tr>
                <td><input type="text" name="name" class="productInput" required></td>
                <td><input type="text" name="description" class="productInput"></td>
                <td><input type="number" name="price" class="productInput" required></td>
                <td>
                    <select name="brandId" class="productInput" required>
                        <c:forEach var="brand" items="${brandList}">
                            <option value="${brand.brandId}">${brand.brandName}</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <select name="categoryId" class="productInput">
                        <option value="">선택 안함</option>
                        <c:forEach var="category" items="${categoryList}">
                            <option value="${category.categoryId}">${category.categoryName}</option>
                        </c:forEach>
                    </select>
                </td>
                <td><input type="text" name="imageUrl" class="productInput"></td>
            </tr>
        </tbody>
    </table>

    <div id="fileNameArea" style="text-align:right; margin-bottom:5px; font-size:0.9em; color:#555;"></div>

    <div class="button-area">
        <button type="button" id="addRowBtn" onclick="addRow()">행 추가</button>

        <button type="button" id="fileSelectBtn" style="margin-left:20px;" onclick="$('#excelFile').click();">파일 선택</button>
        <input type="file" id="excelFile" name="file" accept=".xlsx" onchange="handleFileSelect(this)">

        <button type="button" id="submitBtn" style="margin-left:20px;">등록</button>
    </div>
</form>

<!-- 색상/사이즈 등록 영역 (초기 비활성화) -->
<div id="colorSizeSection" style="display:none; margin-top:40px;">
    <h2>색상 / 사이즈 재고 등록</h2>
    <form id="colorSizeForm">
        <input type="hidden" name="productIds" id="productIds">

        <table border="1">
            <thead>
                <tr>
                    <th rowspan="2">상품명</th>
                    <th rowspan="2">색상</th>
                    <th colspan="13">사이즈 재고</th>
                </tr>
                <tr>
                    <c:forEach var="size" items="${sizeList}">
                        <th>${size.sizeValue}</th>
                    </c:forEach>
                </tr>
            </thead>
            <tbody id="colorSizeBody">
                <!-- JS로 동적 생성 -->
            </tbody>
        </table>

        <div style="text-align:right; margin-top:10px;">
            <button type="button" id="registerColorSizeBtn">재고 등록</button>
        </div>
    </form>
</div>



</body>
</html>
