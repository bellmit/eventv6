<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>新疆纪委扶贫问题列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout">
<div class="MainContent">
	<#include "/zzgl/accountabilityEnforcement/toolbar_accountEnforce.ftl" />
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
			idField:'probId',
			url:'${rc.getContextPath()}/zhsq/accountabilityEnforcement/listData.json',
			columns:[[
				{field:'probId',checkbox:true,width:40,hidden:'true'},
				{field:'probTitle',title:'问题标题', align:'left',width:fixWidth(0.2),sortable:true, formatter: clickFormatter},
				{field:'sourceName',title:'问题线索来源', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
                {field:'regionPath',title:'所属行政辖区', align:'center',width:fixWidth(0.25),sortable:true, formatter: titleFormatter},
				{field:'violationDateStr',title:'违纪违规时间', align:'center',width:fixWidth(0.1),sortable:true},
				{field:'violationObjTypeName',title:'对象类别', align:'center',width:fixWidth(0.1),sortable:true}
				<#if listType?? && (listType == 5 || listType == 3)>
				,{field:'curNodeNameZH',title:'当前状态', align:'center',width:fixWidth(0.1),sortable:true, formatter: titleFormatter}
				</#if>
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
			onDblClickRow:function(index,rec){
				detail(rec.probId);
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
	
	function clickFormatter(value, rec, rowIndex) {
		var title = "",
			probTitle = rec.probTitle,
			listType = $("#listType").val();
		
		if(probTitle) {
			title += '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.probId + '\')">'+ probTitle +'</a>';
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