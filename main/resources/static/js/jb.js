(function ($) {
    "use strict";
	
	
	
	

    /*==================================================================
    [ 장바구니 ]
    ==================================================================*/
	$(document).ready(function () {
		// 장바구니 아이콘과 장바구니 영역 DOM 요소 가져오기
		const cartIcon = document.querySelector('.js-show-cart');
		const cartWrapper = document.querySelector('.wrap-header-cart');

		// 장바구니 고정 상태 여부 (true면 열림 고정 상태)
		let cartLockedOpen = false;

		// 조건부 렌더링된 경우를 고려해서 요소가 존재할 때만 이벤트 바인딩
		if (cartIcon && cartWrapper) {

			// 마우스가 장바구니 아이콘 위에 올라갔을 때 → 장바구니 보여주기
			cartIcon.addEventListener('mouseenter', () => {
				cartWrapper.classList.add('show-header-cart'); // CSS로 보여짐 처리
			});
			// 마우스가 장바구니 아이콘에서 빠졌을 때
			cartIcon.addEventListener('mouseleave', () => {
				setTimeout(() => {
					// 고정 상태가 아니면 장바구니 닫기
					if (!cartLockedOpen) {
						cartWrapper.classList.remove('show-header-cart');
					}
				}, 500); // 0.5초 후 실행 (자연스러운 UX)
			});
			// 장바구니 아이콘 클릭 시
			cartIcon.addEventListener('click', (e) => {
				e.preventDefault(); // a 태그 링크 기본 동작 막기

				if (cartLockedOpen) {
					// 이미 열려 있는 상태 → 장바구니 페이지로 이동
					loadPage('cart')
				} else {
					// 닫힌 상태 → 장바구니 고정 상태로 열기
					cartLockedOpen = true;
					cartWrapper.classList.add('show-header-cart');
				}
			});
			// 장바구니 영역에서 마우스가 빠지면 닫힘
			cartWrapper.addEventListener('mouseleave', () => {
				cartLockedOpen = false; // 고정 해제
				cartWrapper.classList.remove('show-header-cart'); // 숨기기
			});
		}
	});
	
	/*==================================================================
	[ 검색창 ]
	==================================================================*/
	$(document).ready(function () {
		const $searchInput = $('#search-input');
		const $searchSwitch = $('.search-switch');
	
		// 검색 아이콘 클릭 → 열기/닫기 토글
		$searchSwitch.on('click', function (e) {
			e.preventDefault();

			if ($searchInput.hasClass('active')) {
			// 이미 열려 있으면 닫기
			$searchInput.removeClass('active').val('');
			} else {
			// 안 열려 있으면 열기 + 포커스
			$searchInput.addClass('active').focus();
			}
		});

		// 바깥 영역 클릭 시 닫기
		$(document).on('click', function (e) {
			if (
				!$searchInput.is(e.target) &&
				!$searchSwitch.is(e.target) &&
				$searchSwitch.has(e.target).length === 0
			) {
				$searchInput.removeClass('active').val('');
			}
		});
	});
	
	$(document).on('click', '#logoutBtn', function(e) {
	    e.preventDefault();

	    if (confirm("로그아웃 하시겠습니까?")) {
	        $.ajax({
	            url: 'logout',
	            type: 'GET',
	            success: function(response) {
	                if (response === 'success') {
	                    location.reload();
	                } else {
	                    alert("로그아웃 실패. 다시 시도해주세요.");
	                }
	            },
	            error: function() {
	                alert("로그아웃 중 오류가 발생했습니다.");
	            }
	        });
	    }
	});
	
})(jQuery);






function initSlick1() {
    $('.wrap-slick1').each(function () {
        const wrapSlick1 = $(this);
        const slick1 = wrapSlick1.find('.slick1');
        const itemSlick1 = slick1.find('.item-slick1');
        const layerSlick1 = slick1.find('.layer-slick1');
        let actionSlick1 = [];

        // 중복 초기화 방지
        if (slick1.hasClass('slick-initialized')) return;

        slick1.on('init', function () {
            const layerCurrentItem = $(itemSlick1[0]).find('.layer-slick1');

            for (let i = 0; i < actionSlick1.length; i++) clearTimeout(actionSlick1[i]);

            layerSlick1.each(function () {
                $(this).removeClass($(this).data('appear') + ' visible-true');
            });

            for (let i = 0; i < layerCurrentItem.length; i++) {
                actionSlick1[i] = setTimeout(function (index) {
                    $(layerCurrentItem[index]).addClass(
                        $(layerCurrentItem[index]).data('appear') + ' visible-true'
                    );
                }, $(layerCurrentItem[i]).data('delay'), i);
            }
        });

        slick1.slick({
            pauseOnFocus: false,
            pauseOnHover: false,
            slidesToShow: 1,
            slidesToScroll: 1,
            fade: true,
            speed: 1000,
            infinite: true,
            autoplay: true,
            autoplaySpeed: 6000,
            arrows: true,
            appendArrows: wrapSlick1,
            prevArrow:
                '<button class="arrow-slick1 prev-slick1"><i class="zmdi zmdi-caret-left"></i></button>',
            nextArrow:
                '<button class="arrow-slick1 next-slick1"><i class="zmdi zmdi-caret-right"></i></button>',
            dots: wrapSlick1.find('.wrap-slick1-dots').length > 0,
            appendDots: wrapSlick1.find('.wrap-slick1-dots'),
            dotsClass: 'slick1-dots',
            customPaging: function (slick, index) {
                var linkThumb = $(slick.$slides[index]).data('thumb');
                var caption = $(slick.$slides[index]).data('caption');
                return (
                    '<img src="' + linkThumb + '">' +
                    '<span class="caption-dots-slick1">' +
                    caption +
                    '</span>'
                );
            },
        });

        slick1.on('afterChange', function (event, slick, currentSlide) {
            const layerCurrentItem = $(itemSlick1[currentSlide]).find('.layer-slick1');

            for (let i = 0; i < actionSlick1.length; i++) clearTimeout(actionSlick1[i]);

            layerSlick1.each(function () {
                $(this).removeClass($(this).data('appear') + ' visible-true');
            });

            for (let i = 0; i < layerCurrentItem.length; i++) {
                actionSlick1[i] = setTimeout(function (index) {
                    $(layerCurrentItem[index]).addClass(
                        $(layerCurrentItem[index]).data('appear') + ' visible-true'
                    );
                }, $(layerCurrentItem[i]).data('delay'), i);
            }
        });
    });
}
// 공통: 이메일/비번 검증 유틸
function isValidEmail(email) {
  // 끝까지 묶여야 함 (앵커 포함)
  const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,}$/;
  return re.test(String(email).trim());
}
function isValidPassword(pw) {
  // 영문 + 숫자 + 특수문자, 10~20자
  return /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+]).{10,20}$/.test(pw || "");
}

function initJoinFormValidation() {
	// 이메일 중복 검사 + 유효성 검사
	$('#email').on('input', function () {
		const email = $(this).val().trim();
		const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,}$/;
		const msg = $('#email-check-msg');

		if (!emailRegex.test(email)) {
			msg.text("❌ 유효한 이메일 형식이 아닙니다.")
				.css("color", "red");
			$(this).removeClass('valid-input').addClass('invalid-input');
			return;
		}
		
		// 중복 체크
		fetch("checkId?email=" + encodeURIComponent(email))
			.then(response => response.json())
			.then(isAvailable => {
				if (isAvailable) {
					msg.text("✅ 사용 가능한 이메일입니다.").css("color", "green");
					$('#email').removeClass('invalid-input').addClass('valid-input');
				} else {
					msg.text("❌ 이미 등록된 이메일입니다.").css("color", "red");
					$('#email').removeClass('valid-input').addClass('invalid-input');
				}
			})
			.catch(error => {
				console.error("이메일 중복 체크 중 에러:", error);
			});
	});

	// 비밀번호 유효성 검사
	$('#password').on('input', function () {
		const password = $(this).val();
		const pwdRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+]).{10,20}$/;

		if (!pwdRegex.test(password)) {
			$('#pwdValid').text('❌ 영문, 숫자, 특수문자 포함 10~20자리로 입력하세요.')
				.removeClass('valid-msg').addClass('error-msg');
			$(this).removeClass('valid-input').addClass('invalid-input');
		} else {
			$('#pwdValid').text('✅ 사용 가능한 비밀번호입니다.')
				.removeClass('error-msg').addClass('valid-msg');
			$(this).removeClass('invalid-input').addClass('valid-input');
		}
	});

	// 비밀번호 일치 검사
	$('#confirmPassword').on('input', function () {
		const pwd = $('#password').val();
		const confirm = $(this).val();

		if (pwd === confirm && pwd !== "") {
			$('#pwdMatch').text('✅ 비밀번호가 일치합니다.')
				.removeClass('error-msg').addClass('valid-msg');
			$(this).removeClass('invalid-input').addClass('valid-input');
		} else {
			$('#pwdMatch').text('❌ 비밀번호가 일치하지 않습니다.')
				.removeClass('valid-msg').addClass('error-msg');
			$(this).removeClass('valid-input').addClass('invalid-input');
		}
	});


}
/**
 * 로그인 폼 이벤트 핸들러 초기화
 * - Ajax 기반 로그인 요청 처리
 * - 일반 유저: loadPage('main')으로 페이지 일부 갱신
 * - 관리자: location.href로 관리자 페이지 이동
 */
function initLoginFormHandler() {

	// 기존 이벤트 제거 (중복 바인딩 방지)
	$(document).off('submit', '#loginForm');

	// 로그인 폼 submit 이벤트 바인딩
	$(document).on('submit', '#loginForm', function(e) {
		e.preventDefault(); // 기본 폼 제출 막기

		const formData = $(this).serialize(); // 폼 데이터 직렬화

		$.ajax({
			url: 'glogin',  // ✅ 로그인 요청 URL (glogin으로 변경)
			type: 'POST',
			data: formData,
			success: function(response) {

				// ✅ 로그인 성공 - 일반 유저일 경우
				if (response.result === 'user') {
					location.reload();
				}
				// ✅ 로그인 성공 - 관리자일 경우
				else if (response.result === 'admin') {
					alert("관리자 페이지로 이동합니다")
					location.href = '/admin'; // 관리자 페이지로 이동 (전체 페이지 전환)
				}
				// ✅ 로그인 실패
				else if (response.result === 'fail') {
					$('#login-error-msg').html(`<p class="error-msg">아이디 또는 비밀번호를 확인하세요.</p>`);
				}
			},
			error: function() {
				// ✅ Ajax 요청 실패 처리
				$('#login-error-msg').html(`<p class="error-msg">로그인 중 오류가 발생했습니다.</p>`);
			}
		});
	});
}

/*==================================================================
    [ shop 페이지 가격 bar ]
 ==================================================================*/
let currentBrandId = null;
let currentCategoryId = null; // ✅ 추가
let currentIndex1 = 0; // 브랜드 슬라이드용
let currentIndex2 = 0; // 브랜드 슬라이드용
let mode = window.shopMode;
 
function initFilterUI() {
    initPriceSlider();
    bindFilterEvents();

	if (mode === "brand") {
	    $('.brand-prev, .brand-next').off(); // 카테고리 이벤트 지우기
	    initBrandSlide(currentBrandId);
	} else if (mode === "category") {
	    $('.brand-prev, .brand-next').off(); // 브랜드 이벤트 지우기
	    initCategorySlide(currentCategoryId);
	}
}

function initPriceSlider() {
    $("#slider-range").slider({
        range: true,
        min: 0,
        max: 500000,
		step: 10000, // ✅ 만원 단위로 이동
        values: [30000, 400000],
        slide: function(event, ui) {
            $("#amount").val(ui.values[0] + "원  ~  " + ui.values[1] + "원");
        }
    });

    $("#amount").val($("#slider-range").slider("values", 0) + "원 - " + 
                     $("#slider-range").slider("values", 1) + "원");
}

function initBrandSlide(brandId){
    const $items = $('.brand-item');

    currentBrandId = brandId || null;

    if (brandId) {
        $items.each(function(index){
            if ($(this).data('brand-id') == brandId) {
                currentIndex1 = index;
                return false; // break
            }
        });
    }

    function updateBrandSlide() {
        $items.removeClass('active');
        $items.eq(currentIndex1).addClass('active');

        const slideWidth = 60;
        const gap = 20;
        const containerWidth = 540;
		
		if ($items.length * (slideWidth + gap) <= containerWidth) {
	        $('.brand-slide').css('transform', 'translateX(0)');
	        return;
	    }

        const offset = (containerWidth / 2) - ((currentIndex1 + 0.5) * slideWidth) - (currentIndex1 * gap);
        $('.brand-slide').css('transform', `translateX(${offset}px)`);
    }

    $('.brand-prev').off('click').on('click', function(){
        if (currentIndex1 > 0) {
            currentIndex1--;
            currentBrandId = $items.eq(currentIndex1).data('brand-id');
			currentCategoryId = null;
            updateBrandSlide();
            applyFilter(currentBrandId, currentCategoryId, "brand");
        }
    });

    $('.brand-next').off('click').on('click', function(){
        if (currentIndex1 < $items.length - 1) {
            currentIndex1++;
            currentBrandId = $items.eq(currentIndex1).data('brand-id');
			currentCategoryId = null;
            updateBrandSlide();
            applyFilter(currentBrandId, currentCategoryId, "brand");
        }
    });

    $items.off('click').on('click', function(){
        currentIndex1 = $(this).index();
        currentBrandId = $(this).data('brand-id');
		currentCategoryId = null;
        updateBrandSlide();
        applyFilter(currentBrandId, currentCategoryId, "brand");
    });

    // 스크롤 이벤트
    $(window).off('scroll.brandSlide').on('scroll.brandSlide', function(){
        let scrollTop = $(window).scrollTop();

        if (scrollTop <= 50) {
            $('.brand-slide-wrapper').removeClass('hidden');
            $('.brand-slide-space').css('height', '80px');
        }
        else if (scrollTop > 50 && scrollTop <= 150) {
            $('.brand-slide-wrapper').addClass('hidden');
            $('.brand-slide-space').css('height', '80px');
        }
        else {
            $('.brand-slide-wrapper').addClass('hidden');
            $('.brand-slide-space').css('height', '0px');
        }
    });
	

    updateBrandSlide();
}
function initCategorySlide(categoryId){
    const $items = $('.category-item'); // JSP에서 data-category-id 넣어둠
	
    currentCategoryId = categoryId || null;

    if (categoryId) {
        $items.each(function(index){
            if ($(this).data('category-id') == categoryId) {
                currentIndex2 = index;
                return false; // break
            }
        });
    }

    function updateCategorySlide() {
		$items.removeClass('active');
		
        $items.eq(currentIndex2).addClass('active');

        const slideWidth = 60;
        const gap = 20;
        const containerWidth = 420;

		// 전체 폭
	    const totalWidth = $items.length * (slideWidth + gap);

	    // offset 계산
	    let offset = (containerWidth / 2) - ((currentIndex2 + 0.5) * slideWidth) - (currentIndex2 * gap);

	    // 좌우로 너무 벗어나면 보정
	    const maxOffset = 0; // 왼쪽 끝
	    const minOffset = containerWidth - totalWidth; // 오른쪽 끝
	    if (offset > maxOffset) offset = maxOffset;
	    if (offset < minOffset) offset = minOffset;

	    $('.category-slide').css('transform', `translateX(${offset}px)`);
    }

    $('.category-prev').off('click').on('click', function(){
        if (currentIndex2 > 0) {
            currentIndex2--;
            currentCategoryId = $items.eq(currentIndex2).data('category-id');
			currentBrandId = null;
            updateCategorySlide();
            applyFilter(currentBrandId, currentCategoryId, "category");
        }
    });

    $('.category-next').off('click').on('click', function(){
        if (currentIndex2 < $items.length - 1) {
            currentIndex2++;
            currentCategoryId = $items.eq(currentIndex2).data('category-id');
			currentBrandId = null;
            updateCategorySlide();
	        applyFilter(currentBrandId, currentCategoryId, "category");
        }
    });

    $items.off('click').on('click', function(){
        currentIndex2 = $(this).index();
        currentCategoryId = $(this).data('category-id');
		currentBrandId = null;
        updateCategorySlide();
        applyFilter(currentBrandId, currentCategoryId, "category");
    });
	
    updateCategorySlide();
}



function applyFilter(brandId, categoryId, mode) {
    if (brandId !== undefined && brandId !== null) currentBrandId = brandId;
    if (categoryId !== undefined && categoryId !== null) currentCategoryId = categoryId;

	if (mode === "brand") {
	        $.get("/shop/categoriesByBrandFragment", { brandId: currentBrandId }, function(html) {
	            $(".filter-dynamic").html(html);
	        });
	    } else if (mode === "category") {
	        $.get("/shop/brandsByCategoryFragment", { categoryId: currentCategoryId }, function(html) {
	            $(".filter-dynamic").html(html);
	        });
	    }

    loadProductPage(1);
}

function loadProductPage(page) {
    const minPrice = $("#slider-range").slider("values", 0);
    const maxPrice = $("#slider-range").slider("values", 1);

    const sizeList = [];
    $(".size-check:checked").each(function(){
        sizeList.push($(this).val());
    });
    const colorList = [];
    $(".color-checkbox:checked").each(function(){
        colorList.push($(this).val());
    });
    $.ajax({
        url: '/shop/filterProductList',
        type: 'get',
        traditional: true,
        data: {
            brandId: currentBrandId || "",
			categoryId: currentCategoryId || "",
            minPrice: minPrice,
            maxPrice: maxPrice,
            sizeList: sizeList,
            colorList: colorList,
            page: page
        },
        dataType: 'html',
        success: function(response){
            $('.product-list').html(response);
			
			// 새 DOM에 다시 이벤트 바인딩
			if (mode === "brand") {
		        $('.brand-prev, .brand-next').off();
		        initBrandSlide(currentBrandId);
		    } else if (mode === "category") {
		        $('.brand-prev, .brand-next').off();
		        initCategorySlide(currentCategoryId);
		    }
        },
        error: function(){
            alert('상품 목록 로드 실패');
        }
    });
}

function bindFilterEvents() {
    // 사이즈, 색상 체크박스
    $(document).off('change', '.size-check, .color-checkbox')
               .on('change', '.size-check, .color-checkbox', function(){
        applyFilter(currentBrandId, currentCategoryId);
    });

    // 가격 슬라이더
    $(document).off('slidechange', '#slider-range')
               .on('slidechange', '#slider-range', function(){
        applyFilter(currentBrandId, currentCategoryId);
    });

	// 카테고리 필터 클릭 (토글)
    $(document).off('click', '.category-filter')
               .on('click', '.category-filter', function(e){
        e.preventDefault();
        const clickedId = $(this).data('category-id');

        if (currentCategoryId === clickedId) {
            // ✅ 같은 카테고리 다시 클릭 → 해제
            currentCategoryId = null;
            $(".category-filter").removeClass("active");
        } else {
            currentCategoryId = clickedId;
            $(".category-filter").removeClass("active");
            $(this).addClass("active");
        }

        applyFilter(currentBrandId, currentCategoryId);
    });

    // 브랜드 필터 클릭 (토글)
    $(document).off('click', '.brand-filter')
               .on('click', '.brand-filter', function(e){
        e.preventDefault();
        const clickedId = $(this).data('brand-id');

        if (currentBrandId === clickedId) {
            // ✅ 같은 브랜드 다시 클릭 → 해제
            currentBrandId = null;
            $(".brand-filter").removeClass("active");
        } else {
            currentBrandId = clickedId;
            $(".brand-filter").removeClass("active");
            $(this).addClass("active");
        }

        applyFilter(currentBrandId, currentCategoryId);
    });
}

function toggleWishlist(btn) {
  const $btn = $(btn);
  const productId = $btn.data('product-id');
  const isLiked = $btn.find('i').hasClass('fa-solid');
  const url = isLiked ? '/wishlist/delete' : '/wishlist/insert';
  $.ajax({
    url: url,
    method: 'POST',
    data: { productId: productId },
    success: function(res) {
      // "찜 해제"이고, 현재가 "찜목록" 페이지라면 새로고침
      if (isLiked && isLikePage()) {
        // SPA라면 loadPage 호출, 전체 새로고침하려면 location.reload();
        loadPage('like');
        return;
      }

      // 아니면 UI만 토글
      const $icon = $btn.find('i');
      if (isLiked) {
        $icon.removeClass('fa-solid').addClass('fa-regular');
      } else {
        $icon.removeClass('fa-regular').addClass('fa-solid');
      }
    },
    error: function() {
      alert('오류가 발생했습니다. 다시 시도해주세요.');
    }
  });
}

// 현재 페이지가 "찜목록"인지 판별하는 함수
function isLikePage() {
  // 가장 간단: #content-wrapper 등 id/class로 체크
  // 예시: 찜목록의 메인 wrapper에 id="like-page" 부여
  return document.getElementById('like-page') !== null;
}


function handleDotClick(dot) {
    var reviewId = dot.getAttribute('data-review-id');
    var imgIdx = parseInt(dot.getAttribute('data-img-idx'));
    var mainImg = document.querySelector(`.review-main-img[data-review-id='${reviewId}']`);
    var imgList = mainImg.getAttribute('data-images').split(',');
    mainImg.src = contextPath + "/images/review/" + imgList[imgIdx];
    document.querySelectorAll(`.dot[data-review-id='${reviewId}']`).forEach(function(d, i) {
        d.style.background = (i === imgIdx) ? "#555" : "#bbb";
    });
}

