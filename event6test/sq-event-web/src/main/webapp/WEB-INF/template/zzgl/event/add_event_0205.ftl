<table width="100%" cellpadding="0" cellspacing="0" border="0" class="border-t">
	<tr>
		<td class="itemtit">
			人员姓名<span style="color:red;">*</span>
		</td>
		<td class="border_b">
			<input type="text" id="name" name="visitRecord.name" value="<#if event.name??>${event.name}</#if>"/>
		</td>
		<td class="itemtit">
			性别
		</td>
		<td class="border_b" style="line-height:0px;">
			<select name="visitRecord.gender" id="gender" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
				<option value="M">男</option>
				<option value="F">女</option>
			</select>
		</td>
		<td class="itemtit">
			年龄
		</td>
		<td class="border_b">
			<input type="text" id="age" name="visitRecord.age" class="easyui-numberbox" min="0" max="126" maxlength="3" value="<#if event.age??>${event.age}</#if>"/>
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
		<td class="border_b" style="line-height:0px;">
			<select id="visitForm" name="visitRecord.visitForm" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
				<#if visitFormDC??>
					<#list visitFormDC as l>
						<#if event.visitForm??>
							<option value="${l.COLUMN_VALUE}" <#if (l.COLUMN_VALUE==event.visitForm)>select="selected"</#if>>${l.COLUMN_VALUE_REMARK}</option>
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
		<td  class="border_b" style="line-height:0px;">
			<select id="recentState" name="visitRecord.recentState" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
				<#if recentStatusDC??>
					<#list recentStatusDC as l>
						<#if event.recentState??>
							<option value="${l.COLUMN_VALUE}" <#if (l.COLUMN_VALUE==event.recentState)>select="selected"</#if>>${l.COLUMN_VALUE_REMARK}</option>
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
		<td  class="border_b" style="line-height:0px;">
			<select id="visitedType" name="visitRecord.visitedType" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
				<#if visitedTypeDC??>
					<#list visitedTypeDC as l>
						<#if event.visitedType??>
							<option value="${l.COLUMN_VALUE}" <#if (l.COLUMN_VALUE==event.visitedType)>selected</#if>>${l.COLUMN_VALUE_REMARK}</option>
						<#else>
							<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
						</#if>
					</#list>
				</#if>
			</select>
		</td>
		<td class="itemtit" style="line-height:0px;">
			走访时间<span style="color:red;">*</span>
		</td>
		<td  class="border_b">
			<input type="text" class="easyui-datetimebox easyui-validatebox" editable="false" name="visitRecord.visitTimeStr" id="visitTimeStr" value="<#if event.visitRecord.visitTimeStr??>${event.visitRecord.visitTimeStr}</#if>" />&nbsp;</td>
		</td>
		<td class="itemtit">
			走访效果
		</td>
		<td  class="border_b" style="line-height:0px;">
			<select id="visitEffect" name="visitRecord.visitEffect" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
				<#if visitEffectDC??>
					<#list visitEffectDC as l>
						<#if event.visitEffect??>
							<option value="${l.COLUMN_VALUE}" <#if l.COLUMN_VALUE==event.visitEffect>select="selected"</#if>>${l.COLUMN_VALUE_REMARK}</option>
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
			<input type="text" id="telephone" name="telephone" class="easyui-numberbox" maxlength="12" value="<#if event.telephone??>${event.telephone}</#if>"/> 
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
			<textarea id="visitCause" name="visitRecord.visitCause" cols="25" maxlength="10"><#if event.visitCause??>${event.visitCause}</#if></textarea>
		</td>
		<td class="itemtit">
			交谈内容
		</td>
		<td class="border_b" colspan="3">
			<textarea id="talkContent" name="visitRecord.talkContent" cols="25" maxlength="10"><#if event.talkContent??>${event.talkContent}</#if></textarea>
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
			<textarea id="criminalFacts" name="visitRecord.criminalFacts" cols="25" maxlength="10"><#if event.criminalFacts??>${event.criminalFacts}</#if></textarea>
		</td>
	</tr>
	<td class="itemtit">
		处理结果
	</td>
	<td class="border_b" colspan="5">
		<textarea id="result" name="result" cols="70%" maxlength="10"><#if event.result??>${event.result}</#if></textarea>
	</td>
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
</script>