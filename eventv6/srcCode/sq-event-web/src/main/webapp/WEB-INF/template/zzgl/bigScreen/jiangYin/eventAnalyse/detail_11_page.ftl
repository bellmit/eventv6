
<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>

			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">致贫返贫原因:</p>
				</div>
				<p class="white flex1 variable" id="povBackReasonName"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">姓名:</p>
				</div>
				<p class="white flex1 variable" id="personName"></p>
			</div>
			<div class="mdt-item flex"><div><p class="mdt-icon">证件号码:</p></div><p class="white flex1 variable" id="cardNumber"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">致返贫说明:</p></div><p class="white flex1 variable" id="povBackDesc"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">家庭成员:</p></div><p class="white flex1 variable" id="partyMember"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">是否属于本村:</p></div><p class="white flex1 variable" id="isHonmura"></p></div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">备注:</p>
				</div>
				<p class="white flex1 variable" id="remark"></p>
			</div>
</div>
</@override>
<@override name="entryTypeLeftContentSet">
	$('#povBackReasonName').html(event.povBackReasonName);
	$('#personName').html(event.personName);
	$('#cardNumber').html(event.cardNumber);
	if(event.povBackDesc){$('#povBackDesc').html(event.povBackDesc).parent().removeClass('hide');}
	if(event.partyMember){$('#partyMember').html(event.partyMember).parent().removeClass('hide');}
	if(event.isHonmura){$('#isHonmura').html(event.isHonmura).parent().removeClass('hide');}

	$('#remark').html(event.remark||'');
	
</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />