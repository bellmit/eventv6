<@override name="extendHeadInclude">
	<style type="text/css">
		.auditRadio{cursor: pointer; color: #7c7c7c;}
	</style>
</@override>

<@override name="timeAppIntervalTr">
	<tr>
		<td class="LeftTd">
			<label class="LabName"></label><div class="Check_Radio"><input type="radio" name="timeAppCheckStatusRadio" id="auditSuccess" value="1" checked/><label id="auditSuccessLabel" for="auditSuccess" class="auditRadio">核验通过</label></div>
		</td>
		<td class="LeftTd">
			<div class="Check_Radio"><input type="radio" name="timeAppCheckStatusRadio" id="auditFail" value="2"/><label id="auditFailLabel" for="auditFail" class="auditRadio">核验不通过</label></div>
		</td>
	</tr>
</@override>
<@override name="timeApplicationAdviceTdSpan">
	colspan="2"
</@override>
<@override name="reasonLabel">
	<label class="LabName"><span>核验意见：</span></label>
</@override>
<@override name="extendInit">
	$('#timeApplicationForm input[type="radio"][name="timeAppCheckStatusRadio"]').on('click', function() {
		var auditStatus = $(this).val();
		
		$('#reason').validatebox({
			required: auditStatus == '2'
		});
		
		$('#timeAppCheckStatus').val(auditStatus);
	});
	
	$('#timeApplicationForm input[type="radio"][name="timeAppCheckStatusRadio"]:checked').eq(0).click();
</@override>

<@extends name="/zzgl/timeApplication/add_timeApplication_base.ftl" />