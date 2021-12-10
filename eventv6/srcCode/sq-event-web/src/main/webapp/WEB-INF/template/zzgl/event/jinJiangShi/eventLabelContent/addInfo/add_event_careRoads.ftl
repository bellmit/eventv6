<div class="clear title FontDarkBlue careRoads hide" id="eventLabel_careRoads">涉及线路案(事)件</div>
<div class="has-more careRoads hide">
	<div class="basic-infor">
		<input type="hidden" id="isSaveCareRoads" name="isSaveCareRoads" value="false" />
		<input type="hidden" id="careRoads_reId" name="careRoads.reId" value="<#if careRoads?? && careRoads.reId??>${careRoads.reId?c}</#if>" />
		<input type="hidden" id="careRoads_bizType" name="careRoads.bizType" value="<#if careRoads?? && careRoads.bizType??>${careRoads.bizType}<#else>1</#if>" />
		
		<input type="hidden" id="careRoads_bizId" name="careRoads.bizId" value="<#if careRoads?? && careRoads.bizId??>${careRoads.bizId?c}</#if>" />
		<input type="hidden" id="careRoads_bizName" value="" />

		<!-- 涉及人员 -->
		<input type="hidden" id="careRoads_person" name="careRoads.personJson" />

		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="LeftTd" colspan="2">
					<label class="LabName"><span><font color="red">*</font>案件性质：</span></label>
					<input type="hidden" id="careRoads_nature" name="careRoads.nature" value="" />
					<input type="text" class="inp1 easyui-validatebox requestParam" style="width: 200px;" id="careRoads_natureName" value="" />
				</td>
			</tr>
			<tr>
				<td class="LeftTd" colspan="2">
					<label class="LabName"><span><font color="red">*</font>侦破情况：</span></label><textarea name="careRoads.detectedOverview" id="careRoads_detectedOverview" cols="" rows="" class="area1 autoCellWidth easyui-validatebox requestParam" data-options="validType:['maxLength[1024]','characterCheck']" style="height:64px;resize:none;"><#if careRoads?? && careRoads.detectedOverview??>${careRoads.detectedOverview}</#if></textarea>
				</td>
			</tr>
			<#--<tr>
				<td class="LeftTd" <#if !(careRoads??)>colspan="2"</#if> >
				    <label class="LabName"><span><font color="red">*</font>是否破案：</span></label>
				    <input type="hidden" id="careRoads_isDetection"  name="careRoads.isDetection"  value="<#if careRoads?? && careRoads.isDetection??>${careRoads.isDetection}<#else>0</#if>"> 
                    <input class="toggle" style="cursor: pointer;outline:none;" type="checkbox"  id="careRoads_isDetectionCheck" onclick="changeDetectionValue('careRoads');" <#if (careRoads?? && careRoads.isDetection?? && careRoads.isDetection=='1')>checked</#if> />
				</td>
				<#if careRoads?? && careRoads.reNo??>
				    <td>
						<label class="LabName"><span>案件编号：</span></label><div class="Check_Radio FontDarkBlue">${careRoads.reNo}</div>
					</td>
				</#if>
			</tr>-->
			<tr>
					<td class="LeftTd" <#if !(careRoads??)>colspan="2"</#if> >
						<label class="LabName"><span><font color="red">*</font>是否破案：</span></label>
						<div class="zz-form" zz-form-filter="careRoadsIsDetection">
							<input type="checkbox" zz-form-filter="_careRoadsIsDetection" name="careRoads.isDetection" id="careRoads_isDetection" value="<#if careRoads?? && careRoads.isDetection??>${careRoads.isDetection}<#else>0</#if>" <#if (careRoads?? && careRoads.isDetection?? && careRoads.isDetection=='1')>checked</#if>>
							<#--<input type="hidden" id="careRoads_isDetection"  name="careRoads.isDetection"  value="<#if careRoads?? && careRoads.isDetection??>${careRoads.isDetection}<#else>0</#if>">
							<input class="toggle" style="cursor: pointer;outline:none;" type="checkbox"  id="careRoads_isDetectionCheck" onclick="changeDetectionValue('careRoads');" <#if (careRoads?? && careRoads.isDetection?? && careRoads.isDetection=='1')>checked</#if> />-->
						</div>
					</td>
				<#if careRoads?? && careRoads.reNo??>
					<td>
						<label class="LabName"><span>案件编号：</span></label><div class="Check_Radio FontDarkBlue">${careRoads.reNo}</div>
					</td>
				</#if>
			</tr>
			
			<tr>
				<td class="LeftTd leftTdWidth">
				    <label class="LabName"><span>在逃人数：</span></label><input type="text" class="inp1 easyui-numberspinner requestParam" data-options="min:0,validType:'maxLength[5]',onSpinUp:function(){countCrimeNum('careRoads');},onSpinDown:function(){countCrimeNum('careRoads');},onChange:function(){countCrimeNum('careRoads');}" name="careRoads.ecapeNum" id="careRoads_ecapeNum" value="<#if careRoads?? && careRoads.ecapeNum??>${careRoads.ecapeNum?c}<#else>0</#if>" onblur="countCrimeNum('careRoads')" style="width:114px; height:28px;" />
				</td>
				<td>
				    <label class="LabName"><span>抓捕人数：</span></label><input type="text" class="inp1 easyui-numberspinner requestParam" data-options="min:0,validType:'maxLength[5]',onSpinUp:function(){countCrimeNum('careRoads');},onSpinDown:function(){countCrimeNum('careRoads');},onChange:function(){countCrimeNum('careRoads');}" name="careRoads.arrestedNum" id="careRoads_arrestedNum" value="<#if careRoads?? && careRoads.arrestedNum??>${careRoads.arrestedNum?c}<#else>0</#if>" onblur="countCrimeNum('careRoads')" style="width:114px; height:28px;" />
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
				    <label class="LabName"><span>作案人数：</span></label><input readonly="readonly" type="text" class="inp1 easyui-numberspinner requestParam" data-options="min:0,validType:'maxLength[6]'" name="careRoads.crimeNum" id="careRoads_crimeNum" value="<#if careRoads?? && careRoads.crimeNum??>${careRoads.crimeNum?c}<#else>0</#if>" style="width:114px; height:28px;" /></li>
				</td>
			</tr>
		</table>

		<div id="majorSuspectInfoDiv">
			<div id="con8" class="title FontDarkBlue pos-rela" >主要嫌疑犯<div class="title-r-btn"><a href="###" class="BigNorToolBtn add-addr" onclick="addPartyIndividual('12')"><i class="icon-add-w"></i>新 增</a></div></div>
			<div class="table-normal">
				<table id="majorSuspectInfo_list" width="100%" border="0" cellspacing="0" cellpadding="0">
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
					<#--涉及线路主要嫌疑犯-->
					<tbody id="majorSuspectInfo_tbody">
					</tbody>
				</table>
			</div>
		</div>

	</div>
</div>
