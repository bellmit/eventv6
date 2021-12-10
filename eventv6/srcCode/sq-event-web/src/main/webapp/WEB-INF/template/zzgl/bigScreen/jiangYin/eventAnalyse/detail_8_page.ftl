<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>
			
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">规划许可证编号:</p>
				</div>
				<p class="white flex1 variable" id="rcpCode"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">建房户姓名:</p>
				</div>
				<p class="white flex1 variable" id="householder"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">宅基地批准书编号:</p>
				</div>
				<p class="white flex1 variable" id="rhaCode"></p>
			</div>
			<div class="mdt-item flex"><div><p class="mdt-icon">证件类型:</p></div><p class="white flex1 variable" id="rhCardTypeName"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">证件号码:</p></div><p class="white flex1 variable" id="rhIdCard"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">东权利人:</p>
				</div>
				<p class="white flex1 variable" id="eastNeighbor"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">西权利人:</p>
				</div>
				<p class="white flex1 variable" id="westNeighbor"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">南权利人:</p>
				</div>
				<p class="white flex1 variable" id="southNeighbor"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">北权利人:</p>
				</div>
				<p class="white flex1 variable" id="northNeighbor"></p>
			</div>
</div>
</@override>
<@override name="entryTypeLeftContentSet">

	$('#rcpCode').html(event.rcpCode);
	$('#householder').html(event.householder);
	$('#rhaCode').html(event.rhaCode);
	$('#eastNeighbor').html(event.eastNeighbor||'');
	$('#westNeighbor').html(event.westNeighbor||'');
	$('#southNeighbor').html(event.southNeighbor||'');
	$('#northNeighbor').html(event.northNeighbor||'');
	$('#rhCardTypeName').html(event.rhCardTypeName||'');
	$('#rhIdCard').html(event.rhIdCard||'');
	if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}

</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />