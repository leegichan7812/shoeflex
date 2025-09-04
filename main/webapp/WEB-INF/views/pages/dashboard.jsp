<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="path" value="${pageContext.request.contextPath }"/>
<fmt:requestEncoding value="UTF-8"/>
        
<!-- partial -->
	    
		<div class="row">
			<div class="col-xl-6 col-sm-6 grid-margin stretch-card" >
				<div class="card h-100">
					<div class="card-body d-flex flex-column" style="overflow: hidden; ">
					
						<!-- NOTICE TOP5 섹션 -->
						<div id="notice" class="d-flex flex-row justify-content-between">
							<h4 class="fw-bold mb-3">공지사항 TOP 5</h4>
							<a href="#" onclick="loadAdminPage('/admin/inquiry')" class="text-muted mb-1" style="font-size: 15px;">더보기</a>
						</div>
						
						<!-- 테이블 -->
						<table class="table table-bordered table-notice-hover">
							<thead class="thead-dark text-center">
								<tr>
									<th>번호</th>
									<th>제목</th>
									<th>등록일</th>
								</tr>
							</thead>
							<tbody>
								<!-- 1) 먼저 고정된 공지들만 출력 -->
								<c:forEach var="nt" items="${noticeList}">
									<c:if test="${nt.isPinned == 'Y'}">
										<tr data-toggle="modal" data-target="#noticeModal${nt.noticeId}" style="cursor:pointer;">
											<td class="text-center">
							  					공지 <sup class="text-danger font-weight-bold">〃</sup>
											</td>
											<td class="text-left font-weight-bold">${nt.title}</td>
											<td class="text-center">
							  					<fmt:formatDate value="${nt.createdAt}" pattern="yyyy.MM.dd"/>
							  				</td>
										</tr>
									</c:if>
								</c:forEach>
							
								<!-- 2) 고정이 아닌 나머지에 번호 매기기 -->
								<c:set var="cnt" value="0"/>
								<c:forEach var="nt" items="${noticeList}">
									<c:if test="${nt.isPinned != 'Y'}">
										<!-- cnt 하나 올리고 출력 -->
										<c:set var="cnt" value="${cnt + 1}" />
											<tr data-toggle="modal" data-target="#noticeModal${nt.noticeId}" style="cursor:pointer;">
												<td class="text-center">${cnt}</td>
												<td class="text-left font-weight-bold">${nt.title}</td>
												<td class="text-center">
													<fmt:formatDate value="${nt.createdAt}" pattern="yyyy.MM.dd"/>
												</td>
											</tr>
									</c:if>
								</c:forEach>
							
								<!-- 3) 리스트가 비었을 때 처리 -->
								<c:if test="${empty noticeList}">
									<tr>
										<td colspan="3" class="text-center text-muted">
											등록된 공지사항이 없습니다.
										</td>
									</tr>
								</c:if>
							</tbody>
						</table>
						
						<!-- NOTICE 모달들 -->
						<c:forEach var="nt" items="${noticeList}">
						<div
						  class="modal fade"
						  id="noticeModal${nt.noticeId}"
						  tabindex="-1"
						  role="dialog"
						  aria-hidden="true"
						>
						  <div class="modal-dialog modal-lg" role="document">
						    <div class="modal-content bg-light border-dark">
						      <div class="modal-header bg-dark text-white">
						        <h5 class="modal-title">${nt.title}</h5>
						  <button
						    type="button"
						    class="close text-white"
						    data-dismiss="modal"
						    aria-label="Close"
						  >
						    <span aria-hidden="true">&times;</span>
						  </button>
						</div>
						<div class="modal-body">
						  <p>${nt.content}</p>
						</div>
						<div class="modal-footer text-muted">
						  조회수: ${nt.viewCount}
						&nbsp;|&nbsp;
						<fmt:formatDate value="${nt.createdAt}" pattern="yyyy.MM.dd" />
						      </div>
						    </div>
						  </div>
						</div>
						</c:forEach>
					
					
					  
					  
					</div>
				</div>
			</div>
			
			
			
		<div class="col-md-6 grid-margin stretch-card">
			<div class="card">
				<div class="card-body">
					<div class="d-flex flex-row justify-content-between">
						<h4 class="card-title mb-1" style="color:black">미처리 문의사항</h4>
						<a href="#" onclick="loadAdminPage('/admin/inquiry')" class="text-muted mb-1" style="font-size: 15px;">더보기</a>
					</div>
				  	
					<div id="recentInquiryList" class="mt-2">
					    
					</div>
				</div>
			</div>
		</div>
	  
  
		</div>
            
            
            <div class="row">
              <div class="col-md-4 grid-margin stretch-card" style="min-width: 450px;">
                <div class="card">
                  <div class="card-body" style="display: flex; flex-direction: column; align-items: center; justify-content: center;">
                    <h4 class="card-title" style="color:black">분기별 브랜드 매출 차트</h4>
                    <ul id="quarterTabs">
				        <li data-quarter="1" class="active">1분기</li>
				        <li data-quarter="2">2분기</li>
				        <li data-quarter="3">3분기</li>
				        <li data-quarter="4">4분기</li>
				    </ul>
				    <h5 id="quarterTitle"></h5>
                    <div id="admin_MainChart_02" style="width:440px; height:350px"></div>
                    
                    <div class="rounded mt-3">
                      <div id="brandInfoArea" class="brand-info-area">
                      
                      	<div class="brand-info-row" style="display: flex;">
				            <img class="brand-img" alt="logo" width="50px" height="50px">
				            <div class="brand-label-box">
				                <div class="brand-name"></div>
				                <div class="brand-percent"></div>
				            </div>
				            <div class="brand-sales-box">
				                <div></div>
				                <div></div>
				            </div>
				        </div>
                      </div>
                    </div>
                    
                  </div>
                </div>
              </div>
              
              <div class="col-xl-8 col-sm-8 grid-margin stretch-card">
				<div class="card">
					<div class="card-body" style="display: flex; flex-direction: column; align-content: center; justify-content: center;">
						<h4 class="card-title" style="text-align: center; color:black;">최근 3개월 요일별 판매건수</h4>
						<canvas id="admin_MainChart_01" width="800" height="450"></canvas>
					</div>
				</div>
			</div>
              
              
              
            </div>
            
            
            
            
         
          




<div class="modal fade" id="inquiryDetailModal" tabindex="-1" aria-labelledby="inquiryDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="inquiryDetailModalLabel">문의 상세 정보</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <div class="modal-body">
                <div class="modal-subtitle">📌 문의 정보</div>
                <div class="modal-body-section" style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; align-items: start;">
                    <div style="text-align: center;">
                        <img id="detailImageUrl" src="" alt="문의 이미지" style="max-width: 100%; height: auto; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.15);">
                    </div>
                    <div>
                        <p><strong>제품명:</strong> <span id="detailProductName"></span></p>
                        <p><strong>문의 제목:</strong> <span id="detailInquiryTitle"></span></p>
                        <p><strong>문의 날짜:</strong> <span id="detailInquiryDate"></span></p>
                        <p><strong>답변 날짜:</strong> <span id="detailAnswerDate"></span></p>
                        <p><strong>공개 여부:</strong> <span id="detailIsPublic"></span></p>
                        <p><strong>확인 관리자 ID:</strong> <span id="detailViewerAdminId"></span></p>
                        </div>
                </div>

                <div class="modal-subtitle">📝 문의 내용</div>
                <div class="highlight-box">
                    <p id="detailInquiryContent" style="margin: 0; color:black;"></p>
                </div>

                <div class="modal-subtitle">💬 답변 정보</div>
                <div class="modal-body-section">
                    </div>
                <div class="highlight-box">
                    <p id="detailAnswerContent" style="margin: 0; color:black;"></p>
                </div>

                <div class="modal-subtitle">✍️ 답변 작성 / 수정</div>
                <div class="mb-3">
                    <textarea id="answerInput" class="form-control" rows="4" placeholder="답변을 입력하세요." style="color:black;" ></textarea>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="updateAnswerBtn">답변 저장</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
	<script src="${path}/assets/vendors/js/vendor.bundle.base.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.js"></script>
    <script src="${path}/js/jb_ad.js"></script>


    <script>
    
 	// 대시보드 로딩 완료 후 호출
    loadRecentUncheckedInquiries();
    
    
 	// 탭 클릭 이벤트
    $('#quarterTabs li').on('click', function() {
        $('#quarterTabs li').removeClass('active');
        $(this).addClass('active');
        const quarter = $(this).data('quarter');
        $('#quarterTitle').text(getQuarterTitle(quarter));
        loadPieChart(quarter);
    });
 
 	// 최초 로딩(1분기)
    $(function(){
        loadPieChart(1);
        $('#quarterTitle').text(getQuarterTitle(1));
        loadDaySalesBarChart();
    });


    
    function loadAdminPage(url, callback) {
  	  $.ajax({
  	    url: url,
  	    type: 'get',
  	    dataType: 'html',
  	    success: function(response) {
  	      // ✅ 관리자 페이지 컨텐츠 영역만 교체
  	      $('.content-wrapper').html(response);

  	      // 모든 초기화 완료 후 호출할 함수
  	      const done = () => {
  	        // 캘린더 페이지면 초기화
  	        if (url.includes('calendar') && typeof initCalendarPage === 'function') {
  	          initCalendarPage();
  	        }
  	        if (url.includes('/admin/dashboard') && typeof initCharts === 'function') {
  	        	initInquiryPage();   // 🔹 라인/도넛 차트 생성
    	        }
  	        if (url.includes('/admin/ad_Users') && typeof initCharts === 'function') {
  	        	initCharts();   // 🔹 라인/도넛 차트 생성
    	        }
  	        // 모든 초기화 끝난 뒤 콜백
  	        if (typeof callback === 'function') callback();
  	      };

  	      // --- inquiry 페이지 초기화 처리 ---
  	      if (url.includes('/inquiry')) {
  	        // 1) inquiry.js가 이미 로드되어 있고 초기화 함수가 있으면 즉시 초기화
  	        if (typeof initInquiryPage === 'function') {
  	          initInquiryPage();
  	          done();
  	          return;
  	        }

  	        // 2) 아직 inquiry.js가 미로딩이면 동적 로드
  	        if (!window._inquiryScriptLoaded) {
  	          const script = document.createElement('script');
  	          script.src = '/js/inquiry.js'; // 실제 경로에 맞게 조정
  	          script.async = true;
  	          script.onload = function () {
  	            window._inquiryScriptLoaded = true;
  	            if (typeof initInquiryPage === 'function') initInquiryPage();
  	            done();
  	          };
  	          script.onerror = function() {
  	            console.error('inquiry.js 로딩 실패');
  	            done(); // 실패해도 콜백은 진행 (필요시 제거)
  	          };
  	          document.body.appendChild(script);
  	        } else {
  	          // 3) 스크립트 로드 플래그는 있는데 함수 바인딩이 늦는 드문 상황 대비
  	          const t = setInterval(() => {
  	            if (typeof initInquiryPage === 'function') {
  	              clearInterval(t);
  	              initInquiryPage();
  	              done();
  	            }
  	          }, 50);
  	          // 안전장치: 3초 후 타임아웃
  	          setTimeout(() => { try { clearInterval(t); } catch(_){} done(); }, 3000);
  	        }
  	      } else {
  	        // inquiry 페이지가 아니면 바로 done
  	        done();
  	      }
  	      // 탈퇴 이유
  	      if (url.includes('anaChart')) {
  	        if (!window.echarts) {
  	          console.error('ECharts가 로드되지 않았습니다. <script src="echarts.min.js">를 확인하세요.');
  	          return;
  	        }
  	        chart3();   // ← 여기서 호출!
  	        chartWithdrawalEtcTopAll();
  	      }
  	    },
  	    error: function() {
  	      alert('페이지 로딩 중 오류가 발생했습니다.');
  	    }
  	  });
  	}
	$('#approvalModal').on('hidden.bs.modal', function () {
	  window.location.reload(); // or: location.href = location.href;
	});
	</script>