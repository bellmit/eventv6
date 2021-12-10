<@override name="eventDetailPageTitle">
	莘县事件详情页面
</@override>

<@override name="gridPathTr">
	<tr class="DotLine">
		<td align="right" >所属区域：</td>
		<td colspan="5"><code>${event.gridPath!}</code></td>
	</tr>
</@override>
                                
<@extends name="/zzgl/event/detail_event.ftl" />