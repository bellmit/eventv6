<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><@block name="eventAncestorListPageTitle">事件列表历史关联列表</@block></title>
		<#include "/component/commonFiles-1.1.ftl" />
		<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
		
		<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	</head>
	
	<body class="easyui-layout">
		<div class="MainContent">
			<div id="jqueryToolbar"></div>
		</div>
		<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
			<table style="width:100%" id="list"></table>
		</div>
	
		<script type="text/javascript">
		    $(function() {
		    	loadDataList();
		    });
			
		    function loadDataList(){
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
					url : <@block name="eventAncestorListUrl">'${rc.getContextPath()}/zhsq/event/eventDisposalController/listEventAncestorData.json'</@block>,
					columns:[[
						{field:'eventId',title:'ID', align:'center',hidden:true},//
						{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,formatter: clickFormatter},
						{field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
		                {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.2),sortable:true},
						{field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true,formatter: titleFormatter},
		                {field:'eventStatusName',title:'当前状态', align:'center',width:fixWidth(0.12),sortable:true}
					]],
					toolbar:'#jqueryToolbar',
					pagination:true,
					pageSize: 20,
					queryParams: <@block name="eventAncestorListQueryParam">{'eventId': '${eventId?c}'}</@block>,
					onDblClickRow:function(index,rec){
						detail(rec.eventId);
					},
					onLoadError: function () {//数据加载异常
		        		$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
		    		},
					onLoadSuccess: function(data){//事件标题内容左对齐
						if(data.total == 0) {
							$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
						}
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
		    }
		    
			function detail(eventId){
				if(!eventId){
				    $.messager.alert('提示','请选择一条记录！','info');
				}else{
					var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId="+eventId;
					
					if(parent && parent.showMaxJqueryWindow) {
			    		parent.showMaxJqueryWindow("查看事件信息", url);
			    	} else {
			    		openJqueryWindowByParams({
			    			maxWidth: 1000,
			    			title: "查看事件信息",
			    			targetUrl: url
			    		});
			    	}
				}
			}
			
			function clickFormatter(value, rec, rowIndex) {
				var title = "";
				
				if(value) {
					title = '<a class="eName" href="###" title="'+ value +'" onclick=detail("'+ rec.eventId+ '")>'+value+'</a>';
				}
				
				return title;
			}
			
			function titleFormatter(value, rec, rowIndex) {
				var title = "";
				
				if(value) {
					title = "<span title='"+ value +"'>"+ value +"</span>";
				}
				
				return title;
			}
			
			function dateFormatter(value, rowData, rowIndex) {
				if(value && value.length >= 10) {
					value = value.substring(0,10);
				}
				
				return value;
			}
		</script>
	</body>
</html>