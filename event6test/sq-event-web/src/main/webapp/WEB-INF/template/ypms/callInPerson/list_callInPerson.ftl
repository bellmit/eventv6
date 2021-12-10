<!DOCTYPE html>
<html>
<head>
	<title>主叫人员列表</title>
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
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
			<div class="fl">
				<ul>
					<li>姓名：</li><li><input class="inp1" type="text" id="cpName" name="cpName" /></li>
					<li>联系电话：</li><li><input class="inp1" type="text" id="cpMobile" name="cpMobile" /></li>
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
				<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>
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
			url: '${rc.getContextPath()}/zhsq/callInPerson/listData.json',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
			    {field:'cpId',hidden:'true'},
				{field:'cpName', title:'姓名', align:'center', width:100},
				{field:'cpMobile', title:'联系电话', align:'center', width:100},
				{field:'cpSex', title:'性别', align:'center', width:100,
					formatter : function(value, rec, index) {
						var f = '';
						if (value == '02') {
							f = "女";
	                	}else if(value == '01'){
	                		f = "男";
	                	}else {
	                		f = "错误";
	                	}
						return f;
					}	
				}
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
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/callInPerson/form.jhtml';
		showMaxJqueryWindow('新增', url, 400, 230);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/callInPerson/form.jhtml?id=' + rows[0].cpId;
			showMaxJqueryWindow('编辑', url, 400, 230);
		}
	}
	
	//详情
	/*function detail(id) {
		var url = "${rc.getContextPath()}/gmis/callInPerson/detail.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, 500, 400);
	}*/
	
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
						url: '${rc.getContextPath()}/zhsq/callInPerson/del.json',
						data: {
							cpId: rows[0].cpId
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
