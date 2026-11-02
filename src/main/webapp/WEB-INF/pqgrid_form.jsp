<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!--jQuery dependencies-->
        <link rel="stylesheet"
            href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/base/jquery-ui.css" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>   
        <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>

        <!--Include Touch Punch file to provide support for touch devices-->
        <script type="text/javascript" src="grid/touch-punch.js" ></script> 

        <!--ParamQuery Grid files-->
        <link rel="stylesheet" href="grid/pqgrid.min.css" />
        <link class="link-override" rel="stylesheet" href="grid/themes/Office/pqgrid.css" />
        <script type="text/javascript" src="grid/pqgrid.min.js" ></script>     
        <script>
        $(function(){
            var data = [ [1,'Exxon Mobil','339,938.0','36,130.0'],
                    [2,'Wal-Mart Stores','315,654.0','11,231.0'],
                    [3,'Royal Dutch Shell','306,731.0','25,311.0'],
                    [4,'BP','267,600.0','22,341.0'],
                    [5,'General Motors','192,604.0','-10,567.0'],
                    [6,'Chevron','189,481.0','14,099.0'],
                    [7,'DaimlerChrysler','186,106.3','3,536.3'],
                    [8,'Toyota Motor','185,805.0','12,119.6'],
                    [9,'Ford Motor','177,210.0','2,024.0'],
                    [10,'ConocoPhillips','166,683.0','13,529.0'],
                    [11,'General Electric','157,153.0','16,353.0'],       
                    [12,'Total','152,360.7','15,250.0'],               
                    [13,'ING Group','138,235.3','8,958.9'],
                    [14,'Citigroup','131,045.0','24,589.0'],
                    [15,'AXA','129,839.2','5,186.5'],
                    [16,'Allianz','121,406.0','5,442.4'],
                    [17,'Volkswagen','118,376.6','1,391.7'],
                    [18,'Fortis','112,351.4','4,896.3'],
                    [19,'Crédit Agricole','110,764.6','7,434.3'],
                    [20,'American Intl. Group','108,905.0','10,477.0']];

//            var obj = {};
            var obj = { width: 900, height: 460, sortIndx: 0,
                editable: false,
                flexHeight:true,
                title: "Companies listed on the <b>NASDAQ</b>",
                freezeCols: 1, resizable: true, selectionModel: { type: 'row' }, editModel: { clicksToEdit: 2 },
                selectionModel: { mode: 	'single' }
            };
            obj.width = 700;
            obj.height = 400;
            obj.colModel = [{title:"Rank", width:100, dataType:"integer"},
                {title:"Company", width:200, dataType:"string"},
                {title:"Revenues ($ millions)", width:150, dataType:"float", align:"right"},
                {title:"Profits ($ millions)", width:150, dataType:"float", align:"right"}];
            //obj.dataModel = {data:data};
            obj.dataModel = { data: data, paging: "local", rPP: 15, rPPOptions: [10, 15, 20, 50, 100] };
            $("#grid_array").pqGrid( obj );                               

        });
       
        $("#grid_array").on("pqgridrender", function (evt, obj) {
            var $toolbar = $("<div class='pq-grid-toolbar pq-grid-toolbar-crud'></div>").appendTo($(".pq-grid-top", this));

            $("<span>Add</span>").appendTo($toolbar).button({ icons: { primary: "ui-icon-circle-plus"} }).click(function (evt) {
                addRow();
            });
            $("<span>Edit</span>").appendTo($toolbar).button({ icons: { primary: "ui-icon-pencil"} }).click(function (evt) {
                editRow();
            });
            $("<span>Delete</span>").appendTo($toolbar).button({ icons: { primary: "ui-icon-circle-minus"} }).click(function () {
                deleteRow();
            });
            $toolbar.disableSelection();
        });

        var $grid = $("#grid_array").pqGrid(newObj);
        //create popup dialog.
        $("#popup-dialog-crud").dialog({ width: 400, modal: true,
            open: function () { $(".ui-dialog").position({ of: "#grid_array" }); },
            autoOpen: false
        });
        //edit Row
        function editRow() {
            var rowIndx = getRowIndx();
            if (rowIndx != null) {
                var DM = $grid.pqGrid("option", "dataModel");
                var data = DM.data;
                var row = data[rowIndx];
                var $frm = $("form#crud-form");
                $frm.find("input[name='company']").val(row[0]);
                $frm.find("input[name='symbol']").val(row[1]);
                $frm.find("input[name='price']").val(row[3]);
                $frm.find("input[name='change']").val(row[4]);
                $frm.find("input[name='pchange']").val(row[5]);
                $frm.find("input[name='volume']").val(row[6]);

                $("#popup-dialog-crud").dialog({ title: "Edit Record (" + (rowIndx + 1) + ")", buttons: {
                    Update: function () {
                        //save the record in DM.data.
                        row[0] = $frm.find("input[name='company']").val();
                        row[1] = $frm.find("input[name='symbol']").val();
                        row[3] = $frm.find("input[name='price']").val();
                        row[4] = $frm.find("input[name='change']").val();
                        row[5] = $frm.find("input[name='pchange']").val();
                        row[6] = $frm.find("input[name='volume']").val();
                        //$grid.pqGrid("refreshDataAndView").pqGrid('setSelection',{ rowIndx:rowIndx});
                        $grid.pqGrid("refreshRow", { rowIndx: rowIndx }).pqGrid('setSelection', { rowIndx: rowIndx });
                        $(this).dialog("close");
                    },
                    Cancel: function () {
                        $(this).dialog("close");
                    }
                }
                }).dialog("open");
            }
        }
        //append Row
        function addRow() {
            //debugger;
            var DM = $grid.pqGrid("option", "dataModel");
            var data = DM.data;

            var $frm = $("form#crud-form");
            $frm.find("input").val("");

            $("#popup-dialog-crud").dialog({ title: "Add Record", buttons: {
                Add: function () {                   
                    var row = [];
                    //save the record in DM.data.
                    row[0] = $frm.find("input[name='company']").val();
                    row[1] = $frm.find("input[name='symbol']").val();
                    row[3] = $frm.find("input[name='price']").val();
                    row[4] = $frm.find("input[name='change']").val();
                    row[5] = $frm.find("input[name='pchange']").val();
                    row[6] = $frm.find("input[name='volume']").val();
                    data.push(row);
                    $grid.pqGrid("refreshDataAndView");
                    $(this).dialog("close");
                },
                Cancel: function () {
                    $(this).dialog("close");
                }
            }
            });
            $("#popup-dialog-crud").dialog("open");
        }
        //delete Row.
        function deleteRow() {
            var rowIndx = getRowIndx();
            if (rowIndx != null) {
                var DM = $grid.pqGrid("option", "dataModel");
                DM.data.splice(rowIndx, 1);
                $grid.pqGrid("refreshDataAndView");
                $grid.pqGrid("setSelection", { rowIndx: rowIndx });
            }
        }
        function getRowIndx() {
            //var $grid = $("#grid_render_cells");

            //var obj = $grid.pqGrid("getSelection");
            //debugger;
            var arr = $grid.pqGrid("selection", { type: 'row', method: 'getSelection' });
            if (arr && arr.length > 0) {
                var rowIndx = arr[0].rowIndx;

                //if (rowIndx != null && colIndx == null) {
                return rowIndx;
            }
            else {
                alert("Select a row.");
                return null;
            }
        }
       
        </script>           
    </head>
    <body>
        <table width="610" id="nasdaq_market_table" cellspacing="1" cellpadding="0" style="display:none;">
        <div id="grid_array"></div> 
        </table>
    </body>
</html>
