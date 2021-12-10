
<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>

			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">是否损坏:</p></div><p class="white flex1 variable" id="isDamagedStr"></p></div>
			<div class="mdt-item flex "><div><p class="mdt-icon">发现渠道:</p></div><p class="white flex1 variable" id="dataSourceName"></p></div>			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">损坏方式:</p></div><p class="white flex1 variable" id="damageModeName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">发现部门:</p></div><p class="white flex1 variable" id="departmentName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">登记内容:</p></div><p class="white flex1 variable" id="tipoffContent"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">反馈情况:</p></div><p class="white flex1 variable" id="feedbackTypeName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">处置结果:</p></div><p class="white flex1 variable" id="doTypeName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">验收结果:</p></div><p class="white flex1 variable" id="acceptanceResultName"></p></div>
			
			
			<div class="mdt-item flex"><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<!-- 备注id:remark 公共页有赋值 detail_entry_page.ftl -->
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
</div>
</@override>

<@override name="entryTypeLeftContentSet">

$("#dataSourceName").html(event.dataSourceName||'');

if(event.dataSource&&event.dataSource=='01'){setValue(event,'isDamagedStr');}
if(event.dataSource&&event.dataSource=='03'){setValue(event,'departmentName');}
if(event.dataSource&&event.dataSource=='02'){setValue(event,'departmentName');}
if(event.damageMode){setValue(event,'damageModeName');}
if(event.acceptanceResult){setValue(event,'acceptanceResultName');}
if(event.feedbackType){
	setValue(event,'feedbackTypeName');
	if(event.doType){setValue(event,'doTypeName');}
}

if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}


</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />