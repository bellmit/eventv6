﻿<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
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
					<li>报修类型：</li>
	                <li>
	                	<input id="faultType" name="faultType" type="text" value="" class="queryParam hide"/>
	                	<input id="faultTypeName" name="faultTypeName" type="text" class="inp1 InpDisable w150" />
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
	                                    			<label class="LabName width65px"><span>上报时间：</span></label>
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
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
	    //加载数据字典：案件类型
		AnoleApi.initListComboBox("faultTypeName", "faultType", "${faultType}", null, null, {
			ShowOptions : {
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
			url: '${rc.getContextPath()}/zhsq/repairTask/listData.jhtml?indexType=end',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'faultTypeStr', title:'报修类型', align:'center', width:100},
				{field:'faultDesc', title:'故障描述', align:'center', width:120,formatter : function(val, rec) {
					return '<a href="javascript:detail(\''+rec.drtId+'\','+rec.wfInstanceId+');" title="'+val+'">'+val+'</a>';
				}},
				{field:'faultAddr', title:'故障地址', align:'center', width:120,formatter : function(val, rec) {
					return '<span title="'+val+'">'+val+'</span>';
				}},
				{field:'linkUser', title:'联系人', align:'center', width:120,formatter : function(val, rec) {
					return val+' （'+rec.linkTel+'）';
				}},
				{field:'repairTimeStr', title:'上报时间', align:'center', width:80},
				{field:'state', title:'任务状态', align:'center', width:80,formatter : function(val, rec) {
					if(val=='0'){
						return '草稿';
					}else if(val=='1'){
						return '待派发';
					}else if(val=='2'){
						return '待反馈';
					}else if(val=='3'){
						return '待审核';
					}else if(val=='4'){
						return '已归档';
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
	function detail(id,wfInstanceId) {
		var instanceId = '';
		if(wfInstanceId!=null&&wfInstanceId!=''){
			instanceId = wfInstanceId;
		}
		var url = "${rc.getContextPath()}/zhsq/repairTask/detail.jhtml?id=" + id+'&wfInstanceId='+instanceId;
		showMaxJqueryWindow('详情', url, 800, null);
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
