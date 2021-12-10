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

	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
			<div class="fl">
				<ul>
					<li>关键字：</li>
	                <li>
		                <input name="keyWord" type="text" class="inp1" id="keyWord" value="可输入案件编号、标题等关键字" style="color:gray; width:170px;" onfocus="if(this.value=='可输入案件编号、标题等关键字'){this.value='';}$(this).attr('style','width:170px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:170px;');this.value='可输入案件编号、标题等关键字';}" onkeydown="_onkeydown();" />
	            	</li>
					<li>事件类别：</li>
	                <li>
	                	<input id="type" name="type" type="text" value="" class="queryParam hide"/>
	                	<input id="typeName" name="typeName" type="text" class="inp1 InpDisable w150" />
	                </li>
	                <li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:440px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>提交时间：</span></label>
	                                    			<input type="text" id="happenTimeStr" name="happenTimeStr" class="inp1 Wdate easyui-validatebox" style="width:125px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true,maxDate:'#F{$dp.$D(\'endTimeStr\')}', dateFmt:'yyyy-MM-dd', isShowToday:false})"  readonly="readonly">
								                		~
								                	<input type="text" id="endTimeStr" name="endTimeStr" class="inp1 Wdate easyui-validatebox" style="width:125px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, minDate:'#F{$dp.$D(\'happenTimeStr\')}', dateFmt:'yyyy-MM-dd', isShowToday:false})"  readonly="readonly">
	                                    		</td>
	                                    	</tr>
	                                    </table>
	                                </div>
	                                <div class="BottomShadow"></div>
	                            </div>
	                        </div>
	                    </div>
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
			<@ffcs.right rightCode="add" parentCode="${system_privilege_action?default('')}">
				<a href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
			</@ffcs.right>
			<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="toOwnerDeal();">评价</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		//加载数据字典：案件类型
		AnoleApi.initListComboBox("typeName", "type", "${caseTypeDict}", null, null, {
			ShowOptions : {
				EnableToolbar : true
			}
		});
		
		loadList(); //加载列表
	});
	
	//加载列表
	function loadList() {
		$('#list').datagrid({
			width:600,
			height:300,
			nowrap: true,
			rownumbers: true,
			remoteSort: false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'reId',
			url: '${rc.getContextPath()}/zhsq/internetEnterprise/listDataAll.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'reqId',title:'ID', align:'center',hidden:true},
				{field:'code', title:'案件编号', align:'center', width:100,formatter : function(val, rec) {
					if ( rec.state == '0'){
						return '<a href="javascript:details(\''+rec.reqId+'\',\''+rec.tasktype+'\');" >'+val+'</a>';
					}else{
						return '<a href="javascript:detail(\''+rec.reqId+'\',\''+rec.tasktype+'\');" >'+val+'</a>';
					}
				}},
				{field:'creatTimeStr', title:'提交时间', align:'center', width:100},
				{field:'typeStr', title:'案件类型', align:'center', width:100},
				{field:'title', title:'案件标题', align:'center', width:100},
				{field:'state', title:'办理状态', align:'center', width:100,formatter : function(val, rec) {

					var str = '';
					if(rec.tasktype=='2'){
						str = '(子)';
					}if(val=='0'){
						return '草稿'+str;
					}
					else if(val=='1'){
						return '受理中'+str;
					}else if(val=='2'){
						return '单位处理'+str;
					}else if(val=='3'){
						return '退回发起人';
					}else if(val=='4'){
						return '归档待评';
					}else if(val=='5'){
						return '已评价';
					}
					return '';
				}},
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
	
	//详情
	function detail(id,tasktype) {
		var url = "${rc.getContextPath()}/zhsq/internetEnterprise/detail.jhtml?id=" + id+"&tasktype="+tasktype;
		showMaxJqueryWindow('详情', url, 800, fixHeight(0.9));
	}
	
	//详情
	function details(id,tasktype) {
		var url = "${rc.getContextPath()}/zhsq/internetEnterprise/details.jhtml?id=" + id+"&tasktype="+tasktype;
		showMaxJqueryWindow('详情', url, 800, fixHeight(0.9));
	}
	
	//查询
	function searchData() {
		var a = new Array();
		var type = $("#type").val();
		a["type"] = type;
		var happenTimeStr = $('#happenTimeStr').val();
		a["happenTimeStr"] = happenTimeStr;
		var endTimeStr = $('#endTimeStr').val();
		a["endTimeStr"] = endTimeStr;
		var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="可输入案件编号、标题等关键字") {
			a["keyWord"] = keyWord;
		}
		var wfCurOrg = $("#wfCurOrg").val();
		a["wfCurOrg"] = wfCurOrg;
		
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
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/internetEnterprise/addView.jhtml';
		showMaxJqueryWindow("登记", url, 800, fixHeight(0.9));
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
			if(state!=null && state!=""){
				if(state=='0'){
					var id=rows[0].reqId;
					var url = '${rc.getContextPath()}/zhsq/internetEnterprise/toEdit.jhtml?reqId='+id;
					showMaxJqueryWindow("编辑", url, 800, fixHeight(0.9));
					$('#list').datagrid('unselectAll');	
				}else{
					$.messager.alert('提示','已受理或归档不可编辑','info');
					$('#list').datagrid('unselectAll');	
				}
			}
		}
	}
	
	//删除
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var state=rows[0].state;
			if(state!=null && state!=""){
				if(state=='0'){
					$.messager.confirm('提示', '您确定删除选中的信息吗?', function(r) {
				if (r) {
					modleopen(); //打开遮罩层
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/internetEnterprise/del.json',
						data: {
							reqId: rows[0].reqId
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
				}else{
					$.messager.alert('提示','已受理或归档不可删除','info');
					$('#list').datagrid('unselectAll');	
				}
			}
		}
	}
	//评价
	function toOwnerDeal(){
		
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var state = rows[0].state;
			if("4"!=state&&"5"!=state){
				$.messager.alert('提示', '案件归档后方可评价!', 'warning');
			}else{
				var url = '${rc.getContextPath()}/zhsq/requestion/toOwnerDeal.jhtml?id=' + rows[0].reqId;
				showMaxJqueryWindow('满意度评价', url,  500, 300);
			}
		}
	}
</script>
</html>
