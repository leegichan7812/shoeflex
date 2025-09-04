function initReadPage() {
    console.log("▶ initReadPage 실행됨");

    // 전역 상태로 필터 유지
    window.currentFilter = window.currentFilter || {
        name: window.filterName || "",
        brand: window.filterBrand || "",
        category: window.filterCategory || ""
    };

    checkResetButton();

    // ---------------------------
    // 상품 클릭 시 상세 조회
    // ---------------------------
    $(document).off("click", "#content-area .product-row")
               .on("click", "#content-area .product-row", function(){
        const $this = $(this);
        const productId = $this.data("product-id");
        const $detailRow = $this.next(".detail-row");
        const $detailArea = $detailRow.find(".detail-area");

        $(".product-row").removeClass("active");
        $this.addClass("active");

        if ($detailRow.is(":visible")) {
            $detailRow.hide();
            $detailArea.empty();
        } else {
            $detailArea.html("로딩중...");
            $.getJSON("/product/detailStock", { productId }, function(data){
                let colorMap = {};
                data.forEach(item => {
                    const color = item.colorName;
                    const size = Number(item.sizeValue);
                    const stock = Number(item.stock || 0);

                    if (!colorMap[color]) {
                        colorMap[color] = {
                            sizeStock: {},
                            colorTotal: 0,
                            colorStatus: item.colorStatus,
                            colorId: item.colorId,
                            productId: item.productId
                        };
                    }
                    const row = colorMap[color];
                    row.sizeStock[size] = (row.sizeStock[size] || 0) + stock;
                    row.colorTotal += stock;
                });

                let html = "<table style='width:100%; border:1px solid #ccc; border-collapse:collapse;'>";
                html += "<tr>";
                html += "<th style='width:50px;'>구분</th>";
                html += "<th style='width:150px'>색상</th>";
                for (let size=220; size<=280; size+=5) html += "<th>"+size+"</th>";
                html += "<th style='width:100px'>색상 총재고</th>";
                html += "<th style='width:90px;'>일시중단</th>";
                html += "</tr>";

                for (const color in colorMap) {
                    const row = colorMap[color];
                    const colorId = row.colorId;
                    const isPaused = (row.colorStatus === '일시중단');

                    html += "<tr>";
                    html += '<td><input type="checkbox" class="color-check" data-product-id="'+row.productId+'" data-color-id="'+colorId+'"></td>';

                    const nameWithFlag = isPaused
                        ? (color + ' <span style="color:#e67e22; font-size:12px;">(일시중단중)</span>')
                        : color;
                    html += '<td>' + nameWithFlag + '</td>';

                    for (let size = 220; size <= 280; size += 5) {
                        const stockVal = (row.sizeStock[size] ?? 0);
                        const readonlyAttr = isPaused ? ' readonly' : '';
                        const readStyle    = isPaused ? ' background:gray; cursor:not-allowed;' : '';
                        const titleAttr    = isPaused ? ' title="일시중단 상태에서는 수정할 수 없습니다."' : '';
                        html += '<td><input type="text" class="stock-input form-control form-control-sm" '+
                                'min="0" value="'+stockVal+'" '+
                                'data-product-id="'+row.productId+'" '+
                                'data-color-id="'+colorId+'" '+
                                'data-size="'+size+'" '+
                                readonlyAttr + titleAttr +
                                ' style="width:100%; text-align:center;'+readStyle+'"></td>';
                    }
                    html += "<td>" + row.colorTotal + "</td>";

                    html += "<td>";
                    if (isPaused) {
                        html += '<button type="button" class="pause-color-btn btn btn-success" data-product-id="'+row.productId+'" data-color-id="'+colorId+'">재판매</button>';
                    } else {
                        html += '<button type="button" class="pause-color-btn btn btn-warning" data-product-id="'+row.productId+'" data-color-id="'+colorId+'">일시중단</button>';
                    }
                    html += "</td>";
                    html += "</tr>";
                }

                html += "</table>";
                html += `
                    <div style="text-align:right; margin-top:10px;">
                        <button type="button" class="update-color-btn btn btn-primary me-2">수정하기</button>
                        <button type="button" class="delete-color-btn btn btn-danger">삭제하기</button>
                    </div>
                `;

                $detailArea.html(html);
                $detailRow.show();
            }).fail(() => {
                $detailArea.html("<div style='color:#c00;'>재고 조회에 실패했습니다.</div>");
                $detailRow.show();
            });
        }
    });

    // ---------------------------
    // 검색 & 필터
    // ---------------------------
    $(document).off("keypress", "#searchName")
               .on("keypress", "#searchName", function(e){
        if (e.which == 13) {
            currentFilter.name = $('#searchName').val();
            triggerFilter(1);
        }
    });

    $(document).off("change", "#brandFilter, #categoryFilter")
               .on("change", "#brandFilter, #categoryFilter", function(){
        currentFilter.name = $('#searchName').val();
        currentFilter.brand = $('#brandFilter').val();
        currentFilter.category = $('#categoryFilter').val();
        triggerFilter(1);
		console.log("[checkResetButton] 현재 필터:", currentFilter);
    });

    $(document).off("click", "#resetFilterBtn")
               .on("click", "#resetFilterBtn", function(){
        $('#searchName').val('');
        $('#brandFilter').val('');
        $('#categoryFilter').val('');
        currentFilter = {name:"", brand:"", category:""};
        triggerFilter(1);
		console.log("[checkResetButton] 현재 필터:", currentFilter);
    });

    // ---------------------------
    // 페이징
    // ---------------------------
    $(document).off("click", "#content-area .page-btn")
               .on("click", "#content-area .page-btn", function(){
        const page = $(this).data("page");
        triggerFilter(page);
    });

    // ---------------------------
    // 상품 일시중단/재판매 버튼
    // ---------------------------
    $(document).off("click", "#content-area .pause-product-btn")
               .on("click", "#content-area .pause-product-btn", function(){
        const productId = $(this).data('product-id');
        const isPaused = $(this).text().trim() === '재판매';
        const newStatus = isPaused ? '판매중' : '일시중단';
        const msg = isPaused ? '이 상품을 다시 판매하시겠습니까?' : '이 상품을 일시중단 하시겠습니까?';
        if(confirm(msg)) {
            $.post('/product/updateStatus', { productId, status: newStatus }, function(res){
                alert(res.msg || (isPaused ? "재판매 처리되었습니다." : "일시중단 처리되었습니다."));
                triggerFilter(window.currentPage || 1);
            });
        }
    });

    // ---------------------------
    // 상품 삭제 버튼
    // ---------------------------
    $(document).off("click", "#content-area .del-product-btn")
               .on("click", "#content-area .del-product-btn", function(){
        const productId = $(this).data('product-id');
        if(confirm('정말 이 상품을 판매중지(삭제) 하시겠습니까?')) {
            $.post('/product/updateStatus', { productId, status: '판매중지' }, function(res){
                alert(res.msg || "삭제 처리되었습니다.");
                triggerFilter(window.currentPage || 1);
            });
        }
    });

    // ---------------------------
    // 색상 일시중단/재판매
    // ---------------------------
    $(document).off("click", "#content-area .pause-color-btn")
               .on("click", "#content-area .pause-color-btn", function(){
        const $btn = $(this);
        const $tr = $btn.closest("tr");

        if ($btn.data("busy")) return;
        if (!$tr.find(".color-check").prop("checked")) {
            alert("왼쪽 '구분' 체크박스를 먼저 선택하세요.");
            return;
        }

        const productId = $btn.data("product-id");
        const colorId   = $btn.data("color-id");
        const goingToPause = $btn.hasClass("btn-warning");
        const url = goingToPause ? "/product/pauseColor" : "/product/resumeColor";

        const $colorCell = $tr.find("td").eq(1);

        const applyPausedUI = () => {
            const now = $colorCell.text().trim().replace(/\s*\(일시중단중\)\s*$/, "");
            $colorCell.html(now + " <span style='color:#e67e22; font-size:12px;'>(일시중단중)</span>");
            $btn.removeClass("btn-warning").addClass("btn-success").text("재판매");
        };
        const applyResumedUI = () => {
            const now = $colorCell.text().trim().replace(/\s*\(일시중단중\)\s*$/, "");
            $colorCell.text(now);
            $btn.removeClass("btn-success").addClass("btn-warning").text("일시중단");
        };

        $btn.data("busy", true).prop("disabled", true);
        $.post(url, { productId, colorId })
            .done(res => {
                if (res && res.success) {
                    goingToPause ? applyPausedUI() : applyResumedUI();
                } else {
                    alert(res?.msg || "상태 변경에 실패했습니다.");
                }
            })
            .fail(xhr => alert("요청 실패 ("+xhr.status+")"))
            .always(() => $btn.data("busy", false).prop("disabled", false));
    });

    // ---------------------------
    // 색상 재고 수정
    // ---------------------------
    $(document).off("click", "#content-area .update-color-btn")
               .on("click", "#content-area .update-color-btn", function(){
        const $detailArea = $(this).closest(".detail-area");
        const $detailRow  = $detailArea.closest(".detail-row");
        const $productRow = $detailRow.prev(".product-row");

        const $checkedRows = $detailArea.find("tr").has(".color-check:checked");
        if ($checkedRows.length === 0) {
            alert("수정할 색상을 먼저 '구분'에서 체크하세요.");
            return;
        }

        const updates = [];
        let invalid = null;
        $checkedRows.each(function () {
            const $row = $(this);
            const pId  = $row.find(".color-check").data("product-id");
            const cId  = $row.find(".color-check").data("color-id");

            $row.find(".stock-input").each(function () {
                const $inp = $(this);
                if ($inp.prop("readonly")) return;
                const sizeValue = Number($inp.data("size"));
                const stock     = Number($inp.val());
                if (!Number.isInteger(stock) || stock < 0) {
                    invalid = "재고는 0 이상의 정수여야 합니다. (색상:"+$row.find("td").eq(1).text().trim()+", 사이즈:"+sizeValue+")";
                    return false;
                }
                updates.push({ productId: pId, colorId: cId, sizeValue, stock });
            });
            if (invalid) return false;
        });

        if (invalid) { alert(invalid); return; }
        if (updates.length === 0) { alert("변경할 값이 없습니다."); return; }

        const $btn = $(this);
        $btn.prop("disabled", true).text("저장 중...");
        $.ajax({
            url: "/product/updateStocks",
            method: "POST",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({ updates })
        })
        .done(res => {
            if (res && res.success) {
                alert(res.msg || "재고가 저장되었습니다.");
                $detailRow.hide(); $detailArea.empty(); $productRow.trigger("click");
            } else {
                alert(res?.msg || "재고 저장에 실패했습니다.");
            }
        })
        .fail(xhr => alert("요청 실패 ("+xhr.status+")"))
        .always(() => $btn.prop("disabled", false).text("수정하기"));
    });

    // ---------------------------
    // 색상 삭제 (판매중지 처리)
    // ---------------------------
    $(document).off("click", "#content-area .delete-color-btn")
               .on("click", "#content-area .delete-color-btn", function(){
        const $btn = $(this);
        const $detailArea = $btn.closest(".detail-area");
        const $detailRow  = $detailArea.closest(".detail-row");
        const $productRow = $detailRow.prev(".product-row");

        const $checks = $detailArea.find(".color-check:checked");
        if ($checks.length === 0) { alert("먼저 '구분'에서 색상을 선택하세요."); return; }
        if (!confirm("선택한 색상을 '판매중지'로 변경할까요?")) return;

        const reqs = [];
        $checks.each(function(){
            const $c = $(this);
            const productId = $c.data("product-id") || $productRow.data("product-id");
            const colorId   = $c.data("color-id");
            const colorName = $c.closest("tr").find("td").eq(1).text().trim().replace(/\s*\(.*?\)\s*$/, "");
            if (productId != null && colorId != null) {
                reqs.push({ productId, colorId, colorName });
            }
        });
        if (reqs.length === 0) { alert("선택된 항목이 없습니다."); return; }

        $btn.prop("disabled", true).text("처리 중...");
        let success = [], failed = [], doneCount = 0;
        reqs.forEach(r => {
            $.post("/product/updateColorStatus", { productId: r.productId, colorId: r.colorId })
            .done(res => { (res && res.success) ? success.push(r) : failed.push(r); })
            .fail(() => failed.push(r))
            .always(() => {
                doneCount++;
                if (doneCount === reqs.length) {
                    $detailRow.hide(); $detailArea.empty(); $productRow.trigger("click");
                    let msg = "판매중지 처리 완료\n- 성공: "+success.length+"건\n- 실패: "+failed.length+"건";
                    if (failed.length) {
                        msg += "\n실패: "+failed.map(x => x.colorName+"(ID:"+x.colorId+")").join(", ");
                    }
                    alert(msg);
                    $btn.prop("disabled", false).text("삭제하기");
                }
            });
        });
    });

    // ---------------------------
    // 유틸 함수
    // ---------------------------
	function triggerFilter(page) {
	    if (!page) page = 1;
	    if ($("#content-area").length === 0) { location.reload(); return; }

	    // ✅ tbody만 교체 (Ajax fragment 응답 필요)
	    $("#productTbody").load(
	        "/product/ad_Read?page=" + page +
	        "&name=" + encodeURIComponent(currentFilter.name) +
	        "&brand=" + encodeURIComponent(currentFilter.brand) +
	        "&category=" + encodeURIComponent(currentFilter.category) +
	        " #productTbody > *",   // 응답에서 #productTbody 안쪽만 가져오기
	        function() {
	            if (typeof checkResetButton === "function") {
	                checkResetButton();
	            }
	        }
	    );

	    // ✅ 페이징 영역도 교체
	    $(".pagination").load(
	        "/product/ad_Read?page=" + page +
	        "&name=" + encodeURIComponent(currentFilter.name) +
	        "&brand=" + encodeURIComponent(currentFilter.brand) +
	        "&category=" + encodeURIComponent(currentFilter.category) +
	        " .pagination > *"
	    );
	}



	function checkResetButton(){
	    if(
	        (currentFilter.name && currentFilter.name.trim() !== "") ||
	        (currentFilter.brand && currentFilter.brand.trim() !== "") ||
	        (currentFilter.category && currentFilter.category.trim() !== "")
	    ){
	        $('#resetFilterBtn').show();
	    } else {
	        $('#resetFilterBtn').hide();
	    }
		console.log("[checkResetButton] 현재 필터:", currentFilter);
	}
}
