<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<%@ page import="java.util.*,java.sql.*,com.sens.*" %>


<c:import url="/resources/jsp/includeTop.jsp" />
<c:import url="/resources/jsp/erpmenu.jsp" />



<script>
$(function(){
    var colM = [
                { title: "NEWS_KEY", width: 0, hidden:true, dataIndx: "CUSTCD,SEQ" },
                { title: "거래처명", width: 150, dataIndx: "CUSTNAME",align : "left" },
                { title: "전화번호", width: 150, dataIndx: "TELNO",align : "left" },
                { title: "메모", width: 500, dataIndx: "MEMO",align : "left" },
                { title: "일자", width: 120, dataIndx: "PRDATE",align : "center" },
                { title: "예정일", width: 120, dataIndx: "PLANDATE",align : "center" },
                { title: "상태", width: 50, dataIndx: "STAT",align : "center" }
    		];
     var dataModel = {
         location: "remote",
         sorting: "local",
         recIndx: "CUSTCD",
         dataType: "JSON",
         method: "POST",
         url: "/gridGroupNews.do",
       	postData : {},                                    // ajax와 마찬가지로 넘겨줄 데이터
         getData: function (dataJSON) {
             return { data: dataJSON.data };
         }
     };

     var grid =  {
//				flexwidth : true,
//                flexHeight : true,
//				virtualX : true,
//				virtualY : true,
				height : "100%",
				width : "100%",
                collapsible: true,
                bootstrap : { on: true },
                dataModel:  dataModel,
                colModel: colM,
                columnBorders: true,
                rowBorders: true,
                wrap: true, 
                hwrap: true,
                freezeCols: 0,
                freezeRows: 0,                        
                                //move paging toolbar to top in create event callback.

                numberCell: { show: false },
                scrollModel: {autoFit: true},  //넓이 꽉차게
//            pageModel: { type: "local", rPP: 10 },  //그리드 개수 상태
//            pageModel: { type: 'local', rPP: 20, rPPOptions: [1, 10, 20, 30, 40, 50, 100] },
//            scrollModel: {lastColumn: null},

            pageModel: { type: "" },
            selectionModel : { type: 'row' , mode: 'range' },
            trackModel: { on: true },
//            showBottom: true,
            showTitle : false,
            showHeader : true,
            title: "공지사항",
            resizable: true,
            };
     pq.grid( "#grid_paging", grid);


     var colM1 = [
                 { title: "CDATE", width: 0, hidden:true, dataIndx: "CDATE" },
                 { title: "일자", width: 110, dataIndx: "CDATE",align : "center" },
                 { title: "금액", width: 130, dataIndx: "AMT",align : "right",format:"#,###",	 summary: { type: "sum" } },
//                	 summary:{type:["sum", "sum"],title:["Total: {0}", "Sub Total: {0}"]}},
                 { title: "반품", width: 130, dataIndx: "BANPUM",align : "right" ,format:"#,###",summary: { type: "sum" } },
                 { title: "현금", width: 130, dataIndx: "SKAMT",align : "right" ,format:"#,###",summary: { type: "sum" } },
                 { title: "할인", width: 130, dataIndx: "SALEAMT",align : "right" ,format:"#,###",summary: { type: "sum" } },
                 { title: "매입", width: 130, dataIndx: "INAMT",align : "right" ,format:"#,###",summary: { type: "sum" } },
                 { title: "결재", width: 130, dataIndx: "KJAMT",align : "right" ,format:"#,###",summary: { type: "sum" } }
     		];
      var dataModel1 = {
          location: "remote",
          sorting: "local",
          recIndx: "CUSTCD",
          dataType: "JSON",
          method: "POST",
          url: "/gridComDay.do",
        	postData : {},                                    // ajax와 마찬가지로 넘겨줄 데이터
          getData: function (dataJSON) {
              return { data: dataJSON.data };
          }
      };
      var groupModel = {
              on: true,
//              dataIndx: ['CDATE'],
              collapsed: [false, false],
//              merge: true,
//              showSummary: [true, true],
              grandSummary: true,
              title: [  ]
          };
      var grid1 =  {
// 				flexwidth : true,
//              flexHeight : true,
// 				virtualX : true,
// 				virtualY : true,
 				height : "100%",
 				width : "100%",
                 collapsible: true,
                 bootstrap : { on: true },
                 dataModel:  dataModel1,
                 colModel: colM1,
                 columnBorders: true,
                 rowBorders: true,
                 wrap: true, 
                 hwrap: true,
                 freezeCols: 0,
                 freezeRows: 0,                        
                                 //move paging toolbar to top in create event callback.

                 numberCell: { show: false },
//     			scrollModel: { pace: 'fast', autoFit: false, theme: true },       
                 scrollModel: {autoFit: true},  //넓이 꽉차게
//             pageModel: { type: "local", rPP: 10 },  //그리드 개수 상태
//             pageModel: { type: 'local', rPP: 20, rPPOptions: [1, 10, 20, 30, 40, 50, 100] },
//             scrollModel: {lastColumn: null},

//            groupModel: groupModel,
            summaryData: [
                          { CDATE_FMT:'누계', summaryRow: true, pq_fn:{AMT:'sum(C:C)', BANPUM:'sum(D:D)', SKAMT:'sum(E:E)', SALEAMT:'sum(F:F)', INAMT:'sum(G:G)', KJAMT:'sum(H:H)' }},
                          { CDATE_FMT:'평균', summaryRow: true, pq_fn:{AMT:'average(C:C)', BANPUM:'average(D:D)', SKAMT:'average(E:E)', SALEAMT:'average(F:F)', INAMT:'average(G:G)', KJAMT:'average(H:H)'  }}            
                      ],                
             pageModel: { type: "" },
             selectionModel : { type: 'row' , mode: 'range' },
             trackModel: { on: true },
//             showBottom: true,
             showTitle : false,
             showHeader : true,
             title: "매출내역",
             resizable: true,
             };
     pq.grid( "#grid_mc", grid1);
}); 


</script>

<style>
	#mainrow {
	  height: 93%;
	  display: flex;
	}
</style>
    
<body>
<div class="container-fluid text-center" id = "app">
	<div class = "row" id = "mainrow">
		<div class = "col-sm-6" id = "maincol1">
			<div class="alert alert-info text-left" id = "title" role="alert">
				<span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
			  	공지사항
			</div>
							<div id="grid_paging" ></div>
		</div>

		<div class = "col-sm-6" id = "maincol2">
			<div class="alert alert-success text-left" id = "title" role="alert">
				<span class="glyphicon glyphicon-new-window" aria-hidden="true"></span>
			  	매출내역
			</div>
						<div id="grid_mc" ></div>
			
		</div>

	</div>
	
</div>  
<!-- 
<footer class="container-fluid text-center">
  <p>Footer Text</p>
</footer>
 -->

</body>
</html>
