(function ($) {
    "use strict";
	// FullCalendar 초기화 함수 정의
    window.initCalendarPage = function () {
    const calendarEl = document.getElementById("calendar");
    if (!calendarEl) return; // calendar가 없으면 무시
	
	let isBlack = false;

    const calendar = new FullCalendar.Calendar(calendarEl, {
        headerToolbar: {
            left: "prev,next today",
            center: "title",
            right: "dayGridMonth,timeGridWeek,timeGridDay",
        },
        initialDate: new Date().toISOString().split("T")[0],
        navLinks: true,
        selectable: true,
        editable: true,
        dayMaxEvents: true,
        select(event) {
            $("#showModal").click();
            addForm(event, "I");
            calendar.unselect();
        },
		eventClick(arg) {
		  $("#showModal").click();
		  addForm(arg.event, "D");  // 버튼 토글은 addForm에서 처리
		},
        eventDrop(arg) {
            addForm(arg.event, "D");
            callAjax("/calUpdate", arg.revert);
        },
        eventResize(arg) {
            addForm(arg.event, "D");
            callAjax("/calUpdate", arg.revert);
        },
		events(info, successCallback, failureCallback) {
		  $.ajax({
		    url: "/calListAdmin",
		    method: "GET",
		    dataType: "json",
		    success: res => {
		      if (Array.isArray(res)) {          // ← 응답 검증
		        isBlack = true;
		        successCallback(res);
		      } else {
		        isBlack = false;                  // fallback
		        $.ajax({ url: "/calListVisible", dataType: "json",
		          success: r => successCallback(r), error: e => failureCallback(e)
		        });
		      }
		    },
		    error: () => {
		      isBlack = false;
		      $.ajax({ url: "/calListVisible", dataType: "json",
		        success: r => successCallback(r), error: e => failureCallback(e)
		      });
		    }
		  });
		}
    });

    calendar.render();
	function computeIsPublic() {
	  const title = ($("[name=title]").val() || "").trim();
	  return /휴가/.test(title) ? "N" : "Y";
	}

	$("#regBtn").off("click").on("click", () => {
	  $("[name=isPublic]").val(computeIsPublic());
	  return confirm("등록하시겠습니까?") && callAjax("/calInsert");
	});
	$("#uptBtn").off("click").on("click", () => {
	  $("[name=isPublic]").val(computeIsPublic());
	  return confirm("수정하시겠습니까?") && callAjax("/calUpdate");
	});


    $("#delBtn").click(() => confirm("삭제하시겠습니까?") && callAjax("/calDelete"));

	function addForm(event, proc) {
	  $("#frm")[0].reset();
	  $("[name=userId]").val($("#userIdVal").val());

	  $("[name=start]").val(event.startStr);
	  $("[name=end]").val(event.endStr ?? event.startStr);
	  $("[name=allDay]").val(event.allDay ? 1 : 0);

	  // 기존에 걸린 리스너 중복 방지
	  $("[name=title]").off("input._isPublicAuto");

	  if (proc === "I") {
	    // 새 등록 기본값: Y (휴가면 N)
	    const setIsPublicByTitle = () => {
	      const title = ($("[name=title]").val() || "").trim();
	      $("[name=isPublic]").val(/휴가/.test(title) ? "N" : "Y");
	    };
	    $("[name=isPublic]").val("Y"); // 기본은 공개
	    setIsPublicByTitle();          // 현재 제목 기준 1회 반영
	    $("[name=title]").on("input._isPublicAuto", setIsPublicByTitle); // 제목 바뀌면 갱신

	    $("[name=id]").val("");
	    $("#modalTitle").text("일정 등록");
	    $("#regBtn").show(); $("#uptBtn").hide(); $("#delBtn").hide();
	    // BLACK만 공개여부 행 보이기
	    $("#isPublicRow").toggle(!!isBlack);
	    return;
	  }

	  // 상세 모드
	  $("#modalTitle").text("일정 상세");
	  $("#regBtn").hide();

	  $("[name=id]").val(event.id);
	  $("[name=title]").val(event.title);
	  $("[name=backgroundColor]").val(event.backgroundColor);
	  $("[name=textColor]").val(event.textColor);
	  $("[name=writer]").val(event.extendedProps.writer);
	  $("[name=content]").val(event.extendedProps.content);
	  $("[name=status]").val(event.extendedProps.status);
	  $("[name=isPublic]").val(event.extendedProps.isPublic ?? "N");
	  $("[name=userId]").val(event.extendedProps.userId ?? $("#userIdVal").val());

	  const ownerId     = String(event.extendedProps.userId ?? "");
	  const loginUserId = String($("#userIdVal").val() ?? "");
	  const canEdit     = Boolean(isBlack) || ownerId === loginUserId;

	  $("#uptBtn").toggle(canEdit);
	  $("#delBtn").toggle(canEdit);
	  $("#isPublicRow").toggle(!!isBlack);
	}

    function callAjax(url, revertFunc = null) {
        $.ajax({
            url: url,
            method: "post",
            data: $("#frm").serialize(),
            dataType: "json",
            success: function (res) {
                if (res.msg) alert(res.msg);
                calendar.removeAllEvents();
                calendar.refetchEvents();
                $(".btn-close").click();
            },
            error: function (err) {
				const errMsg = err?.responseJSON?.msg || "처리 중 오류가 발생했습니다.";
	            alert(errMsg);

	            // 🔁 만약 롤백 함수가 전달됐다면 실행
	            if (typeof revertFunc === "function") revertFunc();
            }
        });
    }
};
// showModal: true면 모달까지 열어줌(기본 false, notify.js는 이미 모달을 열고 호출하므로 false)
// 승인 대기 일정 목록 로드 (notify.js에서 호출)
window.loadPendingApprovals = function (showModal = false) {
  const $list = $('#pendingList');
  $list.html('<li class="list-group-item text-center text-muted">불러오는 중…</li>');

  $.ajax({
    url: (window.path || '') + '/pendingApprovals',
    method: 'GET',
    dataType: 'json',
    timeout: 8000
  })
  .done(function (res) {
    // ✅ 배열 정규화
    const items = Array.isArray(res) ? res : (res.list || res.data || res.items || []);
    $('#pendingCount').text(Array.isArray(items) ? items.length : 0);

    if (!Array.isArray(items)) {
      $list.html('<li class="list-group-item text-danger text-center">응답 형식 오류</li>');
      return;
    }
    if (items.length === 0) {
      $list.html('<li class="list-group-item text-muted text-center">승인 대기 일정이 없습니다</li>');
      return;
    }

    const fmt = (v) => {
      if (!v) return '날짜 정보 없음';
      const d = (typeof v === 'number') ? new Date(v) : new Date(String(v).replace(' ', 'T'));
      return isNaN(d.getTime()) ? String(v).split(' ')[0] : d.toISOString().split('T')[0];
    };

    const html = items.map(ev => {
      const id = ev.id ?? ev.calendarId ?? ev.scheduleId;
      const start = fmt(ev.start || ev.start1);
      const end   = fmt(ev.end   || ev.end1);
      return `
        <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
          <div class="text-dark" style="max-width:80%;">
            <div><strong>제목 : ${ev.title || '(제목 없음)'}</strong></div>
            <div><strong>작성자 : ${ev.writer || '-'}</strong></div>
            <div><strong>기간 : ${start} ~ ${end}</strong></div>
            <div><strong>내용 : ${ev.content || '내용 없음'}</strong></div>
          </div>
          <button class="btn btn-sm btn-outline-primary mt-2" onclick="approveSchedule(${id})">승인</button>
        </li>`;
    }).join('');

    $list.html(html);
    if (showModal) $('#approvalModal').modal('show');
  })
  .fail(function (xhr) {
    const msg = (xhr.status ? xhr.status + ' ' : '') + (xhr.responseText || '요청 실패');
    $list.html('<li class="list-group-item text-danger text-center">'+ msg +'</li>');
    if (showModal) $('#approvalModal').modal('show');
  });
};
	
})(jQuery);

// 일정 승인 처리
function approveSchedule(id) {
  if (!confirm('해당 일정을 승인하시겠습니까?')) return;
  $.post((window.path || '') + '/approveCalendar', { id })
   .done(function (res) {
     alert(res.msg || '승인되었습니다.');
     // 목록/카운트 갱신
     window.loadPendingApprovals();        // 드롭다운 카운트 갱신
     window.loadPendingApprovals(true);    // 모달 목록 갱신(열려있는 경우도 반영)
     // 캘린더 새로고침
     if (typeof initCalendarPage === 'function') initCalendarPage();
   })
   .fail(function (xhr) {
     alert(xhr.responseText || '승인 처리 중 오류 발생');
   });
}

function loadPendingApprovals() {
  $.ajax({
    url: '/pendingApprovals',
    method: 'GET',
    dataType: 'json',
    success: function(list) {
      $('#pendingCount').text(list.length);
      // 알림 모달이나 드롭다운에 목록 표시 가능
    },
    error: function(err) {
      console.error("알림 로딩 실패", err);
    }
  });
}
