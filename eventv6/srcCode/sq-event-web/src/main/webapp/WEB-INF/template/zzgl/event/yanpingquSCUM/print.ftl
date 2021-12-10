<@override name="eventPrintPageTitle">延平区智慧城管事件打印页面</@override>
<@override name="eventPrintHeadTitle">智慧城管平台事件详情单</@override>
<@override name="contactUseTr">
	<tr class="tr_style">
		<td width="90">受理人员：</td>
	    <td width="410">${event.contactUser!}</td>
	    <td width="90">联系电话：</td>
	    <td width="410">${event.tel!}</td>
	</tr>
</@override>
<@override name="singleLineExtraInfoTr">
	<tr class="tr_style">
		<td width="90">举报人员：</td>
		<td width="410">${event.informant!}</td>
		<td width="90">举报电话：</td>
		<td width="410">${event.informantTel!}</td>
	</tr>
</@override>

<@extends name="/zzgl/event/print.ftl" />