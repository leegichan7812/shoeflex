(function ($) {
    "use strict";

    // 전역 콜백 선언 (URL 기록용)
    window.loadPageCallback = null;

    $(document).ready(function () {
        // 초기 페이지 로드 시 주소 확인 → 새로고침 대응
        const currentPath = window.location.pathname + window.location.search;

        if (currentPath !== '/' && currentPath !== '/main') {
            if (typeof loadPageCallback === 'function') {
                loadPageCallback(currentPath);
            }
            loadPage(currentPath, false);
        }

        // 장바구니
        const cartIcon = document.querySelector('.js-show-cart');
        const cartWrapper = document.querySelector('.wrap-header-cart');
        let cartLockedOpen = false;

        if (cartIcon && cartWrapper) {
            cartIcon.addEventListener('mouseenter', () => {
                cartWrapper.classList.add('show-header-cart');
            });

            cartIcon.addEventListener('mouseleave', () => {
                setTimeout(() => {
                    if (!cartLockedOpen) {
                        cartWrapper.classList.remove('show-header-cart');
                    }
                }, 500);
            });

            cartIcon.addEventListener('click', (e) => {
                e.preventDefault();

                if (cartLockedOpen) {
                    loadPage('cart');
                } else {
                    cartLockedOpen = true;
                    cartWrapper.classList.add('show-header-cart');
                }
            });

            cartWrapper.addEventListener('mouseleave', () => {
                cartLockedOpen = false;
                cartWrapper.classList.remove('show-header-cart');
            });
        }

        // 검색창
        const $searchInput = $('#search-input');
        const $searchSwitch = $('.search-switch');

        $searchSwitch.on('click', function (e) {
            e.preventDefault();

            if ($searchInput.hasClass('active')) {
                $searchInput.removeClass('active').val('');
            } else {
                $searchInput.addClass('active').focus();
            }
        });

        $(document).on('click', function (e) {
            if (
                !$searchInput.is(e.target) &&
                !$searchSwitch.is(e.target) &&
                $searchSwitch.has(e.target).length === 0
            ) {
                $searchInput.removeClass('active').val('');
            }
        });

        // 로그아웃
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
    });

    // 뒤로가기 / 앞으로가기
    window.addEventListener('popstate', function(event) {
        if (event.state && event.state.url) {
            loadPage(event.state.url, false);
        }
    });

})(jQuery);

// ----------- loadPage 외부 콜백 방식 (기존 loadPage 유지) -----------

function loadPage(url, addHistory = true) {
    $.ajax({
        url: url,
        type: "get",
        dataType: "html",
        success: function(response) {
            $("main").html(response);

            // 외부 콜백 실행
            if (typeof loadPageCallback === 'function') {
                loadPageCallback(url);
            }

            // 원래 로직 유지
            if (url === 'insertJoin' || url === 'joinUpdate') {
                initJoinFormValidation();
            }
            if (url === 'main') {
                initSlick1();
            }
            if (url === 'login' || url === 'glogin') {
                initLoginFormHandler();
            }
            if (url.startsWith("shop/brand") || url.startsWith("shop/category")) {
                const params = new URLSearchParams(url.split('?')[1]);
                const brandId = params.get('brandId');
                initFilterUI();
                initBrandSlide(brandId);
                bindFilterEvents();
            }
        },
        error: function(xhr, status, error) {
            console.error("로딩 실패:", error);
        }
    });
}

// ----------- 콜백 등록 -----------

window.loadPageCallback = function(url) {
    if (url !== 'main' && url !== '/main') {
        history.pushState({url: url}, '', url);
    }
}

// ----------- 기존 함수들 유지 -----------

let currentBrandId = null;
let currentIndex = 0;

function initSlick1() {
    $('.wrap-slick1').each(function () {
        const wrapSlick1 = $(this);
        const slick1 = wrapSlick1.find('.slick1');
        const itemSlick1 = slick1.find('.item-slick1');
        const layerSlick1 = slick1.find('.layer-slick1');
        let actionSlick1 = [];

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
            prevArrow: '<button class="arrow-slick1 prev-slick1"><i class="zmdi zmdi-caret-left"></i></button>',
            nextArrow: '<button class="arrow-slick1 next-slick1"><i class="zmdi zmdi-caret-right"></i></button>',
            dots: wrapSlick1.find('.wrap-slick1-dots').length > 0,
            appendDots: wrapSlick1.find('.wrap-slick1-dots'),
            dotsClass: 'slick1-dots',
            customPaging: function (slick, index) {
                var linkThumb = $(slick.$slides[index]).data('thumb');
                var caption = $(slick.$slides[index]).data('caption');
                return '<img src="' + linkThumb + '">' +
                       '<span class="caption-dots-slick1">' + caption + '</span>';
            }
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

function initJoinFormValidation() {
    $('#email').on('input', function () {
        const email = $(this).val().trim();
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,}$/;
        const msg = $('#email-check-msg');

        if (!emailRegex.test(email)) {
            msg.text("❌ 유효한 이메일 형식이 아닙니다.").css("color", "red");
            $(this).removeClass('valid-input').addClass('invalid-input');
            return;
        }

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
                console.error("이메일 중복 체크 에러:", error);
            });
    });

    $('#password').on('input', function () {
        const password = $(this).val();
        const pwdRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+]).{10,20}$/;

        if (!pwdRegex.test(password)) {
            $('#pwdValid').text('❌ 영문, 숫자, 특수문자 포함 10~20자리로 입력하세요.').removeClass('valid-msg').addClass('error-msg');
            $(this).removeClass('valid-input').addClass('invalid-input');
        } else {
            $('#pwdValid').text('✅ 사용 가능한 비밀번호입니다.').removeClass('error-msg').addClass('valid-msg');
            $(this).removeClass('invalid-input').addClass('valid-input');
        }
    });

    $('#confirmPassword').on('input', function () {
        const pwd = $('#password').val();
        const confirm = $(this).val();

        if (pwd === confirm && pwd !== "") {
            $('#pwdMatch').text('✅ 비밀번호가 일치합니다.').removeClass('error-msg').addClass('valid-msg');
            $(this).removeClass('invalid-input').addClass('valid-input');
        } else {
            $('#pwdMatch').text('❌ 비밀번호가 일치하지 않습니다.').removeClass('valid-msg').addClass('error-msg');
            $(this).removeClass('valid-input').addClass('invalid-input');
        }
    });
}

function initLoginFormHandler() {
    $(document).off('submit', '#loginForm');
    $(document).on('submit', '#loginForm', function(e) {
        e.preventDefault();
        const formData = $(this).serialize();

        $.post("login", formData, function(response) {
            if (response.redirect) {
                location.reload();
            } else if (response.error) {
                $('#login-error-msg').html(`<p class="error-msg">${response.error}</p>`);
            }
        });
    });
}

function initFilterUI() {
    initPriceSlider();
}

function initPriceSlider() {
    $("#slider-range").slider({
        range: true,
        min: 0,
        max: 500000,
        step: 10000,
        values: [30000, 400000],
        slide: function(event, ui) {
            $("#amount").val(ui.values[0] + "원  ~  " + ui.values[1] + "원");
        }
    });

    $("#amount").val($("#slider-range").slider("values", 0) + "원 - " + $("#slider-range").slider("values", 1) + "원");
}

function initBrandSlide(brandId){
    const $items = $('.brand-item');
    currentBrandId = brandId || null;

    if (brandId) {
        $items.each(function(index){
            if ($(this).data('brand-id') == brandId) {
                currentIndex = index;
                return false;
            }
        });
    }

    function updateSlide() {
        $items.removeClass('active');
        $items.eq(currentIndex).addClass('active');

        const slideWidth = 60;
        const gap = 20;
        const containerWidth = 540;

        const offset = (containerWidth / 2) - ((currentIndex + 0.5) * slideWidth) - (currentIndex * gap);
        $('.brand-slide').css('transform', `translateX(${offset}px)`);
    }

    $('.brand-prev').off('click').on('click', function(){
        if (currentIndex > 0) {
            currentIndex--;
            currentBrandId = $items.eq(currentIndex).data('brand-id');
            updateSlide();
            applyFilter(currentBrandId);
        }
    });

    $('.brand-next').off('click').on('click', function(){
        if (currentIndex < $items.length - 1) {
            currentIndex++;
            currentBrandId = $items.eq(currentIndex).data('brand-id');
            updateSlide();
            applyFilter(currentBrandId);
        }
    });

    $items.off('click').on('click', function(){
        currentIndex = $(this).index();
        currentBrandId = $(this).data('brand-id');
        updateSlide();
        applyFilter(currentBrandId);
    });

    updateSlide();
}

function applyFilter(brandId) {
    currentBrandId = brandId;
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
            brandId: currentBrandId,
            minPrice: minPrice,
            maxPrice: maxPrice,
            sizeList: sizeList,
            colorList: colorList,
            page: page
        },
        dataType: 'html',
        success: function(response){
            $('.product-list').html(response);
        },
        error: function(){
            alert('상품 목록 로드 실패');
        }
    });
}

function bindFilterEvents() {
    $(".size-check, .color-checkbox").on('change', function(){
        applyFilter(currentBrandId);
    });

    $("#slider-range").on('slidechange', function(){
        applyFilter(currentBrandId);
    });
}