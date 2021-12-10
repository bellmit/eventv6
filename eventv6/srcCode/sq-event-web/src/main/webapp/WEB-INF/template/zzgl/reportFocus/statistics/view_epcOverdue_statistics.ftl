<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>南安入格事件-入格事项超时环节统计报表</title>
    <#include "/component/standard_common_files-1.1.ftl" />
    <link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
    <script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/layui-v2.5.5/layui/css/layui.css" />
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_zhcs/nanan/css/layuiExtend.css" />
    <#include "/component/ComboBox.ftl" />
    <script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
</head>
<body class="easyui-layout">
<div id="governAidObjectDiv" region="center" border="false" style="overflow:hidden;">
    <table id="list"></table>
</div>
<style type="text/css">
    .w150{width:150px;}
</style>
<div id="jqueryToolbar" style="padding-top:0;">
    <form id="delEventQueryForm">
        <input type="hidden" id="reportType" name="reportType" class="queryParam" value="${reportType!}" />
        <input type="hidden" id="infoOrgCode" name="infoOrgCode" type="text" class="queryParam" value="${infoOrgCode!}"/>

        <div class="ConSearch">
            <div class="fl">
                <ul>
                    <li>所属区域：</li>
                    <li>
                        <input id="eOrgCode" name="eOrgCode" type="text" class="hide queryParam"/>
                        <input id="gridId" type="text" class="hide" value="${gridId}"/>
                        <input id="gridName" type="text" class="inp1 InpDisable w150" style="" />
                    </li>
                    <li>报告时间：</li>
                    <li>
                        <div class="layui-form-item">
                            <input class="inp1 hide queryParam" type="text" id="beginTime" name="beginTime" value="<#if beginTime??>${beginTime}</#if>"></input>
                            <input class="inp1 hide queryParam" type="text" id="endTime" name="endTime" value="<#if endTime??>${endTime}</#if>"></input>
                            <input type="text" id="_createTimeDateRender" class="layui-input" style="width:220px;height: 30px;" value="<#if beginTime??>${beginTime}</#if> ~ <#if endTime??>${endTime}</#if>"/>
                        </div>
                       <#-- <input id="beginTime" name="beginTime" style="width:110px; *width:100px; cursor:pointer;" type="text" class="inp1 Wdate fl queryParam"  onclick="WdatePicker({isShowClear:false,dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}',readOnly:true})" readonly="readonly" value="<#if beginTime??>${beginTime}</#if>"/>
                        <span class="Check_Radio" style="padding:0 5px;margin-top: 0px;">至</span>
                        <input id="endTime" name="endTime" style="width:110px; *width:100px; cursor:pointer;" type="text" class="inp1 Wdate fl queryParam"  onclick="WdatePicker({isShowClear:false,dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')}',readOnly:true})" readonly="readonly" value="<#if endTime??>${endTime}</#if>"/>-->
                    </li>
                </ul>
            </div>
            <div class="btns">
                <ul>
                    <li><a href="#" class="chaxun" title="点击查询" onclick="searchData()">查询</a></li>
                    <li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
                </ul>
            </div>
        </div>

        <div class="ToolBar" id="toolbarDiv">
            <div id="actionDiv" class="tool fr hide" >
                <#--<a href="#" class="NorToolBtn ExportBtn" plain="true" onclick="exportData()">导出</a>-->
                <@actionCheck></@actionCheck>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(function(){

        $(".tool a").hover(function(){
            $(this).find(".more2").show();
        },function(){
            $(this).find(".more2").hide();
        });

        AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#infoOrgCode").val(grid.orgCode);
                $("#eOrgCode").val(grid.eOrgCode);
            }
        });

        if($("#actionDiv").find("a").length > 0) {
            $("#actionDiv").show();
        } else {
            $("#toolbarDiv").remove();
        }

        createTimeDateRender  = $('#_createTimeDateRender').anoleDateRender({
            BackfillType : "1",
            ChoiceType : "1",		// 选择方式（0-起始和结束时间必须都有，1-起始和结束时间有一个即可，2-起始和结束时间随意）
            ShowOptions : {
                TabItems : ["常用", "年", "季", "月", "清空"]
            },
            BackEvents : {
                OnSelected : function(api) {
                    $("#beginTime" ).val(api.getStartDate());
                    $("#endTime").val(api.getEndDate());
                },
                OnCleared : function() {
                    $("#beginTime" ).val('');
                    $("#endTime").val('');
                }
            }
        }).anoleDateApi();

        loadDataList();
    })
    function loadDataList() {
        var queryParams = queryData();

        $('#list').datagrid({
            width:600,
            height:300,
            fitColumns: true,
            remoteSort:false,
            nowrap: true,
            striped: true,
            fit: true,
            rownumbers:true,
            singleSelect: true,
            url:'${rc.getContextPath()}/zhsq/statistics/listEpcOverdueData.json',
            columns:[[
                {field:'reportType', checkbox:true, width:40, hidden:'true'},
                {field:'reportUUID', checkbox:true, width:40, hidden:'true'},
                {field:'instanceId', checkbox:true, width:40, hidden:'true'},
                {field:'reportCode', title:'报告编号', align:'center', width:fixWidth(0.12),formatter:showDataDetail},
                {field:'streetName', title:'所属镇', align:'center',width:fixWidth(0.12),},
                {field:'communityName',title:'所属村', align:'center', width:fixWidth(0.12)},
                {field:'gridName', title:'所属网格', align:'center', width:fixWidth(0.12), formatter: titleFormatter},
                {field:'taskName', title:'超时环节',align:'center', width:fixWidth(0.12), formatter: titleFormatter},
                {field:'transactor', title:'超时人员', align:'center', width:fixWidth(0.12), formatter: titleFormatter},
                {field:'interTime', title:'超时时长', align:'center', width:fixWidth(0.12)}
            ]],
            toolbar:'#jqueryToolbar',
            pagination:true,
            pageSize: 20,
            queryParams:queryParams,
            onLoadError: function () {//数据加载异常
                $('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
            },
            onLoadSuccess: function(data){
                if(data.total == 0) {
                    $('.datagrid-body').eq(1).append('<div class="nodata"></div>');
                }
            }
        });

        //设置分页控件
        var p = $('#list').datagrid('getPager');
        $(p).pagination({
            pageSize: 20,//每页显示的记录条数，默认为
            pageList: [20,30,40,50],//可以设置每页记录条数的列表
            beforePageText: '第',//页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
        });
    }

    function gridTreeClickCallback(gridId,gridName,orgId,infoOrgCode,gridInitPhoto) {
        $("#infoOrgCode").val(infoOrgCode);
        $("#gridId").val(gridId);
        $("#beginTime").val("${beginTime!}");
        $("#endTime").val("${endTime!}");
        searchData();
    }

    //查询
    function searchData() {
        doSearch(queryData());
    }

    function queryData() {
        var searchArray = new Array();

        $("#delEventQueryForm .queryParam").each(function() {
            var val = $(this).val(), key = $(this).attr("name");

            if(isNotBlankString(val) && isBlankString(searchArray[key])){
                searchArray[key] = val;
            }
        });

        return searchArray;
    }

    function doSearch(queryParams){
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams=queryParams;
        $("#list").datagrid('load');
    }

    function exportData() {
        var infoOrgCode = $("#infoOrgCode").val();
        var gridId = $("#gridId").val();
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        var url = '${rc.getContextPath()}/zhsq/statistics/exportEpcOverdueData.jhtml?infoOrgCode='+ infoOrgCode +'&gridId='+ gridId +'&beginTime=' + beginTime +'&endTime=' + endTime + '&reportType=' + $('#reportType').val();

        $.messager.confirm('提示','确定导出数据吗？',function (r) {
            if(r){
                location.href = url;
            }
        });
    }
    function resetCondition() {
        $('#infoOrgCode').val("${infoOrgCode}");
        $('#eOrgCode').val("");
        $('#gridName').val("");
        $('#beginTime').val("${beginTime}");
        $('#endTime').val("${endTime}");
        $('#_createTimeDateRender').val("<#if beginTime??>${beginTime}</#if> ~ <#if endTime??>${endTime}</#if>");
        searchData();
    }
    //疫情防控详情
    function showDataDetail(value, rec, rowIndex) {
        var title = "";
        var reportUUID = rec.reportUUID;
        var instanceId = rec.instanceId;
        var reportType = rec.reportType || '4';
        var listType = '5';
        var extraParams = '&capDB=1&jurisdiction=3';

        if(value){
            title = '<a href="###" title="'+ value +'" onclick="dataDetail(\'' + instanceId + '\', \'' + reportUUID + '\', \'' + listType + '\', \'' + reportType + '\')">'+ value +'</a>';
        }
        return title;
    }
    function dataDetail(instanceId,reportUUID,listType,reportType) {
        var targetModel = 'reportEPC';
        var targetTitle = '疫情防控';

        switch (reportType) {
            case '1':
                targetModel = 'reportTwoVioPre';//两违防治
                targetTitle = '两违防治';
                break;
            case '2':
                targetModel = 'reportHHD';//房屋安全隐患
                targetTitle = '房屋安全隐患';
                break;
            case '3':
                targetModel = 'reportEHD';//企业安全隐患
                targetTitle = '企业安全隐患';
                break;
            case '4':
                targetModel = 'reportEPC';//疫情防控
                targetTitle = '疫情防控';
                break;
            case '5':
                targetModel = 'reportWQ';//流域水质
                targetTitle = '流域水质';
                break;
            case '6':
                targetModel = 'reportMeeting';//三会一课
                targetTitle = '三会一课';
                break;
            case '7':
                targetModel = 'reportPSV';//扶贫走访
                targetTitle = '扶贫走访';
                break;
            case '8':
                targetModel = 'reportRuralHousing';//农村建房
                targetTitle = '农村建房';
                break;
            case '9':
                targetModel = 'reportFFP';//森林防灭火
                targetTitle = '森林防灭火';
                break;
            case '10':
                targetModel = 'reportBusPro';//营商问题
                targetTitle = '营商问题';
                break;
            case '11':
                targetModel = 'reportPPM';//致贫返贫监测
                targetTitle = '致贫返贫监测';
                break;
            case '12':
                targetModel = 'reportPetPer';//信访人员稳控
                targetTitle = '信访人员稳控';
                break;
            case '13':
                targetModel = 'reportMarFac';//烈士纪念设施
                targetTitle = '烈士纪念设施';
                break;
            case '14':
                targetModel = 'reportEHT';//环境卫生问题处置
                targetTitle = '环境卫生问题处置';
                break;
            case '15':
                targetModel = 'reportTOT';//三合一整治
                targetTitle = '三合一整治';
        }
        if(reportUUID){
            var url = '${rc.getContextPath()}/zhsq/'+ targetModel +'/toDetail.jhtml?reportUUID=' + reportUUID + "&listType=" + listType + "&reportType=" + reportType;

            if(instanceId) {
                url += "&instanceId=" + instanceId;
            }
            openJqueryWindowByParams({
                maxWidth: 900,
                maximizable: true,
                title: '查看'+ targetTitle +'信息',
                targetUrl: url
            });
        } else {
            $.messager.alert('警告','请选择需要查看的记录!','warning');
        }
    }

    function titleFormatter(value, rowData, rowIndex) {
        var title = "";

        if(value) {
            title = '<span title="'+ value +'" >'+ value +'</span>';
        }

        return title;
    }
</script>

</body>
</html>