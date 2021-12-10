<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>已发送信息列表</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${GMIS_URL}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${GMIS_URL}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${GMIS_URL}/js/jqueryeasyui-last/datagrid-detailview.js"></script>

	<style type="text/css">
		.datagrid-body{position:relative;}/*解决兼容性视图下列表无法跟随滚动*/
	</style>

</head>
<body class="easyui-layout">
	<div class="MainContent hide">
	</div>
	<div id="msgSendMidListDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%;" id="list"></table>
	</div>

	<script type="text/javascript">
	    $(function(){
	    	loadDataList();
	    });
	    
	    
	    function loadDataList(){
	    	var queryParamsObj = {bizId:'${bizId?c}', bizType:'${bizType!}', taskBizType:'${taskBizType!}', targetTypes:'${targetTypes!}'};
	    	
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
				idField:'sendId',
				url:'${rc.getContextPath()}/zhsq/drugEnforcementEvent/listMsgSendMidData.json',
				columns:[[
					{field:'sendId',title:'ID', align:'center',hidden:true},//
					{field:'sendTypeName',title:'发送类型', align:'center',width:fixWidth(0.1),sortable:true},
					{field:'sendDateStr',title:'发送时间', align:'center',width:fixWidth(0.2),sortable:true}
				]],
				pagination:false,
				pageSize: 20,
				queryParams:queryParamsObj,
				onLoadError: function () {//数据加载异常
	        		$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
	    		},
				onLoadSuccess: function(data){
					if(data.total == 0) {
						$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
					}
		        },
		        view: detailview,
		        detailFormatter:function(index,row){
		            return '<div style="padding:2px"><table class="msgSendMidSubTable"></table></div>';
		        },
		        onExpandRow: fetchMsgSendMidSubList
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
	    
	    function fetchMsgSendMidSubList(index, row) {
	    	var msgSendMidSubTable = $(this).datagrid('getRowDetail',index).find('table.msgSendMidSubTable');
	    	
	    	if(msgSendMidSubTable.attr("isloaded") != "true") {
	            msgSendMidSubTable.datagrid({
	                url:'${rc.getContextPath()}/zhsq/drugEnforcementEvent/listMsgSendMidSubData.json?sendId='+row.sendId+'&targetType=SMS',
	                fitColumns:true,
	                singleSelect:true,
	                rownumbers:true,
	                loadMsg:'',
	                height:'auto',
	                columns:[[
	                	{field:'targetFlagName',title:'接收人员', align:'center',width:fixWidth(0.2)},
		                {field:'targetFlag',title:'接收电话', align:'center',width:fixWidth(0.2)}, 
		                {field:'sendMsg',title:'发送内容', align:'center',width:fixWidth(0.5), formatter: titleFormat},
		                {field:'status',title:'发送状态', align:'center',width:fixWidth(0.1), formatter: statusFormat}
	                ]],
	                onResize:function(){
	                    $('#list').datagrid('fixDetailRowHeight',index);
	                },
	                onLoadSuccess:function(data){
	                    setTimeout(function(){
	                        $('#list').datagrid('fixDetailRowHeight',index);
	                    },0);
	                    
	                    $('#list').datagrid('clearSelections');	//清除掉列表选中记录
						if(data.total==0){
							var body = $(this).data().datagrid.dc.body2;
							body.append('<div class="nodata" style="height: 200px;"></div>');	
						}
						
						msgSendMidSubTable.attr("isloaded", "true");//用于防止重复的加载
	                }
	            });
	            $('#list').datagrid('fixDetailRowHeight',index);
            }
	    }
	    
	    function titleFormat(value, rec, rowIndex) {
	    	var title = "";
	    	
	    	if(value) {
	    		title = '<span title="'+ value +'">'+ value +'</span>';
	    	}
	    	
	    	return title;
	    }
	    
	    function statusFormat(value, rec, rowIndex) {
	    	var statusName = "";
	    	
	    	if(value) {
	    		switch(value) {
	    			case "0": {
	    				statusName = "失败"; break;
	    			}
	    			case "1": {
	    				statusName = "成功"; break;
	    			}
	    			case "2": {
	    				statusName = "未发送"; break;
	    			}
	    			case "3": {
	    				statusName = "无效"; break;
	    			}
	    		}
	    	}
	    	
	    	return statusName;
	    }
	</script>

</body>
</html>