<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<#include "/component/AnoleDate.ftl">
	<style type="text/css">
		.inp1 {width:100px;}
	</style>
</head>
<body class="easyui-layout">

	<div id="jqueryToolbar">
		 <div class="ConSearch">
			<form id="searchForm">
			
			 <div class="noprint tool" style="position: fixed; top: 5px; right: 5px;">  
	             <a href="#" class="NorToolBtn PrintBtn" onclick="window.print();">打印</a>
             </div>
	        
			</form>
		</div>
		<div class="h_5" id="TenLineHeight1"></div>
	</div>
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		loadList(); //加载列表
	});
	
	//加载列表
	function loadList() {
		$('#list').datagrid({
		//	rownumbers: true, //行号
			fit: true,
			fitColumns: true, //自适应宽度
			nowrap: false,
			striped: true,
			singleSelect: true,
			scrollbarSize :0,		
			idField:'probId',
			url: '${rc.getContextPath()}/zhsq/unitProb/unitListData.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
			    {field:'probId',checkbox:true,hidden:'true'},
			    {field:'probTitle', title:'问题标题', align:'left', width:100},
                {field:'professionType', title:'单位级别', align:'center', width:100},	
				{field:'violationDate', title:'违纪违规时间', align:'center', width:100},
				{field:'disciplinaryPunishment', title:'纪律处分', align:'center', width:100},
				{field:'procType', title:'问责处理类型', align:'center', width:100},
				{field:'blameFlag', title:'是否问责', align:'center', width:100},
				{field:'amountInvolved', title:'涉案金额', align:'center', width:100},
				{field:'violationMoney', title:'违纪金额', align:'center', width:100},
				{field:'violationMoneyType', title:'违纪违法资金类别', align:'center', width:100},
				{field:'violationType1', title:'违规违纪类别统计', align:'center', width:100},
				{field:'violationType2', title:'作风建设类别统计', align:'center', width:120},
				{field:'violationType0', title:'扫黑除恶类别统计', align:'center', width:100},
				{field:'shape', title:'四种形态', align:'center', width:100},
				{field:'amountOfRecovery', title:'追缴资金（万元）', align:'center', width:100}
			
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
				listSuccess(data); //暂无数据提示
			},
			onLoadError: function() {
				listError();
			}
		});
	}
	
	
</script>
</html>
