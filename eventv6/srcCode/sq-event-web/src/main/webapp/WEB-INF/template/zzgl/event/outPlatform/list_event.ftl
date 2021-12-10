<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>外平台采集事件</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/common.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
</head>
<body class="easyui-layout">
	<div id="outPlatformDiv" region="center" title="${startGridName!''} 外平台采集事件" border="false" style="overflow:hidden;">
	<div class="easyui-layout" fit="true" border="false">
		<div id="jqueryToolbar" style="padding:5px;height:auto">
			<div style="margin-top:5px; padding-bottom:8px; border-bottom:1px solid #ccc;">
				综合查询:
				<input type="text" id="generalSearch" style="width:120px;"/>
				事件描述:
				<input type="text" id="content" style="width:120px;"/>
				事件类型:
				<input type="text" id="eventType" style="width:150px;"/>
				事件类型:
		    	<select name="bigType" id="bigType" class="easyui-combobox" editable="false" data-options="panelHeight:250,width:120">
					<option value="">==请选择==</option>
					<#if bigTypeDC??>
						<#list bigTypeDC as l>
							<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
						</#list>
					</#if>
				</select>
		    	<span> - </span>
		    	<select name="type" id="type" class="easyui-combobox" editable="false" data-options="panelHeight:250,width:130">
					<option value="">==请选择==</option>
				</select>
				
				所属网格:
				<input type="hidden" id="gridId" name="gridId" value="">
				<input type="text" id="gridName" name="gridName" value="" onclick="showSingleMixedGridSelector();" readonly="readonly" style="cursor:pointer"/>
				<a href="###" class="easyui-linkbutton" iconCls="icon-search" onclick="searchData(1)">查询</a>
				<a href="###" class="easyui-linkbutton" iconCls="icon-reload" onclick="resetCondition()">重置</a>
				<a href="###" class="easyui-linkbutton" iconCls="icon-search" onclick="searchMore()" id="buttonMore">显示更多查询条件</a>
			</div>
			
			<div id="searchDialog" style="display:none">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
                    <tr class="item">
                        <td class="itemtit">信息来源</td>
                        <td class="border_b">
                            <select name="source" id="source" class="easyui-combobox" editable="false" data-options="panelHeight:'auto',width:130">
								<option value="">==请选择==</option>
								<#if sourceDC??>
									<#list sourceDC as l>
											<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
									</#list>
								</#if>
							</select>
                        </td>
                    </tr>
                    <tr class="item">
                        <td class="itemtit">影响范围</td>
                        <td class="border_b">
                            <select name="influenceDegree" id="influenceDegree" class="easyui-combobox" editable="false" data-options="panelHeight:'auto',width:130">
								<option value="">==请选择==</option>
								<#if influenceDegreeDC??>
									<#list influenceDegreeDC as l>
										<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
									</#list>
								</#if>
							</select>
                        </td>
                    </tr>
                    <tr class="item">
                        <td class="itemtit">紧急程度</td>
                        <td class="border_b">
                            <select name="urgencyDegree" id="urgencyDegree" class="easyui-combobox" editable="false" data-options="panelHeight:'auto',width:130">
								<option value="">==请选择==</option> 
								<#if urgencyDegreeDC??>
									<#list urgencyDegreeDC as l>
										<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
									</#list>
								</#if>
							</select>
                        </td>
                    </tr>
                    <tr class="item">
                        <td class="itemtit">发生时间</td>
                        <td class="border_b">
                        	<input type="text" class="easyui-datebox easyui-validatebox" editable="false" id="happenTimeStart" name="happenTimeStart"/>
                        	至<input type="text" class="easyui-datebox easyui-validatebox" formatter="" editable="false" id="happenTimeEnd" name="happenTimeEnd"/>
                        </td>
                    </tr>
                    <tr class="item">
                        <td class="itemtit">登记时间</td>
                        <td class="border_b">
                        	<input type="text" class="easyui-datebox easyui-validatebox" editable="false" id="createTimeStart"/>
                        	至<input type="text" class="easyui-datebox easyui-validatebox" formatter="" editable="false" id="createTimeEnd"/>
                        </td>
                    </tr>
                </table>
			</div>
			
			<div style="margin-top:5px;">
				<a href="###" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add()">新增</a>
				<a href="###" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit()">编辑</a>
				<a href="###" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del()">删除</a>
				<a href="###" class="easyui-linkbutton" iconCls="icon-closure" plain="true" onclick="updateAndClosed()">结案</a>
				<a href="###" class="easyui-linkbutton" iconCls="icon-report" plain="true" onclick="saveAndReportDiv()">上报</a>
				
				<#if isNoShow??>
					<#if isNoShow==0>
						<a href="###" class="easyui-linkbutton" iconCls="icon-skipGrade" plain="true" onclick="saveAndReportDiv2()">越级上报</a>
					</#if>
				</#if>
				
				<a href="###" class="easyui-linkbutton" iconCls="icon-toDept" plain="true" onclick="distributary()">分流到部门</a>
				<a href="###" class="easyui-linkbutton" iconCls="icon-toPerson" plain="true" onclick="secondShunt()">分流到人</a>
				<a id="ttip" href="javascript:void(0)" class="easyui-linkbutton easyui-tooltip" style="" data-options="
                    hideEvent: 'none',
                    position: 'left',
                    content: function(){
                        return $('#toolbar');
                    },
                    onShow: function(){
                        var t = $(this);
                        t.tooltip('tip').focus().unbind().bind('blur',function(){
                            t.tooltip('hide');
                        });
                    }
                ">操作帮助</a>
			</div>
			<div style="display:none">
		        <div id="toolbar" style="width:430px;">
		            <a href="javascript:void(0)" class="easyui-linkbutton easyui-tooltip" data-options="iconCls:'icon-report',plain:true">上报：将事件上报到当前用户所属组织的上一级。</a>
		            
					<#if isNoShow?? && isNoShow==0>
		            <a href="javascript:void(0)" class="easyui-linkbutton easyui-tooltip" data-options="iconCls:'icon-skipGrade',plain:true">越级上报：将事件上报到当前用户所属组织的上上级。</a>
		            </#if>
		            
		            <a href="javascript:void(0)" class="easyui-linkbutton easyui-tooltip" data-options="iconCls:'icon-toDept',plain:true">分流到部门：将事件分流到具体的部门，部门内任一人员均可办理该事件。</a>
		            <a href="javascript:void(0)" class="easyui-linkbutton easyui-tooltip" data-options="iconCls:'icon-toPerson',plain:true">分流到人：将事件分流到指定人员，只有该人员才可以办理该事件。</a>
		        </div>
		    </div>
		</div>
		<div region="center" border="false">
			<table id="list"></table>
		</div>
	</div>
	</div>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/customEasyWin.ftl" />
</body>
<#include "/zzgl/event/comboboxSelect.ftl">

<script type="text/javascript">
	var hidd = "${defaultInfoOrgCode}";
	var startGridId = "${startGridId?c}";
	var idStr = "";
	var typeVal = "";
	var cWidth = document.body.clientWidth-670;
	$(function(){
		$('#ttip').css('margin-left',cWidth);
		$('#list').datagrid({
			width:600,
			height:300,
			nowrap: false,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			singleSelect: true,
			idField:'eventId',
			url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/listDraftData.json',
			columns:[[
				{field:'eventId',title:'ID', align:'center',hidden:true},//
				{field:'type',title:'type', align:'center',hidden:true},//
				{field:'opt', title:'操作', width:70, align:'center', formatter:function(value, rec, index){
					var f = '<a href="###" onclick="showDetailRow('+ rec.eventId+ ')">查看</a>&nbsp;';
					return f;
				}},
				{field:'urgencyDegreeName',title:'紧急程度', align:'center',width:65,sortable:true,formatter:function(value,rowData,rowIndex){
						if(value==null)return "";
						if(value == '一般'){
							return '<img width="16px" height="16px" title="一般" src="${rc.getContextPath()}/images/level_1.png">';
						}
						else if(value == '紧急'){
							return '<img width="16px" height="16px" title="紧急" src="${rc.getContextPath()}/images/level_3.png">';
						}
				
				}},
                {field:'eventClass',title:'事件分类', align:'center',width:105,sortable:true},
                {field:'content',title:'事件描述', align:'center',width:250,sortable:true,
                formatter:function(value,rowData,rowIndex){
						if(value==null)return "";
						if(value.length>10){
							value = value.substring(0,10);
						}
						return value;
					}},
				{field:'influenceDegreeName',title:'影响范围', align:'center',width:70,sortable:true},
                {field:'occurred',title:'事发详址', align:'center',width:100,sortable:true},
                {field:'happenTimeStr',title:'发生时间', align:'center',width:80,sortable:true,
                formatter:function(value,rowData,rowIndex){
						if(value!=null && value.length>10){
							value = value.substring(0,10);
						}
						return value;
					}},
				{field:'gridName',title:'所属网格', align:'center',width:70,sortable:true},
                {field:'sourceName',title:'信息来源', align:'center',width:70,sortable:true},
                {field:'involvedNumName',title:'涉及人数', align:'center',width:60,sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{gridId:startGridId},
			onSelect:function(index,rec){
				idStr=rec.eventId;
				typeVal=rec.type;
			},
			onDblClickRow:function(index,rec){
				showDetailRow(rec.eventId);
			}
		});
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
		});
		
		AnoleApi.initEventTypeComboBox("eventType", "content");
		AnoleApi.initGridZtreeComboBox("generalSearch", "content");
		
	});
	
	$('#bigType').combobox({
		onChange:function(newValue,oldValue){
			var bigType = newValue;
			getSmallType(bigType,'');
		}
	});
	
	function resetCondition(){
		$("#generalSearch").val("");
		$("#content").val("");
		$("#bigType").combobox("setValue", "");
		$("#gridId").val("");
		$("#gridName").val("");
		
 		$("#source").combobox("setValue", "");;
		$("#influenceDegree").combobox("setValue", "");
		$("#urgencyDegree").combobox("setValue", "");
	}
	function singleMixedGridSelectCallback(gridId,gridName,orgId,orgCode,gridPhoto) {
		$('#gridId').val(gridId);
		$('#gridName').val(gridName);
	}
	function searchData(b) {
	
		if(b!=null && b!="" && b=="1"){
			$("#source").combobox("setValue", "");;
			$("#influenceDegree").combobox("setValue", "");
			$("#urgencyDegree").combobox("setValue", "");
			$("#happenTimeStart").combobox("setValue","");
			$("#happenTimeEnd").combobox("setValue","");
			$("#createTimeStart").combobox("setValue","");
			$("#createTimeEnd").combobox("setValue","");
		}
	
		var a = new Array();
		var generalSearch = $("#generalSearch").val();
		if(generalSearch!=null && generalSearch!=""){
			a["generalSearch"] = generalSearch;
		}
		var content = $("#content").val();
		if(content!=null && content!="") a["content"]=content;
		var bigType = $('#bigType').combobox('getValue');
		if(bigType!=null && bigType!="") a["bigType"]=bigType;
		var type = $("#type").combobox('getValue');
		if(type!=null && type!="") a["type"]=type;
		var gridId = $("#gridId").val();
		if(gridId!=null && gridId!="") {
			a["gridId"]=gridId;
		} else {
			a["gridId"]=startGridId;
		}
		
		var source = $("#source").combobox('getValue');
		if(source!=null && source!="") a["source"]=source;
		var influenceDegree = $("#influenceDegree").combobox('getValue');
		if(influenceDegree!=null && influenceDegree!="") a["influenceDegree"]=influenceDegree;
		var urgencyDegree = $("#urgencyDegree").combobox('getValue');
		if(urgencyDegree!=null && urgencyDegree!="") a["urgencyDegree"]=urgencyDegree
		var happenTimeStart = $("#happenTimeStart").combobox('getValue');
		if(happenTimeStart!=null && happenTimeStart!=""){
			a["happenTimeStart"] = happenTimeStart;
		}
		var happenTimeEnd = $("#happenTimeEnd").combobox('getValue');
		if(happenTimeEnd!=null && happenTimeEnd!=""){
			a["happenTimeEnd"] = happenTimeEnd;
		}
		if(createTimeStart!=null && createTimeStart!=""){
			a["createTimeStart"] = createTimeStart;
		}
		var createTimeEnd = $("#createTimeEnd").combobox('getValue');
		if(createTimeEnd!=null && createTimeEnd!=""){
			a["createTimeEnd"] = createTimeEnd;
		}
		doSearch(a);
		
		$("#searchDialog").dialog('close');
		
	}
	
	function doSearch(queryParams) {
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		flashData();
	}
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEvent.jhtml';
		showMaxJqueryWindow("新增事件信息", url);
	}
	function del() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
			return;
		}
		$.messager.confirm('提示', '您确定删除选中的事件吗？', function(r){
			if (r){
				modleopen();
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/delEvent.jhtml',
					data: 'idStr='+idStr,
					dataType:"json",
					success: function(data){
						modleclose();
						$.messager.alert('提示', '您成功删除'+data.result+'条事件！', 'info');
						$("#list").datagrid('load');
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
					}
				});
			}
		});
	}
	
	function edit() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
			return;
		}
		//var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toEditEvent.jhtml?eventId='+idStr+'&type='+typeVal;
		var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEvent.jhtml?eventId='+idStr;
		showMaxJqueryWindow("编辑事件信息, url);
	}
	
	//结案
	function updateAndClosed(){
		var rows = $('#list').datagrid('getSelections');
		if(rows.length==0 || rows.length>1){
			$.messager.alert('错误','请选择一条数据来进行结案','error');
			return;
		}
		
		var url = "${rc.getContextPath()}/zzgl/event/outPlatform/showUpdateAndClosedPage.jhtml?eventId=" + rows[0].eventId + "&type="+ rows[0].type +"&result="+encodeURIComponent(encodeURIComponent(rows[0].result));
		showCustomEasyWindow("结案",600,250,url);
	}
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(msg,type){
		if(typeof(type)!=undefined && msg!=undefined && msg!=null && msg!=''){
			$.messager.alert('提示', msg, 'info');
		}
		if(typeof(type)!=undefined && type == 'closed'){
			closeCustomEasyWin();
			$.messager.alert('提示', msg, 'info');
		}else if(typeof(type)!=undefined && type == 'close'){
			closeCustomEasyWin();
		}
		else{//type为update
			closeMaxJqueryWindow();
		}
		
		flashData();
	}
	function reloadDataForDistribute(result,type){
		if(typeof(type)!='undefined' && type == 'closed'){
			closeMaxJqueryWindow();
			$.messager.alert('提示', result, 'info');
		}else if(typeof(type)!='undefined' && type == 'close'){
			closeMaxJqueryWindow();
		}
		else{
			closeCustomEasyWin();
		}
		
		idStr = "";//清除已操作的数据
		$("#list").datagrid('load');
	}
	
	function msgPage(result){
		closeMaxJqueryWindow();
		$.messager.alert('提示', result, 'info');
	}
	function reloadPage(result){
		closeMaxJqueryWindow();
		if(result !='') {
			$.messager.alert('提示', result, 'info');
		}
	}
	//显示上报DIV
	var reportType;
	function saveAndReportDiv(){
		var rows = $('#list').datagrid('getSelections');
		if(rows.length==0 || rows.length>1){
			$.messager.alert('错误','请选择一条数据来进行上报','error');
			return;
		}
		var eventId = rows[0].eventId;
		//城市管理类事件不能进行上报操作
		reportType = 1;
		var url = "${rc.getContextPath()}/zzgl/event/outPlatform/showCreateAndReportPage.jhtml?reportType="+reportType+"&eventId=" +　eventId;
		showCustomEasyWindow("事件上报",580,350,url);
	}
	
	//多级上报
	function saveAndReportDiv2(){
		var rows = $('#list').datagrid('getSelections');
		if(rows.length==0 || rows.length>1){
			$.messager.alert('错误','请选择一条数据来进行多级上报','error');
			return;
		}
		var eventId = rows[0].eventId;
		//城市管理类事件不能进行上报操作
		reportType = 2;
		var url = "${rc.getContextPath()}/zzgl/event/outPlatform/showCreateAndReportPage.jhtml?reportType="+reportType+"&eventId=" +　eventId;
		showCustomEasyWindow("越级上报",580,350,url);
	}
	//一级分流
	function distributary(){
		if(idStr==null || idStr=="") {
			$.messager.alert('提示','请选中数据再执行此操作!','warning');
			return;
		}
		var url = '${rc.getContextPath()}/zzgl/event/outPlatform/showDistributaryPage.jhtml?eventId='+idStr;
		showMaxJqueryWindow("分流到部门", url);
	}
	function secondShunt(){
		var rows = $('#list').datagrid('getSelections');
		if(rows.length==0 || rows.length>1){
			$.messager.alert('错误','请选中数据再执行此操作!','error');
			return;
		}
		var eventId = rows[0].eventId;
		var url = '${rc.getContextPath()}/zzgl/event/outPlatform/showSecondShuntPage.jhtml?eventId='+eventId;
		showMaxJqueryWindow("分流到人", url);
	}
	
	function flashData(){
		idStr = "";								//清除已操作的数据
		$('#list').datagrid('clearSelections');	//清除选择的行
		$("#list").datagrid('load');			//重新加载事件列表
	}
	$('#searchDialog').show().dialog({
                title: '更多查询条件',
                width: 350,
                height: 200,
                closed: true,	
                iconCls: 'icon-search',
                buttons: [{
                    text:'查询',
                    iconCls:'icon-ok',
                    handler:searchData
                },{
                    text:'重置条件',
                    handler:resetCondition
                }]
            });
	
	function searchMore(){
		 $('#searchDialog').dialog('open');
	}
	
	function showDetailRow(eventId){
		if(!eventId){
		    $.messager.alert('错误','请选择一条记录','error');
		}else{
		    var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventId='+eventId;
		    //var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEventWorkflow.jhtml?eventId='+eventId;
		    showMaxJqueryWindow("查看事件信息", url);		
		}
	}
	
	function startWorkFlow(data){
		var formId = data.formId;
		var new_workFlowId = data.workflowId;
		var wftypeId = data.wftypeId;
		var orgCode = data.orgCode;
		var orgType = data.orgType;
		var toClose = data.toClose;
		var json = data.json;
		/*alert("formId="+formId);
		alert("workflowId="+new_workFlowId);
		alert("wftypeId="+wftypeId);
		alert("orgCode="+orgCode);
		alert("orgType="+orgType);
		alert("toClose="+toClose);*/
		
		//var url = 'http://gd.fjsq.org:8081/zzgrid_p/zzgl/flowService/jumpPage.jhtml?instanceId=2330083'+'&workFlowId='+new_workFlowId+'&json='+json+'&formId='+formId;
		//showMaxJqueryWindow("提交", url);
		
		//启动流程
		$.ajax({
			//type: "POST",
			url : '${SQ_ZZGRID_URL}/zzgl/flowService/startFlow.jhtml',
			data: {'formId': formId ,'workFlowId': new_workFlowId,'wftypeId': wftypeId, 'orgCode': orgCode, 'orgType': orgType, 'toClose': toClose},
			dataType:"json",
			success: function(data){
				modleclose();
			    if(data.result){
			    	var instanceId=data.instanceId;
			    	
			    	if(instanceId!=undefined && instanceId!=null && instanceId!=""){
				    	$.ajax({
							type: "POST",
				    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/updateEvent.jhtml',
							data: 'eventId='+formId+'&status=00',
							dataType:"json",
							success: function(data){
								if(data.result){
									//alert("instanceId===="+instanceId);
				    				var url = '${SQ_ZZGRID_URL}/zzgl/flowService/jumpPage.jhtml?instanceId='+instanceId+'&workFlowId='+new_workFlowId+'&json='+json+'&formId='+formId+'&eventType=event';
				    				showMaxJqueryWindow("办理窗口", url);
									//$.messager.alert('提示','更新成功！','info');
								}else{
									$.messager.alert('错误','连接错误！','error');
								}
							},
							error:function(data){
								$.messager.alert('错误','连接错误！','error');
							}
				    	});
			    	}
			    	
			    	
			    }
				else{
				  $.messager.alert('错误','启动失败！','error');
				}
			},
			error:function(data){
				$.messager.alert('错误','启动失败！','error');
			}
		});
	}
</script>

</body>
</html>