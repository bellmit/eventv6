<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<script type="text/javascript" src="${rc.getContextPath()}/ngc3js/ncctools.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/ngc3js/ncc.min.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/jquery.counttimer.min.js"></script>
	<style type="text/css">
		.inp1 {width:100px;}
	</style>
</head>
<body class="easyui-layout">

		<input type="hidden" id="menuType" name="menuType" value="${(menuType)!}"  />
	<form id="searchForm">
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
						
			<div class="fl">
				<ul>
					<li>所属辖区：</li>
					<li>
		                <input type="hidden" id="caseFromId" name="caseFromId" value="" />
						<input type="hidden" id="caseFromCode" name="caseFromCode" value="" />
						<input class="inp1 InpDisable" type="text" id="caseFromCodeCN" name="caseFromCodeCN" value="" class="inp1 easyui-validatebox" data-options="validType:'maxLength[32]', tipPosition:'bottom'" readonly />	
					</li>
					
					<li>关键字：</li>
					<li><input class="inp1" type="text" id="caseNo" name="caseNo" /></li></li>

					<li>办理部门：</li>
					<li>
	            		<input type="text" id="handleDeptName" name="handleDeptName"  class="inp1" data-options="validType:'maxLength[20]', tipPosition:'bottom'" />
					</li>
						
					<li>采集人员：</li>
					<li>
	            		<input type="text" id="receiverName" name="receiverName"  class="inp1" data-options="validType:'maxLength[20]', tipPosition:'bottom'" />
					</li>
						
	                <li style="position: relative;"><a href="#" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	                    <div class="AdvanceSearch DropDownList hide" style="width: 450px; top: 42px; left: -160px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                        
	                                        <tr>
	                                            <td>
	                                                <label class="LabName"><span>呼叫时间：</span></label>
	                                                <input type="text" class="Wdate inp1" style="width:150px; height:28px"
	                                                       onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
	                                                       name="startDateCallin" id="startDateCallin"/>
	                                                <label>到</label>
	                                                <input type="text" class="Wdate inp1" style="width:150px; height:28px"
	                                                       onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
	                                                       name="endDateCallin" id="endDateCallin"/>
	                                            </td>
	                                        </tr>
	                                        
	                                        <tr>
	                                            <td>
	                                                <label class="LabName"><span>案件类别：</span></label>
	                                                <input type="hidden" id="caseType" name="caseType" />
	            									<input type="text" id="caseTypeCN" name="caseTypeCN" style="width: 328px;" class="inp1" data-options="validType:'maxLength[10]', tipPosition:'bottom'" />
	                                            </td>
	                                        </tr>
	                                        
	                                        <tr>
	                                            <td>
	                                                <label class="LabName"><span>处理方式：</span></label>
	                                                <input type="hidden" id="handleWay" name="handleWay" />
	            									<input type="text" id="handleWayCN" name="handleWayCN" style="width: 328px;" class="inp1" data-options="validType:'maxLength[10]', tipPosition:'bottom'" />
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
				
				<#--
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="handleLog();">日志</a>
				<#if  menuType == "untreated" >
					<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="handle();">处理</a>
				</#if>
				<#if menuType == "wait" || menuType == "index">
					<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="handle();">处理</a>
					<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="returnVisit();">回访</a>
				</#if>
				-->
				<#--
				<#if menuType == "index">
					<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">案件登记</a>
				</#if>
				-->
				 <@actionCheck></@actionCheck> 	
				<#--
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="handleLog();">日志</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="returnVisit();">回访</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="handle();">处理</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add('','','','manual');">案件登记</a>
				<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				-->
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">

	
	$(function() {
		//所属辖区
		AnoleApi.initOrgEntityZtreeComboBox("caseFromCodeCN", null, function(gridId, items) {
			if (items && items.length > 0) {
				$("#caseFromCode").val(items[0].orgCode);
				$("#caseFromId").val(items[0].id);
			}else{
			
			}
		}, {
			ShowOptions : {
				EnableToolbar : true
			},
			OnCleared:function(){
				$("#caseFromCode").val("");
				$("#caseFromId").val("");
			}
		});
		
		
		//案件类型；01：意见投诉；02：生活服务；03：困难求助；04：信息咨询
		AnoleApi.initListComboBox("caseTypeCN", "caseType", null, null, ["-1"], {
			ShowOptions : {
				EnableToolbar : true
			},
			DataSrc : [
				{"name":"全部","value":"-1"},
				{"name":"意见投诉","value":"01"},
				{"name":"生活服务","value":"02"},
				{"name":"困难求助","value":"03"},
				{"name":"信息咨询","value":"04"}
			]
		});
		
		//处理方式；01：直接处理；02：12345平台；03：民生110
		AnoleApi.initListComboBox("handleWayCN", "handleWay", null, null, ["-1"], {
			ShowOptions : {
				EnableToolbar : true
			},
			DataSrc : [
				{"name":"全部","value":"-1"},
				{"name":"直接处理","value":"01"},
				{"name":"12345平台","value":"02"},
				{"name":"民生110","value":"03"}
			]
		});
		loadList(); //加载列表
	});
	
	//加载列表
	function loadList() {
		var menuType = $("#menuType").val();
		$('#list').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/ypms/mscase/listData.jhtml?menuType='+menuType,
			queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'caseNo', title:'案件编号', align:'center', width:100,formatter:function(value,rec,index){
					var f = '<a class="" href="###" title="'+ rec.caseNo +'" onclick="detail(' + rec.caseId + ')")>'+value+'</a>';
					return f;
				}},
				{field:'callinTimeStr', title:'呼叫时间', align:'center', width:100},
				{field:'caseType', title:'案件类型', align:'center', width:100,
				formatter:function(value,rec,index){
					//01：意见投诉；02：生活服务；03：困难求助；04：信息咨询
					if (value == "01"){
						return "意见投诉";
					}else if (value == "02") {
						return "生活服务";
					}else if (value == "03") {
						return "困难求助";
					}else if (value == "04") {
						return "信息咨询";
					}
				}},
				{field:'caseDesc', title:'案件描述', align:'center', width:100,
				formatter:function(value,rec,index){
					if(value != null ){
						var length = value.length;
						if (length > 15) {
							value = value.substring(0, 15)+"...";
						}
						var f = '<a style="text-decoration:none;" class="" href="###" title="'+ rec.caseDesc +'" >'+ value +'</a>';
						return f;
					}
					
				}},
				{field:'handleDeptName', title:'办理部门', align:'center', width:100},
				{field:'handleStatus', title:'办理状态', align:'center', width:100,
				formatter:function(value,rec,index){
					var handleStatus = rec.handleStatus;
					var rdhStatus = rec.rdhStatus;
					var rdStatus = rec.rdStatus;
					if (rdhStatus == "02"){
						return "回退";
					}else{
						//01：受理中、02：回访中、03：已结案
						if (handleStatus == "00"){
							return "草稿";
						}else if (handleStatus == "01"){
							return "受理中";
						}else if (handleStatus == "02") {
							return "回访中";
						}else if (handleStatus == "03") {
							return "已结案";
						}else if (handleStatus == "04") {
							return "驳回";
						}
					}
				}},
				{field:'oper', title:'操作', align:'center', width:120,formatter:formatOper}
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
				//listSuccess(data); //暂无数据提示
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
				
			},
			onLoadError: function() {
				listError();
			},
			onClickRow: function (rowIndex, rowData) {
                $(this).datagrid('unselectRow', rowIndex);
   			}
		});
	}
	
	function formatOper(value,rec,index){
		var handleStatus = rec.handleStatus;
		var createOrgCode = rec.createOrgCode;
		var orgInfoCode = "${(orgInfoCode)!}"
		html = "";
		if(createOrgCode == orgInfoCode){
		
			if (handleStatus == "01" || handleStatus == "04"){
			html += '<a href="javascript:void(0)" class="" onclick="handle('+index+');">处理</a>&nbsp;';
			
			}else if (handleStatus == "02"){
			html += '<a href="javascript:void(0)" class="" onclick="returnVisit('+index+');">回访</a>&nbsp;';
			}
		}else{
			html += '<a href="javascript:void(0)" class="" onclick="handle('+index+');">处理</a>&nbsp;';
		}	
			
		html += '<a href="javascript:void(0)" class="" onclick="handleLog('+index+');">日志</a>';
		return html;
	}
	
	//新增(案件登记)
	function add(callinNum,calledNum,callinTime,registerType) {
		if(callinNum == undefined){
			callinNum = "";
		}
		if(calledNum == undefined){
			calledNum = "";
		}
		if(callinTime == undefined){
			callinTime = "";
		}
		if(registerType == undefined){
			registerType = "manual";
		}
		
		var url = '${rc.getContextPath()}/zhsq/ypms/mscase/form.jhtml?registerType='+registerType+'&callinNum='+callinNum+'&calledNum='+calledNum+'&callinTime='+callinTime;
		showMaxJqueryWindow('新增', url, null, null);
	}
	
	//处理
	function handle(index) {
		var menuType = $("#menuType").val();
		$('#list').datagrid('selectRow',index);
		
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/ypms/mscase/form.jhtml?id=' + rows[0].caseId + '&menuType=' + menuType;
			showMaxJqueryWindow('处理', url, null, null);
		}
	}
	
	//回访
	function returnVisit(index) {
		$('#list').datagrid('selectRow',index);
	
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			if(rows[0].handleStatus == "02"){
				var url = '${rc.getContextPath()}/zhsq/ypms/mscase/form.jhtml?id=' + rows[0].caseId;
				showMaxJqueryWindow('回访', url, null, null);
			}else{
				$.messager.alert('提示', '该案件不属于回访阶段!', 'warning');
			}
		}
	}
	
	//查看日志信息
	function handleLog(index){
		$('#list').datagrid('selectRow',index);
		
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var	caseId = rows[0].caseId;
			var listType = "log";
			var url = '${rc.getContextPath()}/zhsq/ypms/mscase/showCaseHandlerInfo.jhtml?caseId=' + caseId + '&listType=' + listType;
			showMaxJqueryWindow('日志', url, 800, 400);
		}
	}
	
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/ypms/mscase/detail.jhtml?id=" + id;
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
						url: '${rc.getContextPath()}/zhsq/ypms/mscase/del.json',
						data: {
							caseId: rows[0].caseId
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
		$('#list').datagrid('load', $('#searchForm').serializeJson());
	}
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		searchData();
	}
</script>
<#include "/ypms/mscase/ncc.html" />
</html>
