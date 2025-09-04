<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 조회</title>
<style>
    table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }
    th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
    .gray { background-color: #eee; color: #aaa; }
    .clickable { cursor: pointer; background-color: #fafafa; }
</style>
</head>
<body>
<div class="row">
	<div class="col-6">
		<h2>상품 조회 / 수정 / 삭제</h2>
	</div>

<!-- 필터 영역 -->
	<div class="col-6">
		<div style="float:right; margin-bottom:10px;">
		    <input type="text" id="searchName" placeholder="상품명 검색" style="padding:5px; width:150px;" value="${filterName}">
		
		    <select id="brandFilter" style="padding:5px;">
		        <option value="">브랜드 전체</option>
		        <c:forEach var="brand" items="${brandList}">
		            <option value="${brand.brandName}" <c:if test="${brand.brandName == filterBrand}">selected</c:if>>${brand.brandName}</option>
		        </c:forEach>
		    </select>
		
		    <select id="categoryFilter" style="padding:5px;">
		        <option value="">카테고리 전체</option>
		        <c:forEach var="cat" items="${categoryList}">
		            <option value="${cat.categoryName}" <c:if test="${cat.categoryName == filterCategory}">selected</c:if>>${cat.categoryName}</option>
		        </c:forEach>
		    </select>
		
		    <button id="resetFilterBtn" style="padding:5px 10px; display:none; margin-left:5px;">필터 해제</button>
		</div>
	</div>
</div>

<table id="productTable" style="width:100%;">
    <thead>
        <tr>
            <th style="width:10%;">이미지</th>
            <th style="width:40%;">상품명</th>
            <th style="width:10%;">브랜드</th>
            <th style="width:10%;">카테고리</th>
            <th style="width:10%;">재고</th>
            <th style="width:10%;">일시중단</th>
            <th style="width:10%;">삭제</th>
        </tr>
    </thead>
    <tbody id="productTbody">
        <c:forEach var="p" items="${productList}">
            <tr class="product-row" data-product-id="${p.productId}">
            	<td><img src="${p.imageUrl}" width="100px" height="100px"></td>
                <td>
				    ${p.productName}
				    <c:if test="${p.productStatus eq '일시중단'}">
				        <span style="color:#e67e22; font-size:14px;">(일시중단 중)</span>
				    </c:if>
				</td>
                <td>${p.brandName}</td>
                <td>${p.categoryName}</td>                
                <td>${p.productTotalStock}</td>
                <td>
					<c:choose>
						<c:when test="${p.productStatus eq '일시중단'}">
							<button class="pause-product-btn btn btn-success"
								data-product-id="${p.productId}"
								style="color:#fff; font-weight:bold;">
								재판매
							</button>
						</c:when>
						<c:otherwise>
							<button class="pause-product-btn btn btn-warning"
								data-product-id="${p.productId}"
								style="color:#fff; font-weight:bold;">
								일시중단
							</button>
						</c:otherwise>
					</c:choose>
				</td>
                <td>
				    <button class="del-product-btn" data-product-id="${p.productId}">
				        &times;
				    </button>
				</td>
            </tr>
            <tr class="detail-row" style="display:none;">
                <td colspan="7" class="detail-area"></td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<!-- 페이징 -->
<%
    int totalPage = Integer.parseInt(String.valueOf(request.getAttribute("totalPage")));
    int currentPage = Integer.parseInt(String.valueOf(request.getAttribute("currentPage")));
    int pageBlock = 10;
    int startPage = ((currentPage - 1) / pageBlock) * pageBlock + 1;
    int endPage = startPage + pageBlock - 1;
    if (endPage > totalPage) endPage = totalPage;
%>

<nav aria-label="상품목록 페이징" style="margin-top:20px;">
    <ul class="pagination justify-content-center">

        <!-- 처음 -->
		<li class="page-item" style="<%= (startPage == 1) ? "display:none;" : "" %>">
		    <button class="page-btn page-link" data-page="1">처음</button>
		</li>
		
		<!-- 이전 -->
		<li class="page-item" style="<%= (startPage == 1) ? "display:none;" : "" %>">
		    <button class="page-btn page-link" data-page="<%= startPage - 1 %>">이전</button>
		</li>

        <%-- 페이지 번호 (active 적용) --%>
        <c:forEach var="i" begin="<%= startPage %>" end="<%= endPage %>">
            <c:choose>
                <c:when test="${i == currentPage}">
                    <li class="page-item active">
                        <button class="page-btn page-link" data-page="${i}">${i}</button>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <button class="page-btn page-link" data-page="${i}">${i}</button>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <%-- 다음(>) --%>
		<li class="page-item" style="<%= (endPage == totalPage) ? "display:none;" : "" %>">
		    <button class="page-btn page-link" data-page="<%= endPage + 1 %>">다음</button>
		</li>
		
		<%-- 마지막(>>) --%>
		<li class="page-item" style="<%= (endPage == totalPage) ? "display:none;" : "" %>">
		    <button class="page-btn page-link" data-page="<%= totalPage %>">마지막</button>
		</li>

    </ul>
</nav>
<div class="text-center text-secondary" style="margin-bottom:10px;">
    총 <strong>${totalPage}</strong> 페이지
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>



</body>
</html>
