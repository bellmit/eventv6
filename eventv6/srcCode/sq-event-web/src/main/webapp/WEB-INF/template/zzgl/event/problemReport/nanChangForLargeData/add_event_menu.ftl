<@override name="eventMenuPageTitle">
	南昌市大数据问题上报事件采集
</@override>
<@override name="attachmentCheck">
	if(isValid) {
		isValid = checkPicture(toClose, $('#bigFileUploadDiv div[file-status="complete"]'));
	}
</@override>
<@override name="extraFtlInclude">
	<#include "/zzgl/event/problemReport/nanChangForLargeData/check_attachment.ftl" />
</@override>
<@extends name="/zzgl/event/add_event_menu.ftl" />