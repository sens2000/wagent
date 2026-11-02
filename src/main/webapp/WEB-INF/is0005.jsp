<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<%@ page import="java.util.*,java.sql.*,com.sens.*" %>

<c:import url="/resources/jsp/includeTop.jsp" />
<c:import url="/resources/jsp/erpmenu.jsp" />

<script src="/resources/js/pqgrid.js"></script>
 
   
<script>
$(function(){
	var srvBrand = JSON.parse('${BRAND}');
	var srvKind = JSON.parse('${KIND}');

	//drop down    
    function autoCompleteEditor( source ) {
        return function (ui) {
            ui.$cell.addClass('ui-front');//so that dropdown remains with input.            

            //initialize the editor
            ui.$editor.autocomplete({
                //appendTo: ui.$cell, //for grid in maximized state.
                source: source,
                position: {
                    collision: 'flipfit',
                    within: ui.$editor.closest(".pq-grid")
                },
                selectItem: { on: true }, //custom option
                highlightText: { on: true }, //custom option
                minLength: 0
            }).focus(function () {
                //open the autocomplete upon focus                
                $(this).autocomplete("search", "");
            });
        }
    }
	
    var colM = [
                { title: "DEL", width: 70, dataIndx: "DEL",align : "center",type: 'checkbox',filter: { crules: [{condition: 'range' }] }, 
                	editable: true,
					cb: {
					    all: false, //header checkbox to affect checkboxes on all pages.
					    header: true, //for header checkbox.
					    check: "Y", //check the checkbox when cell value is "YES".
					    uncheck: "N" //uncheck when "NO".
					}
                },
                { title: "JEPMSEQ", width: 0, hidden:true, dataIndx: "JEPMSEQ" ,filter: { crules: [{condition: 'contain' }] } },
                { title: "브랜드", width: 150, dataIndx: "BRAND",align : "left",	filter: { crules: [{condition: 'contain' }] },
		            	cls: 'pq-dropdown pq-side-icon',
		                editor: {
		                    type: "textbox",
		                    init: autoCompleteEditor( srvBrand ),
				            valueIndx: "value",
				            labelIndx: "value",	 
		                },
	                },
                { title: "분류", width: 100, dataIndx: "KIND",align : "left",	filter: { crules: [{condition: 'contain' }] },
		            	cls: 'pq-dropdown pq-side-icon',
		                editor: {
		                    type: "textbox",
		                    init: autoCompleteEditor( srvKind ),
				            valueIndx: "value",
				            labelIndx: "value",	 
		                },
	                },
                { title: "코드", width: 80, dataIndx: "JEPMCD",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                { title: "제품명", width: 200, dataIndx: "JEPMNM",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                { title: "바코드", width: 100, dataIndx: "BARCD",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                { title: "정가", width: 80, dataIndx: "PRICE",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                { title: "입고가", width: 80, dataIndx: "INPRICE",align : "right",datatype : "integer", format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                { title: "판매가", width: 80, dataIndx: "SALEPRICE",align : "right",datatype : "integer", format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                { title: "적용포인트", width: 80, dataIndx: "JPMBONUS",align : "right",datatype : "integer", format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                { title: "등록일", width: 100, dataIndx: "PRDATE",align : "center" ,filter: { crules: [{condition: 'contain' }] } },
    		];
//  grid.autoRow= true;
//  grid.editModel= {clicksToEdit: 1, keyUpDown: false };
	grid.title = "제품관리";
	grid.colModel = colM;
	grid.editModel= {        
	  	saveKey: $.ui.keyCode.ENTER,
	      keyUpDown: true,		//filterKeys: false,
	//      clicksToEdit: 1		//원클릭 수정(0,1)
	  };
    
//에러메세지	
	grid.warning = { 		    icon: 'ui-icon-info',		    cls: 'ui-state-default'};		//에러메세지

//    grid.create = function (evt, ui) {        this.widget().pqTooltip();    };
    
     var dataModel = {
						location: "remote",
						recIndx: "JEPMSEQ",
						sorting: "local",
						dataType: "JSON",
						method: "POST",
						url: "/is0005grid.do",
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
//recIndx 항목은 제외
		var rowData = { DEL:"N",NEW:"Y", JEPMSEQ: "key", BRAND: "", KIND: "", JEPMCD : "",JEPMNM : ""
						,PRICE : 0
						,INPRICE : 0
						,SALEPRICE : 0
						,JPMBONUS : 0
						,BARCD : "" }; //empty row template

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
		    url: "/is0005save.do", //for ASP.NET, java    
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

	
}); 

   
</script>


<body>

<div class="container-fluid text-left "id="app">	<!-- app -->

  <div id="container" >
	<div class="alert alert-info text-left" id = "title" role="alert">
		<span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
	  	제품 관리
	</div>
  	  
	<div class="panel panel-primary" id = "panel_head">  <!-- panel_head -->
	  <div class="panel-body"  id = "panel_head1">	<!-- panel_head1 -->
		
			<form class="navbar-form navbar-left" role="search" id="FormMain" name="FormMain"  method="post" >
				<div class="container show-grid " >
				    <div class="row " >
				      <div class="col-xs-4 col-sm-4 col-lg-3">
							<span class="label label-default">브랜드</span>			
							<select class="form-control" name = "frmBRAND" >
							    <option value="" selected>전체</option>
<c:forEach var="item" items="${comboBRAND}">
							    <option value="${item.value}">${item.value}</option>
</c:forEach> 
							</select>
				      </div>

				      <div class="col-xs-4 col-sm-4 col-lg-3">
							<span class="label label-default">분류</span>			
							<select class="form-control" name = "frmKIND" >
							    <option value="" selected>전체</option>
<c:forEach var="item" items="${comboKIND}">
							    <option value="${item.value}">${item.value}</option>
</c:forEach> 
							</select>
				      </div>

				      <div class="col-xs-10 col-sm-7 col-lg-3" align="left">
							<span class="label label-default">제품명</span>			
							<input type="text" class="form-control" name = "frmJEPMNM" id = "frmJEPMNM" placeholder="제품명">
				      </div>
				      
				      <div class="col-xs-3 col-sm-4 col-lg-1">
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
