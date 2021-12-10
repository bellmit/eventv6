
<@override name="entryTypePageLeft">
		<div class="md-text md-text1 mcbl-bg bs"  id="reportDiv">
			<div class="angle top-left"></div>
			<div class="angle top-right"></div>
			<div class="angle bottom-left"></div>
			<div class="angle bottom-right"></div>
			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">报告人电话:</p></div><p class="white flex1 variable" id='userTel'></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon"  id='doOrgNameStr'>部门名称:</p></div><p class="white flex1 variable" id='doOrgName'></p></div>
			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">企业名称:</p></div><p class="white flex1 variable" id='enterpriseName'></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">主营业务:</p></div><p class="white flex1 variable" id='mainBusiness'></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">办理业务:</p></div><p class="white flex1 variable" id='doBusiness'></p></div>
			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">反映对象:</p></div><p class="white flex1 variable" id='reportObjStr'></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">群众姓名:</p></div><p class="white flex1 variable" id='massesName'></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">反映途径:</p></div><p class="white flex1 variable" id='reflectWay'></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">开展业务:</p></div><p class="white flex1 variable" id='openWork'></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">注册地:</p></div><p class="white flex1 variable" id='statePlace'></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">开展活动:</p></div><p class="white flex1 variable" id='openMove'></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">处置时限:</p></div><p class="white flex1 variable" id="DUEDATESTR_"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">问题描述:</p></div><p class="white flex1 variable" id="tipoffContent"></p></div>
			<div class="mdt-item flex"><div><p class="mdt-icon">备注:</p></div><p class="white flex1 variable" id="remark"></p></div>
			
			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">营商问题:</p></div><p class="white flex1 variable" id="isProblemStr"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">反馈时限:</p></div><p class="white flex1 variable" id="feedbackTimeStr"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">责任部门:</p></div><p class="white flex1 variable" id="dutyOrgName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">牵头部门:</p></div><p class="white flex1 variable" id="leadOrgName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">反馈类型:</p></div><p class="white flex1 variable" id="feedbackTypeStr"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">处置情况:</p></div><p class="white flex1 variable" id="doConditionName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">处置部门:</p></div><p class="white flex1 variable" id="doDepartment"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">已开展工作:</p></div><p class="white flex1 variable" id="doWorkMessage"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">情况说明:</p></div><p class="white flex1 variable" id="situationMeaasge"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">是否延期:</p></div><p class="white flex1 variable" id="isDelayStr"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">理由:</p></div><p class="white flex1 variable" id="disagreeRemark"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">部门范围:</p></div><p class="white flex1 variable" id="isOrgScopeStr"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">受理日期:</p></div><p class="white flex1 variable" id="doDateStr"></p></div>
			
			<div class="mdt-item flex hide"><div><p class="mdt-icon">负责人:</p></div><p class="white flex1 variable" id="dutyName"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">联系方式:</p></div><p class="white flex1 variable" id="dutyTel"></p></div>
			<div class="mdt-item flex hide"><div><p class="mdt-icon">三定方案:</p></div><p class="white flex1 variable" id="dutyRemark"></p></div>
		</div>
</@override>
<@override name="entryTypeLeftContentSet"> 
	 if(event.userTel){setValue(event,'userTel');}
	
	if(  event.dataSource=='02'){
		if(  event.doOrgName){setValue(event,'doOrgName');}
		if(  event.doBusiness){	setValue(event,'doBusiness');}
	}
	
	if(  event.dataSource=='03'||event.dataSource=='04'){
		if(  event.enterpriseName){	setValue(event,'enterpriseName');}
		if(  event.mainBusiness){	setValue(event,'mainBusiness');}
		if(  event.doOrgName){$("#doOrgNameStr").html("办理部门:");setValue(event,'doOrgName');}
		if(  event.doBusiness){	setValue(event,'doBusiness');}
	}
	
	if(  event.dataSource=='01'||event.dataSource=='05'){
		setValue(event,'reportObjStr');
		if(  event.massesName){	setValue(event,'massesName');}
		if(  event.enterpriseName){	setValue(event,'enterpriseName');}
		if(  event.reflectWay){	setValue(event,'reflectWay');}
		if(  event.openWork){	setValue(event,'openWork');}
		if(  event.doOrgName){	setValue(event,'doOrgName');}
	}
	if(  event.dataSource=='06'){
		if(  event.enterpriseName){	setValue(event,'enterpriseName');}
	}

	if(  event.dataSource=='07'){
		setValue(event,'enterpriseName');
		setValue(event,'statePlace');
		setValue(event,'openMove');
	}
	$("#tipoffContent").html(event.tipoffContent);
	if(event.reportStatus  && event.reportStatus=='60'){	$('#DUEDATESTR_').parent().addClass('hide');}else{$('#DUEDATESTR_').html(event.DUEDATESTR_||'');}
	
	if(event.isProblem){
		setValue(event,'isProblemStr');
		if(event.feedbackTime){setValue(event,'feedbackTimeStr');}
	}
	
	 if(event.dutyOrgName){setValue(event,'dutyOrgName');}	 
	 if(event.leadOrgName){setValue(event,'leadOrgName');}
	 
	 if(event.feedbackType){
		setValue(event,'feedbackTypeStr');
		if(event.doCondition){setValue(event,'doConditionName');}
	}
	 if(event.doDepartment){setValue(event,'doDepartment');}
	 if(event.doWorkMessage){setValue(event,'doWorkMessage');}
	 if(event.situationMeaasge){setValue(event,'situationMeaasge');}
	 if(event.isDelay){setValue(event,'isDelayStr');}
	 if(event.disagreeRemark){setValue(event,'disagreeRemark');}
	 if(event.isOrgScope){
		setValue(event,'isOrgScopeStr');
		if(event.doDate){setValue(event,'doDateStr');}
	}
	if(event.dutyName){
		setValue(event,'dutyName');
		if(event.dutyTel){setValue(event,'dutyTel');}
	}
	 if(event.dutyRemark){setValue(event,'dutyRemark');}
</@override>

<@extends name="/zzgl/bigScreen/jiangYin/eventAnalyse/detail_entry_page.ftl" />