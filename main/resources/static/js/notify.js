(function(){
  if(!window.notifyServer){
    // GlobalControllerAdvice에서 내려주는 값
    window.notifyServer = "${notifyServer}";
  }
  // ✅ 등급 준비 여부 판단
  function gradeKnown() {
    return typeof window.adminGrade === 'string' && window.adminGrade.trim() !== '';
  }

  function isBlack() {
    return gradeKnown() && (window.adminGrade || '').toUpperCase() === 'BLACK';
  }
  function canSeeNotify(n) {
    // 등급을 아직 모를 땐 일단 모두 보여주기 (필터링 보류)
    if (!gradeKnown()) return true;

    // 일정 승인 요청은 BLACK만
    if (n.type === 'CAL_APPROVAL_REQUIRED') return isBlack();

    return true;
  }

  let nSock = null;
  let notifItems = []; // 메모리 목록 (최근 20개)
  const MAX_ITEMS = 20;

  function connect(){
    try{
      nSock = new WebSocket(window.notifyServer);
      nSock.onopen = () => console.log("[notify] connected");
      nSock.onmessage = (evt) => {
        try{
          const msg = JSON.parse(evt.data);
          handleNotify(msg);
        }catch(e){ console.warn("Invalid notify JSON", evt.data); }
      };
      nSock.onclose = () => {
        console.log("[notify] closed, retry in 5s");
        setTimeout(connect, 5000);
      };
      nSock.onerror = (e) => console.error("[notify] error", e);
    }catch(e){
      console.error("[notify] connect failed", e);
    }
  }

  function handleNotify(n){
	if (!canSeeNotify(n)) return;   // ✅ 등급으로 필터
    // BLACK 전용 등 필터링은 서버에서 하는 게 원칙
    addItem(n);
    render();
    bumpBadge();
    // 중요도 높은 건 토스트나 사운드도 가능
  }

  function addItem(n){
    notifItems.unshift({ ts: Date.now(), ...n });
    if(notifItems.length > MAX_ITEMS) notifItems.pop();
  }

  function render(){
    const list = document.getElementById("notifList");
    if(!list) return;
    if(notifItems.length === 0){
      list.innerHTML = `<p class="text-muted text-center my-3">알림이 없습니다.</p>`;
      return;
    }
    list.innerHTML = notifItems.map(item => {
      const color = levelToColor(item.level);
      const msg = escapeHtml(item.message || "");
      const title = escapeHtml(item.title || "알림");
      return `
        <a href="#" class="dropdown-item preview-item" data-type="${item.type}">
          <div class="preview-thumbnail">
            <div class="preview-icon bg-${color} rounded-circle">
              <i class="mdi mdi-bell"></i>
            </div>
          </div>
          <div class="preview-item-content d-flex align-items-left flex-column" style="width: calc(100% - 40px);">
            <p class="preview-subject mb-1">${title}</p>
            <p class="text-muted ellipsis mb-0">${msg}</p>
          </div>
        </a>`;
    }).join("");
  }

  function bumpBadge(){
    const badge = document.getElementById("pendingCount");
    if(!badge) return;
    // 뱃지는 '미확인 수' 개념으로 처리
    const count = notifItems.length;
    badge.textContent = count > 99 ? '99+' : count;
    badge.style.display = count > 0 ? 'inline-block' : 'none';
  }

  // 알림 클릭 → 액션
  document.addEventListener("click", function(e){
    const a = e.target.closest("#notifList .dropdown-item");
    if(!a) return;
    e.preventDefault();
    const idx = Array.from(a.parentNode.children).indexOf(a);
    const item = notifItems[idx];
    if(!item) return;

    switch(item.type){
      case "CAL_APPROVAL_REQUIRED":
        // 승인 모달 열고 목록 로드
        $('#approvalModal').modal('show');
        if (typeof loadPendingApprovals === 'function') loadPendingApprovals();
        break;
      case "INQUIRY_UNRESOLVED":
		loadAdminPage('/admin/inquiry', function () {
		  // 드롭다운 값만 세팅 (이벤트는 안 보냄)
		  const $statusFilter = $('#statusFilter');
		  if ($statusFilter.length) $statusFilter.val('미확인');

		  // ✅ 함수가 실제 준비될 때까지 안전 대기 후 호출
		  const start = Date.now();
		  const timer = setInterval(() => {
		    if (typeof window.loadStatuses === 'function') {
		      clearInterval(timer);
		      window.loadStatuses(1, '미확인');
		    } else if (Date.now() - start > 1000) { // 3초 타임아웃
		      clearInterval(timer);
		      console.error('loadStatuses가 준비되지 않았습니다.');
		    }
		  }, 50);
		});
        break;
      case "CAL_UPCOMING":
        loadAdminPage('/calendar');
        break;
	  case "ORDER_AFTER_SALES_PENDING":
		loadAdminPage('/after-sales/list');
        break;
      case "BROADCAST":
      default:
        if (item.link) loadAdminPage(item.link);
        break;
    }
  });

  // 폴링 백업
  window.refreshNotifications = function(){
    fetch('/notifications/summary')
      .then(r => r.json())
      .then(s => {
        // 카운트 기반의 요약 알림 생성 (예: 존재할 때만 한 줄씩)
        const temp = [];
        if(s.calPendingApprovals > 0){
          temp.push({type:'CAL_APPROVAL_REQUIRED', title:'일정 승인 요청',
                     message:`승인 대기 ${s.calPendingApprovals}건`, level:'warning'});
        }
        if(s.inquiryUnresolved > 0){
          temp.push({type:'INQUIRY_UNRESOLVED', title:'미처리 문의',
                     message:`미처리 문의 ${s.inquiryUnresolved}건`, level:'danger'});
        }
        if(s.calDueSoon > 0){
          temp.push({type:'CAL_UPCOMING', title:'일정 임박',
                     message:`임박 일정 ${s.calDueSoon}건`, level:'info'});
        }
        // 후순위
        if(s.returnsCount > 0){
          temp.push({type:'ORDER_AFTER_SALES_PENDING', title:'AS 처리 필요',
                     message:`반품/취소/교환 ${s.returnsCount}건`, level:'success'});
        }
        if(s.reviewsNoReply > 0){
          temp.push({type:'REVIEW_NO_REPLY', title:'리뷰 답글 필요',
                     message:`미답변 리뷰 ${s.reviewsNoReply}건`, level:'info'});
        }
        // 목록 교체
		notifItems = gradeKnown() ? temp.filter(canSeeNotify) : temp;
        render(); bumpBadge();
      })
      .catch(()=>{ /* 무시 */ });
  };

  function levelToColor(level){
    switch(level){
      case 'success': return 'success';
      case 'warning': return 'warning';
      case 'danger': return 'danger';
      default: return 'info';
    }
  }
  function escapeHtml(s){
    return String(s).replace(/[&<>"']/g, m=>({ '&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;' }[m]));
  }

  // 시작
  connect();
  // 폴링은 60초마다
  setInterval(()=>window.refreshNotifications(), 60000);
  // 첫 진입 즉시 한 번
  window.refreshNotifications();

  // 다른 곳에서 푸시로 알림을 바로 뱃지 반영하고 싶을 때
  window.pushNotifyItem = function(n){ handleNotify(n); };

})();