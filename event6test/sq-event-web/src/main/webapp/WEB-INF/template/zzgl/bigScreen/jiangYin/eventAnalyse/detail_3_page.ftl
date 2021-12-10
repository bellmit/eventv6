
<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>
			
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">企业名称:</p>
				</div>
				<p class="white flex1 variable" id="enterpriseName"></p>
			</div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">存在隐患:</p></div><p class="white flex1 variable" id="hiddenDangerTypeNo">否</p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">隐患类型:</p></div><p class="white flex1 variable" id="hiddenDangerTypeName"></p></div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">举报内容:</p>
				</div>
				<p class="white flex1 variable" id="tipoffContent"></p>
			</div>
			<div class="mdt-item flex"><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
		</div>
</@override>

<@override name="entryTypeLeftContentSet">
	$('#enterpriseName').html(event.enterpriseName);

	if(event.isHiddenDanger && event.isHiddenDanger == '0'){
		$('#hiddenDangerTypeNo').parent().removeClass('hide');
	}else{
		setValue(event,'hiddenDangerTypeName');
	}
	if(event.dataSource == '05'){	
		setValue(event,'tipoffContent');
	}
if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}

</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />