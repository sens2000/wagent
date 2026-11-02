<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>로그인</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
  body {
    background: #f1f3f5;
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .login-card { width: 100%; max-width: 380px; }
</style>
</head>
<body>

<div class="card shadow-sm login-card">
  <div class="card-body p-4">
    <div class="text-center mb-4">
      <h4 class="fw-bold">로그인</h4>
      <p class="text-muted small mb-0">아이디와 암호를 입력해주세요.</p>
    </div>

    <%-- 컨트롤러(loginOk.do)에서 로그인 실패 시 model.addAttribute("msg", ...)로 내려준 메시지 --%>
    <c:if test="${not empty msg}">
      <div class="alert alert-danger py-2 small">${msg}</div>
    </c:if>

    <%-- action: loginOk.do 로 직접 POST, 파라미터명은 컨트롤러가 받는 ID / PASS 그대로 사용 --%>
    <form id="loginForm" action="s_loginOk.do" method="post">
      <div class="mb-3">
        <label class="form-label">아이디</label>
        <input type="text" name="ID" class="form-control" placeholder="아이디 입력"
               autocomplete="username" required>
      </div>
      <div class="mb-3">
        <label class="form-label">암호</label>
        <input type="password" name="PASS" class="form-control" placeholder="암호 입력"
               autocomplete="current-password" required>
      </div>
      <div class="form-check mb-3">
        <input type="checkbox" class="form-check-input" id="rememberMe" name="rememberMe">
        <label class="form-check-label small" for="rememberMe">로그인 상태 유지</label>
      </div>
      <button type="submit" class="btn btn-primary w-100">로그인</button>
    </form>
  </div>
</div>

</body>
</html>