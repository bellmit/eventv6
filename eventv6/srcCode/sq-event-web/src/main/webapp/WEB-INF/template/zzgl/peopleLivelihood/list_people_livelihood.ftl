<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>民生信息列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout" id="layoutArea">
	<div class="MainContent">
		<#include "/zzgl/peopleLivelihood/toolbar_people_livelihood.ftl" />
	</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>
<script>
    
    $(function(){
			
    	loadDataList();
    	
    });
    
    function loadDataList(){
    
    	var queryParams = queryData();
    
		$('#list').datagrid({
        	idField: 'teamId',
            width:600,
			height:300,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			pagination:true,
			pageSize: 20,
			toolbar:'#jqueryToolbar',
			url:'${rc.getContextPath()}/zhsq/peopleLivelihood/listData.jhtml',
			queryParams:queryParams,
            columns: [[
                
                {field:'infoId',align:'center',checkbox:true,hidden:true},//
                {field:'status',align:'center',checkbox:true,hidden:true},//
                {field:'infoOrgCode',align:'center',checkbox:true,hidden:true},//
                {field:'gridId',align:'center',checkbox:true,hidden:true},//
				{field:'gridPath',title:'所属辖区',halign:'center',align:'left',width:fixWidth(0.2),formatter: titleFormatterGrid},//
				{field:'infoTitle',title:'标题', align:'center',width:fixWidth(0.2),sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value == null){
							value = "";
						}
						var f = '<a class="eName" href="###" title="'+ rec.infoTitle +'" onclick="showDetail(\'' + rec.infoId + '\',\''+rec.instanceId+'\')")>'+value+'</a>';
						return f;
					}	
				},
				{field:'infoTypeName',title:'民生信息类型', halign:'center',align:'center',width:fixWidth(0.15),sortable:true,formatter: titleFormatter},
				{field:'infoTrendsTypeName',title:'民生动态类型', halign:'center',align:'center',width:fixWidth(0.15),sortable:true,formatter: titleFormatter},
				{field:'urgenceDegreeName',title:'紧急程度', halign:'center',align:'center',width:fixWidth(0.10),sortable:true,formatter: titleFormatter},
				{field:'statusName',title:'信息状态', halign:'center',align:'center',width:fixWidth(0.05),sortable:true,formatter: titleFormatter},
				{field:'happenTimeStr',title:'上报时间', halign:'center',align:'center',width:fixWidth(0.15),sortable:true,formatter: titleFormatter}

            ]],
            onLoadError: function () {//数据加载异常
        		$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
    		},
    		onSelect: function(index,rec) {
				authority(rec);
			},
			onLoadSuccess:function(data){
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
             
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
    
    function titleFormatter(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'" >'+ value +'</span>';
		}
		
		return title;
	}
	
	
    function titleFormatterGrid(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ rowData.gridPath +'" >'+ value +'</span>';
		}
		
		return title;
	}
	
	
	
	


</script>
</body>
</html>