<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.inp1 {width:150px;}
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
					<li>任务标题：</li><li><input class="inp1" type="text" id="taskTitle" name="taskTitle" /></li>
					<li>任务状态：</li>
	                <li>
	                	<input id="state" name="state" type="text" value="" class="queryParam hide"/>
	                	<input id="stateName" name="stateName" type="text" class="inp1 InpDisable w150" />
	                </li>
	                <li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:430px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>提交时间：</span></label>
	                                    			<input type="text" id="startTimeStr" name="startTimeStr" class="inp1 Wdate easyui-validatebox" style="width:125px; cursor:pointer;"  onclick="WdatePicker({el:'startTimeStr',readOnly:true, maxDate:'#F{$dp.$D(\'endTimeStr\')}', dateFmt:'yyyy-MM-dd',  isShowToday:false})"  readonly="readonly">
								                		~
								                	<input type="text" id="endTimeStr" name="endTimeStr" class="inp1 Wdate easyui-validatebox" style="width:125px; cursor:pointer;"  onclick="WdatePicker({el:'endTimeStr',readOnly:true, minDate:'#F{$dp.$D(\'startTimeStr\')}', dateFmt:'yyyy-MM-dd',  isShowToday:false})"  readonly="readonly">
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
				<#if flag ?? && flag=='1'>
					<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
					<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
					<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>
				</#if>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	
	$(function() {
		AnoleApi.initListComboBox("stateName", "state", null, null, [""], {
			ShowOptions : {
				EnableToolbar : true
			},
        	DataSrc: [{"name":"草稿", "value":"0"},{"name":"待派发", "value":"1"},{"name":"待反馈", "value":"2"},{"name":"待审核", "value":"3"},{"name":"已归档", "value":"4"}]
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
			url: '${rc.getContextPath()}/zhsq/dailyTask/listData.jhtml?indexType=fq',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'taskTitle', title:'任务标题', align:'center', width:100,formatter : function(val, rec) {
					return '<a href="javascript:detail(\''+rec.ddtId+'\','+rec.wfInstanceId+');" title="'+val+'">'+val+'</a>';
				}},
				{field:'createTimeStr', title:'提交时间', align:'center', width:100},
				{field:'wfCurUser', title:'当前办理人', align:'center', width:100},
				{field:'state', title:'任务状态', align:'center', width:100,formatter : function(val, rec) {
					var temp = "";
					if(rec.warnState=="2"){
						temp += '<i title="有退回信息" class="childState_1" style="padding-right: 5px;"></i>';
					}
					if(val=='0'){
						return '草稿';
					}else if(val=='1'){
						return '待派发';
					}else if(val=='2'){
						return temp+'待反馈';
					}else if(val=='3'){
						return '待审核';
					}else if(val=='4'){
						return '归档';
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
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/dailyTask/form.jhtml';
		showMaxJqueryWindow('新增', url, 800, null);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var wfInstanceId = rows[0].wfInstanceId;
			if(wfInstanceId!=null&&wfInstanceId!=''){
				$.messager.alert('提示', '已启动流程不能修改!', 'warning');
			}else{
				var url = '${rc.getContextPath()}/zhsq/dailyTask/form.jhtml?id=' + rows[0].ddtId;
				showMaxJqueryWindow('编辑', url, 800, null);
			}
		}
	}
	
	//详情
	function detail(id,wfInstanceId) {
		var instanceId = '';
		if(wfInstanceId!=null&&wfInstanceId!=''){
			instanceId = wfInstanceId;
		}
		var url = "${rc.getContextPath()}/zhsq/dailyTask/detail.jhtml?id=" + id+'&wfInstanceId='+instanceId;
		showMaxJqueryWindow('详情', url, 800, null);
	}
	
	//删除
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var wfInstanceId = rows[0].wfInstanceId;
			if(wfInstanceId!=null&&wfInstanceId!=''){
				$.messager.alert('提示', '已启动流程不能删除!', 'warning');
			}else{
				$.messager.confirm('提示', '您确定删除选中的信息吗?', function(r) {
					if (r) {
						modleopen(); //打开遮罩层
						$.ajax({
							type: 'POST',
							url: '${rc.getContextPath()}/zhsq/dailyTask/del.json',
							data: {
								ddtId: rows[0].ddtId
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
