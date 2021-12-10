<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>

			
			<div class="mdt-item flex"><div><p class="mdt-icon">姓名:</p></div><p class="white flex1 variable" id="partyName_"></p></div>
			<div class="mdt-item flex "><div><p class="mdt-icon">证件类型:</p></div><p class="white flex1 variable" id="certTypeName"></p></div>
			<div class="mdt-item flex "><div><p class="mdt-icon">证件号码:</p></div><p class="white flex1 variable" id="identityCard"></p></div>
			<div class="mdt-item flex "><div><p class="mdt-icon">现住地:</p></div><p class="white flex1 variable" id="iResidenceAddr"></p></div>
			<div class="mdt-item flex "><div><p class="mdt-icon">人口类型:</p></div><p class="white flex1 variable" id="partyTypeName"></p></div>
			<div class="mdt-item flex "><div><p class="mdt-icon">涉及人数:</p></div><p class="white flex1 variable" id="partyNum"></p></div>
			<div class="mdt-item flex "><div><p class="mdt-icon">上访类型:</p></div><p class="white flex1 variable" id="petitionTypesName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">信访动态:</p></div><p class="white flex1 variable" id="petitionTrend"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">稳控类型:</p></div><p class="white flex1 variable" id="controlTypeName"></p></div>
			<div class="mdt-item flex "><div><p class="mdt-icon">信访事项:</p></div><p class="white flex1 variable" id="itemRemark"></p></div>
			
			<div class="mdt-item flex"><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">备注:</p>
				</div>
				<p class="white flex1 variable" id="remark"></p>
			</div>
</div>
</@override>

<@override name="entryTypeLeftContentSet">
	$('#partyNum').html(event.partyNum||'');
	$('#partyName_').html(event.partyName_||'');
	$('#certTypeName').html(event.certTypeName||'');
	$('#identityCard').html(event.identityCard||'');
	$('#iResidenceAddr').html(event.iResidenceAddr||'');
	$('#partyTypeName').html(event.partyTypeName||'');
	$('#petitionTypesName').html(event.petitionTypesName||'');
	$('#itemRemark').html(event.itemRemark||'');
	$('#remark').html(event.remark||'');
	if(event.dataSource == '02'){	$('#petitionTrend').html(event.petitionTrend||'').parent().removeClass('hide');}
	if(event.dataSource == '03'){	$('#controlTypeName').html(event.controlTypeName||'').parent().removeClass('hide');}
	
	if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}


$('.mdt-box').getNiceScroll().resize();

</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />