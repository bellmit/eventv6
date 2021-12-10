
<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>

			
<div class="mdt-item flex "><div><p class="mdt-icon">业主姓名：</p></div><p class="white flex1 variable" id="personInvolved"></p></div>
<div class="mdt-item flex "><div><p class="mdt-icon">场所面积：</p></div><p class="white flex1 variable" id="siteArea"></p></div>
<div class="mdt-item flex "><div><p class="mdt-icon">隐患类型：</p></div><p class="white flex1 variable" id="hiddenDangerTypeName"></p></div>
<div class="mdt-item flex "><div><p class="mdt-icon">处置时限：</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
<div class="mdt-item flex hide"><div><p class="mdt-icon">处置结果：</p></div><p class="white flex1 variable" id="disposalResultStr"></p></div>
<div class="mdt-item flex hide"><div><p class="mdt-icon">延期日期：</p></div><p class="white flex1 variable" id="extensionDateStr"></p></div>
<div class="mdt-item flex hide"><div><p class="mdt-icon">发现部门：</p></div><p class="white flex1 variable" id="discoveryStaff"></p></div>
<div class="mdt-item flex hide"><div><p class="mdt-icon">反馈时限：</p></div><p class="white flex1 variable" id="feedbackTimeStr"></p></div>
<div class="mdt-item flex hide"><div><p class="mdt-icon">举报内容：</p></div><p class="white flex1 variable" id="tipoffContent"></p></div>
<div class="mdt-item flex hide"><div><p class="mdt-icon">隐患描述：</p></div><p class="white flex1 variable" id="hdtDescribe"></p></div>
			
			<!-- 备注id:remark 公共页有赋值 detail_entry_page.ftl -->
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
</div>
</@override>
<@override name="entryTypeLeftContentSet">
	setOwnValue(event,'personInvolved');
	setOwnValue(event,'siteArea');
	setOwnValue(event,'hiddenDangerTypeName');
	if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}
	if(event.disposalResultStr){
		if(event.extensionDateStr){setValue(event,'extensionDateStr'); }
		setValue(event,'disposalResultStr',"（一级网格处置结果）");
	}
	if(event.discoveryStaff){setValue(event,'discoveryStaff');}
	if(event.tipoffContent){setValue(event,'tipoffContent');}
	
	if(event.dataSource&&event.dataSource=='02'){setValue(event,'feedbackTimeStr');}
	if(event.hdtDescribe&&event.hiddenDangerType=='99'){setValue(event,'hdtDescribe');}

</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />