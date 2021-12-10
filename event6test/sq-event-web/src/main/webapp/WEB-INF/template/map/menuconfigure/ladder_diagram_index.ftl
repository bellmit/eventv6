<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>天梯图初始化配置首页</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl">
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/normal.css" />
<style>
.NorForm td:first-child {
    padding-left: 15%;
}

.BigTool {
    margin-top: 7.3%;
    border-bottom: 1px solid #d8d8d8;
}

.NorToolBtn {
    color: #fff !important;
}
</style>
</head>
<body class="easyui-layout">
<div region="center" border="false" style="width:100%; overflow:hidden;">
	<table id="list"></table>
</div>
<div id="jqueryToolbar">
<form id="tableForm" width="100%" height="50%" name="tableForm" action=""  method="post">	
	<div class="NorForm">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
	    		<td class="LeftTd">
			        <label class="LabName"><span>所属网格：</span></label>
					<input type="hidden" id="gridCode" name="infoOrgCode" value="${gridCode!''}"/>
					<input style="width: 150px;" class="inp1 InpDisable easyui-validatebox" type="text" id="gridName" data-options="required:true,tipPosition:'bottom'" name="gridName" value="${gridName!''}"/>
	    		</td>
	    		<td class="LeftTd">
			        <label class="LabName"><span>地图类型：</span></label>
			        <select id="mapType" name="mapType" class="inp1 InpDisable easyui-validatebox" data-options="required:true,tipPosition:'bottom'" style="width:150px">
                		<option value="1" selected>矢量图</option>
                		<option value="2">影像图</option>
                	</select>
	    		</td>
			</tr>
			<tr>
	    		<td class="LeftTd">
			        <label class="LabName"><span>中心点经度：</span></label>
					<input id="x" name="x" type="text" class="inp1 easyui-numberbox" data-options="required:true,tipPosition:'bottom'" style="width:150px;height:28px;" value="" precision="13" data-options="required:true,tipPosition:'bottom'"/>

	    		</td>
	    		<td class="LeftTd">
			        <label class="LabName"><span>中心点纬度：</span></label>
					<input id="y" name="y" type="text" class="inp1 easyui-numberbox" data-options="required:true,tipPosition:'bottom'" style="width:150px;height:28px;" value=""  precision="13" data-options="required:true,tipPosition:'bottom'"/>
	    		</td>
			</tr>
			<tr>
	    		<td class="LeftTd">
			        <label class="LabName"><span>地图显示层级：</span></label>
					<input id="zoom" name="zoom" type="text" class="inp1 easyui-numberbox" data-options="min:1,max:18,required:true,tipPosition:'bottom'" style="width:150px;height:28px;" value="" data-options="required:true,tipPosition:'bottom'"/>
	    		</td>
				<td class="LeftTd">
					<label class="LabName"><span>排序：</span></label>
					<input id="order" name="order" type="text" class="inp1 easyui-numberbox" data-options="min:1,max:50,required:true,tipPosition:'bottom'" style="width:150px;height:28px;" value="" data-options="required:true,tipPosition:'bottom'"/>
				</td>
			</tr>
		</table>
	</div>
	
	<div class="BigTool">
    	<div class="BtnList">
			<a href="#" onclick="tableSubmit();" class="BigNorToolBtn SubmitBtn">初始化数据</a>
        </div>
    </div>
</form>
</div>
<script type="text/javascript">
    var relatedEventsList = null;
    var gridCode = "<#if gridCode??>${gridCode}</#if>";
    var gridId = <#if gridId??>${gridId?c}<#else>-99</#if>
    
    $(function(){
    	AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(isNotBlankParam(items) && items.length > 0) {
				var grid = items[0];
				$("#gridCode").val(grid.orgCode);
			}
		});
	    loadDataList();
	});
	
    function loadDataList(){
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: true,
			rownumbers:true,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'ARCGIS_CONFIG_INFO_ID',
			url:'${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/loadLaData.json',
			columns:[[
				{field:'ARCGIS_CONFIG_INFO_ID',title:'ID', align:'center',hidden:true},
				{field:'GRID_PATH',title:'所属网格', align:'center',width:fixWidth(0.15)},
				{field:'MAP_TYPE_NAME',title:'地图类型', align:'center',width:fixWidth(0.08)},
				{field:'MAP_CENTER_X',title:'经度', align:'center',width:fixWidth(0.1)},
				{field:'MAP_CENTER_Y',title:'纬度', align:'center',width:fixWidth(0.1)},
				{field:'ZOOM',title:'显示层级', align:'center',width:fixWidth(0.08)},
				{field:'ORDER_INDEX',title:'排序', align:'center',width:fixWidth(0.08)},
				{field:'DEL',title:'操作', align:'center',width:fixWidth(0.1),formatter:function(value, rec, index){
					return "<a href='javascript:;' class='NorToolBtn DelBtn' style='margin-left: 35%;' onclick='del("+rec.ARCGIS_CONFIG_INFO_ID+");'>删除</a>";
				}}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{infoOrgCode:${gridCode!''}},
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
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
		});
    }
    
    function tableSubmit(){
    	var isValid =  $("#tableForm").form('validate');
		if(isValid){
			$.ajax({
            type:'POST',
            url:'${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/saveLd.jhtml',
            data:$("#tableForm").serialize(),
            dataType:'json',
            success:function (data) {
                if (data.result) {
                	$("#x").val("");
                    $("#y").val("");
                    $("#zoom").val("");
                    $("#order").val("");
                    $.messager.alert('提示', '操作成功！', 'info');
                    searchData();
                } else {
                    $.messager.alert('错误', '操作失败！', 'error');
                }
            },
            error:function () {
                $.messager.alert('错误', '连接超时！', 'error');
            }
        	});
		}
    }
    
	function searchData() {
		var a = new Array();
		a["infoOrgCode"] = '${gridCode!''}';
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
		$('#list').datagrid('clearSelections');	//清除选择的行
		$("#list").datagrid('load');			//重新加载事件列表
	}
	
	function del(id){
		if(id==null || id=="") {
			$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
			return;
		}
		$.messager.confirm('提示', '您确定删除选中的记录吗？', function(r){
			if (r){
				modleopen();
				$.ajax({
					type: "POST",
					url: '/zhsq_event/zhsq/map/menuconfigure/menuConfig/deleteLd.jhtml',
					data: 'id='+id,
					dataType:"json",
					success: function(data){
						modleclose();
						$.messager.alert('提示', '删除成功！', 'info');
						$('#list').datagrid('clearSelections');	//清除掉列表选中记录
						$("#list").datagrid('load');
						searchData();
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
					}
				});
			}
		});
	}
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/customEasyWin.ftl" />

</body>
</html>