<div class="clear title EventLabelTitle FontDarkBlue majorRelatedEvents hide" id="majorRelatedEvents">重特大案(事)件</div>
<div class="has-more majorRelatedEvents hide">
	<div class="basic-infor">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="LeftTd leftTdWidth">
					<label class="LabName"><span>案(事)类型：</span></label><div class="Check_Radio FontDarkBlue"><#if majorRelatedEvents??>${majorRelatedEvents.eventTypeCN!}</#if></div>
				</td>
				<td class="LeftTd">
					<label class="LabName"><span>案(事)分级：</span></label><div class="Check_Radio FontDarkBlue"><#if majorRelatedEvents??>${majorRelatedEvents.eventLevelCN!}</#if></div>
				</td>
			</tr>
		</table>
	</div>
</div>