(function () {
  const ctx = window.APP_CTX || '';

  // 유틸
  const $ = (sel, el = document) => el.querySelector(sel);
  const $$ = (sel, el = document) => Array.from(el.querySelectorAll(sel));
  const escapeHtml = s => (s || '').replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));
  const fileName = p => { try { return (p || '').split('/').pop(); } catch { return p; } };

  const modalEl = document.getElementById('eventModal');
  if (!modalEl) return;

  const modal = new bootstrap.Modal(modalEl);
  const tbody = $('#eventTbody', modalEl);
  const checkAll = $('#ev-check-all', modalEl);
  const btnSave = $('#btnEvSave', modalEl);
  const btnDelete = $('#btnEvImgDelete', modalEl);
  const brandIdHidden = $('#ev-brand-id', modalEl);
  const brandTitleSpan = $('#ev-brand-title', modalEl);

  // API 엔드포인트 (data-* 로 커스터마이즈 가능)
  const listURL = modalEl.dataset.listUrl || '/admin/events';
  const saveURL = modalEl.dataset.saveUrl || '/admin/events/{id}/slide';
  const deleteURL = modalEl.dataset.deleteUrl || '/admin/events/{id}/slide';
  
  // 유틸 아래에 추가
  const absUrl = (p) => {
    if (!p) return '';
    const withSlash = p.startsWith('/') ? p : ('/' + p);
    return ctx + withSlash;
  };
  const withBust = (u) => u ? `${u}${u.includes('?') ? '&' : '?'}v=${Date.now()}` : u;

  // 모달이 열릴 때(부트스트랩 기본 동작 이용): 트리거 버튼으로부터 브랜드 정보 확보
  modalEl.addEventListener('show.bs.modal', async (ev) => {
    const trigger = ev.relatedTarget; // .btn-upload
    let brandId = '';
    let brandName = '';
    if (trigger) {
      brandId = trigger.getAttribute('data-brand') || '';
      const tr = trigger.closest('tr');
      brandName = tr ? (tr.querySelector('.brand-name')?.dataset.name || '') : '';
    } else {
      // 수동으로 띄운 경우를 대비해 마지막 클릭 대상 찾기(옵션)
      const last = document.querySelector('.btn-upload[data-last="1"]');
      if (last) {
        brandId = last.getAttribute('data-brand') || '';
        const tr = last.closest('tr');
        brandName = tr ? (tr.querySelector('.brand-name')?.dataset.name || '') : '';
        last.removeAttribute('data-last');
      }
    }

    brandIdHidden.value = brandId;
    brandTitleSpan.textContent = brandName ? `- ${brandName}` : '';

	console.log("브랜드 ID:", brandId);
	console.log("브랜드 이름:", brandName);

    await loadEvents(brandId);
  });

  // 선택 전체
  checkAll.addEventListener('change', () => {
    $$('#eventTbody .ev-check', modalEl).forEach(cb => cb.checked = checkAll.checked);
  });

  // 저장 (체크된 행만)
  btnSave.addEventListener('click', async () => {
    const rows = checkedRows();
    if (rows.length === 0) { alert('저장할 행을 선택하세요.'); return; }

    try {
      await Promise.all(rows.map(rowSaveJob));
      alert('저장되었습니다.');
      await loadEvents(brandIdHidden.value);
    } catch (e) {
      console.error(e);
      alert('일부 저장 실패. 콘솔을 확인하세요.');
    }
  });

  // 이미지 삭제 (체크된 행만)
  btnDelete.addEventListener('click', async () => {
    const rows = checkedRows();
    if (rows.length === 0) { alert('삭제할 행을 선택하세요.'); return; }
    if (!confirm('선택한 이벤트의 슬라이드 이미지를 삭제하시겠습니까?')) return;

    try {
      await Promise.all(rows.map(async tr => {
        const id = tr.dataset.calendarId;
        const url = (deleteURL || '').replace('{id}', id);
        const res = await fetch(ctx + url, { method: 'DELETE' });
        if (!res.ok) throw new Error('삭제 실패: ' + id);
      }));
      alert('삭제했습니다.');
      await loadEvents(brandIdHidden.value);
    } catch (e) {
      console.error(e);
      alert('일부 삭제 실패. 콘솔을 확인하세요.');
    }
  });

  // ===== 내부 함수 =====
  async function loadEvents(brandId) {
    tbody.innerHTML = `<tr><td colspan="6" class="text-center text-muted py-4">로딩 중...</td></tr>`;
    try {
      const url = `${ctx}${listURL}?brandId=${encodeURIComponent(brandId)}&v=${Date.now()}`;
      const res = await fetch(url, { cache: 'no-store' });
      if (!res.ok) throw new Error('목록 조회 실패');
      const list = await res.json(); // [{calendarId,title,start1,end1,slideTitle,slideImgPath}, ...]
      renderRows(list);
    } catch (e) {
      console.error(e);
      tbody.innerHTML = `<tr><td colspan="6" class="text-center text-danger py-4">목록 조회 실패</td></tr>`;
    }
  }

  function renderRows(list) {
    // helpers fallback (absUrl/withBust가 없을 때 안전하게)
    const toAbs = (typeof absUrl === 'function') ? absUrl : (p) => p || '';
    const bust  = (typeof withBust === 'function') ? withBust : (u) => u;

    tbody.innerHTML = '';
    if (!Array.isArray(list) || list.length === 0) {
      tbody.innerHTML = `<tr><td colspan="6" class="text-center text-muted py-4">이벤트가 없습니다.</td></tr>`;
      return;
    }

    list.forEach(ev => {
      // 서버 키 호환
      const imgPath = ev.slideImage;
      const hasImg  = imgPath;
	  console.log(imgPath)

      const tr = document.createElement('tr');
      tr.dataset.calendarId = ev.calendarId;

      tr.innerHTML = `
        <td><input type="checkbox" class="ev-check"></td>
        <td>${escapeHtml(ev.title || '')}</td>
        <td>${escapeHtml(ev.start1 || '')}</td>
        <td>${escapeHtml(ev.end1 || '')}</td>
        <td>
          <input type="text" class="form-control form-control-sm ev-title-input"
                 value="${escapeHtml(ev.slideTitle || '')}" placeholder="타이틀">
        </td>
        <td class="d-flex align-items-center gap-3">
          <img class="ev-thumb d-none" alt="thumb"
               style="width:200px;height:140px;border-radius:8px;cursor:pointer;">
          <input type="file" class="form-control form-control-sm ev-file" accept="image/*"
                 style="max-width:240px;">
        </td>
      `;

      const fileInput  = tr.querySelector('.ev-file');
      const thumb      = tr.querySelector('.ev-thumb');
      const titleInput = tr.querySelector('.ev-title-input');
      const rowCheck   = tr.querySelector('.ev-check');

      // 초기 썸네일 표시/숨김
      if (hasImg) {
        const full = bust(toAbs(imgPath));
        thumb.src = full;
        thumb.dataset.full = full; // 크게 보기용
        thumb.classList.remove('d-none');
        fileInput.classList.add('d-none');
      } else {
        thumb.classList.add('d-none');
        fileInput.classList.remove('d-none');
      }

      // 이미지 로드 실패 시 파일선택 보이도록
      thumb.addEventListener('error', () => {
        thumb.classList.add('d-none');
        fileInput.classList.remove('d-none');
      });

      // 파일 선택 시 즉시 미리보기(썸네일 표시, 파일 입력 숨김)
      fileInput.addEventListener('change', () => {
        if (fileInput.files && fileInput.files.length > 0) {
          // 새 파일을 행에 저장 (rowSaveJob 등에서 사용)
          tr._newFile = fileInput.files[0];
          // 파일만 바꿔도 저장되도록 자동 체크
          rowCheck.checked = true;

          const blobUrl = URL.createObjectURL(fileInput.files[0]);
          thumb.onload = () => { URL.revokeObjectURL(blobUrl); }; // 메모리 정리
          thumb.src = blobUrl;
          thumb.dataset.full = blobUrl;
          thumb.classList.remove('d-none');
          fileInput.classList.add('d-none');
        } else {
          // 선택 취소 시 원상복구
          if (thumb.src && !thumb.classList.contains('d-none')) {
            // 기존 썸네일 유지
            fileInput.classList.add('d-none');
          } else {
            // 썸네일 없음
            thumb.classList.add('d-none');
            fileInput.classList.remove('d-none');
          }
        }
      });

      // 타이틀 입력 시 자동 체크 (사용자 실수 방지)
      titleInput.addEventListener('input', () => {
        rowCheck.checked = true;
      });

      // 썸네일 클릭 → 크게 보기(라이트박스)
      thumb.addEventListener('click', () => {
        const full = thumb.dataset.full || thumb.src;
        if (full && typeof openImageViewer === 'function') openImageViewer(full);
      });

      tbody.appendChild(tr);
    });

    if (typeof checkAll !== 'undefined' && checkAll) checkAll.checked = false;
  }

  
  // 파일 끝쪽(즉시 실행 함수 안) 어딘가에 추가
  function openImageViewer(url) {
    let overlay = document.getElementById('img-viewer-overlay');
    if (!overlay) {
      overlay = document.createElement('div');
      overlay.id = 'img-viewer-overlay';
      overlay.style.cssText = `
        position:fixed; inset:0; background:rgba(0,0,0,.8);
        display:flex; align-items:center; justify-content:center;
        z-index: 1060; /* 부트스트랩 모달 위 */
      `;
      const img = document.createElement('img');
      img.style.cssText = `
        max-width:90vw; max-height:90vh; box-shadow:0 10px 30px rgba(0,0,0,.6);
        border-radius:10px; background:#fff;
      `;
      img.id = 'img-viewer-img';
      overlay.appendChild(img);
      overlay.addEventListener('click', () => overlay.remove());
      document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && document.getElementById('img-viewer-overlay')) overlay.remove();
      });
      document.body.appendChild(overlay);
    }
    const imgEl = document.getElementById('img-viewer-img');
    imgEl.src = url;
  }

  function checkedRows() {
    return $$('#eventTbody tr', modalEl).filter(tr => tr.querySelector('.ev-check')?.checked);
  }

  async function rowSaveJob(tr) {
    const id = tr.dataset.calendarId;
    const titleInput = tr.querySelector('.ev-title-input');
    const fileInput = tr.querySelector('.ev-file');
	

    const fd = new FormData();
    // 제목
    fd.append('slideTitle', (titleInput.value || '').trim());

    // 파일(선택)
    const pickedFile = tr._newFile || (fileInput?.files?.[0]);
	if (pickedFile) {
		fd.append('slideImg', pickedFile);
	}

    // ✅ 브랜드 값 추가 (빈 값이면 append 안 함)
    const brandIdVal = (brandIdHidden.value || '').trim();
    if (brandIdVal) fd.append('brandId', brandIdVal);

    // brandName은 텍스트 노드 대신 raw 값을 hidden으로 갖고 있는 게 안전
    // 현재는 `brandTitleSpan`이 "- {브랜드명}" 형태라서 dataset으로 저장해두거나 hidden input으로 관리 추천
    const brandNameRaw =
      brandTitleSpan.dataset?.brandName || (brandTitleSpan.textContent || '').replace(/^\s*-\s*/, '').trim();
    if (brandNameRaw) fd.append('brandName', brandNameRaw);

    const url = (saveURL || '').replace('{id}', id); // "/admin/events/{id}/slide"
	
	// 🔎 1회성 디버깅: 실제 전송되는 값 확인
	console.log('FD slideTitle =', fd.get('slideTitle'));
	const f = fd.get('slideImg');
	console.log('FD slideImg =', f ? (f.name + ', ' + f.size + 'B') : '(none)');
	
    const res = await fetch(ctx + url, { method: 'POST', body: fd });
    if (!res.ok) throw new Error('저장 실패: ' + id);
    return res.json().catch(() => ({}));
  }

  // 안전장치: .btn-upload가 data-bs-toggle을 안쓴 경우도 지원
  document.addEventListener('click', (e) => {
    const btn = e.target.closest('.btn-upload');
    if (!btn) return;
    if (!btn.hasAttribute('data-bs-toggle')) {
      // 수동 오픈
      btn.setAttribute('data-last', '1');
      modal.show();
    }
  });
})();



// ✅ 선택된 브랜드명 가져오기
function getSelectedBrands() {
  const selected = [];
  document.querySelectorAll("#brandTbody .row-check:checked").forEach(chk => {
    selected.push(chk.value);   // ✅ 브랜드명 그대로 가져오기
  });
  return selected;
}

// ✅ 차트 렌더링 함수
function renderMonthlyChart(data) {
  // data: [{ brandName: "Nike", month: "2024-09", salesCount: 120 }, ...]

  // X축: 최근 12개월
  const months = [...new Set(data.map(d => d.month))].sort();

  // 범례: 브랜드 목록
  const brands = [...new Set(data.map(d => d.brandName))];

  // 시리즈 데이터
  const series = brands.map(brand => {
    return {
      name: brand,
      type: 'line',
      smooth: true,
      symbolSize: 12,
      emphasis: { focus: 'series' },
      endLabel: {
        show: false,
        formatter: '{a}',
        distance: 20
      },
      lineStyle: { width: 3 },
      data: months.map(m => {
        const found = data.find(d => d.month === m && d.brandName === brand);
        return found ? found.salesCount : 0;
      })
    };
  });

  // ECharts 옵션
  const option = {
    title: {
      text: '브랜드별 최근 12개월 판매건수',
      left: 'center'
    },
	color: [
			'#1f77b4', // 파랑
		    '#ff7f0e', // 주황
		    '#2ca02c', // 초록
		    '#d62728', // 빨강
		    '#9467bd', // 보라
		    '#8c564b', // 갈색
		    '#e377c2', // 분홍
		    '#7f7f7f', // 회색
		    '#bcbd22', // 연두
		    '#17becf', // 청록
		    '#393b79', // 진한 남색
		    '#637939', // 카키
		    '#8c6d31', // 황토
		    '#843c39', // 적갈색
		    '#7b4173', // 자주
		    '#aec7e8', // 연파랑
		    '#ffbb78', // 살구
		    '#98df8a', // 연두
		    '#ff9896', // 연분홍
		    '#c5b0d5'  // 연보라
	  ],
    tooltip: {
      trigger: 'axis',
      formatter: function (params) {
        let result = params[0].axisValue + '<br/>';
        params.forEach(p => {
          result += p.marker + ' ' + p.seriesName + ': ' + p.data + '건<br/>';
        });
        return result;
      }
    },
    grid: {
      left: '10%',
      right: '10%',
      bottom: 50,       // ✅ 범례 공간 확보 (범례 줄 수 많으면 더 크게)
      containLabel: true
    },
    legend: {
      data: brands,
      bottom: 0
    },
    toolbox: {
      feature: { saveAsImage: {} }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: months
    },
    yAxis: {
      type: 'value'
    },
    series: series,
	
  };

  // 차트 렌더링
  const chartDom = document.getElementById('monthlyChart');
  const myChart = echarts.init(chartDom);
  myChart.setOption(option, true); // ✅ notMerge로 매번 새로 그림
}

function formatToEokManwon(value) {
  const eok = Math.floor(value / 100000000);        // 억 단위
  const man = Math.floor((value % 100000000) / 10000); // 억 나머지에서 만원 단위
  let result = '';
  if (eok > 0) result += eok.toLocaleString() + '억 ';
  if (man > 0) result += man.toLocaleString() + '만원';
  if (!result) result = '0원';
  return result.trim();
}

// ✅ 차트 렌더링 함수 (브랜드별 연도별 판매금액)
function renderYearlyChart(data) {
  // data: [{ brandName: "Nike", year: "2023", salesAmount: 5000000 }, ...]

  // X축: 금액
  // Y축: 연도
  const years = [...new Set(data.map(d => d.year))].sort();
  const brands = [...new Set(data.map(d => d.brandName))];

  // 시리즈 데이터 (브랜드별)
  const series = brands.map(brand => {
    return {
      name: brand,
      type: 'bar',
      stack: 'total',
	  label: {
		show: false,
		position: 'insideRight',
		formatter: function (p) {
		  return formatToEokManwon(p.value);
		}
	  },
      data: years.map(y => {
        const found = data.find(d => d.year === y && d.brandName === brand);
        return found ? found.salesAmount : 0;
      })
    };
  });

  const option = {
    title: {
      text: '브랜드 연도별 판매금액',
      left: 'center'
    },
    color: [
      '#1f77b4','#ff7f0e','#2ca02c','#d62728','#9467bd',
      '#8c564b','#e377c2','#7f7f7f','#bcbd22','#17becf',
      '#393b79','#637939','#8c6d31','#843c39','#7b4173',
      '#aec7e8','#ffbb78','#98df8a','#ff9896','#c5b0d5'
    ],
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      formatter: function (params) {
		let result = params[0].axisValue + '<br/>';
	    params.forEach(p => {
	      result += p.marker + ' ' + p.seriesName + ': ' 
	             + formatToEokManwon(p.data) + '<br/>';
	    });
        return result;
      }
    },
    grid: {
      left: 40,
      right: 80,
      bottom: 30,
      containLabel: true
    },
    legend: {
      data: brands,
      bottom: 0,
      type: 'scroll'
    },
    xAxis: { type: 'value' },
    yAxis: { type: 'category', data: years },
    series: series
  };

  const chartDom = document.getElementById('yearlyChart');
  const myChart = echarts.init(chartDom);
  myChart.setOption(option, true);
}


// ✅ Ajax로 데이터 불러오기
function loadMonthlyChart() {
  const brands = getSelectedBrands();
  console.log("선택된 브랜드:", brands);

  if (brands.length === 0) {
    // ✅ 체크된 브랜드가 없으면 빈 차트 표시
    renderMonthlyChart([]);
    return;
  }

  $.ajax({
    url: '/api/chart/brand/monthly',
    type: 'GET',
    dataType: 'json',
    traditional: true,      // 배열 전송시 ?brands=A&brands=B 형태 유지
    data: { brands: brands },
    success: function(res) {
      console.log("서버 응답:", res);
      const list = Array.isArray(res) ? res : (res.list || []);
      renderMonthlyChart(list);
    },
    error: function(xhr, status, error) {
      console.error("에러 발생:", error);
    }
  });
}

function loadYearlyChart() {
  const brands = getSelectedBrands();

  if (brands.length === 0) {
    renderYearlyChart([]); // 빈 차트
    return;
  }

  $.ajax({
    url: '/api/chart/brand/yearly',
    type: 'GET',
    dataType: 'json',
    traditional: true,
    data: { brands: brands },
    success: function(res) {
      const list = Array.isArray(res) ? res : (res.list || []);
      renderYearlyChart(list);
    },
    error: function(xhr, status, error) {
      console.error("에러 발생:", error);
    }
  });
}



// ✅ 페이지 로딩 시 빈 차트 먼저 출력
$(document).ready(function() {
  renderMonthlyChart([]); // 빈 선 차트
  renderYearlyChart([]);  // 빈 막대 차트
});

// ✅ 체크박스 변경 이벤트 → 차트 로드
$("#brandTbody").on("change", ".row-check", function() {
  loadMonthlyChart();
  loadYearlyChart();
});

// 브랜드 삭제 버튼 클릭 이벤트
$("#brandTbody").on("click", ".btn-del", function () {
  const brandId = $(this).data("brand");
  const brandName = $(this).closest("tr").find(".brand-name").text().trim();

  if (!confirm(`브랜드 "${brandName}" 을(를) 삭제하시겠습니까?`)) return;

  $.ajax({
    url: ctx + "/admin/brands/delete",   // ✅ 컨트롤러에 맞춰 수정
    type: "POST",                        // ✅ DELETE → POST 로 변경
    data: { brandId: brandId },          // ✅ @RequestParam int brandId 에 맞춤
    success: function (res) {
      if (res === "OK") {
        alert("삭제되었습니다.");
        $(`#brandTbody tr[data-id='${brandId}']`).remove();
        loadMonthlyChart();
        loadYearlyChart();
      } else {
        alert("삭제 실패: " + res);
      }
    },
    error: function (xhr) {
      alert("삭제 중 오류 발생: " + xhr.responseText);
    }
  });
});


