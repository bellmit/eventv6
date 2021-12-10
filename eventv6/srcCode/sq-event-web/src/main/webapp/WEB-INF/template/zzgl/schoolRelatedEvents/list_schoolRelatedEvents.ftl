<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>涉及案件列表</title>
<#include "/component/standard_common_files-1.1.ftl" />
<script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/orgtreeSet.js"></script>
<style type="text/css">
.combo-arrow{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.combo-arrow:hover{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.combo-arrow{opacity:1;}
.textbox-icon{opacity:1;}
.combo{vertical-align:top;}
.combo:hover{border:1px solid #7ecef4; box-shadow:#7ecef4 0 0 5px;}
div.datagrid-cell {
    text-align: left !important;
    font-size: 13px;
    padding: 0px 10px;
    display: block;
    white-space: none;
    overflow: hidden;
    text-overflow: clip;
}
</style>
</head>
<body class="easyui-layout" id="layoutArea">
<input type="hidden" id="bizId" name="bizId" value="" />
<input type="hidden" id="bizName" value="" />
	
<div class="MainContent">
	<#include "/zzgl/schoolRelatedEvents/relatedEventsDataGridToolbar.ftl" />
</div>

<div region="center" id="mainTitle"  border="false" style="overflow:hidden;">
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
    var gridName = "${gridName!''}";
    var nature = "<#if nature??>${nature}</#if>";
    var occuDateStartStr = '${occuDateStartStr!}', occuDateEndStr = '${occuDateEndStr!}';
    
    $(function(){
         
         orgtreeset = new OrgTreeSet({
			treStyle:"${treStyle!'top'}",
			layoutId:'layoutArea',
			topSearchUl:'topSearchUl',
			startGridName:gridName,
			dataDomian:'${COMPONENTS_URL}',
			defaultOrgCode:gridCode,
			onClickCB:function(rec){
				$('#gridCode').val(rec.attributes.orgCode);
				$('#startGridId').val(rec.id);
				if('left'=='${treStyle}'){
					searchData(1);
				}
			},
			resetCB:function(){
				if('left'!='${treStyle}'){
			    	orgCode = orgCode;
				}
			}
    	});
	    
	    loadDataList();
	});
	
    function loadDataList(){
    	var queryParams = {bizType:'${bizType}',gridCode:gridCode,gridId:gridId,nature:nature};
    	if(occuDateStartStr) {
    		queryParams.occuDateStartStr = occuDateStartStr;
    	}
    	if(occuDateEndStr) {
    		queryParams.occuDateEndStr = occuDateEndStr;
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
			url:'${rc.getContextPath()}/zhsq/relatedEvents/SchoolRelatedEventsController/listRelatedEventsData.json',
			columns:[[
				{field:'reId',title:'ID', align:'center',hidden:true},//
				{field:'reName',title:'案(事)件名称', align:'left',width:fixWidth(0.15),sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value!=null && value.length>10){
							value = value.substring(0,10);
						}
						var f = '<a href="###" title='+ rec.reName +' onclick="showDetailRow('+ rec.reId +')">'+value+'</a>';
						return f;
					}
				},
				
				{field:'occuDateStr',title:'发生日期', align:'center',width:fixWidth(0.1),sortable:true,
	                formatter:function(value,rowData,rowIndex){
						if(value!=null && value.length>10){
							value = value.substring(0,10);
						}
						return value;
					}
				 },
				{field:'bizName',title:'所在学校', align:'center',width:fixWidth(0.2),sortable:true,
					formatter:function(value,rowData,rowIndex){
						if(value!=null && value.length>10){
							value = value.substring(0,10);
						}
						return value;
					}
				},
				{field:'situation',title:'案件情况', align:'center',width:fixWidth(0.15),sortable:true,formatter: titleFormatter},
                {field:'detectedOverview',title:'侦破情况', align:'center',width:fixWidth(0.15),sortable:true,formatter: titleFormatter},
				{field:'isDetectionName',title:'是否破案', align:'center',width:fixWidth(0.1),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
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
		var a = new Array();
		if (nature != null && nature != "") {
			a["nature"] = nature;
		}
		a["bizType"] = "<#if bizType??>${bizType}<#else>2</#if>";//默认参数
		var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="案(事)件名称/案件情况/侦破情况") {
			a["keyWord"]=keyWord;
		}
		var bizId = $("#bizId").val();
		if(bizId!=null && bizId!="") {
			a["bizId"]=bizId;
		}
		
		var startGridId = $("#startGridId").val();
		if(startGridId!=null && startGridId!=""){
			a["gridId"] = startGridId;
		}else if(gridId != -99){
			a["gridId"] = gridId;
		}
		
		//高级查询
		var reNo = $("#reNo").val();
		if(reNo!=null && reNo!=""){
			a["reNo"]=reNo;
		}
		var isDetection = $("#isDetection").val();
		if(isDetection!=null && isDetection!=""){
			a["isDetection"] = isDetection;
		}
		var gridCode = $('#gridCode').val();
		if(gridCode != ""){
			a["gridCode"] = gridCode;
		}
		
		if(occuDateStartStr) {
    		a["occuDateStartStr"] = occuDateStartStr;
    	}
    	if(occuDateEndStr) {
    		a["occuDateEndStr"] = occuDateEndStr;
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
		    var url = "${rc.getContextPath()}/zhsq/relatedEvents/SchoolRelatedEventsController/detailRelatedEvents.jhtml?reId="+reId;
		    var opt = $.extend({}, _win_opt, {
		    	title: "查看涉事案件信息",
		    	targetUrl: url,
		    	width: 850,
		    	height:window.screen.availWidth
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
				var url = "${rc.getContextPath()}/zhsq/relatedEvents/RelatedEventsController/detailRelatedEvents.jhtml?reId="+row.reId;
				return url;
			}
		} else {
			throw new Error("当前页未找到事件reId=" + reId + "的记录！");
		}
	}
	
	function titleFormatter(value, rowData, rowIndex) {
            var title = "";
            
            var valueShow="";
            
            valueShow=(null!=value&&value.length>20)?value.substring(0,20):value;

            if(value) {
                title = '<span title="'+ value +'" >'+ valueShow +'</span>';
            }

            return title;
        }
</script>

</body>
</html>