
<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>

			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">接报方式:</p></div><p class="white flex1 variable" id="collectSourceName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">核查时限:</p></div><p class="white flex1 variable" id="verifyTimeStr"></p></div>
			
			<div class="mdt-item flex"><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
</div>
</@override>

<@override name="entryTypeLeftContentSet">

if(event.dataSource&&event.dataSource=='05'){
		setValue(event,'collectSourceName');
		setValue(event,'verifyTimeStr');
	}

if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}


</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />