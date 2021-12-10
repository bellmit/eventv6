<@override name="eventAddPageTitle">
	南昌市大数据背街小巷事件采集/编辑页面
</@override>
<@override name="attachmentCheck">
	if(isValid) {
		isValid = checkPicture(toClose, $('#bigFileUploadDiv div[file-status="complete"]'));
	}
</@override>
<@override name="extraFtlInclude">
	<#include "/zzgl/event/streetLand/nanChangForLargeData/check_attachment.ftl" />
</@override>
<@extends name="/zzgl/event/add_event_000.ftl" />