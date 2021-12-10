<table width="100%" cellpadding="0" cellspacing="0" border="0" class="border-t">
	<tr>
		<td class="itemtit">
			事件名称
		</td>
		<td class="border_b">
			<input type="text" id="eventName" name="eventName" maxlength="100" value="<#if event.eventName??>${event.eventName}</#if>"/>
		</td>
		<td class="itemtit">
			反馈人员
		</td>
		<td class="border_b">
			<input type="text" id="reporter" name="reporter" maxlength="64" value="<#if event.reporter??>${event.reporter}</#if>"/>
		</td>
		<td class="itemtit">
			联系电话
		</td>
		<td class="border_b">
			<input type="text" id="reporteTel" name="reporteTel" class="easyui-numberbox" maxlength="12" value=""/>
		</td>
	</tr>
	<tr>
		<td class="itemtit">
			紧急程度
		</td>
		<td  class="border_b" style="line-height:0px;">
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
		<td class="border_b" style="line-height:0px;">
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
		<td  class="border_b" style="line-height:0px;">
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
			<input type="text" id="rentNum" name="eventRent.rentNum" class="easyui-numberbox" maxlength="3" value="<#if event.eventRent.rentNum??>${event.eventRent.rentNum?c}</#if>"/>
		</td>
	</tr>
	<tr>
		<td class="itemtit" style="line-height:0px;">
			租赁开始时间
		</td>
		<td class="border_b">
			<input type="text" class="easyui-datetimebox easyui-validatebox" id="rentStart" name="eventRent.rentStart" value="<#if event.eventRent.rentStartStr??>${event.eventRent.rentStartStr}</#if>"/>
		</td>
		<td class="itemtit">&nbsp;租赁结束时间</td>
		<td class="border_b" style="line-height:0px;">
			<input type="text" class="easyui-datetimebox easyui-validatebox" id="rentEnd" name="eventRent.rentEnd" value="<#if event.eventRent.rentEndStr??>${event.eventRent.rentEndStr}</#if>"/>
		</td>
		<td class="itemtit">
			涉及人数
		</td>
		<td class="border_b">
			<input type="text" id="involvedNum" name="involvedNum" maxlength="10" value="<#if event.eventRent.lessee??>${event.eventRent.lessee}</#if>"/>
		</td>
	</tr>
	<tr>
		<td class="itemtit" style="line-height:0px;">
			发生时间<span style="color:red;">*</span>
		</td>
		<td class="border_b">
			<input type="text" class="easyui-datetimebox easyui-validatebox" id="happenTimeStr" name="happenTimeStr" value="<#if event.happenTimeStr??>${event.happenTimeStr}</#if>"/>
		</td>
		<td class="itemtit">
			出租人员名单
		</td>
		<td class="border_b">
			<textarea id="rentalStaff" name="eventRent.rentalStaff" cols="20" rows="2"><#if event.content??>${event.content}</#if></textarea>
		</td>
		<td class="itemtit">
			事发详址<span style="color:red;">*</span>
		</td>
		<td class="border_b">
			<textarea id="occurred" name="occurred" cols="20" rows="2"><#if event.occurred??>${event.occurred}</#if></textarea>
		</td>
	</tr>
	<tr>
		<td class="itemtit">
			事件描述<span style="color:red;">*</span>
		</td>
		<td class="border_b" colspan="5">
			<textarea id="content" name="content" cols="70%" ><#if event.content??>${event.content}</#if></textarea>
		</td>
	</tr>
	<tr>
		<td class="itemtit">
			处理结果
		</td>
		<td class="border_b" colspan="5">
			<textarea id="result" name="result" cols="70%" maxlength="10"><#if event.result??>${event.result}</#if></textarea>
		</td>
	</tr>
	<tr>
	<!--
		<td class="itemtit">
			事件处理前照片
		</td>
		<td class="border_b" colspan="5">
			<input type="file" name="firstImgFile" id="firstImgFile" onchange="checkFile(this, 'gif,jpg,jpeg,png', '请选择类型为：gif,jpg,jpeg,png 的图片文件！')" />
		</td>
	-->
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
	
function areaBuildingSelectorCallback(buildingId,buildingName){
	$('#buildingId').val(buildingId);
	$('#buildingName').val(buildingName);
}
</script>