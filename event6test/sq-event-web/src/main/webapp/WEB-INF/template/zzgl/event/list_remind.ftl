<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>督办列表</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/common.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
</head>
<body class="easyui-layout">
	<div class="easyui-layout" fit="true" border="false">
		<div id="jqueryToolbar" style="padding:5px;height:auto">
			<div style="margin-top:5px;">
				<a href="###" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add()">新增</a>
			</div>
		</div>
		<div region="center" border="false">
			<table id="list"></table>
		</div>
	</div>
</body>

<script type="text/javascript">
	var idStr = "";
	var typeVal = "";
	var cWidth = document.body.clientWidth-670;
	var eventId = "${eventId}";
	$(function(){
		$('#ttip').css('margin-left',cWidth);
		$('#list').datagrid({
			width:600,
			height:300,
			nowrap: false,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			singleSelect: true,
			idField:'eventId',
			url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/remindDate.json',
			columns:[[
				{field:'eventId',title:'ID', align:'center',hidden:true},//
                {field:'remindedUserName',title:'被督办人', align:'center',width:105,sortable:true},
                {field:'remarks',title:'督办意见', align:'center',width:350,sortable:true,
                formatter:function(value,rowData,rowIndex){
						if(value==null)return "";
						if(value.length>20){
							value = value.substring(0,20);
						}
						return value;
					}},
				{field:'remindDate',title:'督办时间', align:'center',width:220,sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{eventId:eventId},
			onSelect:function(index,rec){
				idStr=rec.eventId;
				typeVal=rec.type;
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
		
	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddRemind.jhtml?eventId=${eventId}&instanceId=${instanceId}&taskId=${taskId}';
		showCustomEasyWindow("督办信息", url, 500, 270);
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result){
		closeCustomEasyWin();
		$.messager.alert('提示', result, 'info');
		$("#list").datagrid('load');
	}
</script>
<#include "/component/customEasyWin.ftl" />
</body>
</html>