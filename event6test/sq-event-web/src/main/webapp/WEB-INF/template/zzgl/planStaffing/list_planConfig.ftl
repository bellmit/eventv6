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
   <div  region="north" class="NorForm"  style="height: 103px !important">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		
			<tr>
					<td class="LeftTd">
						<label class="LabName"><span><font style="color: red">*</font>预案类型：</span></label>
						<span class="Check_Radio FontDarkBlue">${(emergencyPlan.planTypeName)!}</span>
					</td>
			</tr>
			<tr>
					<td class="LeftTd">
						<label class="LabName"><span>预案内容：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width: 86%">${(emergencyPlan.planContent)!}</span>
					</td>
			</tr>
		</table>
	</div>
 
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
			<input type="hidden" id="planId" name="planId" value="${planId!}" />
			<div class="fl">
				<ul>
					<li>预案等级：</li><li>
					         <input type="hidden" id="planLevel" name="planLevel" value="" />
							 <input type="text" class="inp1 easyui-validatebox singleCell" data-options="tipPosition:'bottom'" id="planLevelName" value="" />
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
		<!-- 		<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a> -->
			</div>
			
		</div>
	</div>
</body>
<script type="text/javascript">
  var planLevelAno=null;
	$(function() {
		
		planLevelAno=AnoleApi.initTreeComboBox("planLevelName", "planLevel", "A001140", null, [''],{
			ShowOptions: {
				EnableToolbar : true
			}
	    });
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
			url: '${rc.getContextPath()}/zhsq/zzgl/planConfig/listData.jhtml',
			queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'planLevelName', title:'预案等级', align:'center', width:100,
					formatter:function(value,rec,rowIndex){
						var f = '<a href="###" title='+ rec.planLevelName +' onclick="detail(\''+ rec.planConfigId +'\')">'+value+'</a>&nbsp;';
						return f;
					}},
				{field:'createTime', title:'创建时间', align:'center', width:100,formatter:function(value){
                    return new Date(value).format('yyyy-MM-dd hh:mm:ss');
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
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/zzgl/planConfig/add.jhtml?planId=${planId!}';
		showMaxJqueryWindow('新增配置', url,800,450);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/zzgl/planConfig/add.jhtml?planId=${planId!}&id=' + rows[0].planConfigId;
			showMaxJqueryWindow('编辑配置', url,800,450);
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/zzgl/planConfig/detail.jhtml?id=" + id;
		showMaxJqueryWindow('配置详情', url,800,450);
	}
	
	//删除
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的配置吗?', function(r) {
				if (r) {
					modleopen(); //打开遮罩层
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/zzgl/planConfig/del.json',
						data: {
							planConfigId: rows[0].planConfigId
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
		$('#searchForm')[0].reset();
		$("#planLevel").val('');
		searchData();
	}
</script>
</html>
