(function(){
  // 컨트롤러의 @RequestMapping 경로와 일치시켜 주세요.
  // 고객 화면이면 '/cust/after-sales' 로 바꾸세요.
  const CER_BASE = '/after-sales';

  let lastCerTrigger = null;

  // 수거주소 셀렉터
  const PICKUP_GRP   = '#cer-pickupAddress-group';
  const PICKUP_INPUT = '#cer-pickupAddress';

  function getCerModalEl() {
    return document.getElementById('cerModal');
  }
  function openCerModal() {
    const el = getCerModalEl();
    if (!el) return;
    const modal = bootstrap.Modal.getOrCreateInstance(el);
    if (!el.classList.contains('show')) modal.show();
  }
  function closeCerModal() {
    const el = getCerModalEl();
    if (!el) return;
    const modal = bootstrap.Modal.getOrCreateInstance(el);
    if (el.classList.contains('show')) modal.hide();
  }

  // 반품/교환 여부
  function needPickupByType(type){
    return type === '반품' || type === '교환';
  }

  // 요청유형 변경 → 수거주소 토글
  $(document).on('change', '#cer-reqType', function(){
    const type = $(this).val();
    const needPickup = needPickupByType(type);
    const $grp = $(PICKUP_GRP);
    const $inp = $(PICKUP_INPUT);

    if (needPickup) {
      $grp.removeClass('d-none');
      $inp.prop('disabled', false);

      // 기본 주소 자동 채움 (비어있을 때만)
      const modal = getCerModalEl();
      const defaultAddr = (modal?.dataset?.defaultAddress || '').trim();
      if (!$inp.val() && defaultAddr) $inp.val(defaultAddr);
    } else {
      // 취소일 때 숨기고 전송 막기
      $grp.addClass('d-none');
      $inp.prop('disabled', true).val('');
    }
  });

  // 모달 표시/숨김 시 포커스 관리
  $(document).on('shown.bs.modal', '#cerModal', function () {
    const first = this.querySelector('#cer-reqType');
    if (first) first.focus();
  });
  $(document).on('hidden.bs.modal', '#cerModal', function () {
    if (lastCerTrigger && document.body.contains(lastCerTrigger)) {
      lastCerTrigger.focus();
    } else if (document.activeElement && this.contains(document.activeElement)) {
      document.activeElement.blur();
    }
  });

  // 주문항목 조회(상품 요약/pcsId 보강)
  function fetchOrderItem(orderItemId) {
    return $.get(CER_BASE + '/api/item', { orderItemId: orderItemId });
  }

  // 버튼 클릭 → 모달 오픈 + 값 세팅 + (필요시) 조회
  $(document).on('click', '.btn-open-cer', function(){
    lastCerTrigger = this;

    const orderItemId = $(this).data('order-item-id');
    const pcsIdBtn    = $(this).data('product-color-size-id'); // 있을 수도/없을 수도
    if (!orderItemId) { alert('주문항목번호가 없습니다.'); return; }

    // 초기화
    $('#cer-orderItemId').val(orderItemId);
    $('#cer-productColorSizeId').val(pcsIdBtn || '');
    $('#cer-reqType').val('');
    $('#cer-reasonCode').val('');
    $('#cer-reasonDetail').val('');
    $('#cer-productSummary').val('');

    // 수거주소 초기 상태: 숨김 + 비활성 + 공백
    $(PICKUP_GRP).addClass('d-none');
    $(PICKUP_INPUT).prop('disabled', true).val('');

    // 조회하여 요약 채우기(+ pcsId 보강)
    fetchOrderItem(orderItemId)
      .done(function(res){
        if (!$('#cer-productColorSizeId').val()) {
          $('#cer-productColorSizeId').val(res.productColorSizeId || '');
        }
        const summary = '제품명: ' + (res.name||'-')
                      + ' / 색상: ' + (res.colorNameKor||'-')
                      + ' / 사이즈: ' + (res.sizeValue||'-')
                      + ' | 수량: ' + (res.quantity||1);
        $('#cer-productSummary').val(summary);
      })
      .fail(function(xhr){
        alert(xhr.responseText || '주문항목 조회 실패');
      })
      .always(openCerModal); // 조회 끝나고 모달 오픈(포커스/ARIA 안전)
  });
  
  $(".btn-open-cer").each(function() {
    const $btn = $(this);
    const reqType = $btn.data("req-type");
    const reqStatus = $btn.data("req-status");

    if (reqStatus === '신청' || reqStatus === '승인') {
      $btn.prop("disabled", true);
      if (reqType === '취소') {
        $btn.text("취소 진행중");
      } else if (reqType === '반품') {
        $btn.text("반품 진행중");
      } else if (reqType === '교환') {
        $btn.text("교환 진행중");
      } else {
        $btn.text("신청 진행중");
      }
    }
  });

  // 신청하기 클릭 → 검증 → 등록
  $(document).on('click', '#cer-submitBtn', function(){
    const $btn = $(this);
    if ($btn.prop('disabled')) return;

    const reqType = $('#cer-reqType').val();
    const needPickup = needPickupByType(reqType);

    const payload = {
      orderItemId:        $('#cer-orderItemId').val(),
      productColorSizeId: $('#cer-productColorSizeId').val(),
      reqType:            reqType,
      reasonCode:         $('#cer-reasonCode').val(),
      reasonDetail:       $('#cer-reasonDetail').val()
    };

    // 수거주소는 반품/교환일 때만 포함
    if (needPickup) {
      const pickup = $(PICKUP_INPUT).val().trim();
      if (!pickup) { alert('수거주소를 입력하세요.'); return; }
      payload.pickupAddress = pickup;
    }

    if (!payload.orderItemId)        { alert('주문항목을 확인하세요.'); return; }
    if (!payload.productColorSizeId) { alert('상품 옵션 식별값이 없습니다. 다시 조회해주세요.'); return; }
    if (!payload.reqType)            { alert('요청유형을 선택하세요.'); return; }

    $btn.prop('disabled', true);

    $.ajax({
      url: CER_BASE + '/submit',
      method: 'POST',
      data: payload, // x-www-form-urlencoded
      success: function(msg){
        if (msg === 'OK') {
          alert('신청이 접수되었습니다.');
          closeCerModal();
          if (typeof loadPage === 'function') {
            loadPage('myPage');
          } else {
            location.reload();
          }
        } else if (msg === 'ALREADY_OPEN') {
          alert('이미 처리 중인 신청이 있습니다.');
        } else if (msg === 'NEED_LOGIN') {
          location.href = '/login';
        } else {
          alert(msg || '신청 중 오류가 발생했습니다.');
        }
      },
      error: function(xhr){
        alert(xhr.responseText || '신청 중 서버 오류가 발생했습니다.');
      },
      complete: function(){
        $btn.prop('disabled', false);
      }
    });
  });

  // 전역 노출(필요 시)
  window.openCerModal = openCerModal;
  window.closeCerModal = closeCerModal;
})();
