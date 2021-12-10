<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>楼栋信息</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
</head>
	<body class="easyui-layout">
		 <div id="areaBuildingInfoDiv" region="center"  border="false" style="overflow:hidden;">
			<table id="areaBuildingInfoList"></table>
		</div>
	  <div id="jqueryToolbar" style="padding:5px;height:auto">
			<div style="margin-top:5px; padding-bottom:8px; border-bottom:1px solid #ccc;">
				楼栋名称：<input type="text" id="buildingName" style="width:120px" />
				使用性质：<select id="useNature" class="easyui-combobox" data-options="panelHeight:200,width:120" editable="false">
		    		<option value="">--全部--</option>
		    		<#if useNatureDC??>
						<#list useNatureDC as l>
							<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
						</#list>
					</#if>
		    	</select>
				<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="searchAll();">查询</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="resetCondition()">重置</a>
			</div>
	</div>
<script type="text/javascript">
var cbiId="${cbiId?c}";
	  //楼栋信息
$(function(){
	$('#areaBuildingInfoList').datagrid({
		width:600,
		height:400,
		nowrap: false,
		striped: true,
		fit: true,
		idField:'buildingId',
		url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/corBaseAreaBuildinglistData.json',
		frozenColumns:[[
           // {field:'buildingId',title:'ID', align:'center',width:100},
            {field:'buildingName',title:'楼宇名称', align:'center',width:100},
            {field:'useNatureLabel',title:'使用性质', align:'center',width:100}
			
		]],
		columns:[[
		    {field:'gridName',title:'所属网格', align:'center',width:150},
		    {field:'buildingAddress',title:'楼宇地址', align:'center',width:100},
		    {field:'buildingFloor',title:'层数', align:'center',width:100},
		    {field:'area',title:'占地面积', align:'center',width:100},
		    {field:'buildingArea',title:'建筑面积', align:'center',width:100}, 
		    {field:'buildingYear',title:'建筑年份', align:'center',width:100}   
		]],
		toolbar:'#jqueryToolbar',
		pagination:true,
		queryParams:{cbiId:cbiId},
		onLoadSuccess:function(data){
		    $('#areaBuildingInfoList').datagrid('clearSelections');	//清除掉列表选中记录
			if(data.total==0){					
				$('.datagrid-body-inner').eq(0).addClass("l_elist");					
				$('.datagrid-body').eq(1).append('<div class="r_elist">无数据</div>');					
			}else{
			    $('.datagrid-body-inner').eq(0).removeClass("l_elist");
			}
		}
	});

	//设置分页控件
    var p = $('#areaBuildingInfoList').datagrid('getPager');
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

   function resetCondition(){
		$("#buildingName").val("");
		$("#useNature").combobox("setValue","");
	}

	
  function searchAll() {
		var a = new Array();
		a["cbiId"]=cbiId;
		var buildingName = $("#buildingName").val();
		if(buildingName!=null && buildingName!="") a["buildingName"]=buildingName;
		var useNature = $("#useNature").combobox("getValue");
		if(useNature!=null && useNature!="") a["useNature"]=useNature;
		doSearch(a);
	}
	
	function doSearch(queryParams){
		$('#areaBuildingInfoList').datagrid('clearSelections');
		$("#areaBuildingInfoList").datagrid('options').queryParams=queryParams;
		$("#areaBuildingInfoList").datagrid('load');
	}
   
	</script>
    </body>
</html>