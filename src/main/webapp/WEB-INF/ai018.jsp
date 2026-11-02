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
    <h4 class="fw-bold mb-3">근태현황</h4>
    <div id="attendance-root"></div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/WEB-INF/common/menu_script.jspf" %>

<!-- React / ReactDOM / Babel Standalone / Chart.js -->
<script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
<script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
<script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.4/dist/chart.umd.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/attendance_review.css">

   <script type="text/babel" src="${pageContext.request.contextPath}/resources/react-widget/attendance_review.jsx"></script>
   <script type="text/babel">
     ReactDOM.createRoot(document.getElementById('attendance-root')).render(
       <AttendanceReviewScreen
         listApi="${pageContext.request.contextPath}/api/attendance"
         equipOptions={["전체", "A라인", "B라인", "C라인"]}
       />
     );
   </script>

</body>
</html>
