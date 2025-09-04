// ✅ 리뷰 페이지 전용 초기화
function reviewInit() {
  const modalEl = document.getElementById('reviewEditModal');
  if (modalEl) {
    modalEl.addEventListener('show.bs.modal', function (event) {
      const btn = event.relatedTarget;
      const reviewId = btn.getAttribute('data-review-id');
      const rating   = btn.getAttribute('data-rating');
      const content  = btn.getAttribute('data-content');

      document.getElementById('editReviewId').value = reviewId;
      document.getElementById('editRating').value   = rating ? Math.round(parseFloat(rating)) : 0;
      document.getElementById('editContent').value  = content ?? '';

      renderExistingFiles(reviewId);
    });
  }

  // ---------- 기존 파일 영역: 삭제 버튼 클릭 ----------
  document.querySelectorAll('button[data-action="delete-file"]').forEach(btn => {
    btn.addEventListener('click', async (e) => {
      const fileId   = btn.getAttribute('data-file-id');
      const reviewId = btn.getAttribute('data-review-id');
      const fname    = btn.getAttribute('data-fname') || '';

      if (!fileId) return;
      if (!confirm(`이미지(${fname || fileId})를 즉시 삭제할까요?`)) return;

      try {
        const params = new URLSearchParams({ fileId, reviewId });
        const res = await fetch(ctx + '/review/file/delete', {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
          body: params
        });

        if (!res.ok) throw new Error('삭제 요청 실패');
        const msg = await res.text();

        const cardCol = btn.closest('.col-md-4');
        if (cardCol) cardCol.remove();

        const holder = document.getElementById('file-data-' + reviewId);
        const span = holder?.querySelector(`.file-item[data-file-id="${fileId}"]`);
        if (span) span.remove();

        if (document.querySelectorAll('#existingFileList .col-md-4').length === 0) {
          document.getElementById('existingFileList').innerHTML =
            '<div class="text-muted">등록된 이미지가 없습니다.</div>';
        }

        alert(msg || '삭제되었습니다.');
      } catch (err) {
        alert('삭제 중 오류: ' + err.message);
      }
    });
  });

  // ---------- 수정 폼 제출 ----------
  $("#reviewEditForm").off("submit").on("submit", function (e) {
    e.preventDefault();
    const fd = new FormData(this);

    fetch(ctx + '/review/updateFull', { method: 'POST', body: fd })
      .then(r => r.text())
      .then(msg => {
        alert(msg || '저장되었습니다.');
        loadPage('review');
      })
      .catch(err => alert('수정 중 오류: ' + err));
  });

  // ---------- dot 클릭 ----------
  document.querySelectorAll(".dot").forEach(dot => {
    dot.addEventListener("click", () => {
      const card = dot.closest('.card');
      if (!card) return;

      const mainImg = card.querySelector('.review-main-img');
      if (!mainImg) return;

      const idx = parseInt(dot.getAttribute("data-img-idx"), 10);
      const images = mainImg.getAttribute("data-images").split(",");
      const target = images[idx]?.trim();

      if (target) {
        mainImg.src = target;
      }

      const dots = card.querySelectorAll('.dot');
      dots.forEach(d => d.style.background = "#bbb");
      dot.style.background = "#555";
    });
  });

  // ---------- 리뷰 삭제 ----------
  
  
}

