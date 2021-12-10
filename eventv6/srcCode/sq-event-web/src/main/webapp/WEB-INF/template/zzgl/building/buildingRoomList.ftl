<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>房屋楼栋选择</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/datagrid-detailview.js"></script>
</head>
<body class="easyui-layout">
<div region="center" border="false" style="width: 760px;">
   <table id="tbtable"  fitColumns="true" width="100%" singleSelect="true" striped="true" nowrap="false">  
      <thead>  
        <tr> 
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
	          所属网格：<input type="text" readonly="readonly" style="width:120;" onclick="showSingleMixedGridSelector();" name="gridName" id="gridName" value="<#if gridName??>${gridName}</#if>" />
		楼宇：<input type="text" id="searchBuildingName" style="width:120px;" value="<#if buildingName??>${buildingName}</#if>"/>
		楼宇代码：<input type="text" id="customCode" style="width:120px;" />
	</div>
	<div style="margin-top:5px;">
	   	楼宇地址：<input type="text" id="buildingAddress" style="width:120px;" />
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
	</div>
</div>

<script type="text/javascript">
	var gridId=${gridId?c};//网格ID
	//加载数据
	$(function(){
		$('#tbtable').datagrid({
	        view: detailview,
	        width:760,
			height:200,
			fit: true,
			idField:'buildingId',
	        url:"${rc.getContextPath()}/zzgl/building/areaBuildingInfo/listData.json",
	        detailFormatter:function(index,row){
	            return '<div class="land"></div>';  
	        },
			toolbar:'#jqueryToolbar',
			pagination:true,
			queryParams:{gridId:gridId,buildingName:$('#searchBuildingName').val()},
			onLoadSuccess:function(data){
				if(data.total==0){
					$('.datagrid-body-inner').eq(0).addClass("l_elist");
					$('.datagrid-body').eq(1).append('<div class="r_elist">无数据</div>');
				}else{
			        $('.datagrid-body-inner').eq(0).removeClass("l_elist");
			     }
			},
			onLoadError:function(){
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
			},
	        onExpandRow: function(index,row){
	            var ddv = $(this).datagrid('getRowDetail',index).find('div.land');  
	            ddv.panel({  
	                border:false,  
	                cache:true,  
	                href:"${rc.getContextPath()}/zzgl/grid/areaRoomInfo/areaRoomList.jhtml?buildingId="+row.buildingId+"&buildingName="+encodeURIComponent(encodeURIComponent(row.buildingName)),  
	                onLoad:function(){  
	                    $('#tbtable').datagrid('fixDetailRowHeight',index);  
	                    $('#tbtable').datagrid('selectRow',index);  
	                    $('#tbtable').datagrid('getRowDetail',index).find('form').form('load',row);  
	                }  
	            });  
	            $('#tbtable').datagrid('fixDetailRowHeight',index);  
	        }
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
		$("#buildingAddress").val("");
		$("#customCode").val("");
		$("#searchUseNature").combobox("setValue","");
	}
	
	//查询
	function searchData() {
		var a = new Array();
		a["gridId"]=gridId;
		var searchBuildingName = $("#searchBuildingName").val();
		if(searchBuildingName!=null && searchBuildingName!="") a["buildingName"]=searchBuildingName;
		var buildingAddress = $("#buildingAddress").val();
		if(buildingAddress!=null && buildingAddress!="") a["buildingAddress"]=buildingAddress;
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

	//关闭窗口
	function closeAreaBuildingInfoSelector(buildingId,buildingName,roomId,roomName,roomAddress) {
		parent.areaBuildingRoomSelectComplete(buildingId,buildingName,roomId,roomName,roomAddress)
	}
	 function singleMixedGridSelectCallback(gridId1,gridName,orgId,orgCode,gridPhoto){
	    gridId=gridId1;
		$("#gridName").attr("value",gridName);
	  }
</script>
 <#include "/component/singleMixedGridSelectorZtree.ftl">
</body>
</html>
