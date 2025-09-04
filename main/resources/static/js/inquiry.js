function myFunction() {
	console.log("jnquiry.js 함수 실행");
}

// ctx(컨텍스트 경로) 안전하게 선언
const ctx = window.ctx || '';

let currentViewingInquiryId = null;
let currentPage = 1;     // 현재 페이지 번호 전역 변수
let currentStatus = '';  // 현재 상태 전역 변수

function initInquiryPage() {
    const updateBtn = document.getElementById('updateAnswerBtn');
    if (updateBtn) updateBtn.addEventListener('click', saveOrUpdateAnswer);

    const deleteBtn = document.getElementById('deleteAnswerBtn');
    if (deleteBtn) deleteBtn.addEventListener('click', deleteAnswer);

    const statusFilter = document.getElementById('statusFilter');
    if (statusFilter) statusFilter.addEventListener('change', e => {
        loadStatuses(1, e.target.value);
    });

    const modalElement = document.getElementById('inquiryDetailModal');
    if (modalElement) {
        modalElement.addEventListener('hidden.bs.modal', () => {
            if (currentViewingInquiryId) {
                updateViewerAdminStatus(currentViewingInquiryId, null);
                currentViewingInquiryId = null;
            }
        });
    }

    // 필요 시 최초 목록 호출
    if (document.getElementById('statusTableBody')) {
        loadStatuses(1, '');
    }
}

// 문의 상태 배지 생성 함수
function createStatusBadge(status) {
    const span = document.createElement('span');
    span.classList.add('status-badge');
    switch (status) {
        case '확인중': span.classList.add('status-확인중'); break;
        case '답변중': span.classList.add('status-답변중'); break;
        case '답변완료': span.classList.add('status-답변완료'); break;
        case '관리자 확인중': span.classList.add('status-관리자-확인중'); break;
        default: span.style.backgroundColor = 'gray';
    }
    span.textContent = status;
    return span;
}

// 테이블 렌더링 함수
function renderTable(list) {
    const tbody = document.getElementById('statusTableBody');
    tbody.innerHTML = '';
    const rowCount = 10;
    list.forEach((item, idx) => {
        const tr = document.createElement('tr');
        tr.dataset.inquiry = JSON.stringify(item);
        // 직접 스타일로 배경색 지정
        if (idx % 2 === 0) {
            tr.style.backgroundColor = '#D3D3D3';
        } else {
            tr.style.backgroundColor = '#B0B0B0';
        }
        // 문의 ID
        const tdId = document.createElement('td');
        tdId.textContent = item.inquiryId;
        tr.appendChild(tdId);
        // 제목
        const tdTitle = document.createElement('td');
        tdTitle.textContent = item.inquiryTitle || '-';
        tr.appendChild(tdTitle);
        // 내용
        const tdContent = document.createElement('td');
        const contentText = item.inquiryContent || '';
        tdContent.textContent = contentText.length > 40 ? contentText.substring(0, 40) + '...(생략)' : contentText;
        tr.appendChild(tdContent);
        // 상태
        const tdStatus = document.createElement('td');
        let displayStatus = item.status;
        if (item.viewerAdminId) {
            displayStatus = '관리자 확인중';
            tr.style.backgroundColor = '#fff3cd'; // 노랑 강조
        }
        tdStatus.appendChild(createStatusBadge(displayStatus));
        tr.appendChild(tdStatus);
        // 확인 관리자
        const tdViewer = document.createElement('td');
        tdViewer.textContent = item.viewerAdminId || '-';
        tr.appendChild(tdViewer);
        tr.addEventListener('click', () => {
            showInquiryDetail(item.inquiryId);
        });
        tbody.appendChild(tr);
    });
    // 빈 줄 추가 (항상 10줄)
    for (let i = list.length; i < rowCount; i++) {
        const tr = document.createElement('tr');
        for (let j = 0; j < 5; j++) {
            const td = document.createElement('td');
            td.innerHTML = '&nbsp;';
            td.style.border = 'none';
            tr.appendChild(td);
        }
        tbody.appendChild(tr);
    }
}

// 페이지네이션 렌더링
function renderPagination(totalCount, page, status) {
  const pageSize  = 10;  // 한 페이지 당 행 수
  const blockSize = 10;  // 페이지 번호 묶음 크기(10페이지 단위)

  const totalPage   = Math.max(1, Math.ceil(totalCount / pageSize));
  const currentPage = Math.min(Math.max(1, page || 1), totalPage);
  const currentBlk  = Math.ceil(currentPage / blockSize);
  const startPage   = (currentBlk - 1) * blockSize + 1;
  const endPage     = Math.min(startPage + blockSize - 1, totalPage);

  const ul = document.getElementById('pagination');
  if (!ul) return;

  // 페이지 정보
  const pageInfoContainer = document.getElementById('page-info');
  if (pageInfoContainer) {
    pageInfoContainer.innerHTML =
      `<div class="text-center text-secondary" style="margin-bottom:10px;">
         총 <strong>${totalPage}</strong> 페이지
       </div>`;
  }

  ul.innerHTML = '';

  // li+a 생성 헬퍼
  const append = (targetPage, label, { disabled=false, active=false, hidden=false } = {}) => {
    if (hidden) return; // 🔴 숨김이면 만들지 않음

    const li = document.createElement('li');
    li.className = 'page-item' + (disabled ? ' disabled' : '') + (active ? ' active' : '');

    const a = document.createElement('a');
    a.className = 'page-link';
    a.href = '#';
    a.innerText = label;

    if (!disabled && !active) {
      a.addEventListener('click', function (e) {
        e.preventDefault();
        loadStatuses(targetPage, status);
      });
    }

    li.appendChild(a);
    ul.appendChild(li);
  };

  // 데이터 없으면 종료
  if (totalCount === 0) return;

  // 🔹 처음 / 이전 블록 (1페이지/첫 블록이면 숨김)
  append(1, '처음', {
    hidden: currentPage === 1
  });

  append(Math.max(1, startPage - 1), '이전', {
    hidden: startPage === 1
  });

  // 🔹 번호들
  for (let p = startPage; p <= endPage; p++) {
    append(p, String(p), { active: p === currentPage });
  }

  // 🔹 다음 블록 / 마지막 (마지막 블록/마지막 페이지이면 숨김)
  append(Math.min(totalPage, endPage + 1), '다음', {
    hidden: endPage === totalPage
  });

  append(totalPage, '마지막', {
    hidden: currentPage === totalPage
  });
}


function loadStatuses(page, status) {
    if (!document.getElementById('statusTableBody')) {
        console.log("statusTableBody 없음 → 메인페이지라서 loadStatuses 실행 안함");
        return;
    }

    const pageSize = 10;
    currentPage = page || 1;
    currentStatus = (typeof status === 'undefined') ? '' : status;

    const params = new URLSearchParams({
        page: currentPage,
        pageSize: pageSize,
        status: currentStatus
    });

    fetch(`${ctx}/api/inquiries/statuses?${params.toString()}`)
        .then(r => r.json())
        .then(res => {
            renderTable(res.list);
            renderPagination(res.totalCount, currentPage, currentStatus);
        })
        .catch(err => console.error('loadStatuses error:', err));
}

// 상세 모달 표시 함수
function showInquiryDetail(inquiryId) {
    fetch(`${ctx}/api/inquiries/detail?inquiryId=${inquiryId}`)
      .then(r => r.json())
      .then(inquiry => {
          // 받아온 상세 데이터로 모달 필드 채우기
          document.getElementById('detailImageUrl').src = inquiry.imageUrl || '';
          document.getElementById('detailProductName').textContent = inquiry.productName || '-';
          document.getElementById('detailInquiryTitle').textContent = inquiry.inquiryTitle || '-';
          document.getElementById('detailInquiryContent').textContent = inquiry.inquiryContent || '-';
          document.getElementById('detailInquiryDate').textContent = inquiry.inquiryDate ? new Date(inquiry.inquiryDate).toLocaleString() : '-';
          document.getElementById('detailIsPublic').textContent = inquiry.isPublic ? '예' : '아니오';
          document.getElementById('detailViewerAdminId').textContent = inquiry.viewerAdminId || '-';
          document.getElementById('detailAnswerContent').textContent = inquiry.answerContent || '-';
          document.getElementById('detailAnswerDate').textContent = inquiry.answerDate ? new Date(inquiry.answerDate).toLocaleString() : '-';
          document.getElementById('answerInput').value = inquiry.answerContent || '';

          currentViewingInquiryId = inquiry.inquiryId;
          updateViewerAdminStatus(currentViewingInquiryId, 1); // 실제 관리자 ID로 교체

          const modal = new bootstrap.Modal(document.getElementById('inquiryDetailModal'));
          modal.show();
      });
}



	// 확인 관리자 상태를 업데이트하는 함수 (누가 문의를 보고 있는지)
	    function updateViewerAdminStatus(inquiryId, adminId) {
	        const params = new URLSearchParams();
	        params.append('inquiryId', inquiryId);
	        if (adminId !== null) {
	            params.append('adminId', adminId);
	        }

	        fetch(`${ctx}/api/inquiries/updateViewerAdmin`, {
	            method: 'POST',
	            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	            body: params.toString()
	        }).then(() => loadStatuses(currentPage, currentStatus)) // 상태 업데이트 후 목록 다시 로드
	          .catch(error => console.error('Error updating viewer admin status:', error));
	    }

	    // 답변을 저장하거나 수정하는 함수
	    function saveOrUpdateAnswer() {
	        const answerContent = document.getElementById('answerInput').value.trim();

	        if (!answerContent) {
	            alert('답변을 입력하세요.');
	            return;
	        }

	        const params = new URLSearchParams();
	        params.append('inquiryId', currentViewingInquiryId);
	        params.append('answerContent', answerContent);

	        fetch(`${ctx}/api/inquiries/answer`, {
	            method: 'POST',
	            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	            body: params.toString()
	        }).then(() => {
	            alert('답변이 저장되었습니다.');
	            loadStatuses(); // 목록 갱신
	            bootstrap.Modal.getInstance(document.getElementById('inquiryDetailModal')).hide(); // 모달 닫기
	        })
	        .catch(error => console.error('Error saving/updating answer:', error));
	    }

	    // 답변을 삭제하는 함수
	    function deleteAnswer() {
	        if (!confirm('정말 답변을 삭제하시겠습니까?')) return;

	        const params = new URLSearchParams();
	        params.append('inquiryId', currentViewingInquiryId);

	        fetch(`${ctx}/api/inquiries/answerDelete`, {
	            method: 'POST',
	            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	            body: params.toString()
	        }).then(() => {
	            alert('답변이 삭제되었습니다.');
	            loadStatuses(); // 목록 갱신
	            bootstrap.Modal.getInstance(document.getElementById('inquiryDetailModal')).hide(); // 모달 닫기
	        })
	        .catch(error => console.error('Error deleting answer:', error));
	    }
