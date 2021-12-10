<@override name="pageTitle">
	事件分类修改申请新增页面
</@override>

<@override name="extendHeadInclude">
	<#include "/component/ComboBox.ftl" />
	
	<style type="text/css">
		.eventTypeLabWidth{width: 100px;}
	</style>
</@override>

<@override name="timeAppIntervalTr">
	<tr>
		<td class="LeftTd">
			<input type="hidden" name="eventTypePre" value="${eventTypePre!}" />
			<label class="LabName eventTypeLabWidth"><span>现有事件分类：</span></label><div class="Check_Radio FontDarkBlue"><#if eventTypePreMap??>${eventTypePreMap.dictFullPath!''}</#if></div>
		</td>
	</tr>
	<tr>
		<td class="LeftTd">
			<label class="LabName eventTypeLabWidth"><span>修改事件分类：</span></label>
			<input id="eventTypeAfter" name="eventTypeAfter" type="text" value="" class="hide"/>
			<input id="eventTypeAfterName" type="text" class="inp1 easyui-validatebox" data-options="required: true, tipPosition:'top'" />
		</td>
	</tr>
</@override>

<@override name="reasonLabel">
	<label class="LabName eventTypeLabWidth"><span>分类修改原因：</span></label>
</@override>

<@override name="extendInit">
	AnoleApi.initTreeComboBox("eventTypeAfterName", "eventTypeAfter", "<#if eventTypePreMap??>${eventTypePreMap.dictCodeTop!}</#if>", null, null, {
		EnabledSearch : true
	});
</@override>

<@override name="extraCheck">
	if(isValid) {
		var eventTypePre = $('#eventTypePre').val(),
			eventTypeAfter = $('#eventTypeAfter').val();
		
		if(eventTypeAfter == eventTypePre) {
			$.messager.alert('提示', '修改后事件分类与现有事件分类一致，无需提交申请！', 'warning');
			return;
		}
	}
</@override>

<@extends name="/zzgl/timeApplication/add_timeApplication_base.ftl" />