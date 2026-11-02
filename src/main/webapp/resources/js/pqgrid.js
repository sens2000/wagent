   var ajaxObj = {
            dataType: "JSON",
            beforeSend: function () {
                this.showLoading();
            },
            complete: function () {
                this.hideLoading();
            },
            error: function () {
                this.rollback();
            }
        };

    function isEditing(grid) {
        var rows = grid.getRowsByClass({ cls: 'pq-row-edit' });
        if (rows.length > 0) {
            var rowIndx = rows[0].rowIndx;
            grid.goToPage({ rowIndx: rowIndx });
            //focus on editor if any 
            grid.editFirstCellInRow({ rowIndx: rowIndx });
            return true;
        }
        return false;
    }

    
var grid =  {
			height : "100%",
			width : "100%",
//		flexwidth : true,
//      flexHeight : true,
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

	
    $.fn.serializeObject = function()	{
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

	/*
    rowSelect: function (evt,ui) {
        console.log('evt', evt);
        console.log('ui', ui);
        var aa = ui.addList[0].rowIndx;
        var a1 = ui.addList[0].rowData['CODE'];
        var a2 = ui.addList[0].rowData['NAME'];
        console.log('aa', aa);
        console.log('a1', a1);
        console.log('a2', a2);
    },

        
        rowSelect: function (evt, ui) {
            console.log('rowSelect', ui);
            alert(ui.colIndx);
            if (ui.rowData) {
                var rowIndx = ui.rowIndx,
                    colIndx = ui.colIndx,
                    dataIndx = ui.dataIndx,
                    cellData = ui.rowData[dataIndx];
                $("#select_cell_display_div").html("Cell selected rowIndx: " + rowIndx + ", colIndx: " + colIndx + ", dataIndx: " + dataIndx + ", value: " + cellData);
            }
        },
        
        selectionModel: { type: 'row', fireSelectChange: true },
                        
        reactive: true,
        roundCorners: true,
        rowBorders: false,
        selectionModel: { type: 'cell' },
        stripeRows: false,
        scrollModel: {autoFit: false},            
        showHeader: true,
        showTitle: true,
        showToolbar: true,
        showTop: true,
        
        toolbar: {
            items: [{
                type: "button",
                label: 'button on toolbar',
                listener: function () {
                    alert("button clicked");
                }
            }]
        },
        
        width: '100%'
        
    });

grid.on( "rowSelect", function( event, ui ) {
 console.log('rowSelect', ui);
 var code = ui.addList[0].rowData['CODE'];
 var name = ui.addList[0].rowData['NAME'];
 console.log('aa', aa);
 location.href="custQueryCard.do?CODE="+code + "&" + "NAME=" + name;
 var url_string =window.location.href;
 var url = new URL(url_string);
 var name = url.searchParams.get("name");
 location.href="" 이동
 location.reload() 새로고침
 history.back() 이전페이지
 history.forward(); 다음페이지


function datasend(){
    var loginForm = document.FormMain;
    var userId = loginForm.userId.value;
    var password = loginForm.password.value;
    
    if(!userId || !password){
        alert("아이디와 비밀번호를 모두 입력해주세요.");
    }else{
        FormMain.submit();
    }
}
*/
	
	
	