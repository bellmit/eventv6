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
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/hotLine/listData.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'caseNo', title:'工单编号', align:'center', width:100},
				{field:'sourcesName', title:'来源渠道', align:'center', width:100},
				{field:'appealTypeName', title:'诉求类型', align:'center', width:100},
				{field:'callTimeStr', title:'来电时间', align:'center', width:100},
				{field:'occurred', title:'事件发生地址', align:'center', width:100},
				{field:'urgency', title:'紧急事项', align:'center', width:100,formatter: isTrue},
				{field:'eventStatusName', title:'事件状态', align:'center', width:100},
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
	
	function isTrue(value, rowData, rowIndex) {
		var isTrue = "";
		
		if(isNotBlankParam(value)) {
			if(value == '1') {
				isTrue = "是";
			} else if(value == '0') {
				isTrue = "否";
			}
		}
		
		return isTrue;
	}
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/hotLine/form.jhtml';
		showMaxJqueryWindow('新增', url, 936, 400, 'no');		
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/hotLine/form.jhtml?id=' + rows[0].caseNo;
			showMaxJqueryWindow('编辑', url, 936, 400, 'no');		
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/hotLine/detail.jhtml?id=" + id;
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
						url: '${rc.getContextPath()}/zhsq/hotLine/del.json',
						data: {
							caseNo: rows[0].caseNo
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
