<!DOCTYPE html>
<html>
<head>
	<title>所有列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<style type="text/css">
		.width65px{width:75px;}
		.w150{width:150px;}
		.keyBlank{color:gray;}
	</style>
</head>
<script type="text/javascript">

//-- 供子页调用的重新载入数据方法
function reloadDataForSubPage(result){
	closeMaxJqueryWindow();
	$.messager.alert('提示', result, 'info');
	$("#list").datagrid('load');
}
</script>
<body class="easyui-layout">

	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
			<div class="fl">
				<ul>
					<li>关键字：</li>
	                <li>
		                <input name="keyWord" type="text" class="inp1" id="keyWord" value="请输入姓名、单位或职务的关键字..." style="color:gray; width:200px;" onfocus="if(this.value=='请输入姓名、单位或职务的关键字...'){this.value='';}$(this).attr('style','width:200px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:200px;');this.value='请输入姓名、单位或职务的关键字...';}" onkeydown="_onkeydown();" />
	            	</li>
					<li>联系电话：</li>
	                <li>
		                <input name="telPhone" type="text" class="inp1" id="telPhone" style="color:gray; width:170px;" />
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
			<@ffcs.right rightCode="del" parentCode="${system_privilege_action?default('')}">
    			<a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>
			</@ffcs.right>
			<@ffcs.right rightCode="edit" parentCode="${system_privilege_action?default('')}">
				<a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
			</@ffcs.right>
			</div>
		</div>
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
			width:700,
			height:300,
			nowrap: false,
			rownumbers: true,
			remoteSort: false,
			striped: true,
			fit: true,
			fitColumns: true,
			idField:'abId',
			url: '${rc.getContextPath()}/zhsq/addressBook/listData.jhtml',
			columns: [[
				{field:'abId',title:'ID', align:'center',hidden:true},
				{field:'abName', title:'姓名', align:'center', width:'20%'},
				{field:'ldName', title:'联动部门', align:'center', width:'20%'},
				{field:'abRole', title:'角色', align:'center', width:'20%', formatter : function (val, rec){
					var str = '';
					if( val=='1' ){
						return '联络员'+str;
					}else if( val=='2' ){
						return '联系人'+str;
					}else {
						return '分管领导'+str;
					}
				}},
				{field:'abDuty', title:'职务', align:'center', width:'19%'},
				{field:'abMobile', title:'联系电话', align:'center', width:'19%'},
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
				listSuccess(data); //暂无数据提示
			},
			onSelect:function(index,rec){
				idStr=rec.abId;
			}, 
			onLoadError: function() {
				listError();
			}
		});
	}
	
	//查询
	function searchData() {
		var a = new Array();
		var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="请输入姓名、单位或职务的关键字...") {
			a["keyWord"] = keyWord;
		}
		var telPhone = $("#telPhone").val();
		a["telPhone"] = telPhone;
		
		doSearch(a);
	}
	
	//重置
	function resetCondition() {
		$("#searchForm")[0].reset();
		$("#keyWord").attr('style','font-size:12px;color:gray; width:170px;');
		searchData();
	}
	
	function doSearch(queryParams) {
		// resetCondition();
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData();
		}
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		var num=rows.length;
		if(num!=1){
		   $.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
		   return;
		}else{
				var state=rows[0].state;
				var id=rows[0].abId;
				var url = '${rc.getContextPath()}/zhsq/addressBook/toEdit.jhtml?abId='+id;
				showMaxJqueryWindow("编辑", url, 600, fixHeight(0.7));
				$('#list').datagrid('unselectAll');	
			}
	}
	
	//删除
	function del() {
		var ids = [];
		var statusArry = [];
		var rows = $('#list').datagrid('getSelections');
		for(var i = 0; i<rows.length; i++){
			ids.push(rows[i].abId);
			statusArry.push(rows[i].status);
		}
		var idStr = ids.join(',');
		if(idStr == null || idStr == "") {
			$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
			return false;
		}
			$.messager.confirm('提示', '您确定删除选中的信息吗？', function(r){
				if (r){
					modleopen();
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/addressBook/del.jhtml',
						data: 'idStr='+idStr,
						dataType:"json",
						success: function(data){
							modleclose();
							$.messager.alert('提示', '您成功删除'+data.result+'条信息！', 'info');
							$("#list").datagrid('load');
						},
						error:function(data){
							$.messager.alert('错误','连接超时！','error');
						}
					});
				}
			});
		
	}
</script>
</html>
