<!DOCTYPE html>
<html>
	<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	    <title>报表统计事件列表</title>
	    <#include "/component/standard_common_files-1.1.ftl" />
	    <link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	    <style type="text/css">
	        .datagrid-body{position:relative;}/*解决兼容性视图下列表无法跟随滚动*/
	        .NorToolBtn{width:auto}
	    </style>
	</head>
	
	<body class="easyui-layout">
		<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		    <table style="width:100%" id="list"></table>
		</div>
		
		<script type="text/javascript">
		    
		    $(function() {
		        loadDataList();
		    });
		
		    function loadDataList(){
		        var queryParams = queryData();
		
		        $('#list').datagrid({
		            width:600,
		            height:300,
		            nowrap: true,
		            rownumbers:true,
		            remoteSort:false,
		            striped: true,
		            fit: true,
		            fitColumns: true,
		            singleSelect: true,
		            idField:'eventId',
		            url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json',
		            columns:[[
		                {field:'eventId',title:'ID', align:'center',checkbox:true,hidden:true},
		                {field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.18),sortable:true,formatter: titleFormatter},
		                {field:'handleDateStr',title:'办结期限', align:'center',width:fixWidth(0.15),sortable:true},
		                {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.18),sortable:true, formatter: titleFormatter},
		                {field:'creatorName',title:'上报人员', align:'center',width:fixWidth(0.18),sortable:true, formatter: titleFormatter},
		                {field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter}
		            ]],
		            pageSize: 20,
		            pagination:true,
		            queryParams: queryParams,
		            onLoadError: function () {//数据加载异常
		                $('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
		            }
		        });
				
		        //设置分页控件
		        var p = $('#list').datagrid('getPager');
		        $(p).pagination({
		            pageSize: 20,//每页显示的记录条数，默认为
		            pageList: [20,30,40,50,100,200],//可以设置每页记录条数的列表
		            beforePageText: '第',//页数文本框前显示的汉字
		            afterPageText: '页    共 {pages} 页',
		            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
		        });
		    }
		    
		    function queryData() {
		    	var outerParams = <#if outerParamJson??>${outerParamJson}<#else>''</#if>,
		    	      queryParams = {};
		    	
		    	if(isNotBlankParam(outerParams)) {
		    		for(var index in outerParams) {
		    			 queryParams[index] = outerParams[index];
		    		}
		    	}
		    	
		    	return queryParams;
		    }
		    
		    function titleFormatter(value, rowData, rowIndex) {
		        var title = "";
		
		        if(value) {
		            title = '<span title="'+ value +'" >'+ value +'</span>';
		        }
		
		        return title;
		    }
		    
		    function dateFormatter(value, rowData, rowIndex) {
		    	if(value && value.length >= 10) {
		    		value = value.substring(0,10);
		    	}
		    	
		    	return value;
		    }
		</script>
		
	</body>
</html>