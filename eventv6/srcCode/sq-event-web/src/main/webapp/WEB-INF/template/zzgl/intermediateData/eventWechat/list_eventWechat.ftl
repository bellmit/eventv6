<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><@block name="eventWechatListTitle">微信事件列表</@block></title>
	<#--<#include "/component/commonFiles-1.1.ftl" />-->
	<#include "/component/standard_common_files-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
	<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/custom_msgClient.js" type="text/javascript" charset="utf-8"></script> 
<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/custom_msgClient_parent.js" type="text/javascript" charset="utf-8"></script> 
</head>
<body class="easyui-layout">

<@block name="toolbar"> 
<div class="MainContent">
	<#include "/zzgl/intermediateData/eventWechat/toolbar_eventWechat.ftl" />
</div>
</@block>

<div id="eventWechatDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
	<table id="list" style="width:100%"></table>
</div>

<script type="text/javascript">
	$(function() {
		loadDataList();
	});
	
	function loadDataList() {
		var queryParam = fetchQueryParams();
		
		$('#list').datagrid({
			width:600,
			height:600,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,//让列宽自适应窗口宽度
			singleSelect: true,
			idField:'eventVerifyHashId',
			url:'${rc.getContextPath()}/zhsq/eventWechat/listData.json',
			columns:[[
				{field:'eventVerifyHashId',checkbox:true,width:40,hidden:'true'},
				<@block name="detailToEventName">
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value == null){
							value = "";
						}
						var f = '<a class="eName" href="###" title="'+ rec.eventName +'" onclick="detail(\'' + rec.eventVerifyHashId + '\')")>'+value+'</a>';
						return f;
					}	
				},
				</@block>
				<@block name="occurredColumnBlock">
				{field:'occurred',title:'事发地址',align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
				</@block>
				<#if EVENT_VERIFY_BIZPLATFORM_PCODE!=null&&EVENT_VERIFY_BIZPLATFORM_PCODE!="">
				{field:'bizPlatformName',title:'平台来源',align:'center',width:fixWidth(0.1),sortable:true, formatter: titleFormatter},
				</#if>
				<@block name="happenTimeColumnBlock">
				{field:'happenTimeStr',title:'事发时间',align:'center',width:fixWidth(0.1),sortable:true,
					formatter:function(value, rowData, rowIndex){
						if(value != null && value.length >= 10){
							value = value.substring(0,10);
						}
						return value;
					}
				},
				</@block>
				{field:'contactUser',title:'上报人员',align:'center',width:fixWidth(0.1),sortable:true, formatter: titleFormatter},
                {field:'tel',title:'联系电话',align:'center',width:fixWidth(0.1),sortable:true},
                {field:'gridPath',title:'所属网格',align:'center',width:fixWidth(0.18),sortable:true, formatter: titleFormatter},
                {field:'statusName',title:'状态',align:'center',width:fixWidth(0.05),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParam,
			onDblClickRow:function(index,rec){
				detail(rec.eventVerifyHashId);
			},
			onSelect: function(index,rec) {
				authority(rec);
			},
			onLoadSuccess:function(data){
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
			onLoadError:function(){
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
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
				$(this).pagination('loaded');
			}*/
		});
	}
	
	function titleFormatter(value, rec, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'">'+ value +'</span>';
		}
		
		return title;
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result, isCurrent){
		closeMaxJqueryWindow();
		
		if(result) {
			DivShow(result);
		}
		
		searchData(isCurrent);
	}
	
</script>
</body>
</html>