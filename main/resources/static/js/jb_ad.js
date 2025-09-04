
function loadRecentUncheckedInquiries() {
    fetch(`${ctx}/api/inquiries/recent-unchecked`)
        .then(r => r.json())
        .then(list => {
            let html = `
            <table class="table table-bordered table-notice-hover" style="text-align:center;">
                <thead class="thead-dark">
                    <tr>
                        <th style="width:30%">제목</th>
                        <th style="width:70%">내용</th>
                    </tr>
                </thead>
                <tbody>`;
            if (list.length === 0) {
                html += `<tr>
                    <td colspan="2" class="text-center text-muted">미확인 문의가 없습니다.</td>
                </tr>`;
            } else {
                list.forEach(inq => {
                    html += `
                    <tr class="recent-inquiry-row" data-inquiry-id="${inq.inquiryId}" style="cursor:pointer">
                        <td class="font-weight-bold text-truncate">${inq.inquiryTitle}</td>
                        <td class="text-truncate" style="max-width:350px;">${inq.inquiryContent || '-'}</td>
                    </tr>`;
                });
            }
            html += `</tbody></table>`;
            document.getElementById('recentInquiryList').innerHTML = html;

            // 테이블 행 클릭 이벤트 바인딩
            document.querySelectorAll('.recent-inquiry-row').forEach(row => {
                row.addEventListener('click', function(){
                    const inquiryId = this.getAttribute('data-inquiry-id');
                    showInquiryDetail(inquiryId); // 기존 함수 그대로 사용!
                });
            });
        });
}


$(document).off('click', '.logout-link')
           .on('click', '.logout-link', function(e) {
  e.preventDefault(); // 기본 이동 막기

  if (confirm('로그아웃 하시겠습니까?')) {
    // 그냥 페이지 이동
    window.location.href = $(this).attr('href');
  }
});

