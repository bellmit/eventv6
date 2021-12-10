<@override name="extraTr4EventAttrEdit">
	<@super></@super>
	<tr>
		<td>
			<label class="LabName"><span><label class="Asterik">*</label>事件标题：</span></label><input type="text" class="inp1 easyui-validatebox w60" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="eventName" id="eventName" value="${event.eventName!}" />
		</td>
	</tr>
</@override>
