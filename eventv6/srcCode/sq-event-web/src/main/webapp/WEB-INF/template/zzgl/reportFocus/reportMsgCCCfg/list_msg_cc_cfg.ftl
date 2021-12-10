<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>人员配置信息列表</title>
		<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
	</head>
	
	<body class="easyui-layout">
		<div class="MainContent">
			<#include "/zzgl/reportFocus/reportMsgCCCfg/toolbar_msg_cc_cfg.ftl" />
		</div>
		<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
			<table style="width:100%" id="list"></table>
		</div>
		
		<script type="text/javascript">
		    $(function() {
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
					idField:'cfgUUID',
					url:'${rc.getContextPath()}/zhsq/reportMsgCCCfg/listData.json',
					columns:[[
						{field:'cfgUUID', checkbox:true, width:40, hidden:'true'},
						{field:'workflowName', title:'流程图名称', align:'left', width:fixWidth(0.2), formatter: clickFormatter},
						{field:'wfEndNodeName', title:'目标节点', align:'center', width:fixWidth(0.12), formatter: wfNodeNameFormatter},
						{field:'orgChiefLevelName', title:'组织层级', align:'center', width:fixWidth(0.1)},
						{field:'orgCode', title:'组织编码', align:'center', width:fixWidth(0.1)},
						{field:'ccTypeName', title:'配送类型', align:'center', width:fixWidth(0.1)},
						{field:'cfgTypeName', title:'配置类型', align:'center', width:fixWidth(0.1)},
						{field:'cfgValue', title:'配置值', align:'center', width:fixWidth(0.1)},
						{field:'cfgStatus', title:'配置状态', align:'center', width:fixWidth(0.08), formatter: cfgStatusFormatter},
						{field:'remark', title:'备注', align:'center', width:fixWidth(0.2), formatter: titleFormatter}
					]],
					toolbar:'#jqueryToolbar',
					pagination:true,
					pageSize: 20,
					queryParams:queryParams,
					onDblClickRow:function(index,rec) {
						detail(rec.cfgUUID);
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
					displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
					onBeforeRefresh:function(){
						$(this).pagination('loading');
						alert('before refresh');
						$(this).pagination('loaded');
					}*/
				});
		    }
			
			function clickFormatter(value, rec, rowIndex) {
				var title = "";
				
				if(value) {
					title += '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.cfgUUID + '\')">'+ value +'</a>';
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
			
			function cfgStatusFormatter(value, rowData, rowIndex) {
				var valName = "启用";
				
				if(value && value == '0') {
					valName = "停用";
				}
				
				return valName;
			}
			
			function wfNodeNameFormatter(value, rowData, rowIndex) {
				var wfNodeName = value,
					wfStartNodeName = rowData.wfStartNodeName || '';
				
				if(wfStartNodeName) {
					wfNodeName = wfStartNodeName + '->' + wfNodeName;
				}
				
				return wfNodeName;
			}
			
		</script>
	</body>
</html>