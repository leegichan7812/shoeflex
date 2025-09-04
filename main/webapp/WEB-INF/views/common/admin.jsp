<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="path" value="${pageContext.request.contextPath }"/>
<fmt:requestEncoding value="UTF-8"/>
<!DOCTYPE html>
<%--


 --%>
<html>
<head>
<meta charset="UTF-8">



	<!-- plugins:css -->
    <link rel="stylesheet" href="${path}/assets/vendors/mdi/css/materialdesignicons.min.css">
    <link rel="stylesheet" href="${path}/assets/vendors/css/vendor.bundle.base.css">
    <!-- endinject -->
    <!-- Plugin css for this page -->
    <link rel="stylesheet" href="${path}/assets/vendors/jvectormap/jquery-jvectormap.css">
    <link rel="stylesheet" href="${path}/assets/vendors/flag-icon-css/css/flag-icon.min.css">
    <link rel="stylesheet" href="${path}/assets/vendors/owl-carousel-2/owl.carousel.min.css">
    <link rel="stylesheet" href="${path}/assets/vendors/owl-carousel-2/owl.theme.default.min.css">
	<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
	
	<link rel="stylesheet" href="${path}/com/bootstrap.min.css" >
    <!-- End plugin css for this page -->
    <!-- inject:css -->
	<link rel="stylesheet" type="text/css" href="${path}/css/jb.css">
	<!-- FullCalendar -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.10/main.min.css">
    <!-- endinject -->
    <!-- Layout styles -->
    <link rel="stylesheet" href="${path}/assets/css/style.css">
    <link rel="stylesheet" href="${path}/css/admin.css">
    <!-- End layout styles -->
    <link rel="shortcut icon" href="${path}/assets/images/favicon.png" />

</head>

<body>
<div class="container-scroller">
      <!-- partial:partials/_sidebar.html -->
      <nav class="sidebar sidebar-offcanvas" id="sidebar">
        <div class="sidebar-brand-wrapper d-none d-lg-flex align-items-center justify-content-center fixed-top">
          <a class="sidebar-brand brand-logo" href="/admin"><img src="${path}/img/admin/logo.png" alt="logo" style="width: 200px; height: auto;"/></a>
          <a class="sidebar-brand brand-logo-mini" href="/admin"><img src="${path}/img/admin/logo-mini.png" alt="logo" style="width: 50px; height: auto;"/></a>
        </div>
        <ul class="nav">
          <li class="nav-item profile">
            <div class="profile-desc">
              <div class="profile-pic">
                <div class="count-indicator">
					<i class="mdi mdi-key-variant"
					   style="color:${grade eq 'BLACK' ? 'gray' : (grade eq 'GREEN' ? '#66ff99' : 'black')};
					          font-size:25px;">
					</i>
                </div>
                <div class="profile-name">
                  <h5 class="mb-0 font-weight-normal">${sessionScope.loginUser.name}</h5>
                  <span>${grade}</span>
                </div>
              </div>
            </div>
          </li>
          <li class="nav-item nav-category">
            <span class="nav-link">메뉴</span>
          </li>
          <li class="nav-item menu-items check">
            <a class="nav-link" href="#" onclick="loadAdminPage('/admin/dashboard')">
              <span class="menu-icon">
                <i class="mdi mdi-speedometer"></i>
              </span>
              <span class="menu-title">대시보드</span>
            </a>
          </li>
          <li class="nav-item menu-items">
            <a class="nav-link" href="#" onclick="loadAdminPage('/admin/ad_Users')">
              <span class="menu-icon" style="background: whtie;">
                <i class="mdi mdi-account-key"></i>
              </span>
              <span class="menu-title">회원조회</span>
            </a>
            
          </li>
		  <li class="nav-item menu-items">
			<a class="nav-link" data-toggle="collapse" href="#productPage" aria-expanded="false" aria-controls="productPage">
		        <span class="menu-icon"><i class="mdi mdi-gift"></i></span>
		        <span class="menu-title">상품/브랜드 관리</span>
		        <i class="menu-arrow"></i>
		    </a>
		    <div class="collapse" id="productPage">
             <ul class="nav flex-column sub-menu">
               <li class="nav-item"> <a class="nav-link" href="#" onclick="loadAdminPage('/admin/ad_product')">상품 관리</a></li>
               <li class="nav-item"> <a class="nav-link" href="#" onclick="loadAdminPage('/admin/brands')">브랜드 관리</a></li>
             </ul>
           </div>
		  </li>
          <li class="nav-item menu-items">
          	<a class="nav-link" data-toggle="collapse" href="#orderPage" aria-expanded="false" aria-controls="orderPage">
			  <span class="menu-icon">
                <i class="mdi mdi-cart"></i>
              </span>
              <span class="menu-title">주문관리</span>
              <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="orderPage">
              <ul class="nav flex-column sub-menu">
                <li class="nav-item" style="display: none;"> <a class="nav-link" href="#" onclick="">배송 관리</a></li>
                <li class="nav-item"> <a class="nav-link" href="#" onclick="loadAdminPage('/after-sales/list')">교환/반품/취소</a></li>
              </ul>
            </div>
          </li>
          <li class="nav-item menu-items">
          	<a class="nav-link" data-toggle="collapse" href="#schedulePage" aria-expanded="false" aria-controls="schedulePage">
              <span class="menu-icon">
                <i class="mdi mdi-calendar-check"></i>
              </span>
              <span class="menu-title">일정관리</span>
              <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="schedulePage">
              <ul class="nav flex-column sub-menu">
                <li class="nav-item"> <a class="nav-link" href="#" onclick="loadAdminPage('/support/noticeA')">공지관리</a></li>
                <li class="nav-item"> <a class="nav-link" href="#" onclick="loadAdminPage('/calendar')">스케줄러</a></li>
              </ul>
            </div>
          </li>
          <li class="nav-item menu-items">
          	<a class="nav-link" data-toggle="collapse" href="#inquiryPage" aria-expanded="false" aria-controls="inquiryPage">
              <span class="menu-icon">
                <i class="mdi mdi-headset"></i>
              </span>
              <span class="menu-title">문의관리</span>
              <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="inquiryPage">
              <ul class="nav flex-column sub-menu">
                <li class="nav-item"> <a class="nav-link" href="#" onclick="loadAdminPage('/admin/inquiry')">상품문의</a></li>
                <li class="nav-item" style="display: none;"> <a class="nav-link" href="#" onclick="">상품리뷰</a></li>
                <li class="nav-item"> <a class="nav-link" id="chatToggleBtn" href="javascript:void(0)">1:1 문의</a></li>
                <li class="nav-item"> <a class="nav-link" href="#" onclick="loadAdminPage('/faqA')">FAQ</a></li>
              </ul>
            </div>
          </li>
          <li class="nav-item menu-items">
            <a class="nav-link" data-toggle="collapse" href="#anaPage" aria-expanded="false" aria-controls="anaPage">
              <span class="menu-icon">
                <i class="mdi mdi-chart-line"></i>
              </span>              
              <span class="menu-title">분석페이지</span>
              <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="anaPage">
              <ul class="nav flex-column sub-menu">
                <li class="nav-item"> <a class="nav-link" href="#" onclick="loadAdminPage('/anaChart')">탈퇴사유 분석</a></li>
                <li class="nav-item"> <a class="nav-link" href="#" onclick="loadAdminPage('/withdrawalManual_List')">탈퇴사유 대응방안</a></li>
                <li class="nav-item"> <a class="nav-link" href="#" onclick="loadAdminPage('/exceptionLogs')">에러로그 조회</a></li>
                <li class="nav-item"> <a class="nav-link" href="pages/ui-features/typography.html">에러 페이지</a></li>
              </ul>
            </div>
          </li>
          
			
          
        </ul>
        
      </nav>
		<!-- partial -->
		<div class="container-fluid page-body-wrapper">
	        <!-- partial:partials/_navbar.html -->
			<nav class="navbar p-0 fixed-top d-flex flex-row">
				<div class="navbar-brand-wrapper d-flex d-lg-none align-items-center justify-content-center">
					<a class="navbar-brand brand-logo-mini" href="index.html"><img src="${path}/assets/images/logo-mini.svg" alt="logo" /></a>
				</div>
				<div class="navbar-menu-wrapper flex-grow d-flex align-items-stretch">
			        
					<ul class="navbar-nav navbar-nav-right">
			        	
						<button class="navbar-toggler navbar-toggler align-self-center" type="button" data-toggle="minimize">
							<span class="mdi mdi-menu" style="font-size: 25px;"></span>
						</button>
						
			 
			 
					 	<!-- 일정 승인 알림 -->
						<li class="nav-item dropdown border-left">
							<a class="nav-link count-indicator dropdown-toggle" id="notifDropdown" href="#" data-toggle="dropdown" aria-expanded="false">
								<i class="mdi mdi-bell"></i>
								<span id="pendingCount" class="count bg-danger">0</span>
							</a>
							<div class="dropdown-menu dropdown-menu-right navbar-dropdown preview-list" aria-labelledby="notifDropdown" 
							style="min-width:200px; max-height:500px; overflow:auto;">
								<h6 class="p-3 mb-0">알림</h6>
								<div class="dropdown-divider"></div>
								<div id="notifList">
									<p class="text-muted text-center my-3">알림이 없습니다.</p>
								</div>
								<div class="dropdown-divider"></div>
								<div class="dropdown-item small text-muted" style="text-align: center;">
									<a href="#" onclick="refreshNotifications(); return false;" style="width: 100%; color:inherit; text-decoration: none;">새로고침</a>
								</div>
							</div>
						</li>
						<!-- 일정 승인 알림 end -->
			
						<!-- 프로필 -->
						<li class="nav-item dropdown border-left">
							<a class="nav-link" id="profileDropdown" href="#" data-toggle="dropdown">
								<div class="navbar-profile">
									<i class="mdi mdi-key-variant" style="color:${grade eq 'BLACK' ? 'gray' : (grade eq 'GREEN' ? '#66ff99' : 'black')}; font-size:25px; margin-right:-5px; margin-bottom: -3px;"></i>
									<p class="mb-0 d-none d-sm-block navbar-profile-name" style="font-size:20px;">${sessionScope.loginUser.name}&nbsp;</p>
									<i class="mdi mdi-menu-down d-none d-sm-block" style="margin-right: 10px;"></i>
								</div>															
							</a>
							<div class="dropdown-menu dropdown-menu-right navbar-dropdown preview-list" aria-labelledby="profileDropdown">               
								<a class="dropdown-item preview-item logout-link" href="${pageContext.request.contextPath}/logout2">
									<div class="preview-thumbnail">
										<div class="preview-icon bg-dark rounded-circle">
											<i class="mdi mdi-logout text-danger"></i>
										</div>
									</div>
									<div class="preview-item-content">
										<p class="preview-subject mb-1">로그아웃</p>
									</div>
								</a>
							</div>
						</li>
						<!-- 프로필 end -->			
					</ul>
					<button class="navbar-toggler navbar-toggler-right d-lg-none align-self-center" type="button" data-toggle="offcanvas">
						<span class="mdi mdi-format-line-spacing"></span>
					</button>
				</div>
			</nav>
        
        
<!-- partial -->
<div class="main-panel mt-3">
	<div class="content-wrapper" style="background:#222222;">
	    
		<div class="row">
			<div class="col-xl-6 col-sm-6 grid-margin stretch-card" >
				<div class="card h-100">
					<div class="card-body d-flex flex-column" style="overflow: hidden; ">
					
						<!-- NOTICE TOP5 섹션 -->
						<div id="notice" class="d-flex flex-row justify-content-between">
							<h4 class="fw-bold mb-3">공지사항 TOP 5</h4>
							<a href="#" onclick="loadAdminPage('/support/noticeA')" class="text-muted mb-1" style="font-size: 15px;">더보기</a>
							
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
						<div id="admin_MainChart_01" style="width:800px; height:450px;"></div>
					</div>
				</div>
			</div>
              
              
              
            </div>
            
            
            
            
          </div>
          <!-- content-wrapper ends -->
          <!-- partial:partials/_footer.html -->
          <footer class="footer">
            <div class="d-sm-flex justify-content-center justify-content-sm-between">
              <span class="text-muted d-block text-center text-sm-left d-sm-inline-block">Copyright © bootstrapdash.com 2020</span>
              <span class="float-none float-sm-right d-block mt-1 mt-sm-0 text-center"> Free <a href="https://www.bootstrapdash.com/bootstrap-admin-template/" target="_blank">Bootstrap admin templates</a> from Bootstrapdash.com</span>
            </div>
          
         <!-- 채팅 위젯 -->
		<div id="chatWidget" class="card shadow" 
		     style="position: fixed; bottom: 20px; right: 20px; width: 300px; height: 400px;
		            z-index: 9999; border-radius: 12px; overflow: hidden; display: none;">
		  <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
		    <span><i class="fa fa-comments"></i> 1:1 고객센터</span>
		    <button type="button" class="btn btn-sm btn-light" id="chatCloseBtn">&times;</button>
		  </div>
		  <div class="card-body p-2" id="chatMessages" 
		       style="height: 300px; overflow-y: auto; background-color: #fdfdfd;">
		    <!-- 메시지가 여기에 표시됨 -->
		  </div>
		  <div class="card-footer p-2">
			<div class="input-group input-group-sm">
			  <input type="text" id="chatInput"
			         class="form-control bg-white text-dark"
			         placeholder="메시지를 입력하세요">
			  <div class="input-group-append">
			    <button class="btn btn-success" id="chatSendBtn">전송</button>
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


		<!-- 승인 대기 일정 모달 -->
		<div class="modal fade" id="approvalModal" tabindex="-1">
		  <div class="modal-dialog modal-lg modal-dialog-centered">
		    <div class="modal-content shadow">
		      <div class="modal-header bg-dark text-white">
		        <h5 class="modal-title">승인 대기 일정 목록</h5>
		        <button type="button" class="close text-white" data-bs-dismiss="modal" style="padding-right:25px;padding-top:25px;">
		        	<span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body p-3" style="background-color: #f9f9f9;">
		        <ul id="pendingList" class="list-group">
		          <li class="list-group-item text-muted text-center">불러오는 중...</li>
		        </ul>
		      </div>
		    </div>
		  </div>
		</div>



          </footer>
          <!-- partial -->
        </div>
        <!-- main-panel ends -->
      </div>
      <!-- page-body-wrapper ends -->
    </div>
    <!-- container-scroller -->
    <!-- plugins:js -->
    <script src="${path}/assets/vendors/js/vendor.bundle.base.js"></script>
    <script src="assets/js/off-canvas.js"></script>
    <script src="assets/js/hoverable-collapse.js"></script>
    <!-- endinject -->
    <!-- 차트 -->
    <script src="https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/echarts@5.5.0/dist/echarts.min.js"></script>
    <!-- FullCalendar -->
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.10/index.global.min.js"></script>
    <!-- Custom js for this page -->
    <script>
	  window.adminGrade = "${grade}";  // ✅ 변수명 일치시키기
	</script>
    
    <script src="${path}/assets/js/dashboard.js"></script>
    <script src="${path}/js/CER.js"></script>
    <script src="${path}/js/gc.js"></script>
    <script src="${path}/js/jb_ad.js"></script>    
    <script src="${path}/js/chart.js"></script>
    <script src="${path}/js/calendar.js"></script>    
    <script src="${path}/js/ad_Insert.js"></script> 
	<script src="${path}/js/ad_Read.js"></script> 
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	<script>
	  // GlobalControllerAdvice에서 내려준 값 사용
	  window.notifyServer = '${notifyServer}';
	</script>
	
    <script>window.ctx = window.ctx || '${pageContext.request.contextPath}';</script>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <script src="${path}/js/inquiry.js"></script>
    <script src="${path}/js/notify.js"></script>
    <!-- End custom js for this page -->
    
    
	    
    <script>
 	// 대시보드 로딩 완료 후 호출
    loadRecentUncheckedInquiries();
    initInquiryPage();
    
    
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

    function goToNoticeAdmin() {
        loadAdminPage('/support/noticeA');
    }
    
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
    	        // 대시보드
    	        if (url.includes('/admin/dashboard') && typeof initInquiryPage === 'function') {
    	          initInquiryPage();   // 🔹 라인/도넛 차트 생성
    	        }
    	        // 사용자 통계
    	        if (url.includes('/admin/ad_Users') && typeof initCharts === 'function') {
    	          initCharts();        // 🔹 라인/도넛 차트 생성
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
    	            done(); // 실패해도 콜백은 진행
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
    	        return; // inquiry 처리 끝
    	      }

    	      // ✅ noticeA 분기 (공지 수정 모달 초기화)
    	      if (url.includes('/support/noticeA')) {
    	        // 모달 래퍼가 있으면 body로 이동(선택: z-index/backdrop 이슈 예방)
    	        const $wrap = $('#editNoticeModals');
    	        if ($wrap.length && !$wrap.parent().is('body')) {
    	          $('body').append($wrap);
    	        }
    	        if (typeof initNoticeEditModals === 'function') {
    	          initNoticeEditModals();
    	        }
    	        // _swapNotice로 목록/모달을 교체하는 경우, 스왑 후 재초기화를 보장(1회 훅)
    	        if (typeof _swapNotice === 'function' && !window.__noticeSwapHooked) {
    	          window.__noticeSwapHooked = true;
    	          const origSwap = _swapNotice;
    	          window._swapNotice = function(html) {
    	            origSwap(html);
    	            if (typeof initNoticeEditModals === 'function') initNoticeEditModals();
    	          };
    	        }
    	        done();
    	        return; // noticeA 처리 끝
    	      }

    	      // inquiry/noticeA 이외 페이지는 바로 done
    	      done();

    	      // 탈퇴 이유(anaChart) — 해당 URL에서만
    	      if (url.includes('anaChart')) {
    	        if (!window.echarts) {
    	          console.error('ECharts가 로드되지 않았습니다. <script src="echarts.min.js">를 확인하세요.');
    	          return;
    	        }
    	        chart3();
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
	
<script>
  window.chatConfig = {
    socketServer: "${socketServer}",
    currentUser: "${sessionScope.loginUser != null ? sessionScope.loginUser.name : '관리자'}",
    accountType: "ADMIN"   // ✅ 관리자에서는 반드시 ADMIN
  };
</script>
<!-- 반드시 chatConfig 정의 후에 mc.js 불러오기 순서변경 xxxx-->
<script src="${path}/js/mc.js"></script>


	
	
	
  </body>
</html>