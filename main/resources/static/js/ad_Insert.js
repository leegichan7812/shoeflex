function initInsertPage() {
    console.log("▶ initInsertPage 실행됨");

    // 행 추가
    function addRow() {
        const firstRow = document.querySelector("#productTable tr");
        const newRow = firstRow.cloneNode(true);

        newRow.querySelectorAll("input").forEach(input => {
            input.value = '';
            input.classList.add("productInput");
        });
        newRow.querySelectorAll("select").forEach(select => {
            select.selectedIndex = 0;
            select.classList.add("productInput");
        });

        document.getElementById("productTable").appendChild(newRow);
        bindInputEvents();
    }

    // 파일 선택 시
    function handleFileSelect(input) {
        if (input.files.length > 0) {
            document.getElementById("fileNameArea").innerText =
                "선택한 파일: " + input.files[0].name;

            // 입력폼 비활성화
            document.querySelectorAll(".productInput").forEach(e => {
                e.disabled = true;
                e.classList.add("disabled");
            });
            document.getElementById("addRowBtn").disabled = true;
            document.getElementById("addRowBtn").classList.add("disabled");
        }
    }

    function bindInputEvents() {
        document.querySelectorAll(".productInput").forEach(e => {
            e.removeEventListener("input", disableFileInput);
            e.removeEventListener("change", disableFileInput);

            e.addEventListener("input", disableFileInput);
            e.addEventListener("change", disableFileInput);
        });
    }

    function disableFileInput() {
        document.getElementById("fileSelectBtn").disabled = true;
        document.getElementById("fileSelectBtn").classList.add("disabled");
    }

    function downloadTemplate() {
        window.location.href = ctx + "/product/downloadTemplate";
    }

    // 최초 바인딩
    bindInputEvents();

    // Ajax 등록 이벤트
    $('#submitBtn').off("click").on("click", function() {
        const formData = new FormData(document.getElementById("productForm"));

        $.ajax({
            url: ctx + "/product/ajaxMultiInsert",
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            success: function(res) {
                if (res.status === "success") {
                    $('#productIds').val(res.productIds.join(","));
                    renderColorSizeRows(res.productList);
                    $('#colorSizeSection').show();
                } else {
                    alert("등록 실패: " + res.message);
                }
            },
            error: function() {
                alert("등록 오류 발생");
            }
        });
    });

    function renderColorSizeRows(productList) {
        let html = "";
        productList.forEach(prod => {
            html += "<tr>";
            html += "<td>" + prod.name + "</td>";

            html += "<td><select name='color_" + prod.productId + "'>";
            colorList.forEach(c => {
                html += "<option value='" + c.colorId + "'>" + c.colorNameKor + "</option>";
            });
            html += "</select></td>";

            sizeList.forEach(s => {
                html += "<td><input type='number' name='stock_" + prod.productId + "_" + s.sizeId + "' value='0' min='0' style='width:50px;'></td>";
            });

            html += "</tr>";
        });

        $('#colorSizeBody').html(html);
    }

    // 색상/사이즈 재고 등록
    $('#registerColorSizeBtn').off("click").on("click", function() {
        const data = $('#colorSizeForm').serialize();

        $.post(ctx + "/product/insertColorSizeMultiAjax", data, function(res) {
            if (res === "success") {
                alert("재고 등록 완료!");
                parent.$("#content-area").load("/product/ad_Insert", function(){
                    if (typeof initInsertPage === "function") initInsertPage();
                });
            } else {
                alert("재고 등록 실패");
            }
        });
    });

    // 전역에서 쓰려면 함수 export
    window.addRow = addRow;
    window.handleFileSelect = handleFileSelect;
    window.downloadTemplate = downloadTemplate;
}
