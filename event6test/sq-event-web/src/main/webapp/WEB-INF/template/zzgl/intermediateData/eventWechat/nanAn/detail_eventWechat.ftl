<@override name="showEventWin">
	<!--审核详情页查看事件进度-->
	<a href="#" class="BigNorToolBtn PreviewBtn" onclick="parent.showEventWin('${eventWechat.eventId?c}','${eventWechat.reportUUID!}','${eventWechat.reportType!}');">查看进度</a>
</@override>
<@override>
	<td class="LeftTd">
		<label class="LabName"><span>事件标题：</span></label><div style="width: calc(50vw - 100px);" class="Check_Radio FontDarkBlue">${eventWechat.eventName!}</div>
	</td>
	<td>
		<label class="LabName"><span>事件分类：</span></label><div class="Check_Radio FontDarkBlue">${eventWechat.eventTypeName!}</div>
	</td>
</@override>
<@extends name="/zzgl/intermediateData/eventWechat/detail_eventWechat.ftl" />