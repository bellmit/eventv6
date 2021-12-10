<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>联合办理/联席交办列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout">
	<div class="MainContent">
		<#include "/zzgl/event/eventExtend/toolbar_eventEx.ftl" />
	</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>

	<script type="text/javascript">
	    $(function(){
	    	loadDataList();
	    });
		
	    function loadDataList(){
			var queryParams = queryData();
			
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
				url:'${rc.getContextPath()}/zhsq/eventExtend/listEventExData.json',
				columns:[[
					{field:'eventId',checkbox:true,width:40,hidden:'true'},
					{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true, formatter: clickFormatter},
					{field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
					{field:'handleDateStr',title:'办结期限', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
	                {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.2),sortable:true},
					{field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
					{field:'eventStatusName',title:'当前状态', align:'center',width:fixWidth(0.12),sortable:true},
					{field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter}
				]],
				toolbar:'#jqueryToolbar',
				pagination:true,
				pageSize: 20,
				queryParams:queryParams,
				onDblClickRow:function(index,rec){
					detail(rec.eventId);
				},
				onLoadError: function () {//数据加载异常
	        		$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
	    		},
				onLoadSuccess: function(data){//案件标题内容左对齐
					if(data.total == 0) {
						$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
					} else {
						var panel = $(this).datagrid('getPanel'),
							tr = panel.find('.datagrid-view2 tr.datagrid-row'),//获取展示的列表行
			            	trLen = 0,
			            	trHeight = 0,
			            	leftOffset = 0,
			            	dataViewHeight = 0,
			            	trIndexMax = 0;
			            
			            if(tr) {
			            	trLen = tr.length;
			            	trHeight = tr.eq(0).outerHeight(true);//单行高度
			            	leftOffset = tr.eq(0).find('td[field]:visible').eq(0).width() / 5;//首列宽度的1/5
			            } 
			            
		             	dataViewHeight = $('.datagrid-view').height();//获取列表的高度(包括列表标题)
		             	trIndexMax = parseInt(dataViewHeight / trHeight, 10) - 1;//扣除一行，表行索引从0开始
		             	
			            tr.each(function(i){
			            	var operateNotice = $(this).find('.OperateNotice');
			            	
			                $(this).mouseover(function() {  
			                	var trOffset = $(this).offset(),
			                		scrollTop = $(".datagrid-body").scrollTop(),
			                		arrowHeight = $(this).find('.arrow').eq(0).outerHeight(true);//箭头高度
			                	
			                	if(i==(trLen - 1) && i != 0 && (scrollTop > 0 || trIndexMax == i)){//有滚动条或者无滚动条时能展示的最大行数的最后一行
			                		$(this).find('.arrow').css({'top': '28px', 'background': 'url(${rc.getContextPath()}/images/arrow2.png)'});
			                		
			                		operateNotice.css('top', $(this).position().top - trHeight + arrowHeight);
			                	} else {
			                		operateNotice.css('top', $(this).position().top  + trHeight);
			                	}
			                	
			                	operateNotice.css('left', $(this).position().left + leftOffset).show();
			                }); 
			                
			                $(this).mouseout(function(){  
			                	operateNotice.hide();
			                }); 
			            }); 
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
		
		function clickFormatter(value, rec, rowIndex) {
			var title = "",
				operate = "",
				listType = $("#listType").val();
			
			if(value) {
				switch(listType) {
					case '2': {//我的被督查督办
						operate = '<div class="OperateNotice" style="display:none"><div class="operate"><ul><li class="duban" onclick="addTimeApplication('+ rec.eventId + ', 2);" ) >申请延时</li></ul><div class="arrow"></div></div></div>';
						break;
					}
					case '3': {//辖区内超时
						operate = '<div class="OperateNotice" style="display:none"><div class="operate"><ul><li class="duban" onclick="addTimeApplication('+ rec.eventId + ', 1);" ) >督查督办</li></ul><div class="arrow"></div></div></div>';
						break;
					}
					case '4': {//待办
						operate = '<div class="OperateNotice" style="display:none"><div class="operate"><ul><li class="duban" onclick="addTimeApplication('+ rec.eventId + ', 3);" ) >申请延时</li></ul><div class="arrow"></div></div></div>';
						break;
					}
				}
				
				title += operate + '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.eventId + '\')">'+ value +'</a>';
			}
			
			return title;
		}
		
		function titleFormatter(value, rowData, rowIndex) {
			var title = "";
			
			if(value) {
				title = '<span title="'+ value +'">'+ value +'</span>';
			}
			
			return title;
		}
		
		function dateFormatter(value, rowData, rowIndex) {
			if(value && value.length >= 10) {
				value = value.substring(0, 10);
			}
			
			return value;
		}
	</script>
</body>
</html>