<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>房屋安全隐患我的阅办列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/standard_common_files-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout">
<div class="MainContent">
	<#include "/zzgl/reportFocus/houseHiddenDanger/toolbar_hhd_msgReading.ftl" />
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
			idField:'msgId',
			url:'${rc.getContextPath()}/zhsq/reportHHD/listReportFocusData.json',
			columns:[[
				{field:'msgId', checkbox:true, width:40, hidden:'true'},
				{field:'reportCode', title:'报告编号', align:'left', width:fixWidth(0.15), formatter: clickFormatter},
				{field:'msgSenderName', title:'报告人姓名', align:'center', width:fixWidth(0.1), formatter: titleFormatter},
				{field:'msgContent', title:'报告内容', align:'center', width:fixWidth(0.4), formatter: titleFormatter},
				{field:'msgSendTimeStr', title:'报告时间', align:'center', width:fixWidth(0.12)},
				{field:'msgReceiveStatusName', title:'阅读状态', align:'center', width:fixWidth(0.08)}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
			onDblClickRow:function(index,rec) {
				var instanceId = rec.instanceId || '';
				
				detail(rec.reportUUID, instanceId, rec.msgId, rec.msgReceiveStatus);
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
	
	function clickFormatter(value, rec, rowIndex) {
		var title = "";
		
		if(value) {
			var instanceId = rec.instanceId || '';
			
			title += '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.reportUUID + '\', \'' + instanceId + '\', \'' + rec.msgId + '\', \'' + rec.msgReceiveStatus + '\')">'+ value +'</a>';
		}
		
		return title;
	}
	
	function titleFormatter(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'" >'+ value +'</span>';
		}
		
		return title;
	}
	
	function dateFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 10) {
			value = value.substring(0, 10);
		}
		
		return value;
	}
	
</script>
</body>
</html>