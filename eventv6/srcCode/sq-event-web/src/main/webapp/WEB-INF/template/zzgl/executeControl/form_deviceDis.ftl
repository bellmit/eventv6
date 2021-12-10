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
                        <li>设备名称：</li><li><input type="text" id="deviceName" name="deviceName" class="inp1 w100 input-text" /></li>
                    </div>
                    <div class="btns">
                        <ul>
                            <li><a href="javascript:;" class="chaxun" title="查询数据" onclick="searchPmData()">查询</a></li>
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
                <a href="javascript:;" class="BigNorToolBtn SaveBtn save" onClick="saveSelectPms();">确定</a>
                <a href="javascript:;" class="BigNorToolBtn CancelBtn save" onClick="closeSelectPms();">关闭</a>
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
            //调用回调函数
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
     * 选择器入口
     * options:
     *		_selected_pms:默认选择人员，格式如下{id1:{id:id1,name:name1,orgId:orgId1,orgCode:orgCode1,orgName:orgName1},id2:{}...}
     *      selectNum: 可选择人数（默认表示多选，1表示单选）,
     *      single_org_id: 选择器将只展示当前组织的人员，不包含子集
     *		root_org_code: 人员的顶级组织Code(不写，表示最高组织为当前用户的组织编码),
     *		include_moblie_pm: '1'/'0' 是/否包含流动党员，默认包含
     *		ex_org_realation:排除组织关系， 1正常、2转入、3转出、4退开 ，支持排除多个，eg: '3,4'表示排除组织关系是转出和退开的
     *		callback:	回调函数，存在回调函数时，不会采取默认的方式回写选中值
     *		width:弹窗宽度
     *		height：弹窗高度
     */
    function selectPms(options) {
        clearAll();

        $('#searchFormPm')[0].reset();

        if(null == options) {
            $.messager.alert('警告','设备选择器初始化错误！','warning');
            return false;
        }
        $.extend(_options, options);

        //渲染回填效果
        renderRsDiv(_options);
        initDataGrid(_options);

        // 是否展示树风格
        noTreeStyle(_options);


        $('#userSelectorWin').window({
            title: '设备选择',
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

    //渲染回填效果
    function renderRsDiv(options) {
        $('#selectedPm').html('');
        if(JSON.stringify(options._selected_pms) == "{}") {
            $('#selectedPm').html('<div style="padding:10px;">您的选择结果:　<font style="color:red;">暂无</font></div>');
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
        // 移除党组织树布局
        $('#divLayout').layout('remove', 'west');
        initDataGrid(options);
    }

    function initDataGrid(options){
        $('#pmlist').datagrid({
            rownumbers: true, //行号
            fitColumns: true, //自适应宽度
            striped: true,
            singleSelect: (options.selectNum == 1),//1则只能选一个
            selectOnCheck: true,
            checkOnSelect: true,
            fit: true,
            url: '${rc.getContextPath()}/zhsq/event/pointInfo/listData.jhtml',
            queryParams: {
                deviceStatus: 1
            },
            columns: [[
                {field:'deviceId',checkbox:true,width:40,hidden:false},
                {field:'deviceName', title:'设备名称', align:'center', width:100}
            ]],
            pagination: true,
            pageSize: 5,
            pageList: [5,10,20,50],
            toolbar: '#jqueryToolbarPm',
            onLoadSuccess: function(data) {
                pmlistSuccess(data); //暂无数据提示
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
                $('#userSelectorWin .datagrid-view').eq(0).append('<div class="r_elist">数据加载出错</div>');
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
                //取当前页data
                var rows = $('#pmlist').datagrid('getRows');
                removeRows(rows, options);
            }
        });
    }

    // 全选
    function appendRows(rows, options) {
        for(var i=rows.length-1; i>=0; i--) {
            appendRow(rows[i], options);
        }
    }
    // 取消行所有
    function removeRows(rows, options) {
        for(var i=rows.length-1; i>=0; i--) {
            var row = rows[i];
            removeRow(row, options);
        }
    }
    // 添加
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
                // 单一选择器时候，添加时候应该删除其他所有
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
                    $('#selectedPm').html('<div style="padding:10px;">您的选择结果:　<font style="color:red;">暂无</font></div>');
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
    //删除
    function removeRow(row, options){
        debugger
        $('#selectedPm').html('');
        var selected_pms = options._selected_pms;
        if(JSON.stringify(selected_pms) == "{}") {
            $('#selectedPm').html('<div style="padding:10px;">您的选择结果:　<font style="color:red;">暂无 </font></div>');
        } else {
            if(selected_pms.hasOwnProperty(row.deviceId)){
                // 先移除
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

    //查询
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

    //美化滚动条
    function initSelectDivBar(){
        var options = {
            axis : "yx",
            theme : "minimal-dark"
        };
        enableScrollBar('selectedPm',options);
    }

    //列表加载成功时
    function pmlistSuccess(data) {
        $('#pmlist').datagrid('clearSelections');	//清除掉列表选中记录
        if (data.total == 0) {
            var noDataImg=$('#userSelectorWin .datagrid-view').find("#noDataImg");
            if(noDataImg.length==0){
                $('#userSelectorWin .datagrid-view').eq(0).append('<div id="noDataImg" style="text-align: center;padding-top:40px;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>');
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