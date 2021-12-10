<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件消息列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout">
<div class="MainContent">
	<#include "/zzgl/event/eventExtra/toolbar_event_msg.ftl" />
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
			url:'${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/listMsgData.json',
			columns:[[
				{field:'msgId',checkbox:true,width:40,hidden:'true'},
				{field:'msgContent',title:'消息内容', align:'left',width:fixWidth(0.2),sortable:true},
				{field:'sendTimeStr',title:'发送时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
                {field:'sendTimeStr',title:'发送人员', align:'center',width:fixWidth(0.2),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
			onDblClickRow:function(index,rec){
				detail(rec.msgId);
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
	
	function dateFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 10) {
			value = value.substring(0, 10);
		}
		
		return value;
	}
</script>
</body>
</html>