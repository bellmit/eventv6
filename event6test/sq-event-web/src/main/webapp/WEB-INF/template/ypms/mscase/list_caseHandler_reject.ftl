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
		.nodata{height:180px} 
	</style>
</head>
<body class="easyui-layout">
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
		<div class="ConSearch" style="display:none">
			<form id="searchForm">
					<input class="inp1" type="hidden" id="relaCaseId" name="relaCaseId" value="${(caseId)!}"/>
					<input class="inp1" type="hidden" id="listType" name="listType" value="${(listType)!}"/>
			</form>
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
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/ypms/mscase/listDataCaseHandler.jhtml',
			queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'handleTimeStr', title:'日期', align:'center', width:100},
				{field:'handlerOrgCN', title:'单位', align:'center', width:100},
				{field:'handlerName', title:'操作员', align:'center', width:100},
				{field:'handlerTel', title:'联系方式', align:'center', width:100},
				{field:'handleContent', title:'驳回理由', align:'center', width:100,
				formatter:function(value,rec,index){
					if (value != null){
						var length = value.length;
						if (length > 15) {
							value = value.substring(0, 15)+"...";
						}
						var f = '<a style="text-decoration:none;" class="" href="###" title="'+ rec.handleContent +'" >'+ value +'</a>';
						return f;
					}
				}},
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
				//listSuccess(data); //暂无数据提示
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
			onLoadError: function() {
				listError();
			}
		});
	}
	
	
	
	
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/ypms/mscase/detailCaseHandler.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, 500, 400);
	}
	
	//删除
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的信息吗?', function(r) {
				if (r) {
					modleopen(); //打开遮罩层
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/ypms/mscase/delCaseHandler.json',
						data: {
							chId: rows[0].chId
						},
						dataType: 'json',
						success: function(data) {
							if (data.result == 'fail') {
								$.messager.alert('错误', '删除失败！', 'error');
							} else {
								$.messager.alert('提示', '删除成功！', 'info');
								searchData();
							}
						},
						error: function(data) {
							$.messager.alert('错误', '连接超时！', 'error');
						},
						complete : function() {
							modleclose(); //关闭遮罩层
						}
					});
				}
			});
		}
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
