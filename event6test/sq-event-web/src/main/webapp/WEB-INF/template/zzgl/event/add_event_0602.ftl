<table width="100%" cellpadding="0" cellspacing="0" border="0" class="border-t">
	<tr>
		<td class="itemtit">
			检查负责人
		</td>
		<td class="border_b">
			<input type="text" id="checker" name="eventSurvey.checker" editable="false" value="<#if event.eventSurvey.checker??>${event.eventSurvey.checker}</#if>"/>
		</td>
		<td class="itemtit" style="line-height:0px;">
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
			<input type="hidden" id="buildingId" name="eventSurvey.buildingId" editable="false" value="<#if event.buildingId??>${event.buildingId}</#if>"/>
			<input type="text" id="buildingName" name="eventSurvey.buildingName" style="cursor:pointer" editable="false" onclick="showAreaBuildingSelector()" value="<#if event.buildingName??>${event.buildingName}</#if>"/>
		</td>
		<td class="itemtit">
			楼宇地址
		</td>
		<td class="border_b" colspan="3">
			<input type="hidden" id="buildingAddre" name="eventSurvey.buildingAddre" maxlength="64" value="<#if event.eventSurvey.buildingAddre??>${event.eventSurvey.buildingAddre}</#if>"/>
			<label id="buildingAddrel"><#if event.eventSurvey.buildingAddre??>${event.eventSurvey.buildingAddre}</#if></label>
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
			<input type="hidden" id="plaAdd" name="eventSurvey.plaAdd" maxlength="64" value="<#if event.eventSurvey.plaAdd??>${event.eventSurvey.plaAdd}</#if>"/>
			<label id="plaAddl"></label>
			<span style="color:red;">(楼宇地址和场所场所地址必填一个)</span>
		</td>
	</tr>
	<tr>
		<td class="itemtit">
			场所性质
		</td>
		<td class="border_b">
			<input type="hidden" id="plaType" name="eventSurvey.plaType" maxlength="64" value="<#if event.eventSurvey.plaType??>${event.eventSurvey.plaType}</#if>"/>
			<label id="plaTypel"></label>
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
			<input type="text" id="telephone" name="telephone" class="easyui-numberbox" maxlength="12" value="<#if event.telephone??>${event.telephone}</#if>"/>
		</td>
	</tr>
	<tr>
		<td class="itemtit">
			消防隐患情况
		</td>
		<td class="border_b" colspan="5">
			<input type="text" id="surveyDanger" name="eventSurvey.surveyDanger" style="width:80%"/>
		</td>
	</tr>
	<tr>
		<td class="itemtit">
			是否有老弱病残孕
		</td>
		<td  class="border_b" style="line-height:0px;">
			<select id="isElderlyPregnancy" name="eventSurvey.isElderlyPregnancy" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
				<option value="0">无</option>
				<option value="1">有</option>
			</select>
		</td>
		<td class="itemtit">
			老弱病残孕人数
		</td>
		<td class="border_b">
			<input type="text" id="elderlyPregnancyNum" class="easyui-numberbox" name="eventSurvey.elderlyPregnancyNum" maxlength="64" value="<#if event.occurred??>${event.eventSurvey.elderlyPregnancyNum}</#if>" disabled/>
		</td>
		<td class="itemtit">
			是否整改<span style="color:red;">*</span>
		</td>
		<td  class="border_b" style="line-height:0px;">
			<select id="isRefit" name="eventSurvey.isRefit" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
				<option value="0">否</option>
				<option value="1">是</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="itemtit">
			检查情况<span style="color:red;">*</span>
		</td>
		<td class="border_b" colspan="5">
			<textarea id="checkContent" name="eventSurvey.checkContent" cols="70%"></textarea>
			<input type="hidden" name="content" id="content" value="09iu8" />
		</td>
	</tr>
	<tr>
		<td class="itemtit">
			消防设施情况
		</td>
		<td class="border_b" colspan="5">
			<textarea id="plaFacilities" name="eventSurvey.plaFacilities" cols="70%"></textarea>
		</td>
	</tr>
	<tr id="refitSchemeTr" style="display:none">
		<td class="itemtit">
			整改措施
		</td>
		<td class="border_b" colspan="5">
			<textarea id="refitScheme" name="eventSurvey.refitScheme" cols="70%"></textarea>
		</td>
	</tr>
	<tr id="refitResultTr" style="display:none">
		<td class="itemtit">
			整改结果 
		</td>
		<td class="border_b" colspan="5">
			<textarea id="refitResult" name="eventSurvey.refitResult" cols="70%"></textarea>
		</td>
	</tr>
	<tr>
		<td class="itemtit">
			处理结果
		</td>
		<td class="border_b" colspan="5">
			<textarea id="result" name="result" cols="70%"><#if event.result??>${event.result}</#if></textarea>
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
<#include "/component/areaBuildingSelector.ftl"/>
<#include "/component/placeInfoSelector.ftl"/>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
<script type="text/javascript">
	$(function(){
		swfUpload1 = fileUpload({ 
			positionId:'_fileupload',//附件列表DIV的id值',
			type:'add',//add edit detail
			initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${rc.getContextPath()}',
			ajaxData: {'eventSeq':1},//未处理
			file_types:'*.jpg;*.gif;*.png;*.jpeg'
		});
		
		swfUpload2 = fileUpload({
			positionId:'_fileuploaded',//附件列表DIV的id值',
			upload_table:'upload_table1',
			cancel_button:'cancel_button1',		
			type:'add',//add edit detail
			initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${rc.getContextPath()}',
			ajaxData: {'eventSeq':3},//处理后
			file_types:'*.jpg;*.gif;*.png;*.jpeg'
		});
	});
	
function placeInfoSelectorCallback(plaId,plaName,roomAddress,plaType,plaTypeName){
	$('#plaId').val(plaId);
	$('#plaName').val(plaName);
	$('#plaAdd').val(roomAddress);
	$('#plaType').val(plaType);
	$('#plaAddl').html(roomAddress);
	$('#plaTypel').html(plaTypeName);
}

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
</script>