<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><@block name="eventOrgListPageTitle">经办组织事件列表</@block></title>
	<#include "/component/commonFiles-1.1.ftl" />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
</head>
<body class="easyui-layout">
	<div class="MainContent">
		<#include "/zzgl/event/workflow/select_user.ftl" />
		<@block name="eventOrgToolbar">
			<#include "/zzgl/event/eventOrg/toolbar_event_org.ftl" />
		</@block>
	</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>

<script type="text/javascript">
    $(function(){
    	loadDataList();
    });
    
    function loadDataList(){
    	var queryParam = queryData();
    	
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'eventId',
			url:'${rc.getContextPath()}/zhsq/event/eventOrg/listEventOrgData.json',
			columns:[[
				{field:'eventId',title:'ID', align:'center',hidden:true},
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.25),sortName:'T1.EVENT_NAME',sortable:true,
					formatter:function(value,rec,rowIndex){
                		var urgency = rec.urgencyDegree;
                		var urgencyName = rec.urgencyDegreeName;
                		var urgencyPic = "";
                		var handleStatus = rec.handleDateFlag;
                		
						if(urgencyName!=null && urgency!='01'){
							urgencyPic += '<i title="'+ urgencyName +'" class="ToolBarUrgency"></i>';
						}
						
                		if(value==null){
                			value="";
                		}
                		if(value.length>20){
							value = value.substring(0,20);
						}
						
						var handlePic = "";
						if(handleStatus == '2'){
							handlePic = '<i title="将到期" class="ToolBarDue"></i>';
						} else if(handleStatus == '3'){
							handlePic = '<i title="已过期" class="ToolBarOverDue"></i>';
						}
						
						var influencePic = "";
						if(rec.remindStatus=='2' || rec.remindStatus=='3'){
							influencePic = '<img src="${rc.getContextPath()}/images/duban2.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
						}
						if(rec.influenceDegree == '04'){
							influencePic += "<b class='FontRed'>[重大]</b>";
						}
						
                		var f = influencePic+'<a class="eName" href="###" title="'+ rec.eventName +'" onclick=detail('+ rec.eventId+ ','+rec.instanceId+')>'+value+'</a>'+urgencyPic+handlePic;
						return f;
					} 	
				},
				{field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.12),sortName:'T1.HAPPEN_TIME',sortable:true},
				{field:'handleDateStr',title:'办结期限', align:'center',width:fixWidth(0.12),sortName:'T1.HANDLE_DATE',sortable:true},
				{field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.15),sortName:'T1.TYPE_',sortable:true, formatter: titleFormatter},
				<@block name="gridPathField">
				{field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.18), formatter: titleFormatter},
				</@block>
				{field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.08)},
				{field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortName:'T1.CREATE_TIME',sortable:true, formatter: dateFormatter}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams: queryParam,
			onSelect:function(index,rec){
			},
			onSortColumn: function(fieldId, order) {
				var sortColumn = $('#list').datagrid('getColumnOption', fieldId),
					gridLevel = $('#gridLevel').val(),
					remotSortGridLevel = "${remotSortGridLevel!},";
			
				if(remotSortGridLevel.indexOf(gridLevel) > 0 && sortColumn.sortName) {
					$('#orderByField').val(sortColumn.sortName + ' ' + order.toUpperCase());
					conditionSearch();
				}
			},
			onDblClickRow:function(index,rec){
				detail(rec.eventId, rec.instanceId);
			},
			onLoadError: errorResult,
			onLoadSuccess: function(data) {//事件标题内容左对齐
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				} else {
	            	addIcons();
	            }
	        }
		});
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50,100,200],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
		});
    }
	
	<@block name="function_addIcons">
	function addIcons() {
		<@block name="function_addIcons_body">
		var iconDivLength = $("#iconDiv").length;
		if(iconDivLength == 0) {
			var icons = 
			'<div id="iconDiv" class="fl">'+
				'<a href="###" id="_allSearchAnchor" class="icon_select" onclick="iconSearchData(0, this);"><i class="ToolBarAll"></i>所有</a>'+
				'<a href="###" onclick="iconSearchData(1, this);"><i class="ToolBarUrgency"></i>紧急</a>'+
				'<a href="###" onclick="iconSearchData(2, this);"><i class="ToolBarDue"></i>将到期</a>'+
				'<a href="###" onclick="iconSearchData(3, this);"><i class="ToolBarOverDue"></i>已过期</a>'+
				'<a href="###" onclick="iconSearchData(4, this);"><i class="ToolBarNormal"></i>正常</a>'+
			'</div>';
			$('.ToolBar').append(icons);
		}
		</@block>
    }
    </@block>
    
    function dateFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 10) {
			value = value.substring(0,10);
		}
		
		return value;
	}
	
	function titleFormatter(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'">'+ value +'</span>';
		}
		
		return title;
	}
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />


</body>
</html>