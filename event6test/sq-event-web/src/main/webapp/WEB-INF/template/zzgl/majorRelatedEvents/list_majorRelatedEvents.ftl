<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>涉及案件列表</title>
<#include "/component/standard_common_files-1.1.ftl" />
<script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
<style type="text/css">
.combo-arrow{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.combo-arrow:hover{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.combo-arrow{opacity:1;}
.textbox-icon{opacity:1;}
.combo{vertical-align:top;}
.combo:hover{border:1px solid #7ecef4; box-shadow:#7ecef4 0 0 5px;}
</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="bizId" name="bizId" value="" />
<input type="hidden" id="bizName" value="" />

<div class="MainContent">
	<#include "/zzgl/majorRelatedEvents/relatedEventsDataGridToolbar.ftl" />
</div>

<div id="relatedEventsContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
	<table id="list"></table>
</div>

<script type="text/javascript">
    var _win_opt = {
    	"padding_left" : 10,
    	"padding_right" : 10,
    	"padding_top" : 10,
    	"padding_bottom" : 10
    };
	var idStr = "";
    var gridFlag="0";
    var relatedEventsList = null;
    var gridCode = "<#if gridCode??>${gridCode}</#if>";
    var gridId = <#if gridId??>${gridId?c}<#else>-99</#if>;
    var eventType = null, eventLevel = null,regionCode=null;
    $(function(){
    	<#if isOuter?? && isOuter>
			eventType = '<#if isOuterrelatedEventsForSearch??>${isOuterrelatedEventsForSearch.eventType!}</#if>';
			eventLevel = '<#if isOuterrelatedEventsForSearch??>${isOuterrelatedEventsForSearch.eventLevel!}</#if>';
			regionCode='<#if regionCode??>${regionCode!}</#if>';
			if(eventType) {
				$("#eventTypeName").attr('disabled', true);
			}
			if(eventLevel) {
				$("#eventLevelName").attr('disabled', true);
			}
			$("#jqueryToolbar .ToolBar").remove();//移除按钮
		</#if>
	     loadDataList();
	});
	
    function loadDataList(){
    var queryParams = getQueryData();
    if(eventType) {
			queryParams.eventType = eventType;
		}
	if(eventLevel) {
			queryParams.eventLevel = eventLevel;
		}
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
			url:'${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/listRelatedEventsData.json',
			columns:[[
				{field:'reId',title:'ID', align:'center',hidden:true},//
				{field:'reName',title:'案(事)件名称', align:'left',width:'18%',sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value == null) return "";
						var f = '<a href="###" title='+ rec.reName +' onclick="showDetailRow('+ rec.reId +')">'+value+'</a>';
						return f;
					}
				},
				{field:'occuDateStr',title:'发生日期', align:'center',width:'8%',sortable:true,
	                formatter:function(value,rowData,rowIndex){
						if(value!=null && value.length>10){
							value = value.substring(0,10);
						}
						return value;
					}
				 },
				{field:'eventTypeCN',title:'案件类型', align:'center',width:'8%',sortable:true},
				{field:'eventLevelCN',title:'案件分级', align:'center',width:'6%',sortable:true},
				{field:'gridPath',title:'所属网格', align:'center',width:'18%',sortable:true,
                    formatter:function(value, rec, index){
                        if(value == null)return "";
                        return '<div title="'+rec.gridPath+'" >'+value+'</div>'
                    }
                },
				{field:'situation',title:'案件情况', align:'center',width:'20%',sortable:true},
				{field:'occuAddr',title:'发生地详址', align:'center',width:'20%',sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams: queryParams,
			onSelect:function(index,rec){
				idStr=rec.reId;
			},
			onDblClickRow:function(index,rec){
				showDetailRow(rec.reId);
			},
			onLoadSuccess: function(data){
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
				var panel = $(this).datagrid('getPanel');  
				var tr = panel.find('div.datagrid-body tr');  
	            tr.each(function(i){
	            	var td = $(this).children('td[field="reName"]');
	                td.children("div").css({
	                	"text-align": "left"  
	                });
	            });  
	            
	            relatedEventsList = $('#list').datagrid("getRows"); 
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
    
	function searchData(b) {
		var a = getQueryData();
		var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="案(事)件名称/案件情况") {
			a["keyWord"]=keyWord;
		} else {
			a["keyWord"]="";
		}
		doSearch(a);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		flashData();
	}
	
	function flashData(msg, type){
		if(msg!=undefined && msg!=null && msg!=""){
			$.messager.alert('提示',msg,'info');
			if(type!=undefined && type!=null){
				if(type == "1"){
					closeMaxJqueryWindow();
				}else if(type == "2"){
					closeCustomEasyWin();
				}
			}
		}
		idStr = "";								//清除已操作的数据
		$('#list').datagrid('clearSelections');	//清除选择的行
		$("#list").datagrid('load');			//重新加载事件列表
	}
	
	function showDetailRow(reId){
		if(!reId){
		    $.messager.alert('提示','请选择一条记录！','info');
		}else{
		    var url = "${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/detailRelatedEvents.jhtml?reId="+reId;
		    var opt = $.extend({}, _win_opt, {
		    	title: "查看涉事案件信息",
		    	targetUrl: url,
		    	width: 650,
		    	maxHeight: 406
		    });
		    openJqueryWindowByParams(opt);
		}
	}
	
	function nextColumn(reId) {//获取下一条事件
		return getNextOrPrevUrl("next", reId);
	}
	
	function prevColumn(reId) {//获取上一条事件
		return getNextOrPrevUrl("prev", reId);
	}
	
	function getNextOrPrevUrl(type, reId) {
		var index = -1;
		var rows = relatedEventsList;
		var length = rows.length;
		$.each(rows, function(i, row) {
			if (row.reId == reId) {
				index = i;
				return false;
			}
		});
		if (index != -1) {
			if (index == 0 && type == "prev") {
				return "";
			} else if (index == length - 1 && type == "next") {
				return "";
			} else {
				index = type == "prev" ? index - 1 : index + 1;
				var row = rows[index];
				var url = "${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/detailRelatedEvents.jhtml?reId="+row.reId;
				return url;
			}
		} else {
			throw new Error("当前页未找到事件reId=" + reId + "的记录！");
		}
	}
</script>

</body>
</html>