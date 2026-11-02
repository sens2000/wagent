<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<%@ page import="java.util.*,java.sql.*,com.sens.*" %>

<c:import url="/resources/jsp/includeTop.jsp" />

<script src="/resources/js/pqgrid.js"></script>
 
   
<script>
$(function(){
	
    var colM = [
                { title: "DEL", width: 70, dataIndx: "DEL",hidden:true,align : "center",type: 'checkbox',filter: { crules: [{condition: 'range' }] }, 
                	editable: true,
					cb: {
					    all: false, //header checkbox to affect checkboxes on all pages.
					    header: true, //for header checkbox.
					    check: "Y", //check the checkbox when cell value is "YES".
					    uncheck: "N" //uncheck when "NO".
					}
                },
                { title: "CUSTCD", width: 0, hidden:true, dataIndx: "CUSTCD" ,filter: { crules: [{condition: 'contain' }] } },
                { title: "거래처명", width: 150, dataIndx: "CUSTNAME",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                { title: "대표", width: 100, dataIndx: "OWNER",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                { title: "담당자", width: 100, dataIndx: "SANAME",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                { title: "전화번호", width: 100, dataIndx: "TELNO",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                { title: "핸드폰", width: 100, dataIndx: "HANDNO",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                { title: "등록일", width: 100, dataIndx: "PRDATE",align : "center" ,filter: { crules: [{condition: 'contain' }] } }
    		];
	
	grid.title = "거래처검색";
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
						url: "/pop0001grid.do",
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

		

    $grid.on( "rowSelect", function( event, ui ) {
        console.log('rowSelect', ui);
        var code = ui.addList[0].rowData['CUSTCD'];
        var name = ui.addList[0].rowData['CUSTNAME'];
        var telno = ui.addList[0].rowData['TELNO'];
        var saname = ui.addList[0].rowData['SANAME'];
        var returnValue = {
        	    code: code,
        	    name: name,
        	    telno: telno,
        	    saname: saname
        	  };
        	  window.opener.getReturnValue(JSON.stringify(returnValue));
        	  window.close();
        	  
        	  //        console.log('aa', aa);
//		 location.href="custQueryCard.do?CODE="+code + "&" + "NAME=" + name;
   } );

	
}); 

   
</script>


<body>

<div class="container-fluid text-left "id="app">	<!-- app -->

  <div id="container" >
	<div class="alert alert-info text-left" id = "title" role="alert">
		<span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
	  	거래처검색
	</div>
  	  
	<div class="panel panel-primary" id = "panel_head">  <!-- panel_head -->
	  <div class="panel-body"  id = "panel_head1">	<!-- panel_head1 -->
		
			<form class="navbar-form navbar-left" role="search" id="FormMain" name="FormMain"  method="post" >
				<div class="container show-grid " >
				    <div class="row " >
				      				    
				      <div class="col-sm-2 " align="left">
							<input type="text" class="form-control" name = "frmCUSTNAME" id = "frmCUSTNAME" placeholder="거래처명">
				      </div>
				      <div class="col-sm-2 " align="left">
							<input type="text" class="form-control" name = "frmTELNO" id = "frmTELNO" placeholder="전화번호">
				      </div>
				      <div class="col-sm-2 " align="left">
							<input type="text" class="form-control" name = "frmMGENAME" id = "frmMGENAME" placeholder="담당자">
				      </div>
				      
				      <div class="col-sm-2 " align="left">
							<button type="button" class="btn btn-primary pull-right" id="SEARCH" name="SEARCH" >
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>검색
							</button>
				      </div>
				    </div> <!-검색-  Row End -->
				    
				    
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
