<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>매출현황</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Tabulator -->
<link href="https://unpkg.com/tabulator-tables@5.6.1/dist/css/tabulator_bootstrap5.min.css" rel="stylesheet">
<script src="https://unpkg.com/tabulator-tables@5.6.1/dist/js/tabulator.min.js"></script>

<!-- 엑셀 다운로드용 (Tabulator xlsx export 의존 라이브러리) -->
<script src="https://oss.sheetjs.com/sheetjs/xlsx.full.min.js"></script>

<style>
<%@ include file="/WEB-INF/common/menu_style.jspf" %>

  #report-table.filters-hidden .tabulator-col:not([tabulator-field="_no"]) .tabulator-header-filter {
    display: none;
  }

  #report-table .tabulator-header-filter input,
  #report-table .tabulator-header-filter select {
    font-size: 0.75rem;
    padding: 2px 4px;
  }

  /* 검색 영역을 화면 상단에 고정 */
  .search-card-sticky {
    position: sticky;
    top: 0;
    z-index: 20;
    background: #fff;
  }
</style>
</head>
<body>

<%@ include file="/WEB-INF/common/menu_nav.jspf" %>

  <!-- 콘텐츠 영역 -->
  <div class="flex-grow-1 p-4">
    <div class="d-flex align-items-center mb-3">
      <h4 class="fw-bold mb-0">매출현황</h4>
    </div>

    <!-- 검색 영역 -->
    <div class="card mb-3 search-card-sticky">
      <div class="card-body">
        <div class="row g-3 align-items-end">
          <div class="col-auto">
            <label class="form-label small mb-1 d-block">검색구분</label>
            <div class="btn-group btn-group-sm" role="group">
              <input type="radio" class="btn-check" name="searchType" id="searchTypeAll" value="all" checked>
              <label class="btn btn-outline-primary" for="searchTypeAll">전체</label>

              <input type="radio" class="btn-check" name="searchType" id="searchTypeSabun" value="sabun">
              <label class="btn btn-outline-primary" for="searchTypeSabun">사원검색</label>

              <input type="radio" class="btn-check" name="searchType" id="searchTypeCust" value="cust">
              <label class="btn btn-outline-primary" for="searchTypeCust">거래처검색</label>
            </div>
          </div>

          <div class="col-auto" id="searchKeywordWrap" style="display:none;">
            <label class="form-label small mb-1" id="searchKeywordLabel">검색어</label>
            <input type="text" id="searchKeyword" class="form-control form-control-sm" placeholder="검색어 입력">
          </div>

          <div class="col-auto">
            <label class="form-label small mb-1 d-block">기간일자</label>
            <div class="d-flex align-items-center gap-1">
              <input type="date" id="dateFrom" class="form-control form-control-sm">
              <span>~</span>
              <input type="date" id="dateTo" class="form-control form-control-sm">
            </div>
          </div>

          <div class="col-auto">
            <button class="btn btn-sm btn-primary me-1" onclick="handleSearch()">검색</button>
            <button class="btn btn-sm btn-success" onclick="handleExcelDownload()">엑셀</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 매출현황 그리드 -->
    <div id="report-table"></div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/common/menu_script.jspf" %>
<script>
const API_URL = "/ar303_data.do";
let table;
let filterToggleBtn = null;
let filtersHidden = false;

function toggleHeaderFilters() {
  filtersHidden = !filtersHidden;
  document.getElementById("report-table").classList.toggle("filters-hidden", filtersHidden);
  if (filterToggleBtn) {
    filterToggleBtn.textContent = filtersHidden ? "필터 보이기" : "필터 숨기기";
  }
}

function formatNumber(n) {
  var v = Number(n || 0);
  return v.toLocaleString("ko-KR");
}

// toISOString()은 UTC로 변환하면서 한국(UTC+9) 기준 자정 근처 날짜가 하루 밀리는 문제가 있어,
// 로컬 날짜 그대로 "YYYY-MM-DD" 문자열을 만든다.
function formatDateLocal(d) {
  var y = d.getFullYear();
  var m = String(d.getMonth() + 1).padStart(2, "0");
  var day = String(d.getDate()).padStart(2, "0");
  return y + "-" + m + "-" + day;
}
function moneyFormatter(cell) {
  return formatNumber(cell.getValue());
}

// ---- 금액 컬럼용 "기준금액보다 크다/작다" 헤더 필터 ----
// 필터 입력창에 ">100000" 또는 "<50000" 처럼 부등호 + 숫자를 입력하면 비교 필터링됨.
// 부등호 없이 숫자만 입력하면 기본으로 "크다(>)" 로 처리.
function numberCompareFilterFunc(headerValue, rowValue, rowData, filterParams) {
  if (headerValue === undefined || headerValue === null || headerValue === "") return true;

  var str = String(headerValue).trim();
  var op = ">";
  if (str.charAt(0) === ">" || str.charAt(0) === "<") {
    op = str.charAt(0);
    str = str.slice(1).trim();
  }

  var num = Number(str);
  if (str === "" || isNaN(num)) return true;

  var rv = Number(rowValue || 0);
  return op === ">" ? rv > num : rv < num;
}

document.addEventListener("DOMContentLoaded", function () {
  // 기간일자 기본값 = 당월 1일 ~ 오늘
  var today = new Date();
  var firstOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
  document.getElementById("dateFrom").value = formatDateLocal(firstOfMonth);
  document.getElementById("dateTo").value = formatDateLocal(today);

  // 검색구분(전체/사원검색/거래처검색)에 따라 검색어 입력창 표시/라벨 변경
  document.querySelectorAll('input[name="searchType"]').forEach(function (radio) {
    radio.addEventListener("change", function () {
      var wrap = document.getElementById("searchKeywordWrap");
      var label = document.getElementById("searchKeywordLabel");
      var keywordInput = document.getElementById("searchKeyword");

      if (this.value === "all") {
        wrap.style.display = "none";
        keywordInput.value = "";
      } else {
        wrap.style.display = "";
        if (this.value === "sabun") {
          label.textContent = "사원명";
          keywordInput.placeholder = "사원명 입력";
        } else {
          label.textContent = "거래처명";
          keywordInput.placeholder = "거래처명 입력";
        }
      }
    });
  });

  table = new Tabulator("#report-table", {
    layout: "fitColumns",
    height: "calc(100vh - 260px)",
    placeholder: "조회된 데이터가 없습니다.",
    columns: [
      { title: "No", field: "_no", formatter: "rownum", hozAlign: "center", width: 55, headerSort: false,
        headerFilter: function (cell, onRendered, success, cancel, editorParams) {
          var btn = document.createElement("button");
          btn.type = "button";
          btn.className = "btn btn-sm btn-outline-secondary w-100";
          btn.style.padding = "1px 2px";
          btn.style.fontSize = "0.7rem";
          btn.textContent = "필터 숨기기";
          btn.addEventListener("click", function (e) {
            e.preventDefault();
            toggleHeaderFilters();
          });
          filterToggleBtn = btn;
          return btn;
        }
      },
      { title: "사원명", field: "SABUNNM", hozAlign: "left", width: 110,
        headerFilter: "input", headerFilterPlaceholder: "필터",
        bottomCalc: function () { return "합계"; } },
      { title: "거래처명", field: "CUSTNAME", hozAlign: "left",
        headerFilter: "input", headerFilterPlaceholder: "필터" },
      { title: "매출액", field: "AMT", hozAlign: "right", width: 110,
        headerFilter: "input", headerFilterPlaceholder: "예: >100000", headerFilterFunc: numberCompareFilterFunc,
        formatter: moneyFormatter, bottomCalc: "sum", bottomCalcFormatter: moneyFormatter },
      { title: "반품", field: "BANPUM", hozAlign: "right", width: 100,
        headerFilter: "input", headerFilterPlaceholder: "예: >100000", headerFilterFunc: numberCompareFilterFunc,
        formatter: moneyFormatter, bottomCalc: "sum", bottomCalcFormatter: moneyFormatter },
      { title: "실매출", field: "MCAMT", hozAlign: "right", width: 110,
        headerFilter: "input", headerFilterPlaceholder: "예: >100000", headerFilterFunc: numberCompareFilterFunc,
        formatter: moneyFormatter, bottomCalc: "sum", bottomCalcFormatter: moneyFormatter },
      { title: "수금", field: "SKAMT", hozAlign: "right", width: 100,
        headerFilter: "input", headerFilterPlaceholder: "예: >100000", headerFilterFunc: numberCompareFilterFunc,
        formatter: moneyFormatter, bottomCalc: "sum", bottomCalcFormatter: moneyFormatter },
      { title: "조정", field: "SALEAMT", hozAlign: "right", width: 100,
        headerFilter: "input", headerFilterPlaceholder: "예: >100000", headerFilterFunc: numberCompareFilterFunc,
        formatter: moneyFormatter, bottomCalc: "sum", bottomCalcFormatter: moneyFormatter },
      { title: "이익", field: "PROFIT", hozAlign: "right", width: 100,
        headerFilter: "input", headerFilterPlaceholder: "예: >100000", headerFilterFunc: numberCompareFilterFunc,
        formatter: moneyFormatter, bottomCalc: "sum", bottomCalcFormatter: moneyFormatter },
      { title: "미수금", field: "JANAMT", hozAlign: "right", width: 110,
        headerFilter: "input", headerFilterPlaceholder: "예: >100000", headerFilterFunc: numberCompareFilterFunc,
        formatter: moneyFormatter, bottomCalc: "sum", bottomCalcFormatter: moneyFormatter },
    ]
  });

  handleSearch();
  checkLogin();
});

function getSearchType() {
  var checked = document.querySelector('input[name="searchType"]:checked');
  return checked ? checked.value : "all";
}

function handleSearch() {
  var type = getSearchType();
  var keyword = document.getElementById("searchKeyword").value.trim();
  var dateFrom = document.getElementById("dateFrom").value;
  var dateTo = document.getElementById("dateTo").value;

  var params = new URLSearchParams();
  params.append("type", type);
  if (type !== "all" && keyword) params.append("keyword", keyword);
  if (dateFrom) params.append("from", dateFrom);
  if (dateTo) params.append("to", dateTo);

  fetch(API_URL + "?" + params.toString())
    .then(function (res) { return res.json(); })
    .then(function (data) { table.setData(data); })
    .catch(function () { alert("매출현황 조회 중 오류가 발생했습니다."); });
}

function handleExcelDownload() {
  table.download("xlsx", "매출현황.xlsx", { sheetName: "매출현황" });
}
</script>

</body>
</html>