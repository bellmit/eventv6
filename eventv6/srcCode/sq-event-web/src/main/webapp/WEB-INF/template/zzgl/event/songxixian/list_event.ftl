<@override name="eventListPageTitle">
	松溪县事件列表页面
</@override>
<@override name="extraListParam">
<input type="hidden" name="isCapCurNodeInfo" value="<#if eventType?? && eventType == 'all' && model?? && model == 'c'>true<#else>false</#if>" class="queryParam" />
</@override>
<@override name="extraListColumn">
<#if eventType?? && eventType == "all">
	{field:'curNodeNameZH',title:'当前环节', align:'center',width:fixWidth(0.12),sortable:true, formatter: titleFormatter},
</#if>
</@override>
<@override name="eventToolbar">
	<#include "/zzgl/event/songxixian/eventDataGridToolbar.ftl" />
</@override>

<@extends name="/zzgl/event/list_event.ftl" />