(function ($) {
    // 엄격한 문법으로 JavaScript 실행 (실수 방지용)
    "use strict";

    /*==================================================================
    [ 슬라이더 1번 설정 - slick1 ]
    ==================================================================*/
    $('.wrap-slick1').each(function(){
        var wrapSlick1 = $(this);             // 슬릭1 전체 감싸는 요소
        var slick1 = $(this).find('.slick1'); // 실제 슬라이더 요소

        var itemSlick1 = $(slick1).find('.item-slick1');     // 각각의 슬라이드 항목
        var layerSlick1 = $(slick1).find('.layer-slick1');   // 애니메이션용 레이어
        var actionSlick1 = []; // 애니메이션 효과를 위한 타이머들 저장 배열

        // 슬릭 초기화되었을 때 실행되는 함수
        $(slick1).on('init', function(){
            var layerCurrentItem = $(itemSlick1[0]).find('.layer-slick1'); // 첫 번째 슬라이드의 레이어

            // 이전에 실행 중이던 애니메이션 타이머 정지
            for(var i=0; i<actionSlick1.length; i++) {
                clearTimeout(actionSlick1[i]);
            }

            // 모든 레이어에서 클래스 제거 (초기화)
            $(layerSlick1).each(function(){
                $(this).removeClass($(this).data('appear') + ' visible-true');
            });

            // 현재 보여지는 슬라이드에 애니메이션 클래스 추가 (delay 적용)
            for(var i=0; i<layerCurrentItem.length; i++) {
                actionSlick1[i] = setTimeout(function(index) {
                    $(layerCurrentItem[index]).addClass($(layerCurrentItem[index]).data('appear') + ' visible-true');
                }, $(layerCurrentItem[i]).data('delay'), i); 
            }        
        });

        // 도트(점 모양 내비게이션) 표시 여부 결정
        var showDot = false;
        if($(wrapSlick1).find('.wrap-slick1-dots').length > 0) {
            showDot = true;
        }

        // 슬릭1 슬라이더 실행
        $(slick1).slick({
            pauseOnFocus: false, // 클릭해도 멈추지 않음
            pauseOnHover: false, // 마우스 올려도 멈추지 않음
            slidesToShow: 1,     // 한 번에 보여줄 슬라이드 수 (3개)
            slidesToScroll: 1,   // 한 번에 넘어갈 슬라이드 수 (3개)
            fade: false,          // 페이드 효과 사용 (서서히 바뀜)
            speed: 1000,         // 전환 속도 (1초)
            infinite: true,      // 무한 반복
            autoplay: true,      // 자동 재생
            autoplaySpeed: 6000, // 자동 전환 시간 (6초)
            arrows: true,        // 화살표 보이기
            appendArrows: $(wrapSlick1), // 화살표 붙일 위치
            prevArrow:'<button class="arrow-slick1 prev-slick1"><i class="zmdi zmdi-caret-left"></i></button>',
            nextArrow:'<button class="arrow-slick1 next-slick1"><i class="zmdi zmdi-caret-right"></i></button>',
            dots: showDot, // 도트 보일지 여부
            appendDots: $(wrapSlick1).find('.wrap-slick1-dots'), // 도트 위치
            dotsClass:'slick1-dots', // 도트 스타일
            customPaging: function(slick, index) {
                // 도트에 이미지와 설명 넣기
                var linkThumb = $(slick.$slides[index]).data('thumb'); // 썸네일 이미지 주소
                var caption = $(slick.$slides[index]).data('caption'); // 캡션(설명)
                return  '<img src="' + linkThumb + '">' +
                        '<span class="caption-dots-slick1">' + caption + '</span>';
            },
        });

        // 슬라이드 바뀐 후 실행
        $(slick1).on('afterChange', function(event, slick, currentSlide){ 
            var layerCurrentItem = $(itemSlick1[currentSlide]).find('.layer-slick1'); // 현재 슬라이드의 레이어

            // 이전 애니메이션 정지
            for(var i=0; i<actionSlick1.length; i++) {
                clearTimeout(actionSlick1[i]);
            }

            // 모든 레이어 초기화
            $(layerSlick1).each(function(){
                $(this).removeClass($(this).data('appear') + ' visible-true');
            });

            // 현재 슬라이드에 애니메이션 효과 주기 (딜레이 포함)
            for(var i=0; i<layerCurrentItem.length; i++) {
                actionSlick1[i] = setTimeout(function(index) {
                    $(layerCurrentItem[index]).addClass($(layerCurrentItem[index]).data('appear') + ' visible-true');
                }, $(layerCurrentItem[i]).data('delay'), i); 
            }       
        });
    });

    /*==================================================================
    [ 슬라이더 2번 설정 - slick2 ]
    ==================================================================*/
    $('.wrap-slick2').each(function(){
        $(this).find('.slick2').slick({
            slidesToShow: 3,   // 기본 4개 보여줌
            slidesToScroll: 1, // 4개씩 넘김
            infinite: false,   // 끝에서 멈춤 (무한 반복 X)
            autoplay: false,
            autoplaySpeed: 6000,
            arrows: true,
            appendArrows: $(this),
            prevArrow:'<button class="arrow-slick2 prev-slick2"><i class="fa fa-angle-left" aria-hidden="true"></i></button>',
            nextArrow:'<button class="arrow-slick2 next-slick2"><i class="fa fa-angle-right" aria-hidden="true"></i></button>',  

            // 화면 크기 작아지면 반응형 설정
            responsive: [
                {
                    breakpoint: 1200, // 화면 1200px 이하
                    settings: {
                        slidesToShow: 4,
                        slidesToScroll: 4
                    }
                },
                {
                    breakpoint: 992,
                    settings: {
                        slidesToShow: 3,
                        slidesToScroll: 3
                    }
                },
                {
                    breakpoint: 768,
                    settings: {
                        slidesToShow: 2,
                        slidesToScroll: 2
                    }
                },
                {
                    breakpoint: 576,
                    settings: {
                        slidesToShow: 1,
                        slidesToScroll: 1
                    }
                }
            ]    
        });
    });

    // 탭 메뉴 클릭 시 슬릭2 다시 계산 (화면에 안 보이다 보이면 슬라이더 깨짐 방지)
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        var nameTab = $(e.target).attr('href'); 
        $(nameTab).find('.slick2').slick('reinit');          
    });      

    /*==================================================================
    [ 슬라이더 3번 설정 - slick3 ]
    ==================================================================*/
    $('.wrap-slick3').each(function(){
        $(this).find('.slick3').slick({
            slidesToShow: 1,
            slidesToScroll: 1,
            fade: true,     // 페이드 효과 사용
            infinite: true, // 무한 반복
            autoplay: false,
            autoplaySpeed: 6000,

            arrows: true,
            appendArrows: $(this).find('.wrap-slick3-arrows'),
            prevArrow:'<button class="arrow-slick3 prev-slick3"><i class="fa fa-angle-left" aria-hidden="true"></i></button>',
            nextArrow:'<button class="arrow-slick3 next-slick3"><i class="fa fa-angle-right" aria-hidden="true"></i></button>',

            dots: true, // 도트 내비게이션 표시
            appendDots: $(this).find('.wrap-slick3-dots'),
            dotsClass:'slick3-dots',
            customPaging: function(slick, index) {
                // 도트에 이미지 넣기
                var portrait = $(slick.$slides[index]).data('thumb');
                return '<img src=" ' + portrait + ' "/><div class="slick3-dot-overlay"></div>';
            },  
        });
    });

})(jQuery);