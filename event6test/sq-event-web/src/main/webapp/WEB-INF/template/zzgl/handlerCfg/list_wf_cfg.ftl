<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>业务配置列表</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>
<body class="easyui-layout">

<div class="MainContent">
	<#include "/zzgl/handlerCfg/toolbar_wf_cfg.ftl" />
</div>

<div id="wfCfgDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
	<table id="list" style="width:100%"></table>
</div>

<script type="text/javascript">
	$(function(){
		loadDataList();
	});
	
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
			idField:'wfcId',
			url:'${rc.getContextPath()}/zhsq/event/eventHandlerWfCfg/listWfCfgData.json',
			columns:[[
				{field:'wfcId',checkbox:true,width:40,hidden:'true'},
				{field:'regionName',title:'所属地域', align:'left',width:fixWidth(0.3),sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value == null){
							value = "";
						}
						var f = '<a class="eName" href="###" title="'+ rec.regionName +'" onclick="detail(' + rec.wfcId + ')")>'+value+'</a>';
						return f;
					}	
				},
				{field:'bizTypeName',title:'业务类型',align:'center',width:fixWidth(0.2),sortable:true},
				{field:'wfCfgName',title:'配置信息',align:'center',width:fixWidth(0.4),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{},
			onDblClickRow:function(index,rec){
				detail(rec.wfcId);
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
	
	//-- 供子页调用的重新载入数据方法
	function flashData(msg, isCurrentPage){
		if(msg) {
			try {
				closeMaxJqueryWindow();
			} catch(e) {}
			
			DivShow(msg);
		}
		
		$('#list').datagrid('clearSelections');	//清除选择的行
		
		if(isCurrentPage) {
			$("#list").datagrid('reload');	//重新加载当前页面
		} else {
			$("#list").datagrid('load');	//重新加载事件列表
		}
	}
</script>
</body>
</html>