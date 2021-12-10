<@override name="eventDetailPageTitle">延平区智慧城管草稿事件信息详情</@override>
<@override name="contactUserTd">
	<td><label class="LabName"><span>受理人员：</span></label><div class="Check_Radio FontDarkBlue" style="width:62%">${event.contactUser!}<#if event.tel??>(${event.tel})</#if></div></td>
</@override>
<@override name="contactUserTelTd">
	<td><label class="LabName"><span>举报人员：</span></label><div class="Check_Radio FontDarkBlue" style="width:62%">${event.informant!}<#if event.informantTel??>(${event.informantTel})</#if></div></td>
</@override>
<@override name="bigFileUploadInitOption">
	$.extend(bigFileUploadOpt, {
		fileExt: '.jpg,.gif,.png,.jpeg,.webp,.mp4,.mp3'
	});
</@override>

<@extends name="/zzgl/event/detail_event_draft.ftl" />