<table width="100%" cellpadding="0" cellspacing="0" border="0" class="border-t">
	<tr>
		<td class="itemtit">
			案（事）件编号
		</td>
		<td class="border_b">
			<input type="hidden" id="eventId" name="eventId" editable="false" value="<#if event.eventCode??>${event.eventCode?c}</#if>"/>
			<input type="hidden" id="eventCode" name="eventCode" editable="false" style="cursor:pointer" value="<#if event.eventCode??>${event.eventCode}</#if>"/>
		</td>
		<td class="itemtit">
			案（事）件名称<span style="color:red;">*</span>
		</td>
		<td class="border_b">
			<input type="text" id="eventName" name="eventName" editable="false" value="<#if event.eventName??>${event.eventName}</#if>"/>
			<!--<input type="text" id="eventName" name="eventName" maxlength="20" value="<#if event.reporter??>${event.reporter}</#if>"/>-->
		</td>
		<td class="itemtit">
			发生时间<span style="color:red;">*</span>
		</td>
		<td class="border_b" style="line-height:0px;">
			<input type="text" class="easyui-datetimebox easyui-validatebox" editable="false" id="happenTimeStr" name="happenTimeStr" value="<#if event.happenTimeStr??>${event.happenTimeStr}</#if>"/>
		</td>
	</tr>
	<tr>
		<td class="itemtit">
			案（事）件性质<span style="color:red;">*</span>
		</td>
		<td  class="border_b" style="line-height:0px;">
			<select name="eventNature" id="eventNature" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:150">
				<option value="1">治安案件</option>
				<option value="2">刑事案件</option>
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
				<option value="Y">是</option>
				<option value="N">否</option>
			</select>
		</td>
	</tr>
	<tr class="item">
	           <td class="itemtit">
	                          主犯（嫌疑人）<span style="color:red;">*</span>
	           <input type="hidden" name="eventInvolvedPeople" class="eventInvolvedPeople" id="eventInvolvedPeople" value=""/>
	           </td>
	           <td colspan="5" class="border_b"  style="width: auto;">
		           <table id="principal"  width="100%" border="0" cellspacing="1" cellpadding="0" style="background:#BED3DF;">
                       <thead>
                       <tr style="font-weight:bold;font-size:12px; background:url(${rc.getContextPath()}/image/titleBg.png) repeat-x;height:30px; line-height:25px;color:#1885bc; background-color:#d5e2eb;" align="center">
                          <th width="20%">证件类型</td>
		                  <th width="30%">证件号码</td>
		                  <th width="20%">姓名</td>	
		                  <th width="30%">
		                  	 &nbsp;<img border="0" align="absmiddle" class="addPrincipal" src="${rc.getContextPath()}/css/addForCh.gif" title="添加自定义当事人">
                             <!--<input type="button" value="添加主犯" onclick="addPrincipal()" title="添加自定义主犯（嫌疑犯）"/>-->
                          </th>
		               </tr>
                       </thead>
		               <tbody>
		               </tbody>
		           </table>
	           </td>
	       </tr>
	<tr>
		<td class="itemtit">
			在逃人数
		</td>
		<td class="border_b">
			<input type="text" id="fugitiveAmount" name="fugitiveAmount" class="easyui-numberbox" maxlength="11" min="0" value=""/>
		</td>
		<td class="itemtit">
			抓捕人数
		</td>
		<td class="border_b">
			<input type="text" id="arrestedAmount" name="arrestedAmount" class="easyui-numberbox" maxlength="11" min="0" value="">
		</td>
		<td class="itemtit">
			作案人数
		</td>
		<td class="border_b">
			<span class="involvedNum" name="involvedNum"></span>
			<input type="hidden" name="involvedNum" id="involvedNum"/>
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
			<textarea id="detectedDesc" name="detectedDesc" cols="70%" rows=3><#if event.detectedDesc??>${event.detectedDesc}</#if></textarea>
		</td>
	</tr>
</table>

<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
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
	trEl.push('<td style="background:white;" align="center">&nbsp;');         
	trEl.push('<img class="delPrincipal" src="${rc.getContextPath()}/css/del_all.gif" title="删除自定义主犯（嫌疑犯）"/>');
	trEl.push('</td>');
	trEl.push('</tr>');		   
	$("#principal tbody").append(trEl.join(""));
} 
	$(function(){
		$("input[name='fugitiveAmount'],input[name='arrestedAmount'],input[name='involvedNum']").blur(function(){
			var a = $('#arrestedAmount').val();
			var f = $('#fugitiveAmount').val();
			var text = a*1 + f* 1;
			$('.involvedNum').html(text);
			$('#involvedNum').val(text);
		});
	})
$(document).on('blur','.idCard,.eventPeopleName',function(){
	var users = "";
	$("#principal tbody tr").each(function(){
		if (users != "") {
			users += "；";
		}
		users += $(this).find("td:eq(0)").find("select").val()+ "，" + $(this).find("td:eq(2)").find("input").val() + "，" + $(this).find("td:eq(1)").find("input").val();
	});
	$("#eventInvolvedPeople").val(users);
});
</script>
