// admin_user.js

// 전역 차트 객체
let purchaseChart = null;
let doughnutChart = null;

// 차트 초기화 함수
function initCharts() {
  // 구매 금액 라인 차트
  const purchaseCtx = document.getElementById('purchaseChart');
  if (purchaseCtx) {
    // 이미 존재하면 파괴 후 다시 생성 (중복 방지)
    if (purchaseChart) {
      purchaseChart.destroy();
    }
    purchaseChart = new Chart(purchaseCtx.getContext('2d'), {
      type: 'line',
      data: {
        labels: [],
        datasets: [{
          label: '구매 금액 (원)',
          data: []
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }

  // 판매 건수 도넛 차트
  const canvas = document.getElementById('doughnutChart');
    if (!canvas) return;

    const ctx = canvas.getContext('2d');

    // 이미 차트가 있으면 파괴
    if (doughnutChart) {
      doughnutChart.destroy();
    }

    // 새 도넛 차트 생성 (초기 데이터 포함)
    doughnutChart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['3월', '4월', '5월', '6월', '7월', '8월'],
        datasets: [{
          label: '판매 건수',
          data: [25, 35, 30, 40, 33, 45],
          backgroundColor: [
            '#FF6384', '#36A2EB', '#FFCE56',
            '#4BC0C0', '#9966FF', '#FF9F40'
          ],
          borderColor: 'white',
          borderWidth: 2,
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: 'bottom' }
        }
      }
    });
}
const doughnutCtx = document.getElementById('doughnutChart').getContext('2d');
	  doughnutChart = new Chart(doughnutCtx, {
	    type: 'doughnut',
	    data: {
	      labels: ['3월', '4월', '5월', '6월', '7월', '8월'],
	      datasets: [{
	        label: '판매 건수',
	        data: [25, 35, 30, 40, 33, 45],
	        backgroundColor: ['#FF6384','#36A2EB','#FFCE56','#4BC0C0','#9966FF','#FF9F40'],
	        borderColor: 'white',
	        borderWidth: 2,
	      }]
	    },
	    options: {
	      responsive: true,
	      maintainAspectRatio: false,
	      plugins: {
	        legend: { position: 'bottom' }
	      }
	    }
	  });

// 차트 업데이트 (임시 데이터 or Ajax)
function updateChart(userId) {
  const labels = window.chartLabels || [];
  const dataMap = {
    'hong@example.com': [120000, 150000, 100000, 180000, 130000, 170000],
    'kim@example.com': [90000, 110000, 130000, 90000, 120000, 150000],
  };
  const data = dataMap[userId] || [100000, 120000, 110000, 140000, 125000, 160000];

  if (!purchaseChart) return;
  purchaseChart.data = {
    labels: labels,
    datasets: [{
      label: '구매 금액 (원)',
      data: data,
      borderColor: '#764ba2',
      backgroundColor: 'rgba(118,75,162,0.2)',
      fill: true,
      tension: 0.4,
      pointRadius: 3,
      pointBackgroundColor: '#764ba2',
      borderWidth: 1,
    }]
  };
  purchaseChart.update();
}

// Ajax 로 차트 데이터 불러오기
function loadChartData() {
  const email = $("#emailInput").val();
  $.ajax({
    url: "chartJson",
    dataType: "json",
    data: { userId: email },
    success: function(chart) {
      if (chart && chart.mons) {
        doughnutChart.data.datasets[0].data = chart.salesCounts;
        doughnutChart.data.labels = chart.mons;

        purchaseChart.data.datasets[0].data = chart.monthTots;
        purchaseChart.data.labels = chart.mons;

        doughnutChart.update();
        purchaseChart.update();
      }
    },
    error: function(err) {
      console.log("차트 데이터 로딩 실패", err);
    }
  });
}

// 회원 검색
function searchUser() {
  const email = document.getElementById("emailInput").value.trim();
  if (!email) {
    alert("이메일을 입력하세요.");
    return;
  }
  $.ajax({
    url: `/admin/ad_Users/data`,
    data: { email: email },
    method: "GET",
    dataType: "json",
    success: function(res) {
      renderUserData(res);
    },
    error: function(err) {
      console.error("검색 실패", err);
      $("#resultArea").html("<p style='color:red'>검색 중 오류가 발생했습니다.</p>");
    }
  });
}

// 차트에 새 데이터 반영
function updateCharts(chartData) {
  if (!purchaseChart || !doughnutChart) {
    console.warn("차트가 아직 초기화되지 않았습니다.");
    return;
  }
  const labels = chartData.map(c => c.mon);
  const salesCounts = chartData.map(c => c.salesCount);
  const monthTots = chartData.map(c => c.monthTot);

  purchaseChart.data.labels = labels;
  purchaseChart.data.datasets[0].data = monthTots;
  purchaseChart.update();

  doughnutChart.data.labels = labels;
  doughnutChart.data.datasets[0].data = salesCounts;
  doughnutChart.update();
}

// 회원 정보 + 리뷰 + QnA 렌더링
function renderUserData(data) {
  $("#name").val(data.member?.name || "");
  $("#gender").val(data.member?.gender || "");
  $("#age").val(data.member?.age || "");
  $("#email").val(data.member?.email || "");
  $("#phone").val(data.member?.phone || "");
  if (data.member?.createdAt) {
    const createdDate = new Date(data.member.createdAt);
    $("#joinDate").val(createdDate.toISOString().split("T")[0]);
  } else {
    $("#joinDate").val("");
  }
  if (data.member?.updatedAt) {
    const updatedDate = new Date(data.member.updatedAt);
    $("#lastActiveDate").val(updatedDate.toISOString().split("T")[0]);
  } else {
    $("#lastActiveDate").val("");
  }
  $("#memo").val(data.member?.userNote || "");

  // 리뷰 테이블
  let reviewHtml = "";
  if (data.reviewlist?.length > 0) {
    data.reviewlist.forEach(r => {
      const productName = r.productName
        ? (r.productName.length > 12 ? r.productName.substring(0, 12) + "..." : r.productName)
        : "";
      const content = r.content
        ? (r.content.length > 12 ? r.content.substring(0, 12) + "..." : r.content)
        : "";
      reviewHtml += `<tr>
        <td>${productName}</td>
        <td>${content}</td>
        <td>${r.rating ?? ""}</td>
        <td>${r.createdAt ? new Date(r.createdAt).toISOString().split("T")[0] : ""}</td>
      </tr>`;
    });
  } else {
    reviewHtml = `<tr><td colspan="4">리뷰가 없습니다.</td></tr>`;
  }
  $(".review-table tbody").html(reviewHtml);

  // QnA 테이블
  let qnaHtml = "";
  if (data.qnalist?.length > 0) {
    data.qnalist.forEach(q => {
      qnaHtml += `<tr>
        <td>${q.categoryName || ""}</td>
        <td>${q.inquiryTitle || ""}</td>
        <td>${q.inquiryDate ? new Date(q.inquiryDate).toISOString().split("T")[0] : ""}</td>
        <td>${q.status || ""}</td>
      </tr>`;
    });
  } else {
    qnaHtml = `<tr><td colspan="4">QnA가 없습니다.</td></tr>`;
  }
  $(".qna-table tbody").html(qnaHtml);

  // 차트 업데이트
  if (data.chartlist) updateCharts(data.chartlist);
}
