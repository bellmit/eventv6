<html>
<head>
    <style>
        .w100{
            width:100px !important;
        }
        .p-relative {
            position: relative;
        }
        .option-box {
            width: 100%;
            height:50px;
            overflow: auto;
            background-color:#f4f4f4;
        }
        .option-box::-webkit-scrollbar {
            width: 4px;
        }
        .option-box::-webkit-scrollbar-track {
            background-color: rgba(0,0,0,0);
        }
        .option-box::-webkit-scrollbar-thumb {
            background-color: rgba(0, 0, 0, 0);
            border-radius: 2px;
        }
        .option-box:hover::-webkit-scrollbar-thumb {
            background-color: rgba(0, 0, 0, 0.2);
        }
        /*.option-box .mCSB_container {
            width:528px !important;
        }*/
        .option {
            position: relative;
            display: inline-block;
            min-width: 62px;
            height: 30px;
            line-height: 30px;
            background-color: rgb(215,233,243);
            padding: 0 5px;
            margin: 10px;
            text-align: center;
        }
        .option .remove {
            position: absolute;
            top: -8px;
            right: -6px;
            width: 16px;
            height: 16px;
            background-image: url("${rc.getContextPath()}/images/close_3.png");
            background-size: 100%;
        }
        .option .remove:hover {
            background-image: url("${rc.getContextPath()}/images/closeHover.png");
            background-size: 100%;
        }
        .save {
            /*position: absolute;
            bottom: 5px;
            left: 50%;*/
            float:none;
            width: 78px;
            /*margin-left: -39px;*/
            background-position-y: 9px;
            background-size: 15px;
            box-sizing: border-box;
            font-size: 15px;
        }
        .chaxun {
            width: 75px;
            background-position-y: 6px;
            background-size: 15px;
            box-sizing: border-box;
            font-size: 15px;
        }
        .box .datagrid-header-row,.box .datagrid-row {
            height: 30px;
        }
        .box .datagrid-cell,
        .box .datagrid-cell-group,
        .box .datagrid-header-rownumber,
        .box .datagrid-cell-rownumber {
            height: 30px;
            line-height: 30px;
        }
        .ConSearch .input-text {
            height: 28px;
            line-height: 28px;
            box-sizing: border-box;
            outline: 0;
        }
        .check_input {
            display: inline-block;
            vertical-align: middle;
            margin-top: -1px;
        }
        .hidediv{
            display:none
        }
    </style>
</head>

<body>
<div id="userSelectorWin" class="easyui-window hidediv">
    <div class="easyui-layout box" id="divLayout" style="width:100%; height:100%;">
        <div region="west" border="false"  style="width: 150px; padding:1px; height:100%;">
            <ul id="selectPmOrgTree" class="ztree"  style="height:auto" animate="true"></ul>
        </div>
        <div class="table-box" region="center" style="width:600px; height:100%;">
            <table id="pmlist"></table>
        </div>
        <div id="jqueryToolbarPm">
            <div class="ConSearch">
                <form id="searchFormPm">
                    <input type="hidden" id="orgId" name="orgId" />
                    <input  type="hidden" id="orgCode" name="orgCode" />
                    <input type="hidden" id="excludeIds" name= "excludeIds"/>
                    <input type="hidden" id="includeMobilePm" name="includeMobilePm" />
                    <input type="hidden" id="orgRealations" name="orgRealations" />
                    <div class="fl">
                        <li>???????????????</li><li><input type="text" id="deviceName" name="deviceName" class="inp1 w100 input-text" /></li>
                    </div>
                    <div class="btns">
                        <ul>
                            <li><a href="javascript:;" class="chaxun" title="????????????" onclick="searchPmData()">??????</a></li>
                        </ul>
                    </div>
                </form>
            </div>
            <div class="h_10" id="TenLineHeight1" style="border-bottom: 1px solid #d8d8d8;"></div>
        </div>
        <div class="p-relative" data-options="region:'south', split:true" style="height:100px;">
            <div id="selectedPm" class="option-box" style="">

            </div>
            <div style="width: 100%; height: calc(100% - 50px); background: #fff; padding-top: 5px; box-sizing: border-box; text-align: center;">
                <a href="javascript:;" class="BigNorToolBtn SaveBtn save" onClick="saveSelectPms();">??????</a>
                <a href="javascript:;" class="BigNorToolBtn CancelBtn save" onClick="closeSelectPms();">??????</a>
            </div>

        </div>
    </div>
</div>

</body>

<script type="text/javascript">
    var _options = {selectNum: 0, _selected_pms: {},onBeforeClose:function(){}};
    $(function(){
        $("#userSelectorWin").removeClass("hidediv");
        $('#userSelectorWin').window('close');
    });
    function saveSelectPms() {

        var _selected_ids = new Array();
        var _selected_names = new Array();
        if(JSON.stringify(_options._selected_pms) != "{}") {
            var selected_pms = _options._selected_pms;
            for (var k in selected_pms) {
                var obj = selected_pms[k];
                _selected_ids.push(obj.deviceId);
                _selected_names.push(obj.deviceName);
            }
        }
        if(_options.callback){
            //??????????????????
            _options.callback(_options._selected_pms);
        } else{
            if(_options.ids_id) {
                $('#' + _options.ids_id).val(_selected_ids.join(','));
            }
            if(_options.names_id) {
                $('#' + _options.names_id).val(_selected_names.join(','));
            }
        }
        closeSelectUsersWin();
    }

    function closeSelectPms() {
        closeSelectUsersWin();
        $('iframe[id="MaxJqueryWindow_0_iframe"]')[0].contentWindow.modleclose();
    }
    /**
     * ???????????????
     * options:
     *		_selected_pms:?????????????????????????????????{id1:{id:id1,name:name1,orgId:orgId1,orgCode:orgCode1,orgName:orgName1},id2:{}...}
     *      selectNum: ???????????????????????????????????????1???????????????,
     *      single_org_id: ????????????????????????????????????????????????????????????
     *		root_org_code: ?????????????????????Code(?????????????????????????????????????????????????????????),
     *		include_moblie_pm: '1'/'0' ???/????????????????????????????????????
     *		ex_org_realation:????????????????????? 1?????????2?????????3?????????4?????? ????????????????????????eg: '3,4'?????????????????????????????????????????????
     *		callback:	?????????????????????????????????????????????????????????????????????????????????
     *		width:????????????
     *		height???????????????
     */
    function selectPms(options) {
        clearAll();

        $('#searchFormPm')[0].reset();

        if(null == options) {
            $.messager.alert('??????','?????????????????????????????????','warning');
            return false;
        }
        $.extend(_options, options);

        //??????????????????
        renderRsDiv(_options);
        initDataGrid(_options);

        // ?????????????????????
        noTreeStyle(_options);


        $('#userSelectorWin').window({
            title: '????????????',
            width: 750,
            height: 430,
            left: ($(window).width() - 750) / 2,
            top: ($(window).height() - 400) / 2 + $(window).scrollTop(),
            modal: false,
            minimizable: false,
            maximizable: false,
            collapsible: false,
            boder: false,
            onBeforeClose:_options.onBeforeClose
        });
    }

    //??????????????????
    function renderRsDiv(options) {
        $('#selectedPm').html('');
        if(JSON.stringify(options._selected_pms) == "{}") {
            $('#selectedPm').html('<div style="padding:10px;">??????????????????:???<font style="color:red;">??????</font></div>');
        } else {
            var selected_pms = options._selected_pms;
            var _html = '';
            for(var k in selected_pms) {
                var o = selected_pms[k];
                _html += ('<div class="option"><span>'+o.name
                        +'</span><a deviceId="' + o.id + '" deviceName="' + o.name
                        +'" href="javascript:void(0);" class="remove" onclick="removePm(this);"></a></div>');
            }
            $('#selectedPm').append(_html);
        }
    }

    function noTreeStyle(options) {
        // ????????????????????????
        $('#divLayout').layout('remove', 'west');
        initDataGrid(options);
    }

    function initDataGrid(options){
        $('#pmlist').datagrid({
            rownumbers: true, //??????
            fitColumns: true, //???????????????
            striped: true,
            singleSelect: (options.selectNum == 1),//1??????????????????
            selectOnCheck: true,
            checkOnSelect: true,
            fit: true,
            url: '${rc.getContextPath()}/zhsq/event/pointInfo/listData.jhtml',
            queryParams: {
                deviceStatus: 1
            },
            columns: [[
                {field:'deviceId',checkbox:true,width:40,hidden:false},
                {field:'deviceName', title:'????????????', align:'center', width:100}
            ]],
            pagination: true,
            pageSize: 5,
            pageList: [5,10,20,50],
            toolbar: '#jqueryToolbarPm',
            onLoadSuccess: function(data) {
                pmlistSuccess(data); //??????????????????
                var selected_pms = options._selected_pms;
                if(JSON.stringify(selected_pms) != "{}" && data.total != 0){
                    var rows = data.rows;
                    for(var i = 0; i < rows.length; i++){
                        if(selected_pms.hasOwnProperty(rows[i].deviceId)){
                            $('#pmlist').datagrid('selectRow', i);
                        }
                    }
                }
            },
            onLoadError: function() {
                $('#userSelectorWin .datagrid-view').eq(0).append('<div class="r_elist">??????????????????</div>');
            },
            onSelect: function(index, row) {
                appendRow(row, options);
            },
            onUnselect: function(index, row) {
                removeRow(row, options);
            },
            onSelectAll: function(rows) {
                appendRows(rows, options);
            }
        });

        $('#userSelectorWin .datagrid-header-check input[type="checkbox"]').click(function(){
            var rows = $('#pmlist').datagrid('getSelections');
            if(rows.length == 0) {
                //????????????data
                var rows = $('#pmlist').datagrid('getRows');
                removeRows(rows, options);
            }
        });
    }

    // ??????
    function appendRows(rows, options) {
        for(var i=rows.length-1; i>=0; i--) {
            appendRow(rows[i], options);
        }
    }
    // ???????????????
    function removeRows(rows, options) {
        for(var i=rows.length-1; i>=0; i--) {
            var row = rows[i];
            removeRow(row, options);
        }
    }
    // ??????
    function appendRow(row, options) {
        var selected_pms = options._selected_pms;
        if(selected_pms.hasOwnProperty(row.id)){
            var obj = selected_pms[row.id];
            var obj = {
                deviceId: row.deviceId,
                deviceName: row.deviceName
            };
        } else {
            $('#selectedPm').html('');
            var obj = {
                deviceId: row.deviceId,
                deviceName: row.deviceName
            };
            var _html = '';
            if(options.selectNum == 1) {
                // ????????????????????????????????????????????????????????????
                selected_pms = {};
                selected_pms[row.deviceId] = obj;
                _html += ('<div class="option"><span>'+row.deviceName
                        +'</span><a deviceId="' + row.deviceId + '" deviceName="'+ row.deviceName
                        +'" href="javascript:void(0);" class="remove" onclick="removePm(this);"></a></div>');
                $('#selectedPm').append(_html);
            } else {
                selected_pms[row.deviceId] = obj;
                if(JSON.stringify(selected_pms) != "{}"){
                    for(var k in selected_pms) {
                        var o = selected_pms[k];
                        _html += ('<div class="option"><span>'+o.deviceName
                                +'</span><a deviceId="' + o.deviceId + '" deviceName="' + o.deviceName
                                +'" href="javascript:void(0);" class="remove" onclick="removePm(this);"></a></div>');
                    }
                    $('#selectedPm').append(_html);
                } else {
                    $('#selectedPm').html('<div style="padding:10px;">??????????????????:???<font style="color:red;">??????</font></div>');
                }
            }
            options._selected_pms = selected_pms;
        }

        return false;
    }

    function removePm(obj) {
        var o = $(obj);
        var _data = {
            deviceId: o.attr('deviceId'),
            deviceName: o.attr('deviceName')
        };
        var rows = $('#pmlist').datagrid('getRows');
        var inCurrentRows = false;
        for(var i = rows.length - 1; i >= 0; i--){
            if(_data.deviceId+"" == rows[i].deviceId + "") {
                inCurrentRows = true;
                var rowIndex=$('#pmlist').datagrid('getRowIndex', rows[i]);
                $('#pmlist').datagrid('unselectRow', rowIndex);
                return true;
            }
        }
        if(!inCurrentRows) {
            removeRow(_data, _options);
        }
    }
    //??????
    function removeRow(row, options){
        debugger
        $('#selectedPm').html('');
        var selected_pms = options._selected_pms;
        if(JSON.stringify(selected_pms) == "{}") {
            $('#selectedPm').html('<div style="padding:10px;">??????????????????:???<font style="color:red;">?????? </font></div>');
        } else {
            if(selected_pms.hasOwnProperty(row.deviceId)){
                // ?????????
                delete selected_pms[row.deviceId];
                var _html = '';
                for(var k in selected_pms) {
                    var o = selected_pms[k];
                    _html += ('<div class="option"><span>'+o.deviceName
                            +'</span><a deviceId="' + o.deviceId + '" deviceName="'+ o.deviceName
                            +'" href="javascript:void(0);" class="remove" onclick="removePm(this);"></a></div>');
                }
                $('#selectedPm').append(_html);
            } else {
                console.log('removeRow is not in options, confirm the opreate, id is: ' + row.deviceId);
            }
        }
        options._selected_pms = selected_pms;

        return false;
    }

    //??????
    function searchPmData() {
        $('#pmlist').datagrid('reload', $('#searchFormPm').serializeJson());
    }

    function closeSelectUsersWin(){
        $("#userSelectorWin").window('close');
        clearAll();
        event.stopPropagation();
    }
    function clearAll(){
        _options = {selectNum: 0, _selected_pms: {},onBeforeClose:function(){}};
        _options._selected_pms = {};
        try {
            $('#pmlist').datagrid('loadData', {total: 0, rows: []});
        } catch(e) {
            console.log('pmlist not init to datagrid');
        }
    }

    //???????????????
    function initSelectDivBar(){
        var options = {
            axis : "yx",
            theme : "minimal-dark"
        };
        enableScrollBar('selectedPm',options);
    }

    //?????????????????????
    function pmlistSuccess(data) {
        $('#pmlist').datagrid('clearSelections');	//???????????????????????????
        if (data.total == 0) {
            var noDataImg=$('#userSelectorWin .datagrid-view').find("#noDataImg");
            if(noDataImg.length==0){
                $('#userSelectorWin .datagrid-view').eq(0).append('<div id="noDataImg" style="text-align: center;padding-top:40px;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="????????????"/></div>');
            }
        }else{
            var noDataImg=$('#userSelectorWin #noDataImg');
            if(noDataImg.length>0){
                $('#userSelectorWin #noDataImg').remove();
            }
        }
    }

</script>
</html>