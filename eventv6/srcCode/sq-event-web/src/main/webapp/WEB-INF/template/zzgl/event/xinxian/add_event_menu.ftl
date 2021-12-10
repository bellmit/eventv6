<@override name="eventMenuPageTitle">
	莘县事件采集页面
</@override>

<@override name="gridNameTd">
	<td>
		<label class="LabName"><span>所属区域：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox" style="width:122px;" data-options="required:true" id="gridName" name="gridName" value="${event.gridName!}" />
	</td>
</@override>

<@override name="extraGridOption">
	$.extend(gridOpt, {
		rootName : "所属区域"
	});
</@override>

<@extends name="/zzgl/event/add_event_menu.ftl" />