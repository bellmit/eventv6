<@override name="eventDetailPageTitle">
	南昌市大数据背街小巷草稿事件详情页面
</@override>
<@override name="eventExtraJs">
	<#include "/zzgl/event/streetLand/nanChangForLargeData/check_attachment.ftl" />
</@override>
<@override name="attachmentCheck">
	if(!checkPicture(toClose, $('#bigFileUploadDiv div[file-status="complete"]'))) {
		return false;
	}
</@override>

<@extends name="/zzgl/event/detail_event_draft.ftl" />