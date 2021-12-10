<@override name="eventListPageTitle">
	晋江事件列表
</@override>

<@override name="eventToolbar">
	<#include "/zzgl/event/jinJiangShi/eventDataGridToolbar.ftl" />
</@override>

<@override name="eventDetailUrlExtraParam">
	url += '&isCapEventLabelInfo=true';
</@override>

<@override name="eventEditUrlExtraParam">
	url += '&isCapEventLabelInfo=true';
</@override>

<@override name="detailEventPageInfo">
	opt.title = "查看事件信息";
	opt.targetUrl = url;
	openJqueryWindowByParams(opt);
</@override>

<@extends name="/zzgl/event/list_event.ftl" />