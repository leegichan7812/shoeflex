function formatKoreanMoney(value) {
    value = Math.floor(value / 10000) * 10000;
    const 억 = Math.floor(value / 100000000);
    const 만 = Math.floor((value % 100000000) / 10000);
    let result = '';
    if (억 > 0) result += `${억}억 `;
    if (만 > 0) result += `${만}만원`;
    if (!result) result = '0원';
    return result.trim();
}

// 서버에서 데이터 받아서 차트 그리기
function loadPieChart(quarter) {	
	// [A] 반드시 함수 안에서 선언!
	   const chartDom = document.getElementById('admin_MainChart_02');
	   if (!chartDom) return;

	   // [B] 이미 차트 인스턴스가 있으면 먼저 dispose (중복 방지)
	   if (echarts.getInstanceByDom(chartDom)) {
	       echarts.getInstanceByDom(chartDom).dispose();
	   }
	   // [C] 반드시 함수 내에서만 let으로 선언
	   let myChart = echarts.init(chartDom);
	   
    $.ajax({
        url: '/api/chart/brand-sales-pie?quarter=' + quarter,
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            data.sort((a, b) => b.SALESTOTAL - a.SALESTOTAL);

            const chartData = data.slice(0, 19).map(d => ({
                value: d.SALESTOTAL,
                name: d.BRANDNAME,
                itemCount: d.ITEMCOUNT,
				img: d.BRANDIMG
				        ? (d.BRANDIMG.startsWith('/') ? d.BRANDIMG : '/' + d.BRANDIMG)
				        : '/img/noimg.png'
            }));

            // 커스텀 라벨: 상위 10개만 상시, 나머지는 숨김
            let dynamicIndex = null; // 마우스 오버 중인 인덱스

            const getLabelFormatter = (params) => {
                if (params.dataIndex < 10) {
                    // 상위 10개 항상 공개
                    return `${params.data.name}\n[${params.percent.toFixed(1)}%]`;
                } else if (dynamicIndex === params.dataIndex) {
                    // 마우스 오버된 조각만 공개
                    return `${params.data.name}\n[${params.percent.toFixed(1)}%]`;
                } else {
                    // 평소엔 감춤
                    return '';
                }
            };

            const option = {
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
					show: false,
                    trigger: 'item',
                    formatter: function(params) {
                        const percent = params.percent.toFixed(1);
                        const sales = formatKoreanMoney(params.value);
                        const cnt = params.data.itemCount.toLocaleString() + "건";
                        return `<b>${params.data.name} [${percent}%]</b><br>매출액 ${sales}<br>판매 ${cnt}`;
                    }
                },
                legend: { show: false },
                graphic: [
                    {
                        id: 'brand-image',
                        type: 'image',
                        left: 'center',
                        top: '38%',
                        z: 11,
                        style: {
                            image: '',
                            width: 70,
                            height: 70,
                            opacity: 0
                        }
                    },
                    {
                        id: 'brand-text',
                        type: 'text',
                        left: 'center',
                        top: '58%',
                        z: 12,
                        style: {
                            text: '',
                            font: 'bold 24px sans-serif',
                            fill: '#222',
                            textAlign: 'center'
                        }
                    }
                ],
                series: [{
                    name: '브랜드별 매출',
                    type: 'pie',
                    radius: ['35%', '90%'],
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 8, borderColor: '#fff', borderWidth: 2
                    },
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: getLabelFormatter
                    },
					labelLine: {
					    show: false,
						length: 0,           // 원에서 선까지의 거리 (px)
						length2: 5,          // 선 꺾이는 점부터 라벨까지 거리 (px)
					},
                    data: chartData
                }]
            };

            myChart.setOption(option, false);

            // hover시 label 동적 처리
            myChart.off('mouseover');
            myChart.off('mouseout');
            myChart.on('mouseover', function(params) {
                if (
                    params.componentType === 'series'
                    && params.seriesType === 'pie'
                    && params.dataIndex >= 10
                ) {
                    dynamicIndex = params.dataIndex;
                    myChart.setOption({
                        series: [{
                            label: {
                                formatter: getLabelFormatter
                            }
                        }],
                        graphic: [
                            {
                                id: 'brand-image',
                                style: {
                                    image: params.data.img || '',
                                    opacity: params.data.img ? 1 : 0
                                }
                            },
                            {
                                id: 'brand-text',
                                style: {
                                    text: params.data.name
                                }
                            }
                        ]
                    });
                } else if (
                    params.componentType === 'series'
                    && params.seriesType === 'pie'
                ) {
                    // 브랜드명/이미지는 모두에 표시
                    myChart.setOption({
                        graphic: [
                            {
                                id: 'brand-image',
                                style: {
                                    image: params.data.img || '',
                                    opacity: params.data.img ? 1 : 0
                                }
                            },
                            {
                                id: 'brand-text',
                                style: {
                                    text: params.data.name
                                }
                            }
                        ]
                    });
                }
            });
            myChart.on('mouseout', function(params) {
                if (
                    params.componentType === 'series'
                    && params.seriesType === 'pie'
                    && params.dataIndex >= 10
                ) {
                    dynamicIndex = null;
                    myChart.setOption({
                        series: [{
                            label: {
                                formatter: getLabelFormatter
                            }
                        }],
                        graphic: [
                            {
                                id: 'brand-image',
                                style: { opacity: 0 }
                            },
                            {
                                id: 'brand-text',
                                style: { text: '' }
                            }
                        ]
                    });
                } else if (
                    params.componentType === 'series'
                    && params.seriesType === 'pie'
                ) {
                    myChart.setOption({
                        graphic: [
                            { id: 'brand-image', style: { opacity: 0 } },
                            { id: 'brand-text', style: { text: '' } }
                        ]
                    });
                }
            });
			
			const total = chartData.reduce((sum, d) => sum + d.value, 0);
			// 카드에 상위 1개 먼저 표시
			const top3 = chartData.slice(0, 1).map((d, idx) => ({
			    name: d.name,
			    img: d.img,
			    percent: ((d.value / total) * 100).toFixed(1),
			    sales: formatKoreanMoney(d.value),
			    count: d.itemCount.toLocaleString() + '건'
			}));
			renderBrandInfoArea(top3);

			myChart.on('mouseover', function(params) {
			    if (params.componentType === 'series' && params.seriesType === 'pie') {
			        renderBrandInfoArea([{
			            name: params.data.name,
			            img: params.data.img,
			            percent: params.percent.toFixed(1),
			            sales: formatKoreanMoney(params.data.value),
			            count: params.data.itemCount.toLocaleString() + '건'
			        }]);
			    }
			});
			myChart.on('mouseout', function(params) {
			    // 다시 top3
			    renderBrandInfoArea(top3);
			});
        }
    });
}

function renderBrandInfoArea(brands) {
    // brands: [{name, img, percent, sales, count}]
    let html = brands.map(b => `
        <div class="brand-info-row">
            <img class="brand-img" src="${b.img ? b.img : 'noimg.png'}" alt="logo">
            <div class="brand-label-box">
                <div class="brand-name">${b.name}</div>
                <div class="brand-percent">[${b.percent}%]</div>
            </div>
            <div class="brand-sales-box">
                <div>매출: ${b.sales}</div>
                <div>판매건: ${b.count}</div>
            </div>
        </div>
    `).join('');
    $('#brandInfoArea').html(html);
}



function getQuarterTitle(quarter) {
    const now = new Date();
    const year = now.getFullYear();
    let title = '';
    if (quarter == 1 || quarter == 2) {
        title = `${year}년도 ${quarter}/4분기 데이터`;
    } else {
        title = `${year - 1}년도 ${quarter}/4분기 데이터`;
    }
    return title;
}

function loadDaySalesBarChart() {
    $.ajax({
        url: '/api/chart/recent-3month-daysales',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
			console.log('AJAX 도착 데이터:', data);
            // [month, dow, dowName, count, sales] 형식의 데이터라고 가정
            // 1. 월별 분류
            const months = [...new Set(data.map(d => d.month))].sort(); // 예: ['2024-05','2024-06','2024-07']
            const dows = ['1','2','3','4','5','6','7'];
            const dowNames = { '1':'일', '2':'월', '3':'화', '4':'수', '5':'목', '6':'금', '7':'토' };

            // 2. 월별+요일별로 count, sales 매핑
            let monthBar = {}, monthSales = {};
            months.forEach(m => {
                monthBar[m] = dows.map(d => 0);
                monthSales[m] = dows.map(d => 0);
            });
            data.forEach(row => {
                const m = row.month, d = row.dow;
                monthBar[m][d-1] = row.count;
                monthSales[m][d-1] = row.sales;
            });

            // 3. 시리즈 생성
            const series = months.map(m => ({
                name: m.replace(/^\d{2}(\d{2})-(\d{2})$/, '$1년 $2월'),
				realKey: m, // 추가!
                type: 'bar',
                data: monthBar[m],
                barGap: 0.05,
                label: {
					show: true,
					fontSize: 20,         // 너무 크지 않게
					formatter: '{c}건',					
					rotate: 90,
					align: 'left',
					verticalAlign: 'middle',
					position: 'insideBottom',
					distance: 15
                },
                emphasis: { focus: 'self' }
            }));

            // 4. ECharts 옵션
			const seriesArray = series;
            const option = {
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
					trigger: 'item',  // ✅ 막대 하나만 대상
					  axisPointer: { type: 'shadow' }, // 없어도 무방, 유지해도 됨
					  formatter: function (item) {
					    // item: 개별 데이터 (params 배열 아님)
					    let realKey = seriesArray[item.seriesIndex].realKey;
					    let idx = item.dataIndex;
					    let sales = monthSales[realKey] ? monthSales[realKey][idx] : undefined;

					    let html = item.name + "요일<br/>";
					    html += `<b>${item.seriesName}</b>: ${item.data}건`;
					    if (sales) html += ` / 매출액: <b>${formatKoreanMoney(sales)}</b>`;
					    return html;
					  }
                },
                legend: { 
					data: series.map(s => s.name), 
					top: 0,
					right:0,
					textStyle:{
						fontSize:10
					}
				},
                xAxis: [{
                    type: 'category',
                    data: dows.map(d => dowNames[d]),
					
                    axisTick: { show: false }
                }],
                yAxis: [{ type: 'value', minInterval: 0.5 }],
                series: series,
				grid: {
					left:60,
					right:20,
					top:25,
					bottom:20
				}
            };
			
			

            const chart = echarts.init(document.getElementById('admin_MainChart_01'));
            chart.setOption(option);
        }
    });
}

// 기찬 작업
// ▼ 예측 원본/적용본 캐시 (시나리오 적용/원복에 사용)
window._wdForecastCache = {
  reasons: [],
  fcWeeks: [],
  originalFcMap: {}, // {이유: [원본 예측값 배열]}
  chartF: null       // 예측 차트 인스턴스
};

// 탈퇴 이유
function chart3() {
  const el = document.getElementById('withdrawalReasonPie');
  if (!el) {
    console.warn('[anaChart] container #withdrawalReasonPie not found');
    return;
  }

  if (echarts.getInstanceByDom(el)) {
    echarts.getInstanceByDom(el).dispose();
  }

  const chart = echarts.init(el);

  // 배경이 흰색이면 '#111', 어두우면 '#fff' 로 사용
  const bg = '#fff';   // 차트 배경이 흰색이라면 그대로 두세요
  const fg = '#111';   // ★ 글자/라인 색(흰 배경용)

  const baseOption = {
    // backgroundColor: bg, // 필요 시 캔버스 배경도 명시
    tooltip: {
      trigger: 'item',
      formatter: '{b}<br/>{c}건 ({d}%)'
    },
    legend: {
      top: '5%',
      left: 'center',
      textStyle: { color: fg }          // ★ 범례 글자색
    },
    series: [{
      name: '회원 탈퇴 이유',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '70%'],
      startAngle: 180,
      endAngle: 360,                     // ECharts 5.5+
      itemStyle: {
        borderColor: bg,                 // ★ 테두리를 배경색과 동일하게
        borderWidth: 2
      },
      label: {
        color: fg,                       // ★ 라벨 글자색
        fontWeight: '600',
        formatter: p => `${p.name}\n${p.percent.toFixed(1)}%`
      },
      labelLine: {
        show: true,
        length: 18,
        length2: 10,
        smooth: true,
        lineStyle: { color: fg }         // ★ 라벨 선 색
      }
    }]
  };
  chart.setOption(baseOption);

  const url = (window.path || '') + '/api/chart/stats/withdrawal/reason-pie';

  fetch(url)
    .then(res => res.json())
    .then(list => {
      const data = Array.isArray(list) ? list.slice(0, 5) : [];
      chart.setOption({
        legend: { data: data.map(d => d.name) },
        series: [{ data }]
      });
    })
    .catch(err => {
      console.error('[reason-pie] load error', err);
      chart.setOption({
        title: {
          text: '데이터 로딩 실패',
          left: 'center',
          top: 'middle',
          textStyle: { color: fg }       // ★ 에러 문구도 보이게
        }
      });
    });

  window.addEventListener('resize', () => chart.resize());
}
// 최근 N주간 탈퇴이유
async function chartWithdrawalTrendSplit() {
  const elA = document.getElementById('withdrawalActual');
  const elF = document.getElementById('withdrawalForecast');
  if (!elA || !elF) return;

  // 재생성 가드
  [elA, elF].forEach(el => {
    const inst = echarts.getInstanceByDom(el);
    if (inst) inst.dispose();
  });

  const chartA = echarts.init(elA);
  const chartF = echarts.init(elF);

  // 공통 옵션
  const base = {
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: [] },
    yAxis: { type: 'value', min: 0, minInterval: 1 },
    grid: { left: 30, right: 20, top: 50, bottom: 30, containLabel: true },
    legend: { type: 'scroll', top: 8, data: [] },
    series: []
  };
  chartA.setOption(base);
  chartF.setOption(base);

  // ----- 데이터 로드
  const weeksParam = 24;
  const url = (window.path || '') + `/api/chart/stats/withdrawal/weekly-forecast?weeks=${weeksParam}`;
  const res = await fetch(url).then(r => r.json());

  // 문자열 정규화(공백/제로폭/NFC)
  const norm = s => (s ?? '').normalize('NFC')
    .replace(/\u00A0/g,' ').replace(/\u200B/g,'').trim();
  const toNums = a => (a || []).map(v => Number(v));
  const clamp0  = n => Math.max(0, Number(n));

  const weeks    = res.weeks || [];
  const fcWeeks  = res.forecastWeeks || [];
  const reasons  = (res.reasons || []).map(norm);

  // 실제/예측 맵 정규화
  const seriesMap = {};
  Object.keys(res.series || {}).forEach(k => seriesMap[norm(k)] = toNums(res.series[k]));

  const fcMap = {};
  Object.keys(res.forecast || {}).forEach(k => fcMap[norm(k)] = toNums(res.forecast[k]).map(clamp0));

  // -------------------- 실제 차트(스택 에어리어)
  const actualSeries = reasons.map(r => ({
    name: r,
    type: 'line',
    stack: 'total',
    areaStyle: {},
    showSymbol: false,
    emphasis: { focus: 'series' },
    data: seriesMap[r] || []
  }));

  chartA.setOption({
    xAxis: { data: weeks },
    legend: {
      data: reasons,
      itemWidth: 12, itemHeight: 8, textStyle: { fontSize: 12 }
    },
    series: actualSeries
  });

  // -------------------- 예측 차트(점선만)
  // 각 이유별 마지막 실제값(시작점 표시용; 원치 않으면 제거)
  const lastActual = {};
  reasons.forEach(r => {
    const seq = seriesMap[r] || [];
    lastActual[r] = seq.length ? Number(seq[seq.length - 1]) : 0;
  });

  const forecastSeries = reasons.map(r => {
    const pred = (fcMap[r] || []).map(clamp0);
    return {
      name: `${r}`,
      type: 'line',
      symbol: 'circle',
      lineStyle: { type: 'dashed', width: 2 },
      // 시작점으로 마지막 실제값을 한 점 추가하고 싶으면 아래처럼:
      // data: [lastActual[r], ...pred],
      // "오로지 예측만" 보이게 하려면:
      data: pred
    };
  });

  chartF.setOption({
    xAxis: { data: fcWeeks },
    legend: {
      data: forecastSeries.map(s => s.name),
      itemWidth: 12, itemHeight: 8, textStyle: { fontSize: 12 }
    },
    series: forecastSeries
  });
  
  // ▼ 캐시 저장(원본 보관)
  window._wdForecastCache.reasons  = reasons.slice();
  window._wdForecastCache.fcWeeks  = fcWeeks.slice();
  window._wdForecastCache.originalFcMap = {};
  reasons.forEach(r => {
    // 원본을 깊은 복사로 저장
    window._wdForecastCache.originalFcMap[r] = (fcMap[r] || []).slice();
  });
  window._wdForecastCache.chartF = chartF;

  // ▼ 시나리오 UI 생성(anaChart.jsp에 panel이 있을 때만)
  if (document.getElementById('scenarioControls')) {
    buildWithdrawalScenarioPanel(reasons);
    bindScenarioButtons();
  }

  // 반응형(좁은 화면일 때 범례 세로 배치)
  function applyResponsive() {
    const narrow = elA.clientWidth < 600;
    const leg = isForecast => ({
      type: 'scroll',
      orient: narrow ? 'vertical' : 'horizontal',
      left: narrow ? 6 : 'center',
      top: 8,
      itemWidth: 12, itemHeight: 8,
      itemGap: narrow ? 6 : 12,
      textStyle: { fontSize: narrow ? 11 : 12 },
      data: isForecast ? forecastSeries.map(s=>s.name) : reasons
    });
    chartA.setOption({ legend: leg(false), grid: narrow ? {left: 150, right: 10, top: 50, bottom: 30, containLabel:true} : base.grid });
    chartF.setOption({ legend: leg(true),  grid: narrow ? {left: 150, right: 10, top: 50, bottom: 30, containLabel:true} : base.grid });
  }
  applyResponsive();
  window.addEventListener('resize', () => { chartA.resize(); chartF.resize(); applyResponsive(); });
}

// 기타 상세 사유 TOP 5
async function chartWithdrawalEtcTopAll() {
  const el = document.getElementById('withdrawalEtcTopAll');
  if (!el) return;

  // 레이아웃 잡히기 전에 높이 0이면 다음 프레임에 재시도
  if (!el.offsetWidth || !el.offsetHeight) {
    requestAnimationFrame(chartWithdrawalEtcTopAll);
    return;
  }

  const prev = echarts.getInstanceByDom(el);
  if (prev) prev.dispose();
  const chart = echarts.init(el);

  // 도넛 기본 옵션
  chart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: p => `${p.name}<br/>${p.value}건 (${p.percent}%)`
    },
    legend: {
      type: 'scroll',
      top: 6,
      left: 'center',
      itemWidth: 12,
      itemHeight: 8,
      textStyle: { fontSize: 12 }
    },
    series: [{
      name: '기타 상세 사유',
      type: 'pie',
      radius: ['45%', '70%'],   // 도넛
      center: ['50%', '58%'],
      avoidLabelOverlap: true,
      label: {
        show: true,
        formatter: p => `${wrap(p.name, 12)}\n${p.value}건 (${p.percent}%)`,
        fontSize: 12
      },
      labelLine: { length: 14, length2: 10, smooth: true },
      emphasis: { scale: true, scaleSize: 4 }
    }]
  });

  chart.showLoading({ text: 'Loading…' });

  try {
    // 기본 limit=5 (컨트롤러 기본도 5)
    const url = (window.path || '') + `/api/chart/stats/withdrawal/others/top-all?limit=5`;
    const resp = await fetch(url, { headers: { 'Accept': 'application/json' } });
    const raw = await resp.text();

    let data;
    try { data = JSON.parse(raw); }
    catch { throw new Error('Not JSON: ' + raw.slice(0, 200)); }

    // 키 자동 매핑 (name/value 혹은 etc_text/cnt)
    const items = (Array.isArray(data) ? data : []).map(r => ({
      name: r.name ?? r.etc_text ?? r.ETC_TEXT ?? '',
      value: Number(r.value ?? r.cnt ?? r.CNT ?? 0)
    })).filter(x => x.name && x.value > 0);

    chart.hideLoading();

    if (!items.length) {
      chart.setOption({ title: { text: '표시할 데이터가 없습니다', left: 'center', top: 'middle' } });
      return;
    }

    chart.setOption({
      legend: { data: items.map(d => d.name) },
      series: [{ data: items }]
    });

  } catch (e) {
    chart.hideLoading();
    console.error('[etc-top-all] donut load error', e);
    chart.setOption({ title: { text: '데이터 로딩 실패', left: 'center', top: 'middle' } });
  }

  window.addEventListener('resize', () => chart.resize());
  if ('ResizeObserver' in window) new ResizeObserver(() => chart.resize()).observe(el);

  // 긴 항목 줄바꿈 헬퍼
  function wrap(str, max) {
    if (!str) return '';
    const s = String(str);
    if (s.length <= max) return s;
    const chunks = [];
    for (let i = 0; i < s.length; i += max) chunks.push(s.slice(i, i + max));
    return chunks.join('\n');
  }
}

// 사유명을 input id로 쓰기 힘들 수 있어, 인덱스로 식별
function buildWithdrawalScenarioPanel(reasons) {
  const box = document.getElementById('scenarioControls');
  if (!box) return;
  box.innerHTML = ""; // 초기화

  reasons.forEach((name, idx) => {
    const id = `sc-${idx}`;
    // 기본값(감축률 %)은 0. 필요 시 특정 사유에 디폴트 제안치 부여 가능.
    const html = `
	  <div class="sc-item">
	    <label for="${id}" style="font-size:12px; display:block; color:#333; margin-bottom:2px;">
	      ${name} <span id="${id}-val" class="text-muted">0%</span>
	    </label>
	    <input id="${id}" type="range" min="0" max="80" value="0" step="5"
	           data-reason="${name}" class="scenario-slider">
	  </div>
    `;
    box.insertAdjacentHTML('beforeend', html);
  });
  
  // chartWithdrawalTrendSplit()의 캐시 세팅 후, buildWithdrawalScenarioPanel(reasons) 다음에 추가(선택)
  (function preset(){
    const map = {
      '가격': 30,
      '서비스': 25,
      'CS': 25,
      '기능': 20
    };
    // 부분 일치 기반 적용
    const box = document.getElementById('scenarioControls');
    if (!box) return;
    const inputs = box.querySelectorAll('input[type="range"][data-reason]');
    inputs.forEach(inp => {
      const name = String(inp.dataset.reason || '');
      for (const k in map) {
        if (name.includes(k)) {
          inp.value = map[k];
          const lab = document.getElementById(inp.id + '-val');
          if (lab) lab.textContent = inp.value + '%';
          break;
        }
      }
    });
  })();
  
  // 값 표시 연동
  reasons.forEach((_, idx) => {
    const id = `sc-${idx}`;
    const slider = document.getElementById(id);
    const lab = document.getElementById(`${id}-val`);
    slider.addEventListener('input', () => lab.textContent = slider.value + '%');
  });
}

// 적용/원복 버튼 이벤트
function bindScenarioButtons() {
  const applyBtn = document.getElementById('applyScenarioBtn');
  const resetBtn = document.getElementById('resetScenarioBtn');
  if (applyBtn) applyBtn.onclick = applyWithdrawalScenario;
  if (resetBtn) resetBtn.onclick = resetWithdrawalScenario;
}

// 현재 슬라이더에서 감축률 수집 → {이유명: 0.0~0.8}
function collectScenarioEffects() {
  const effects = {}; // { reason: rate }
  const box = document.getElementById('scenarioControls');
  if (!box) return effects;

  const inputs = box.querySelectorAll('input[type="range"][data-reason]');
  inputs.forEach(inp => {
    const name = String(inp.dataset.reason || '').trim();
    const rate = Math.max(0, Math.min(0.8, Number(inp.value) / 100)); // 상한 80% 감축
    effects[name] = rate;
  });
  return effects;
}

// (핵심) 적용: 원본 예측값에 (1 - rate) 곱해서 차트 업데이트
function applyWithdrawalScenario() {
  const cache = window._wdForecastCache;
  if (!cache || !cache.chartF) return;

  const effects = collectScenarioEffects();

  // 총 변화량 계산용
  let baseSum = 0, adjSum = 0;

  const newSeries = cache.reasons.map(r => {
    const base = (cache.originalFcMap[r] || []).slice();
    const rate = effects[r] || 0;
    const adjusted = base.map(v => {
      const a = Math.max(0, Number(v) * (1 - rate));
      baseSum += Number(v) || 0;
      adjSum  += a;
      return a;
    });

    return {
      name: r,
      type: 'line',
      symbol: 'circle',
      lineStyle: { type: 'dashed', width: 2 },
      data: adjusted
    };
  });

  cache.chartF.setOption({
    xAxis: { data: cache.fcWeeks },
    legend: {
      data: newSeries.map(s => s.name),
      itemWidth: 12, itemHeight: 8, textStyle: { fontSize: 12 }
    },
    series: newSeries
  });

  // 요약 표시
  const diff = baseSum - adjSum;
  const pct = baseSum > 0 ? (diff / baseSum * 100) : 0;
  const sumEl = document.getElementById('scenarioSummary');
  if (sumEl) {
    sumEl.textContent = `적용 결과(향후 ${cache.fcWeeks.length}주): 총 예측 ${Math.round(baseSum)}건 → ${Math.round(adjSum)}건 (약 ${pct.toFixed(1)}% 감소)`;
  }
}

// 원복: 원본 예측으로 되돌리기
function resetWithdrawalScenario() {
  const cache = window._wdForecastCache;
  if (!cache || !cache.chartF) return;

  const baseSeries = cache.reasons.map(r => ({
    name: r,
    type: 'line',
    symbol: 'circle',
    lineStyle: { type: 'dashed', width: 2 },
    data: (cache.originalFcMap[r] || []).slice()
  }));

  cache.chartF.setOption({
    xAxis: { data: cache.fcWeeks },
    legend: {
      data: cache.reasons.slice(),
      itemWidth: 12, itemHeight: 8, textStyle: { fontSize: 12 }
    },
    series: baseSeries
  });

  // 슬라이더도 0으로 초기화
  const box = document.getElementById('scenarioControls');
  if (box) {
    box.querySelectorAll('input[type="range"]').forEach(inp => {
      inp.value = 0;
      const lab = document.getElementById(inp.id + '-val');
      if (lab) lab.textContent = '0%';
    });
  }
  const sumEl = document.getElementById('scenarioSummary');
  if (sumEl) sumEl.textContent = '';
}