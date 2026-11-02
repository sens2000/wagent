<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker3.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker3.standalone.min.css">
</head>

<style>
#datepicker{z-index:999 !important;}
</style>

<script>
function today(){
    var d = new Date();
    var curr_date = d.getDate();
    var curr_month = d.getMonth() + 1;
    var curr_year = d.getFullYear();
    document.write(curr_date + "-" + curr_month + "-" + curr_year);
}
</script>
<body>
	<div>
		<label>날짜</label> <input type="text" id="datePicker" value ="today();">
	</div>
	<button type="button" id="click-btn">날짜 값 확인용</button>
	
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js">
		
	</script> <script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/locales/bootstrap-datepicker.kr.min.js">
		
	</script> <script type="text/javascript">
	    var date = new Date();
	
	    // subtract a day
	    date.setDate(date.getDate() - 1);
	    

	    
		$('#datePicker').datepicker({
			format : "yyyy-mm-dd", // 달력에서 클릭시 표시할 값 형식
			todayBtn: true ,
			minDate:(new Date()).toDateString(),
			useCurrent: true,
			clearBtn: false,
            calendarWeeks: false,
            todayHighlight: true,
            autoclose: true,
			uiLibrary: 'bootstrap',
			footer: true, modal: true,
			  inline: true,
			  autoSize: true,
			  nextText: '&rarr;',
			  prevText: '&larr;',
			  showOtherMonths: true,
			  showOn: "button",
			  buttonImage: "/images/calendar.svg",			
			startDate : '-3d',
			language : "kr" // 언어(js 추가가 필요하다.)
		}).datepicker("setDate", "today");

		$('#click-btn').on('click', function() {
			var cdate = $('#datePicker').val();
			console.log(cdate);
			alert(cdate);
		});
	</script>
</body>
</html>