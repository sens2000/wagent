<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<%@ page import="java.util.*,java.sql.*,com.sens.*" %>


<c:import url="/resources/jsp/includeTop.jsp" />
<c:import url="/resources/jsp/m_erpmenu.jsp" />



<script>
$(function(){
    var colM = [
                { title: "NEWS_KEY", width: 0, hidden:true, dataIndx: "NEWS_KEY" },
                { title: "제목", width: 150, dataIndx: "TITLE",align : "left" },
                { title: "내용", width: 300, dataIndx: "CONTENT",align : "left" },
                { title: "등록일", width: 70, dataIndx: "NEWS_DATE",align : "center" },
                { title: "만료일", width: 70, dataIndx: "END_DATE",align : "center" },
                { title: "첨부", width: 150, dataIndx: "FILENAME",align : "center" }
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
                 { title: "일자", width: 110, dataIndx: "CDATE_FMT",align : "center" },
                 { title: "금액", width: 130, dataIndx: "AMT",align : "right",format:"#,###",
                	 summary: { type: "sum" } },
//                	 summary:{type:["sum", "sum"],title:["Total: {0}", "Sub Total: {0}"]}},
                 { title: "현금", width: 130, dataIndx: "CASHAMT",align : "right" ,format:"#,###",summary: { type: "sum" } },
                 { title: "카드", width: 130, dataIndx: "CARDAMT",align : "right" ,format:"#,###",summary: { type: "sum" } },
                 { title: "방문수", width: 80, dataIndx: "VISITCNT",align : "right" ,format:"#,###",summary: { type: "sum" } },
                 { title: "객단가", width: 90, dataIndx: "AVGAMT",align : "right" ,format:"#,###" }
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
                          { CDATE_FMT:'누계', summaryRow: true, pq_fn:{AMT:'sum(C:C)', CASHAMT:'sum(D:D)', CARDAMT:'sum(E:E)', VISITCNT:'sum(F:F)' }},
                          { CDATE_FMT:'평균', summaryRow: true, pq_fn:{AMT:'average(C:C)', CASHAMT:'average(D:D)', CARDAMT:'average(E:E)', VISITCNT:'average(F:F)', AVGAMT:'average(G:G)'  }}            
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
	.td-hidden { display: none; }   
	.td-cont {word-break: break-all;}
	.table {font-size : 8px;}
	
</style>
    
<body>
	<div class="alert alert-info text-left" id = "title" role="alert">
		<span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
	  	공지사항
	</div>
       <div class="container-fluid">
               <table class="table table-bordered table-striped" id="dataTable" cellspacing="0" >
                 <thead>
                   <tr>
                     <th class="td-hidden">NEWS_KEY</th>
                     <th style="width:30%">제목</th>
                     <th style="width:20%">등록일</th>
                     <th style="width:50%">내용</th>
                   </tr>
                 </thead>
                 <tbody>
<c:forEach var="item" items="${data.news}">
                   <tr>
                     <td class="td-hidden">${item.NEWS_KEY}</td>
                     <td>${item.TITLE}</td>
                     <td>${item.NEWS_DATE}</td>
                     <td class="td-cont" >${item.CONTENT}</td>
                   </tr>
</c:forEach> 
                 </tbody>
               </table>
       </div>
       <!-- /.container-fluid -->

	<div class="alert alert-info text-left" id = "title" role="alert">
		<span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
	  	매출내역
	</div>

      <div class="container-fluid">
              <table class="table table-bordered table-striped" id="dataTable" cellspacing="0">
                <thead>
                  <tr>
                    <th class="td-hidden">CDATE</th>
                    <th align="center" style="width:35%">일자</th>
                    <th align="center" style="width:25%">금액</th>
                    <th align="center" style="width:15%">방문</th>
                    <th align="center" style="width:15%">객단가</th>
                  </tr>
                </thead>
                <tbody>
<c:set var = "totamt" value = "0" />
<c:set var = "totcash" value = "0" />
<c:set var = "totcard" value = "0" />
<c:set var = "totvisit" value = "0" />
<c:set var = "avgamt" value = "0" />
                
<c:forEach var="item" items="${data.day}">
                  <tr>
                    <td class="td-hidden">${item.CDATE}</td>
                    <td align="center">${item.CDATE_FMT}</td>
                    <td align="right">${item.AMT}</td>
                    <td align="right">${item.VISITCNT}</td>
                    <td align="right">${item.AVGAMT}</td>
                  </tr>
	<c:set var= "totamt" value="${totamt + item.AMT}"/>
	<c:set var= "totcash" value="${totcash + item.CASHAMT}"/>
	<c:set var= "totcard" value="${totcard + item.CARDAMT}"/>
	<c:set var= "totvisit" value="${totvisit + item.VISITCNT}"/>
</c:forEach> 
                    <tr style = "background-color: lightgray;">
	                    <td class="td-hidden">CDATE</th>
	                    <td align="center">합계</th>
	                    <td align="right"> <fmt:formatNumber value="${totamt}" /> </th>
	                    <td align="right"> <fmt:formatNumber value="${totvisit}" /></th>
	                    <td align="right"> <fmt:formatNumber value="${totamt / totvisit}" /></th>
                    </tr>
                </tbody>
              </table>
      </div>
      <!-- /.container-fluid -->
<!-- 
<footer class="container-fluid text-center">
  <p>Footer Text</p>
</footer>
 -->

</body>
</html>
