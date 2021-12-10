<@override name="handlePageTitle"></@override>
<@override name="intervalTr">
	<tr class="intervalTr hide">
		<td>
			<label class="LabName"><span><label class="Asterik">*</label>时限：</span></label>
			<input type="text" id="interval" name="interval" class="inp1 easyui-numberbox" data-options="tipPosition:'bottom',max:60,min:1" style="width: 210px; height: 28px;" value="" />
		</td>
	</tr>
	<tr class="intervalTr hide">
		<td>
			<label class="LabName"><span>时限单位：</span></label>
			<div class="Check_Radio"><input type="radio" name="intervalUnit" id="intervalUnit_1" value="1" checked /> <label for="intervalUnit_1" style="cursor:pointer; padding-right: 40px;">工作日</label></div>
			<div class="Check_Radio"><input type="radio" name="intervalUnit" id="intervalUnit_3" value="3" /> <label for="intervalUnit_3" style="cursor:pointer;">小时</label></div>
		</td>
	</tr>
</@override>
<@override name="bigFileUploadInitOption">
	$.extend(bigFileUploadOpt, {
		fileExt: '.jpg,.gif,.png,.jpeg,.webp,.mp4,.mp3'
	});
</@override>
<@override name="selectUserInitExtraParam">
	callback4Confirm : sendSMS4Department,
	isShowOrgNameFuzzyQuery : true,
	isShowUserPartyNameFuzzyQuery : true,
</@override>
<@override name="extraFunction4Handle">
	function sendSMS4Department(isForceCheck) {
		var nextNodeName = $('#nodeName_').val(), DEPARTMENT_NODE_CODE = "task7",
			isSendSMSChecked = $("#sendSms_").is(":checked");
		isForceCheck = isForceCheck === undefined ? true : isForceCheck;
		
		if((nextNodeName == DEPARTMENT_NODE_CODE) && (isForceCheck == true || isSendSMSChecked == true)) {
			$('#remarkTr').hide();
			
			$('#interval').numberbox({
				required: false
			});
			
			$('#sendSms_').attr('checked', true);
			showSmsCont();
			
			$('#interval').numberbox({
				required: true
			});
			$('#advice').validatebox({
				required: true
			});
			$('#remarkTr').show();
			
		}
	}
</@override>
<@override name="operateAfterNextNodeSelected">
	if(data) {
		var interval = data.interval,
			intervalUnit = data.intervalUnit,
			nextStaffNames = data.userNames || '',
			nextOrgNames = data.orgNames || '';
		
		if(interval) {
			$('#interval').numberbox('setValue', interval);
		}
		
		if(intervalUnit) {
			$('#intervalUnit_' + intervalUnit).attr('checked', true);
		}
		
		if(nextStaffNames) {
			$('#nextStaffNames').val(nextStaffNames);
		}
		
		if(nextOrgNames) {
			$('#curOrgNames').val(nextOrgNames);
		}
	}
</@override>

<@extends name="/zzgl/event/workflow/handle_nch_jointco.ftl" />