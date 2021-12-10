<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列表</title>
<#include "/component/commonFiles-1.1.ftl" />
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
	<#include "/map/menuconfigure/menuConfigDataGridToolbar.ftl" />
</div>

<div id="relatedEventsContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
	<table id="list"></table>
</div>

<script type="text/javascript">
    
	var idStr = "";
    var gridFlag="0";
    var relatedEventsList = null;
    var gridCode = "<#if gridCode??>${gridCode}</#if>";
    var gridId = <#if gridId??>${gridId?c}<#else>-99</#if>
    
    $(function(){
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
			idField:'pgIdxCfgId',
			url:'${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/listPageIndexCfgData.json',
			columns:[[
				{field:'pgIdxCfgId',title:'ID', align:'center',hidden:true},//
				{field:'gridName',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true},
				{field:'pgIdxTypeName',title:'地图类型', align:'center',width:fixWidth(0.2),sortable:true},
				{field:'displayStyle',title:'展示方式', align:'center',width:fixWidth(0.2),sortable:true,
	                formatter:function(value,rowData,rowIndex){
						if(value!=null && value=='0'){
							value = "平铺";
						}else if(value!=null && value=='1'){
							value = "树形";
						}
						return value;
					}
				 },
				{field:'status',title:'状态', align:'center',width:fixWidth(0.2),sortable:true,
	                formatter:function(value,rowData,rowIndex){
						if(value!=null && value=='1'){
							value = "启用";
						}else if(value!=null && value=='2'){
							value = "禁用";
						}
						return value;
					}
				 }
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{bizType:'1',<#if !admin>gridCode:gridCode,</#if>gridId:gridId,orgCode:gridCode},
			onSelect:function(index,rec){
				idStr=rec.pgIdxCfgId;
			},
			onLoadSuccess: function(data){
				$('#list').datagrid('clearSelections');	//清除掉列表选中记录
 			    if(data.total==0){
 				  	var body = $(this).data().datagrid.dc.body2;
 					body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/images/nodata.png" title="暂无数据"/></div>');
 				}else{
 		          $('.datagrid-body-inner').eq(0).removeClass("l_elist");
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
    
	function searchData(b) {
		var a = new Array();
		var pgIdxType = $("#pgIdxType").val();
		if(pgIdxType!=null && pgIdxType!=""){
			a["pgIdxType"] = pgIdxType;
		}
		var startGridId = $("#startGridId").val();
		if(startGridId!=null && startGridId!=""){
			a["gridId"] = startGridId;
		}else if(gridId != -99){
			a["gridId"] = gridId;
		}
		var regionCode = $("#regionCode").val();//alert(regionCode);
		if(regionCode!=null && regionCode!=""){
			a["gridCode"] = regionCode;
		}<#if !admin>else{
			a["gridCode"] = gridCode;
		}</#if>
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
		    var url = "${rc.getContextPath()}/zhsq/relatedEvents/RelatedEventsController/detailRelatedEvents.jhtml?reId="+reId;
		    //showMaxJqueryWindow("查看涉事案件信息", url, 800, 406, false, 'auto');
		    showMaxJqueryWindow("查看涉事案件信息", url, 800, 370);
		}
	}
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/customEasyWin.ftl" />

</body>
</html>