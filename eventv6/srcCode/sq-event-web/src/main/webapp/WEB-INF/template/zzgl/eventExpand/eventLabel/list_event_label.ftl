<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件标签列表</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>
<body class="easyui-layout">

<div class="MainContent">
	<#include "/zzgl/eventExpand/eventLabel/toolbar_event_label.ftl" />
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
			idField:'eventVerifyId',
			url:'${rc.getContextPath()}/zhsq/event/eventLabelController/listData.json',
			columns:[[
				{field:'labelId',checkbox:true,width:40,hidden:'true'},
				{field:'labelName',title:'标签名称',align:'center',width:fixWidth(0.4),sortable:true,
				    formatter:function(value,rec,rowIndex){
						if(value == null){
							value = "";
						}
						var f = '<a class="eName" href="###" title="'+ rec.eventName +'" onclick="detail(' + rec.labelId + ')")>'+value+'</a>';
						return f;
					}
				},
                {field:'labelModelName',title:'标签所属模块',align:'center',width:fixWidth(0.3),sortable:true},
                {field:'gridPath',title:'所属区域',align:'center',width:fixWidth(0.3),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParam,
			onDblClickRow:function(index,rec){
				detail(rec.eventVerifyId);
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
				$(this).pagination('loaded');
			}*/
		});
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