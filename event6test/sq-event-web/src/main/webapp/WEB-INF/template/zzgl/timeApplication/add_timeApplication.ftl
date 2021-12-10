<@override name="timeAppIntervalTr">
	<tr>
		<td class="LeftTd">
			<label class="LabName"><span>时限：</span></label>
			<input type="text" id="interval" name="interval" class="inp1 easyui-numberbox" data-options="required:true, max:<#if maxInterval??>${maxInterval?c}<#else>60</#if>, min:1" style="width: 210px; height: 28px;" value="" />
		</td>
	</tr>
	<tr <#if isShowIntervalUnit?? && isShowIntervalUnit == false>class="hide"</#if>>
		<td class="LeftTd">
			<label class="LabName"><span>时限单位：</span></label>
			<#if intervalUnitMapList?? && (intervalUnitMapList?size > 0)>
				<#list intervalUnitMapList as intervalUnitItem>
					<div class="Check_Radio"><input type="radio" name="intervalUnit" id="intervalUnit_${intervalUnitItem.dictGeneralCode}" value="${intervalUnitItem.dictGeneralCode}" checked /> <label for="intervalUnit_${intervalUnitItem.dictGeneralCode}" style="cursor:pointer; padding-right: 40px;">${intervalUnitItem.dictName}</label></div>
				</#list>
			<#else>
				<div class="Check_Radio"><input type="radio" name="intervalUnit" id="intervalUnit_1" value="1" checked /> <label for="intervalUnit_1" style="cursor:pointer; padding-right: 40px;">工作日</label></div>
				<div class="Check_Radio"><input type="radio" name="intervalUnit" id="intervalUnit_2" value="2" /> <label for="intervalUnit_2" style="cursor:pointer;">自然日</label></div>
			</#if>
		</td>
	</tr>
</@override>
<@override name="extendInit">
	$('#intervalUnit_' + $('#defaultIntervalUnit').val()).attr('checked', true);
</@override>

<@extends name="/zzgl/timeApplication/add_timeApplication_base.ftl" />