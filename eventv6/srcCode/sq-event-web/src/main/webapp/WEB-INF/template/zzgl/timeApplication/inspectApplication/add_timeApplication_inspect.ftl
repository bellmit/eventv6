<@override name="pageTitle">
	事件督查申请新增页面
</@override>

<@override name="extendHeadInclude">
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.LabName{width: 70px;}
	</style>
</@override>

<@override name="timeApplicationAdviceColoumnSpan">
	colspan="2"
</@override>

<@override name="timeAppIntervalTr">
	<tr>
		<td class="LeftTd">
			<label class="LabName"><span>督查程度：</span></label>
			<input id="inspectDegree" name="inspectDegree" type="text" value="" class="hide"/>
			<input id="inspectDegreeName" type="text" class="inp1 easyui-validatebox" data-options="required: true, tipPosition:'top'" />
		</td>
		<td class="LeftTd">
			<label class="LabName"><span>督查类型：</span></label>
			<input id="inspectType" name="inspectType" type="text" value="" class="hide"/>
			<input id="inspectTypeName" type="text" class="inp1 easyui-validatebox" data-options="required: true, tipPosition:'top'" />
		</td>
	</tr>
</@override>

<@override name="extendInit">
	AnoleApi.initTreeComboBox("inspectDegreeName", "inspectDegree", "B191", null, ["01"]);
	
	AnoleApi.initTreeComboBox("inspectTypeName", "inspectType", "B197", null, ["99"]);
</@override>

<@extends name="/zzgl/timeApplication/add_timeApplication_base.ftl" />