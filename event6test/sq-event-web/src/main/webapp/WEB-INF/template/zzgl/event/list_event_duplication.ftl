<@override name="eventAncestorListPageTitle">重复事件关联列表</@override>
<@override name="eventAncestorListUrl">
	'${rc.getContextPath()}/zhsq/event/eventDisposalController/listEventDuplicationData.json'
</@override>
<@override name="eventAncestorListQueryParam">
	{'leadEventId': '${leadEventId?c}'}
</@override>

<@extends name="/zzgl/event/list_event_ancestor.ftl" />