<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件导入表数据列表</title>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>
<body class="easyui-layout">
	<div class="MainContent">
		<#include "/zzgl/event/eventImport/eventImport4File/toolbar_event_i4f.ftl" />
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
			idField:'ROW_ID',
			url:'${rc.getContextPath()}/zhsq/event/importEvent4File/listData.json',
			columns:[[
				{field:'ROW_ID',checkbox:true,width:40,hidden:'true'},
				{field:'EVENT_NAME',title:'事件标题', align:'left',width:fixWidth(0.25),sortable:true, formatter: eventNameFormatter},
				{field:'HAPPEN_TIME',title:'事发时间', align:'center',width:fixWidth(0.15),sortable:true},
				{field:'TYPE_NAME',title:'事件分类', align:'center',width:fixWidth(0.15),sortable:true},
				{field:'INFO_ORG_CODE',title:'地域编码', align:'center',width:fixWidth(0.1),sortable:true},
				{field:'MSG_WRONG_STR',title:'失败原因', align:'center',width:fixWidth(0.25),sortable:true, formatter: titleFormatter},
				{field:'STATUS_',title:'导入状态', align:'center',width:fixWidth(0.1),sortable:true, formatter: statusFormatter}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{},
			onDblClickRow:function(index,rec){
				detailRecord(rec.ROW_ID);
			},
			onLoadError: errorResult,
			onLoadSuccess: function(data) {//事件标题内容左对齐
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
	
	function eventNameFormatter(value, rec, rowIndex) {
		var title = value;
		
		if(rec.EVENT_ID && value) {
			title = '<a href="###" title="'+ value +'" style="text-decoration:none;" onclick="detail('+rec.EVENT_ID+')">'+value+'</a>';
		} else if(value) {
			title = titleFormatter(value, rec, rowIndex);
		}
		
		return title;
	}
	
	function statusFormatter(value, rec, rowIndex) {
		var title = value;
		
		if(value) {
			switch(value) {
				case '0' : {
					title = "导入失败"; break;
				}
				case '1' : {
					title = "导入成功"; break;
				}
				case '2' : {
					title = "待导入"; break;
				}
			}
		}
		
		return title;
	}
	
	function titleFormatter(value, rec, rowIndex) {
		var title = value;
		
		if(value) {
			title = '<span title="'+ value +'">'+ value +'</span>';
		}
		
		return title;
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result, isCurrent){
		try{
			closeMaxJqueryWindow();
		}catch(e){}
		DivShow(result);
		searchData(isCurrent);
	}
</script>

</body>
</html>