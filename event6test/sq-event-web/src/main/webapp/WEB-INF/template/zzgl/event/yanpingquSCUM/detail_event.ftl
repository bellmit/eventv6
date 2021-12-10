<@override name="eventDetailPageTitle">延平区智慧城管事件信息详情</@override>
<@override name="urgencyDegreeNameTd">
	<#if event.handleDateStr??>
	<td width="13%" align="right" >办结时限：</td>
	<td>
		<code>
			${event.handleDateStr}
		</code>
	</td>
	<#else >
	<td width="13%" align="right" ></td>
	<td><code></code></td>
	</#if>
</@override>
<@override name="contactUserLabelSpan">受理人员：</@override>
<@override name="singleLineExtraInfoTr">
	<td align="right">紧急程度：</td>
	<td>
		<#if event.urgencyDegree?? && event.urgencyDegree!='01'>
			<code style="color:#e60012;">${event.urgencyDegreeName!}</code>
		<#else>
			<code>${event.urgencyDegreeName!}</code>
		</#if>
	</td>
	<td align="right">举报人员：</td>
	<td colspan="3">
		<code>${event.informant!}<#if event.informantTel??>(${event.informantTel})</#if></code>
	</td>
</@override>

<@extends name="/zzgl/event/detail_event.ftl" />