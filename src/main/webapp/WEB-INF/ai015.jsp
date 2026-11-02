<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>사원 관리</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Tabulator -->
<link href="https://unpkg.com/tabulator-tables@5.6.1/dist/css/tabulator_bootstrap5.min.css" rel="stylesheet">
<script src="https://unpkg.com/tabulator-tables@5.6.1/dist/js/tabulator.min.js"></script>

<style>
<%@ include file="/WEB-INF/common/menu_style.jspf" %>


  
</style>
</head>
<body>

<%@ include file="/WEB-INF/common/menu_nav.jspf" %>

  <!-- 콘텐츠 영역: 사원 관리 -->
  <div class="flex-grow-1 p-4">
    <div class="sticky-toolbar">   <!-- 검색 / 버튼 영역고정 -->
      <div class="d-flex align-items-center mb-3">
        <h4 class="fw-bold mb-0">사원 관리</h4>
      </div>

      <!-- 검색 / 버튼 영역 -->
      <div class="card mb-3">
        <div class="card-body">
          <div class="row g-2 align-items-end">
            <div class="col-auto">
              <label class="form-label small mb-1">성명</label>
              <input type="text" id="searchName" class="form-control form-control-sm" placeholder="이름 입력">
            </div>
            <div class="col-auto">
              <button class="btn btn-sm btn-primary me-1" onclick="handleSearch()">조회</button>
              <button class="btn btn-sm btn-secondary me-1" onclick="handleReset()">초기화</button>
            </div>
            <div class="col-auto ms-auto">
              <button class="btn btn-sm btn-success me-1" onclick="openWriteModal()">신규등록</button>
              <button class="btn btn-sm btn-warning me-1" onclick="openEditModal()">수정</button>
              <button class="btn btn-sm btn-danger" onclick="deleteEmployee()">삭제</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 사원 목록 그리드 -->
    <div id="employee-table"></div>
  </div>
</div>

<!-- 사원 등록/수정 모달 -->
<div class="modal fade" id="empModal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="empModalTitle">사원 신규등록</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <div class="row g-3">
          <div class="col-6">
            <label class="form-label">사번</label>
            <input type="text" id="empCode" class="form-control" placeholder="사번 입력">
          </div>
          <div class="col-6">
            <label class="form-label">직위</label>
            <input type="text" id="empPosition" class="form-control" placeholder="직위 입력">
          </div>
          <div class="col-6">
            <label class="form-label">성명</label>
            <input type="text" id="empName" class="form-control" placeholder="성명 입력">
          </div>
          <div class="col-6">
            <label class="form-label">입사일</label>
            <input type="date" id="empIpdate" class="form-control">
          </div>
          <div class="col-6">
            <label class="form-label">전화번호</label>
            <input type="text" id="empTelno" class="form-control" placeholder="전화번호 입력">
          </div>
          <div class="col-6">
            <label class="form-label">휴대폰번호</label>
            <input type="text" id="empHandno" class="form-control" placeholder="휴대폰번호 입력">
          </div>
          <div class="col-12">
            <label class="form-label">비고</label>
            <textarea id="empRemk" class="form-control" rows="2" placeholder="비고 입력"></textarea>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
        <button type="button" class="btn btn-primary" onclick="saveEmployee()">저장</button>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/common/menu_script.jspf" %>
<script>
const API_URL = "/ai015grid.do";
let table;
let empModal;
let editMode = "new"; // "new" | "edit"

document.addEventListener("DOMContentLoaded", function () {
  empModal = new bootstrap.Modal(document.getElementById("empModal"));

  table = new Tabulator("#employee-table", {
    layout: "fitColumns",
    height: "calc(100vh - 300px)",// 그리드 헤더 고정 + 내부 스크롤
/*    pagination: true,
    paginationSize: 10,
    paginationSizeSelector: [10, 20, 50],
*/
    placeholder: "등록된 사원이 없습니다.",
    selectable: 1,
    columns: [
      { title: "직위", field: "POSITION", hozAlign: "center", width: 100 },
      { title: "사번", field: "CODE", hozAlign: "center", width: 100 },
      { title: "성명", field: "NAME", hozAlign: "left" },
      { title: "전화번호", field: "TELNO", hozAlign: "center", width: 130 },
      { title: "휴대폰번호", field: "HANDNO", hozAlign: "center", width: 130 },
      { title: "입사일", field: "IPDATE", hozAlign: "center", width: 110 },
      { title: "비고", field: "REMK", hozAlign: "left" },
      { title: "등록일", field: "PRDATE", hozAlign: "center", width: 110 },
    ]
  });

  loadData();
  checkLogin();
});

function loadData(name) {
  const params = new URLSearchParams();
  if (name) params.append("name", name);

  fetch(API_URL + "?" + params.toString())
    .then(function (res) { return res.json(); })
    .then(function (data) { table.setData(data); })
    .catch(function () { alert("사원 목록 조회 중 오류가 발생했습니다."); });
}

function handleSearch() {
  const name = document.getElementById("searchName").value.trim();
  loadData(name);
}

function handleReset() {
  document.getElementById("searchName").value = "";
  loadData();
}

function openWriteModal() {
  editMode = "new";
  document.getElementById("empModalTitle").textContent = "사원 신규등록";
  document.getElementById("empCode").value = "";
  document.getElementById("empCode").disabled = false;
  document.getElementById("empPosition").value = "";
  document.getElementById("empName").value = "";
  document.getElementById("empTelno").value = "";
  document.getElementById("empHandno").value = "";
  document.getElementById("empIpdate").value = "";
  document.getElementById("empRemk").value = "";
  empModal.show();
}

function openEditModal() {
  const selected = table.getSelectedData();
  if (selected.length === 0) {
    alert("수정할 사원을 선택해주세요.");
    return;
  }
  const row = selected[0];
  editMode = "edit";
  document.getElementById("empModalTitle").textContent = "사원 정보 수정";
  document.getElementById("empCode").value = row.CODE || "";
  document.getElementById("empCode").disabled = true;
  document.getElementById("empPosition").value = row.POSITION || "";
  document.getElementById("empName").value = row.NAME || "";
  document.getElementById("empTelno").value = row.TELNO || "";
  document.getElementById("empHandno").value = row.HANDNO || "";
  document.getElementById("empIpdate").value = row.IPDATE || "";
  document.getElementById("empRemk").value = row.REMK || "";
  empModal.show();
}

function saveEmployee() {
  const code = document.getElementById("empCode").value.trim();
  const name = document.getElementById("empName").value.trim();

  if (!code) { alert("사번을 입력해주세요."); return; }
  if (!name) { alert("성명을 입력해주세요."); return; }

  const payload = {
    mode: editMode,
    CODE: code,
    POSITION: document.getElementById("empPosition").value.trim(),
    NAME: name,
    TELNO: document.getElementById("empTelno").value.trim(),
    HANDNO: document.getElementById("empHandno").value.trim(),
    IPDATE: document.getElementById("empIpdate").value,
    REMK: document.getElementById("empRemk").value.trim()
  };

  fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  })
    .then(function (res) {
      if (!res.ok) return Promise.reject();
      empModal.hide();
      handleReset();
    })
    .catch(function () { alert("저장 중 오류가 발생했습니다."); });
}

function deleteEmployee() {
  const selected = table.getSelectedData();
  if (selected.length === 0) {
    alert("삭제할 사원을 선택해주세요.");
    return;
  }
  if (!confirm(selected.length + "건을 삭제하시겠습니까?")) return;

  const codeList = selected.map(function (row) { return row.CODE; });

  fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ mode: "delete", CODE_LIST: codeList })
  })
    .then(function (res) {
      if (!res.ok) return Promise.reject();
      handleReset();
    })
    .catch(function () { alert("삭제 중 오류가 발생했습니다."); });
}
</script>

</body>
</html>