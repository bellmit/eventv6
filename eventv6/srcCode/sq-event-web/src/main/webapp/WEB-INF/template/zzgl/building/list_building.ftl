<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>房屋楼栋选择</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
</head>
<body class="easyui-layout">
<div region="center" border="false" style="width: 760px;">
   <table id="tbtable"  fitColumns="true" width="100%" singleSelect="true" striped="true" nowrap="false">  
      <thead>  
        <tr> 
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th field="buildingName" width="59">楼宇名称</th>
            <th field="customCode" width="100">楼宇代码</th>
            <th field="buildingAddress" width="50" >楼宇地址</th>
            <th field="useNatureLabel" width="50" >使用性质</th>
        </tr>  
      </thead>  
   </table> 
</div>

<div id="jqueryToolbar" style="padding:5px;height:auto">
	<div style="margin-top:5px; padding-bottom:8px; border-bottom:1px solid #ccc;">
		楼宇：<input type="text" id="searchBuildingName" style="width:120px;" />
		楼宇代码：<input type="text" id="customCode" style="width:120px;" />
	</div>
	<div style="margin-top:5px;">
		性质：<select id="searchUseNature" editable="false" class="easyui-combobox" data-options="panelHeight:200,width:150">
    		<option value="">--全部--</option>
    		<#if useNatureDC??>
				<#list useNatureDC as l>
					<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
				</#list>
			</#if>
    	</select>
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="searchData()">查询</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="resetCondition()">重置</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="selectComplete()">确定</a>
	</div>
</div>

<script type="text/javascript">
	var gridId=${gridId?c};//网格ID

	//加载数据
	$(function(){
		$('#tbtable').datagrid({
	        width:760,
			height:200,
			fit: true,
			idField:'buildingId',
	        url:"${rc.getContextPath()}/zhsq/building/areaBuildingInfo/listData.json",
			toolbar:'#jqueryToolbar',
			pagination:true,
			queryParams:{gridId:gridId}
	    });
	
	    
	    //设置分页控件
	    var p = $('#tbtable').datagrid('getPager');
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

	//重置查询
	function resetCondition(){
		$("#searchBuildingName").val("");
		$("#customCode").val("");
		$("#searchUseNature").combobox("setValue","");
	}
	
	//查询
	function searchData() {
		var a = new Array();
		a["gridId"]=gridId;
		var searchBuildingName = $("#searchBuildingName").val();
		if(searchBuildingName!=null && searchBuildingName!="") a["buildingName"]=searchBuildingName;
		var customCode = $("#customCode").val();
		if(customCode!=null && customCode!="") a["customCode"]=customCode;
		var searchUseNature = $("#searchUseNature").combobox("getValue");
		if(searchUseNature!=null && searchUseNature!="") a["useNature"]=searchUseNature;
		doSearch(a);
	}
	
	//进行查询
	function doSearch(queryParams){
		$('#tbtable').datagrid('clearSelections');
		$("#tbtable").datagrid('options').queryParams=queryParams;
		$("#tbtable").datagrid('load');
	}
	
	function selectComplete() {
		var row = $('#tbtable').datagrid('getSelected');
		if (row){
			parent.areaBuildingSelectComplete(row.buildingId, row.buildingName, row.buildingAddress, row);
		} else {
			$.messager.alert('提示','请选中对应的楼栋！','warning');
		}
	}
</script>
</body>
</html>
