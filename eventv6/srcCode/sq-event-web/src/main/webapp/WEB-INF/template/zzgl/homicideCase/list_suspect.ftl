<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>命案防控-嫌疑人信息</title>
	<#include "/component/standard_common_files-1.1.ftl" />
</head>
<body class="easyui-layout">

<div class="MainContent">
	<#include "/zzgl/homicideCase/toolbar_suspect.ftl" />
</div>

<div id="homicideCaseDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
	<table id="list" style="width:100%"></table>
</div>

<#include "/component/singleMaxJqueryEasyUIWin.ftl" />

<script type="text/javascript">
	
	$(function(){
		loadDataList();
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
			url:'${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/listSuspectData.json',
			columns:[[
				{field:'hashId',checkbox:true,width:40,hidden:'true'},
				{field:'name',title:'姓名', align:'left',width:fixWidth(0.18),sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value == null){
							value = "";
						}
						var f = '<a class="eName" href="###" title="'+ rec.name +'" onclick="detail(\'' + rec.hashId + '\')")>'+value+'</a>';
						return f;
					}	
				},
                {field:'sexName',title:'性别',align:'center',width:fixWidth(0.07),sortable:true},
                {field:'cardTypeName',title:'证件类型',align:'center',width:fixWidth(0.12),sortable:true},
                {field:'idCardHS',title:'证件号码',align:'center',width:fixWidth(0.15),sortable:true},
                {field:'nationalityName',title:'国籍',align:'center',width:fixWidth(0.1),sortable:true},
                {field:'birthPlaceName',title:'籍贯',align:'center',width:fixWidth(0.2),sortable:true},
                {field:'marriageName',title:'婚姻状况',align:'center',width:fixWidth(0.1),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:false,
			pageSize: 20,
			queryParams:{bizType:$("#bizType").val(), hashBizId:$("#hashBizId").val()},
			onDblClickRow:function(index,rec){
				detail(rec.hashId);
			},
			onLoadSuccess:function(data){
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata" style="width:100%;height:100%;"></div>');
				}
				
				if(parent.countSuspect && typeof parent.countSuspect == 'function') {
					parent.countSuspect($("#bizType").val(), data.total);
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
			closeMaxJqueryWindow();
		}catch(e){}
		DivShow(result);
		searchData();
	}
</script>
</body>
</html>