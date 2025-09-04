<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="container mt-5">
    <div class="mb-4 d-flex align-items-center justify-content-between">
        <h2 class="fw-bold">
            <i class="bi bi-card-list"></i> 문의 상태 목록
        </h2>
        <!-- 상태 필터 드롭다운 -->
        <select id="statusFilter" class="form-select form-select-sm" style="width:180px;">
            <option value="">전체</option>
            <option value="미확인">미확인</option>
            <option value="답변중">답변중</option>
            <option value="답변완료">답변완료</option>
        </select>
    </div>
    <table class="table table-hover" id="statusTable" style="text-align:center;" >
        <thead class="table-secondary">
            <tr>
                <th width="10%">번호</th>
                <th width="25%">제목</th>
                <th width="40%">내용</th>
                <th width="10%">상태</th>
                <th width="15%">담당 관리자</th>
            </tr>
        </thead>
        <tbody id="statusTableBody"></tbody>
    </table>
    <nav aria-label="Page navigation" class="mt-4">
	    <ul class="pagination justify-content-center" id="pagination"></ul>
	
	    <!-- 📌 총 게시글 수 표시 영역 (페이지네이션 아래) -->
	    <div id="page-info" class="text-center text-secondary mt-2"></div>
	</nav>
	
</div>

<div class="modal fade" id="inquiryDetailModal" tabindex="-1" aria-labelledby="inquiryDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="inquiryDetailModalLabel">문의 상세 정보</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <div class="modal-body">
                <div class="modal-subtitle">📌 문의 정보</div>
                <div class="modal-body-section" style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; align-items: start;">
                    <div style="text-align: center;">
                        <img id="detailImageUrl" src="" alt="문의 이미지" style="max-width: 100%; height: auto; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.15);">
                    </div>
                    <div>
                        <p><strong>제품명:</strong> <span id="detailProductName"></span></p>
                        <p><strong>문의 제목:</strong> <span id="detailInquiryTitle"></span></p>
                        <p><strong>문의 날짜:</strong> <span id="detailInquiryDate"></span></p>
                        <p><strong>답변 날짜:</strong> <span id="detailAnswerDate"></span></p>
                        <p><strong>공개 여부:</strong> <span id="detailIsPublic"></span></p>
                        <p><strong>확인 관리자 ID:</strong> <span id="detailViewerAdminId"></span></p>
                        </div>
                </div>

                <div class="modal-subtitle">📝 문의 내용</div>
                <div class="highlight-box">
                    <p id="detailInquiryContent" style="margin: 0; color:black;"></p>
                </div>

                <div class="modal-subtitle">💬 답변 정보</div>
                <div class="modal-body-section">
                    </div>
                <div class="highlight-box">
                    <p id="detailAnswerContent" style="margin: 0; color:black;"></p>
                </div>

                <div class="modal-subtitle">✍️ 답변 작성 / 수정</div>
                <div class="mb-3">
                    <textarea id="answerInput" class="form-control" rows="4" placeholder="답변을 입력하세요." style="color:black;" ></textarea>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-danger" id="deleteAnswerBtn">답변 삭제</button>
                <button type="button" class="btn btn-primary" id="updateAnswerBtn">답변 저장</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

