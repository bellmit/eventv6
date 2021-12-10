<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>
			
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">会议类型:</p>
				</div>
				<p class="white flex1 variable" id="meetingTypeName"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">党组织名称:</p>
				</div>
				<p class="white flex1 variable" id="partyGroupName"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">应到会党员:</p>
				</div>
				<p class="white flex1 variable" id="shouldAttriveNum"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">实到会党员:</p>
				</div>
				<p class="white flex1 variable" id="actualNumber"></p>
			</div>
			<div class="mdt-item flex"><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
</div>
</@override>

<@override name="entryTypeLeftContentSet">

$('#meetingTypeName').html(event.meetingTypeName);
$('#partyGroupName').html(event.partyGroupName);
$('#shouldAttriveNum').html(event.shouldAttriveNum+"人");
$('#actualNumber').html(event.actualNumber+"人");

	if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}
</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />