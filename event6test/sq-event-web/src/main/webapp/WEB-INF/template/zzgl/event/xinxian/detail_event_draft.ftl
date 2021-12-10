<@override name="eventDetailPageTitle">
	莘县事件详情页面
</@override>

<@override name="gridPathTd">
	<td class="LeftTd"><label class="LabName"><span>所属区域：</span></label><div class="Check_Radio FontDarkBlue" style="width:62%"><#if event.gridPath??>${event.gridPath}</#if></div></td>
</@override>
								    
<@extends name="/zzgl/event/detail_event_draft.ftl" />