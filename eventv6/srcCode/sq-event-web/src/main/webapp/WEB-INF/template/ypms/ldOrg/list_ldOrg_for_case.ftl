<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<style type="text/css">
		.inp1 {width:100px;}
	</style>
</head>
<body class="easyui-layout">
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
			<div class="fl">
				<ul>
					<li>名称：</li><li><input class="inp1" type="text" id="ldName" name="ldName" /></li>
					
				</ul>
			</div>
	        <div class="btns">
				<ul>
					<li><a href="javascript:;" class="chaxun" title="查询数据" onclick="searchData()">查询</a></li>
		            <li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
				</ul>
	        </div>
			</form>
		</div>
		<div class="h_10" id="TenLineHeight1"></div>
		<div class="ToolBar">
			<div class="tool fr">
				<a href="javascript:void(0)" class="NorToolBtn SubmitBtn" onclick="choose();">确定</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		loadList(); //加载列表
	});
	
	//加载列表
	function loadList() {
		$('#list').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: false,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/ldOrg/listData.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'ck', align:'center', width:100,checkbox:true},
				{field:'ldName', title:'名称', align:'center', width:100},
				{field:'ldType', title:'单位类型', align:'center', width:100,
					formatter:function(value,rec,index){
						if(value == '0'){
							return '联动队伍';
						}else if (value == '1'){
							return '专业化队伍';
						}
				}}
				
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
	
	//编辑
	function choose() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择单位!', 'warning');
		} else {
			parent.addDataDept(rows);
			parent.closeMaxJqueryWindow();
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/gmis/ldOrg/detail.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, 500, 400);
	}
	
	
	//查询
	function searchData() {
		$('#list').datagrid('reload', $('#searchForm').serializeJson());
	}
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		searchData();
	}
</script>
</html>
