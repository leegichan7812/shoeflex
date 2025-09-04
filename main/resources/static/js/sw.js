// 전역 모달 인스턴스
 let reviewModal = null;

 // 페이지 로드 시 모달 인스턴스 준비 (DOM에 모달이 있을 때)
 $(document).ready(function () {
   const modalEl = document.getElementById('reviewWriteModal');
   if (modalEl) {
     reviewModal = new bootstrap.Modal(modalEl);
   }
 });

 // [클릭] 리뷰 작성 버튼 (동적 로드 대비 - 위임 바인딩)
 $(document).on('click', '.btn-open-review', function () {
   // 모달 인스턴스가 아직 없으면 다시 시도
   if (!reviewModal) {
     const modalEl = document.getElementById('reviewWriteModal');
     if (!modalEl) {
       console.error('reviewWriteModal 요소를 찾을 수 없습니다.');
       return;
     }
     reviewModal = new bootstrap.Modal(modalEl);
   }

   const $btn = $(this);

   // 버튼에 실은 데이터
   const userId      = $btn.data('user-id');
   const productSize = $btn.data('product-size'); // 보여주기용
   const pcsId       = $btn.data('pcs-id') || ''; // 실제 insert 컬럼(pcsi)

   // 같은 행에서 상품명 가져와 타이틀 만들기 (필요 시 selector 조정)
   const $row = $btn.closest('.d-flex.border-bottom');
   let productTitle = $row.find('h4.fw-bold').text().trim();
   if (productSize) productTitle += ` / 사이즈 ${productSize}`;

   // 모달 히든/보여주기 값 세팅
   $('#rv-user-id').val(userId);
   $('#rv-pcs-id').val(pcsId);                // name="pcsi" 여야 DAO와 매칭됨 (폼에서 name 수정했는지 확인!)
   $('#rv-product-title').val(productTitle);

   // 모달 오픈
   reviewModal.show();
 });

 // [제출] 리뷰 폼
 $(document).on('submit', '#reviewForm', function (e) {
   e.preventDefault();

   // 간단한 클라 검증 (선택)
   const rating = $(this).find('select[name="rating"]').val();
   const content = $(this).find('textarea[name="content"]').val()?.trim();
   const userId = $('#rv-user-id').val();
   const pcsi = $('#rv-pcs-id').val();

   if (!userId) {
     alert('로그인이 필요합니다.');
     return;
   }
   if (!pcsi) {
     // pcsi가 없으면 DB insert 실패하므로 안내
     alert('상품 정보가 올바르지 않습니다. 다시 시도해주세요.');
     return;
   }
   if (!rating) {
     alert('별점을 선택해주세요.');
     return;
   }
   if (!content) {
     alert('리뷰 내용을 입력해주세요.');
     return;
   }

   const fd = new FormData(this);

   $.ajax({
     url: '/review/insert',
     type: 'POST',
     data: fd,
     processData: false,
     contentType: false,
     success: function (res) {
       reviewModal.hide();
       // 파일 input 초기화
       $('#rv-files').val('');
       // 폼 리셋
       $('#reviewForm')[0].reset();

       alert(res || '리뷰가 등록되었습니다.');
       // 필요 시 페이지 일부 갱신
       // loadPage('review');
       // 또는 loadPage('myPage');
     },
     error: function (xhr) {
       const msg = xhr?.responseText || '리뷰 등록 중 오류가 발생했습니다.';
       alert(msg);
     }
   });
 });