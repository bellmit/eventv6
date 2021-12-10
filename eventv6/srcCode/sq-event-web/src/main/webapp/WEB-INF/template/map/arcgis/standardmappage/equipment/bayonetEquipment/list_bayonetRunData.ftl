<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>立体防控——卡口设备——运行数据</title>
	<#include "/component/commonFiles-1.1.ftl" />
	
	<style type="text/css">
		.MainContent{padding: 0;}
	</style>
</head>
<body class="easyui-layout">

<div class="MainContent">
	<#include "toolbar_bayonetRunData.ftl" />
</div>

<div region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
	<table id="list" style="width:100%"></table>
</div>

<script type="text/javascript">
	$(function(){
		loadDataList();
		$("#list").hide();
	})
	
	function loadDataList() {
		$('#list').datagrid({
			width:600,
			height:600,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,//让列宽自适应窗口宽度
			singleSelect: true,
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/listBayonetRunData.json',
			columns:[[
				{field:'plateInfo',title:'车牌号码', align:'left',width:fixWidth(0.06),sortable:true, formatter:function(value,rec,rowIndex){
					var plateLabel = "";
					
					if(rec.plateTextColor) {
						plateLabel = '<label style="color:'+ rec.plateTextColor +'">'+ value +'</label>';
					} else {
						plateLabel = value;
					}
					if(rec.plateColorValue) {
						plateLabel = '<span style="background-color:'+ rec.plateColorValue +'; padding: 5px;">'+ plateLabel +'</span>';
					}
					
					return plateLabel;
				}},
				{field:'crossingName',title:'卡口名称',align:'center',width:fixWidth(0.12),sortable:true},
                {field:'passTime',title:'经过时间',align:'center',width:fixWidth(0.11),sortable:true},
                {field:'laneName',title:'车道名称',align:'center',width:fixWidth(0.05),sortable:true},
                {field:'directionName',title:'行驶方向',align:'center',width:fixWidth(0.05),sortable:true},
                {field:'alarmActionName',title:'违法行为',align:'center',width:fixWidth(0.05),sortable:true},
                {field:'plateTypeName',title:'车牌类型',align:'center',width:fixWidth(0.08),sortable:true},
                {field:'vehicleTypeName',title:'车辆类型',align:'center',width:fixWidth(0.09),sortable:true},
                {field:'autoBrandName',title:'车辆品牌',align:'center',width:fixWidth(0.05),sortable:true},
                {field:'plateAttach',title:'归属地',align:'center',width:fixWidth(0.1),sortable:true},
                {field:'acsStatus4',title:'回放',align:'center',width:fixWidth(0.05),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{bizId: $("#bizId").val(), searchType: "1"},
			onLoadSuccess:function(data){
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
			onLoadError: errorResult
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
	
	//数据列表查询方法
	function doSearch(queryParams) {
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');
	}
	
	//数据列表重置方法
	function resetCondition() {
		$resetCondition();
	}
</script>
</body>
</html>