<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>南昌扫黑除恶-线索管理列表</title>
    <link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
    <#include "/component/commonFiles-1.1.ftl" />
    <script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout">
<div class="MainContent">
<#include "zzgl/sweepBlackRemoveEvil/eventSBREClue/toolbar_eventSBREClue.ftl" />
</div>
<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
    <table style="width:100%" id="list"></table>
</div>

<script type="text/javascript">
    $(function(){
        loadDataList();
    });

    function loadDataList(){
        var queryParams = queryData();

        $('#list').datagrid({
            width:600,
            height:300,
            nowrap: true,
            rownumbers:true,
            remoteSort:false,
            striped: true,
            fit: true,
            fitColumns: true,
            singleSelect: true,
            idField:'clueId',
            url:'${rc.getContextPath()}/zhsq/eventSBREClue/listData.json',
            columns:[[
                {field:'clueId',checkbox:true,width:40,hidden:'true'},
                {field:'gridPath',title:'发生区域', align:'center',width:fixWidth(0.25),sortable:true, formatter: titleFormatter},
                {field:'clueTitle',title:'线索标题', align:'center',width:fixWidth(0.2),sortable:true, formatter: clickFormatter},
                {field:'clueSourceName',title:'线索来源', align:'center',width:fixWidth(0.15),sortable:true},
                {field:'importantDegreeName',title:'重要程度', align:'center',width:fixWidth(0.1),sortable:true},
                {field:'createDateStr',title:'登记时间', align:'center',width:fixWidth(0.1),sortable:true},
                <#if listType??>
	                <#if listType != 1>
		                {field:'handleDateStr',title:'办理期限', align:'center',width:fixWidth(0.1),sortable:true},
		                {field:'creatorOrgName',title:'登记单位', align:'center',width:fixWidth(0.15),sortable:true, formatter: titleFormatter},
		                <#if listType == 4>
		                	{field:'curOrgName',title:'当前办理单位', align:'center',width:fixWidth(0.15),sortable:true, formatter: titleFormatter},
		                </#if>
	                </#if>
                </#if>
                {field:'clueStatusName',title:'状态', align:'center',width:fixWidth(0.1),sortable:true}
            ]],
            toolbar:'#jqueryToolbar',
            pagination:true,
            pageSize: 20,
            queryParams:queryParams,
            onSelect: function(index, rec) {
            	var clueStatus = rec.clueStatus;
            	if(clueStatus == '04') {
            		$('#edit').hide();
            	} else {
            		$('#edit').show();
            	}
            },
            onDblClickRow: function(index, rec) {
                detail(rec.clueId, null, rec.isEncrypt);
            },
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
            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
        });
    }

    function titleFormatter(value, rowData, rowIndex) {
        var title = "";

        if(value) {
            title = '<span title="'+ value +'" >'+ value +'</span>';
        }

        return title;
    }

    function clickFormatter(value, rec, rowIndex) {
        var title = "",
            clueTitle = rec.clueTitle,
            listType = $("#listType").val();

        if(clueTitle) {
        	var handlePic = "",
        		handleDateFlag = rec.handleDateFlag;
        	
        	if(handleDateFlag == '2'){
				handlePic = '<i title="将到期" class="ToolBarDue" style="padding-right: 5px;"></i>';
			} else if(handleDateFlag == '3'){
				handlePic = '<i title="已过期" class="ToolBarOverDue" style="padding-right: 5px;"></i>';
			}
			
			clueTitle = handlePic + clueTitle;
			
            title += '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.clueId + '\', null, \''+ rec.isEncrypt +'\')">'+ clueTitle +'</a>';
        }

        return title;
    }

</script>
</body>
</html>