<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>
			
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">业主姓名:</p>
				</div>
				<p class="white flex1 variable" id="personInvolved"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">占地面积:</p>
				</div>
				<p class="white flex1 variable" id="areaCovered"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">办理用地和规划手续:</p>
				</div>
				<p class="white flex1 variable" id="isRoutine"></p>
			</div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">土地证号:</p></div><p class="white flex1 variable" id="lecCode"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">规划许可证号:</p></div><p class="white flex1 variable" id="ppnCode"></p></div>
			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">图斑编号:</p></div><p class="white flex1 variable" id="mapNum"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">耕地面积:</p></div><p class="white flex1 variable" id="cultivableLandArea"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">基本农田面积:</p></div><p class="white flex1 variable" id="farmlandArea"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">林地面积:</p></div><p class="white flex1 variable" id="woodlandArea"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">允许建设区面积:</p></div><p class="white flex1 variable" id="constructableArea"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">公益林面积:</p></div><p class="white flex1 variable" id="publicForestArea"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">条件建设区面积:</p></div><p class="white flex1 variable" id="conditionalConstructionArea"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">城乡规划面积:</p></div><p class="white flex1 variable" id="urpArea"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">限制建设区面积:</p></div><p class="white flex1 variable" id="restrictedConstructionArea"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">村庄规划面积:</p></div><p class="white flex1 variable" id="villagePlanningArea"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">生态红线面积:</p></div><p class="white flex1 variable" id=""></p></div>
			<div class="mdt-item flex "><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">建筑状态:</p>
				</div>
				<p class="white flex1 variable" id="constructionStatusName"></p>
			</div>
			<div class="mdt-item flex">
				<div>
					<p class="mdt-icon">建（构）筑物用途:</p>
				</div>
				<p class="white flex1 variable" id="buildingUsageName"></p>
			</div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">举报内容:</p></div><p class="white flex1 variable" id="tipoffContent"></p></div>
			<div class="mdt-item flex "><div><p class="mdt-icon">举报内容:</p></div><p class="white flex1 variable" id="remark"></p></div>
		</div>
</@override>
<@override name="entryTypeLeftContentSet">
	$('#personInvolved').html(event.personInvolved);
	$('#areaCovered').html(event.areaCovered?event.areaCovered+"(平方米)":"");
	$('#isRoutine').html(event.isRoutine=='0'?'否':'是');
	$('#constructionStatusName').html(event.constructionStatusName);
	$('#buildingUsageName').html(event.buildingUsageName);
	if(event.isRoutine &&event.isRoutine=='1'){
		setValue(event,'lecCode');
		setValue(event,'ppnCode');
	}
	if(event.dataSource == '03'){	
		setValue(event,'mapNum');
		setValue(event,'cultivableLandArea',"(平方米)");
		setValue(event,'farmlandArea',"(平方米)");
		setValue(event,'woodlandArea',"(平方米)");
		setValue(event,'constructableArea',"(平方米)");
		setValue(event,'publicForestArea',"(平方米)");
		setValue(event,'conditionalConstructionArea',"(平方米)");
		setValue(event,'urpArea',"(平方米)");
		setValue(event,'restrictedConstructionArea',"(平方米)");
		setValue(event,'villagePlanningArea',"(平方米)");
		setValue(event,'ecologicalRedlineArea',"(平方米)");
	}
	if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}
	if(  event.dataSource=='05'){ $('#tipoffContent').html(event.tipoffContent||'');}
	
	
</@override>


<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />