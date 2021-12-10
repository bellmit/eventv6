<@override name="eventToolbar">
<#if isPointPage??>
<#include "/zzgl/event/nanChangForLargeData/eventDataGridToolbar.ftl" />
<#else>
<#include "/zzgl/event/nanChangForLargeData/eventDataGridToolbarForEventColumn.ftl" />
</#if>
</@override>

<@extends name="/zzgl/event/column_event.ftl" />
