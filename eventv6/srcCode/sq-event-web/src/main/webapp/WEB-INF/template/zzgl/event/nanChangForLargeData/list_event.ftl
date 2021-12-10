<@override name="eventListPageTitle">
	江西省南昌市大数据平台事件列表页面
</@override>

<@override name="eventToolbar">
<#if isPointPage??>
<#include "/zzgl/event/nanChangForLargeData/eventDataGridToolbar.ftl" />
<#else>
<#include "/zzgl/event/nanChangForLargeData/eventDataGridToolbarForEvent.ftl" />
</#if>
</@override>


<#if isPointPage??>
	<@override name="generalColumns">
		columns:[[
	        {field:'eventId',title:'ID', align:'center',checkbox:true,hidden:true},
	       	{field:'type',title:'type', align:'center',hidden:true},
	        {field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,formatter: clickFormatter},
	       	{field:'occurred',title:'事件地址', align:'center',width:fixWidth(0.18),sortable:true, formatter: titleFormatter},
	        {field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.08),sortable:true},
	        {field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.14),sortable:true}
	    ]],
    </@override>
    <@override name="generalPageSize">
    	pageSize: 10,
    </@override>
    
    <@override name="generalPaging">
        //设置分页控件
        var p = $('#list').datagrid('getPager');
        $(p).pagination({
            pageSize: 10,//每页显示的记录条数，默认为
            pageList: [10,20,30,40,50],//可以设置每页记录条数的列表
            beforePageText: '第',//页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
        });
       </@override>
        
        <@override name="showDetailRowFunction">
 		 function showDetailRow(eventId,instanceId,workFlowId,bizPlatform,type){
	        if(!eventId){
	            $.messager.alert('提示','请选择一条记录！','info');
	        }else{
	            if(bizPlatform == "001"){
	                var url = '${SQ_ZZGRID_URL}/zzgl/event/innerPlatform/detail.jhtml?eventId='+eventId;
	                showMaxJqueryWindow("查看事件信息", url);
	            }else if(bizPlatform == "3601017"){
	            	var url = "${rc.getContextPath()}/zhsq/event/problemReportController/detailEvent.jhtml?eventType=${eventType}&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&model=${model}&cachenum=" + Math.random();
	                showMaxJqueryWindow("查看事件信息", url);
	            }else{
	                var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=${eventType}&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&model=${model}&cachenum=" + Math.random();
	                showMaxJqueryWindow("查看事件信息", url, null, null, true);
	            }
	        }
	    }
     </@override>
<#else>
	<@override name="showDetailRowFunction">
 		 function showDetailRow(eventId,instanceId,workFlowId,bizPlatform,type){
	        if(!eventId){
	            $.messager.alert('提示','请选择一条记录！','info');
	        }else{
	            if(bizPlatform == "001"){
	                var url = '${SQ_ZZGRID_URL}/zzgl/event/innerPlatform/detail.jhtml?eventId='+eventId;
	                showMaxJqueryWindow("查看事件信息", url);
	            }else if(bizPlatform == "3601017"){
	            	var url = "${rc.getContextPath()}/zhsq/event/problemReportController/detailEvent.jhtml?eventType=${eventType}&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&model=${model}&cachenum=" + Math.random();
	                showMaxJqueryWindow("查看事件信息", url, fetchWinWidth(), defaultShowWindowHeight && defaultShowWindowHeight<document.body.clientHeight?defaultShowWindowHeight:undefined, true);
	            }else{
	                var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=${eventType}&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&model=${model}&cachenum=" + Math.random();
	                showMaxJqueryWindow("查看事件信息", url, fetchWinWidth(), defaultShowWindowHeight && defaultShowWindowHeight<document.body.clientHeight?defaultShowWindowHeight:undefined, true);
	            }
	        }
	    }
     </@override>
</#if>


<@extends name="/zzgl/event/list_event.ftl" />