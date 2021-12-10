<div class="title FontDarkBlue eventBasicInfo" id="eventBasicInfo">基本信息</div>
<div class="has-more">
	<div class="basic-infor">
		<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
		<input type="hidden" id="toClose" name="toClose" value="0" />
		
		<!--用于地图-->
		<input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" />
		<input type="hidden" id="markerOperation" name="markerOperation" value="${markerOperation!}"/>
        
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="LeftTd leftTdWidth">
					<label class="LabName"><span>事件编号：</span></label><div class="Check_Radio FontDarkBlue">${event.code!}</div>
				</td>
				<td>
					<label class="LabName"><span>事件分类：</span></label><div class="Check_Radio FontDarkBlue">${event.eventClass!}</div>
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span>影响范围：</span></label><div class="Check_Radio FontDarkBlue">${event.influenceDegreeName!}</div>
				</td>
				<td>
					<label class="LabName"><span>事件标题：</span></label><div class="Check_Radio FontDarkBlue" style="width:72%">${event.eventName!}</div>
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span>事发时间：</span></label><div class="Check_Radio FontDarkBlue">${event.happenTimeStr!}</div>
				</td>
				<td>
					<label class="LabName"><span>事发详址：</span></label><div class="Check_Radio FontDarkBlue" style="width:72%">${event.occurred!}</div>
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span>信息来源：</span></label><div class="Check_Radio FontDarkBlue">${event.sourceName!}</div>
				</td>
				<td>
					<label class="LabName"><span>地理位置：</span></label>
					<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
					<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span>涉及人数：</span></label><div class="Check_Radio FontDarkBlue"><#if event.involvedNumInt??>${event.involvedNumInt}<#else >0</#if><label><span>（人）</span></label></div>
				</td>
				<td>
					<label class="LabName"><span>紧急程度：</span></label><div class="Check_Radio FontDarkBlue">${event.urgencyDegreeName!}</div>
				</td>
			</tr>
			<tr>
				<td class="LeftTd" colspan="2">
					<label class="LabName"><span>事件描述：</span></label><div class="Check_Radio FontDarkBlue" style="width:87%">${event.content!}</div>
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span>联系人员：</span></label><div class="Check_Radio FontDarkBlue">${event.contactUser!}</div>
				</td>
				<td>
					<label class="LabName"><span>联系电话：</span></label><div class="Check_Radio FontDarkBlue">${event.tel!}</div>
				</td>
			</tr>
		</table>
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr id="adviceTr" class="hide">
				<td class="LeftTd">
					<label class="LabName"><span>办理意见：</span></label><textarea rows="3" style="height:80px;" id="advice" name="advice" class="area1 autoCellWidth easyui-validatebox fast-reply" data-options="tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']">${advice!}</textarea>
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span>涉及人员：</span></label>
					<div class="Check_Radio FontDarkBlue" style="width: 86%;">
						<#if involvedPeopleList?? && (involvedPeopleList?size >0)>
							<table id="involvedPeopleList" style="width: 100%; margin-top: -4px;">
								<tr>
									<td style="width: 25.5%;padding-top: 0px;">
										<label class="LabName" style="text-align: left"><span>姓名</span></label>
									</td>
									<td style="width: 23%;padding-top: 0px;">
										<label class="LabName" style="text-align: left"><span>证件类型</span></label>
									</td>
									<td style="width: 25%;padding-top: 0px;">
										<label class="LabName" style="text-align: left"><span>证件号码</span></label>
									</td>
									<td style="width: 21%;padding-top: 0px;">
										<label class="LabName" style="text-align: left"><span>联系方式</span></label>
									</td>
								</tr>
								
								<input id="numOfInvolvedPeople" type="hidden" value="${involvedPeopleList?size}"/>
								<#list involvedPeopleList as l >
									<tr>
										<td style="width: 24.5%;"><span>${l.name!''}</span></td>
										<td style="width: 25%;"><span>${l.cardTypeName!''}</span></td>
										<td style="width: 25%;"> <span>${l.idCard!''}</span></td>
										<td style="width: 25%;padding-right: 10px;"><span>${l.tel!''}</span></td>
										<td><input id="ciRsId${l_index}" value="${l.ciRsId!''}" type="hidden"/></td>
									</tr>
								</#list>
							</table>
						<#else>
							（无）
						</#if>
					</div>
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span>附件上传：</span></label><div id="bigFileUploadDiv"></div>
				</td>
			</tr>
		</table>
		
	</div>
	
	<#if bizDetailUrl??>
		<div class="WebNotice">
			<p>此事件关联了业务单信息，<a href="###" onclick="showDetail()">点击查看</a></p>
		</div>
	</#if>
                    
</div>