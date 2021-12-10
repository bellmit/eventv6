<div class="clear title FontDarkBlue schoolRelatedEvents hide" id="eventLabel_schoolRelatedEvents">涉及师生(事)件</div>
<div class="has-more schoolRelatedEvents hide">
	<div class="basic-infor">
		<input type="hidden" id="isSaveSchoolRelatedEvents" name="isSaveSchoolRelatedEvents" value="false" />
		<input type="hidden" id="schoolRelatedEvents_reId" name="schoolRelatedEvents.reId" value="<#if schoolRelatedEvents?? && schoolRelatedEvents.reId??>${schoolRelatedEvents.reId?c}</#if>" />
		<input type="hidden" id="schoolRelatedEvents_bizType" name="schoolRelatedEvents.bizType" value="<#if schoolRelatedEvents?? && schoolRelatedEvents.bizType??>${schoolRelatedEvents.bizType}<#else>2</#if>" />
		
		<input type="hidden" id="schoolRelatedEvents_bizId" name="schoolRelatedEvents.bizId" value="<#if schoolRelatedEvents?? && schoolRelatedEvents.bizId??>${schoolRelatedEvents.bizId?c}</#if>" />
		<input type="hidden" id="schoolRelatedEvents_bizName" value="" />

		<!-- 涉及人员 -->
		<input type="hidden" id="schoolRelatedEvents_person" name="schoolRelatedEvents.personJson" />
			
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="LeftTd" colspan="2">
					<label class="LabName"><span><font color="red">*</font>案件性质：</span></label>
					<input type="hidden" id="schoolRelatedEvents_nature" name="schoolRelatedEvents.nature" value="" />
					<input type="text" class="inp1 easyui-validatebox requestParam" style="width: 200px;" id="schoolRelatedEvents_natureName" value="" />
				</td>
			</tr>
			<tr>
				<td class="LeftTd" colspan="2">
					<label class="LabName"><span><font color="red">*</font>侦破情况：</span></label><textarea name="schoolRelatedEvents.detectedOverview" id="schoolRelatedEvents_detectedOverview" cols="" rows="" class="area1 autoCellWidth easyui-validatebox requestParam" data-options="validType:['maxLength[1024]','characterCheck']" style="height:64px;resize:none;"><#if schoolRelatedEvents?? && schoolRelatedEvents.detectedOverview??>${schoolRelatedEvents.detectedOverview}</#if></textarea>
				</td>
			</tr>
			<tr>
				<td class="LeftTd" <#if !(schoolRelatedEvents??)>colspan="2"</#if> >
				    <label class="LabName"><span><font color="red">*</font>是否破案：</span></label>
					<div class="zz-form" zz-form-filter="schoolRelatedEventsIsDetection">
						<input type="checkbox" zz-form-filter="_schoolRelatedEventsIsDetection" name="schoolRelatedEvents.isDetection" id="schoolRelatedEvents_isDetection" value="<#if schoolRelatedEvents?? && schoolRelatedEvents.isDetection??>${schoolRelatedEvents.isDetection}<#else>0</#if>" <#if (schoolRelatedEvents?? && schoolRelatedEvents.isDetection?? && schoolRelatedEvents.isDetection=='1')>checked</#if>>
				    <#--<input type="hidden" id="schoolRelatedEvents_isDetection"  name="schoolRelatedEvents.isDetection"  value="<#if schoolRelatedEvents?? && schoolRelatedEvents.isDetection??>${schoolRelatedEvents.isDetection}<#else>0</#if>">
                    <input class="toggle" style="cursor: pointer;outline:none;" type="checkbox"  id="schoolRelatedEvents_isDetectionCheck" onclick="changeDetectionValue('schoolRelatedEvents');" <#if (schoolRelatedEvents?? && schoolRelatedEvents.isDetection?? && schoolRelatedEvents.isDetection=='1')>checked</#if> />-->
					</div>
				</td>
				    <#if schoolRelatedEvents?? && schoolRelatedEvents.reNo??>
				    <td>
						<label class="LabName"><span>案件编号：</span></label><div class="Check_Radio FontDarkBlue">${schoolRelatedEvents.reNo}</div>
					</td>
				    </#if>
			</tr>
			
			<tr>
				<td class="LeftTd leftTdWidth">
				    <label class="LabName"><span>在逃人数：</span></label><input type="text" class="inp1 easyui-numberspinner requestParam" data-options="min:0,validType:'maxLength[5]',onSpinUp:function(){countCrimeNum('schoolRelatedEvents');},onSpinDown:function(){countCrimeNum('schoolRelatedEvents');},onChange:function(){countCrimeNum('schoolRelatedEvents');}" name="schoolRelatedEvents.ecapeNum" id="schoolRelatedEvents_ecapeNum" value="<#if schoolRelatedEvents?? && schoolRelatedEvents.ecapeNum??>${schoolRelatedEvents.ecapeNum?c}<#else>0</#if>" onblur="countCrimeNum('schoolRelatedEvents')" style="width:114px; height:28px;" />
				</td>
				<td>
				    <label class="LabName"><span>抓捕人数：</span></label><input type="text" class="inp1 easyui-numberspinner requestParam" data-options="min:0,validType:'maxLength[5]',onSpinUp:function(){countCrimeNum('schoolRelatedEvents');},onSpinDown:function(){countCrimeNum('schoolRelatedEvents');},onChange:function(){countCrimeNum('schoolRelatedEvents');}" name="schoolRelatedEvents.arrestedNum" id="schoolRelatedEvents_arrestedNum" value="<#if schoolRelatedEvents?? && schoolRelatedEvents.arrestedNum??>${schoolRelatedEvents.arrestedNum?c}<#else>0</#if>" onblur="countCrimeNum('schoolRelatedEvents')" style="width:114px; height:28px;" />
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
				    <label class="LabName"><span>作案人数：</span></label><input readonly="readonly" type="text" class="inp1 easyui-numberspinner requestParam" data-options="min:0,validType:'maxLength[6]'" name="schoolRelatedEvents.crimeNum" id="schoolRelatedEvents_crimeNum" value="<#if schoolRelatedEvents?? && schoolRelatedEvents.crimeNum??>${schoolRelatedEvents.crimeNum?c}<#else>0</#if>" style="width:114px; height:28px;" /></li>
				</td>
			</tr>
		</table>

		<div id="primeSuspectInfoDiv">
			<div id="con8" class="title FontDarkBlue pos-rela" >主要嫌疑犯<div class="title-r-btn"><a href="###" class="BigNorToolBtn add-addr" onclick="addPartyIndividual('13')"><i class="icon-add-w"></i>新 增</a></div></div>
			<div class="table-normal">
				<table id="primeSuspectIn_list" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
					<tr>
						<th style="display: none;"><font color="red">*</font>人员类型</th>
						<th style="width:144px"><font color="red" >*</font>姓名</th>
						<th style="width:145px"><font color="red" >*</font>证件类型</th>
						<th style="width:239px"><font color="red" >*</font>证件号码</th>
						<th style="width:239px">联系电话</th>
						<th style="width:100px"><font color="red" ></font>性别</th>
						<th style="width:92px;"></th>
					</tr>
					</thead>
					<tbody id="primeSuspectIn_tbody">
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>