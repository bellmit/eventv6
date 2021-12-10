<@override name="eventOrgListPageTitle">
	莘县事件列表页面
</@override>

<@override name="labelName4QueryGrid">
	所属区域：
</@override>

<@override name="extraGridOption">
	$.extend(gridOpt, {
		rootName : "所属区域"
	});
</@override>

<@override name="gridPathField">
	{field:'gridPath',title:'所属区域', align:'center',width:fixWidth(0.18), formatter: titleFormatter},
</@override>

<@extends name="/zzgl/event/eventOrg/list_event_org.ftl" />