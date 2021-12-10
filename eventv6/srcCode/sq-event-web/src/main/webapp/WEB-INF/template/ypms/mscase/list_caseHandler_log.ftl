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
				{field:'handleType', title:'办理状态', align:'center', width:100,
				formatter:function(value,rec,index){
					//01：登记派发；02：部门反馈处理；03：联动单位驳回；04：中心反馈处理；05：中心回退；06：中心再次派发；07：中心回访；08：归档；09：归档前退回（退回到中心回访环节）
					if (value == "00"){
						return "保存草稿";
					}else if (value == "01"){
						return "派发";
					}else if (value == "02") {
						return "反馈";
					}else if (value == "03") {
						return "驳回";
					}else if (value == "04") {
						return "中心待处理";
					}else if (value == "05") {
						return "中心回退";
					}else if (value == "06") {
						return "派发";
					}else if (value == "07") {
						return "回访";
					}else if (value == "08") {
						return "归档";
					}
				}},
				{field:'handlerName', title:'操作员', align:'center', width:100},
				{field:'handleTimeStr', title:'操作时间', align:'center', width:100},
				{field:'handleContent', title:'备注', align:'center', width:100,
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
