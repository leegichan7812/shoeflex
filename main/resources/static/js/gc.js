(function ($) {
    "use strict";
	// 비밀번호 검증 ajax 추가
	$(document).on("click", "#pwdCheckBtn", function(){
	    const inputPwd = $('#checkPwd').val().trim();
	    if(inputPwd === ""){
	        alert("비밀번호를 입력하세요.");
	        return;
	    }

	    $.ajax({
	        url: "checkPwd",
	        type: "POST",
	        data: { password: inputPwd },
	        success: function(res){
	            if(res === "ok"){
	                // 비밀번호 맞으면 비밀번호 확인 영역 숨기기
	                $("#pwdCheckSection").hide();

	                // 수정폼 보이기
	                $("#userUpdateSection").show();
	            }else{
	                alert("비밀번호가 틀렸습니다.");
	            }
	        },
	        error: function(){
	            alert("서버 오류 발생");
	        }
	    });
	});
	
	// 회원가입 폼 Ajax 전송 (검증 포함 — 단일 출처)
	$(document).off('submit.join', '#joinForm').on('submit.join', '#joinForm', function(e){
	  e.preventDefault();

	  const email = $('#email').val().trim();
	  const pw = $('#password').val();
	  const cf = $('#confirmPassword').val();

	  if (!isValidEmail(email)) { alert('유효한 이메일 형식이 아닙니다.'); $('#email').focus(); return; }
	  if (!isValidPassword(pw)) { alert('비밀번호는 영문, 숫자, 특수문자를 포함한 10~20자리여야 합니다.'); $('#password').focus(); return; }
	  if (pw !== cf) { alert('비밀번호가 일치하지 않습니다.'); $('#confirmPassword').focus(); return; }

	  $.ajax({
	    url: (window.path||'') + 'insertJoin',
	    type: 'POST',
	    dataType: 'json',
	    data: $(this).serialize()
	  }).done(function(res){
	    if(res.success){
	      alert('회원가입이 완료되었습니다!');
	      if (typeof loadPage === 'function') loadPage('glogin');
	      else location.href = (window.path||'') + 'glogin';
	    } else {
	      alert(res.msg || '회원가입 실패');
	    }
	  }).fail(function(){
	    alert('서버 오류가 발생했습니다.');
	  });
	});
	
	// 회원정보 수정
	$(document).on('submit', '#joinUpt', function (e) {
	  e.preventDefault();

	  const password = $('#password').val().trim();
	  const confirm  = $('#confirmPassword').val().trim();

	  const payload = {
	    name: $("input[name='name']").val(),
	    phone: $("input[name='phone']").val(),
	    address: $("input[name='address']").val()
	  };

	  if (password || confirm) {
	    // 비번을 바꾸려는 경우만 검증 + 포함
	    const pwdRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+]).{10,20}$/;
	    if (!pwdRegex.test(password)) { alert("비밀번호는 영문, 숫자, 특수문자 포함 10~20자리."); return; }
	    if (password !== confirm) { alert("비밀번호가 일치하지 않습니다."); return; }
	    payload.password = password;
	  }

	  $.ajax({
	    url: "ajaxJoinUpdate",
	    type: "PUT",
	    contentType: "application/json",
	    data: JSON.stringify(payload),
	    success: function(res) {
	      if(res === "수정성공") {
	        alert("회원정보가 수정되었습니다!");
	        loadPage('index');
	      } else {
	        alert("수정 실패: " + res);
	      }
	    },
	    error: function(xhr, status, err){
	      alert("에러 발생: " + err);
	    }
	  });
	});
	// 수정 버튼 클릭시 수정
	$(document).on("click", "#joinUptBtn", function(){
		$('#joinUpt').submit();
	});
	

	// 탈퇴 버튼 클릭 -> 모달 띄우기 + 이유 로딩
	$(document).on("click", "#joinDelBtn", function(){
	    $.ajax({
	        url: "getWithdrawalReasons", // reason 리스트 가져오는 API
	        type: "GET",
	        success: function(list){
	            let html = "";
	            list.forEach(reason => {
	                html += `<option value="${reason.reasonId}">${reason.reasonText}</option>`;
	            });
	            $("#withdrawReason").html(html);
	            $("#withdrawModal").modal("show");
	        }
	    });
	});
	// 기타 선택 시만 textarea 보이기
	$(document).on("change", "#withdrawReason", function(){
	    const selectedText = $("#withdrawReason option:selected").text();
	    if (selectedText === "기타") {
	        $("#etcReason").show();
	    } else {
	        $("#etcReason").hide();
	        $("#etcReason").val(""); // 기타 내용 초기화
	    }
	});

	// 탈퇴하기 버튼 클릭
	$(document).on("click", "#withdrawSubmit", function(){
		const $btn = $(this);
	  	if ($btn.data("busy")) return;        // ✅ 중복 클릭 차단
	  	$btn.data("busy", true).prop("disabled", true);
		  
		const data = {
	        reasonId: $("#withdrawReason").val(),
	        etcReason: $("#etcReason").val()
	    };

		$.ajax({
		    url: "ajaxWithdrawal",
		    type: "PUT",
		    contentType: "application/json",
		    data: JSON.stringify(data)
	  	})
	  	.done(function(res){
		    alert("탈퇴가 완료되었습니다.");
		    location.href = "index";
	  	})
	  	.fail(function(err){
	    	// 서버에서 이미 무효화된 경우라도 사용자에겐 동일 메시지로 처리
	    	alert("에러 발생: " + (err.responseText || "잠시 후 다시 시도해주세요."));
	  	})
	  	.always(function(){
	    	$btn.data("busy", false).prop("disabled", false);
	  	});
	});
	
	// 아이디 및 비밀번호 찾기
	// 하이픈 제거 유틸
	function normalizePhone(p){ return (p||'').replace(/[^0-9]/g,''); }

	// 아이디 찾기
	$(document).on('click', '#btnFindId', function(){
	  const $form = $('#findIdForm');
	  const name  = $form.find('[name=name]').val().trim();
	  const phone = ($form.find('[name=phone]').val() || '').replace(/[^0-9]/g,'');
	  if(!name || !phone){ alert('이름과 휴대폰 번호를 입력해주세요.'); return; }

	  $('#findIdResult').removeClass('text-danger text-success').text('조회 중…');

	  $.ajax({
	    url: (window.contextPath || '') + '/findId',
	    method: 'POST',
	    dataType: 'json',
	    data: { name, phone }
	  }).done(function(res){
	    if(res.success){
	      $('#findIdResult').addClass('text-success').text('아이디: ' + res.email);
	    }else{
	      $('#findIdResult').addClass('text-danger').text(res.msg || '일치하는 정보가 없습니다.');
	    }
	  }).fail(function(xhr){
	    console.error('findId fail', xhr.status, xhr.responseText);
	    $('#findIdResult').addClass('text-danger').text('요청 실패');
	  });
	});

	// 비밀번호 찾기(임시 비번 메일 발송)
	$(document).on('click', '#btnResetPw', function(){
	  const $form = $('#resetPwForm');
	  const email = $form.find('[name=email]').val().trim();
	  const name  = $form.find('[name=name]').val().trim();
	  const phone = ($form.find('[name=phone]').val() || '').replace(/[^0-9]/g,'');
	  if(!email || !name || !phone){ alert('아이디(이메일), 이름, 휴대폰 번호를 입력해주세요.'); return; }

	  $('#resetPwResult').removeClass('text-danger text-success').text('처리 중…');

	  $.ajax({
	    url: (window.contextPath || '') + '/resetPassword',
	    method: 'POST',
	    dataType: 'json',
	    data: { email, name, phone }
	  }).done(function(res){
	    if(res.success){
	      $('#resetPwResult').addClass('text-success').text('임시 비밀번호를 메일로 발송했습니다.');
	    }else{
	      $('#resetPwResult').addClass('text-danger').text(res.msg || '일치하는 정보가 없습니다.');
	    }
	  }).fail(function(xhr){
	    console.error('resetPassword fail', xhr.status, xhr.responseText);
	    $('#resetPwResult').addClass('text-danger').text('요청 실패');
	  });
	});
	
	// 카트 버튼 클릭
	// + 버튼
	$(document).on('click', '.btn-num-product-up', function(){
		const input = $(this).siblings('input.num-product');
		if(input.prop("disabled")) {
			alert("품절된 상품입니다.");
			return;
		}
		const maxStock = parseInt(input.attr('max'));
		let quantity = parseInt(input.val()) + 1;

		if (quantity > maxStock) {
		    alert("재고를 초과할 수 없습니다.");
		    return;
		}

		input.val(quantity);
		updateQuantity(input);
	});

	// - 버튼
	$(document).on('click', '.btn-num-product-down', function(){
		const input = $(this).siblings('input.num-product');
		let quantity = parseInt(input.val());
		if(quantity > 1){
			quantity--;
			input.val(quantity);
			updateQuantity(input);
		}
	});
	// 장바구니에서 상품 삭제
	$(document).on('click', '.btn-delete-cart', function(){
		if(!confirm("이 상품을 장바구니에서 삭제하시겠습니까?")) return;

		const cartId = $(this).data('cart-id');
		const tr = $(this).closest('tr');

		$.ajax({
			url: "/deleteCartItem",
			type: "POST",
			data: { cartId: cartId },
			success: function(res){
				tr.remove();
				checkEmptyCart(); // ✅ 공통 사용
					if(res.totalCount !== 0){
						$("#totalCount").text(res.totalCount + " 개");
						$("#totalPrice").text(res.totalPrice.toLocaleString() + "원");
					}
			},
			error: function(){
				alert("삭제 실패");
			}
		});
	});
	// 장바구니 총 갯수. 총 금액 변경
	$(document).on('change', '.cart-select', function () {
		updateCartSummary();
	});
	// 전체 선택/해제 기능
	$(document).on("change", "#selectAllCart", function () {
	    const isChecked = $(this).is(":checked");
	    $(".cart-select").each(function () {
	        if (!$(this).is(":disabled")) {
	            $(this).prop("checked", isChecked);
	        }
	    });
	    updateCartSummary(); // 총합 업데이트
	});

	// 개별 선택 변경 시 전체 선택 체크박스 상태 조정
	$(document).on("change", ".cart-select", function () {
	    const allCheckboxes = $(".cart-select").not(":disabled");
	    const checkedCheckboxes = $(".cart-select:checked").not(":disabled");

	    // 모두 선택되어 있으면 전체 선택 체크
	    $("#selectAllCart").prop("checked", allCheckboxes.length === checkedCheckboxes.length);

	    updateCartSummary(); // 총합 업데이트
	});
	
	// 선택된 cartId만 orderPage로 넘기기
	$(document).on('click', '#goOrderBtn', function () {
	    const selected = [];
	    $('.cart-select:checked').each(function () {
	        selected.push($(this).val());
	    });

	    if (selected.length === 0) {
	        alert("결제할 상품을 선택하세요.");
	        return;
	    }

	    // URL로 cartIds를 JSON 문자열로 인코딩하여 넘기기 (세션이나 쿠키도 가능)
	    const encoded = encodeURIComponent(JSON.stringify(selected));
	    loadPage('orderPage?cartIds=' + encoded);
	});
	// 상품상세페이지 색상선택 시 사이즈 가져오기 + 선택한 색상명 표시
	$(document).on('click', '.color-option', function () {
	    // 전체 색상 border 초기화
	    $('.color-option').css('border', '2px solid #ccc');
	    // 현재 선택된 색상 border 강조
	    $(this).css('border', '2px solid black');

	    const colorId = $(this).data('color-id');
	    const colorName = $(this).data('color-name'); // ✅ 색상 이름 가져오기
	    const productId = $('#colorGroup').data('product-id');

	    $('#colorSelect').val(colorId); // 숨겨진 input에 저장

	    // ✅ 선택된 색상 이름을 화면에 표시
	    $('#selectedColorName').text(colorName);
		$('#colorNameBox').css('display', 'flex');
		

	    // 기존 Ajax 호출
	    $.ajax({
	        url: 'getSizesByColor',
	        method: 'GET',
	        data: { productId, colorId },
	        success: function (sizeList) {
	            const $sizeSelect = $('#sizeSelect');
	            $sizeSelect.empty();
	            $sizeSelect.append('<option selected disabled>사이즈를 선택하세요</option>');

	            sizeList.forEach(function (size) {
	                $sizeSelect.append(`
	                    <option value="${size.productColorSizeId}" 
							data-size-id="${size.sizeId}" 
							data-stock="${size.stock}">
	                        ${size.sizeValue} (재고: ${size.stock})
	                    </option>
	                `);
	            });
	        }
	    });
	});
	// 사이즈 선택 시 재고 수를 input의 max에 설정하기
	$(document).on('change', '#sizeSelect', function () {
	    const maxStock = $('option:selected', this).data('stock');
	    if (maxStock) {
	        $('#quantity').attr('max', maxStock);
	    }
	});
	
	// 상세 페이지 전용 수량 증가/감소 버튼
	$(document).on('click', '.pd-btn-down', function () {
	    const $qtyInput = $('#quantity');
		const current = parseInt($qtyInput.val());
	    if (current > 1) {
	        $qtyInput.val(current - 1);
			updateTotalPrice(); // ✅ 총 금액 갱신
	    }
	});

	$(document).on('click', '.pd-btn-up', function () {
	    const $qtyInput = $('#quantity');
		const current = parseInt($qtyInput.val());
	    const max = parseInt($qtyInput.attr('max')) || 99;

	    if (current < max) {
	        $qtyInput.val(current + 1);
			updateTotalPrice(); // ✅ 총 금액 갱신
	    } else {
	        alert("재고를 초과할 수 없습니다.");
	    }
	});
	// 수량 직접 입력시도에도 재고 초과 막기
	$(document).on('blur', '#quantity', function () {
	    const max = parseInt($(this).attr('max')) || 99;
	    let val = parseInt($(this).val());

	    if (isNaN(val) || val < 1) val = 1;
	    if (val > max) val = max;

	    $(this).val(val);
	});
	// 수량을 직접 입력했을 때도 총 금액 갱신
	$(document).on('input', '#quantity', updateTotalPrice);
	
	// 상품 상세에서 장바구니에 추가
	$(document).on('click', '#addToCartBtn', function () {
	    const userId = $('#loginUserId').val(); // 로그인 유저 ID (세션에서 JSP로 전달)
	    const productColorSizeId = $('#sizeSelect').val();
	    const quantity = $('#quantity').val();

	    if (!userId) {
	        alert("로그인이 필요합니다.");
	        return;
	    }
	    if (!productColorSizeId) {
	        alert("색상과 사이즈를 선택하세요.");
	        return;
	    }

	    $.ajax({
	        url: "addToCart",
	        type: "POST",
	        data: {
	            userId: userId,
	            productColorSizeId: productColorSizeId,
	            quantity: quantity
	        },
	        success: function (res) {
	            if (res === "success") {
	                alert("장바구니에 추가되었습니다!");
					location.reload();
	            } else {
	                alert("이미 장바구니에 존재하는 상품입니다.");
	            }
	        },
	        error: function () {
	            alert("서버 오류가 발생했습니다.");
	        }
	    });
	});
	
	// 바로 구매 버튼
	$(document).on('click', '#buyNowBtn', function () {
		const userId = $("#loginUserId").val(); // 로그인 확인
		    if (!userId) {
		        alert("로그인이 필요합니다.");
		        return;
		    }
	    const productId = $("#colorGroup").data("product-id");
	    const colorId = $("#colorSelect").val();
	    const sizeId = $('#sizeSelect option:selected').data('size-id');
	    const quantity = $("#quantity").val();

	    if (!colorId || !sizeId) {
	        alert("색상과 사이즈를 모두 선택해주세요.");
	        return;
	    }

	    $.ajax({
	        url: "getProductColorSizeId",
	        type: "GET",
	        data: { productId, colorId, sizeId },
	        success: function (pcsId) {
	            if (!pcsId || pcsId === 0) {
	                alert("상품 정보를 불러올 수 없습니다.");
	                return;
	            }
	            loadPage(`orderPage?productColorSizeId=${pcsId}&quantity=${quantity}`);
	        },
	        error: function () {
	            alert("옵션 정보를 불러오는 중 오류가 발생했습니다.");
	        }
	    });
	});
	// 배송지 목록 불러 오기
	$(document).on('show.bs.modal', '#addressModal', function () {
	    loadShippingAddresses();
	});
	// 주문 창 배송지 추가
	$(document).on("submit", "#addAddressForm", function(e) {
	    e.preventDefault();

	    const formData = $(this).serialize(); // 폼 데이터 직렬화

	    $.ajax({
	        url: "addShippingAddress",
	        method: "POST",
	        data: formData,
	        success: function(response) {
	            if(response === "success") {
	                alert("배송지가 추가되었습니다.");
	                $('#addAddressModal').modal('hide');
	                loadShippingAddresses(); // 추가 후 목록 새로고침 (선택 구현)
					
	            } else {
	                alert("배송지 추가 실패: " + response);
	            }
	        },
	        error: function() {
	            alert("배송지 등록 중 오류가 발생했습니다.");
	        }
	    });
	});

	// 결제 버튼 클릭 시 주문 정보 전송
	$(document).on("click", "#placeOrderBtn", function () {
	  const shippingAddressId = $("#shippingAddressId").val();
	  const deliveryMemo = $("#deliveryMemo").val();
	  const paymentMethodId = $("input[name='paymentMethodId']:checked").val();

	  // 상품 목록 수집
	  const items = [];
	  $("tbody tr").each(function () {
	    const $row = $(this);
	    const productColorSizeId = $row.data("pcs-id"); // 아래에서 data 속성 부여 필요
	    const quantity = parseInt($row.find(".qty").text());
	    const unitPrice = parseInt($row.find(".unit-price").data("price"));
	    const cartId = $row.data("cart-id") || null;

	    if (productColorSizeId) {
	      items.push({
	        productColorSizeId,
	        quantity,
	        unitPrice,
	        cartId
	      });
	    }
	  });

	  const shippingFee = parseInt($("#shippingFee").data("fee")); // span이나 hidden에 따로 data-fee 설정 필요

	  const orderData = {
	    shippingAddressId,
	    shippingFee,
	    paymentMethodId,
	    items
	  };
	  console.log("Shipping Address ID:", shippingAddressId);
	  $.ajax({
	    url: "submitOrder",
	    type: "POST",
	    contentType: "application/json",
	    data: JSON.stringify(orderData),
	    success: function (res) {
	      if (res === "success") {
	        alert("주문이 완료되었습니다!");
	        location.href = "index";
	      } else if (res === "login") {
	        alert("로그인이 필요합니다.");
	        location.href = "index";
	      } else {
	        alert("주문 처리 중 오류가 발생했습니다.");
	      }
	    },
	    error: function () {
	      alert("서버 통신 오류가 발생했습니다.");
	    }
	  });
	});
	
	// 클릭 시 예외 로그 상세 목록 모달창
	$(document).on("click", ".log-row", function () {
	    const logId = $(this).data("log-id");

	    $.ajax({
	        url: "/getLogDetail",  // Controller에서 처리할 경로
	        type: "GET",
	        data: { logId: logId },
	        success: function (log) {
	            $("#logDetailModal .modal-body").html(`
	                <p><strong>발생시각:</strong> ${formatDateTime(log.occurredAt)}</p>
	                <p><strong>클래스명:</strong> ${log.className}</p>
	                <p><strong>메서드명:</strong> ${log.methodName}</p>
	                <p><strong>예외타입:</strong> ${log.exceptionType}</p>
	                <p><strong>에러유형:</strong> ${log.errorType}</p>
	                <p><strong>수행시간:</strong> ${log.executionTimeMs} ms</p>
	                <p><strong>사용자 이메일:</strong> ${log.userEmail}</p>
	                <p><strong>사용자명:</strong> ${log.userName}</p>
	                <p><strong>메시지:</strong><br/> ${log.exceptionMessage}</p>
					<p><strong>담당자:</strong> 신길동, <strong>연락처:</strong> 010-4673-7422</p>
	            `);
	            $("#logDetailModal").modal("show");
	        },
	        error: function () {
	            alert("예외 로그 조회 중 오류가 발생했습니다.");
	        }
	    });
	});
	
})(jQuery);

// 수량 변경 및 총액 업데이트 함수
function updateQuantity(input){
	const cartId = input.data('cart-id');
	const quantity = input.val();
	$.ajax({
		url: "/updateCartQuantity",
		type: "POST",
		data: { cartId: cartId, quantity: quantity },
		success: function(res){
			if(res.success){
				// 정상처리
				// 개별 총액 업데이트
				const price = input.data('price');
				const total = price * quantity;
				input.closest('tr').find('.item-total').text(total.toLocaleString() + "원");
				// 전체 총액, 총 갯수 업데이트
				$('#totalCount').text(res.totalCount + " 개");
				$('#totalPrice').text(res.totalPrice.toLocaleString() + "원");
				
				updateCartSummary();  // ✅ 선택 항목 기준 총계도 갱신
			}else{
				// 재고 초과 등 서버 검증 실패
				alert(res.message);
				// 클라이언트 수량을 서버와 맞추기 위해 리셋 (권장)
				// ex) 장바구니 페이지를 새로고침 하거나, 기존 수량으로 복구
				location.reload();
			}
		},
		error: function(){
			alert("변경 실패 (서버 오류)");
		}
	});
}
function checkEmptyCart() {
	const itemCount = $(".table_row").length;
	console.log("checkEmptyCart ▶ itemCount=", itemCount);  // <— 여기 로그 뜨는지 확인!
	if(itemCount === 0){
		$("#cartTableArea").hide();
		$("#cartTotalArea").hide();
		$("#emptyCartMessage").show();
	} else {
		$("#cartTableArea").show();
		$("#cartTotalArea").show();
		$("#emptyCartMessage").hide();
	}
}
// 선택 시 장바구니 총 갯수, 총 금액 계산
function updateCartSummary() {
    let selected = $('.cart-select:checked');
    let totalQty = 0;
    let totalPrice = 0;

    if (selected.length === 0) {
        // 선택 없을 때 전체 계산
        $('.num-product').each(function () {
            const qty = parseInt($(this).val());
            const price = parseInt($(this).data('price'));
            totalQty += qty;
            totalPrice += qty * price;
        });
    } else {
        // 선택된 항목만 계산
        selected.each(function () {
            const row = $(this).closest('tr');
            const qty = parseInt(row.find('.num-product').val());
            const price = parseInt(row.find('.num-product').data('price'));
            totalQty += qty;
            totalPrice += qty * price;
        });
    }

    $('#totalCount').text(totalQty + " 개");
    $('#totalPrice').text(totalPrice.toLocaleString() + "원");
}
// 상품 상세 수량 및 총 금액 계산
function updateTotalPrice() {
    const qty = parseInt($('#quantity').val()) || 1;
    const unitPriceRaw = $('#unitPrice').val();
    const unitPrice = parseInt(unitPriceRaw);
    const total = unitPrice * qty;

    $('#totalPrice').text(total.toLocaleString());
}

$(document).ready(function () {
	// 페이지 로드시 초기금액
    updateTotalPrice();
});
	
// 배송지 목록 불러오기
function loadShippingAddresses() {
    $.ajax({
        url: 'getShippingAddresses', // 이건 GET으로 userId 세션에서 처리
        method: 'GET',
        success: function(addresses) {
            const tbody = $("#addressTableBody");
            tbody.empty();

            if (addresses.length === 0) {
                tbody.append(`<tr><td colspan="5">등록된 배송지가 없습니다.</td></tr>`);
                return;
            }

            addresses.forEach(addr => {
                const row = `
                    <tr>
                        <td>${addr.addressName || ''}</td>
                        <td>${addr.receiverName}</td>
                        <td>${addr.receiverPhone}</td>
                        <td>${addr.address1} ${addr.address2} (${addr.zipcode})</td>
                        <td>
                            <button class="btn btn-sm btn-success" onclick='selectAddress(${JSON.stringify(addr)})'>선택</button>
                        </td>
                    </tr>`;
                tbody.append(row);
            });
        }
    });
}

// 배송지 주소 변경 선택
function selectAddress(addr) {
    // 주소 정보 표시
    $("#selectedAddress").html(`
        <strong>${addr.receiverName}</strong> (${addr.receiverPhone})<br/>
        ${addr.address1} ${addr.address2} (${addr.zipcode})
    `);

    // hidden input에 배송지 ID 설정
    $("#shippingAddressId").val(addr.addressId);

    // 메모 필드에 해당 배송지의 memo 반영
    $("#deliveryMemo").val(addr.deliveryMemo || ""); // null이면 공백 처리

    // 모달 닫기
    $("#addressModal").modal("hide");
}
// 예외 로그 검색창 
function gcSubmitSearch() {
    const keyword = document.getElementById("gcSearchKeyword").value.trim();
    loadAdminPage(`/exceptionLogs?curPage=1&keyword=${encodeURIComponent(keyword)}`);
}

// 예외 로그 시간 변경
function formatDateTime(isoStr) {
    const date = new Date(isoStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");  // 0-based
    const day = String(date.getDate()).padStart(2, "0");

    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    const seconds = String(date.getSeconds()).padStart(2, "0");

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}