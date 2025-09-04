<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style>
    /* Custom styles for Q/A icons and specific backgrounds if Bootstrap doesn't have exact match */
    .qa-icon {
        display: inline-flex;
        justify-content: center;
        align-items: center;
        width: 28px;
        height: 28px;
        border-radius: 50%;
        font-weight: bold;
        color: white;
        font-size: 0.8rem;
        margin-right: 0.75rem;
        flex-shrink: 0;
    }
    .qa-icon-q { background-color: #007bff; }
    .qa-icon-a { background-color: #28a745; }

    .inquiry-content-bg {
        background-color: #f8f9fa;
        border: 1px solid #e9ecef;
        border-radius: 0.25rem;
        padding: 1rem;
        margin-top: 0.75rem;
        margin-bottom: 0.75rem;
    }
    .inquiry-answer-bg {
        background-color: #d4edda;
        border: 1px solid #c3e6cb;
        border-radius: 0.25rem;
        padding: 1rem;
        margin-top: 0.75rem;
    }

    /* Mimic table header row */
    .inquiry-header-row {
        display: flex;
        font-weight: bold;
        padding: 0.75rem 1.25rem;
        background-color: #e9ecef;
        border-bottom: 1px solid rgba(0, 0, 0, .125);
        border-top-left-radius: calc(.25rem - 1px);
        border-top-right-radius: calc(.25rem - 1px);
    }
    .inquiry-header-row > div, .inquiry-item-row > div {
        flex: 1;
        text-align: center;
    }
    .inquiry-header-row > div:nth-child(1), .inquiry-item-row > div:nth-child(1) { flex: 0.5; }  /* 번호 */
    .inquiry-header-row > div:nth-child(2), .inquiry-item-row > div:nth-child(2) { flex: 1; }    /* 상품 */
    .inquiry-header-row > div:nth-child(3), .inquiry-item-row > div:nth-child(3) { flex: 2; text-align: left; } /* 제목 */
    .inquiry-header-row > div:nth-child(4), .inquiry-item-row > div:nth-child(4) { flex: 1; }    /* 작성자 */
    .inquiry-header-row > div:nth-child(5), .inquiry-item-row > div:nth-child(5) { flex: 1.2; }  /* 날짜 */
    .inquiry-header-row > div:last-child, .inquiry-item-row > div:last-child {
        flex: 0.2; text-align: right; /* 화살표 */
    }

    .inquiry-item-row {
        display: flex;
        padding: 0.75rem 1.25rem;
        border-bottom: 1px solid rgba(0, 0, 0, .125);
        align-items: center;
        cursor: pointer;
        background-color: #fff;
    }
    .inquiry-item-row:hover { background-color: #f2f2f2; }
    .inquiry-item-row:last-child { border-bottom: none; }

    /* Arrow icon rotation */
    .collapse-toggle-icon { transition: transform 0.3s ease-in-out; }
    .inquiry-item-row[aria-expanded="true"] .collapse-toggle-icon { transform: rotate(180deg); }

    /* Status Badges */
    .status-badge {
        display: inline-block;
        padding: .25em .4em;
        font-size: 75%;
        font-weight: 700;
        line-height: 1;
        text-align: center;
        white-space: nowrap;
        vertical-align: baseline;
        border-radius: .25rem;
        color: #fff;
    }
    .status-확인중 { background-color: #6c757d; }
    .status-보류   { background-color: #ffc107; color: #212529; }
    .status-답변중 { background-color: #17a2b8; }
    .status-답변완료 { background-color: #28a745; }

    /* small utility margins for badges (if Bootstrap not loaded with ms-*) */
    .ms-1 { margin-left: .25rem; }
    .ms-2 { margin-left: .5rem; }
    .badge { vertical-align: middle; }
</style>

<div class="container mb-4">
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-secondary text-white">
            <strong>상품 문의 작성</strong>
        </div>

        <div class="card-body">
            <c:choose>
                <c:when test="${empty sessionScope.loginUser}">
                    <div class="alert alert-warning">
                        로그인 후 문의를 작성할 수 있습니다.
                    </div>
                </c:when>
                <c:otherwise>
                    <form action="/inquiry/submit" method="post" onsubmit="return confirm('등록하시겠습니까?')" class="p-4 border rounded bg-white shadow-sm">
                        <input type="hidden" name="productId" value="${param.productId}" />
                        <input type="hidden" name="userId" value="${sessionScope.loginUser.userId}" />

                        <h5 class="mb-4 fw-bold">문의 등록</h5>

                        <div class="mb-3">
                            <label for="inquiryTitle" class="form-label">문의 제목</label>
                            <input type="text" class="form-control" id="inquiryTitle" name="inquiryTitle" placeholder="제목을 입력하세요" required />
                        </div>

                        <div class="mb-3">
                            <label for="inquiryContent" class="form-label">문의 내용</label>
                            <textarea class="form-control" id="inquiryContent" name="inquiryContent" rows="6" placeholder="문의 내용을 입력하세요" required></textarea>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="isPublic" class="form-label">공개 여부</label>
                                <select class="form-select" id="isPublic" name="isPublic" required>
                                    <option value="1">공개</option>
                                    <option value="0">비공개</option>
                                </select>
                            </div>
                            <div class="col-md-6 text-end d-flex align-items-end justify-content-end">
                                <button type="submit" class="btn btn-primary px-4">등록</button>
                            </div>
                        </div>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<div class="container">
    <c:if test="${empty pInquiryList}">
        <div class="alert alert-info text-center" role="alert">
            문의 내역이 없습니다.
        </div>
    </c:if>

    <c:set var="currentInquiryId" value="0" />

    <div class="card shadow-sm mb-4">
        <div class="inquiry-header-row">
            <div>번호</div>
            <div>상품</div>
            <div>제목</div>
            <div>작성자</div>
            <div>날짜</div>
            <div>상태</div>
            <div></div>
        </div>

        <div class="card-body p-0">
            <c:forEach var="item" items="${pInquiryList}">
                <c:if test="${item.inquiryId ne currentInquiryId}">
                    <c:set var="currentInquiryId" value="${item.inquiryId}" />

                    <%-- 로그인 유저가 작성자인지 미리 체크 --%>
                    <c:set var="isOwner" value="${not empty sessionScope.loginUser && sessionScope.loginUser.userId == item.userId}" />

                    <!-- 요약 행 (클릭하여 상세 접기/펼치기) -->
                    <div class="inquiry-item-row"
                         data-toggle="collapse"
                         data-target="#collapseInquiry${item.inquiryId}"
                         aria-expanded="false"
                         aria-controls="collapseInquiry${item.inquiryId}">
                        <div>${item.inquiryId}</div>
                        <div>${item.productId}</div> <!-- TODO: 상품명으로 변경 필요 -->

                        <div>
                            <c:choose>
                                <c:when test="${item.isPublic == 0 && !isOwner}">
                                    <i class="fas fa-lock text-muted"></i>
                                    <span class="text-muted font-italic ms-1">비공개 문의입니다.</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-primary fw-bold">${item.inquiryTitle}</span>
                                    <c:if test="${isOwner}">
                                        <span class="badge" style="background-color: #FFA500; color: white; ms-2">나의 문의</span>
                                    </c:if>
                                    <c:if test="${item.isPublic == 0}">
                                        <span class="badge bg-secondary ms-1">비공개</span>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div>${item.userId}</div> <!-- TODO: 사용자명/마스킹 -->
                        <div>${item.inquiryDate}</div>

                        <div>
                            <c:choose>
                                <c:when test="${item.status eq '확인중'}"><span class="status-badge status-확인중">확인중</span></c:when>
                                <c:when test="${item.status eq '보류'}"><span class="status-badge status-보류">보류</span></c:when>
                                <c:when test="${item.status eq '답변중'}"><span class="status-badge status-답변중">답변중</span></c:when>
                                <c:when test="${item.status eq '답변완료'}"><span class="status-badge status-답변완료">답변완료</span></c:when>
                                <c:otherwise><span class="status-badge status-확인중">${item.status}</span></c:otherwise>
                            </c:choose>
                        </div>

                        <div class="collapse-toggle-icon">
                            <i class="fas fa-chevron-down"></i>
                        </div>
                    </div>

                    <!-- 상세(내용/답변) 영역 -->
                    <div id="collapseInquiry${item.inquiryId}" class="collapse">
                        <div class="px-4 pb-3">
                            <%-- 비공개 + 타인 = 비공개 안내 / 그 외는 내용과 답변 표시 --%>
                            <c:choose>
                                <c:when test="${item.isPublic == 0 && !isOwner}">
                                    <div class="alert alert-warning my-3" role="alert">
                                        이 문의는 비공개로 설정되어 있습니다.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!-- 문의 내용 -->
                                    <div class="d-flex align-items-start inquiry-content-bg">
                                        <span class="qa-icon qa-icon-q">Q</span>
                                        <p class="mb-0 text-dark">${item.inquiryContent}</p>
                                    </div>

                                    <%-- 답변 유무 체크 --%>
                                    <c:set var="hasAnswer" value="false" />
                                    <c:forEach var="answer" items="${pInquiryList}">
                                        <c:if test="${answer.inquiryId == item.inquiryId && not empty answer.answerContent}">
                                            <c:set var="hasAnswer" value="true" />
                                        </c:if>
                                    </c:forEach>

                                    <c:if test="${hasAnswer}">
                                        <div class="d-flex align-items-start inquiry-answer-bg">
                                            <span class="qa-icon qa-icon-a">A</span>
                                            <div class="flex-grow-1">
                                                <c:forEach var="answer" items="${pInquiryList}">
                                                    <c:if test="${answer.inquiryId == item.inquiryId && not empty answer.answerContent}">
                                                        <p class="mb-0 text-success">${answer.answerContent}</p>
                                                        <c:if test="${not empty answer.answerDate}">
                                                            <p class="text-muted small mt-2">
                                                                <strong>답변 날짜:</strong> ${answer.answerDate}
                                                            </p>
                                                        </c:if>
                                                        <hr class="my-2 border-success" />
                                                    </c:if>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>
