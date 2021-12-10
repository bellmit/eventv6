
<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>
			
			<div class="mdt-item flex"><div><p class="mdt-icon">问题类型:</p></div><p class="white flex1 variable" id="matterTypeName"></p>			</div>
			<div class="mdt-item flex"><div><p class="mdt-icon" id="personInvolvedDomp"></p></div><p class="white flex1 variable" id="personInvolved"></p>			</div>
			<div class="mdt-item flex"><div><p class="mdt-icon">违规排放企业名称:</p></div><p class="white flex1 variable" id="ildischargeEnterpriseName"></p>			</div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon" id="tipoffContentName"></p></div><p class="white flex1 variable" id="tipoffContent"></p>			</div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">行政处罚:</p></div><p class="white flex1 variable" id="administrativeSactionTypeName"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
</div>
</@override>

<@override name="entryTypeLeftContentSet">
	$('#matterTypeName').html(event.matterTypeName);
	$('#ildischargeEnterpriseName').html(event.ildischargeEnterpriseName);
	$('#administrativeSactionTypeName').html(event.administrativeSactionTypeName);
	$('#personInvolved').html(event.personInvolved);

	if(event.dataSource&&event.dataSource=='04'){
		$('#personInvolvedDomp').html('交办部门:');
		$('#tipoffContentName').html('内容摘要:');
		setValue(event,'tipoffContent');
	}else{
		$('#personInvolvedDomp').html('业主姓名:');
	}
	if(event.dataSource&&event.dataSource=='03'){
		$('#tipoffContentName').html('举报内容:');
		setValue(event,'tipoffContent');
	}

	if(event.administrativeSactionTypeName){	
		setValue(event,'administrativeSactionTypeName');
	}
	if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}

</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />