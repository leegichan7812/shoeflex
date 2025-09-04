/* =========================
   Notice 전용 공통 스크립트
   - 상세 모달 로딩
   - 날짜 포맷
   - 페이징(goNoticePage)
   - 검색(searchNotice)
   - 목록 갱신(loadNoticeList)
   - CRUD(submitNotice, updateNotice, deleteNotice)
   - 다국어(공지 관련 키만)
   ========================= */

/* 공지 상세 모달 로딩 */

/* 공지 상세 모달 로딩 */
// 현재 화면이 관리자 목록인지에 따라 /faq vs /faqA 선택
function _faqBaseUrl() {
  const v = String($("#faqTableBody").data("is-admin") || "").toLowerCase();
  const isAdmin = (v === "true" || v === "y" || v === "1");
  return isAdmin ? "/faqA" : "/faq";
}

// --- Bootstrap4 전용 모달 열기/닫기 ---
function _showModalById(id) {
  $("#" + id).modal("show");
}

function _hideModalById(id) {
  $("#" + id).modal("hide");
}


// ----------------------
// 검색
// ----------------------
function searchFaq() {
  const base = _faqBaseUrl();
  const keyword = ($("#faqKeyword").val() || "").trim();
  const qs = new URLSearchParams();
  if (keyword) qs.set("keyword", keyword);
  loadAdminPage(base + (qs.toString() ? "?" + qs.toString() : ""));
  return false;
}

// ----------------------
// 페이징(위임 이벤트)
// ----------------------
$(document).on('click', '#faqPagination .page-link', function (e) {
  e.preventDefault();
  const page = Number($(this).data('page') || $(this).text());
  if (!Number.isNaN(page)) {
    loadFaqList({ page });
  }
});

// ----------------------
// 카테고리 필터
// ----------------------
function filterFaqByCategory(category) {
  const base = _faqBaseUrl();
  const qs = new URLSearchParams();
  if (category) qs.set("category", category);
  loadAdminPage(base + (qs.toString() ? "?" + qs.toString() : ""));
}

// ----------------------
// 등록
// ----------------------
function submitFaq() {
  const formData = $("#faqForm").serialize();
  $.ajax({
    type: "POST",
    url: "/faq",
    data: formData,
    success: function () {
      alert("등록 완료");
	  closeFaqModalBS5("faqModal");   // ← 이 줄만
      loadFaqList();               // 목록 갱신(모달도 갱신)
    },
    error: function () {
      alert("등록 실패");
    }
  });
}

/* ✅ Bootstrap 5 전용: 강제 종료 + 정리 */
function closeFaqModalBS5(id) {
  var modalId = id || 'faqModal';
  var el = document.getElementById(modalId);
  if (!el) return;

  // 1) 인스턴스가 있으면 정석으로 닫기
  try {
    // getOrCreateInstance: 있으면 가져오고 없으면 생성 (BS5.2+)
    var inst = (bootstrap.Modal.getOrCreateInstance)
      ? bootstrap.Modal.getOrCreateInstance(el)
      : (bootstrap.Modal.getInstance(el) || new bootstrap.Modal(el));
    inst.hide();
  } catch (e) {
    // ignore
  }

  // 2) 혹시 모를 레이스컨디션 대비: DOM 강제 정리(즉시)
  el.classList.remove('show');
  el.setAttribute('aria-hidden', 'true');
  el.style.display = 'none';

  // 3) backdrop/스크롤 잠금 정리
  document.querySelectorAll('.modal-backdrop').forEach(function(b){ b.remove(); });

  // 열려 있는 모달이 더 없으면 body 상태 복구
  if (!document.querySelector('.modal.show')) {
    document.body.classList.remove('modal-open');
    document.body.style.removeProperty('padding-right');
  }
}
// ----------------------
// 수정
// ----------------------
function updateFaq(faqId) {
  const formData = $("#updateFaqForm" + faqId).serialize();
  $.ajax({
    type: "POST",
    url: "/faq/update",
    data: formData,
    success: function () {
      alert("수정 완료");
      _hideModalById("faqModal" + faqId); // 수정 모달 닫기
      loadFaqList();                      // 목록 갱신(모달도 갱신)
    },
    error: function () {
      alert("수정 실패");
    }
  });
}

// ----------------------
// 삭제
// ----------------------
function deleteFaq(faqId) {
  if (!confirm("정말 삭제하시겠습니까?")) return;
  $.ajax({
    type: "POST",
    url: "/faq/delete",
    data: { faqId: faqId },
    success: function () {
      alert("삭제 완료");
      // 수정 모달 닫기
      _hideModalById("faqModal" + faqId);
      // 목록 즉시 갱신(행/모달 동시 반영)
      loadFaqList();
    },
    error: function () {
      alert("삭제 실패");
    }
  });
}

// ----------------------
// 목록 갱신(핵심): tbody + 수정모달 동시 갱신
// ----------------------
// FAQ 목록 갱신: loadAdminPage 사용 버전
function loadFaqList(opts = {}) {
  const base = _faqBaseUrl(); // /faq or /faqA 자동 선택
  const qs   = new URLSearchParams();

  // 1) 현재 검색어 유지
  const keyword = ($("#faqKeyword").val() || "").trim();
  if (keyword) qs.set("keyword", keyword);

  // 2) 현재 페이지 유지(옵션으로 오면 우선 사용)
  let page = Number(opts.page);
  if (!page) {
    // active page에서 숫자 추출 (data-page 또는 텍스트)
    const $active = $("#faqPagination .page-item.active .page-link");
    page = Number($active.data("page") || $active.text() || 1);
  }
  if (page && !Number.isNaN(page)) qs.set("curPage", String(page));

  // 3) (선택) 현재 선택 카테고리 유지: 버튼에 .active 달아뒀다면 사용
  const $activeCat = $("#faqCategoryGroup .btn.active");
  if ($activeCat.length && $activeCat.attr("id") !== "faqCatAll") {
    // 버튼 클릭 시 filterFaqByCategory('배송')처럼 한글 라벨을 그대로 value로 쓰는 경우
    // data-category가 있으면 그걸 먼저 사용
    const cat = $activeCat.data("category") || $activeCat.text().trim();
    if (cat) qs.set("category", cat);
  }

  const url = base + (qs.toString() ? ("?" + qs.toString()) : "");
  loadAdminPage(url);
}
// ----------------------
// 수정 모달 열기
// ----------------------
function openFaqModal(faqId) {
  _showModalById("faqModal" + faqId);
}

function loadNoticeDetail(noticeId) {
  $.ajax({
    url: "/support/notice/detail",
    data: { id: noticeId },
    success: function (data) {
      $("#noticeModalTitle").text(data.title);
      $("#noticeModalContent").html(String(data.content || "").replace(/\n/g, "<br>"));

      // createdAt이 문자열/숫자형이어도 Date로 처리
      $("#noticeModalInfo").html(
        "조회수: " + (data.viewCount ?? 0) + " &nbsp;|&nbsp; 등록일: " + formatDateDot(data.createdAt)
      );

      $("#noticeModal").modal("show");
    },
    error: function () {
      alert("공지사항 정보를 불러오는 데 실패했습니다.");
    }
  });
}

/* 날짜 포맷: yyyy.MM.dd */
function formatDateDot(dateInput) {
  const date = new Date(dateInput);
  if (isNaN(date.getTime())) return "";
  const yyyy = date.getFullYear();
  const mm = ("0" + (date.getMonth() + 1)).slice(-2);
  const dd = ("0" + date.getDate()).slice(-2);
  return yyyy + "." + mm + "." + dd;
}

/* 모달 완전 종료 처리(백드롭 잔여 제거) */
// 모달 열기
// Bootstrap 4 전용 모달 열기/닫기
function _showModalById(id) {
  $("#" + id).modal("show");
}

function _hideModalById(id) {
  $("#" + id).modal("hide");
}
/* 공지 수정 */
function updateNotice(id) {
  const arr = $("#editNoticeForm" + id).serializeArray();
  const payload = {};
  arr.forEach(({ name, value }) => payload[name] = value);

  // 체크박스 기본값 처리
  if (!('isPopup' in payload))  payload.isPopup  = 'N';
  if (!('isPinned' in payload)) payload.isPinned = 'N';

  $.ajax({
    type: "POST",
    url: "/support/notice/update",
    data: payload,
    success: function () {
      alert("수정 완료");
      _hideModalById("editNoticeModal" + id);  // ✅ 여기서 정상 닫힘
      loadNoticeList(); // 목록 갱신
      localStorage.setItem("NOTICE_CHANGED", Date.now().toString());
      if (window.refreshNoticePopup) window.refreshNoticePopup(false);
    },
    error: function () { 
      alert("수정 실패"); 
    }
  });
}
// 1) 페이지 클릭 위임 (동적 교체에도 동작)
$(document).on('click', '#noticePagination .page-link', function (e) {
  e.preventDefault();
  const page = parseInt($(this).data('page') || $(this).text(), 10);
  if (!isNaN(page)) goNoticePage(page);
});

// 2) 공용 URL
function _noticeListUrl(){
  // JSP에 아래 data-list-url을 달아뒀다면 이 값을 우선 사용
  // <div id="noticeTableWrap" data-list-url="/admin/support/notice">
  return $("#noticeTableWrap").data("list-url") || "/support/notice";
}

// 3) 목록 스왑: 래퍼와 id는 유지, 안쪽 내용만 교체
function _swapNotice(html){
  const $html = $(html);

  // tbody
  const $tbody = $html.find("#noticeTableBody");
  if ($tbody.length) $("#noticeTableBody").html($tbody.html());

  // thead(필요 시)
  const $thead = $html.find("#noticeTable thead");
  if ($thead.length) $("#noticeTable thead").html($thead.html());

  // pagination: ul 내용만 교체(래퍼/ID 보존)
  const $newPagi = $html.find("#noticePagination");
  if ($newPagi.length) {
    $("#noticePagination").html($newPagi.html());
    $("#noticePaginationWrap").removeClass("d-none");
  } else {
    $("#noticePagination").empty();
    $("#noticePaginationWrap").addClass("d-none");
  }
  const $modals = $html.find("#editNoticeModals");
  if ($modals.length) {
    $("#editNoticeModals").html($modals.html());
  } else {
    // 응답에 모달이 없다면 기존 모달을 비워 충돌 방지(선택)
    $("#editNoticeModals").empty();
  }
}

// 4) 페이지 이동
function goNoticePage(page){
  const keyword  = ($("#noticeKeyword").val() || "").trim();
  $.ajax({
    url: _noticeListUrl(),
    type: "GET",
    dataType: "html",
    data: { curPage: page, pageSize: 10, keyword },
    success: _swapNotice,
    error: () => alert("공지사항 목록을 불러오지 못했습니다.")
  });
}

// 5) 검색
function searchNotice(){
  const el = document.getElementById("noticeKeyword");
  if (!el) { alert("검색창이 없습니다."); return false; }
  const keyword = el.value.trim();
  if (!keyword){ alert("검색어를 입력해주세요."); el.focus(); return false; }

  $.ajax({
    url: _noticeListUrl(),
    type: "GET",
    dataType: "html",
    data: { keyword, curPage: 1, pageSize: 10 },
    success: (html)=>{ _swapNotice(html); $("#noticeKeyword").val(keyword); },
    error: () => alert("공지사항 검색 실패")
  });
  return false;
}

// 6) 수정/삭제 후 목록 갱신(현재 active 페이지 유지)
function loadNoticeList(curPage){
  const keyword = ($("#noticeKeyword").val() || "").trim();
  const cur = parseInt($("#noticePagination .page-item.active .page-link").text(), 10) || 1;
  const page = curPage || cur;

  $.ajax({
    url: _noticeListUrl(),
    type: "GET",
    dataType: "html",
    data: { curPage: page, pageSize: 10, keyword },
    success: _swapNotice,
    error: () => alert("공지사항 목록 갱신 실패")
  });
}

/* ====== CRUD (관리자 화면에서 사용) ====== */

/* 새 공지 등록 */
function submitNotice() {
  const formData = $("#noticeForm").serialize();
  $.ajax({
    type: "POST",
    url: "/support/notice/insert",
    data: formData,
    success: function () {
      alert("등록 완료");
      closeNewNoticeModalAfterSave();   // ← 요 한 줄만 호출
    },
    error: function () { alert("등록 실패"); }
  });
}

// 폼 리셋
function resetNewNoticeForm() {
  const form = document.getElementById('noticeForm');
  if (!form) return;
  form.reset();
  form.classList.remove('was-validated');
  form.querySelectorAll('input[type="text"], textarea').forEach(el => el.value = '');
  form.querySelectorAll('input[type="checkbox"]').forEach(el => el.checked = false);
}

// 등록 성공 후 호출 (닫기 → 리셋 → 목록/팝업 갱신)
function closeNewNoticeModalAfterSave() {
  var $modal = $('#newNoticeModal');
  var done = false;

  function forceHide() {
    // 실제로 모달을 안 보이게 처리(중요!)
    $modal.removeClass('show')
          .attr('aria-hidden', 'true')
          .css('display', 'none');
    $('.modal-backdrop').remove();
    $('body').removeClass('modal-open').css('padding-right', '');
  }

  function afterHidden() {
    if (done) return;
    done = true;
    forceHide();           // 혹시 모달이 안 닫혔어도 강제 숨김
    resetNewNoticeForm();  // 폼 초기화
    loadNoticeList(1);
    localStorage.setItem("NOTICE_CHANGED", Date.now().toString());
    if (window.refreshNoticePopup) window.refreshNoticePopup(false);
  }

  // 정상 플로우: hidden 이벤트 한 번만 수신
  $modal.one('hidden.bs.modal', afterHidden);

  // 닫기 시도 (정석)
  try { $modal.modal('hide'); } catch (e) {}

  // 안전망: hidden이 유실되면 강제 후처리
  setTimeout(afterHidden, 450);
}
/* 공지 수정 */
function updateNotice(id) {
  const arr = $("#editNoticeForm" + id).serializeArray();
  const payload = {};
  arr.forEach(({name, value}) => payload[name] = value);

  // 체크 안 하면 key 자체가 없음 → 기본값 부여
  if (!('isPopup' in payload))  payload.isPopup  = 'N';
  if (!('isPinned' in payload)) payload.isPinned = 'N';

  $.ajax({
    type: "POST",
    url: "/support/notice/update",
    data: payload,
    success: function () {
      alert("수정 완료");
      $("#editNoticeModal" + id).modal("hide");
      loadNoticeList(); // 목록 갱신
      localStorage.setItem("NOTICE_CHANGED", Date.now().toString());
      if (window.refreshNoticePopup) window.refreshNoticePopup(false);
    },
    error: function () { alert("수정 실패"); }
  });
}
/* 공지 삭제 */
function deleteNotice(id) {
  if (!confirm("정말 삭제하시겠습니까?")) return;

  $.ajax({
    type: "POST",
    url: "/support/notice/delete",
    data: { noticeId: id },
    success: function () {
      alert("삭제 완료");
      $("#noticeModal").modal("hide");
      // 행 직접 제거(빠른 피드백)
      $("#noticeRow" + id).remove();
      // 목록/페이지네이션 재정렬을 위해 갱신
      loadNoticeList();
    },
    error: function () {
      alert("삭제 실패");
    }
  });
}

/* ====== 다국어(공지 관련 요소만 갱신) ====== */
// 전역에 한 번만 셋업
window.currentLang = window.currentLang || "ko";

window.setLang = function (lang, btn) {
  window.currentLang = lang;
  window.changeLang(lang);

  if (btn) {
    $(".langBtn").removeClass("active btn-dark").addClass("btn-outline-secondary");
    $(btn).removeClass("btn-outline-secondary").addClass("btn-dark");
  }
};

window.changeLang = function (lang) {
  $.ajax({
    url: "/changeLang",
    type: "POST",
    data: { lang: lang },
    success: function (msgs) {
      const setText = (sel, key) => { if (msgs[key] !== undefined) $(sel).text(msgs[key]); };
      const setPh   = (sel, key) => { if (msgs[key] !== undefined) $(sel).attr("placeholder", msgs[key]); };

      // --- 기존 갱신 로직 그대로 ---
      setText("#noticeTop5",         "notice.top5");
      setText("#noticeSearchLabel2", "notice.search2");
      setText("#noticeSearchLabel",  "notice.search");
      setPh  ("#noticeKeyword",      "notice.search.placeholder");
      setText("#noticeNo",           "notice.no");
      setText("#noticeTitle",        "notice.title");
      setText("#noticeDate",         "notice.date");

      if (msgs["notice.pin"] !== undefined) {
        $(".noticePin .pinText").each(function () { $(this).text(msgs["notice.pin"]); });
      }

      setText("#faqTab",     msgs["sc.tab.faq"] !== undefined ? "sc.tab.faq" : "notice.tab.faq");
      setText("#qnaTab",     "sc.tab.qna");
      setText("#noticeTab",  "notice.tab.notice");

      setText("#faqTitle",       "faq.title");
      setText("#faqSearchLabel", "faq.search");
      setPh  ("#faqKeyword",     "faq.search.placeholder");

      setText("#faqNo",       "faq.no");
      setText("#faqCategory", "faq.category");
      setText("#faqQuestion", "faq.question");
      setText("#faqDate",     "faq.date");

      setText("#faqCatAll",      "faq.category.all");
      setText("#faqCatOrder",    "faq.category.order");
      setText("#faqCatDelivery", "faq.category.delivery");
      setText("#faqCatMember",   "faq.category.member");
      setText("#faqCatProduct",  "faq.category.product");
      setText("#faqCatPayment",  "faq.category.payment");
      setText("#faqCatRefund",   "faq.category.refund");
      setText("#faqCatCoupon",   "faq.category.coupon");
    },
    error: function () {
      alert("언어 변경 실패");
    }
  });
};

(function () {
  if (!window.chatConfig) {
	window.__chatTitle = window.__chatTitle || { timer: null, orig: document.title };
    console.error("⚠️ chatConfig가 정의되지 않았습니다. 예) window.chatConfig = { socketServer:'wss://...', currentUser:'사용자' }");
    window.chatConfig = { socketServer: "", currentUser: "guest" }; // 최소 기본값
  }

  const socketServer = () => String(window.chatConfig.socketServer || "");
  const currentUser  = () => String(window.chatConfig.currentUser || "guest");

  // 전역에 하나만 유지
  if (!window._chat) window._chat = { wsocket: null, isOpen: false, lastSent: null, lastSentAt: 0 };

  // ---- 내부 유틸 ----
  function buildWsUrl() {
    const base = socketServer();
    if (!base) return "";
    const sep = base.includes("?") ? "&" : "?";
    const acct = String((window.chatConfig && window.chatConfig.accountType) || "USER");
	return `${base}${sep}accountType=${acct}&userId=${encodeURIComponent(currentUser())}`;
  }

  function classify(msg) {
    const s = String(msg || "").trim();

    // 1) ALERT: "[ALERT] CONNECT_REQUEST|이름|sid" 형태
    if (s.startsWith("[ALERT]")) {
      const rest = s.replace(/^\[ALERT\]\s?/, "");
      const [kind = "", name = "", sid = "", extra = ""] = rest.split("|");
      return {
        type: "alert",
        kind: kind.trim(),
        name: name.trim(),
        sid: sid.trim(),
        text: extra.trim(), // 필요하면 추가 정보 사용
      };
    }

    // 2) [USER 이름] / [ADMIN 이름] 본문  (이름 포함 버전)
    let m = s.match(/^\[(USER|ADMIN)\s+([^\]]+)\]\s*(.*)$/i);
    if (m) {
      return { type: m[1].toLowerCase(), name: (m[2] || "").trim(), text: m[3] || "" };
    }

    // 3) [USER] / [ADMIN] / [BOT] / [SYSTEM] 본문  (이름 없는 버전)
    m = s.match(/^\[(USER|ADMIN|BOT|SYSTEM)\]\s*(.*)$/i);
    if (m) {
      return { type: m[1].toLowerCase(), name: "", text: m[2] || "" };
    }

    // 4) 기타 일반 문자열
    return { type: "plain", name: "", text: s };
  }

  // ---- WebSocket 열기/닫기 ----
  function startChat() {
    const url = buildWsUrl();
    if (!url) { alert("채팅 서버 설정이 없습니다."); return; }
	 if (window._chat.wsocket &&
	     (window._chat.wsocket.readyState === WebSocket.OPEN ||
	      window._chat.wsocket.readyState === WebSocket.CONNECTING)) return;
    const ws = new WebSocket(url);
    window._chat.wsocket = ws;
	
    ws.onopen = function () {
      window._chat.isOpen = true;
	  
    };

	ws.onmessage = function (evt) {
	  const raw = String(evt.data || "").trim(); // ← trim 추가

	  // --- 에코/중복 무시 ---
	  if (/상담을 시작했습니다\.$/.test(raw)) return;
	  if (raw === window._chat.lastSent && Date.now() - window._chat.lastSentAt < 1500) return;
	  const pref = currentUser() + ":";
	  if (raw.startsWith(pref)) {
	    const body = raw.slice(pref.length).trim();
	    if (body === window._chat.lastSent && Date.now() - window._chat.lastSentAt < 1500) return;
	  }
	  if (raw.includes("모든 고객과의 상담을 종료했습니다")) {
	    clearChatBadge();
	    hideAllToasts();
	    flashTitleStop();
	    addIncomingMessage("[SYSTEM] 모든 고객과의 상담을 종료했습니다.");
	    return;
	  }

	  // === 여기서 제어 메시지 먼저 가로채기 (채팅창에 표시하지 않음) ===
	  if (raw.startsWith("[ROUTE]")) {
	    const key = raw.slice(7).trim();
	    handleRoute(key);     // CTA 렌더/페이지 이동 등
	    return;               // ← 채팅 메시지로 렌더 금지
	  }
	  if (raw.startsWith("[SUGGEST]")) {
	    const items = raw.slice(9).trim().split("|").map(s => s.trim()).filter(Boolean);
	    renderQuickReplies(items);  // 퀵 리플라이 버튼만 렌더
	    return;                     // ← 채팅 메시지로 렌더 금지
	  }
	  // === 여기까지 추가 ===

	  // --- 분류 ---
	  const parsed = classify(raw);
	  const acct = String((window.chatConfig && window.chatConfig.accountType) || "USER").toUpperCase();

	  // --- ALERT 처리 (관리자 화면에서만 UX 표시) ---
	  if (parsed.type === "alert") {
	    if (acct === "ADMIN") {
	      switch (parsed.kind) {
	        case "CONNECT_REQUEST": {
	          if (typeof notifyIncomingRequest === "function") {
	            notifyIncomingRequest({ name: parsed.name, sid: parsed.sid });
	          }
	          addIncomingMessage(`[SYSTEM] ${parsed.name || "고객"} 고객님이 상담을 요청했습니다.`);
	          break;
	        }
	        default:
	          addIncomingMessage(`[SYSTEM] 알림 수신: ${parsed.kind}${parsed.name ? " - " + parsed.name : ""}`);
	      }
	    }
	    return; // ALERT는 여기서 소비
	  }

	  // --- 기타 메시지는 기존 렌더러로 ---
	  addIncomingMessage(raw);
	};

    ws.onclose = function () {
      window._chat.isOpen = false;
	  addIncomingMessage("[SYSTEM] 상담이 종료되었습니다."); // ← 여기로 변경
    };

    ws.onerror = function (evt) {
      console.error("채팅 에러:", evt);
      addIncomingMessage("[SYSTEM] 에러가 발생했습니다. 잠시 후 다시 시도해주세요.");
    };
  }

  function closeChat() {
    $("#chatWidget").hide();

    const acct = String((window.chatConfig && window.chatConfig.accountType) || "USER").toUpperCase();
    const ws = window._chat.wsocket;

    if (acct === "ADMIN") {
      // ✅ 관리자: UI만 닫고, 서버로 종료 명령 전송 (소켓은 유지)
      try {
        if (ws && ws.readyState === WebSocket.OPEN) {
          ws.send("/end");          // ← 서버가 이미 처리하도록 되어 있음
        }
      } catch (e) {}
      return; // 소켓은 열어둠(알림 수신 유지)
    }

    // ✅ 일반 유저: 기존대로 소켓 닫기
    if (ws) {
      try { /* 필요 시 종료 메시지 전송 */ } catch(e){}
      ws.close();
      window._chat.wsocket = null;
    }
  }

  function toggleChatWidget() {
    const $w = $("#chatWidget");
    $w.toggle();
    if ($w.is(":visible")) startChat();
  }
  
  function addMine(text) {
    const name = String((window.chatConfig && window.chatConfig.currentUser) || "나");

    // 보낸 사람 라벨(오른쪽 정렬)
    const $sender = $("<div/>").text(name).css({
      fontSize: "12px",
      color: "#555",
      margin: "0 0 2px auto",
      textAlign: "right"
    });

    // 내 말풍선(오른쪽, 연두색)
    const $bubble = $("<div/>").text(text).css({
      textAlign: "right",
      background: "#c8f7c5",
      padding: "6px 10px",
      margin: "2px 0",
      borderRadius: "8px",
      maxWidth: "80%",
      alignSelf: "flex-end"
    });

    const $wrap = $("<div/>").css({
      display: "flex",
      flexDirection: "column",
      alignItems: "flex-end"
    }).append($sender, $bubble);

    $("#chatMessages").append($wrap);

    const el = $("#chatMessages")[0];
    if (el) el.scrollTop = el.scrollHeight;
  }

  // ---- 송신 ----
  function sendChatMessage() {
    const ws = window._chat.wsocket;
    if (!ws || ws.readyState !== WebSocket.OPEN) {
      alert("채팅 서버에 연결되지 않았습니다.");
      return;
    }
    const $input = $("#chatInput");
    const msg = ($input.val() || "").trim();
    if (!msg) return;

    // 내가 보낸 말풍선은 클라이언트에서 바로 찍어줍니다.
    addMine(msg);
    sendRaw(msg);

    $input.val("");
  }
  
  $(function () {
	const acct = String((window.chatConfig && window.chatConfig.accountType) || "USER").toUpperCase();
	if (acct === "ADMIN") startChat();   // 관리자 화면은 자동 연결
  });
  function sendRaw(text) {
    const ws = window._chat.wsocket;
    if (!ws || ws.readyState !== WebSocket.OPEN) return;
    if (!text || !String(text).trim()) return;
    // ✨ 더 이상 "사용자이름:" 접두사를 붙이지 않습니다. -> 서버가 원문만 받도록
	 const payload = String(text);
	 window._chat.lastSent   = payload;
	 window._chat.lastSentAt = Date.now();
	 ws.send(payload);
  }

  // ---- 수신/렌더링 ----
  function addIncomingMessage(raw) {
    const msg  = classify(raw) || {};
    const type = msg.type || "plain";
    const text = msg.text ?? "";
    const name = msg.name;

    const map = {
      system: { color: "#888", align: "left", bg: "#f3f3f3" },
      bot:    { color: "#233", align: "left", bg: "#f0f3ff" },
      admin:  { color: "#7a3", align: "left", bg: "#fff7e6" },
      user:   { color: "#333", align: "left", bg: "#ffffff" },
      plain:  { color: "#333", align: "left", bg: "#ffffff" }
    };
    const sty  = map[type] || map.plain;
    const acct = String((window.chatConfig && window.chatConfig.accountType) || "USER").toUpperCase();

    // ✅ 라벨 결정
    let label = "";
    if (acct === "ADMIN" && type === "user") {
      // 관리자 화면: 고객 메시지 → "홍길동 고객님"
      const base = name || "고객님";
      label = base.endsWith("고객님") ? base : (base + " 고객님");
    } else if (acct === "USER") {
      // 고객 화면
      if (type === "admin") {
        label = "상담원";        // 실제 상담원(관리자) 메시지
      } else if (type === "bot" || type === "system") {
        label = "ShoeFlex";      // 연결 전/봇/시스템 메시지
      } else if (type === "user") {
        label = "";              // 내 메시지는 라벨 생략
      }
    } else {
      // 기타 계정 타입(게스트 등)
      if (type === "admin") label = "상담원";
      if (type === "bot" || type === "system") label = "ShoeFlex";
    }

    const $wrap = $("<div/>").css({
      display: "flex", flexDirection: "column", alignItems: "flex-start"
    });

    if (label) {
      $wrap.append($("<div/>").text(label).css({
        fontSize: "12px", color: "#555", margin: "0 0 2px 0", textAlign: "left"
      }));
    }

    const $bubble = $("<div/>").text(String(text)).css({
      textAlign: "left",
      background: sty.bg, color: sty.color,
      padding: "6px 10px", margin: "2px 0",
      borderRadius: "8px", maxWidth: "80%"
    });

    $wrap.append($bubble);
    $("#chatMessages").append($wrap);
    const el = $("#chatMessages")[0];
    if (el) el.scrollTop = el.scrollHeight;
  }
// ---- 관리자 알림 UX ----
function notifyAdminRequest(name, sid) {
  showToast("상담 연결 요청", `${name} 고객님이 상담을 요청했습니다.`, sid);
  bumpChatBadge(sid);
  showBrowserNotification(name, sid);
  flashTitleStart();
}

// ===== 알림 상태 관리 =====
window._chatNotify = window._chatNotify || { unread: new Set() };

// 뱃지 숫자 업데이트(당신이 이미 갖고있는 함수면 이대로 사용)
function updateBadge(n) {
  const $btn = $("#chatToggleBtn");
  if (!$btn.length) return;
  if (!$btn.find(".chat-badge").length) {
    $btn.css("position","relative")
        .append('<span class="chat-badge" style="position:absolute;top:5px;left:60px;background:#dc3545;color:#fff;border-radius:12px;padding:0px 8px;font-size:12px;">0</span>');
  }
  $btn.find(".chat-badge").text(n);
  $btn.toggleClass("attention", n > 0);
}

function bumpChatBadge(sid) {
  window._chatNotify.unread.add(String(sid));   // ✅ 문자열 통일
  updateBadge(window._chatNotify.unread.size);
}

// 미리 있던 함수에 안전장치 추가(읽음 0이면 타이틀 깜빡임 멈춤)
function clearChatBadge(sid) {
  const unread = window._chatNotify?.unread;
  if (unread) {
    if (sid) unread.delete(sid);
    else unread.clear();
    updateBadge(unread.size);
  } else {
    updateBadge(0);
  }
  if ((window._chatNotify?.unread?.size || 0) === 0) flashTitleStop();
}

// 모든 토스트 닫기(부트스트랩 5/4/폴백 모두 지원)
function hideAllToasts() {
  const hasBs = !!(window.bootstrap && window.bootstrap.Toast);
  $(".toast").each((_, el) => {
    if (hasBs) {
      window.bootstrap.Toast.getOrCreateInstance(el).hide();
    } else {
      $(el).remove();
    }
  });
}
// 컨테이너 (우상단)
function ensureToastContainer() {
  let $ct = $("#chatToastContainer");
  if ($ct.length) return $ct;
  $ct = $('<div id="chatToastContainer" class="toast-container position-fixed top-0 end-0 p-3" style="z-index:9999;"></div>');
  $("body").append($ct);
  return $ct;
}

function showToast(title, body, sid) {
  const $ct = ensureToastContainer();
  const id = "toast-" + Date.now();
  const html = `
    <div id="${id}" class="toast text-bg-light border-0" role="alert" aria-live="assertive" aria-atomic="true" data-sid="${String(sid)}">
      <div class="toast-header">
        <strong class="me-auto">${title}</strong>
        <small>지금</small>
        <button type="button" class="btn-close ms-2 mb-1" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
      <div class="toast-body">
        ${body}
        <div class="mt-2 pt-2 border-top d-flex gap-2">
          <button type="button" class="btn btn-sm btn-primary" data-accept="${String(sid)}">바로 보기</button>
        </div>
      </div>
    </div>`;
  const $t = $(html).appendTo($ct);
  const el = $t[0];

  const hasBs5 = !!(window.bootstrap && window.bootstrap.Toast);
  const hasBs4 = !!($.fn && $.fn.toast);

  // 공통: 토스트가 사라질 때 정리
  const onHidden = () => {
    $t.remove();
    if ((window._chatNotify?.unread?.size || 0) === 0) flashTitleStop();
  };

  if (hasBs5) {
    const toast = bootstrap.Toast.getOrCreateInstance(el, { delay: 6000, autohide: true });
    $t.on("hidden.bs.toast", onHidden);
    toast.show();
  } else if (hasBs4) {
    $t.on("hidden.bs.toast", onHidden);
    $t.toast({ delay: 6000, autohide: true }).toast("show");
  } else {
    // 폴백
    $t.addClass("show").css({ display: "block" });
    const timer = setTimeout(() => { $t.fadeOut(150, onHidden); }, 6000);
    $t.on("click", ".btn-close", () => { clearTimeout(timer); onHidden(); });
  }

  // ‘바로 보기’ 클릭 시: 읽음 처리 + 모든 토스트 닫기 + 위젯 열기
  $t.on("click", "button[data-accept]", function () {
    const sid = $(this).data("accept");
    clearChatBadge(String(sid));     // ✅ 뱃지 감소 및 깜빡임 관리
    hideAllToasts();                 // ✅ 떠있는 토스트 전부 닫기
    flashTitleStop();                // ✅ 타이틀 복구
    $("#chatWidget").show();
    if (window.ChatWidget?.open) window.ChatWidget.open();
    else if (typeof startChat === "function") startChat();
  });

  // 헤더/바디 아무 곳이나 클릭해도 닫히도록(관리자 쪽 ‘안 꺼짐’ 방지)
  $t.on("click", ".toast-header, .toast-body", () => {
	const sid = $t.data("sid");                 // ✅ 읽음 처리
	clearChatBadge(String(sid));
    if (hasBs5) bootstrap.Toast.getOrCreateInstance(el).hide();
    else if (hasBs4) $t.toast("hide");
    else $t.remove();
  });
}


// ===== 이벤트 위임 =====
$(document).off(".chatWidget");
$(document)
  .on("click.chatWidget", "#chatToggleBtn", function (e) {
    e.preventDefault();
    toggleChatWidget();
	
  })
  .on("click.chatWidget", "#chatCloseBtn",  function () { closeChat(); })
  .on("click.chatWidget", "#chatSendBtn",   function () { sendChatMessage(); })
  .on("keyup.chatWidget", "#chatInput",     function (e) { if (e.key === "Enter") sendChatMessage(); });
  // 전역에서 열 수 있게
  window.ChatWidget = {
    open:  function () { $("#chatWidget").show(); startChat(); },
    close: closeChat,
    toggle: toggleChatWidget,
    send:  sendChatMessage
  };
// 브라우저 알림
function showBrowserNotification(name, sid) {
  if (!("Notification" in window)) return;
  if (Notification.permission === "granted") {
	const n = new Notification("상담 연결 요청", { body: `${name} 고객님이 상담을 요청했습니다.` });
    n.onclick = () => window.focus();
  } else if (Notification.permission !== "denied") {
    Notification.requestPermission().then(p=>{
      if (p === "granted") showBrowserNotification(name, sid);
    });
  }
}

// ===== 타이틀 깜빡임 =====
function ensureTitleState() {
  if (!window.__chatTitle) {
    window.__chatTitle = { timer: null, orig: document.title };
  }
  return window.__chatTitle;
}
function flashTitleStart() {
  const st = ensureTitleState();
  if (st.timer) return;
  let on = false;
  st.timer = setInterval(() => {
    document.title = on ? "【상담 요청】" : st.orig;
    on = !on;
  }, 700);
}
function flashTitleStop() {
  const st = ensureTitleState();
  if (st.timer) { clearInterval(st.timer); st.timer = null; document.title = st.orig; }
}
// ===== 새 상담 요청 수신 시 공통 처리 =====
function notifyIncomingRequest({ name, sid }) {
  try {
    window._chatNotify.unread.add(String(sid));
    updateBadge(window._chatNotify.unread.size);
    flashTitleStart();
	showToast(`${name} 고객님`, "상담 연결 요청이 도착했습니다.", sid);
    showBrowserNotification(name, sid);
  } catch(e) { /* no-op */ }
}
window.addEventListener("focus", flashTitleStop);

})();

