<div class="clear title FontDarkBlue homicideCase hide" id="eventLabel_homicideCase">命案防控</div>
<div class="has-more homicideCase hide">
	<div class="basic-infor">
		<input type="hidden" id="isSaveHomicideCase" name="isSaveHomicideCase" value="false" />
		<input type="hidden" name="homicideCase.reId" value="<#if homicideCase?? && homicideCase.reId??>${homicideCase.reId?c}</#if>"/>
		<input type="hidden" name="homicideCase.bizType" value="4" />
		
		<!-- 涉及人员 -->
		<input type="hidden" id="homicideCase_person" name="homicideCase.personJson" />
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="LeftTd leftTdWidth">
					<input type="hidden" name="homicideCase.occuDateStr" value="${event.happenTimeStr!}" />
					<label class="LabName"><span>发生开始日期：</span></label>
					<div class="Check_Radio FontDarkBlue">${event.happenTimeStr!}</div>
				</td>
				<td>
					<label class="LabName"><span>侦查结束日期：</span></label>
					<input type="text" name="homicideCase.spyEndDateStr" class="inp1 Wdate easyui-validatebox" style="cursor: pointer;" data-options="tipPosition:'bottom'" onclick="WdatePicker({readOnly:true, minDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="<#if homicideCase??>${homicideCase.spyEndDateStr!}</#if>" readonly="readonly"></input>
				</td>
			</tr>
			<tr>
				<td class="LeftTd" colspan="2">
					<label class="LabName"><span>简要情况：</span></label><textarea name="homicideCase.situation" class="area1 autoCellWidth easyui-validatebox" style="height:125px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[4000]','characterCheck']" ><#if homicideCase??>${homicideCase.situation!}</#if></textarea>
				</td>
			</tr>
		</table>

		<div id="suspectInfoDiv">
			<div id="con8" class="title FontDarkBlue pos-rela" >命案犯罪嫌疑人<div class="title-r-btn"><a href="###" class="BigNorToolBtn add-addr" onclick="addPartyIndividual('03')"><i class="icon-add-w"></i>新 增</a></div></div>
				<div class="table-normal">
					<table id="suspectInfo_list" width="100%" border="0" cellspacing="0" cellpadding="0">
						<thead>
						<tr>
							<th style="display: none;"><font color="red">*</font>涉及人员业务类型</th>
							<th style="width:144px"><font color="red" >*</font>姓名</th>
							<th style="width:110px"><font color="red" >*</font>证件类型</th>
							<th style="width:180px"><font color="red" >*</font>证件号码</th>
							<th style="width:150px">联系电话</th>
							<th style="width:239px">人员类型</th>
							<th style="width:100px"><font color="red" ></font>性别</th>
							<th style="width:92px;"></th>
						</tr>
						</thead>
						<tbody id="suspectInfo_tbody">
						</tbody>
					</table>
				</div>
		</div>
		<div id="victimInfoDiv">
			<div id="con8" class="title FontDarkBlue pos-rela" >命案受害人<div class="title-r-btn"><a href="###" class="BigNorToolBtn add-addr" onclick="addPartyIndividual('04')"><i class="icon-add-w"></i>新 增</a></div></div>
			<div class="table-normal">
				<table id="victimInfo_list" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
					<tr>
						<th style="display: none;"><font color="red">*</font>人员类型</th>
						<th style="width:144px"><font color="red" >*</font>姓名</th>
						<th style="width:110px"><font color="red" >*</font>证件类型</th>
						<th style="width:180px"><font color="red" >*</font>证件号码</th>
						<th style="width:150px">联系电话</th>
						<th style="width:239px"></th>
						<th style="width:100px"><font color="red" ></font>性别</th>
						<th style="width:92px;"></th>
					</tr>
					</thead>
					<tbody id="victimInfo_tbody">
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div id="partyIndividualInfoDiv" style="height: 1px;display: none" >
		<iframe name="baseInfoIframe" id="baseInfoIframe" width="1000" frameborder="0" ></iframe>
	</div>
</div>