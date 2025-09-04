<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="path" value="${pageContext.request.contextPath }"/>
<fmt:requestEncoding value="UTF-8"/>

<h3 class="mb-3 h6 text-uppercase text-black d-block">『 브랜드 』</h3>
<ul style="list-style: none;">
  <li>
    <div class="row row-cols-5 gy-1 text-center">
      <c:forEach var="brand" items="${brandFilterList}">
        <div class="col" style="box-shadow: 0 1px 2px rgba(0,0,0,0.3);">
          <a class="dropdown-item d-flex flex-column align-items-center brand-filter"
             href="#"
             data-brand-id="${brand.brandId}">
            <img src="${pageContext.request.contextPath}/${brand.brandImg}" 
                 alt="${brand.brandName} 이미지" width="50" height="50">
            <span style="font-size: 12px;">${brand.brandName}</span>
          </a>
        </div>
      </c:forEach>
    </div>
  </li>
</ul>
