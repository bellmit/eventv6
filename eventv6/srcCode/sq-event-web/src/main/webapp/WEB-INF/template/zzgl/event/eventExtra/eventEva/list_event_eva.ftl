<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>归档事件需要评价列表</title>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/basic/yancheng/level-star.css" />
	<#include "/component/standard_common_files-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout" id="easyuiLayoutBody">
	<div class="MainContent">
		<#include "/zzgl/event/eventExtra/eventEva/toolbar_event_eva.ftl" />
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
			singleSelect: false,
			idField:'eventId',
			url:'${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/listEventEvaData.json',
			columns:[[
				{field:'eventId',checkbox:true,width:40},
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2), formatter: clickFormatter},
				{field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.1), formatter: dateFormatter},
                {field:'finTimeStr',title:'办结时间', align:'center',width:fixWidth(0.1)},
                {field:'streetEvaLevel',title:'镇级评价', align:'center',width:fixWidth(0.15), formatter: starFormatter},
                {field:'districtEvaLevel',title:'区级复评', align:'center',width:fixWidth(0.15), formatter: starFormatter}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
			onDblClickRow:function(index,rec){
				detail(rec.eventId);
			},
			onSelect: selectItem,
			onUnselect: selectItem,
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
			eventName = rec.eventName;
		
		if(eventName) {
			title += '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.eventId + '\')">' + eventName +'</a>';
		}
		
		return title;
	}
	
	function dateFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 10) {
			value = value.substring(0, 10);
		}
		
		return value;
	}
	
	function starFormatter(value, rowData, rowIndex) {
		var starContent = "";
		
		if(value) {
			var index = 0, starTotal = parseInt(value, 10), star = "", startColor = null;
			
			while(index < 5) {
				if(index < starTotal) {
					startColor = 'star-yellow';
					
				} else {
					startColor = 'star-gray';
				}
				
				star += '<i class="' + startColor + '"></i>';
				index++;
			}
			
			starContent = '<div class="level-star">' + star + '</div>';
		}
		
		return starContent;
	}
</script>
</body>
</html>