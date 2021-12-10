<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/standard_common_files-1.1.ftl" />
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
			<input type="hidden" id="bizType" name="bizType" value="${bizType}">
			<div class="fl">
				<ul>
					<li>热点主题：</li>
                    <li>
                        <input id="topicName" name="topicName" type="text" class="inp1 InpDisable"/>
                    </li>
                    <li>发布状态：</li>
                    <li>
                        <input id="isRelease" name="isRelease" type="text" class="hide queryParam"/>
                        <input id="isReleaseStr" name="isReleaseStr" type="text" class="inp1 InpDisable"/>
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
				<!-- 
				<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>
				<a href="javascript:void(0)" class="NorToolBtn ShangBaoBtn" onclick="publish();">发布</a>
				-->
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	
	var bizType="${bizType}";

	$(function() {
		anoleApi = AnoleApi.initTreeComboBox("isReleaseStr", "isRelease", "${isRelease}", null,[], 
		{ ShowOptions : {EnableToolbar : true},DefText : '请选择'});

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
			url: '${rc.getContextPath()}/zhsq/eventTopic/listData.json',
			queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'id_', hidden:true},
				{field:'isRelease', hidden:true},
				{field:'topicName', title:'热点主题', align:'center', width:100,formatter:topicNameFmt},
				{field:'rule', title:'分析规则', align:'center', width:100,formatter:ruleFmt},
				{field:'isReleaseStr', title:'状态', align:'center', width:100},
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
	
	//热点主题
	function topicNameFmt(value, rec, rowIndex){
		if(value == "") return "";
		return '<a href="###" onclick="detail('+rec.id_+')">'+value+'</span></a>';
	}
	
	//分析规则
	function ruleFmt(value, rec, rowIndex){
		if(value == "") return "";
		return '<span title="'+value+'">'+value+'</span>';
	}
	
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/eventTopic/form.jhtml?bizType=' + bizType;
		showMaxJqueryWindow('新增', url, 590, 380);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/eventTopic/form.jhtml?bizType='+bizType+'&id=' + rows[0].id_;
			showMaxJqueryWindow('编辑', url, 590, 380);
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/eventTopic/view.jhtml?id=" + id +'&bizType=' + bizType;
		showMaxJqueryWindow('详情', url, 550, 250);
	}
	
	//发布
	function publish(){
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		}else{
			if(rows[0].isRelease == '1'){
				$.messager.alert('提示', '该条记录已经发布,请重新选择记录!', 'info');
				return;
			}
			findReleaseCount(rows[0].id_);
		}
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
						url: '${rc.getContextPath()}/zhsq/eventTopic/del.json',
						data: {
							id_: rows[0].id_
						},
						dataType: 'json',
						success: function(data) {
							if (data.result == 'fail') {
								$.messager.alert('提示', '网络异常！', 'info');
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
	
	function release(id){
		$.ajax({
			type: 'POST',
			url: '${rc.getContextPath()}/zhsq/eventTopic/save.json',
			data: {"id_":id,"isRelease":"1"},
			dataType: 'json',
			success: function(data) {
				if (data.result == 'fail') {
					$.messager.alert('提示', '网络异常！', 'info');
				} else {
					$.messager.alert('提示', '发布成功！', 'info');
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
	
	function findReleaseCount(id){
		$.ajax({
			type: 'POST',
			url: '${rc.getContextPath()}/zhsq/eventTopic/findReleaseCount.json',
			data: {"bizType":bizType},
			dataType: 'json',
			success: function(data) {
				if(data != null && data == -1){
					$.messager.alert('提示', '缺失相关配置参数！', 'info');
					return;
				}else if(data != null && data >= 10){
					$.messager.alert('提示', '最多只能发布10条记录！', 'info');
					return;
				}else{
					release(id);
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
	
	//查询
	function searchData() {
		$('#list').datagrid('reload', $('#searchForm').serializeJson());
	}
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		$("#bizType").val(bizType);
		searchData();
	}
</script>
</html>
