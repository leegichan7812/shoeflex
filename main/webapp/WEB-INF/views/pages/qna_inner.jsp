<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table class="table">
  <thead>
    <tr>
      <th>번호</th>
      <th>카테고리</th>
      <th>제목</th>
      <th>작성일</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="qna" items="${qnaList}" varStatus="st">
      <tr>
        <td>${st.count}</td>
        <td>${qna.category}</td>
        <td>${qna.title}</td>
        <td><fmt:formatDate value="${qna.createdAt}" pattern="yyyy-MM-dd"/></td>
      </tr>
    </c:forEach>
    <c:if test="${empty qnaList}">
      <tr><td colspan="4">검색 결과가 없습니다.</td></tr>
    </c:if>
  </tbody>
</table>
