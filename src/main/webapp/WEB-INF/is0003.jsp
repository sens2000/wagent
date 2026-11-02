<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<%@ page import="java.util.*,java.sql.*,com.sens.*" %>

<c:import url="/resources/jsp/includeTop.jsp" />
<c:import url="/resources/jsp/erpmenu.jsp" />
<script src="/resources/js/pqgrid.js"></script>

 <script>
$(function(){
	var position = JSON.parse('${POSITION}');
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
                { title: "SACD", width: 0, hidden:true, dataIndx: "SACD" ,filter: { crules: [{condition: 'contain' }] } },
                { title: "직위", width: 120, dataIndx: "POSITION",align : "left" ,filter: { crules: [{condition: 'contain' }] } ,
                	cls: 'pq-dropdown pq-side-icon',
	                    editor: {
	                        type: "select",
//	                        init: autoCompleteEditor( position ),
	    		            options: position,
	    		            valueIndx: "text",
	    		            labelIndx: "text"	                   
	                    },
	                    
                	},
               { title: "사번", width: 100, dataIndx: "SABUN",align : "left" ,filter: { crules: [{condition: 'contain' }] },
                   validations: [
                                 { type: 'nonEmpty',  msg: "사번을입력해주세요!" },
               				]
               	},
                { title: "성명", width: 150, dataIndx: "SANAME",align : "left" ,filter: { crules: [{condition: 'contain' }] },
                    validations: [
                                  { type: 'nonEmpty',  msg: "성명을입력해주세요!" },
                				]
                	},
               	
                { title: "폰번호", width: 200, dataIndx: "HANDNO",align : "center" ,filter: { crules: [{condition: 'contain' }] } },
                { title: "등록일", width: 100, dataIndx: "PRDATE",align : "center" ,filter: { crules: [{condition: 'contain' }] } },
    		];
    
//    grid.autoRow= true;
//    grid.editModel= {clicksToEdit: 1, keyUpDown: false };
	grid.title = "사원관리";
	grid.colModel = colM;
    grid.editModel= {        
    	saveKey: $.ui.keyCode.ENTER,
        keyUpDown: true,		//filterKeys: false,
//        clicksToEdit: 1		//원클릭 수정(0,1)
    };
	var dataModel = {
         location: "remote",
         sorting: "local",
         recIndx: "SACD",
         dataType: "JSON",
         method: "POST",
         url: "/is0003grid.do",
       	 postData : {},                                    // ajax와 마찬가지로 넘겨줄 데이터
         getData: function (dataJSON) {
             return { data: dataJSON.data };
         }
     };

 	grid.dataModel = dataModel;		//트레이스 처리(update + addlist)
     pq.grid( "#grid_paging", grid);

		$("#SEARCH").on("click",function()				{
			$( "#grid_paging" ).pqGrid( "option", "dataModel", dataModel );
			var paramData = JSON.stringify($("#FormMain").serializeObject());  //:문자열
			var paramUser = '${USER}';
			$( "#grid_paging" ).pqGrid( "option", "dataModel.postData", function( ui ){
			    return {
			    	param : paramData,
			    	user : paramUser
			    	};
			} );
		 	$("#grid_paging").pqGrid("refreshDataAndView");
		});
		
		$("#ADD").on("click",function(){
			//recIndx 항목은 제외
			var rowData = { DEL:"N",NEW:"Y", SACD: "key", POSITION: "", SABUN: "", SANAME : "",HANDNO : "" }; //empty row template
			var rowIndx = $("#grid_paging").pqGrid( "addRow", {rowData: rowData, checkEditable: true});
			$( "#grid_paging" ).pqGrid( "goToPage", { rowIndx: rowIndx } );
			$( "#grid_paging" ).pqGrid("editFirstCellInRow", { rowIndx: rowIndx });
		});
			
		$("#SAVE").on("click",function(){
			var gridChanges = $("#grid_paging").pqGrid("getChanges", { format: "byVal" });
//			     alert(JSON.stringify(gridChanges));
			$.ajax({
			    dataType: "json",
			    type: "POST",
			    async: true,
			    beforeSend: function (jqXHR, settings) {
			 	   $("#grid_paging").pqGrid("showLoading");
			    },
			    url: "/is0003save.do", //for ASP.NET, java    
			    data: {
			        list: JSON.stringify(gridChanges)
			    },
			    success: function (changes) {
			        $("#grid_paging").pqGrid("refreshDataAndView"); //reload fresh page data from server.
			    },
			    complete: function () {
			 	   $("#grid_paging").pqGrid("hideLoading");
			    }
			});
		});
		
 
}); 


</script>
<body>
<div class="container-fluid text-center "id="app" >
  <div id="container">
	<div class="alert alert-info text-left" role="alert" style="padding: 5px 5px 5px 5px;margin:0px 0px 5px 0px;">
		<span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
	  	사원 관리
	</div>

	<div class="panel panel-primary" id = "panel_head">
	  <div class="panel-body"  id = "panel_head1">
		
			<form class="navbar-form navbar-left" role="search" id="FormMain" name="FormMain"  method="post" >
				<div class="container show-grid ">
				    <div class="row">

				      <div class="col-xs-3 col-sm-4 col-lg-2">
							<input type="checkbox" id="frmCHECK" name = "frmCHECK" class="custom-control-input" checked="on">
							<label class="custom-control-label" for="jb-checkbox">사용</label>
				      </div>
				      <div class="col-xs-10 col-sm-7 col-lg-4" align="left">
							<span class="label label-default">사원명</span>			
							<input type="text" class="form-control" name = "frmACCTNAME" id = "frmACCTNAME" placeholder="사원명">
				      </div>
				      <div class="col-xs-2 col-sm-5 col-lg-2 navbar-right" >
							<button type="button" class="btn btn-primary pull-right" id="SEARCH" name="SEARCH">
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>						
								검색
							</button>
				      </div>
				    </div> <!--검색  Row End -->
				    
				    
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
		</div>	<!-- panel-body End -->
	</div>  <!-- panel-primary End -->
  </div>  <!-- container-fluid End -->

 
	<div id="grid_paging" ></div>

</div>
    

<!-- 
	<footer class="container-fluid text-center">
	  <p>Footer Text</p>
	</footer>
 -->
 
</body>
</html>
