<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><#if extType == '1'>补充信息记录<#elseif extType == '2'>催单记录</#if></title>
	<#include "/component/commonFiles-1.1.ftl" />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
</head>
<body class="easyui-layout">
<div class="MainContent">
	<div id="jqueryToolbar"></div>
</div>
<div id="extRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
	<table style="width:100%" id="list"></table>
</div>
<script type="text/javascript">
	$(function() {
		loadDataList('${extType}');
	});
	function loadDataList(extType){
		var doPerson = '<#if extType == '1'>处理人员<#elseif extType == '2'>办理人员</#if>';
		var doDate = '<#if extType == '1'>处理时间<#elseif extType == '2'>催单时间</#if>';
		var doContent = '<#if extType == '1'>补充信息<#elseif extType == '2'>催单意见</#if>';
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
			idField:'extUUId',
			url : '${rc.getContextPath()}/zhsq/reportFeedback/listExtData.json',
			columns:[[
				{field:'extUUId',title:'ID', align:'center',hidden:true},
				{field:'seUUID',title:'seUUID', align:'center',hidden:true},
				{field:'doPerson',title:doPerson, align:'center',width:fixWidth(0.2),sortable:true},
				{field:'doDate',title:doDate, align:'center',width:fixWidth(0.2),sortable:true},
				{field:'doContent',title:doContent, align:'center',width:fixWidth(0.2),sortable:true,formatter: titleFormatter}
			]],
			toolbar:'#jqueryToolbar',
			pagination:false,
			pageSize: 20,
			queryParams: {'seUUId': '${seUUId}','extType':'${extType}'},
			onDblClickRow:function(index,rec){
			},
			onLoadError: function () {//数据加载异常
				$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
			},
			onLoadSuccess: function(data){//事件标题内容左对齐
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			}
		});
	}

	function titleFormatter(value, rec, rowIndex) {
		var title = "";

		if(value) {
			title = "<span title='"+ value +"'>"+ value +"</span>";
		}

		return title;
	}

	/*function dateFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 10) {
			value = value.substring(0,10);
		}

		return value;
	}*/
</script>
</body>

</html>