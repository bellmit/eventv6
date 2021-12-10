<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>流程环节配置设置信息列表</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>

</head>
<body class="easyui-layout">

<div class="MainContent">
	<#include "/zzgl/handlerCfg/toolbar_wf_proc_cfg.ftl" />
</div>

<div id="wfProcCfgContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
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
			remoteSort:false,
			rownumbers:true,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'wfpcId',
			url:'${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/listProcCfgData.json',
			columns:[[
				{field:'wfpcId',checkbox:true,width:40,hidden:'true'},
				{field:'regionName',title:'所属地域', align:'left',width:fixWidth(0.2),sortable:true,
					formatter:function(value,rec,rowIndex){
						var f = '<a href="###" title='+ rec.regionName +' onclick="detail('+ rec.wfpcId +')">'+value+'</a>&nbsp;';
						return f;
					}
				},
				{field:'taskCodeName',title:'流程环节', align:'center',width:fixWidth(0.2),sortable:true},
				{field:'eventCodeNames',title:'事件类别', align:'center',width:fixWidth(0.5),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{wfcId:<#if handlerWfCfg.wfcId??>${handlerWfCfg.wfcId?c}<#else>-1</#if>},
			onDblClickRow:function(index,rec){
				detail(rec.wfpcId);
			},
			onLoadSuccess: function(data){//所属组织左对齐
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
    
	function flashData(msg, isCurrentPage){
		if(msg){
			DivShow(msg);
			try {
				closeMaxJqueryWindow();
			} catch(e) {}
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