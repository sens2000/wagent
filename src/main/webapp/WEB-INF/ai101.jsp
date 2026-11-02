<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>판매</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Tabulator -->
<link href="https://unpkg.com/tabulator-tables@5.6.1/dist/css/tabulator_bootstrap5.min.css" rel="stylesheet">
<script src="https://unpkg.com/tabulator-tables@5.6.1/dist/js/tabulator.min.js"></script>
<!-- SheetJS: Tabulator의 엑셀(xlsx) 다운로드 기능에 필요 -->
<script src="https://cdn.jsdelivr.net/npm/xlsx@0.18.5/dist/xlsx.full.min.js"></script>

<style>
<%@ include file="/WEB-INF/common/menu_style.jspf" %>

  .misu-table th, .misu-table td {
    font-size: 0.9rem;
  }
  .misu-table td.balance {
    font-weight: 700;
  }

  .tabulator-row.tabulator-selected {
    background-color: #cfe2ff !important;
  }

  /* SMS전송 그리드의 '내역' 칸: 다 안보이면 줄바꿈해서 아래줄에 이어서 보여준다 */
  #sms-table .sms-memo-cell {
    white-space: normal;
    word-break: break-word;
  }

  #product-table .tabulator-col[tabulator-field="JEPMCD"],
  #product-table .tabulator-col[tabulator-field="BARCODE"],
  #product-table .tabulator-col[tabulator-field="JEPMNM"] {
    cursor: pointer;
  }

</style>
</head>
<body>

<%@ include file="/WEB-INF/common/menu_nav.jspf" %>

  <!-- 콘텐츠 영역 -->
  <div class="flex-grow-1 p-4">

    <!-- 상단: 거래처정보 -->
    <div class="card mb-3">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-start mb-3 flex-wrap gap-2">
          <h5 class="fw-bold mb-0">거래처정보</h5>
          <div class="d-flex flex-wrap gap-2">
            <div>
              <button class="btn btn-sm btn-primary me-1" onclick="handleQuery()">조회</button>
              <button class="btn btn-sm btn-success me-1" onclick="handleNew()">신규</button>
              <button class="btn btn-sm btn-outline-primary me-1" onclick="handleSave()">저장</button>
              <button class="btn btn-sm btn-danger me-1" onclick="handleDelete()">삭제</button>
              <button class="btn btn-sm btn-outline-secondary" onclick="openStatement()">명세서</button>
            </div>
            <div>
              <button class="btn btn-sm btn-outline-primary me-1" onclick="openDetailView()">상세보기</button>
              <button class="btn btn-sm btn-outline-success me-1" onclick="openSms()">SMS전송</button>
              <button class="btn btn-sm btn-outline-secondary" onclick="openMemo()">메모</button>
            </div>
          </div>
        </div>
        <div class="row g-3 align-items-end">
          <div class="col-6 col-md-2">
            <label class="form-label small mb-1">판매일자</label>
            <input type="date" id="ipdate" class="form-control form-control-sm">
          </div>
          <div class="col-6 col-md-3">
            <label class="form-label small mb-1">거래처명</label>
            <input type="hidden" id="infoCustcd">
            <input type="hidden" id="infoJpno">
            <div class="input-group input-group-sm">
              <input type="text" id="infoCustname" class="form-control" readonly placeholder="검색해서 선택하세요">
              <button class="btn btn-outline-primary" type="button" onclick="openCustSearch()">검색</button>
            </div>
          </div>
          <div class="col-6 col-md-2">
            <label class="form-label small mb-1">대표</label>
            <input type="text" id="infoOwner" class="form-control form-control-sm" readonly>
          </div>
          <div class="col-6 col-md-2">
            <label class="form-label small mb-1">전화번호</label>
            <input type="text" id="infoHandno" class="form-control form-control-sm" readonly>
          </div>
          <div class="col-12 col-md-3">
            <label class="form-label small mb-1">사업자번호</label>
            <input type="text" id="infoSano" class="form-control form-control-sm" readonly>
          </div>
        </div>
      </div>
    </div>

    <!-- 중간: 미수현황 -->
    <div class="card mb-3">
      <div class="card-body">
        <h5 class="fw-bold mb-3">미수현황</h5>
        <div class="table-responsive">
          <table class="table table-bordered table-sm text-center align-middle mb-0 misu-table">
            <thead class="table-light">
              <tr>
                <th style="width:80px;">구분</th>
                <th>전미수</th>
                <th>매출</th>
                <th>반품</th>
                <th>수금</th>
                <th>조정</th>
                <th>잔액</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th class="table-light">당일</th>
                <td id="dayEwol">0</td>
                <td id="dayMcamt">0</td>
                <td id="dayBanpum">0</td>
                <td id="daySkamt">0</td>
                <td id="daySaleamt">0</td>
                <td id="dayJanamt" class="balance">0</td>
              </tr>
              <tr>
                <th class="table-light">당월</th>
                <td id="monEwol">0</td>
                <td id="monMcamt">0</td>
                <td id="monBanpum">0</td>
                <td id="monSkamt">0</td>
                <td id="monSaleamt">0</td>
                <td id="monJanamt" class="balance">0</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 하단: 판매제품 -->
    <div class="card">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h5 class="fw-bold mb-0">판매제품</h5>
          <button class="btn btn-sm btn-success" onclick="addProductRow()">제품추가</button>
        </div>
        <div id="product-table"></div>
      </div>
    </div>

  </div>
</div>

<!-- 거래처 검색 모달 -->
<div class="modal fade" id="custSearchModal" tabindex="-1">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">거래처 검색</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <form onsubmit="event.preventDefault(); searchCustomers();">
          <div class="row g-2 mb-3">
            <div class="col-6 col-md-3">
              <label class="form-label small mb-1">거래처명</label>
              <input type="text" id="custSearchName" class="form-control form-control-sm" placeholder="거래처명">
            </div>
            <div class="col-6 col-md-3">
              <label class="form-label small mb-1">대표</label>
              <input type="text" id="custSearchOwner" class="form-control form-control-sm" placeholder="대표">
            </div>
            <div class="col-6 col-md-3">
              <label class="form-label small mb-1">전화번호</label>
              <input type="text" id="custSearchHandno" class="form-control form-control-sm" placeholder="전화번호">
            </div>
            <div class="col-6 col-md-3">
              <label class="form-label small mb-1">사업자번호</label>
              <input type="text" id="custSearchSano" class="form-control form-control-sm" placeholder="사업자번호">
            </div>
          </div>
          <div class="text-end mb-3">
            <button class="btn btn-sm btn-primary" type="submit">검색</button>
          </div>
        </form>
        <div id="cust-search-table"></div>
      </div>
    </div>
  </div>
</div>

<!-- 제품 검색 모달 -->
<div class="modal fade" id="productSearchModal" tabindex="-1">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">제품 검색</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <form onsubmit="event.preventDefault(); searchProducts();">
          <div class="row g-2 mb-3">
            <div class="col-4">
              <label class="form-label small mb-1">제품코드</label>
              <input type="text" id="productSearchCode" class="form-control form-control-sm" placeholder="제품코드">
            </div>
            <div class="col-4">
              <label class="form-label small mb-1">바코드</label>
              <input type="text" id="productSearchBarcode" class="form-control form-control-sm" placeholder="바코드">
            </div>
            <div class="col-4">
              <label class="form-label small mb-1">제품명</label>
              <input type="text" id="productSearchName" class="form-control form-control-sm" placeholder="제품명">
            </div>
          </div>
          <div class="text-end mb-3">
            <button class="btn btn-sm btn-primary" type="submit">검색</button>
          </div>
        </form>
        <p class="text-muted small mb-2">제품을 여러 개 클릭해서 선택한 뒤, 아래 "선택 추가" 버튼을 눌러주세요.</p>
        <div id="product-search-table"></div>
        <div class="text-end mt-3">
          <button type="button" class="btn btn-sm btn-success" onclick="addSelectedProducts();">선택 추가</button>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- 판매 조회 모달 -->
<div class="modal fade" id="saleQueryModal" tabindex="-1">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">판매 조회</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <div class="row g-2 mb-2 align-items-end">
          <div class="col-6 col-md-2">
            <label class="form-label small mb-1">시작일자</label>
            <input type="date" id="queryFromDate" class="form-control form-control-sm">
          </div>
          <div class="col-6 col-md-2">
            <label class="form-label small mb-1">종료일자</label>
            <input type="date" id="queryToDate" class="form-control form-control-sm">
          </div>
          <div class="col-8 col-md-5">
            <label class="form-label small mb-1">거래처</label>
            <input type="hidden" id="queryCustcd">
            <input type="text" id="queryCustname" class="form-control form-control-sm" placeholder="전체 (거래처명 입력)"
                   oninput="document.getElementById('queryCustcd').value='';">
          </div>
          <div class="col-4 col-md-3">
            <button class="btn btn-sm btn-primary w-100" type="button" onclick="searchSaleQuery()">검색</button>
          </div>
        </div>
        <p class="text-muted small mb-2">왼쪽 목록을 클릭하면 오른쪽에 상세내역이 표시됩니다. 왼쪽 목록을 더블클릭하면 메인화면에 불러옵니다.</p>
        <div class="row g-3">
          <div class="col-md-5">
            <div id="sale-query-list-table"></div>
          </div>
          <div class="col-md-7">
            <div id="sale-query-detail-table"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- 거래처 상세보기 모달 -->
<div class="modal fade" id="detailViewModal" tabindex="-1">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="detailViewModalTitle">거래처 상세보기</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <div class="d-flex justify-content-end mb-2">
          <button class="btn btn-sm btn-outline-success" type="button" onclick="downloadDetailViewExcel()">엑셀 다운로드</button>
        </div>
        <div id="detail-view-table"></div>
      </div>
    </div>
  </div>
</div>

<!-- 메모 모달 -->
<div class="modal fade" id="memoModal" tabindex="-1">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="memoModalTitle">메모</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <div class="d-flex justify-content-end gap-1 mb-2">
          <button class="btn btn-sm btn-success" type="button" onclick="handleMemoNew()">신규</button>
          <button class="btn btn-sm btn-outline-primary" type="button" onclick="handleMemoSave()">저장</button>
          <button class="btn btn-sm btn-danger" type="button" onclick="handleMemoDelete()">삭제</button>
        </div>
        <p class="text-muted small mb-2">행을 클릭해서 선택한 뒤 삭제할 수 있습니다. 메모/예정일은 셀을 클릭해서 바로 수정하세요.</p>
        <div id="memo-table"></div>
      </div>
    </div>
  </div>
</div>

<!-- SMS전송 모달 -->
<div class="modal fade" id="smsModal" tabindex="-1">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="smsModalTitle">SMS전송</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class="col-md-7">
            <div class="mb-2">가능회수 : <span id="smsCntLeft">0</span> 건</div>
            <div id="sms-table"></div>
          </div>
          <div class="col-md-5">
            <div class="mb-2">
              <label class="form-label">핸드폰번호</label>
              <input type="text" class="form-control" id="smsHandno" placeholder="010-0000-0000">
            </div>
            <div class="mb-2">
              <label class="form-label">회신번호</label>
              <input type="text" class="form-control" id="smsRecvno">
            </div>
            <div class="mb-2">
              <label class="form-label">내역</label>
              <textarea class="form-control" id="smsMemo" rows="4"></textarea>
            </div>
            <div class="mb-2">
              <label class="form-label">가능회수</label>
              <input type="text" class="form-control" id="smsCnt" readonly>
            </div>
            <button class="btn btn-sm btn-success w-100" type="button" onclick="handleSmsSend()">전송</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/common/menu_script.jspf" %>
<script>
let productTable, custSearchTable, productSearchTable;
let saleQueryListTable, saleQueryDetailTable, detailViewTable, memoTable, smsTable;
let custSearchModal, productSearchModal, saleQueryModal, detailViewModal, memoModal, smsModal;
let currentProductRow = null;
let rowSeq = 0;
// 제품 검색 모달에서 선택된 제품들 (Tabulator의 selectable 기능을 쓰지 않고
// 직접 클래스 토글 + 객체로 관리한다. - selectable/rowClick 등 내장 이벤트가
// 이 환경에서는 씹히는 경우가 있어, 다른 그리드와 동일하게 수동으로 처리)
let selectedProducts = {};

// ---- 숫자 콤마 포맷 ----
function formatNumber(n) {
  var v = Number(n || 0);
  return v.toLocaleString("ko-KR");
}

// ---- 판매제품: 빈 행 생성 ----
function blankProductRow() {
  rowSeq++;
  return {
    _rid: rowSeq,
    JEPMNO: "",
    JEPMCD: "",
    BARCODE: "",
    JEPMNM: "",
    PRICE: 0,
    SALEPRICE: 0,
    SALEPER: 0,
    QTY: 0,
    AMT: 0,
    REMK: ""
  };
}

// 할인율 수정 시: 판매가 자동계산 = 정가 - (정가 × 할인율 ÷ 100), 금액 자동 재계산
function recalcFromSaleper(row) {
  var d = row.getData();
  var price = Number(d.PRICE || 0);
  var per = Number(d.SALEPER || 0);
  var qty = Number(d.QTY || 0);

  var salePrice = Math.round(price - (price * per / 100));
  var amt = Math.round(salePrice * qty);

  row.update({ SALEPRICE: salePrice, AMT: amt });
}

// 판매가 직접 수정 시: 할인율 = (정가 - 판매가) ÷ 정가 × 100 로 역산, 금액 자동 재계산
function recalcFromSalePrice(row) {
  var d = row.getData();
  var price = Number(d.PRICE || 0);
  var salePrice = Number(d.SALEPRICE || 0);
  var qty = Number(d.QTY || 0);

  var per = price > 0 ? Math.round(((price - salePrice) / price * 100) * 10) / 10 : 0;
  var amt = Math.round(salePrice * qty);

  row.update({ SALEPER: per, AMT: amt });
}

// 수량 수정 시: 판매가/할인율은 그대로 두고 금액 = 판매가 × 수량 만 재계산
function recalcAmtOnly(row) {
  var d = row.getData();
  var salePrice = Number(d.SALEPRICE || 0);
  var qty = Number(d.QTY || 0);

  var amt = Math.round(salePrice * qty);

  row.update({ AMT: amt });
}

function addProductRow() {
  productTable.addRow(blankProductRow());
}

// 거래처 검색 결과 그리드 - 모달이 열릴 때마다 destroy 후 새로 만든다.
// (모달이 숨겨진 상태에서 만들어진 Tabulator는 클릭 이벤트 바인딩이 깨지는 경우가 있어,
//  redraw()로도 해결이 안 되면 아예 새로 생성하는 게 가장 확실하다)
// 모달 안에서는 Tabulator의 내장 rowClick이 씹히는 경우가 있어(원인 불명),
// 그리드 컨테이너에 직접 클릭 리스너를 달아 어떤 행이 클릭됐는지 수동으로 찾아낸다.
function handleCustSearchRowClick(rowEl) {
  var rows = custSearchTable.getRows();
  for (var i = 0; i < rows.length; i++) {
    if (rows[i].getElement() === rowEl) {
      var d = rows[i].getData();
      console.log("[cust-search] row clicked (manual):", d);

      try {
        document.getElementById("custSearchName").value = d.CUSTNAME || "";
        document.getElementById("custSearchOwner").value = d.OWNER || "";
        document.getElementById("custSearchHandno").value = d.HANDNO || "";
      } catch (err) {
        console.error("검색 필드 채우기 중 오류:", err, d);
      }

      selectCustomer(d);
      return;
    }
  }
}

function buildCustSearchTable() {
  custSearchTable = new Tabulator("#cust-search-table", {
    layout: "fitColumns",
    height: 300,
    placeholder: "검색 결과가 없습니다.",
    columns: [
      { title: "거래처코드", field: "CUSTCD", visible: false },
      { title: "JPNO", field: "JPNO", visible: true },
      { title: "거래처명", field: "CUSTNAME", hozAlign: "left" },
      { title: "대표", field: "OWNER", hozAlign: "center", width: 100 },
      { title: "전화번호", field: "HANDNO", hozAlign: "center", width: 130 },
      { title: "사업자번호", field: "SANO", hozAlign: "center", width: 130 },
    ]
  });
}

// 제품 검색 결과 클릭 시: 즉시 선택하지 않고, 선택 상태만 수동으로 토글한다.
// (Tabulator의 selectable 기능은 쓰지 않는다 - 내장 클릭 처리가 우리가 직접 거는
//  클릭 리스너와 같이 반응해서 두 번 토글되며 하이라이트가 사라지는 문제가 있었다.
//  그래서 클래스 토글과 선택 목록 관리를 전부 직접 처리한다)
function handleProductSearchRowClick(rowEl) {
  var rows = productSearchTable.getRows();
  for (var i = 0; i < rows.length; i++) {
    if (rows[i].getElement() === rowEl) {
      var key = "row_" + i;
      var isSelected = !!selectedProducts[key];

      if (isSelected) {
        delete selectedProducts[key];
        rowEl.classList.remove("tabulator-selected");
      } else {
        selectedProducts[key] = rows[i].getData();
        rowEl.classList.add("tabulator-selected");
      }

      console.log("[product-search] row toggled (manual):", rows[i].getData(), !isSelected);
      return;
    }
  }
}

function buildProductSearchTable() {
  selectedProducts = {};
  productSearchTable = new Tabulator("#product-search-table", {
    layout: "fitColumns",
    height: 300,
    placeholder: "검색 결과가 없습니다.",
    columns: [
      { title: "일련번호", field: "JEPMNO", visible: false },
      { title: "제품코드", field: "JEPMCD", hozAlign: "center", width: 100 },
      { title: "바코드", field: "BARCODE", hozAlign: "center", width: 130 },
      { title: "제품명", field: "JEPMNM", hozAlign: "left" },
      { title: "정가", field: "PRICE", hozAlign: "right", width: 100,
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
    ]
  });
}

// 판매조회 - 왼쪽 목록 클릭: 오른쪽에 상세내역을 표시한다.
function handleSaleQueryListRowClick(rowEl) {
  var rows = saleQueryListTable.getRows();
  for (var i = 0; i < rows.length; i++) {
    if (rows[i].getElement() === rowEl) {
      var d = rows[i].getData();
      console.log("[sale-query] row clicked (manual):", d);
      loadSaleQueryDetail(d.JPNO);
      return;
    }
  }
}

// 판매조회 - 왼쪽 목록 더블클릭: 메인화면에 불러오고 모달을 닫는다.
function handleSaleQueryListRowDblClick(rowEl) {
  var rows = saleQueryListTable.getRows();
  for (var i = 0; i < rows.length; i++) {
    if (rows[i].getElement() === rowEl) {
      var d = rows[i].getData();
      console.log("[sale-query] row double-clicked (manual):", d);
      loadSaleIntoMain(d.JPNO);
      return;
    }
  }
}

function buildSaleQueryListTable() {
  saleQueryListTable = new Tabulator("#sale-query-list-table", {
    layout: "fitColumns",
    height: 400,
    placeholder: "조회된 내역이 없습니다.",
    columns: [
      { title: "JPNO", field: "JPNO", visible: false },
      { title: "거래처코드", field: "CUSTCD", visible: false },
      { title: "일자", field: "CDATE", hozAlign: "center", width: 100 },
      { title: "거래처", field: "CUSTNAME", hozAlign: "left" },
      { title: "금액", field: "AMT", hozAlign: "right", width: 100,
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
    ]
  });
}

function buildSaleQueryDetailTable() {
  saleQueryDetailTable = new Tabulator("#sale-query-detail-table", {
    layout: "fitColumns",
    height: 400,
    placeholder: "왼쪽에서 내역을 선택해주세요.",
    columns: [
      { title: "순번", field: "SEQ", hozAlign: "center", width: 60 },
      { title: "제품코드", field: "JEPMCD", hozAlign: "center", width: 90 },
      { title: "제품명", field: "JEPMNM", hozAlign: "left" },
      { title: "정가", field: "PRICE", hozAlign: "right", width: 90,
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
      { title: "판매가", field: "SALEPRICE", hozAlign: "right", width: 90,
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
      { title: "수량", field: "QTY", hozAlign: "center", width: 60 },
      { title: "금액", field: "AMT", hozAlign: "right", width: 100,
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
      { title: "비고", field: "REMK", hozAlign: "left" },
    ]
  });
}

function buildDetailViewTable() {
  detailViewTable = new Tabulator("#detail-view-table", {
    layout: "fitColumns",
    height: 450,
    placeholder: "조회된 내역이 없습니다.",
    columns: [
        { title: "JPNO", field: "JPNO", visible: false },
      { title: "일자", field: "CDATE", hozAlign: "center", width: 100 },
      { title: "구분", field: "MCSK", hozAlign: "center", width: 90 },
      { title: "매출", field: "AMT", hozAlign: "right",
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
      { title: "반품", field: "BANPUM", hozAlign: "right",
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
      { title: "수금", field: "CASHAMT", hozAlign: "right",
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
      { title: "조정", field: "MISU", hozAlign: "right",
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
      { title: "미수", field: "MISU", hozAlign: "right",
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
      { title: "비고", field: "REMK", hozAlign: "left" },
      { title: "등록자", field: "PRUSER", hozAlign: "center", width: 100 },
    ]
  });
}

function buildMemoTable() {
  memoTable = new Tabulator("#memo-table", {
    layout: "fitColumns",
    height: 400,
    selectable: 1,
    placeholder: "메모가 없습니다.",
    columns: [
      { title: "CUSTCD", field: "CUSTCD", visible: false },
      { title: "순번", field: "SEQ", hozAlign: "center", width: 60, headerSort: false },
      { title: "메모", field: "MEMO", hozAlign: "left",
        headerSort: false,
        editor: "input" },
      { title: "등록일", field: "PRDATE", hozAlign: "center", width: 110, headerSort: false },
      { title: "예정일", field: "PLANDATE", hozAlign: "center", width: 130,
        headerSort: false,
        editor: "input", editorParams: { elementAttributes: { placeholder: "YYYY-MM-DD" } } },
    ]
  });
}

function buildSmsTable() {
  smsTable = new Tabulator("#sms-table", {
    layout: "fitColumns",
    height: 400,
    variableHeight: true,
    placeholder: "전송 내역이 없습니다.",
    columns: [
      { title: "순번", field: "SEQ", hozAlign: "center", width: 60 },
      { title: "핸드폰번호", field: "HANDNO", hozAlign: "center", width: 120 },
      { title: "내역", field: "MEMO", hozAlign: "left", cssClass: "sms-memo-cell" },
      { title: "전송일", field: "PRDATE", hozAlign: "center", width: 140,
        formatter: function (cell) {
          var d = cell.getRow().getData();
          return ((d.PRDATE || "") + " " + (d.PRTIME || "")).trim();
        } },
      { title: "회신번호", field: "RECVNO", hozAlign: "center", width: 120 },
    ]
  });
}

document.addEventListener("DOMContentLoaded", function () {
  // 판매일자 기본값 = 오늘
  document.getElementById("ipdate").value = new Date().toISOString().slice(0, 10);

  custSearchModal = new bootstrap.Modal(document.getElementById("custSearchModal"));
  productSearchModal = new bootstrap.Modal(document.getElementById("productSearchModal"));
  saleQueryModal = new bootstrap.Modal(document.getElementById("saleQueryModal"));
  detailViewModal = new bootstrap.Modal(document.getElementById("detailViewModal"));
  memoModal = new bootstrap.Modal(document.getElementById("memoModal"));
  smsModal = new bootstrap.Modal(document.getElementById("smsModal"));

  buildCustSearchTable();
  buildProductSearchTable();
  buildSaleQueryListTable();
  buildSaleQueryDetailTable();
  buildDetailViewTable();
  buildMemoTable();
  buildSmsTable();

  // 모달이 완전히 열릴 때마다 테이블을 destroy 후 재생성해서
  // 클릭 이벤트가 확실히 새로 바인딩되도록 한다.
  document.getElementById("custSearchModal").addEventListener("shown.bs.modal", function () {
    if (custSearchTable) custSearchTable.destroy();
    buildCustSearchTable();
  });
  document.getElementById("productSearchModal").addEventListener("shown.bs.modal", function () {
    if (productSearchTable) productSearchTable.destroy();
    buildProductSearchTable();
  });
  document.getElementById("saleQueryModal").addEventListener("shown.bs.modal", function () {
    if (saleQueryListTable) saleQueryListTable.destroy();
    if (saleQueryDetailTable) saleQueryDetailTable.destroy();
    buildSaleQueryListTable();
    buildSaleQueryDetailTable();
  });
  document.getElementById("detailViewModal").addEventListener("shown.bs.modal", function () {
    if (detailViewTable) detailViewTable.destroy();
    buildDetailViewTable();

    var custcd = document.getElementById("infoCustcd").value;
    if (custcd) loadDetailView(custcd);
  });
  document.getElementById("memoModal").addEventListener("shown.bs.modal", function () {
    if (memoTable) memoTable.destroy();
    buildMemoTable();

    var custcd = document.getElementById("infoCustcd").value;
    if (custcd) loadMemoList(custcd);
  });
  document.getElementById("smsModal").addEventListener("shown.bs.modal", function () {
    if (smsTable) smsTable.destroy();
    buildSmsTable();

    var custcd = document.getElementById("infoCustcd").value;
    if (custcd) loadSmsList(custcd);
  });

  // Tabulator의 rowClick이 모달 안에서 씹히는 문제 우회: 그리드 컨테이너(고정 DOM)에
  // 클릭 리스너를 딱 한 번만 걸어둔다. (모달 열 때마다 테이블은 재생성되지만 컨테이너는 그대로라
  //  여기서 한 번만 등록해도 항상 최신 custSearchTable/productSearchTable을 참조한다)
  document.querySelector("#cust-search-table").addEventListener("click", function (e) {
    var rowEl = e.target.closest(".tabulator-row");
    if (rowEl) handleCustSearchRowClick(rowEl);
  });
  document.querySelector("#product-search-table").addEventListener("click", function (e) {
    var rowEl = e.target.closest(".tabulator-row");
    if (rowEl) handleProductSearchRowClick(rowEl);
  });
  document.querySelector("#sale-query-list-table").addEventListener("click", function (e) {
    var rowEl = e.target.closest(".tabulator-row");
    if (rowEl) handleSaleQueryListRowClick(rowEl);
  });
  document.querySelector("#sale-query-list-table").addEventListener("dblclick", function (e) {
    var rowEl = e.target.closest(".tabulator-row");
    if (rowEl) handleSaleQueryListRowDblClick(rowEl);
  });

  // 판매제품 입력 그리드 - 기본 10라인 생성
  productTable = new Tabulator("#product-table", {
    layout: "fitColumns",
    selectable: 1,
    data: Array.from({ length: 5 }, blankProductRow),
    columns: [
      { title: "No", formatter: "rownum", hozAlign: "center", width: 50, headerSort: false },
      { title: "SEQ", field: "SEQ", visible: false },
      { title: "일련번호", field: "JEPMNO", visible: false },
      { title: "제품코드", field: "JEPMCD", hozAlign: "center", width: 90,
        headerSort: false,
        editor: "input",
        headerClick: function (e, column) { openProductSearchForSelectedRow(); }
      },
      { title: "바코드", field: "BARCODE", hozAlign: "center", width: 120,
        headerSort: false,
        editor: "input",
        headerClick: function (e, column) { openProductSearchForSelectedRow(); }
      },
      { title: "제품명", field: "JEPMNM", hozAlign: "left",
        headerSort: false,
        editor: "input",
        headerClick: function (e, column) { openProductSearchForSelectedRow(); }
      },
      { title: "정가", field: "PRICE", hozAlign: "right", width: 90,
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
      { title: "판매가", field: "SALEPRICE", hozAlign: "right", width: 90,
        editor: "number", editorParams: { min: 0, step: 1 },
        formatter: function (cell) { return formatNumber(cell.getValue()); },
        cellEdited: function (cell) {
          recalcFromSalePrice(cell.getRow());   // 판매가 → 할인율 역산 + 금액 재계산
        } },
      { title: "할인율(%)", field: "SALEPER", hozAlign: "center", width: 90,
        editor: "number", editorParams: { min: 0, max: 100, step: 0.1 },
        cellEdited: function (cell) {
          recalcFromSaleper(cell.getRow());     // 할인율 → 판매가 자동계산 + 금액 재계산
        } },
      { title: "수량", field: "QTY", hozAlign: "center", width: 70,
        editor: "number", editorParams: { min: 0, step: 1 },
        cellEdited: function (cell) {
          recalcAmtOnly(cell.getRow());         // 수량 → 금액만 재계산 (판매가 × 수량)
        } },
      { title: "금액", field: "AMT", hozAlign: "right", width: 100,
        formatter: function (cell) { return formatNumber(cell.getValue()); } },
      { title: "비고", field: "REMK", hozAlign: "left", editor: "input" },
    ]
  });
});

// ---- 거래처 검색 ----
function openCustSearch() {
  document.getElementById("custSearchName").value = "";
  document.getElementById("custSearchOwner").value = "";
  document.getElementById("custSearchHandno").value = "";
  document.getElementById("custSearchSano").value = "";
  custSearchTable.setData([]);
  custSearchModal.show();
}

function searchCustomers() {
  var name = document.getElementById("custSearchName").value.trim();
  var owner = document.getElementById("custSearchOwner").value.trim();
  var handno = document.getElementById("custSearchHandno").value.trim();
  var sano = document.getElementById("custSearchSano").value.trim();

  var params = new URLSearchParams();
  if (name) params.append("custname", name);
  if (owner) params.append("owner", owner);
  if (handno) params.append("handno", handno);
  if (sano) params.append("sano", sano);

  fetch("/ai101_cust_search.do?" + params.toString())
    .then(function (res) { return res.json(); })
    .then(function (data) { custSearchTable.setData(data); })
    .catch(function () { alert("거래처 검색 중 오류가 발생했습니다."); });
}

function selectCustomer(cust) {
  // 모달은 무조건 먼저 닫는다 (아래 처리 중 오류가 나도 닫힘 동작에는 영향 없도록)
  custSearchModal.hide();

  try {
    document.getElementById("infoCustcd").value = cust.CUSTCD || "";
    document.getElementById("infoCustname").value = cust.CUSTNAME || "";
    document.getElementById("infoOwner").value = cust.OWNER || "";
    document.getElementById("infoHandno").value = cust.HANDNO || "";
    document.getElementById("infoSano").value = cust.SANO || "";

    loadMisu(cust.CUSTCD);
    checkLogin();
  } catch (err) {
    console.error("selectCustomer 처리 중 오류:", err, cust);
  }
}

function loadMisu(custcd) {
  fetch("/ai101_misu.do?custcd=" + encodeURIComponent(custcd))
    .then(function (res) { return res.json(); })
    .then(function (data) {
      var day = data.day || {};
      var month = data.month || {};

      document.getElementById("dayEwol").textContent = formatNumber(day.EWOL);
      document.getElementById("dayMcamt").textContent = formatNumber(day.MCAMT);
      document.getElementById("dayBanpum").textContent = formatNumber(day.BANPUM);
      document.getElementById("daySkamt").textContent = formatNumber(day.SKAMT);
      document.getElementById("daySaleamt").textContent = formatNumber(day.SALEAMT);
      document.getElementById("dayJanamt").textContent = formatNumber(day.JANAMT);

      document.getElementById("monEwol").textContent = formatNumber(month.EWOL);
      document.getElementById("monMcamt").textContent = formatNumber(month.MCAMT);
      document.getElementById("monBanpum").textContent = formatNumber(month.BANPUM);
      document.getElementById("monSkamt").textContent = formatNumber(month.SKAMT);
      document.getElementById("monSaleamt").textContent = formatNumber(month.SALEAMT);
      document.getElementById("monJanamt").textContent = formatNumber(month.JANAMT);
    })
    .catch(function () { alert("미수현황 조회 중 오류가 발생했습니다."); });
}

// ---- 제품 검색 ----
function openProductSearchFor(row) {
  currentProductRow = row;
  document.getElementById("productSearchCode").value = "";
  document.getElementById("productSearchBarcode").value = "";
  document.getElementById("productSearchName").value = "";
  selectedProducts = {};
  productSearchTable.setData([]);
  productSearchModal.show();
}

// 컬럼 타이틀(제품코드/바코드/제품명) 클릭 시: 현재 선택되어 있는 행을 대상으로 검색
function openProductSearchForSelectedRow() {
  var selected = productTable.getSelectedRows();
  if (!selected || selected.length === 0) {
    alert("검색할 행을 먼저 클릭해서 선택해주세요.");
    return;
  }
  openProductSearchFor(selected[0]);
}

function searchProducts() {
  var code = document.getElementById("productSearchCode").value.trim();
  var barcode = document.getElementById("productSearchBarcode").value.trim();
  var name = document.getElementById("productSearchName").value.trim();

  var params = new URLSearchParams();
  if (code) params.append("jepmcd", code);
  if (barcode) params.append("barcode", barcode);
  if (name) params.append("jepmnm", name);

  fetch("/ai101_product_search.do?" + params.toString())
    .then(function (res) { return res.json(); })
    .then(function (data) {
      selectedProducts = {};
      productSearchTable.setData(data);
    })
    .catch(function () { alert("제품 검색 중 오류가 발생했습니다."); });
}

// 제품 검색 모달에서 선택(체크)된 제품들을 판매제품 그리드에 한꺼번에 반영한다.
async function addSelectedProducts() {
  var selected = Object.values(selectedProducts);
  if (!selected || selected.length === 0) {
    alert("추가할 제품을 먼저 선택해주세요.");
    return;
  }

  for (var i = 0; i < selected.length; i++) {
    var product = selected[i];
    var targetRow = await getEmptyProductRow();

    targetRow.update({
      JEPMNO: product.JEPMNO || "",
      JEPMCD: product.JEPMCD || "",
      BARCODE: product.BARCODE || "",
      JEPMNM: product.JEPMNM || "",
      PRICE: product.PRICE || 0,
      QTY: 1
    });

    recalcFromSaleper(targetRow);
  }

  currentProductRow = null;
  selectedProducts = {};
  productSearchModal.hide();

  // 맨 마지막 행까지 채워졌으면 빈 행을 하나 더 자동 추가
  maybeAddRow();
}

// 판매제품 그리드에서 값을 채워넣을 빈 행을 반환한다. 빈 행이 없으면 새 행을 추가한다.
async function getEmptyProductRow() {
  var rows = productTable.getRows();
  for (var i = 0; i < rows.length; i++) {
    var rd = rows[i].getData();
    if (!rd.JEPMCD || String(rd.JEPMCD).trim() === "") {
      return rows[i];
    }
  }
  return await productTable.addRow(blankProductRow());
}

function maybeAddRow() {
  var rows = productTable.getRows();
  var lastRow = rows[rows.length - 1];
  if (!lastRow) return;

  var d = lastRow.getData();
  if (d.JEPMCD && String(d.JEPMCD).trim() !== "") {
    productTable.addRow(blankProductRow());
  }
}

// ---- 조회 / 신규 / 저장 / 삭제 / 명세서 ----
function resetMisu() {
  ["dayEwol", "dayMcamt", "dayBanpum", "daySkamt", "daySaleamt", "dayJanamt",
   "monEwol", "monMcamt", "monBanpum", "monSkamt", "monSaleamt", "monJanamt"]
    .forEach(function (id) { document.getElementById(id).textContent = "0"; });
}

// 조회 버튼: 기간 + 거래처를 조건으로 판매 내역을 검색하는 모달을 연다
function handleQuery() {
  openSaleQuery();
}

// 판매조회 모달을 연다. 기간은 이번달 1일~오늘로 기본값을 채우고,
// 메인화면에 이미 선택된 거래처가 있으면 그 거래처를 기본 조건으로 채운다.
function openSaleQuery() {
  var today = new Date();
  var firstOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);

  document.getElementById("queryFromDate").value = firstOfMonth.toISOString().slice(0, 10);
  document.getElementById("queryToDate").value = today.toISOString().slice(0, 10);
  document.getElementById("queryCustcd").value = document.getElementById("infoCustcd").value || "";
  document.getElementById("queryCustname").value = document.getElementById("infoCustname").value || "";

  saleQueryListTable.setData([]);
  saleQueryDetailTable.setData([]);
  saleQueryModal.show();
}

// 판매조회 - 검색 버튼: 기간/거래처 조건으로 판매 내역 목록(왼쪽)을 조회한다
// 거래처는 검색모달로 정확히 선택했으면 custcd로, 직접 타이핑했으면(=custcd가 비어있으면)
// custname 부분일치로 검색한다.
function searchSaleQuery() {
  var fromDate = document.getElementById("queryFromDate").value;
  var toDate = document.getElementById("queryToDate").value;
  var custcd = document.getElementById("queryCustcd").value;
  var custname = document.getElementById("queryCustname").value.trim();

  var params = new URLSearchParams();
  if (fromDate) params.append("fromDate", fromDate);
  if (toDate) params.append("toDate", toDate);
  if (custcd) {
    params.append("custcd", custcd);
  } else if (custname) {
    params.append("custname", custname);
  }

  fetch("/ai101_sale_query.do?" + params.toString())
    .then(function (res) { return res.json(); })
    .then(function (data) {
      saleQueryListTable.setData(data || []);
      saleQueryDetailTable.setData([]);
    })
    .catch(function () { alert("판매 조회 중 오류가 발생했습니다."); });
}

// 판매조회 - 왼쪽 목록에서 클릭한 건의 상세내역(오른쪽)을 불러온다
function loadSaleQueryDetail(jpno) {
  fetch("/ai101_sale_detail.do?jpno=" + encodeURIComponent(jpno))
    .then(function (res) { return res.json(); })
    .then(function (data) {
      saleQueryDetailTable.setData(data.products || []);
    })
    .catch(function () { alert("상세내역 조회 중 오류가 발생했습니다."); });
}

// 판매조회 - 왼쪽 목록을 더블클릭한 건을 메인화면에 불러오고 모달을 닫는다
function loadSaleIntoMain(jpno) {
  fetch("/ai101_sale_detail.do?jpno=" + encodeURIComponent(jpno))
    .then(function (res) { return res.json(); })
    .then(function (data) {
      var header = data.header || {};
      var products = (data.products && data.products.length > 0) ? data.products : Array.from({ length: 5 }, blankProductRow);

      document.getElementById("infoCustcd").value = header.CUSTCD || "";
      document.getElementById("infoJpno").value = header.JPNO || "";
      document.getElementById("infoCustname").value = header.CUSTNAME || "";
      document.getElementById("infoOwner").value = header.OWNER || "";
      document.getElementById("infoHandno").value = header.HANDNO || "";
      document.getElementById("infoSano").value = header.SANO || "";

      if (header.CDATE) {
        var s = String(header.CDATE);
        document.getElementById("ipdate").value = s.slice(0, 4) + "-" + s.slice(4, 6) + "-" + s.slice(6, 8);
      }

      productTable.setData(products);
      loadMisu(header.CUSTCD);

      saleQueryModal.hide();
    })
    .catch(function () { alert("판매 내역을 불러오는 중 오류가 발생했습니다."); });
}

// 화면을 전부 초기화하고 새 판매 입력을 시작한다
function handleNew() {
  document.getElementById("infoCustcd").value = "";
  document.getElementById("infoJpno").value = "";
  document.getElementById("infoCustname").value = "";
  document.getElementById("infoOwner").value = "";
  document.getElementById("infoHandno").value = "";
  document.getElementById("infoSano").value = "";
  document.getElementById("ipdate").value = new Date().toISOString().slice(0, 10);

  resetMisu();
  productTable.setData(Array.from({ length: 5 }, blankProductRow));
}

// 거래처 + 판매일자 + 판매제품(빈 행 제외)을 서버에 저장한다
function handleSave() {
  var custcd = document.getElementById("infoCustcd").value;
  var jpno = document.getElementById("infoJpno").value;
  var ipdate = document.getElementById("ipdate").value;

  if (!custcd) {
    alert("거래처를 먼저 선택해주세요.");
    return;
  }

  var products = productTable.getData().filter(function (row) {
    return row.JEPMCD && String(row.JEPMCD).trim() !== "";
  });

  if (products.length === 0) {
    alert("판매제품을 한 개 이상 입력해주세요.");
    return;
  }

  var payload = {
    CUSTCD: custcd,
    JPNO: jpno,
    IPDATE: ipdate,
    products: products
  };

  fetch("/ai101_save.do", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  })
    .then(function (res) {
      if (!res.ok) return Promise.reject();
      alert("저장되었습니다.");
      handleNew();
    })
    .catch(function () { alert("저장 중 오류가 발생했습니다."); });
}

// 현재 거래처 + 판매일자에 해당하는 판매 내역을 삭제한다
function handleDelete() {
  var custcd = document.getElementById("infoCustcd").value;
  var jpno = document.getElementById("infoJpno").value;
  var ipdate = document.getElementById("ipdate").value;

  if (!jpno) {
	  alert("삭제할 판매 내역을 먼저 조회해서 불러와주세요.");
	  return;
	}
  
  if (!custcd) {
    alert("삭제할 거래처를 먼저 선택해주세요.");
    return;
  }
  if (!confirm(ipdate + " 판매 내역을 삭제하시겠습니까?")) return;

  fetch("/ai101_delete.do", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ JPNO: jpno, CUSTCD:custcd, IPDATE:ipdate })
  })
    .then(function (res) {
      if (!res.ok) return Promise.reject();
      alert("삭제되었습니다.");
      handleNew();
    })
    .catch(function () { alert("삭제 중 오류가 발생했습니다."); });
}

// 거래명세서를 새 창으로 연다
function openStatement() {
  var jpno = document.getElementById("infoJpno").value;

  if (!jpno) {
    alert("조회된 판매 내역이 없습니다. 먼저 조회해주세요.");
    return;
  }

  window.open("/ai101_statement.do?jpno=" + encodeURIComponent(jpno), "_blank");
}

// ---- 거래처 상세보기 ----
// 상세보기 모달을 연다. 열리자마자 바로 조회한다.
function openDetailView() {
  var custcd = document.getElementById("infoCustcd").value;
  if (!custcd) {
    alert("거래처를 먼저 선택해주세요.");
    return;
  }

  var custname = document.getElementById("infoCustname").value || "";
  document.getElementById("detailViewModalTitle").textContent = "거래처 상세보기 - " + custname;

  detailViewModal.show();
  // 실제 조회는 모달이 완전히 열린(shown.bs.modal) 뒤에 실행한다.
  // (그 전에 조회하면, 모달이 열리면서 테이블을 재생성하는 코드가
  //  뒤늦게 실행되어 방금 채운 데이터를 지워버리는 문제가 있었다)
}

// 상세보기 데이터를 서버에서 불러와 그리드에 채운다.
function loadDetailView(custcd) {
  var params = new URLSearchParams();
  params.append("custcd", custcd);

  fetch("/ai101_detail_view.do?" + params.toString())
    .then(function (res) { return res.json(); })
    .then(function (data) { return detailViewTable.setData(data || []); })
    .then(function () {
      // 조회 완료 후 그리드 커서(스크롤)를 맨 아래로 이동
      var rows = detailViewTable.getRows();
      if (rows.length > 0) {
        detailViewTable.scrollToRow(rows[rows.length - 1], "bottom", false);
      }
    })
    .catch(function () { alert("상세보기 조회 중 오류가 발생했습니다."); });
}

// 상세보기 - 현재 그리드 내용을 엑셀(xlsx)로 다운로드한다.
function downloadDetailViewExcel() {
  var custname = document.getElementById("infoCustname").value || "거래처";
  var filename = "상세보기_" + custname + ".xlsx";

  detailViewTable.download("xlsx", filename, { sheetName: "상세보기" });
}

// ---- SMS전송 ----
// SMS 모달을 연다. 실제 조회는 모달이 완전히 열린 뒤(shown.bs.modal)에 실행한다.
function openSms() {
  var custcd = document.getElementById("infoCustcd").value;
  if (!custcd) {
    alert("거래처를 먼저 선택해주세요.");
    return;
  }

  var custname = document.getElementById("infoCustname").value || "";
  document.getElementById("smsModalTitle").textContent = "SMS전송 - " + custname;

  document.getElementById("smsHandno").value = "";
  document.getElementById("smsRecvno").value = "";
  document.getElementById("smsMemo").value = "";

  smsModal.show();
}

// 전송 내역 + 가능회수를 서버에서 불러온다.
function loadSmsList(custcd) {
  fetch("/ai101_sms_list.do?custcd=" + encodeURIComponent(custcd))
    .then(function (res) { return res.json(); })
    .then(function (data) {
      smsTable.setData(data.list || []);
      var cnt = data.cnt || 0;
      document.getElementById("smsCntLeft").textContent = cnt;
      document.getElementById("smsCnt").value = cnt;
      document.getElementById("smsRecvno").value = data.telno || "";
    })
    .catch(function () { alert("SMS 내역 조회 중 오류가 발생했습니다."); });
}

// 전송 버튼: 입력한 내용으로 IpsDataAdd.do를 호출해 SMS를 전송한다.
function handleSmsSend() {
  var custcd = document.getElementById("infoCustcd").value;
  if (!custcd) {
    alert("거래처를 먼저 선택해주세요.");
    return;
  }

  var handno = document.getElementById("smsHandno").value.trim();
  var recvno = document.getElementById("smsRecvno").value.trim();
  var memo = document.getElementById("smsMemo").value.trim();

  if (!handno) {
    alert("핸드폰번호를 입력해주세요.");
    return;
  }
  if (!memo) {
    alert("내역을 입력해주세요.");
    return;
  }

  fetch("/IpsDataAdd.do", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ CUSTCD: custcd, HANDNO: handno, RECVNO: recvno, MEMO: memo })
  })
    .then(function (res) {
      if (!res.ok) return Promise.reject();
      alert("전송되었습니다.");
      document.getElementById("smsMemo").value = "";
      loadSmsList(custcd);
    })
    .catch(function () { alert("전송 중 오류가 발생했습니다."); });
}

// ---- 메모 ----
// 메모 모달을 연다. 실제 조회는 모달이 완전히 열린 뒤(shown.bs.modal)에 실행한다.
function openMemo() {
  var custcd = document.getElementById("infoCustcd").value;
  if (!custcd) {
    alert("거래처를 먼저 선택해주세요.");
    return;
  }

  var custname = document.getElementById("infoCustname").value || "";
  document.getElementById("memoModalTitle").textContent = "메모 - " + custname;

  memoModal.show();
}

// 메모 목록을 서버에서 불러와 그리드에 채운다.
function loadMemoList(custcd) {
  fetch("/ai101_memo_list.do?custcd=" + encodeURIComponent(custcd))
    .then(function (res) { return res.json(); })
    .then(function (data) { return memoTable.setData(data || []); })
    .then(function () {
      // 조회 완료 후 그리드 커서(스크롤)를 맨 아래(마지막 줄)로 이동
      var rows = memoTable.getRows();
      if (rows.length > 0) {
        memoTable.scrollToRow(rows[rows.length - 1], "bottom", false);
      }
    })
    .catch(function () { alert("메모 조회 중 오류가 발생했습니다."); });
}

// 메모 - 신규 버튼: 빈 행을 하나 추가한다. (등록일은 저장 시 서버에서 채운다)
function handleMemoNew() {
  var custcd = document.getElementById("infoCustcd").value;
  if (!custcd) {
    alert("거래처를 먼저 선택해주세요.");
    return;
  }

  memoTable.addRow({
    CUSTCD: custcd,
    SEQ: "",
    MEMO: "",
    PRDATE: "",
    PLANDATE: ""
  });
}

// 메모 - 저장 버튼: 그리드의 모든 행(신규/수정 포함)을 서버에 저장한다.
function handleMemoSave() {
  var custcd = document.getElementById("infoCustcd").value;
  if (!custcd) {
    alert("거래처를 먼저 선택해주세요.");
    return;
  }

  var list = memoTable.getData();

  fetch("/ai101_memo_save.do", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ CUSTCD: custcd, list: list })
  })
    .then(function (res) {
      if (!res.ok) return Promise.reject();
      alert("저장되었습니다.");
      loadMemoList(custcd);  // 새로 채번된 SEQ, 등록일을 반영하기 위해 다시 조회
    })
    .catch(function () { alert("저장 중 오류가 발생했습니다."); });
}

// 메모 - 삭제 버튼: 선택(클릭)된 행을 지운다.
// 아직 저장 안 된(SEQ가 없는) 신규 행이면 서버 호출 없이 그리드에서만 지운다.
function handleMemoDelete() {
  var selected = memoTable.getSelectedRows();
  if (!selected || selected.length === 0) {
    alert("삭제할 행을 먼저 클릭해서 선택해주세요.");
    return;
  }

  var row = selected[0];
  var d = row.getData();

  if (!d.SEQ) {
    row.delete();
    return;
  }

  if (!confirm("선택한 메모를 삭제하시겠습니까?")) return;

  var custcd = document.getElementById("infoCustcd").value;

  fetch("/ai101_memo_delete.do", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ CUSTCD: custcd, SEQ: d.SEQ })
  })
    .then(function (res) {
      if (!res.ok) return Promise.reject();
      row.delete();
    })
    .catch(function () { alert("삭제 중 오류가 발생했습니다."); });
}
</script>

</body>
</html>