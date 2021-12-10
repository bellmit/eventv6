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
	                <li>学校名称</li>
	                <li><input name="schoolName" id="schoolName" maxLength="30" type="text" class="inp1 easyui-validatebox"
	                           style="width: 120px;" data-options="tipPosition:'bottom'"/>
	                </li>
	                <li>统计年份</li>
	                <li><input name="statYear" id="statYear" maxLength="30" type="text" class="inp1 easyui-validatebox"
	                           style="width: 120px;" data-options="tipPosition:'bottom'"/>
	                </li>
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
				<@actionCheck></@actionCheck>
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
			url: '${rc.getContextPath()}/zhsq/szzg/schoolStat/listData.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'statId', hidden:'true'},
				{field:'schId', hidden:'true'},
				{field:'schoolName', title:'学校名称', align:'center', width:100,formatter: function (val, rec) {
                     return '<a href="###"  onclick="detail('+rec.statId+')"><span style="color:blue; text-decoration:underline;">'+val+'</span></a>';
                }},
				{field:'teachers', title:'老师人数', align:'center', width:100},
				{field:'students', title:'学生人数', align:'center', width:100},
				{field:'males', title:'男生人数', align:'center', width:100},
				{field:'females', title:'女生人数', align:'center', width:100},
				{field:'statYear', title:'统计年份', align:'center', width:100}
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
		var url = '${rc.getContextPath()}/zhsq/szzg/schoolStat/form.jhtml';
		showMaxJqueryWindow('新增', url, 400, 400);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/szzg/schoolStat/form.jhtml?id=' + rows[0].statId;
			showMaxJqueryWindow('编辑', url, 400, 400);
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/szzg/schoolStat/detail.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, 400, 400);
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
						url: '${rc.getContextPath()}/zhsq/szzg/schoolStat/del.json',
						data: {
							statId: rows[0].statId
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
