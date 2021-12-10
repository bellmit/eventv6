<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件任务列表</title>
	<#include "/component/commonFiles-1.1.ftl" />
</head>
<body class="easyui-layout">
<div class="MainContent">
	<#include "/zzgl/event/eventTask/toolbar_event_task.ftl" />
</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>

<script type="text/javascript">
    $(function(){
    	loadDataList();
    });
    
    function loadDataList(){
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
			idField:'CUR_TASK_ID',
			url:'${rc.getContextPath()}/zhsq/event/eventTask/listEventTaskData.json',
			columns:[[
				{field:'CUR_TASK_ID',checkbox:true, hidden:true},
				{field:'CUR_ACTIVITY_NAME',title:'环节名称', align:'left',width:fixWidth(0.1),sortable:true},
				{field:'CUR_TASK_CREATE_TIME',title:'任务到达时间', align:'center',width:fixWidth(0.2),sortable:true},
				{field:'CUR_USER_NAME',title:'当前办理人', align:'center',width:fixWidth(0.3),sortable:true, formatter: titleFormatter},
				{field:'EVENT_NAME',title:'事件标题', align:'center',width:fixWidth(0.2),sortable:true, 
					formatter:function(value,rec,rowIndex){
						if(value && value.length > 0) {
							return '<a class="eName" href="###" title="'+ rec.EVENT_NAME +'" onclick=detail('+ rec.EVENT_ID+ ','+rec.INSTANCE_ID+')>'+value+'</a>';
						}
					}	
				},
				{field:'GRID_PATH',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{eventType:$("#eventType").val(),infoOrgCode:$("#startInfoOrgCode").val()},
			onSelect:function(index,rec){
			},
			onDblClickRow:function(index,rec){
				detail(rec.EVENT_ID, rec.INSTANCE_ID);
			},
			onLoadError: errorResult,
			onLoadSuccess: successResult  
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
	
	function titleFormatter(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'">'+ value +'</span>';
		}
		
		return title;
	}
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/zzgl/event/workflow/select_user.ftl" />

</body>
</html>