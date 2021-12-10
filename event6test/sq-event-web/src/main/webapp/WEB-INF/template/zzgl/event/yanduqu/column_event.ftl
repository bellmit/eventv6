<@override name="eventListPageTitle">
	盐城市盐都区事件列表页面
</@override>
<@override name="eventToolbar">
	<#if map??>
		<!-- 3357982GIS上看网格员整体工作情况  -->
		<#include "/zzgl/event/yanduqu/eventDataGridToolbarForMap.ftl" />
	<#else>
		<#include "/zzgl/event/yanduqu/eventDataGridToolbar.ftl" />
	</#if>
</@override>

<@extends name="/zzgl/event/column_event.ftl" />