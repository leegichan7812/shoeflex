<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="mt-4" style="background-color:white;width:100%;padding:10px;border-radius:10px;">
    <!-- 제목 + 검색을 flex 한 줄에 -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0" style="color:black;">예외 로그 목록</h2>

        <!-- 검색 -->
        <form class="d-flex align-items-center" onsubmit="return false;">
            <input type="text" id="gcSearchKeyword" class="form-control me-2"
                   style="background:white;width:250px;margin-right:10px;color:black;"
                   placeholder="클래스명, 메서드명, 메시지 검색" value="${keyword}" />
            <button type="button" class="btn btn-dark"
                    style="background:white;color:black;padding:10px 15px;"
                    onclick="gcSubmitSearch()">검색</button>
        </form>
    </div>

    <!-- 로그 목록 -->
    <div class="table-responsive" style="overflow-x:auto; max-width:100%;">
    <table class="table table-bordered table-notice-hover" style="min-width: 1000px; table-layout: fixed; word-break: break-all;">
        <thead class="table-dark text-center">
            <tr>
                <th style="width:10%;color:white;">발생시각</th>
                <th style="width:25%;color:white;">클래스명</th>
                <th style="width:10%;color:white;">메서드</th>
                <th style="width:15%;color:white;">예외타입</th>
                <th style="width:10%;color:white;">에러유형</th>
                <th style="width:10%;color:white;">수행시간(ms)</th>
                <th style="width:10%;color:white;">사용자 이메일</th>
                <th style="width:10%;color:white;">사용자명</th>
            </tr>
        </thead>
        <tbody class="text-center">
            <c:forEach var="log" items="${logList}">
                <tr class="log-row" data-log-id="${log.logId}" style="cursor:pointer;">
                    <td style="overflow: hidden; text-overflow: ellipsis;"><fmt:formatDate value="${log.occurredAt}" pattern="yyyy-MM-dd"/></td>
                    <td style="overflow: hidden; text-overflow: ellipsis;">${log.className}</td>
                    <td style="overflow: hidden; text-overflow: ellipsis;">${log.methodName}</td>
                    <td style="overflow: hidden; text-overflow: ellipsis;">${log.exceptionType}</td>
                    <td style="overflow: hidden; text-overflow: ellipsis;">${log.errorType}</td>
                    <td style="overflow: hidden; text-overflow: ellipsis;">${log.executionTimeMs}</td>
                    <td style="overflow: hidden; text-overflow: ellipsis;">${log.userEmail}</td>
                    <td style="overflow: hidden; text-overflow: ellipsis;">${log.userName}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty logList}">
                <tr>
                    <td colspan="8" class="text-center text-muted">예외 로그가 없습니다.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
    </div>

    <!-- 페이징 -->
    <div class="d-flex justify-content-center mt-4">
        <nav>
            <ul class="pagination">
	            <!-- 처음 블록 -->
				<c:if test="${curPage > 1}">
				    <li class="page-item">
				        <a class="page-link" href="javascript:loadAdminPage('/exceptionLogs?curPage=1&keyword=${keyword}')">처음</a>
				    </li>
				</c:if>
				
				<!-- 이전 블록 -->
				<c:if test="${startPage > 1}">
				    <li class="page-item">
				        <a class="page-link" href="javascript:loadAdminPage('/exceptionLogs?curPage=${startPage - 1}&keyword=${keyword}')">이전</a>
				    </li>
				</c:if>
				
				<!-- 페이지 번호 -->
				<c:forEach begin="${startPage}" end="${endPage}" var="p">
				    <li class="page-item ${p == curPage ? 'active' : ''}">
				        <a class="page-link" href="javascript:loadAdminPage('/exceptionLogs?curPage=${p}&keyword=${keyword}')">${p}</a>
				    </li>
				</c:forEach>
				
				<!-- 다음 블록 -->
				<c:if test="${endPage < totalPage}">
				    <li class="page-item">
				        <a class="page-link" href="javascript:loadAdminPage('/exceptionLogs?curPage=${endPage + 1}&keyword=${keyword}')">다음</a>
				    </li>
				</c:if>
				
				<!-- 마지막 블록 -->
				<c:if test="${curPage < totalPage}">
				    <li class="page-item">
				        <a class="page-link" href="javascript:loadAdminPage('/exceptionLogs?curPage=${totalPage}&keyword=${keyword}')">마지막</a>
				    </li>
				</c:if>
            </ul>
        </nav>
        
        
    </div>
    <!-- 📌 총 페이지 수 표시 -->
	<div class="text-center" style="color:black; margin-bottom:10px;">
	    총 <strong>${totalPage}</strong> 페이지
	</div>
</div>

<!-- 예외 로그 상세 모달 -->
<div class="modal fade" id="logDetailModal" tabindex="-1" aria-labelledby="logDetailModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-scrollable">
    <div class="modal-content bg-light border-dark">
      <div class="modal-header bg-dark text-white">
        <h5 class="modal-title" id="logDetailModalLabel">예외 로그 상세 정보</h5>
			<button
			  type="button"
			  class="close text-white"
			  data-bs-dismiss="modal"
			  aria-label="닫기"
			>
	    	<span aria-hidden="true">&times;</span>
	  		</button>
      </div>
      <div class="modal-body" style="font-size:15px;color:black;">
        <!-- JavaScript로 내용 삽입 -->
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-bs-dismiss="modal">닫기</button>
      </div>
    </div>
  </div>
</div>
