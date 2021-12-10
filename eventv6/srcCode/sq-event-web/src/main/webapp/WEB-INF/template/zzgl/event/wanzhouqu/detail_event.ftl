<@override name="eventDetailPageTitle">
	重庆万州区事件详情页面
</@override>

<@override name="detailExtend">
	<#if event.eventExtend??>
	<tr>
		<td align="right" >预案类型：</td>
		<td width="18%"><code>${event.eventExtend.planTypeName!}</code></td>
		<td align="right" >预案等级：</td>
		<td colspan="3"><code>${event.eventExtend.planLevelName!}</code></td>
	</tr>
	<#if event.eventExtend.influenceLevel??>
	<tr>
		<td align="right" >影响程度：</td>
		<td><#if event.eventExtend.influenceLevel != '0'><img src="${rc.getContextPath()}/images/influenceLevel/${event.eventExtend.influenceLevel!}.png" style="margin:0 10px 0 0; width:28px; height:28px;"></#if></td>
		<td align="right" >启动预案：</td>
		<td colspan="3">
			<code>
				<#if event.eventExtend.isPlan?? && event.eventExtend.isPlan == '1'>
					是<#if event.eventExtend.planConfigId??><img title="查看预案人员信息" onclick="showPlanConfigInfo();" src="${uiDomain!''}/images/workLog.png" style="padding-left: 5px; cursor: pointer;"/></#if>
				<#else>
					否
				</#if>
			</code>
		</td>
	</tr>
	</#if>
	</#if>
</@override>

<@override name="extraDetailFunction">
	function showPlanConfigInfo() {
		var planConfigId = "<#if event.eventExtend??>${event.eventExtend.planConfigId!}</#if>";
		
		if(planConfigId) {
			var url = "${rc.getContextPath()}/zhsq/zzgl/planConfig/detail.jhtml?id=" + planConfigId;
			
			showMaxJqueryWindow('查看预案人员信息', url);
		}
	}
</@override>

<@extends name="/zzgl/event/detail_event.ftl" />