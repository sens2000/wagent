<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<%@ page import="java.util.*,java.sql.*,com.sens.*" %>

<c:import url="/resources/jsp/includeTop.jsp" />
<c:import url="/resources/jsp/erpmenu.jsp" />

<script src="/resources/js/pqgrid.js"></script>
 
   
<script>
$(function(){
	
	var colM = [
                { title: "DEL", width: 70, dataIndx: "DEL",align : "center",type: 'checkbox',filter: { crules: [{condition: 'range' }] }, 
//                	cls: 'ui-state-default',
                	editable: true,
					cb: {
					    all: false, //header checkbox to affect checkboxes on all pages.
					    header: true, //for header checkbox.
					    check: "Y", //check the checkbox when cell value is "YES".
					    uncheck: "N" //uncheck when "NO".
					}
                },
//filter,      validations          
	            { title: "매장번호", width: 100,  dataIndx: "COMCD",filter: { crules: [{condition: 'contain' }] },
                    validations: [
                                  { type: 'minLen', value: 6, msg: "MIN매장번호입력해주세요!" },
                                  { type: 'maxLen', value: 6, msg: "MAX6자리로 입력해주세요!" },                	
//                                  { type: 'nonEmpty', msg: '6자리로 입력해주세요'}
//                                  { type: 'gte', value: 1, msg: "should be >= 1"}
//									minLen, maxLen, gt, gte, lt, lte, neq,regexp, nonEmpty
                				]
                	},
//                { title: "매장명", width: 150, dataIndx: "COMNAME",align : "left" , menuInDisable: true},
                { title: "매장명", width: 150, dataIndx: "COMNAME",align : "left",filter: { crules: [{condition: 'contain' }] } },
                { title: "별명", width: 120, dataIndx: "NICKNAME",align : "center" ,filter: { crules: [{condition: 'contain' }] } },
                { title: "재고수정", width: 100, dataIndx: "JEGOEDIT",align : "center",type: 'checkbox',filter: { crules: [{condition: 'range' }] },
//                	cls: 'ui-state-default',
//체크박스
                	editable: true,
					cb: {
					    all: false, //header checkbox to affect checkboxes on all pages.
					    header: true, //for header checkbox.
					    check: "Y", //check the checkbox when cell value is "YES".
					    uncheck: "N" //uncheck when "NO".
					}
                }
    		];
	
	grid.title = "매장관리";
	grid.colModel = colM;
//에러메세지	
	grid.warning = { 		    icon: 'ui-icon-info',		    cls: 'ui-state-default'};		//에러메세지
//    grid.columnTemplate = { render: tooltipRender };
//                var ans = window.confirm("There are unsaved changes. Are you sure?");

//    grid.create = function (evt, ui) {        this.widget().pqTooltip();    };
    
     var dataModel = {
						location: "remote",
						recIndx: "COMCD",
						sorting: "local",
						dataType: "JSON",
						method: "POST",
						url: "/is0001grid.do",
						postData : {},                                    // ajax와 마찬가지로 넘겨줄 데이터
						getData: function (dataJSON) {
							return { data: dataJSON.data };
						}
			};
     
 	grid.dataModel = dataModel;			//트레이스 처리(update + addlist)
	var $grid = pq.grid( "#grid_paging", grid);

     
	$("#SEARCH").on("click",function(){
		$( "#grid_paging" ).pqGrid( "option", "dataModel", dataModel );
//		$( "#grid_paging" ).pqGrid( "option", "height", "100%" );
//			var $form = $("#FormMain");			alert($form.serialize());   &연결 문자열

			var paramData = JSON.stringify($("#FormMain").serializeObject());  //:문자열
			var paramUser = '${USER}';
			$( "#grid_paging" ).pqGrid( "option", "dataModel.postData", function( ui ){
			    return {
			    	param : paramData,
			    	user : paramUser
			    	};
			} );
		 	$("#grid_paging").pqGrid("refreshDataAndView");
//		search($( "#grid_paging" ),$("#FormMain"));
	});

		
	$("#ADD").on("click",function(){
//        addRow($grid);
//		var colNick = $( "#grid_paging" ).pqGrid( "getCell", { rowIndx: 2, dataIndx: "NICKNAME" } );  rowinx ; 0부터시작
//        console.log('colNick', colNick.text());
//		var rowD = $( "#grid_paging" ).pqGrid( "getRow", {rowIndxPage: 2} );
//        console.log('rowD', rowD.text());
//        var rowData = $( "#grid_paging" ).pqGrid( "getRowData", {rowIndxPage: 2} );
//        console.log('rowData', rowData);
//		{ type: 'regexp', value: '^[0-9]{2}/[0-9]{2}/[0-9]{4}$', msg: 'Not in mm/dd/yyyy format' }  일자포멧 check validations        
//	폼 에러메세지  https://jsfiddle.net/trixta/tLXXN/

//recIndx 항목은 제외
		var rowData = {DEL:"N",NEW:"Y", COMNAME: "", NICKNAME : "",JEGOEDIT : "Y"}; 
		var rowIndx = $("#grid_paging").pqGrid( "addRow", {rowData: rowData, checkEditable: true});

		$( "#grid_paging" ).pqGrid( "goToPage", { rowIndx: rowIndx } );
		$( "#grid_paging" ).pqGrid("editFirstCellInRow", { rowIndx: rowIndx });
	});
		
	$("#SAVE").on("click",function(){
		var gridChanges = $("#grid_paging").pqGrid("getChanges", { format: "byVal" });
//		     alert(JSON.stringify(gridChanges));
//        $.ajax($.extend({}, ajaxObj, {
		$.ajax({
		    dataType: "json",
		    type: "POST",
		    async: true,
		    beforeSend: function (jqXHR, settings) {
		 	   $("#grid_paging").pqGrid("showLoading");
		    },
		    url: "/is0001save.do", //for ASP.NET, java    
		    data: {
		        list: JSON.stringify(gridChanges)
		    },
		    success: function (changes) {
		        $("#grid_paging").pqGrid("refreshDataAndView"); //reload fresh page data from server.
//				search($( "#grid_paging" ),$("#FormMain"));
//		    	$( "#grid_paging").pqGrid( "history", { method: 'reset' } );
		    },
		    complete: function () {
		 	   $("#grid_paging").pqGrid("hideLoading");
		    }
		});
	});

	 /*    
    grid.on( "rowSelect", function( event, ui ) {
        console.log('rowSelect', ui);
        var code = ui.addList[0].rowData['CODE'];
        var name = ui.addList[0].rowData['NAME'];
//        console.log('aa', aa);
		 location.href="custQueryCard.do?CODE="+code + "&" + "NAME=" + name;
   } );
*/

	
}); 

   
</script>


<body>

<div class="container-fluid text-left "id="app">	<!-- app -->

  <div id="container" >
	<div class="alert alert-info text-left" id = "title" role="alert">
		<span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
	  	매장 관리
	</div>
  	  
	<div class="panel panel-primary" id = "panel_head">  <!-- panel_head -->
	  <div class="panel-body"  id = "panel_head1">	<!-- panel_head1 -->
		
			<form class="navbar-form navbar-left" role="search" id="FormMain" name="FormMain"  method="post" >
				<div class="container show-grid " >
				    <div class="row " >
				      <div class="col-xs-10 col-sm-7 col-lg-4" align="left">
							<span class="label label-default">매장명</span>			
							<input type="text" class="form-control" name = "frmCUSTNAME" id = "frmCUSTNAME" placeholder="매장명">
				      </div>
				      
				      <div class="col-xs-3 col-sm-4 col-lg-2">
							<input type="checkbox" id="frmCHECK" name = "frmCHECK" class="custom-control-input" checked="on">
							<label class="custom-control-label" for="jb-checkbox">사용</label>
				      </div>
				      
				      <div class="col-xs-2 col-sm-5 col-lg-2 navbar-right" >
							<button type="button" class="btn btn-primary pull-right" id="SEARCH" name="SEARCH" >
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>검색
							</button>
				      </div>
				    </div> <!-검색-  Row End -->

				    
				    <div class="container-fluid">
				     <div class="row-fluid navbar-right">
				      <div class="col" style="border:3px solid #fff;">
				      	<div class="btn-toolbar navbar-right" >
							<button type="button" class="btn btn-default btn-sm" id="ADD" name="ADD" style="width:65px;" onclick="datasend()">신규</button>
							<button type="button" class="btn btn-default btn-sm" id="SAVE" name="SAVE" style="width:65px;" onclick="datasend()">저장</button>
				      	</div> <!--  btn-toolbar End -->
				      </div> <!--  col End -->
				    </div> <!--  row-fluid End -->
				   </div> <!-- 버튼 container-fluid End -->
				    
				    
				    
				</div> <!-- container show-grid End -->
			</form>
		</div>
	</div>  <!-- panel-primary End -->
  </div>  <!-- container-fluid End -->
	   

	<div id="grid_paging" style="margin:3px;padding:1px;"></div>

	<footer class="container-fluid id="footer">
	  Footer Text
	</footer>
	
</div>
    


</body>
</html>
