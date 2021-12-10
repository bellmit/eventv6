<div class="clear title FontDarkBlue majorRelatedEvents hide" id="eventLabel_majorRelatedEvents">重特大案(事)件</div>
<div class="has-more majorRelatedEvents hide">
	<div class="basic-infor">
		<input type="hidden" id="isSaveMajorRelatedEvents" name="isSaveMajorRelatedEvents" value="false" />
		<input type="hidden" id="majorRelatedEvents_isDetection" name="majorRelatedEvents.isDetection" value="<#if majorRelatedEvents?? && majorRelatedEvents.isDetection??>${majorRelatedEvents.isDetection}<#else>0</#if>" />
		<input type="hidden" name="majorRelatedEvents.reId" value="<#if majorRelatedEvents?? && majorRelatedEvents.reId??>${majorRelatedEvents.reId?c}</#if>" />
		<input type="hidden" id="majorRelatedEvents_bizType" name="majorRelatedEvents.bizType" value="<#if majorRelatedEvents?? && majorRelatedEvents.bizType??>${majorRelatedEvents.bizType}<#else>3</#if>" /><!--重特大案件bizType写死-->
			
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="LeftTd leftTdWidth">
					<label class="LabName"><span><font color="red">*</font>案(事)类型：</span></label>
					<input type="hidden" id="majorRelatedEvents_eventType" name="majorRelatedEvents.eventType" value="" />
					<input type="text" class="inp1 easyui-validatebox requestParam" id="majorRelatedEvents_eventTypeName" value="" />
				</td>
				<td class="LeftTd">
					<label class="LabName"><span><font color="red">*</font>案(事)分级：</span></label>
					<input id="majorRelatedEvents_eventLevelName" type="text" class="inp1 easyui-validatebox requestParam" value=""/>
					<input type="hidden" id="majorRelatedEvents_eventLevel" name="majorRelatedEvents.eventLevel" value="" />
				</td>
			</tr>
		</table>
	</div>
</div>