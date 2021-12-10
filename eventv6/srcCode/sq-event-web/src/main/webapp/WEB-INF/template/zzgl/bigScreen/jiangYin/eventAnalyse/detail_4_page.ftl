 
<@override name="entryTypePageLeft"> 
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">人员姓名:</p>
				</div>
				<p class="white flex1 variable" id="kpcName"></p>
			</div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">病人情况:</p></div><p class="white flex1 variable" id="kpcSituationName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">医疗机构名称:</p></div><p class="white flex1 variable" id="tipoffContent"></p></div>
			
			
			<div class="mdt-item flex hide">
				<div>
					<p class="mdt-icon">管控类型:</p>
				</div>
				<p class="white flex1 variable" id="kpcTypeName"></p>
			</div>
			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">数据来源:</p></div><p class="white flex1 variable" id="dataSourceName"></p></div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">证件类型:</p>
				</div>
				<p class="white flex1 variable" id="kpcCardTypeName"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">证件号码:</p>
				</div>
				<p class="white flex1 variable" id="kpcIdCard"></p>
			</div>
			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">来自何国:</p></div><p class="white flex1 variable" id="kpcOriginName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">年龄:</p></div><p class="white flex1 variable" id="kpcAge"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">入南目的:</p></div><p class="white flex1 variable" id="kpcGoalName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">入南时间:</p></div><p class="white flex1 variable" id="arriveTimeStr"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">交通方式:</p></div><p class="white flex1 variable" id="trafficModeName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">交通班次:</p></div><p class="white flex1 variable" id="trafficFrequency"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">随行人员:</p></div><p class="white flex1 variable" id="kpcRetinue"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
</div>
</@override>
 

<@override name="entryTypeLeftContentSet">
	if(event.dataSource == '05'){	
		setValue(event,'kpcSituationName');
		setValue(event,'tipoffContent');
	}else{
		setValue(event,'kpcTypeName');
		if (event.collectSource  && (event.collectSource == '02' || event.collectSource == '03' || event.collectSource == '04')){
			setValue(event,'dataSourceName');
			setValue(event,'trafficModeName');
			setValue(event,'kpcGoalName');
			setValue(event,'trafficFrequency');
			setValue(event,'kpcRetinue');
			setValue(event,'kpcOriginName');
			setValue(event,'kpcAge','(周岁)');
		}
	}

	$('#kpcName').html(event.kpcName);
	$('#kpcCardTypeName').html(event.kpcCardTypeName);
	$('#kpcIdCard').html(event.kpcIdCard);
	
	if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}

</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />