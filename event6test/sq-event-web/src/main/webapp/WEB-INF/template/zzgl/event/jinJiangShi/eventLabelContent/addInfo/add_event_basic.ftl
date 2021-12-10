<div class="title FontDarkBlue eventBasicInfo" id="eventLabel_eventBasicInfo">基本信息</div>
<div class="has-more">
	<div class="basic-infor">
		<input type="hidden" id="name" name="name" value="${event.eventName!}" />
	
		<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
		<input type="hidden" id="gridCode" name="gridCode" value="${event.gridCode!}">
		<input type="hidden" id="type" name="type" value="${event.type!}" />
		<input type="hidden" id="code" name="code" value="${event.code!}" />
		<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
		<input type="hidden" name="parentEventId" value="<#if parentEventId??>${parentEventId?c}</#if>" />
		<!--办理意见-->
		<input type="hidden" id="result" name="result" value="${event.result!}" />
		
		<!--用于地图-->
		<input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" /> 
		<input type="hidden" id="markerOperation" name="markerOperation" value="${markerOperation!}"/>
		<!--事件上报关联模块-->
		<input type="hidden" name="eventReportRecordInfo.bizId" value="<#if event.eventReportRecordInfo?? && event.eventReportRecordInfo.bizId??>${event.eventReportRecordInfo.bizId?c}</#if>" />
		<input type="hidden" name="eventReportRecordInfo.bizType" value="<#if event.eventReportRecordInfo??>${event.eventReportRecordInfo.bizType!}</#if>" />
		
		<input type="hidden" name="redisKey" value="${redisKey!}" />
		
        <!--晋江涉事人员-->
        <input type="hidden" id="peopleListJson" name="peopleListJson" />
        <input type="hidden" id="isInvolvedPeopleAltered" name="isInvolvedPeopleAltered" value="true"/>
        <input type="hidden" name="committee" id="committee"/>
        <input type="hidden" name="nationality" id="nationality"/>
        <input type="hidden" name="resMarker.x" id="x"/>
        <input type="hidden" name="resMarker.y" id="y"/>
        <input type="hidden" name="resMarker.mapType" id="mapType"/>
        
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="LeftTd leftTdWidth">
					<label class="LabName"><span>事件分类：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="typeName" name="typeName" maxlength="100" value="${event.typeName!event.eventClass!}" />
				</td>
				<td>
					<#if event.code??>
						<label class="LabName"><span>事件编号：</span></label><div class="Check_Radio">${event.code}</div>
					<#else>
						<label class="LabName"><span></span></label>
					</#if>
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span><label class="Asterik">*</label>事件标题：</span></label><input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="eventName" id="eventName" value="${event.eventName!}" />
				</td>
				<td>
					<label class="LabName"><span>影响范围：</span></label>
					<input type="hidden" id="influenceDegree" name="influenceDegree" value="${event.influenceDegree!}" />
					<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="influenceDegreeName" value="${event.influenceDegreeName!}" />
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span><label class="Asterik">*</label>事发时间：</span></label><input type="text" id="happenTimeStr" name="happenTimeStr" class="inp1 Wdate easyui-validatebox" style="cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${event.happenTimeStr!}" readonly="readonly"></input>
				</td>
				<td>
					 <label class="LabName"><span>信息来源：</span></label>
					 <#if isDetail2Edit?? && isDetail2Edit>
					 	<div class="Check_Radio FontDarkBlue">${event.sourceName!}</div>
					 <#else>
					 	<input type="hidden" id="source" name="source" value="${event.source!}" />
					 	<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="sourceName" value="${event.sourceName!}" />
					 </#if>
				</td>
			</tr>
			<tr>
				<td class="LeftTd" colspan="2">
					<label class="LabName"><span><label class="Asterik">*</label>事发详址：</span></label>
					<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" name="occurred" id="occurred" value="${event.occurred!}" />
				</td>
			</tr>
			<tr>
				<td>
					<label class="LabName"><span>涉及人数：</span></label>
					<input  id="involvedNumInt" name="involvedNumInt" style="float: left;" class="inp1 easyui-validatebox" data-options="validType:'numLength[3]',required:true,tipPosition:'bottom'" value="${event.involvedNumInt!'0'}" />
					<label><span style="float: left;margin-top: 4px;color: #7c7c7c;">（人）</span></label>
				</td>
				<td>
					<label class="LabName"><span>紧急程度：</span></label>
					<input type="hidden" id="urgencyDegree" name="urgencyDegree" value="${event.urgencyDegree!}" />
					<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="urgencyDegreeName" value="${event.urgencyDegreeName!}" />
				</td>
			</tr>
			<tr>
				<td class="LeftTd" colspan="2">
					<label class="LabName"><span><label class="Asterik">*</label>事件描述：</span></label><textarea name="content" id="content" cols="" rows="" class="area1 autoCellWidth easyui-validatebox" style="height:108px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']" >${event.content!}</textarea>
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span>联系人员：</span></label><input  id="contactUser" name="contactUser" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="${event.contactUser!}" />
				</td>
				<td>
					<label class="LabName"><span>联系电话：</span></label><input name="tel" id="tel" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:'mobileorphone'" value="${event.tel!}" />
				</td>
			</tr>
		</table>
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr id="adviceTr" class="hide">
				<td class="LeftTd">
					<#if isDetail2Edit?? && isDetail2Edit>
					<#else>
					<label class="LabName"><span>办理意见：</span></label><textarea rows="3" style="height:80px;" id="advice" name="advice" class="area1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']">${advice!}</textarea>
					</#if>
				</td>
			</tr>
			<#--<tr>
				<td class="LeftTd">
					<label class="LabName"><span>涉及单位：</span></label>
					<input type="text" id="involveUnit" name="involveUnit"
						   value="<#if event?? && event.involveUnit??>${event.involveUnit}</#if>"
						   class="inp1 comboselector requestParam" style="width:205px;height:32px;"
						   data-options="dType:'legal',panelWidth:300, editable:true, hasDownArrow: false" query-params="orgCode=<#if orgCode??>${orgCode}</#if>"/>
						<a href="javascript:void(0)" id="newHtml" style="float:inherit;height: 38px;margin: 0 10px;padding: 0px 10px 0 15px;" class="BigNorToolBtn add-addr" onclick="addRecOrg();">
						<i class="icon-add-w"></i></a>
					&lt;#&ndash;<a href="javascript:void(0)" id="newHtml" class="NorToolBtn AddBtn" style="background-color: #fec434;border-radius: 3px;margin-top: 3px;" onclick="addRecOrg();">新增</a>&ndash;&gt;
				</td>
				&lt;#&ndash;<td class="LeftTd">
					<a href="javascript:void(0)" id="newHtml" class="NorToolBtn AddBtn" style="background-color: #fec434;border-radius: 3px;margin-top: 3px;" onclick="addRecOrg();">新增</a>
				</td>&ndash;&gt;
			</tr>-->
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span>涉及人员：</span></label>
					<input type="hidden" name="eventInvolvedPeople" id="eventInvolvedPeople" value="${event.eventInvolvedPeople!}" />
					<input type="hidden" name="involvedPersion" id="involvedPersion" value="${event.involvedPersion!}" />
					<div class="addinfo" style="width:667px;">
						<a href="javascript:void(0)" class="NorToolBtn AddBtn" style="background-color: #fec434;border-radius: 3px;margin-top: 3px;" onclick="addInvoledPeople();">新增</a>
					</div>
				</td>
			</tr>
			<tr>
				<td style="border: none;">
					<table id="involvedPeopleList" style="width: 100%;">
						<tr>
							<td style="width: 74px;"></td>
							<td style="width: 24%;">
								<label class="LabName" style="text-align: left"><span><font color="red">*</font>姓名</span></label>
							</td>
							<td style="width: 23%;">
								<label class="LabName" style="text-align: left"><span><font color="red">*</font>证件类型</span></label>
							</td>
							<td style="width: 24%;">
								<label class="LabName" style="text-align: left"><span><font color="red">*</font>证件号码</span></label>
							</td>
							<td style="width: 24%;">
								<label class="LabName" style="text-align: left"><span>联系方式</span></label>
							</td>
							<#if involvedPeopleList?? && (involvedPeopleList?size >0)>
								<input id="numOfInvolvedPeople" type="hidden" value="${involvedPeopleList?size}"/>
								<#list involvedPeopleList as l >
									<tr>
										<td>
											<a href="javascript:void(0)" class="NorToolBtn DelBtn" style="background-color: #fec434;border-radius: 3px;white-space:nowrap;margin-left: 10px;margin-top: 4px;" onclick="delInvoledPeople(this);">删除</a>
										</td>
										<td style="width: 24%;">
											<input id="involvedPeopleName${l_index}" value="${l.name!''}" class="inp1" style="width: 100px;" onblur="getPerson(${l_index})"/>
										</td>
										<td style="width: 22%;">
											<input id="cardType${l_index}" type="hidden" class="inp1" />
											<input id="cardTypeName${l_index}" class="inp1 easyui-validatebox" style="width: 90px;"/>
										</td>
										<td style="width: 24%;">
											<input id="cardNumber${l_index}" value="${l.idCard!''}" class="inp1 " style="width: 140px;" onblur="getPerson(${l_index})"/>
										</td>
										<td style="width: 21%;">
											<input id="phoneNumber${l_index}" value="${l.tel!''}" class="inp1" style="width: 100px;"/>
										</td>
										<td>
											<input id="ciRsId${l_index}" value="${l.ciRsId!''}" type="hidden"/>
										</td>
									</tr>
								</#list>
							</#if>
						</tr>
					</table>
				</td>
			</tr>
			<#if isDetail2Edit?? && isDetail2Edit>
			<#else>
			<tr>
				<td class="LeftTd">
					<label class="LabName"><span>附件上传：</span></label><div id="bigFileUploadDiv"></div>
				</td>
			</tr>
			</#if>
		</table>
		
	</div>
</div>