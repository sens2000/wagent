<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>대시보드</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
<%@ include file="/WEB-INF/common/menu_style.jspf" %>
</style>
</head>
<body>

<%@ include file="/WEB-INF/common/menu_nav.jspf" %>

  <!-- 콘텐츠 영역: 대시보드 -->
  <div class="flex-grow-1 p-4">
    <h4 class="fw-bold mb-3">대시보드</h4>
    <div id="dashboard-root"></div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/common/menu_script.jspf" %>

<!-- React / ReactDOM / Babel Standalone / Chart.js -->
<script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
<script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
<script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.4/dist/chart.umd.min.js"></script>

<!-- 대시보드 위젯 본체 -->
<script type="text/babel" src="resources/react-widget/dashboard-widget.jsx"></script>

<script>
  // dashboard-widget.jsx는 Babel Standalone이 비동기로 fetch → 변환 → 실행하므로,
  // window.DashboardWidget이 생성되는 시점이 이 스크립트보다 늦을 수 있다.
  // DOMContentLoaded만으로는 순서가 보장되지 않아 준비될 때까지 짧게 재시도한다.
  function waitForDashboardWidget(retriesLeft) {
    if (window.DashboardWidget) {
      DashboardWidget.mount("dashboard-root", { apiUrl: "s_dashboardStat.do" });
      return;
    }
    if (retriesLeft <= 0) {
      console.error("DashboardWidget 로드 실패: dashboard-widget.jsx 로딩/변환을 확인하세요.");
      return;
    }
    setTimeout(function () { waitForDashboardWidget(retriesLeft - 1); }, 50);
  }

  document.addEventListener("DOMContentLoaded", function () {
    waitForDashboardWidget(40); // 최대 2초(50ms x 40회) 대기
    checkLogin();
  });
</script>

</body>
</html>
