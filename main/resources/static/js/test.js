/*
     _ _      _       _
 ___| (_) ___| | __  (_)___
/ __| | |/ __| |/ /  | / __|
\__ \ | | (__|   < _ | \__ \
|___/_|_|\___|_|\_(_)/ |___/
                   |__/

 Version: 1.8.0
  Author: Ken Wheeler
 Website: http://kenwheeler.github.io
    Docs: http://kenwheeler.github.io/slick
    Repo: http://github.com/kenwheeler/slick
  Issues: http://github.com/kenwheeler/slick/issues
 */

/* 전역 설정: window, document, define, jQuery, setInterval 등 전역 객체를 사용할 수 있게 함 */
;(function(factory) {
    'use strict';

    // 만약 AMD(모듈 방식)를 쓰면 define으로 등록
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);

    // Node.js 방식이라면 module.exports로 등록
    } else if (typeof exports !== 'undefined') {
        module.exports = factory(require('jquery'));

    // 아무 것도 아니라면, jQuery를 직접 사용
    } else {
        factory(jQuery);
    }

}(function($) {
    'use strict';

    // Slick이라는 이름으로 기능을 만들기 시작
    var Slick = window.Slick || {};

    // Slick 이라는 함수를 새로 정의함
    Slick = (function() {

        var instanceUid = 0; // 고유 번호 (여러 개 슬라이더가 있을 때 구분용)

        function Slick(element, settings) {
            // 이 함수는 슬라이더 하나를 만들 때 실행됨

            var _ = this; // this를 _로 줄여서 사용

            _.defaults = {
                // 슬릭 슬라이더의 기본 설정값들을 정의 (설명은 아래 주석 참고)
                accessibility: true, // 키보드로 조작 가능하게
                adaptiveHeight: false, // 슬라이드마다 높이 자동조절
                appendArrows: $(element), // 이전/다음 버튼이 추가될 곳
                appendDots: $(element), // 점(페이지네이션)이 추가될 곳
                arrows: true, // 이전/다음 버튼을 보일지
                asNavFor: null, // 다른 슬릭과 연결해서 같이 움직일지
                prevArrow: '<button class="slick-prev" aria-label="Previous" type="button">Previous</button>',
                nextArrow: '<button class="slick-next" aria-label="Next" type="button">Next</button>',
                autoplay: false, // 자동으로 넘어갈지
                autoplaySpeed: 3000, // 자동으로 넘어가는 시간 간격 (3초)
                centerMode: false, // 가운데 정렬 모드
                centerPadding: '50px', // 가운데 정렬일 때 좌우 여백
                cssEase: 'ease', // CSS로 부드럽게 움직이게 설정
                customPaging: function(slider, i) {
                    // 점(페이지네이션)을 어떻게 표시할지
                    return $('<button type="button" />').text(i + 1); // 숫자 1부터 시작
                },
                dots: false, // 하단 점(페이지네이션) 표시 여부
                dotsClass: 'slick-dots', // 점에 적용할 클래스명
                draggable: true, // 마우스로 드래그해서 넘길 수 있게
                easing: 'linear', // 움직이는 속도 스타일
                edgeFriction: 0.35, // 맨 끝에서 넘길 때 튕기는 정도
                fade: false, // 페이드 효과를 줄지
                focusOnSelect: false, // 클릭하면 해당 슬라이드로 이동할지
                infinite: true, // 끝까지 가면 다시 처음으로 돌아가게
                initialSlide: 0, // 시작할 슬라이드 번호
                lazyLoad: 'ondemand', // 필요한 이미지만 불러오기
                mobileFirst: false, // 반응형 디자인 기준을 모바일 우선으로 할지
                pauseOnHover: true, // 마우스를 올리면 자동 넘김 멈춤
                pauseOnFocus: true, // 포커스를 가지면 자동 넘김 멈춤
                respondTo: 'window', // 반응형 기준 (창 크기 기준)
                rows: 1, // 한 슬라이드에 들어갈 줄 수 (기본 1줄)
                rtl: false, // 오른쪽에서 왼쪽으로 보여줄지
                slide: '', // 슬라이드로 인식할 요소
                slidesPerRow: 1, // 한 줄에 들어갈 슬라이드 개수
                slidesToShow: 1, // 한 화면에 보여줄 슬라이드 개수
                slidesToScroll: 1, // 넘길 때 몇 개씩 넘길지
                speed: 500, // 넘기는 속도 (0.5초)
                swipe: true, // 손가락으로 넘기기 허용
                touchMove: true, // 터치로 넘기기 허용
                touchThreshold: 5, // 얼마나 움직이면 넘기는지 기준
                useCSS: true, // CSS로 움직임 적용
                useTransform: true, // 트랜스폼 사용 여부
                variableWidth: false, // 슬라이드 너비가 각각 다를 경우
                vertical: false, // 세로 슬라이더일지
                verticalSwiping: false, // 세로로 손가락 넘기기 허용
                waitForAnimate: true, // 애니메이션이 끝날 때까지 기다릴지
                zIndex: 1000 // z-index 기본값
            };

            // 이 아래부터는 슬라이더 내부에서 사용할 초기 상태값들임
            _.initials = {
                animating: false, // 애니메이션 중인지
                dragging: false, // 드래그 중인지
                autoPlayTimer: null, // 자동넘김 타이머 저장용
                currentSlide: 0, // 현재 슬라이드 번호
                $dots: null, // 점 목록
                $nextArrow: null, // 다음 버튼
                $prevArrow: null, // 이전 버튼
                slideCount: null, // 슬라이드 총 개수
                $list: null, // 슬라이드 리스트 영역
                touchObject: {}, // 터치 정보 저장용
                transformsEnabled: false, // 변형 사용 여부
                unslicked: false // 슬라이더 해제 여부
            };

            // 초기 상태와 기본 설정값을 복사해서 이 슬라이더에 적용
            $.extend(_, _.initials);