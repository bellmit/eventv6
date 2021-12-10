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
<script type="text/javascript" src="${rc.getContextPath()}/js/openWin.js"></script>
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
			singleSelect: false,
			scrollbarSize :20,		
			idField:'probId',
			url: '${rc.getContextPath()}/zhsq/personProb/personListData.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
			    {field:'probId',checkbox:true,hidden:'true'},
			    {field:'probTitle', title:'问题标题', align:'center', width:100},
			    {field:'name', title:'姓名', align:'center', width:100},
			    {field:'sex', title:'性别', align:'center', width:100},
			    {field:'politics', title:'政治面貌', align:'center', width:100},
			    {field:'workUnit', title:'单位', align:'center', width:100},
			    {field:'profession', title:'职位', align:'center', width:100},
				{field:'professionType', title:'违纪人员<br/>职位', align:'center', width:100},		
				{field:'violationDate', title:'违纪违规<br/>时间', align:'center', width:100},
			    {field:'partyFlag', title:'党纪处分', align:'center', width:100},
			    {field:'disciplineyFlag', title:'政纪处分', align:'center', width:100},
				{field:'orgProcType', title:'组织处理<br/>类型', align:'center', width:100},	
				{field:'blameFlag', title:'是否问责', align:'center', width:100},
				{field:'transferJusticeFlag', title:'是否移送<br/>司法', align:'center', width:100},
				{field:'amountInvolved', title:'涉案金额', align:'center', width:100},
				{field:'violationMoney', title:'违纪金额', align:'center', width:100},
				{field:'violationMoneyType', title:'违纪违法资金 <br/>类别', align:'center', width:200},
				{field:'violationType1', title:'违规违纪类别<br/> 统计', align:'center', width:200},
				{field:'violationType2', title:'作风建设类别<br/> 统计', align:'center', width:200},
				{field:'violationType0', title:'扫黑除恶类别 统计', align:'center', width:200},
				{field:'shape', title:'四种形态', align:'center', width:100},
				{field:'amountOfRecovery', title:'追缴资金<br/>(万元)', align:'center', width:100}
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
