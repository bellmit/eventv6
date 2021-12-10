<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>两违防治列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/standard_common_files-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout">
<div class="MainContent">
	<#include "/zzgl/reportFocus/twoVioPre/toolbar_twoVioPre.ftl" />
</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>

<script type="text/javascript">
    $(function() {
    	addIcons();
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
			idField:'reportId',
			url:'${rc.getContextPath()}/zhsq/reportTwoVioPre/listReportFocusData.json',
			columns:[[
				{field:'reportId', checkbox:true, width:40, hidden:'true'},
				{field:'reportCode', title:'报告编号', align:'left', width:fixWidth(0.18), formatter: clickFormatter},
				{field:'reporterName', title:'报告人姓名', align:'center', width:fixWidth(0.14)},
				{field:'personInvolved', title:'业主姓名', align:'center', width:fixWidth(0.13)},
				{field:'reportTimeStr', title:'报告时间', align:'center', width:fixWidth(0.13)},
				{field:'occurred', title:'发生地址', align:'center', width:fixWidth(0.2), formatter: titleFormatter},
				<#if listType??>
					<#if listType == '3' || listType == '5'>
						{field:'curUserName', title:'当前办理人员', align:'center', width:fixWidth(0.14), formatter: curUserFormatter},
					<#elseif listType == '6'>
						{field:'instanceEndTimeStr', title:'办结时间', align:'center', width:fixWidth(0.12), formatter: hourFormatter},
					</#if>
				</#if>
				{field:'reportStatusName', title:'报告状态', align:'center', width:fixWidth(0.08)}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
			onSelect: btnAuthority,
			onDblClickRow:function(index,rec) {
				var instanceId = rec.instanceId || '';
				
				detail(rec.reportUUID, instanceId, $('#listType').val());
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
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
		});
    }
</script>
</body>
</html>