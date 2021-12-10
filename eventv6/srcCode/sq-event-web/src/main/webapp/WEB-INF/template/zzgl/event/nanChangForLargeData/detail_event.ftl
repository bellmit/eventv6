<@override name="eventDetailPageTitle">
	江西省南昌市大数据平台详情页面
</@override>
<@override name="detailExtend">
<tr>
    <td width="75px;" align="right" >巡访类型：</td>
    <td><code>${patrolTypeName!}</code></td>
    <td width="13%" align="right" >责任点位：</td>
    <td><code>${pointSelectionName!}</code></td>
    <td width="13%" style="text-align:right;">发生类型：</td>
    <td><code>${eventLabelName!}</code></td>
</tr>
</@override>

<@override name="contactUserLabelSpan"><label class="Asterik">*</label>联系人员：</@override>

<@extends name="/zzgl/event/detail_event.ftl" />