<@override name="extraJs4EventAttrEdit">
	<@super></@super>
	<#include "/component/ComboBox.ftl" />
</@override>
<@override name="extraTr4EventAttrEdit">
	<@super></@super>
	<tr>
		<td>
			<label class="LabName"><span>所属网格：</span></label>
			<input type="text" class="inp1 InpDisable easyui-validatebox w60" data-options="required:true" id="gridName" name="gridName" value="${event.gridName!}" />
			<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>" />
			<input type="hidden" id="gridCode" name="gridCode" value="${event.gridCode!}" />
		</td>
	</tr>
</@override>
<@override name="extraInit4EventAttrEdit">
	<@super></@super>
	AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
		if(isNotBlankParam(items) && items.length > 0) {
			var grid = items[0];
			$("#gridCode").val(grid.orgCode);
		}
	});
</@override>
