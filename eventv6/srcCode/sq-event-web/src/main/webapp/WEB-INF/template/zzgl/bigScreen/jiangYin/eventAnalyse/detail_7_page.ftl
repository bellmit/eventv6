<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>
			
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">贫困户姓名:</p>
				</div>
				<p class="white flex1 variable" id="poorVillagerName"></p>
			</div>
			<div class="mdt-item flex"><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
</div>
</@override>
<@override name="entryTypeLeftContentSet">

$('#poorVillagerName').html(event.poorVillagerName);

	if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}
</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />