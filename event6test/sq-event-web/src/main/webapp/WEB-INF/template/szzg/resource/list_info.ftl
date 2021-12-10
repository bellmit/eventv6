<!DOCTYPE html>
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资源信息操作</title> 
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />

    <style >
        .abs-right_name{ width: 100px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;}
        .abs-right_content{ width: 200px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;}

    </style>
</head>
<body class="easyui-layout">
	<div region="center" title="<span class='easui-layout-title'>资源信息列表</span>" border="false" style="overflow:hidden;">
		<table id="list"></table>
	</div>

<div id="jqueryToolbar">
	<div class="ConSearch">
		<div class="fl">
        	<ul>
				<li>所属网格：</li>
                <li><input id="orgCode" type="hidden" value="${orgCode}"/>
                <input value="${orgName}" id="orgName" type="text" class="inp1 InpDisable" style="width:100px;"/></li>
                <li>资源名称</li>
                <li><input name="name" type="text" class="inp1" id="name" placeholder="资源名称" style="color:gray; width:150px;" onfocus="if(this.value=='1'){this.value='';}$(this).attr('style','width:280px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:280px;');}" onkeydown="_onkeydown();" /></li>
            </ul>
        </div>
        <div class="btns">
        	<ul>
            	<li><a href="#" class="chaxun" title="点击搜索" onclick="searchData()">搜索</a></li>
            	<li><a href="#" class="chongzhi" title="重置搜索条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
	</div>

	<div class="h_10 clear"></div>
</div>
    
	<script type="text/javascript">
		var resTypeCode = '${id!0}';
		
		$(function(){
			AnoleApi.initGridZtreeComboBox("orgName", null, function(gridId, items) {
				if (items && items.length > 0) {
					document.getElementById('orgCode').value=items[0].orgCode;
				}
			}, { 
				ChooseType : '1',
				isShowPoorIcon: "0",
				ShowOptions : {EnableToolbar : false}
			});
			loadDataList();
		});
		
	    function loadDataList(){
	    	$('#list').datagrid({
				nowrap: false,
				striped: true,
				fit: true,
				remoteSort:false,
				fitColumns: true,
				scrollbarSize :0,
				url:'${rc.getContextPath()}/zhsq/szzg/zgResourceController/findInfoList.json',
				columns:[[
	                {field:'resId',title:'resId',width:10,hidden:true},
	                {field:'resName',title:'资源名称',width:150},
	                {field:'lng',title:'经度',width:100},
	                {field:'lat',title:'纬度',width:100},
	                {field:'orgName',title:'区域',width:150,
                        formatter: function (val, rec) {
                            var content = '<li title="' + val + '" class="right_content">' + val + '</li>';
                            return content;
                        }}
				]],
				toolbar:'#jqueryToolbar',
				pagination:true,
				singleSelect:true,
				queryParams:{typeCode:resTypeCode,orgCode:${orgCode}},//默认查询参数
				rownumbers:true,//显示行号的列
				onLoadError: function () {//数据加载异常
					$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
				},
				onLoadSuccess: function(data){//事件标题内容左对齐
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}}
			});
			//设置分页控件
		    var p = $('#list').datagrid('getPager');
			$(p).pagination({
				pageSize: 10,//每页显示的记录条数，默认为10
				pageList: [10,20,40,50],//可以设置每页记录条数的列表
				beforePageText: '第',//页数文本框前显示的汉字
				afterPageText: '页    共 {pages} 页',
				displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
			});
	    }
		
		function searchData() {
			var queryParams = {orgCode:document.getElementById('orgCode').value,typeCode:resTypeCode};
			var name = $("#name").val();
			if(name!=null && name!=""){
				 queryParams["name"]=name;
			}
			$('#list').datagrid('clearSelections');
			$("#list").datagrid('options').queryParams=queryParams;
			$("#list").datagrid('load');
		}
		
		
		function resetCondition() {
			$("#name").val("");
			document.getElementById('orgCode').value='${orgCode}';
			document.getElementById('orgName').value='${orgName}';
			searchData();
		}
		
		//回车查询
		function _onkeydown(){
			var keyCode = event.keyCode;
			if(keyCode == 13){
				searchData();
			}
		}
		
		//-- 供子页调用的重新载入数据方法
		function reloadDataForSubPage( result){
			closeMaxJqueryWindow();
			DivShow(result.tipMsg);
			searchData();
			treeReload();
		}
	</script>
</body>
</html>