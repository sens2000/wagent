<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<%@ page import="java.util.*,java.sql.*,com.sens.*" %>

<c:import url="/resources/jsp/includeTop.jsp" />
<c:import url="/resources/jsp/erpmenu.jsp" />

<link href="/resources/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="/resources/pqgrid/pqgrid.min.css" />
<link rel="stylesheet" href="/resources/pqgrid/pqgrid.ui.min.css" />
<link rel="stylesheet" href="/resources/pqgrid/themes/office/pqgrid.css" />
    <link rel="stylesheet" href="/resources/pqgrid/pqgrid.ui.dev.css" />

	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

    <script type="text/javascript" src="/resources/pqgrid/pqgrid.min.js"></script>
    <script src="/resources/pqgrid/localize/pq-localize-kr.js"></script>
    <script type="text/javascript" src="/resources/pqgrid/jsZip-2.5.0/jszip.min.js"></script>
<!--     
    <link rel="stylesheet" href="/resources/pqgrid/themes/bootstrap/pqgrid.css" />
 -->    
 
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
	            { title: "매장번호", width: 100,  dataIndx: "COMCD",filter: { crules: [{condition: 'contain' }] } },
//                { title: "매장명", width: 150, dataIndx: "COMNAME",align : "left" , menuInDisable: true},
                { title: "매장명", width: 150, dataIndx: "COMNAME",align : "left",filter: { crules: [{condition: 'contain' }] } },
                { title: "별명", width: 120, dataIndx: "NICKNAME",align : "center" ,filter: { crules: [{condition: 'contain' }] } },
                { title: "재고수정", width: 100, dataIndx: "JEGOEDIT",align : "center",type: 'checkbox',filter: { crules: [{condition: 'range' }] },
//                	cls: 'ui-state-default',
                	editable: true,
					cb: {
					    all: false, //header checkbox to affect checkboxes on all pages.
					    header: true, //for header checkbox.
					    check: "Y", //check the checkbox when cell value is "YES".
					    uncheck: "N" //uncheck when "NO".
					}
                }
    		];
	
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

     var grid =  {
//			flexwidth : true,
			height : "100%",
			width : "100%",
//         flexHeight : true,
                collapsible: true,
                bootstrap : { on: true },
//                filterModel: { header: true, type: 'local', menuIcon: true },
//                dataModel: dataModel,
                colModel: colM,
                columnBorders: true,
                rowBorders: true,
                wrap: true, 
                hwrap: false,
                freezeCols: 0,
                freezeRows: 0,   
                filterModel: { 
                    on: true, 
                    mode: "AND", 
                    header: true, 
//                    menuIcon: true //show filter row icon initially.
                },
//                menuIcon: true,
//                menuUI:{                    tabs: ['hideCols']                },                
                                //move paging toolbar to top in create event callback.

                numberCell: { show: false },
            pageModel: { type: "" },
            selectionModel : { type: 'row' , mode: 'range' },
//            showBottom: true,
            trackModel: { on: true },
            title: "매장관리",
            showTitle : false,
          
//            scrollModel: { autoFit: true },            
//            scrollModel: { pace: 'fast', autoFit: false, theme: true },       
            
            validation: { icon: 'ui-icon-info' },
//            swipeModel: { on: false },
            editModel: { saveKey: $.ui.keyCode.ENTER },
            editor: { select: true  },                
            sortModel: {ignoreCase: true},

            
            resizable: true
            };
     
     var $grid = pq.grid( "#grid_paging", grid);

     
		$("#SEARCH").on("click",function(){
		     
//		     $("#grid_paging").pqGrid( "option" , "dataModel.postData",[] );//
//		     $("#grid_paging").pqGrid( "option" , "dataModel:dataModel" );
//			$( "#grid_paging" ).pqScrollBar( "option", "bootstrap", { on: true } );
			$( "#grid_paging" ).pqGrid( "option", "dataModel", dataModel );
			$( "#grid_paging" ).pqGrid( "option", "height", "100%" );
//			var $form = $("#FormMain");			alert($form.serialize());   &연결 문자열
			
			var paramData = JSON.stringify($("#FormMain").serializeObject());  //:문자열
			var paramUser = '${USER}';
//	alert (paramData);
//			var  custname = $("#frmCUSTNAME").val();	
//			var  usecheck = "off";	
//			if($("#frmCheckbox").is(":checked") == true){
//				var  usecheck = "on";
//			}
			$( "#grid_paging" ).pqGrid( "option", "dataModel.postData", function( ui ){
			    return {
			    	param : paramData,
			    	user : paramUser
			    	};
			} );

			$("#grid_paging").pqGrid("refreshDataAndView");
		     
		});

		
		$("#ADD").on("click",function(){
            var rowData = { COMCD: "", COMNAME: "", NICKNAME : "",JEGOEDIT : "Y", DEL : "N" }; //empty row template
            var rowIndx = $("#grid_paging").pqGrid( "addRow", {rowData: rowData, checkEditable: true});
            $( "#grid_paging" ).pqGrid( "goToPage", { rowIndx: rowIndx } );
            $( "#grid_paging" ).pqGrid("editFirstCellInRow", { rowIndx: rowIndx });

//            editRow(rowIndx, $("#grid_paging") );
		});
		
		$("#SAVE").on("click",function(){
		     var gridChanges = $("#grid_paging").pqGrid("getChanges", { format: "byVal" });
//		     alert(JSON.stringify(gridChanges));
//		     return false;
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
	                   //debugger;
//	                   grid.commit({ type: 'add', rows: changes.addList });
//	                   grid.commit({ type: 'update', rows: changes.updateList });
//	                   grid.commit({ type: 'delete', rows: changes.deleteList });

//	                   grid.history({ method: 'reset' });
	                   $("#grid_paging").pqGrid("refreshDataAndView"); //reload fresh page data from server.
	               },
	               complete: function () {
	            	   $("#grid_paging").pqGrid("hideLoading");
	               }
	           });
		});
		
     
}); 

function editRow(rowIndx, $grid ) {
    $grid.pqGrid("addClass", { rowIndx: rowIndx, cls: 'pq-row-edit' });

    $grid.pqGrid( "refreshRow", {rowIndx:rowIndx} );

//    $grid.pqGrid("editFirstCellInRow", { rowIndx: rowIndx });
    
}


$.fn.serializeObject = function()
{
   var o = {};
   var a = this.serializeArray();
   $.each(a, function() {
       if (o[this.name]) {
           if (!o[this.name].push) {
               o[this.name] = [o[this.name]];
           }
           o[this.name].push(this.value || '');
       } else {
           o[this.name] = this.value || '';
       }
   });
   return o;
};    
</script>


<body>

<div class="container-fluid text-left "id="app">

  <div id="container" >
	<div class="alert alert-info text-left" id = "title" role="alert">
		<span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
	  	매장 관리
	</div>
  	  
	<div class="panel panel-primary" id = "panel_head">
	  <div class="panel-body"  id = "panel_head1">
		
			<form class="navbar-form navbar-left" role="search" id="FormMain" name="FormMain"  method="post" >
				<div class="container show-grid " >
				    <div class="row " >
				      <div class="col-xs-10 col-sm-7 col-lg-4" align="left">
							<span class="label label-default">매장명</span>			
							<input type="text" class="form-control" name = "frmCUSTNAME" id = "frmCUSTNAME" placeholder="매장명">
				      </div>
				      
				      <div class="col-xs-3 col-sm-4 col-lg-2">
							<input type="checkbox" id="frmCheckbox" name = "frmCheckbox" class="custom-control-input" checked="on">
							<label class="custom-control-label" for="jb-checkbox">사용</label>
				      </div>
				      
				      <div class="col-xs-2 col-sm-5 col-lg-2 navbar-right" >
							<button type="button" class="btn btn-primary pull-right" id="SEARCH" name="SEARCH" >
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>검색
							</button>
				      </div>
				    </div> <!--  Row End -->
				    
				    <div class="container-fluid">
				     <div class="row-fluid navbar-right">
				      <div class="col" style="border:3px solid #fff;">
				      	<div class="btn-toolbar navbar-right" >
							<button type="button" class="btn btn-default btn-sm" id="ADD" name="ADD" style="width:80px;" onclick="datasend()">신규</button>
							<button type="button" class="btn btn-default btn-sm" id="SAVE" name="SAVE" style="width:80px;" onclick="datasend()">저장</button>
				      	</div>
				      </div>
				    </div> <!--  Row End -->
				    </div>
				</div> <!-- container show-grid End -->
			</form>
		</div>
	</div>  <!-- panel-primary End -->
  </div>  <!-- container-fluid End -->
	   

	<div id="grid_paging" id="contents" ></div>
	
</div>
    

<!-- 
	<footer class="container-fluid text-center">
	  <p>Footer Text</p>
	</footer>
 -->
 <div id="popup-dialog-crud" style="display:none;">
<form id="crud-form">
    <table align="center">
    <tbody><tr>
    <td>Company Name:</td>
    <td><input type="text" name="company"></td>
    </tr>
    <tr>
    <td>Symbol:</td>
    <td><input type="text" name="symbol"></td>
    </tr>
    <tr>
    <td>Price:</td>
    <td><input type="text" name="price"></td>
    </tr>
    <tr>
    <td>Change:</td>
    <td><input type="text" name="change"></td>
    </tr>
    <tr>
    <td>%Change:</td>
    <td><input type="text" name="pchange"></td>
    </tr>
    <tr>
    <td>Volume:</td>
    <td><input type="text" name="volume"></td>
    </tr>
    </tbody></table>
</form>
</div>

</body>
</html>
