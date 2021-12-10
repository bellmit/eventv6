<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>删除事件-数据详情报表</title>

<#include "/component/commonFiles-1.1.ftl" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.min.js"></script>

</head>
<body class="easyui-layout">
<div region="west" title="<span class='easui-layout-title' style='color: #fafafa;'>网格信息域</span>" split="true" style="width:220px;">
<#include "/component/gridTree.ftl" />
</div>
<div id="governAidObjectDiv" region="center" border="false" style="overflow:hidden;">
    <table id="list"></table>
</div>

<div id="jqueryToolbar" style="padding-top:0;">
    <form id="delEventQueryForm">
        <input  type="hidden" id="rootValue" value=""/>
        <input type="hidden" id="infoOrgCode" name="infoOrgCode" class="queryParam" value="<#if infoOrgCode??>${infoOrgCode!}</#if>" />
        <input type="hidden" id="gridId" name="gridId" class="queryParam" value="<#if gridId??>${gridId!}</#if>" />
        <input type="hidden" id="reportType" name="reportType" class="queryParam" value="<#if reportType??>${reportType?c}</#if>" />
        
    <div class="ConSearch" style="padding:10px 10px 10px 10px;">
        <div class="fl" style="width: 330px;">
            <ul>
                <li>查询时间：</li>
                <tr>
                    <td>
                    <input id="beginTime" name="beginTime" style="width:110px; *width:100px; cursor:pointer;" type="text" class="inp1 Wdate fl queryParam"  onclick="WdatePicker({isShowClear:false,readOnly:true})" readonly="readonly" value="<#if beginTime??>${beginTime}</#if>"/>
                    <span class="Check_Radio" style="padding:0 5px;margin-top: 0px;">至</span>
                    <input id="endTime" name="endTime" style="width:110px; *width:100px; cursor:pointer;" type="text" class="inp1 Wdate fl queryParam"  onclick="WdatePicker({isShowClear:false,readOnly:true})" readonly="readonly" value="<#if endTime??>${endTime}</#if>"/>
                    </td>
                </tr>
                </li>
            </ul>
        </div>
        <div class="btns">
            <ul>
                <li><a href="#" class="chaxun" title="点击查询" onclick="searchData()">查询</a></li>
            </ul>
        </div>
    </div>

    <div class="ToolBar">
        <div class="tool fr" >
            <a href="#" class="NorToolBtn ExportBtn" plain="true" onclick="exportData()">导出</a>
        </div>
    </div>
    </form>
</div>
<script type="text/javascript">

    var startInfoOrgId=${startInfoOrgId?c};

    $(function(){

        $(".tool a").hover(function(){
            $(this).find(".more2").show();
        },function(){
            $(this).find(".more2").hide();
        });
        if(startInfoOrgId==-99) {
            $('#governAidObjectDiv').html("<table id='list'><tr><td style='color:red;'>没有网格，无法载入数据</td></tr></table>");
        } else {
            renderTree();
            loadDataList();
        }
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
            url:'${rc.getContextPath()}/zhsq/timeApplicationReportFordelEvent/listData.json?t='+Math.random(),
            columns:[[

                {field:'GRID_NAME', title:'网格名称', align:'center',width:fixWidth(0.3)},
                {field:'TOTAL_NUM_DELTED',title:'<@block name="totalNumDeletedFieldTitle">总删除量</@block>', align:'center', width:fixWidth(0.12)},
                {field:'TOTAL_NUM_REPORTED', title:'总上报量', align:'center', width:fixWidth(0.12),},
                {field:'PERCENT_DELETED', title:'<@block name="percentDeletedFieldTitle">删除率</@block>', align:'center', width:fixWidth(0.12)},
                {field:'PERCENT_TAKING', title:'占比率', align:'center', width:fixWidth(0.12)}
            ]],
            toolbar:'#jqueryToolbar',
            pagination:false,
            queryParams:queryParams
        });
        //设置分页控件
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
        var url = '${rc.getContextPath()}/zhsq/timeApplicationReportFordelEvent/exportData.jhtml?infoOrgCode='+ infoOrgCode +'&gridId='+ gridId +'&beginTime=' + beginTime +'&endTime=' + endTime + '&reportType=' + $('#reportType').val();

        $.messager.confirm('提示','确定导出数据吗？',function (r) {
            if(r){
                location.href = url;
            }
        });
    }

</script>

</body>
</html>