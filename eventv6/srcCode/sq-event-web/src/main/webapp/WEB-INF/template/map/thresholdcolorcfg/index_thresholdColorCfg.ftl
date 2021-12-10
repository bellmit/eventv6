<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>填报管理页面</title>
	<#include "/component/commonFiles-1.1.ftl" />
</head>
<body class="easyui-layout">

<#include "/map/thresholdcolorcfg/toolbar_thresholdColorCfg.ftl" />

<div region="center" border="false" style="overflow:hidden;">
	<table id="list"></table>
</div>

<script type="text/javascript">
	$(function() {
		loadDataList();
	});
	
	function loadDataList() {
		$('#list').datagrid({
			url:"${rc.getContextPath()}/zhsq/map/thresholdcolorcfg/thresholdColorCfg/listData.json?t="+Math.random(),
			width:600,
			height:600,
			nowrap: false,
			striped: true,
			fit: true,
			singleSelect: true,
			rownumbers: true,
			columns:[[
			
				{field:'className',title:'配置类别', align:'left',width:150,
					formatter : function(value, rec, index) {
						return value;
					}
				},
				{field:'gridName',title:'所属网格', align:'left',width:150},
				{field:'status_',title:'状态', align:'left',width:150,
					formatter : function(value, rec, index) {
						var statusName = '';
						if(value == '1') {
							statusName = '启用';
						}else if(value == '2') {
							statusName = '禁用';
						}
						return statusName;
					}
				},
                {
					field : 'remark_',
					title : ' 备注',
					align : 'center',
					width : $(this).width() * 0.15,
					formatter : function(value, rec, index) {
						return value;
					}
				}
			]],
			 
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams: getQueryData(),
			onLoadSuccess : function(data) {
			    $('#list').datagrid('clearSelections');	//清除掉列表选中记录
				if(data.total == 0) {
					var body = $(this).data().datagrid.dc.body2;
					body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/frame/images/nodata.png" title="暂无数据"/></div>');
				}
			},
			onClickRow : function(rowIndex, rowData) {
				buttonAccessByStatus(rowData.status_);
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
	function reloadDataForSubPage(result) {
		closeMaxJqueryWindow();
		$.messager.alert('提示', result, 'info');
		searchData();
	}
	
	function getQueryData() {
		var a = {};
		var orgCode = $("#orgCode").val();
		a["orgCode"] = orgCode;
		return a;
	}
	
	function searchData() {
		doSearch(getQueryData());
	}
	
	function doSearch(queryParams) {
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');
	}
	
	function resetCondition() {
		$("#searchForm")[0].reset();
		searchData();
	}
	
</script>

</body>
</html>