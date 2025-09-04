<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
	.chart-row{
	  display: grid;
	  grid-template-columns: repeat(auto-fit, minmax(600px, 1fr));
	  gap: 16px;
	  align-items: stretch;
	  margin-top:15px;
	}
	.chart-card{ background:#fff;border-radius:12px;padding:12px 14px;min-width:0; }
	.chart-title{ margin: 6px 0 10px; color:black;}
	.chart-canvas{ width:100%; height:320px; min-height:280px; }
	
	/* 화면이 좁아지면 자동으로 세로 배치 */
	@media (max-width: 1100px){
	  .chart-row{ grid-template-columns: 1fr; }
	  .chart-canvas{ height: 300px; }
	}
	
	#scenarioControls {
	  display: flex;
	  flex-wrap: nowrap;   /* 무조건 한 줄 */
	  gap: 12px;           /* 아이템 사이 간격 */
	  width: 100%;
	  overflow-x: auto;    /* 좁아지면 가로 스크롤 */
	}
	
	#scenarioControls .sc-item {
	  flex: 0 0 auto;      /* 크기 고정, 줄바꿈 안 함 */
	  min-width: 180px;    /* 슬라이더 하나 최소 넓이 */
	}
	
	#scenarioControls .scenario-slider {
	  width: 100%;         /* 아이템 안에서 꽉 채우기 */
	}
	.center-card {
	  display: flex;
	  flex-direction: column;
	  justify-content: center;  /* 세로 가운데 */
	}
	
	.center-card .chart-canvas {
	  flex: 1;
	  display: flex;
	  align-items: center;      /* 그래프 세로 중앙 */
	  justify-content: center;  /* 가로 중앙도 원하면 추가 */
	}
</style>

<div class="chart-row">
	<section class="chart-card">
	    <h3 class="chart-title">회원 탈퇴 이유</h3>
	<div id="withdrawalReasonPie" class="chart-canvas"></div>
	</section>
	<section class="chart-card">
	  	<h3 class="chart-title">기타 상세 사유 TOP 5</h3>
	  <div id="withdrawalEtcTopAll" class="chart-canvas"></div>
	</section>
</div>
<div class="chart-row">
  <section class="chart-card center-card">
    <h3 class="chart-title">최근 24주 추이(실제)</h3>
    <div id="withdrawalActual" class="chart-canvas"></div>
  </section>

  <section class="chart-card">
    <h3 class="chart-title">향후 4주 예측(모델 기반)</h3>
    <!-- ▼▼ 예측 카드 안에서, forecast 차트 위쪽에 삽입 ▼▼ -->
	<div id="withdrawalScenarioPanel" class="scenario-panel" style="margin-bottom:10px;">
	  <div class="d-flex align-items-center" style="gap:8px; flex-wrap:wrap;">
	    <strong style="margin-right:8px;">대응방안 시뮬레이터</strong>
	    <!-- 동적으로 사유별 슬라이더가 여기에 생성됨 -->
	    <div id="scenarioControls" style="display:flex; gap:16px; flex-wrap:wrap;"></div>
	    <div style="margin-left:auto; display:flex; gap:8px;">
	      <button id="applyScenarioBtn" class="btn btn-sm btn-primary">적용</button>
	      <button id="resetScenarioBtn" class="btn btn-sm btn-secondary">원복</button>
	    </div>
	  </div>
	  <div id="scenarioSummary" class="text-muted" style="font-size:12px; margin-top:6px;"></div>
	</div>
    <div id="withdrawalForecast" class="chart-canvas"></div>
  </section>
</div>
<script>chartWithdrawalTrendSplit();</script>

