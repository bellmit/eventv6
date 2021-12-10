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
					<li>申请类别：</li><li>
						<input type="hidden" name="applyType" id="applyType"/>
						<input type="text" id="applyTypeName" name="applyTypeName" value="" class="inp1 easyui-validatebox" />
					</li>
					<li>法官姓名：</li><li><input class="inp1" type="text" id="courtName" name="courtName" /></li>
					<li>所属部门：</li><li><input class="inp1" type="text" id="department" name="department" /></li>
					<li style="position: relative;"><a href="#" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
						<div class="AdvanceSearch DropDownList hide" style="width: 400px; top: 42px; left: -160px;">
							<div class="LeftShadow">
								<div class="RightShadow">
									<div class="list NorForm">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>
													<!-- publishDate -->
													<label class="LabName"><span>申请时间：</span></label>
													<input type="text" class="inp1 Wdate" style="width:120px; height:28px"
														   onClick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'applyDateEnd\')}'});"
														   name="applyDateStart" id="applyDateStart"/>
													<label>~</label>
													<input type="text" class="inp1 Wdate" style="width:120px; height:28px"
														   onClick="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'applyDateStart\')}'});"
														   name="applyDateEnd" id="applyDateEnd"/>
												</td>
											</tr>
											<tr>
												<td>
													<label class="LabName"><span>事项说明：</span></label>
													<input class="inp1" type="text" id="itemDescription" name="itemDescription" />
												</td>
											</tr>
											<tr>
												<td>
													<label class="LabName"><span>所属网格：</span></label>
													<input type="hidden" id="gridCode" name="gridCode"/>
													<input type="text" class="inp1 easyui-validatebox" name="gridName" id="gridName"/>
												</td>
											</tr>
											<tr>
												<td>
													<label class="LabName"><span>状态：</span></label>
													<input type="hidden" id="status" name="status"/>
													<input type="text" class="inp1 easyui-validatebox" name="statusCN" id="statusCN"/>
												</td>
											</tr>
											<tr>
												<td>
													<label class="LabName"><span>满意度：</span></label>
													<input type="hidden" id="satisfaction" name="satisfaction"/>
													<input type="text" class="inp1 easyui-validatebox" name="satisfactionName" id="satisfactionName"/>
												</td>
											</tr>
										</table>
									</div>

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
				<#if menuType=="mycreate" || menuType=="mywait">
<#--					<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>-->
<#--					<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>-->
<#--					<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="handle();">办理</a>-->
<#--					<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>-->
					<@actionCheck></@actionCheck>
				</#if>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var applyTypeDict ={};
	//初始化字典
	<#if applyTypeDict?exists>
		<#list applyTypeDict?keys as key>
			applyTypeDict['${key}'] = '${applyTypeDict[key]}'
		</#list>
	</#if>
	var satisfDict = {};
	<#if satisfDict?exists>
		<#list satisfDict?keys as key>
			satisfDict['${key}'] = '${satisfDict[key]}'
		</#list>
	</#if>
	var statusMap = {}
	<#if statusMap?exists>
		<#list statusMap?keys as key>
			statusMap['${key}'] = '${statusMap[key]}'
		</#list>
	</#if>
	var menuType = '${menuType}';
	$(function() {
		AnoleApi.initGridZtreeComboBox("gridName", "gridCode", function(regionCode, items) {
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#gridCode").val(grid.gridCode);
			}
		},{
			ShowOptions : {
				EnableToolbar : true
			},
		});
		AnoleApi.initListComboBox("statusCN", "status", null, null, null, {
			DataSrc: [{"name":"草稿", "value":'01'},{"name":"办理中", "value":'02'},{"name":"待评价", "value":'03'},{"name":"结束", "value":'04'}],
			ShowOptions: {
				EnableToolbar : true
			}
		});
		AnoleApi.initListComboBox("satisfactionName", "satisfaction", "B12322003",null,null, {
			ShowOptions: {
				EnableToolbar : true
			}
		});
		AnoleApi.initListComboBox("applyTypeName", "applyType", "B12322002",null,[]);


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
			url: '${rc.getContextPath()}/zhsq/courtSynergism/listData.jhtml?menuType='+menuType,
			queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'applyType', title:'申请类别', align:'center', width:100,formatter:function(value, rec, rowIndex){return applyTypeDict[value]}},
				{field:'courtName', title:'法官姓名', align:'center', width:100,formatter:function (value, rec) {
						var htmlStr = '<div class="clearfix" ><a class="hidden-more" href="###" class="" title="' + value + '" onclick="detail(' + rec.synergismId + ')"> ' + value  +'</a></div>' ;
						return htmlStr;
				}},
				{field:'department', title:'所属部门', align:'center', width:100},
				{field:'contactInformation', title:'联系方式', align:'center', width:100},
				{field:'applyDate', title:'申请时间', align:'center', width:100},
				{field:'itemDescription', title:'事项说明', align:'center', width:100,formatter:function (value, rec) {
						let str = "-"
						if(value != "" && value != null){
							if(value.length > 10 ){
								str = value.substr(0,5) + "..."
							}else{
								str = value;
							}
						}
						return '<p title="'+value+'">'+str+'</p>';
					}},
				{field:'gridName', title:'所属网格', align:'center', width:100},
				{field:'creatorName', title:'申请人', align:'center', width:100},
                {field:'satisfaction', title:'满意度', align:'center', width:100,formatter:function(value, rec, rowIndex){return satisfDict[value] }},
                {field:'status', title:'状态', align:'center', width:100,formatter:function(value, rec, rowIndex){return statusMap[value]}},
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
		var url = '${rc.getContextPath()}/zhsq/courtSynergism/form.jhtml';
		showMaxJqueryWindow('新增', url, null, null);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			if(rows[0].status != '01'){
				$.messager.alert('提示', '已发起流程无法编辑!', 'warning');
				return;
			}

			var url = '${rc.getContextPath()}/zhsq/courtSynergism/form.jhtml?id=' + rows[0].synergismId;
			showMaxJqueryWindow('编辑', url, null, null);
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/courtSynergism/view.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, null, null);
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
						url: '${rc.getContextPath()}/zhsq/courtSynergism/del.json',
						data: {
							synergismId: rows[0].synergismId
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

	//办理
	function handle() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/courtSynergism/handle.jhtml?id=' + rows[0].synergismId;
			showMaxJqueryWindow('办理', url, null, null);
		}
	}
</script>
</html>
