<@override name="eventDetailPageTitle">
	江西省南昌市大数据平台详情页面
</@override>
<@override name="eventTitleInput">
	  <td><label class="LabName"><span><label class="Asterik">*</label>事件标题：</span></label><div class="Check_Radio FontDarkBlue"><#if event.eventName??>${event.eventName}</#if></div></td>
	  <td><label class="LabName"><span>巡访类型：</span></label><div class="Check_Radio FontDarkBlue"><#if patrolTypeName??>${patrolTypeName}</#if></div></td>
</@override>

<@override name="geographicalLabelingInput">
<tr class="DotLine">
	  <td colspan="2" class="LeftTd"><label class="LabName"><span>地理位置：</span></label>
		  <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
	      <input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
	  </td>
	  <td><label class="LabName"><span>责任点位：</span></label><div class="Check_Radio FontDarkBlue"><#if pointSelectionName??>${pointSelectionName}</#if></div></td>
</tr>
</@override>

<@override name="occurInputBlock">
<tr>
    <td colspan="2" class="LeftTd"><label class="LabName"><span><label class="Asterik">*</label>事发详址：</span></label><div class="Check_Radio FontDarkBlue" style="width:83%"><#if event.occurred??>${event.occurred}</#if></div></td>
	<td><label class="LabName"><span>发生类型：</span></label><div class="Check_Radio FontDarkBlue"><#if eventLabelName??>${eventLabelName}</#if></div></td>
</tr>
</@override>

<@override name="contactUserTd">
	<td><label class="LabName"><span id="contactUserLabelSpan"><label class="Asterik">*</label>联系人员：</span></label><div class="Check_Radio FontDarkBlue"><#if event.contactUser??>${event.contactUser}</#if></div></td>
</@override>
<@override name="contactUserTelTd">
	<td><label class="LabName"><span id="contactTelLabelSpan"><label class="Asterik">*</label>联系电话：</span></label><div class="Check_Radio FontDarkBlue"><#if event.tel??>${event.tel}</#if></div></td>
</@override>

<@extends name="/zzgl/event/detail_event_draft.ftl" />