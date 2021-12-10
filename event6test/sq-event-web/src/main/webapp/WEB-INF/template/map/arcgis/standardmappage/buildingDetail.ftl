<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>楼房-详情</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<style>
	li{
		margin:0; padding:0; list-style:none;
	}
</style>
</head>
<body>
<div id="jqueryToolbar" style="padding:5px;height:auto">
	<div style="margin-top:5px; padding-bottom:5px;">
	人口性质：<select id="type" class="easyui-combobox" data-options="panelHeight:null,width:120" editable="false">
	  		<option value="">--全部--</option>
	  		  <#if typeDC??>
				<#list typeDC as l>
					<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
				</#list>
			</#if>
	  	</select>
	  管理：<select id="manageState" class="easyui-combobox" data-options="panelHeight:200,width:120" editable="false">
	  		<option value="">--全部--</option>
	  		<option value="001">吸毒人员 </option>
	  		<option value="002">矫正人员</option>
	  		<option value="003">上访人员</option>
	  		<option value="004">刑释解教人员</option>
	  		<option value="005">危险品从业员</option>
	  		<option value="006">精神障碍患者人员</option>
	  		<option value="007">邪教人员 </option>
	  	</select>
	<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="searchAll();">查询</a>
	<a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="resetCondition();">重置</a>
	</div>
</div>
<div id="infoTabs" class="easyui-tabs" fit="true" border="false" style="margin: 0; height:500px;">
	<div title="楼宇简介" style="margin:0;">
		<div style="float:left; width:50%;height: 100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
				<tr class="item">
					<td class="itemtit">&nbsp;楼宇名称</td>
					<td class="border_b" width="30%">&nbsp;${record.buildingName!''}</td>
					<td class="itemtit">&nbsp;楼宇代码</td>
					<td class="border_b" width="30%">&nbsp;${record.customCode!''}</td>
				</tr>
				<tr class="item">
				    <td class="itemtit">&nbsp;使用性质</td>
					<td class="border_b" width="30%">&nbsp;${record.useNatureLabel!''}</td>
				    <td class="itemtit">&nbsp;楼层总数</td>
					<td class="border_b">&nbsp;<#if record.buildingFloor??>${record.buildingFloor?c}</#if></td>
				</tr>
				<tr class="item">
				    <td class="itemtit">&nbsp;建筑年份</td>
					<td class="border_b">&nbsp;<#if record.buildingYear??>${record.buildingYear?c}</#if></td>
				    <td class="itemtit">&nbsp;地面楼层</td>
					<td class="border_b">&nbsp;<#if record.groundFloorNum??>${record.groundFloorNum?c}</#if></td>
				</tr>
				<tr class="item">
				    <td class="itemtit">&nbsp;地下楼层</td>
					<td class="border_b">&nbsp;<#if record.undergroundFloorNum??>${record.undergroundFloorNum?c}</#if></td>
				    <td class="itemtit">&nbsp;楼高</td>
					<td class="border_b">&nbsp;<#if record.hight??>${record.hight?c}</#if></td>
				</tr>
				<tr class="item">
				    <td class="itemtit">&nbsp;占地面积</td>
					<td class="border_b">&nbsp;<#if record.area??>${record.area?c}</#if></td>
				    <td class="itemtit">&nbsp;管理单位</td>
					<td class="border_b">&nbsp;<#if record.managementCompany??>${record.managementCompany}</#if></td>
				</tr>
			    <tr class="item">
			        <td class="itemtit">&nbsp;管理员电话</td>
					<td class="border_b"><#if record.adminPhone??>
						<ul style="margin:0px 0px 0px 5px;padding:0;">
						<#list record.adminPhone?split(";;") as val>
							<li>${val}</li>
						</#list>
						</ul>
						<#else>
						&nbsp;
					</#if></td>
			        <td class="itemtit">&nbsp;楼宇地址</td>
					<td class="border_b">&nbsp;<#if record.buildingAddress??>${record.buildingAddress}</#if></td>
				</tr>
			    <tr class="item">
			        <td class="itemtit">&nbsp;楼层平面图 </td>
					<td class="border_b">&nbsp;<a href="javascript:showRow(${record.buildingId?c})">查看</a></td>
			        <td class="itemtit">&nbsp;楼宇外观图</td>
					<td class="border_b">&nbsp;<a href="javascript:showRow2(${record.buildingId?c})">查看</a></td>
				</tr>
			</table>
		</div>
		<div style="float:left;width:25%;height: 100%; border-left:2px solid #0000cc;" >
			<#if record.buildingExteriorFigure??>
				<img src="${RESOURSE_SERVER_PATH}${record.buildingExteriorFigure}" border=0 width="380px;" height="400px;" style="margin-left: 10px;margin-top: 10px;"/>
			<#else>
				<img src="${rc.getContextPath()}/images/notbuilding.gif" border=0 style="margin-left: 10px;margin-top: 10px;"/>
			</#if>
		</div>
	</div>
	
	<div title="楼宇结构" style="margin:0">
		<iframe scrolling='auto' frameborder='0' src='${rc.getContextPath()}/zzgl/grid/areaRoomInfo/buildingStructureRoom.jhtml?buildingId=${record.buildingId?c}&gridId=${record.gridId?c}' style='width:99.9%;height:100%;'></iframe>
	</div>
    <div title="场所信息" style="margin:0">
		<table id="placeInfoList"></table>
	</div>
	<div title="企业信息" style="margin:0">
		<table id="corBaseInfoList"></table>
	</div>
	<div title="人员信息" style="margin:0">
		<table id="cirsInfoList"></table>
	</div>
		
</div>
<script type="text/javascript">
var buildingId=${record.buildingId?c};
var gridId=${record.gridId?c};
//场所列表
$(function(){
	
	$("img[id^=400_]").each(function(){
		var width = $(this).width();
		if(width>400) $(this).css("width", 400);
	});

	$('#placeInfoList').datagrid({
		width:600,
		height:400,
		nowrap: false,
		striped: true,
		fit: true,
		idField:'plaId',
		url:'${rc.getContextPath()}/zzgl/grid/placeInfo/listData.json',
		frozenColumns:[[
            {field:'ck',checkbox:true},
           // {field:'plaId',title:'ID', align:'center',width:50},
            {field:'plaName',title:'场所名称', align:'center',width:100},
            {field:'gridName',title:'所属网格', align:'center',width:100}
			
		]],
		columns:[[
			{field:'opt', title:'操作', width:70, align:'center', formatter:function(value, rec, index){
				var f = '<a href="###" onclick="showDetailRow('+ rec.plaId+ ')">详情</a>&nbsp;';
				return f;
			}},
		    {field:'roomAddress',title:'场所地址', align:'center',width:150},
		    {field:'plaTypeLabel',title:'场所分类', align:'center',width:100},
		    {field:'isFocus', title:'是否重点场所', width:100, align:'center', formatter:function(value, rec, index){
				if(value=='1'){
					return '是';
				}else{
				   return '否';
				}
			}},
		    {field:'principal',title:'负责人', align:'center',width:100},
		    {field:'principalPhone',title:'负责人联系电话', align:'center',width:100}
		   
		]],
		pagination:true,
		queryParams:{buildingId:buildingId},
		onLoadSuccess:function(data){
				if(data.total==0){
				  	$('.datagrid-body-inner').eq(0).addClass("l_elist");
					$('.datagrid-body').eq(1).append('<div class="r_elist">无数据</div>');
				}else{
			        $('.datagrid-body-inner').eq(0).removeClass("l_elist");
			     }
		  },
		onLoadError:function(){
		        $('.datagrid-body-inner').eq(0).removeClass("l_elist");
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
		}
	});

	//设置分页控件
    var p = $('#placeInfoList').datagrid('getPager');
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
})

//企业信息列表
$(function(){
	$('#corBaseInfoList').datagrid({
		width:600,
		height:400,
		nowrap: false,
		striped: true,
		fit: true,
		idField:'cbiId',
		url:'${rc.getContextPath()}/zzgl/grid/corBaseInfo/listData.json',
		frozenColumns:[[
            {field:'ck',checkbox:true},
          //  {field:'cbiId',title:'ID', align:'center',width:50},
            {field:'corName',title:'法人名称', align:'center',width:100}/*,
            {field:'gridName',title:'所属网格', align:'center',width:100}*/
			
		]],
		columns:[[
			{field:'opt', title:'操作', width:70, align:'center', formatter:function(value, rec, index){
				var f = '<a href="###" onclick="showCorBaseInfoDetailRow('+ rec.cbiId+ ')">详情</a>&nbsp;';
				return f;
			}},
			{field:'roomAddress',title:'办公地址', align:'center',width:150},
		    {field:'corTypeLabel',title:'法人类型', align:'center',width:150},
		    {field:'corAddr',title:'法人地址', align:'center',width:100},
		    {field:'representativeName',title:'法人代表', align:'center',width:100},
		    {field:'establishDateStr',title:'成立日期', align:'center',width:100},
		    {field:'registeredCapital',title:'注册资金', align:'center',width:100},
		    {field:'categoryLabel',title:'行业分类', align:'center',width:100},
		    {field:'economicTypeLabel',title:'经济类型', align:'center',width:100},
		    {field:'administrativeDivision',title:'行政区划', align:'center',width:100},
		    {field:'telephone',title:'电话号码', align:'center',width:100}
		   
		]],
		pagination:true,
		queryParams:{buildingId:buildingId},
		onLoadSuccess:function(data){
				if(data.total==0){
				  	$('.datagrid-body-inner').eq(1).addClass("l_elist");
					$('.datagrid-body').eq(3).append('<div class="r_elist">无数据！</div>');
				}else{
			        $('.datagrid-body-inner').eq(1).removeClass("l_elist");
			     }
		  },
		onLoadError:function(){
				$('.datagrid-body-inner').eq(1).addClass("l_elist");
				$('.datagrid-body').eq(3).append('<div class="r_elist">数据加载出错！</div>');
		}
	});

	//设置分页控件
    var p = $('#corBaseInfoList').datagrid('getPager');
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
})

//人员信息列表
$(function(){
		$('#cirsInfoList').datagrid({
			width:600,
			height:400,
			nowrap: false,
			striped: true,
			fit: true,
			idField:'ciRsId',
			url:'${rc.getContextPath()}/zzgl/grid/ciRsRoom/roomCiRsListData.json',
			frozenColumns:[[
                {field:'ck',checkbox:true},
               // {field:'ciRsId',title:'ID', align:'center',width:100},
                  /*{field:'orgName',title:'所属网格',align:'center',width:120},*/
                {field:'name',title:'姓名', align:'center',width:100},
                {field:'gender',title:'性别', align:'center',width:70}
				
			]],
			columns:[[
			    {field:'identityCard',title:'身份证', align:'center',width:150},
			    {field:'education',title:'学历', align:'center',width:90},
			    {field:'marriageLabel',title:'婚姻', align:'center',width:80},
			    {field:'holderRelation',title:'与户主关系', align:'center',width:80},
			     {field:'typeLabel',title:'人口性质', align:'center',width:80},
			    {field:'phone',title:'联系电话', align:'center',width:100},
			    {field:'residenceAddr',title:'现居住地址', align:'center',width:180}
			   
			]],
			toolbar:'#jqueryToolbar',
				pagination:true,  
			queryParams:{buildingId:buildingId},
		onLoadSuccess:function(data){
				if(data.total==0){
				  	$('.datagrid-body-inner').eq(2).addClass("l_elist");
					$('.datagrid-body').eq(5).append('<div class="r_elist">无数据！</div>');
				}else{
			        $('.datagrid-body-inner').eq(2).removeClass("l_elist");
			     }
		  },
		onLoadError:function(){
				$('.datagrid-body-inner').eq(2).addClass("l_elist");
				$('.datagrid-body').eq(5).append('<div class="r_elist">数据加载出错！</div>');
		},
		onDblClickRow: function(index,rowData){
			var row = $("#cirsInfoList").datagrid("getSelected");
			showPeople(row.ciRsId);
			}
		});
		
		//设置分页控件
	    var p = $('#cirsInfoList').datagrid('getPager');
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
	
	
	});
//重置
function resetCondition(){
	$("#type").combobox("setValue","");
	$("#manageState").combobox("setValue","");
	
}

//查询
function searchAll() {
	var a = new Array();
	a["buildingId"]=buildingId;
	var type = $("#type").combobox("getValue");
	if(type!=null && type!="") a["type"]=type;
	var manageState = $("#manageState").combobox("getValue");
	if(manageState!=null && manageState!="") a["manageState"]=manageState;
	doSearch(a);
}
//执行查询
function doSearch(queryParams){
	$('#cirsInfoList').datagrid('clearSelections');
	$("#cirsInfoList").datagrid('options').queryParams=queryParams;
	$("#cirsInfoList").datagrid('load');
}

    function showRow(id) {
    	var url = '${rc.getContextPath()}/zzgl/grid/areaBuildingInfo/flatStructure.jhtml?buildingId='+id;
		showMaxJqueryWindow("平面结构图", url);
	}
	
	function showRow2(id) {
    	var url = '${rc.getContextPath()}/zzgl/grid/areaBuildingInfo/appearancePhotos.jhtml?buildingId='+id;
		showMaxJqueryWindow("楼宇外观图", url);
	}
	
   function showDetailRow(id) {
		var url = '${rc.getContextPath()}/zzgl/grid/placeInfo/placeCorDetail.jhtml?plaId='+id;
		showMaxJqueryWindow("场所信息与企业基本信息", url);
   }
   function showCorBaseInfoDetailRow(id) {
		var url = '${rc.getContextPath()}/zzgl/grid/corBaseInfo/detail.jhtml?cbiId='+id;
		showMaxJqueryWindow("企业详细", url);
  }
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result){
		closeMaxJqueryWindow();
		$("#placeInfoList").datagrid('load');
		$("#cirsInfoList").datagrid('load');
	}
function showPeople(cirsId){
parent.showInfo(cirsId);
}	
</script>
	<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>
</html>
