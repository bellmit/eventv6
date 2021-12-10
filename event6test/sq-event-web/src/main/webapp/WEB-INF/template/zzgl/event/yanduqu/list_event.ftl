<@override name="eventListPageTitle">
	盐城市盐都区事件列表页面
</@override>
<@override name="eventToolbar">
	<#if map??>
		<!-- 3357982GIS上看网格员整体工作情况  -->
		<#include "/zzgl/event/yanduqu/eventDataGridToolbarForMap.ftl" />
	<#else>	
		<#include "/zzgl/event/yanduqu/eventDataGridToolbar.ftl" />
		
		<@override name="function_clickFormatter_body">
			<#if eventAttrTrigger?? && eventAttrTrigger == '12345HotLine'>
				return '<a class="eName" href="###" title="'+ rec.eventName +'" onclick=showDetailRow('+ rec.eventId+ ','+rec.instanceId+','+rec.workFlowId+',"'+rec.bizPlatform+'","'+rec.type+'")>'+value+'</a>';
			<#else>
				var isAttention = rec.isAttention,
					  wfStatus = rec.wfStatus,
					  eventType = "${eventType!}",
					  value = value || "";
				
				var attentionVal = '';
				<#if isShowAttentionBtn?? && isShowAttentionBtn>
					if(isAttention) {
						attentionVal = '<li class="guanzhu" onMouseover="showVal(this)" onMouseout="hideVal(this)"  onclick=attentionEvent(this,"'+ rec.isAttention+ '",'+rec.eventId+')>已关注</li>';
					} else {
						attentionVal = '<li class="guanzhu"  onclick=attentionEvent(this,"'+ rec.isAttention + '",'+rec.eventId+')>添加关注</li>';
					}
				</#if>
				
				var tab = '';
				<#if eventType == "all" || eventType == "toRemind">
					var remindVal = "";
					
					if(wfStatus == '1') {
						<#if isShowAttentionBtn?? && isShowAttentionBtn>
							remindVal += '<li class="line"></li>';
						</#if>
						
						remindVal += '<li class="duban" onclick=remindEvent1(this,'+rec.eventId+','+rec.instanceId+')>督办</li>';
					}
					
					if(remindVal) {
						tab = '<div class="OperateNotice" style="display:none"><div class="operate"><ul>'+attentionVal+remindVal+'</ul><div class="arrow"></div></div></div>';
					}
				<#elseif eventType == "my">
					if(wfStatus == '1') {
						var urgeVal = '<li class="cuiban" onclick=urgeEvent(this,'+rec.eventId+','+rec.instanceId+')>催办</li>';
						tab = '<div class="OperateNotice" style="display:none"><div class="operate"><ul>'+urgeVal+'</ul><div class="arrow"></div></div></div>';
					}
				</#if>
				
				<#if eventAttrTrigger?? && eventAttrTrigger == '12345HotLine'>
				<#else>
					value = iconFormatter(value, rec, rowIndex) + value;
				</#if>
				
				return tab +'<a class="eName" href="###" title="'+ rec.eventName +'" onclick=showDetailRow('+ rec.eventId+ ','+rec.instanceId+','+rec.workFlowId+',"'+rec.bizPlatform+'","'+rec.type+'")>'+value+'</a>';
			</#if>
		</@override>
	</#if>
</@override>


<@override name="singleSelect">
	<#if multiple??>
		singleSelect:false,
	</#if>
</@override>

<@override name="checkbox">
	<#if multiple??>
	{field:'ck',checkbox:true,outMenu:true},
	</#if>
</@override>

<@override name="handleDateStrField">
	{field:'handleDateStr',title:'办结期限', align:'center',width:fixWidth(0.15),sortName:'T1.HANDLE_DATE',sortable:true<#if eventAttrTrigger?? && eventAttrTrigger == '12345HotLine'><#else>, formatter: handleDateStrFormatter</#if>},
</@override>

<#if eventAttrTrigger?? && eventAttrTrigger == '12345HotLine'>
	<@override name="eventClassField">
		{field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.18), sortName:'T1.TYPE_', sortable:true, hidden:true, formatter: titleFormatter},
	</@override>
</#if>

<@override name="extraFunction">
	<#if multiple??>
	onBeforeLoad: function(param){
	   	$('#list').datagrid('clearChecked');
	},
	</#if>
</@override>

<@extends name="/zzgl/event/list_event.ftl" />
