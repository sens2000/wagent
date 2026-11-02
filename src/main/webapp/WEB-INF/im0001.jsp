<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<%@ page import="java.util.*,java.sql.*,com.sens.*" %>

<c:import url="/resources/jsp/includeTop.jsp" />
<c:import url="/resources/jsp/erpmenu.jsp" />

<script src="/resources/js/pqgrid.js"></script>
 
<link rel="stylesheet"	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker3.min.css">
<link rel="stylesheet"	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker3.standalone.min.css">
<script	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js">	</script> 
<script	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/locales/bootstrap-datepicker.kr.min.js">	</script> 

<style>

    .ui-dialog *
    {
        font-family:Tahoma;
        font-size:11px;
    }
    .ui-dialog form#crud-form
    {
        margin-top:20px;
    }
    .ui-dialog form#crud-form input
    {
        width:230px;
        overflow:visible;/*fix for IE*/
    }
    .ui-dialog form#crud-form td.label
    {
        font-weight:bold;padding-right:5px;
    }
    div.pq-grid-toolbar-crud
    {
        text-align:center;
    }
    #popup-dialog-crud
    {
        width:100% !important;
        min-height:100% !important;
    }    
</style>


<body>

<div class="container-fluid text-left "id="app">	<!-- app -->

  <div id="container" >
	<div class="alert alert-info text-left" id = "title" role="alert">
		<span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
	  	매출등록
	</div>
  	  
	<div class="panel panel-primary" id = "panel_head">  <!-- panel_head -->
	  <div class="panel-body"  id = "panel_head1">	<!-- panel_head1 -->
		
			<form class="navbar-form navbar-left" role="search" id="FormMain" name="FormMain"  method="post" >
				<div class="container show-grid " >
				    <div class="row " >
				      <div class="col-xs-10 col-sm-7 col-lg-4" align="left">
						<span class="label label-default">일자</span>			
				        <div class="input-group date">
				            <input type="text" class="form-control datePicker" style="width:100px;"  id="FRDATE" name="FRDATE">
				        </div>
				        <div class="input-group date">
				            <input type="text" class="form-control datePicker" style="width:100px;"  id="TODATE" name="TODATE">
				        </div>
				      </div>	
				      				    
				      <div class="col-xs-10 col-sm-7 col-lg-4" align="left">
							<span class="label label-default">거래처명</span>			
							<input type="text" class="form-control" name = "frmCUSTNAME" id = "frmCUSTNAME" placeholder="거래처명">
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
							<button type="button" class="btn btn-default btn-sm" id="ADD" name="ADD" style="width:65px;" onclick="datasend()">매출</button>
							<button type="button" class="btn btn-default btn-sm" id="SAVE" name="SAVE" style="width:65px;" onclick="datasend()">수금</button>
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

<div id="popup-dialog-crud" style="display:none;">
	<div class="container"> 
		<div class="row"> 
			<div class="col-lg-10">
				<input type="hidden" id="popCUSTCD" name=""popCUSTCD"" />

                <table class="table " id="dataTable" width="100%" cellspacing="0">
                    <tr>
                      <td class="col-slg3">
						<input type="text" class="form-control" name = "popCUSTNAME" id = "popCUSTNAME" placeholder="거래처명">
                      </td>
                      <td class="col-lg-2">
						<input type="text" class="form-control" name = "popTELNO" id = "popTELNO" placeholder="전화번호">
                      </td>
                      <td class="col-lg-2">
						<input type="text" class="form-control" name = "popMGENAME" id = "popMGENAME" placeholder="담당자">
                      </td>
                      <td class="col-lg-1 navbar-left">
						<button type="button" class="btn btn-primary" id="frmCUSTSEARCH" name="frmCUSTSEARCH" >검색</button>
                      </td">
                    </tr>
			                    
                    <tr>
                      <td class="col-lg-3" >
							<select class="form-control" id="frmJEPMSEL" style="width:40%;float:left"> 
								<option>제품코드</option> 
								<option>바코드</option> 
							</select>
							<span> 
						<input type="text" class="form-control" name = "frmJEPMCD" id = "frmJEPMCD" placeholder="코드" style="width:60%;float:right">
						</span>
                      </td>
                      <td class="col-lg-2">
						<input type="text" class="form-control" name = "frmBRAND" id = "frmBRAND" placeholder="브랜드">
                      </td>
                      <td class="col-lg-2">
						<input type="text" class="form-control" name = "frmJEPMNAME" id = "frmJEPMNAME" placeholder="제품명">
                      </td>
                      <td class="col-lg-5">
				      	<div class="btn-toolbar navbar-left" >
							<button type="button" class="btn btn-primary" id="frmJEPMSEARCH" name="frmJEPMSEARCH" style="width:65px;">검색</button>
							<button type="button" class="btn btn-primary" id="frmSAVE" name="frmSAVE" style="width:65px;">저장</button>
				      	</div> <!--  btn-toolbar End -->                      
                      </td>
                    </tr>                    
			</table>
		      
		</div>  <!-- col end -->
	</div>	<!-- row end -->

	<div id="grid_mc" style="margin:3px;padding:1px;"></div>
	
</div>
</div>

<script>

	w = 640; //팝업창의 너비
	h = 510; //팝업창의 높이 
	LeftPosition=(screen.width-w)/2;
	TopPosition=(screen.height-h)/2;
	
	function openPopup(strUrl, strWindowName){
	    var options ="scrollbars, resizable, toolbar=no, left="+screen.width/2;
		if(navigator.userAgent.indexOf('Chrome')>-1)		{
			options ="top=30, width=800, height=700,chrome=1,centerscreen=1";
	//		options = "width="+w+",height="+h+",top="+TopPosition+",left="+LeftPosition+", scrollbars=no";
			}
		else{	
			options ="top=30, width=800, height=700,chrome=1,centerscreen=1";
	//		options = "width="+w+",height="+h+",top="+TopPosition+",left="+LeftPosition+", scrollbars=no";
	//chrome=1,centerscreen=1
			}
		window.open(strUrl, strWindowName, options);
	};
	
	function getReturnValue(returnValue) {
		var obj = JSON.parse(returnValue);
		$("#popCUSTCD").val(obj.code);
		$("#popCUSTNAME").val(obj.name);
		$("#popTELNO").val(obj.telno);
		$("#popMGENAME").val(obj.saname);
		 
		 /*	 var addr = 'admin?cmd=impl&impl=route&act=smi&rid=${rid }';
		  addr += '&sid=' + obj.sid;
		  addr += '&sorder=' + obj.sorder;
		  addr += '&sname=' + encodeURIComponent(obj.sname); //이건 한글을 받아올때 사용
		 addr += '&slat=' + obj.slat;
		 addr += '&slong=' + obj.slong; 
		 location.href = addr; */
	//     row[0] = $frm.find("input[name='company']").val();
	//     $frm.find("input[name='company']").val(row[0]);
		 
	//	 $("table[name='frmCUSTNAME']").val(obj.name);
	//	 $("table > input[name*='frmCUSTNAME']").css("background", "yellow");
	//	  alert(obj.name);
		}
	
</script>


<script>
$(function(){
	var srvMimc = JSON.parse('${MIMC}');
	var srvSaname = JSON.parse('${SAWONLIST}');
	var srvMcGrade = JSON.parse('${MCGRADE}');
	
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
//		openPopup("/pop0001.do","판매등록");		
		newPopup("form#crud-form");
	});
		
	$("#SAVE").on("click",function(){
		openPopup("/pop0001.do","수금등록");		
	});
		
	$("#frmCUSTSEARCH").on("click",function(){
		openPopup("/pop0001.do","거래처검색");		
	});	

	$("#frmJEPMSEARCH").on("click",function(){
		openPopup("/pop0002.do","제품검색");		
//		var rowData = {DEL:"N",NEW:"Y" }; //empty row template
//		var rowIndx = $("#grid_mc").pqGrid( "addRow", {rowData: rowData, checkEditable: true});
//		$( "#grid_mc" ).pqGrid( "goToPage", { rowIndx: rowIndx } );
//		$( "#grid_mc" ).pqGrid("editFirstCellInRow", { rowIndx: rowIndx });
	});	
	
	
	$('.datePicker').datepicker({
		format : "yyyy-mm-dd", // 달력에서 클릭시 표시할 값 형식
		todayBtn: true ,
		useCurrent: true,
		todayHighlight: true,
		autoclose: true,
		footer: true, 
		showOtherMonths: true,
		autoSize: true,
		language : "kr", // 언어(js 추가가 필요하다.)
//		minDate:(new Date()).toDateString(),
//		clearBtn: false,
//		calendarWeeks: true,
//		uiLibrary: 'bootstrap',
//		modal: true,
//		inline: true,
//		nextText: '&rarr;',
//		prevText: '&larr;',
//		showOn: "button",
//		buttonImage: "/resources/img/logo_100.jpg",			
//		startDate : '-3d',
	}).datepicker("setDate", "today");
	
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

    function editRow(rowIndx, $grid) {
        //debugger;
        alert("11");
        $grid.pqGrid("addClass", { rowIndx: rowIndx, cls: 'pq-row-edit' });
        $grid.pqGrid("editFirstCellInRow", { rowIndx: rowIndx });

        //change edit button to update button and delete to cancel.
        var $tr = $grid.pqGrid("getRow", { rowIndx: rowIndx }),
            $btn = $tr.find("button.edit_btn");
        $btn.button("option", { label: "Update", "icons": { primary: "ui-icon-check"} })
            .unbind("click")
            .click(function (evt) {
                evt.preventDefault();
                return update(rowIndx, $grid);
            });
        $btn.next().button("option", { label: "Cancel", "icons": { primary: "ui-icon-cancel"} })
            .unbind("click")
            .click(function (evt) {
                $grid.pqGrid("quitEditMode")
                    .pqGrid("removeClass", { rowIndx: rowIndx, cls: 'pq-row-edit' })
                    .pqGrid("rollback");
            });
    }
		
    function newPopup(form) {

        var $frm = $(form);
        $frm.find("input").val("");

        $("#popup-dialog-crud").dialog({ 
        	title: "판매등록",
        	width : 1200,
        	 height : 700,
        	modal : true,
//            autoOpen: false, 
//	        		var $gridMc = pq.grid( "#grid_mc", grid1);
        	open:function(event,ui){
        		var $gridMc = pq.grid( "#grid_mc", grid1);
        	},

        	buttons: {
	            "등록": function () {
	                var row = [];
	                //save the record in DM.data.
	                row[0] = $frm.find("input[name='company']").val();
	                row[1] = $frm.find("input[name='symbol']").val();
	                row[3] = $frm.find("input[name='price']").val();
	                row[4] = $frm.find("input[name='change']").val();
	                row[5] = $frm.find("input[name='pchange']").val();
	                row[6] = $frm.find("input[name='volume']").val();
	
	                $grid.pqGrid('addRow', { rowData: row });
	                $(this).dialog("close");
	            },
	           	"닫기": function () {
	                $(this).dialog("close");
	            }
	            , show: {                // 애니메이션 효과 - 보여줄때
	                effect: "blind",
	                duration: 1000
	            }
	          }
        });
        $("#grid_paging").dialog("open");
    }
    
    function getRowIndx() {
        var arr = $grid.pqGrid("selection", { type: 'row', method: 'getSelection' });
        console.log("arr=" + arr);
        if (arr && arr.length > 0) {
            return arr[0].rowIndx;                                
        }
        else {
            alert("Select a row.");
            return null;
        }
    }
    
    function editPopup() {
        var rowIndx = getRowIndx();
        if (rowIndx != null) {

            var row = $grid.pqGrid('getRowData', {rowIndx: rowIndx});

            var $frm = $("form#crud-form");
            $frm.find("input[name='company']").val(row[0]);
            $frm.find("input[name='symbol']").val(row[1]);
            $frm.find("input[name='price']").val(row[3]);
            $frm.find("input[name='change']").val(row[4]);
            $frm.find("input[name='pchange']").val(row[5]);
            $frm.find("input[name='volume']").val(row[6]);

            $("#popup-dialog-crud").dialog({ title: "Edit Record (" + (rowIndx + 1) + ")", buttons: {
                Update: function () {
                    //update row.
                    var row = [];
                    row[0] = $frm.find("input[name='company']").val();
                    row[1] = $frm.find("input[name='symbol']").val();
                    row[3] = $frm.find("input[name='price']").val();
                    row[4] = $frm.find("input[name='change']").val();
                    row[5] = $frm.find("input[name='pchange']").val();
                    row[6] = $frm.find("input[name='volume']").val();

                    $grid.pqGrid('updateRow', { rowIndx: rowIndx, row: row, checkEditable: false });

                    $(this).dialog("close");
                },
                Cancel: function () {
                    $(this).dialog("close");
                }
            }
            }).dialog("open");
        }
    }    


/*    
    <a class="popUp_btn" href=addCustomer target="addCustWindow" 
	     onClick="openPopUp(this.href, this.target); return false;" title="팝업"
		 >추가</a>
*/		 
		 
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
	            { title: "JPNO", width: 0, hidden:true, dataIndx: "JPNO" ,filter: { crules: [{condition: 'contain' }] } },
	            { title: "사원", width: 100, dataIndx: "SANAME",align : "left",	filter: { crules: [{condition: 'contain' }] } },
	            { title: "거래처명", width: 150, dataIndx: "CUSTNAME",align : "left",	filter: { crules: [{condition: 'contain' }] } },
	            { title: "CUSTCD", width: 150,hidden:true, dataIndx: "CUSTCD",align : "left",	filter: { crules: [{condition: 'contain' }] } },
	            { title: "일자", width: 100, dataIndx: "CDATE",align : "left",	filter: { crules: [{condition: 'contain' }] } },
	            //dataType: 'date', format: 'yy-mm-dd'
	            { title: "매출금액", width: 80, dataIndx: "AMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
	            { title: "반품", width: 80, dataIndx: "BANPUM",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
	            { title: "총금액", width: 80, dataIndx: "TOTAMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
	            { title: "현금", width: 80, dataIndx: "CASHAMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
	            { title: "할인", width: 80, dataIndx: "SALEAMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
	            { title: "미수", width: 80, dataIndx: "MISUAMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
	            { title: "비고", width: 200, dataIndx: "REMK",align : "left",	filter: { crules: [{condition: 'contain' }] } },
	            { title: "등록일", width: 100, dataIndx: "PRDATE",align : "center" ,filter: { crules: [{condition: 'contain' }] } },
	            { title: "", editable: false, minWidth: 85, sortable: false,
	                render: function (ui) {
	                    return "<button type='button' class='edit_btn'>수정</button>";
	                },
	                postRender: function (ui) {
	                    var grid = this,
	                        $cell = grid.getCell(ui);
	                    $cell.find(".edit_btn")
	                        .button({ icons: { primary: 'ui-icon-pencil'} })
	                        .bind("click", function (evt) {
	                    	    if (ui.rowData) {
	                                var rowIndx = ui.rowIndx,
	                                    colIndx = ui.colIndx,
	                                    dataIndx = ui.dataIndx,
	                                    cellData = ui.rowData[dataIndx];
	                                editPopup();
	//                                alert("Cell selected rowIndx: " + rowIndx + ", colIndx: " + colIndx + ", dataIndx: " + dataIndx + ", value: " + cellData);
	                            }
	                    	    //grid.deleteRow({ rowIndx: ui.rowIndx });
	                        });
	                }
	            }
			];
	grid.postRenderInterval= -1; //synchronous post render.
	
	//grid.autoRow= true;
	//grid.editModel= {clicksToEdit: 1, keyUpDown: false };
	grid.title = "거래처관리";
	grid.colModel = colM;
	grid.editModel= {        
	  	saveKey: $.ui.keyCode.ENTER,
	      keyUpDown: true,		//filterKeys: false,
	//      clicksToEdit: 1		//원클릭 수정(0,1)
	  };
	
	//에러메세지	
	grid.warning = { 		    icon: 'ui-icon-info',		    cls: 'ui-state-default'};		//에러메세지
	
	//grid.create = function (evt, ui) {        this.widget().pqTooltip();    };
	
	 var dataModel = {
						location: "remote",
						recIndx: "JPNO",
						sorting: "local",
						dataType: "JSON",
						method: "POST",
						url: "/im0001grid.do",
						postData : {},                                    // ajax와 마찬가지로 넘겨줄 데이터
						getData: function (dataJSON) {
							return { data: dataJSON.data };
						}
			};
	 
		grid.dataModel = dataModel;			//트레이스 처리(update + addlist)
	grid.summaryData= [
	              { CDATE_FMT:'누계', summaryRow: true, pq_fn:{AMT:'sum(G:G)', BANPUM:'sum(H:H)', TOTAMT:'sum(I:I)', CASHAMT:'sum(J:J)',SALEAMT:'sum(K:K)' }},
	          ];
	/* 	
		grid.rowSelect = function( event, ui ) {
		console.log(event);
		console.log(ui);
	    if (ui.rowData) {
	        var rowIndx = ui.rowIndx,
	            colIndx = ui.colIndx,
	            dataIndx = ui.dataIndx,
	            cellData = ui.rowData[dataIndx];
	        alert("Cell selected rowIndx: " + rowIndx + ", colIndx: " + colIndx + ", dataIndx: " + dataIndx + ", value: " + cellData);
	    }
	alert("11");
	};
	*/


	var $grid = pq.grid( "#grid_paging", grid);


	var grid1 =  {
//			height : 500,
//			width : "900",
			flexwidth : true,
			flexHeight : true,
			collapsible: true,
			bootstrap : { on: true },
//            filterModel: { header: true, type: 'local', menuIcon: true },
//            dataModel: dataModel,
//            colModel: colM,
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
//                menuIcon: true //show filter row icon initially.
            },
//            menuIcon: true,
//            menuUI:{                    tabs: ['hideCols']                },                
                            //move paging toolbar to top in create event callback.
			numberCell: { show: false },
			pageModel: { type: "" },
			selectionModel : { type: 'row' , mode: 'range' },
//        showBottom: true,
			trackModel: { on: true },
//			title: "매장관리",
			showTitle : false,
//          scrollModel: {autoFit: true},  //넓이 꽉차게
			scrollModel: { pace: 'fast', autoFit: false, theme: true },       
			validation: { icon: 'ui-icon-info' },
//        swipeModel: { on: false },
			editModel: { saveKey: $.ui.keyCode.ENTER },
			editor: { select: true  },                
			sortModel: {ignoreCase: true},
			resizable: true
        };
    var col1 = [
                   { title: "DEL", width: 70, dataIndx: "DEL",hidden:false,align : "center",type: 'checkbox',filter: { crules: [{condition: 'range' }] }, 
                   	editable: true,
   					cb: {
   					    all: false, //header checkbox to affect checkboxes on all pages.
   					    header: true, //for header checkbox.
   					    check: "Y", //check the checkbox when cell value is "YES".
   					    uncheck: "N" //uncheck when "NO".
   					}
                   },
                   { title: "JPNO", width: 0, hidden:true, dataIndx: "JPNO" ,filter: { crules: [{condition: 'contain' }] } },
                   { title: "SEQ", width: 0, hidden:true, dataIndx: "SEQ" ,filter: { crules: [{condition: 'contain' }] } },
                   { title: "JEPMSEQ", width: 0, hidden:true, dataIndx: "JEPMSEQ" ,filter: { crules: [{condition: 'contain' }] } },
                   { title: "NO", width: 30, dataIndx: "NO",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                   { title: "제품코드", width: 100, dataIndx: "SANAME",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                   { title: "제품명", width: 250, dataIndx: "CUSTNAME",align : "left",	filter: { crules: [{condition: 'contain' }] } },
                   { title: "정가", width: 80, dataIndx: "AMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                   { title: "판매가", width: 80, dataIndx: "AMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                   { title: "%", width: 50, dataIndx: "AMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                   { title: "수량", width: 50, dataIndx: "AMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                   { title: "덤", width: 50, dataIndx: "AMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                   { title: "부가세", width: 80, dataIndx: "AMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                   { title: "금액", width: 100, dataIndx: "AMT",align : "right",datatype : "integer", sortable: true, format : "#,##0", filter: { crules: [{condition: 'contain' }] } },
                   { title: "비고", width: 150, dataIndx: "REMK",align : "left",	filter: { crules: [{condition: 'contain' }] } }
       		];
    
    var data1 = {
			location: "remote",
			recIndx: "JPNO",
			sorting: "local",
			dataType: "JSON",
			method: "POST",
			url: "/im0001grid.do",
			postData : {},                                    // ajax와 마찬가지로 넘겨줄 데이터
			getData: function (dataJSON) {
				return { data: dataJSON.data };
			}
	};
    grid1.colModel = col1;
    grid1.dataModel = data1;			//트레이스 처리(update + addlist)

    
});		//jquery end

</script>



</body>
</html>
