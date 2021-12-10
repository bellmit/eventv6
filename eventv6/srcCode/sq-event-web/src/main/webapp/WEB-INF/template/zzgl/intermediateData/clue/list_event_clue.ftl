<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>线索事件列表</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>
<body class="easyui-layout">

<div class="MainContent">
	<#include "toolbar_event_clue.ftl" />
</div>

<div id="eventWechatDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
	<table id="list" style="width:100%"></table>
</div>

<script type="text/javascript">
	$(function() {
		loadDataList();
	});
	
	function loadDataList() {
		var queryParam = fetchQueryParams();
		
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
			idField:'eventWechatId',
			url:'${rc.getContextPath()}/zhsq/eventWechat/listData.json',
			columns:[[
				{field:'eventWechatId',checkbox:true,width:40,hidden:'true'},
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value == null){
							value = "";
						}
						var f = '<a class="eName" href="###" title="'+ rec.eventName +'" onclick="detail(' + rec.eventWechatId + ')")>'+value+'</a>';
						return f;
					}	
				},
				{field:'occurred',title:'事发地址',align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
				{field:'happenTimeStr',title:'事发时间',align:'center',width:fixWidth(0.1),sortable:true,
					formatter:function(value, rowData, rowIndex){
						if(value != null && value.length >= 10){
							value = value.substring(0,10);
						}
						return value;
					}
				},
				{field:'contactUser',title:'上报人员',align:'center',width:fixWidth(0.1),sortable:true, formatter: titleFormatter},
                {field:'tel',title:'联系电话',align:'center',width:fixWidth(0.1),sortable:true},
                {field:'gridPath',title:'所属网格',align:'center',width:fixWidth(0.15),sortable:true, formatter: titleFormatter},
                {field:'status',title:'状态',align:'center',width:fixWidth(0.1),sortable:true, formatter: statusFormatter}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParam,
			onDblClickRow:function(index,rec){
				detail(rec.eventWechatId);
			},
			onSelect: function(index,rec) {
				authority(rec);
			},
			onLoadSuccess:function(data){
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
			onLoadError:function(){
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
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
	
	function titleFormatter(value, rec, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'">'+ value +'</span>';
		}
		
		return title;
	}
	
	function statusFormatter(status, rec, rowIndex) {
		var statusObj = {'01':'待审核', '02':'已上报', '03':'被驳回'};
		
		return statusObj[status];
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result, isCurrent){
		closeMaxJqueryWindow();
		
		if(result) {
			DivShow(result);
		}
		
		searchData(isCurrent);
	}
</script>
</body>
</html>