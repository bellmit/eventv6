<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>命案防控信息</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<script type="text/javascript" src="${COMPONENTS_DOMAIN}/js/rs/orgtreeSet.js"></script>
	<script type="text/javascript" src="${RS_DOMAIN}/js/resident_common.js"></script>
	<style>
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
<body class="easyui-layout" fit="true" id="layoutArea">

<div class="MainContent">
	<#include "/zzgl/homicideCase/toolbar_homicideCase.ftl" />
</div>

<div id="homicideCaseDiv" region="center" border="false" style="overflow:hidden;">
	<table id="list"></table>
</div>

<#include "/component/singleMaxJqueryEasyUIWin.ftl" />

<script type="text/javascript"> 
	var infoOrgCode = "${infoOrgCode!}";
	var  isOuterregionCode = null;
	$(function(){
		
//		renderTree(null, startGridId, false);//调用树
		orgtreeset = new OrgTreeSet({
			treStyle:'${treStyle}',
			layoutId:'layoutArea',
			topSearchUl:'topSearchUl',
			startGridName:'${gridName}',
			dataDomian:'${COMPONENTS_DOMAIN}',
			defaultOrgCode:'${infoOrgCode}',
			onClickCB:function(rec){
			    $("#infoOrgCode").val(rec.attributes.orgCode);
				searchData();
			},
			resetCB:function(){
				orgCode = "${infoOrgCode}";
			}
       });

		
		<#if isOuter?? && isOuter>
			isOuterregionCode = '<#if isOuterregionCode??>${isOuterregionCode!}</#if>';
			if(isOuterregionCode) {
				$("#gridName").attr('disabled', true);
			}
			$("#jqueryToolbar .ToolBar").remove();//移除按钮
		</#if>
		
		if(infoOrgCode == "") {
			$('#homicideCaseDiv').html("<table id='list'><tr><td style='color:red;'>没有网格，无法载入数据</td></tr></table>");
		} else {
			loadDataList();
		}
		
	})
	
	function loadDataList() {
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
			idField:'hashId',
			url:'${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/listHomicideCaseData.json',
			columns:[[
				{field:'hashId',checkbox:true,width:40,hidden:'true'},
				{field:'reName',title:'案件名称', align:'left',width:fixWidth(0.27),sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value == null){
							value = "";
						}
						var f = '<a class="eName" href="###" title="'+ rec.reName +'" onclick="detail(\'' + rec.hashId + '\')")>'+value+'</a>';
						return f;
					}	
				},
                {field:'reNo',title:'案件编号',align:'center',width:fixWidth(0.10),sortable:true},
                {field:'gridPath',title:'所属网格',align:'center',width:fixWidth(0.10),sortable:true,
                    formatter:function(value,rowData,rowIndex){
                        if(value==null)return "";
                        var tag='';
                        tag = '<div title="'+rowData.gridPath+'">'+value+'</div>';
                        return tag;
                    }
				},
                {field:'occuDateStr',title:'发生开始日期',align:'center',width:fixWidth(0.06),sortable:true},
                {field:'spyEndDateStr',title:'侦查结束日期',align:'center',width:fixWidth(0.06),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{gridCode:infoOrgCode, bizType:$("#bizType").val()},
			onDblClickRow:function(index,rec){
				detail(rec.hashId);
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
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
		});
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result){
		try{
			//closeMaxJqueryWindow();
			closeWinExt()
		}catch(e){}
		DivShow(result);
		searchData();
	}
</script>
</body>
</html>