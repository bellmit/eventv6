<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>外平台采集事件</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>

<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>

<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>

<script type="text/javascript">
$(function(){
	fileUpload({
		positionId:'_fileupload',//附件列表DIV的id值',
		type:'edit',//add edit detail
		initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
		context_path:'${rc.getContextPath()}',
		ajaxData: {'bizId':${event.eventId?c},'attachmentType':'${event.type}','eventSeq':1},
		ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml', 	//获取上传附件的URL （新增页面可不写）	
		file_types:'*.jpg;*.gif;*.png;*.jpeg'
	});
	
	fileUpload({
		positionId:'_fileuploaded',//附件列表DIV的id值',
		upload_table:'upload_table1',
		cancel_button:'cancel_button1',		
		type:'edit',//add edit detail
		initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
		context_path:'${rc.getContextPath()}',
		ajaxData: {'bizId':${event.eventId?c},'attachmentType':'${event.type}','eventSeq':3},
		ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml', 	//获取上传附件的URL （新增页面可不写）	
		file_types:'*.jpg;*.gif;*.png;*.jpeg'
	});
});


function tableSubmit(m){
	var gridId = $("#gridId").val();
	if(gridId==null || gridId=="") {
		$.messager.alert('错误','请选择所属网格！','error');
		return false;
	}
	var today=new Date();
	if(typeVal=='0205'){
		var name = $("#name").val();
		if(name==null || name==""){
			$.messager.alert('错误','请输入人员姓名！','error');
			return false;
		}
		var identityCard = $("#identityCard").val();
		if(identityCard==null || identityCard==""){
			$.messager.alert('错误','请输入身份证号！','error');
			return false;
		}
		//var visitTimeStr = $("#visitTimeStr").val();
		var visitTimeStr =  $("#visitTimeStr").datetimebox("getValue");
		if(visitTimeStr==null || visitTimeStr==""){
			$.messager.alert('错误','请选择走访时间！','error');
			return false;
		}
		if(today.getTime()-timeChange("#visitTimeStr")<0){
			//alert("走访时间不能超过当前时间！");
			//return false;
		}
	} else if(typeVal=='0207'){
		//var happenTimeStr = $("#happenTimeStr").val();
		var happenTimeStr =  $("#happenTimeStr").datetimebox("getValue");
		if(happenTimeStr==null || happenTimeStr==""){
			$.messager.alert('错误','请选择发生时间！','error');
			return false;
		}
		if(today.getTime()-timeChange("#happenTimeStr")<0){
			//alert("发生时间不能超过当前时间！");
			//return false;
		}
		var content = $("#content").val();
		if(content==null || content==""){
			$.messager.alert('错误','请输入事件描述！','error');
			return false;
		}else if(checkStrLength(content)>800){
			$.messager.alert('错误','事件描述信息过长！','error');
			return false;
		}
	}else if(typeVal=='0602'){
		//var checkTimeStr = $("#checkTimeStr").val();
		var checkTimeStr = $("#checkTimeStr").datetimebox("getValue");
		if(checkTimeStr==null || checkTimeStr==""){
			$.messager.alert('错误','请选择检查时间！','error');
			return false;
		}
		if(today.getTime()-timeChange("#checkTimeStr")<0){
			//alert("检查时间不能超过当前时间！");
			//return false;
		}
		
		var checkContent = $("#checkContent").val();
  		if(checkContent==null || checkContent==""){
  			$.messager.alert('错误','请输入检查情况!','error');
  			return false;
  		}
		var telephone = $("#telephone").val();
		if((!/^((\(\d{2,3}\))|(\d{3}\-))?((0\d{2,3})|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(telephone))&&telephone!=null && telephone!=""){
	    	if((!/^(13|15|18)\d{9}$/i.test(telephone))&& telephone!=null && telephone!=""){
		     	$.messager.alert('错误',"联系电话格式不对！",'error');
				return false;
			}
		}
		var surveyDanger = $("#surveyDanger").val();
		if(checkStrLength(surveyDanger)>200){
			$.messager.alert('错误','消防隐患情况信息过长！','error');
			return false;
		}
		var checkContent = $("#checkContent").val();
		if(checkStrLength(checkContent)>400){
			$.messager.alert('错误','检查情况信息过长！','error');
			return false;
		}
		var plaFacilities = $("#plaFacilities").val();
		if(checkStrLength(plaFacilities)>200){
			$.messager.alert('错误','消防设施情况信息过长！','error');
			return false;
		}
		var refitScheme = $("#refitScheme").val();
		if(checkStrLength(refitScheme)>400){
			$.messager.alert('错误','整改措施信息过长！','error');
			return false;
		}
		var refitResult = $("#refitResult").val();
		if(checkStrLength(refitResult)>400){
			$.messager.alert('错误','整改结果信息过长！','error');
			return false;
		}
	}else if(typeVal=='0215') {
		var eventName = $('#eventName').val();
		if(eventName == null || eventName =="") {
			$.messager.alert('错误','请输入案件名称！','error');
			return false;
		}
		
		//var happenTimeStr = $("#happenTimeStr").val();
		var happenTimeStr = $("#happenTimeStr").datetimebox("getValue");
		if(happenTimeStr==null || happenTimeStr==""){
			$.messager.alert('错误','请选择发生时间！','error');
			return false;
		}
		
		var length = $("#principal").find("tr").length-1;
		if(length == 0) {
	   	    alert("请添加主犯（嫌疑人）");
	   	    return false;
	   	}else{
	   		var idCardInput = $('.idCard').length;
	   		if(idCardInput>0) {
				var idCard = $(".idCard").val();
				var eventPeopleName = $(".eventPeopleName").val();
				if(idCard==null || idCard==""){
					$.messager.alert('错误','请输入证件号码！','error');
					return false;
				} else if(eventPeopleName==null || eventPeopleName==""){
					$.messager.alert('错误','请输入主犯姓名！','error');
					return false;
				} else {
					users = "";
					$("#principal tbody tr").each(function(){
						if(users != "") {
							users += "；";
						}
						users += $(this).find("td:eq(0)").find("select").val()+ "，" +$(this).find("td:eq(2)").find("input").val() + "，" + $(this).find("td:eq(1)").find("input").val();
					});
					$("#eventInvolvedPeople").val(users);
				}
			}
		}
		var fugitiveAmount = $("#fugitiveAmount").val();
		var arrestedAmount = $("#arrestedAmount").val();
		if(fugitiveAmount!=null && arrestedAmount!=null){
			var sum = fugitiveAmount*1 + arrestedAmount*1;
			if(sum<length){
				$.messager.alert('错误','请检查涉案人数！','error');
				return false;
			}else
				$('#involvedNum').val(sum);
		}
		var content = $("#content").val();
		if(content==null || content==""){
			$.messager.alert('错误','请输入案（事）件描述！','error');
			return false;
		}else if(checkStrLength(content)>800){
			$.messager.alert('错误','案（事）件描述信息过长！','error');
			return false;
		}
		
		var detectedDesc = $("#detectedDesc").val();
		if(detectedDesc==null || detectedDesc==""){
			$.messager.alert('错误','请输入案件侦破情况！','error');
			return false;
		}
		
	} else {
		//var happenTimeStr = $("#happenTimeStr").val();
		var happenTimeStr = $("#happenTimeStr").datetimebox("getValue");
		if(happenTimeStr==null || happenTimeStr==""){
			$.messager.alert('错误','请选择发生时间！','error');
			return false;
		}
		if(today.getTime()-timeChange("#happenTimeStr")<0){
			//alert("发生时间不能超过当前时间！");
			//return false;
		}
		var content = $("#content").val();
		if(content==null || content==""){
			$.messager.alert('错误','请输入事件描述！','error');
			return false;
		}else if(checkStrLength(content)>800){
			$.messager.alert('错误','事件描述信息过长！','error');
			return false;
		}
	}
	
	var result = $("#result").val();
	if(checkStrLength(result)>200){
		$.messager.alert('错误','处理结果信息过长！','error');
		return false;
	}
	$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventDisposalController/"+m+".jhtml");
	modleopen();
	$("#tableForm").submit();
}
function cancel(){
	window.parent.closeMaxJqueryWindow();
}
/*function delImg() {
	$.messager.confirm("提示","是否删除处理前照片？",function(r){
		if(r){
			$.ajax({
				type : "POST",
				url : "${rc.getContextPath()}/zzgl/event/outPlatform/update.jhtml?eventId=" + $("#eventId").val() + "&gridId=" + $("#gridId").val() + "&firstImg=",
				data : null,
				async: true,
				success : function() {
					alert("照片删除成功!");
					$("#firstImgTd").text("无");
					//$.messager.alert("提示", "照片删除成功!", "info");
				},
				error: function(){
					$.messager.alert("提示", "照片删除失败!", "info");
				}
			});
		}
	});
}*/

//区分中英文的字符长度计算
  function checkStrLength(str){
        return   str.match(/[^ -~]/g) == null ? str.length : str.length + str.match(/[^ -~]/g).length ;
  }
</script>
</head>
<body>
	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zzgl/event/outPlatform/update.jhtml" method="post" enctype="multipart/form-data">
		<input type="hidden" id="eventId" name="eventId" value="${event.eventId?c}"/>
		<div style="width:100%; height:100%;overflow: auto;"> 
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="border-t" >
			<tr class="item" colspan="6">
				<td class="itemtit">&nbsp;事件类型</td>
			    <td class="border_b" colspan="3">
			    	<select id="bigType" name="bigType" class="easyui-combobox" editable="false" data-options="panelHeight:200,width:120" >
					    			<option value=""></option>
					    			<#if bigTypeDC??>
						  				<#list bigTypeDC as l>
						  				<!--
					    					<#if orgCode!='350205'>
					    						<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
					    					</#if>
					    				-->
					    					<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
					 					</#list>
					    			</#if>
					    	    </select>
					    	    <span> - </span>
					    	    <select id="type" name="type" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:130" >
					    	    <!--
					    	 		<option value="${event.type}"><#if event.typeName??>${event.typeName}</#if></option>
					    	 	-->
					    	 		<#if smallTypeDC??>
						  				<#list smallTypeDC as l>
						  					<#if event.type == l.COLUMN_VALUE>
						  						<option value="${l.COLUMN_VALUE}" selected="selected">${l.COLUMN_VALUE_REMARK}</option>
						  					<#else>
						  						<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
						  					</#if>
					 					</#list>
					    			</#if>
					    	 		
					</select>
			    </td>
				<td class="itemtit">所属网格<span style="color:red;">*</span></td>
				<td class="border_b" >
				  <input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
				  <input type="text" id="gridName" name="gridName" value="<#if event.gridName??>${event.gridName}</#if>" onclick="showSingleMixedGridSelector();" readonly="readonly" style="cursor:pointer"/>
				  <span style="color:red;">(当前所属网格区域)</span>
				</td>
			</tr>

	<#if event.type=='0205'>
		<tr>
			<td class="itemtit">
				人员姓名<span style="color:red;">*</span>
			</td>
			<td class="border_b">
				<input type="text" id="name" name="visitRecord.name" value="<#if event.visitRecord.name??>${event.visitRecord.name}</#if>"/>
			</td>
			<td class="itemtit">
				性别
			</td>
			<td class="border_b">
				<select name="visitRecord.gender" id="gender" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
				<#if event.visitRecord.gender??>
					<option value="M" <#if ('M'==event.visitRecord.gender)>selected="selected"</#if>>男</option>
					<option value="F" <#if ('F'==event.visitRecord.gender)>selected="selected"</#if>>女</option>
					<option value="X">空</option>
				<#else>
					<option value="M">男</option>
					<option value="F">女</option>
					<option value="X" selected="selected">空</option>
				</#if>
				</select>
			</td>
			<td class="itemtit">
				年龄
			</td>
			<td class="border_b">
				<input type="text" id="age" name="visitRecord.age" class="easyui-numberbox" maxlength="3" value="<#if event.visitRecord.age??>${event.visitRecord.age}</#if>"/>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				身份证号<span style="color:red;">*</span>
			</td>
			<td  class="border_b">
				<input type="text" id="identityCard" name="visitRecord.identityCard" maxlength="20" value="<#if event.visitRecord.identityCard??>${event.visitRecord.identityCard}</#if>"/>
			</td>
			<td class="itemtit">
				走访形式
			</td>
			<td class="border_b">
				<select id="visitForm" name="visitRecord.visitForm" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
					<#if visitFormDC??>
						<#list visitFormDC as l>
							<#if event.visitRecord.visitForm??>
								<option value="${l.COLUMN_VALUE}" <#if (l.COLUMN_VALUE==event.visitRecord.visitForm)>selected="selected"</#if>>${l.COLUMN_VALUE_REMARK}</option>
							<#else>
								<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
							</#if>
						</#list>
					</#if>
				</select>
			</td>
			<td class="itemtit">
				近期动态
			</td>
			<td  class="border_b">
				<select id="recentState" name="visitRecord.recentState" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
					<#if recentStatusDC??>
						<#list recentStatusDC as l>
							<#if event.visitRecord.recentState??>
								<option value="${l.COLUMN_VALUE}" <#if (l.COLUMN_VALUE==event.visitRecord.recentState)>selected="selected"</#if>>${l.COLUMN_VALUE_REMARK}</option>
							<#else>
								<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
							</#if>
						</#list>
					</#if>
				</select>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				走访类型
			</td>
			<td  class="border_b">
				<select id="visitedType" name="visitRecord.visitedType" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
					<#if visitedTypeDC??>
						<#list visitedTypeDC as l>
							<#if event.visitRecord.visitedType??>
								<option value="${l.COLUMN_VALUE}" <#if (l.COLUMN_VALUE==event.visitRecord.visitedType)>selected="selected"</#if>>${l.COLUMN_VALUE_REMARK}</option>
							<#else>
								<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
							</#if>
						</#list>
					</#if>
				</select>
			</td>
			<td class="itemtit">
				走访时间<span style="color:red;">*</span>
			</td>
			<td  class="border_b">
				<input type="text" class="easyui-datetimebox easyui-validatebox" editable="false" name="visitRecord.visitTimeStr" id="visitTimeStr" value="<#if event.visitRecord.visitTimeStr??>${event.visitRecord.visitTimeStr}</#if>"/></td>
			</td>
			<td class="itemtit">
				走访效果
			</td>
			<td  class="border_b">
				<select id="visitEffect" name="visitRecord.visitEffect" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
					<#if visitEffectDC??>
						<#list visitEffectDC as l>
							<#if event.visitRecord.visitEffect??>
								<option value="${l.COLUMN_VALUE}" <#if l.COLUMN_VALUE==event.visitRecord.visitEffect>selected="selected"</#if>>${l.COLUMN_VALUE_REMARK}</option>
							<#else>
								<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
							</#if>
						</#list>
					</#if>	
				</select>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				当事人姓名
			</td>
			<td class="border_b">
				<input type="text" id="contactUser" name="contactUser" maxlength="10" value="<#if event.contactUser??>${event.contactUser}</#if>"/>
			</td>
			<td class="itemtit">
				联系电话
			</td>
			<td class="border_b" colspan="3">
				<input type="text" id="telephone" name="telephone" class="easyui-validatebox  w150" data-options="validType:'mobileorphone'" value="<#if event.telephone??>${event.telephone}</#if>"/> 
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				居住地址
			</td>
			<td class="border_b" colspan="5">
				<textarea id="liveAddress" name="liveAddress" cols="70%" maxlength="10"><#if event.liveAddress??>${event.liveAddress}</#if></textarea>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				走访原因
			</td>
			<td class="border_b">
				<textarea id="visitCause" name="visitRecord.visitCause" cols="25" maxlength="10"><#if event.visitRecord.visitCause??>${event.visitRecord.visitCause}</#if></textarea>
			</td>
			<td class="itemtit">
				交谈内容
			</td>
			<td class="border_b" colspan="3">
				<textarea id="talkContent" name="visitRecord.talkContent" cols="25" maxlength="10"><#if event.visitRecord.talkContent??>${event.visitRecord.talkContent}</#if></textarea>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				采取措施
			</td>
			<td class="border_b">
				<textarea id="measures" name="visitRecord.measures" cols="25" maxlength="10"><#if event.visitRecord.measures??>${event.visitRecord.measures}</#if></textarea>
			</td>
			<td class="itemtit">
				近况简述
			</td>
			<td class="border_b" colspan="3">
				<textarea id="criminalFacts" name="visitRecord.criminalFacts" cols="25" maxlength="10"><#if event.visitRecord.criminalFacts??>${event.visitRecord.criminalFacts}</#if></textarea>
			</td>
		</tr>
	<#elseif event.type=='0602'>
		<tr>
			<td class="itemtit">
				检查负责人
			</td>
			<td class="border_b">
				<input type="text" id="checker" name="eventSurvey.checker" editable="false" value="<#if event.eventSurvey.checker??>${event.eventSurvey.checker}</#if>"/>
			</td>
			<td class="itemtit">
				检查时间<span style="color:red;">*</span>
			</td>
			<td class="border_b" colspan="3">
				<input type="text" class="easyui-datetimebox easyui-validatebox" id="checkTimeStr" name="eventSurvey.checkTimeStr" value="<#if event.eventSurvey.checkTimeStr??>${event.eventSurvey.checkTimeStr}</#if>"/>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				楼宇名称
			</td>
			<td class="border_b">
				<input type="hidden" id="buildingId" name="eventSurvey.buildingId" value="<#if event.eventSurvey.buildingId??>${event.eventSurvey.buildingId}</#if>"/>
				<input type="text" id="buildingName" name="eventSurvey.buildingName" style="cursor:pointer" onclick="showAreaBuildingSelector()" value="<#if event.eventSurvey.buildingName??>${event.eventSurvey.buildingName}</#if>"/>
			</td>
			<td class="itemtit">
				楼宇地址
			</td>
			<td class="border_b" colspan="3">
				<input type="hidden" id="buildingAddre" name="eventSurvey.buildingAddre" style="width:80%" maxlength="50" value="<#if event.eventSurvey.buildingAddre??>${event.eventSurvey.buildingAddre}</#if>"/>
				<label id="buildingAddress" ><#if event.eventSurvey.buildingAddre??>${event.eventSurvey.buildingAddre}</#if></label>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				场所名称
			</td>
			<td class="border_b">
				<input type="hidden" id="plaId" name="eventSurvey.plaId" editable="false" value="<#if event.plaId??>${event.plaId?c}</#if>"/>
				<input type="text" id="plaName" name="eventSurvey.plaName" style="cursor:pointer" editable="false" onclick="showPlaceInfoSelector()" value="<#if event.eventSurvey.plaName??>${event.eventSurvey.plaName}</#if>"/>
			</td>
			<td class="itemtit">
				场所地址
			</td>
			<td class="border_b" colspan="3">
				<input type="hidden" id="plaAdd" name="eventSurvey.plaAdd" style="width:80%" maxlength="64" value="<#if event.eventSurvey.plaAdd??>${event.eventSurvey.plaAdd}</#if>"/>
				<label id="plaAdd1"><#if event.eventSurvey.plaAdd??>${event.eventSurvey.plaAdd}</#if></label>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				场所性质
			</td>
			<td class="border_b">
				<input type="hidden" id="plaType" name="eventSurvey.plaType" maxlength="64" value="<#if event.eventSurvey.plaType??>${event.eventSurvey.plaType}</#if>"/>
				<label id="plaType1"><#if event.eventSurvey.plaTypeLabel??>${event.eventSurvey.plaTypeLabel}</#if></label>
			</td>
			<td class="itemtit">
				联系人姓名
			</td>
			<td class="border_b">
				<input type="text" id="contactUser" name="contactUser" maxlength="10" value="<#if event.contactUser??>${event.contactUser}</#if>"/>
			</td>
			<td class="itemtit">
				联系电话
			</td>
			<td class="border_b">
				<input type="text" id="telephone" name="telephone" class="easyui-validatebox  w150" data-options="validType:'mobileorphone'" value="<#if event.telephone??>${event.telephone}</#if>"/>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				是否有老弱病残孕
			</td>
			<td  class="border_b">
				<select id="isElderlyPregnancy" name="eventSurvey.isElderlyPregnancy" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
					<#if event.eventSurvey.isElderlyPregnancy??>
						<option value="0" <#if event.eventSurvey.isElderlyPregnancy=='0'>selected="selected"</#if>>无</option>
						<option value="1" <#if event.eventSurvey.isElderlyPregnancy=='1'>selected="selected"</#if>>有</option>
					<#else>
						<option value="0" selected="selected">无</option>
						<option value="1">有</option>
					</#if>
				</select>
			</td>
			<td class="itemtit">
				老弱病残孕人数
			</td>
			<td class="border_b">
				<input type="text" id="elderlyPregnancyNum" class="easyui-numberbox" name="eventSurvey.elderlyPregnancyNum" maxlength="3" value="<#if event.eventSurvey.elderlyPregnancyNum??>${event.eventSurvey.elderlyPregnancyNum}</#if>"/>
			</td>
			<td class="itemtit">
				是否整改
			</td>
			<td  class="border_b">
				<select id="isRefit" name="eventSurvey.isRefit" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
					<#if event.eventSurvey.isRefit??>
						<option value="0" <#if event.eventSurvey.isRefit=='0'>selected="selected"</#if>>否</option>
						<option value="1" <#if event.eventSurvey.isRefit=='1'>selected="selected"</#if>>是</option>
					<#else>
						<option value="0" selected="selected">否</option>
						<option value="1">是</option>
					</#if>
				</select>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				检查情况<span style="color:red;">*</span>
			</td>
			<td class="border_b" colspan="5">
				<textarea id="checkContent" name="eventSurvey.checkContent" cols="70%" maxlength="10"><#if event.eventSurvey.checkContent??>${event.eventSurvey.checkContent}</#if></textarea>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				消防设施情况
			</td>
			<td class="border_b" colspan="5">
				<textarea id="plaFacilities" name="eventSurvey.plaFacilities" cols="70%" maxlength="10"><#if event.eventSurvey.plaFacilities??>${event.eventSurvey.plaFacilities}</#if></textarea>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				消防隐患情况
			</td>
			<td class="border_b" colspan="5">
				<textarea id="surveyDanger" name="eventSurvey.surveyDanger" cols="70%" maxlength="10"><#if event.eventSurvey.surveyDanger??>${event.eventSurvey.surveyDanger}</#if></textarea>
			</td>
		</tr>
		<tr id="refitSchemeTr">
			<td class="itemtit">
				整改措施
			</td>
			<td class="border_b" colspan="5">
				<textarea id="refitScheme" name="eventSurvey.refitScheme" cols="70%" maxlength="10"><#if event.eventSurvey.refitScheme??>${event.eventSurvey.refitScheme}</#if></textarea>
			</td>
		</tr>
		<tr id="refitResultTr">
			<td class="itemtit">
				整改结果 
			</td>
			<td class="border_b" colspan="5">
				<textarea id="refitResult" name="eventSurvey.refitResult" cols="70%" maxlength="10"><#if event.eventSurvey.refitResult??>${event.eventSurvey.refitResult}</#if></textarea>
			</td>
		</tr>
	<#elseif event.type=="0215">
		<tr>
			<td class="itemtit">
				案件编号
			</td>
			<td class="border_b">
				<#if event.eventId??>${event.eventId?c}</#if>
			</td>
			<td class="itemtit">
				案件名称<span style="color:red;">*</span>
			</td>
			<td class="border_b">
				<input type="text" id="eventName" name="eventName" value="<#if event.eventName??>${event.eventName}</#if>"/>
			</td>
			<td class="itemtit">
				发生时间<span style="color:red;">*</span>
			</td>
			<td class="border_b">
				<input type="text" class="easyui-datetimebox easyui-validatebox" id="happenTimeStr" name="happenTimeStr" value="<#if event.happenTimeStr??>${event.happenTimeStr}</#if>"/>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				案件性质<span style="color:red;">*</span>
			</td>
			<td  class="border_b" style="line-height:0px;">
				<select name="eventNature" id="eventNature" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
				<#if event.eventNature??>
					<option value="1" <#if event.eventNature=='1'>selected="selected"</#if>>治安事件</option>
					<option value="2" <#if event.eventNature=='2'>selected="selected"</#if>>刑事事件</option>
				<#else>
					<option value="1" selected="selected">治安事件</option>
					<option value="2">刑事事件</option>
				</#if>
				</select>
			</td>
			<td class="itemtit">
				涉及线路
			</td>
			<td class="border_b" style="line-height:0px;">
				<input type="hidden" name="" value=""/>
			</td>
			<td class="itemtit">
				是否破案<span style="color:red;">*</span>
			</td>
			<td class="border_b" style="line-height:0px;">
				<select name="isDetection" id="isDetection" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
					<#if event.incidentInvolved.isDetection??>
						<option value="Y" <#if event.incidentInvolved.isDetection=='Y'>selected="selected"</#if>>是</option>
						<option value="N" <#if event.incidentInvolved.isDetection=='N'>selected="selected"</#if>>否</option>
					<#else>
						<option value="Y">是</option>
						<option value="N" selected="selected">否</option>
					</#if>
				</select>
			</td>
		</tr>
	
		<tr class="item">
		     <td class="itemtit">
		                          主犯（嫌疑人）<span style="color:red;">*</span>
		         <input type="hidden" id="eventInvolvedPeople" name="eventInvolvedPeople"/>
		     </td>
		     <td colspan="5" class="border_b"  style="width: auto;">
		     	<table id="principal"  width="90%" border="0" cellspacing="1" cellpadding="0" style="background:#BED3DF;">
	                <thead>
	                   <tr style="font-weight:bold;font-size:12px; background:url(${rc.getContextPath()}/image/titleBg.png) repeat-x;height:30px; line-height:25px;color:#1885bc; background-color:#d5e2eb;" align="center">
	                       <td width="20%">证件类型</td>
			               <td width="30%">证件号码</td>
			               <td width="20%">姓名</td>	
			               <td width="30%">
			                 <img border="0" align="absmiddle" class="addPrincipal" src="${rc.getContextPath()}/css/addForCh.gif" title="添加自定义当事人">
	                       </td>
			           </tr>
	                </thead>
			        <tbody>
			           <#if involvedPeopleList ??>
			             <#list involvedPeopleList as l>
	                        <tr class="partyUserTr">
	                            <td style="background:white;" align="center">
		                            <select name="cardType" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
		                            	<#if certTypeDC??>
		                            	<#list certTypeDC as c>
											<option value="${c.COLUMN_VALUE}" <#if c.COLUMN_VALUE==l.cardType>selected="selected"</#if> >${c.COLUMN_VALUE_REMARK}</option>
										</#list>
										</#if>
									</select>
								</td>
								<td style="background:white;" align="center"><input type="text" class="idCard" name="idCard" value="<#if l.idCard??>${l.idCard}</#if>" /></td>
								<td style="background:white;" align="center"><input type="text" class="eventPeopleName" name="eventPeopleName" value="<#if l.name??>${l.name}</#if>" /></td>
								<td style="background:white;" align="center"><img class="delPrincipal" src="${rc.getContextPath()}/css/del_all.gif" title="删除主犯（嫌疑犯）"/></td>
	                         </tr>
	                     </#list>
	                   </#if>
			        </tbody>
			    </table>
		    </td>
		</tr>
		
		<tr>
			<td class="itemtit">
				在逃人数
			</td>
			<td class="border_b">
				<input type="text" id="fugitiveAmount" name="fugitiveAmount" class="easyui-numberbox" maxlength="11" min="0" value="<#if event.incidentInvolved.fugitiveAmount??>${event.incidentInvolved.fugitiveAmount?c}</#if>"/>
			</td>
			<td class="itemtit">
				抓捕人数
			</td>
			<td class="border_b">
				<input type="text" id="arrestedAmount" name="arrestedAmount" class="easyui-numberbox" maxlength="11" min="0" value="<#if event.incidentInvolved.arrestedAmount??>${event.incidentInvolved.arrestedAmount?c}</#if>"/>
			</td>
			<td class="itemtit">
				作案人数
			</td>
			<td class="border_b">
				<span class="involvedNum"><#if event.involvedNum??>${event.involvedNum?c}</#if></span>
				<input type="hidden" name="involvedNum" id="involvedNum"/>
				<!--<input type="hidden" id="involvedNum" name="involvedNum" class="easyui-numberbox" maxlength="11" min="0"/>-->
			</td>
		</tr>
			
		<tr>
			<td class="itemtit">
				案（事）件描述<span style="color:red;">*</span>
			</td>
			<td class="border_b" colspan="5">
				<textarea id="content" name="content" cols="70%" row=3><#if event.content??>${event.content}</#if></textarea>
				
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				案件侦破情况<span style="color:red;">*</span>
			</td>
			<td class="border_b" colspan="5">
				<textarea id="detectedDesc" name="detectedDesc" cols="70%" rows=3><#if event.incidentInvolved.detectedDesc??>${event.incidentInvolved.detectedDesc}</#if></textarea>
			</td>
		</tr>
	<#else>
		<tr>
			<td class="itemtit">
				事件名称
			</td>
			<td class="border_b">
				<input type="text" id="eventName" name="eventName" maxlength="100" value="<#if event.eventName??>${event.eventName}</#if>"/>
			</td>
			<td class="itemtit">
				发生时间
			</td>
			<td class="border_b">
				<input type="text" class="easyui-datetimebox easyui-validatebox"  id="happenTimeStr" name="happenTimeStr" value="<#if event.happenTimeStr??>${event.happenTimeStr}</#if>"/>
			</td>
			<td class="itemtit">
				反馈人员
			</td>
			<td class="border_b">
				<input type="text" id="reporter" name="reporter" maxlength="64" value="<#if event.reporter??>${event.reporter}</#if>"/>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				紧急程度
			</td>
			<td  class="border_b">
				<select name="urgencyDegree" id="urgencyDegree" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
					<#if urgencyDegreeDC??>
						<#list urgencyDegreeDC as l>
							<#if event.urgencyDegree??>
								<option value="${l.COLUMN_VALUE}" <#if (l.COLUMN_VALUE==event.urgencyDegree)>selected="selected"</#if>>${l.COLUMN_VALUE_REMARK}</option>
							<#else>
								<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
							</#if>
						</#list>
					</#if>
				</select>
			</td>
			<td class="itemtit">
				影响范围
			</td>
			<td class="border_b">
				<select name="influenceDegree" id="influenceDegree" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
					<#if influenceDegreeDC??>
						<#list influenceDegreeDC as l>
							<#if event.influenceDegree??>
								<option value="${l.COLUMN_VALUE}" <#if (l.COLUMN_VALUE==event.influenceDegree)>selected="selected"</#if>>${l.COLUMN_VALUE_REMARK}</option>
							<#else>
								<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
							</#if>
						</#list>
					</#if>
				</select>
			</td>
			<td class="itemtit">
				信息来源
			</td>
			<td  class="border_b">
				<select name="source" id="source" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
					<#if sourceDC??>
						<#list sourceDC as l>
							<#if event.source??>
								<option value="${l.COLUMN_VALUE}" <#if (l.COLUMN_VALUE==event.source)>selected="selected"</#if>>${l.COLUMN_VALUE_REMARK}</option>
							<#else>
								<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
							</#if>
						</#list>
					</#if>
				</select>
			</td>
		</tr>
		<#if event.type='0207'>
		<tr>
			<td class="itemtit">
				出租人
			</td>
			<td class="border_b">
				<input type="text" id="lessor" name="eventRent.lessor" maxlength="10" value="<#if event.eventRent.lessor??>${event.eventRent.lessor}</#if>"/>
			</td>
			<td class="itemtit">
				承租人
			</td>
			<td class="border_b">
				<input type="text" id="lessee" name="eventRent.lessee" maxlength="10" value="<#if event.eventRent.lessee??>${event.eventRent.lessee}</#if>"/>
			</td>
			<td class="itemtit">
				租赁人数
			</td>
			<td class="border_b">
				<input type="text" id="rentNum" name="eventRent.rentNum" class="easyui-numberbox" maxlength="5" value="<#if event.eventRent.rentNum??>${event.eventRent.rentNum?c}</#if>"/>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				租赁开始时间
			</td>
			<td class="border_b">
				<input type="text" class="easyui-datetimebox easyui-validatebox" id="rentStart" name="eventRent.rentStart" value="<#if event.eventRent.rentStartStr??>${event.eventRent.rentStartStr}</#if>"/>
			</td>
			<td class="itemtit">&nbsp;租赁结束时间</td>
			<td class="border_b">
				<input type="text" class="easyui-datetimebox easyui-validatebox" id="rentEnd" name="eventRent.rentEnd" value="<#if event.eventRent.rentEndStr??>${event.eventRent.rentEndStr}</#if>"/>
			</td>
			<td class="itemtit">
				涉及人数
			</td>
			<td class="border_b">
				<input type="text" id="involvedNum" name="involvedNum" class="easyui-numberbox" maxlength="5" value="<#if event.involvedNum??>${event.involvedNum?c}</#if>"/>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				出租人员名单
			</td>
			<td class="border_b">
				<textarea id="rentalStaff" name="eventRent.rentalStaff" cols="20" rows="2"><#if event.eventRent.rentalStaff??>${event.eventRent.rentalStaff}</#if></textarea>
			</td>
			<td class="itemtit">
				事发详址<span style="color:red;">*</span>
			</td>
			<td class="border_b" colspan="3">
				<textarea id="occurred" name="occurred" cols="40%" rows="2"><#if event.occurred??>${event.occurred}</#if></textarea>
			</td>
		</tr>
		<#else>
		<tr>
			<#if event.type=='0203' && !isLock>
				<td class="itemtit">
					涉及金额(元)
				</td> 
				<td  class="border_b">
					<input type="text" id="involvedMoney " name="involvedMoney" class="easyui-numberbox" data-options="min:0,precision:2,groupSeparator:',',max:999999999" value="${event.involvedMoney?if_exists}"/>
				</td>
			</#if>
			<td class="itemtit">
				涉及人数
			</td>
			<td  class="border_b">
				<input type="text" id="involvedNum" name="involvedNum" class="easyui-numberbox" maxlength="5" value="<#if event.involvedNum??>${event.involvedNum?c}</#if>"/>
			</td>
			<td class="itemtit">
				涉及人员
			</td>
			<td class="border_b" colspan="3" >
				<!-- 涉事人员：以；分隔涉事人员，以，分隔人员的姓名和身份证号 -->
				<input type="hidden" name="eventInvolvedPeople" id="eventInvolvedPeople" value="<#if event.eventInvolvedPeople??>${event.eventInvolvedPeople}</#if>" />
				<label id="eventInvolvedPeopleDiv">
					<#list involvedPeopleList as l>
			    		<#if l.name??>${l.name}</#if><#if l.idCard??>（${l.idCard}）</#if>；
			    	</#list>
				</label>
				<label onclick="showInvoledPeopleSelector();" style="cursor:hand;" onmouseover="this.style.textDecoration='underline'" onmouseout="this.style.textDecoration='none'"><font color="red">添加涉及人员</font></label>
			</td>
		</tr>
		<tr>
			<td class="itemtit">
				联系电话
			</td>
			<td class="border_b">
				<input type="text" id="reporteTel" name="reporteTel" class="easyui-validatebox  w150" data-options="validType:'mobileorphone'"  value="<#if event.reporteTel??>${event.reporteTel}</#if>"/>
			</td>
			<td class="itemtit">
				事发详址
			</td>
			<td class="border_b">
				<input type="text" id="occurred" name="occurred" maxlength="64" value="<#if event.occurred??>${event.occurred}</#if>"/>
			</td>
			<td class="itemtit">
				事发地理坐标
			</td>
			<td class="border_b" colspan="3">
				<div id="gisInfo" style="display:none">
				x:<label id="x"></label><br/>
				y:<label id="y"></label>
				</div>
		    	<input type="hidden" id="hx" name="resMarker.x" value=""/>
		    	<input type="hidden" id="hy" name="resMarker.y" value=""/>
		    	<input type="hidden" id="mapType" name="resMarker.mapType" value=""/>
		    	<input type="button" onclick="initMapMarkerInfoSelector()" value = "添加标注"></input>
			</td>
		</tr>
		</#if>
		<tr>
			<td class="itemtit">
				事件描述<span style="color:red;">*</span>
			</td>
			<td class="border_b" colspan="5">
				<textarea id="content" name="content" cols="70%"><#if event.content??>${event.content}</#if></textarea>
			</td>
		</tr>
	</#if>
	<tr>
		<td class="itemtit">
			处理结果
		</td>
		<td class="border_b" colspan="5">
			<textarea id="result" name="result" cols="70%" ><#if event.result??>${event.result}</#if></textarea>
		</td>
	</tr>
	<tr>
		<td class="border_b" colspan="4">
			事件处理前照片
			<div id="_fileupload"></div>
		</td>
		<td class="border_b" colspan="4">
			事件处理后照片
			<div id="_fileuploaded"></div>
		</td>
	</tr>
</table>
</div>
<div>
	<tr class="item" colspan="6">
	    <td class="itemtit">&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="javascript:tableSubmit('editEvent');">保存</a></td>
	    <td class="itemtit">&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="cancel()">取消</a></td>
	</tr>
</div>
</form>
<#include "/component/singleMixedGridSelectorZtree.ftl"/>
<#include "/component/areaBuildingSelector.ftl"/>
<#include "/component/placeInfoSelector.ftl"/>
<#include "/component/involvedPeopleSelector.ftl">
<#include "/component/popMapMarkerSelector.ftl">
</body>
<#include "/zzgl/event/comboboxSelect.ftl">
<script type="text/javascript">
var typeVal='${event.type}';
var bigTypeBack = "";
var typeBack = "";
//document.all("bigType").selectedIndex=typeVal.substring(0,2);
   $('#bigType').combobox({
    	onChange:function(newValue,oldValue){
			var bigType = newValue;
			if(oldValue!="" && newValue!=bigTypeBack){//为了防止初始化事件大类时，也触发小类查询
				bigTypeBack = oldValue;
				$.messager.confirm("提示","更改事件分类，将清空原先填写的内容，是否继续？",function(r){
					if(r){
						getSmallType(bigType,"update");
					}else{
						$('#bigType').combobox('select',oldValue);
					}
				});
			}
    	}
	});
	
	$('#type').combobox({
    	onChange:function(newValue,oldValue){
    		var newBigType = newValue.substring(0,2);
    		var oldBigType = oldValue.substring(0,2);
    		if(newBigType == oldBigType){
    			if(newValue != typeBack){
    				typeBack = oldValue;
		    		$.messager.confirm("提示","更改事件分类，将清空原先填写的内容，是否继续？",function(r){
		    			if(r){
		    				modleopen();
		    				var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toEditEvent.jhtml?eventId='+${event.eventId?c}+'&type='+newValue;
		    				window.location.href = url;
		    			}else{
		    				$('#type').combobox('select',oldValue);
		    			}
		    		});
	    		}
    		}else{
    			modleopen();
				var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toEditEvent.jhtml?eventId='+${event.eventId?c}+'&type='+newValue;
				window.location.href = url;
    		}
    	}
	});
initXY(${event.gridId?c});
function singleMixedGridSelectCallback(gridId,gridName,orgId,orgCode,gridPhoto) {
	$('#gridId').val(gridId);
	$('#gridName').val(gridName);
	initXY(gridId);
}
function initXY(gridId){
	if(typeVal!='0205'||typeVal!='0207'||typeVal!='0602'){
	$.ajax({
		type: "POST",
		url: '${rc.getContextPath()}/zhsq/map/getGridSubMapType.json',
		data: 'gridId='+gridId,
		dataType:"json",
		success: function(data){
			$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/res/marker/getResMarkerList.json',
				data: 'markerType=0301&resourcesId='+${event.eventId?c}+'&mapType='+data,
				dataType:"json",
				success: function(data){
					if(data==''){
						$("#gisInfo").hide();
						$("#x").text('');
						$("#y").text('');
						$("#hx").val('');
						$("#hy").val('');
						$("#mapType").val('');
					}else{
						$("#gisInfo").show();
						$("#x").text(data[0].x);
						$("#y").text(data[0].y);
						$("#hx").val(data[0].x);
						$("#hy").val(data[0].y);
						$("#mapType").val(data[0].mapType);
					}
				},
				error:function(data){
					$.messager.alert('错误','取得坐标连接错误！','error');
				}
			});
			
		},
		error:function(data){
			$.messager.alert('错误','连接错误！','error');
		}
	});
	}
}

function placeInfoSelectorCallback(plaId,plaName,roomAddress,plaType,plaTypeName){
	$('#plaId').val(plaId);
	$('#plaName').val(plaName);
	$('#plaAdd').val(roomAddress);
	$('#plaType').val(plaType);
	$('#plaAdd1').html(roomAddress);
	$('#plaType1').html(plaTypeName);
}

function areaBuildingSelectorCallback(buildingId,buildingName,buildingAddress){
	$('#buildingId').val(buildingId);
	$('#buildingName').val(buildingName);
	$('#buildingAddre').val(buildingAddress);
	$('#buildingAddress').html(buildingAddress);
}
function involedPeopleCallback(users) {
	var usersDiv = "";
	var userArray = users.split("；");
	$.each(userArray, function(i, n){
		if (i > 0) {
			usersDiv += "；";
		}
		var items = n.split("，");
		usersDiv += items[1] + "（" + items[2] + "）";
	});
	$("#eventInvolvedPeopleDiv").text(usersDiv);//用于显示
	$("#eventInvolvedPeople").val(users);//用于后台保存
}
	function initMapMarkerInfoSelector(){
		var gridId = $("#gridId").val();
		var arr = new Array();
		arr[0] = new Array();
		arr[0][0] = "2";
		arr[0][1] = $("#hx").val();
		arr[0][2] = $("#hy").val();
		showMapMarkerInfoSelector(gridId, arr);
	}

	function mapMarkerSelectorCallback(mapt, x, y){
		 $("#mapType").val(mapt);
         $("#hx").val(x);
         $("#hy").val(y);
         $("#x").text(x);
		$("#y").text(y);
	}
function timeChange(str){
	var str =$(str).datetimebox("getValue");
	str=str.toString();
    str =  str.replace(/-/g,"/");
    var oDate1 = new Date(str);
    return oDate1.getTime();
}
</script>
<script type="text/javascript">

$('#isElderlyPregnancy').combobox({//无老弱病残孕时，老弱病残孕人数不可填写，并将原有值清零
	onChange:function(newValue,oldValue){
		if(newValue == "0"){
			$("#elderlyPregnancyNum").attr("disabled",true);
			$("#elderlyPregnancyNum").val("0");
		}else{
			$("#elderlyPregnancyNum").attr("disabled",false);
		}
	}
});

$('#isRefit').combobox({//不需整改时，隐藏整改措施和整改结果，并清除原有填写内容
	onChange:function(newValue,oldValue){
		if(newValue == "0"){
			$("#refitSchemeTr").attr("style","display:none");
			$("#refitResultTr").attr("style","display:none");
			$("#refitScheme").val("");
			$("#refitResult").val("");
		}else{
			$("#refitSchemeTr").attr("style","display:");
			$("#refitResultTr").attr("style","display:");
		}
	}
});


$(function (){
	modleclose();
	var type = "${event.type}";
	$('#bigType').combobox('select',type.substring(0,2));
	
	<#if event.type=='0602'>
		if(type == '0602'){//消防事件 初始化处理
			var isElderlyPregnancy = "0";
			<#if event.eventSurvey.isElderlyPregnancy??>
				isElderlyPregnancy = "${event.eventSurvey.isElderlyPregnancy}";
			</#if>
			var isRefit = "0";
			<#if event.eventSurvey.isRefit??>
				isRefit = "${event.eventSurvey.isRefit}";
			</#if>
			if(isElderlyPregnancy == "0"){
				$("#elderlyPregnancyNum").attr("disabled",true);
			}else{
				$("#elderlyPregnancyNum").attr("disabled",false);
			}
			
			if(isRefit == "0"){
				$("#refitSchemeTr").attr("style","display:none");
				$("#refitResultTr").attr("style","display:none");
			}else{
				$("#refitSchemeTr").attr("style","display:");
				$("#refitResultTr").attr("style","display:");
			}
		}
	<#elseif event.type=='0215'>
		$(document).on('blur','#involvedNum,#fugitiveAmount,#arrestedAmount',function(){
			var a = $('#arrestedAmount').val();
			var f = $('#fugitiveAmount').val();
			var text = a*1 + f* 1;
			$('.involvedNum').html(text);
			$('#involvedNum').val(text);
		});
	</#if>
});

</script>
<script type="text/javascript">
//添加自定义主犯
$(".addPrincipal").click(function(){
	addPrincipal();
	return true;
});
//删除自定义主犯
$(document).on('click', '.delPrincipal', function(event){
    $(this).parent().parent().remove();
});

function addPrincipal(){
	var trEl = [];
	trEl.push('<tr class="addPrincipalTr">');
	trEl.push('<td style="background:white;" align="center">');
	trEl.push('<select name="cardType" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150"><#if certTypeDC??><#list certTypeDC as l>');
	trEl.push('<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option></#list></#if></select></td>');
	trEl.push('<td style="background:white;" align="center"><input type="text" class="idCard" name="idCard" /></td>');
	trEl.push('<td style="background:white;" align="center"><input type="text" class="eventPeopleName" name="eventPeopleName" /></td>');
	trEl.push('<td style="background:white;" align="center">');         
	trEl.push('<img class="delPrincipal" src="${rc.getContextPath()}/css/del_all.gif" title="删除自定义主犯（嫌疑犯）"/>');
	trEl.push('</td>');
	trEl.push('</tr>');		   
	$("#principal tbody").append(trEl.join(""));
}
</script>
</html>