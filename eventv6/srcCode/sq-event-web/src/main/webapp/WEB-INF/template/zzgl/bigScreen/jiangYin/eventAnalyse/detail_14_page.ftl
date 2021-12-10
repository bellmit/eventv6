
<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>

			
<div class="mdt-item flex hide"><div><p class="mdt-icon">发现渠道：:</p></div><p class="white flex1 variable" id="discoveryChannelName"></p></div>
<div class="mdt-item flex hide"><div><p class="mdt-icon" id="discoveryStaffName">发现部门:</p></div><p class="white flex1 variable" id="discoveryStaff"></p></div>			
<div class="mdt-item flex "><div><p class="mdt-icon">问题类型:</p></div><p class="white flex1 variable" id="problemTypeName"></p></div>
<div class="mdt-item flex hide"><div><p class="mdt-icon">反馈时限:</p></div><p class="white flex1 variable" id="feedbackTimeStr"></p></div>
<div class="mdt-item flex hide"><div><p class="mdt-icon">登记内容:</p></div><p class="white flex1 variable" id="tipoffContent"></p></div>
<div class="mdt-item flex hide"><div><p class="mdt-icon">问题描述:</p></div><p class="white flex1 variable" id="problemDesc"></p></div>
			
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