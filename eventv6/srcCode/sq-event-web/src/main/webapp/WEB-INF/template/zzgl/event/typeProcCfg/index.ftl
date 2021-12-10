<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>涉及案件列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
<style type="text/css">
.combo-arrow{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.combo-arrow:hover{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.combo-arrow{opacity:1;}
.textbox-icon{opacity:1;}
.combo{vertical-align:top;}
.combo:hover{border:1px solid #7ecef4; box-shadow:#7ecef4 0 0 5px;}
</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="bizId" name="bizId" value="" />
<input type="hidden" id="bizName" value="" />

<div class="MainContent">
	<#include "/zzgl/event/typeProcCfg/dataGridToolbar.ftl" />
</div>

<div id="relatedEventsContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
	<table id="list"></table>
</div>

<script type="text/javascript">
    var _win_opt = {
    	"padding_left" : 10,
    	"padding_right" : 10,
    	"padding_top" : 10,
    	"padding_bottom" : 10
    };
    
    var relatedEventsList = null;
    var regionCode = "<#if regionCode??>${regionCode}</#if>";
    var gridId = <#if gridId??>${gridId?c}<#else>-99</#if>;
    
    $(function(){
	     loadDataList();
	});
	
    function loadDataList(){
    	var queryParams = {regionCode: regionCode};
    	
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: false,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'reId',
			url:'${rc.getContextPath()}/zhsq/event/eventTypeProcCfgController/listData.json',
			columns:[[
				{field:'regionName',title:'地域名称', align:'left',width:fixWidth(0.2),sortable:true},
				{field:'regionCode',title:'地域编码', align:'center',width:fixWidth(0.2),sortable:true},
				{field:'typeName',title:'事件类型', align:'center',width:fixWidth(0.2),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
			onSelect:function(index,rec){
				idStr=rec.reId;
			},
			onDblClickRow:function(index,rec){
				showDetailRow(rec.reId);
			},
			onLoadSuccess: function(data){
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
				
				var panel = $(this).datagrid('getPanel');  
				var tr = panel.find('div.datagrid-body tr');  
	            tr.each(function(i){
	            	var td = $(this).children('td[field="reName"]');
	                td.children("div").css({
	                	"text-align": "left"  
	                });
	            });  
	            
	            relatedEventsList = $('#list').datagrid("getRows"); 
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
    
	function searchData(b) {
		var a = new Array();
		a["regionCode"] = $("#regionCode").val();
    	a["type"] = $("#eventType").val();
		doSearch(a);
	}
	
	function doSearch(queryParams) {
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');			//重新加载事件列表
	}
	
</script>

</body>
</html>