<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>메인 - 게시판</title>
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

  <!-- 콘텐츠 영역: 게시판 -->
  <div class="flex-grow-1 p-4">
    <div class="d-flex align-items-center mb-3">
      <h4 class="fw-bold mb-0">게시판</h4>
      <button class="btn btn-sm btn-success ms-auto" onclick="openWriteModal()">+ 글쓰기</button>
    </div>

    <!-- 검색 영역 -->
    <div class="card mb-3">
      <div class="card-body">
        <div class="row g-2 align-items-end">
          <div class="col-auto">
            <label class="form-label small mb-1">제목</label>
            <input type="text" id="searchTitle" class="form-control form-control-sm" placeholder="제목 입력">
          </div>
          <div class="col-auto">
            <button class="btn btn-sm btn-primary me-1" onclick="handleSearch()">조회</button>
            <button class="btn btn-sm btn-secondary" onclick="handleReset()">초기화</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 게시글 목록 -->
    <div id="board-table"></div>
  </div>
</div>

<!-- 게시글 상세/작성 모달 -->
<div class="modal fade" id="postModal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="postModalTitle">글쓰기</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <div class="mb-3">
          <label class="form-label">제목</label>
          <input type="text" id="postTitle" class="form-control" placeholder="제목을 입력하세요">
        </div>
        <div class="mb-3">
          <label class="form-label">내용</label>
          <textarea id="postContent" class="form-control" rows="6" placeholder="내용을 입력하세요"></textarea>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
        <button type="button" class="btn btn-primary" onclick="savePost()">등록</button>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/common/menu_script.jspf" %>
<script>
const API_URL = "/s_boards.do";
let table;
let postModal;

document.addEventListener("DOMContentLoaded", function () {
  postModal = new bootstrap.Modal(document.getElementById("postModal"));

  table = new Tabulator("#board-table", {
    layout: "fitColumns",
    pagination: true,
    paginationSize: 10,
    paginationSizeSelector: [10, 20, 50],
    placeholder: "등록된 게시글이 없습니다.",
    columns: [
      { title: "번호", formatter: "rownum", hozAlign: "center", width: 80 },
      {
        title: "메모",
        field: "MEMO",
        hozAlign: "left",
        formatter: function (cell) {
          return '<a href="#" class="post-title-link text-decoration-none">' + cell.getValue() + '</a>';
        },
        cellClick: function (e, cell) {
          if (e.target.classList.contains("post-title-link")) {
            openViewModal(cell.getRow().getData());
          }
        }
      },
      { title: "작성자", field: "SABUNNM", width: 400, hozAlign: "left" },
      { title: "예정일", field: "PLANDATE", width: 400, hozAlign: "left" },
      { title: "작성일", field: "PRDATE", width: 140, hozAlign: "center" },
    ]
  });

  loadData();
  checkLogin();
});

function loadData(title) {
  const params = new URLSearchParams();
  if (title) params.append("title", title);

  fetch(API_URL + "?" + params.toString())
    .then(function (res) { return res.json(); })
    .then(function (data) { table.setData(data); })
    .catch(function () { alert("게시글 목록 조회 중 오류가 발생했습니다."); });
}

function handleSearch() {
  const title = document.getElementById("searchTitle").value.trim();
  loadData(title);
}

function handleReset() {
  document.getElementById("searchTitle").value = "";
  loadData();
}

function openWriteModal() {
  document.getElementById("postModalTitle").textContent = "글쓰기";
  document.getElementById("postTitle").value = "";
  document.getElementById("postTitle").disabled = false;
  document.getElementById("postContent").value = "";
  document.getElementById("postContent").disabled = false;
  postModal.show();
}

function openViewModal(post) {
  document.getElementById("postModalTitle").textContent = post.title;
  document.getElementById("postTitle").value = post.title;
  document.getElementById("postTitle").disabled = true;
  document.getElementById("postContent").value = post.content || "";
  document.getElementById("postContent").disabled = true;
  postModal.show();
}

function savePost() {
  const title = document.getElementById("postTitle").value.trim();
  const content = document.getElementById("postContent").value.trim();

  if (!title) {
    alert("제목을 입력해주세요.");
    return;
  }

  fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ title: title, content: content })
  })
    .then(function (res) {
      if (!res.ok) return Promise.reject();
      postModal.hide();
      handleReset();
    })
    .catch(function () { alert("등록 중 오류가 발생했습니다."); });
}

// 로그인 상태 확인
/*
fetch("/s_me.do")
  .then(function (res) {
    if (!res.ok) throw new Error("not logged in");
    return res.json();
  })
  .then(function (member) {
    document.getElementById("userLabel").textContent = (member.name || member.username) + "님";
  })
  .catch(function () {
    window.location.href = "/sens.jsp";
  });


*/
</script>

</body>
</html>