<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>联合办理/联席交办列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout">
	<div class="MainContent">
		<#include "/zzgl/event/eventExtend/amoy/toolbar_eventEx.ftl" />
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
				idField:'eventId',
				url:'${rc.getContextPath()}/zhsq/eventExtend/listEventExData.json',
				columns:[[
					{field:'eventId',checkbox:true,width:40,hidden:'true'},
					{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true, formatter: clickFormatter},
					{field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
					{field:'handleDateStr',title:'办结期限', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
	                {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.2),sortable:true},
					{field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
					{field:'eventStatusName',title:'当前状态', align:'center',width:fixWidth(0.12),sortable:true},
					{field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
					{field:'integrityScore',title:'事件完整度(%)', align:'center',width:fixWidth(0.1),sortable:true}
				]],
				toolbar:'#jqueryToolbar',
				pagination:true,
				pageSize: 20,
				queryParams:queryParams,
				onDblClickRow:function(index,rec){
					detail(rec.eventId);
				},
				onLoadError: function () {//数据加载异常
	        		$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
	    		},
				onLoadSuccess: function(data){//案件标题内容左对齐
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
			var title = "",
				operate = "",
				listType = $("#listType").val();
			
			if(value) {
				switch(listType) {
					case '2': {//我的被督查督办
						operate = '<div class="OperateNotice" style="display:none"><div class="operate"><ul><li class="duban" onclick="addTimeApplication('+ rec.eventId + ', 2);" ) >申请延时</li></ul><div class="arrow"></div></div></div>';
						break;
					}
					case '3': {//辖区内超时
						operate = '<div class="OperateNotice" style="display:none"><div class="operate"><ul><li class="duban" onclick="addTimeApplication('+ rec.eventId + ', 1);" ) >督查督办</li></ul><div class="arrow"></div></div></div>';
						break;
					}
					case '4': {//待办
						operate = '<div class="OperateNotice" style="display:none"><div class="operate"><ul><li class="duban" onclick="addTimeApplication('+ rec.eventId + ', 3);" ) >申请延时</li></ul><div class="arrow"></div></div></div>';
						break;
					}
				}
				
				title += operate + '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.eventId + '\')">'+ value +'</a>';
			}
			
			return title;
		}
		
		function titleFormatter(value, rowData, rowIndex) {
			var title = "";
			
			if(value) {
				title = '<span title="'+ value +'">'+ value +'</span>';
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