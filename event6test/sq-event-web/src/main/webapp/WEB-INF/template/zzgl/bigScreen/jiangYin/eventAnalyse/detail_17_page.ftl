
<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>

			
<div class="mdt-item flex hide"><div><p class="mdt-icon">：</p></div><p class="white flex1 variable" id="discoveryChannelName"></p></div>
			
			<!-- 备注id:remark 公共页有赋值 detail_entry_page.ftl -->
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
</div>
</@override>
<@override name="entryTypeLeftContentSet">
	$("#problemTypeName").html(event.problemTypeName||'');
	if(event.discoveryChannel){
		setValue(event,'discoveryChannelName');
		setValue(event,'discoveryStaff');
		if(event.discoveryStaffName){$("#discoveryStaffName").html(event.discoveryStaffName);}
	}
	
if(event.dataSource&&event.dataSource=='02'){setValue(event,'feedbackTimeStr');setValue(event,'tipoffContent');}
if(event.problemDesc){setValue(event,'problemDesc');}

</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />