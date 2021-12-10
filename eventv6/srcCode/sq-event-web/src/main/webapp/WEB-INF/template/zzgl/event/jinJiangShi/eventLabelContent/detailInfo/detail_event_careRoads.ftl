<div class="clear title EventLabelTitle FontDarkBlue careRoads hide" id="careRoads">涉及线路案(事)件</div>
<div class="has-more careRoads hide">
	<div class="basic-infor">
			
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="LeftTd" colspan="2"> 
					<label class="LabName"><span>案件性质：</span></label><div class="Check_Radio FontDarkBlue"><#if careRoads?? && careRoads.natureName??>${careRoads.natureName}</#if></div>
				</td>
			</tr>
			<tr>
				<td class="LeftTd" colspan="2">
					<label class="LabName"><span>侦破情况：</span></label><div class="Check_Radio FontDarkBlue" style="width:87%"><#if careRoads?? && careRoads.detectedOverview??>${careRoads.detectedOverview}</#if></div>
				</td>
			</tr>
			<tr>
				<td class="LeftTd leftTdWidth"  >
				    <label class="LabName"><span>是否破案：</span></label><div class="Check_Radio FontDarkBlue"><#if (careRoads?? && careRoads.isDetection?? && careRoads.isDetection=='1')>是<#else>否</#if></div>
				</td>
				<td>
					<label class="LabName"><span>案件编号：</span></label><div class="Check_Radio FontDarkBlue"><#if careRoads?? && careRoads.reNo??>${careRoads.reNo}</#if></div>
				</td>
			</tr>
			
			<tr>
				<td class="LeftTd">
				    <label class="LabName"><span>在逃人数：</span></label><div class="Check_Radio FontDarkBlue"><#if careRoads?? && careRoads.ecapeNum??>${careRoads.ecapeNum}</#if>(人)</div>
				</td>
				<td>
				    <label class="LabName"><span>抓捕人数：</span></label><div class="Check_Radio FontDarkBlue"><#if careRoads?? && careRoads.arrestedNum??>${careRoads.arrestedNum}</#if>(人)</div>
				</td>
			</tr>
			<tr>
				<td class="LeftTd">
				    <label class="LabName"><span>作案人数：</span></label><div class="Check_Radio FontDarkBlue"><#if careRoads?? && careRoads.crimeNum??>${careRoads.crimeNum}</#if>(人)</div>
				</td>
			</tr>
		</table>

		<div id="majorSuspectInfoDiv">
			<div id="con8" class="title EventLabelTitle FontDarkBlue pos-rela" >主要嫌疑犯</div>
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
					</tr>
					</thead>
					<tbody>
						<#if careRoads?? && careRoads.suspectList?? && (careRoads.suspectList?size > 0)>
							<#list careRoads.suspectList as list>
								<tr>
									<td style="display:none;">
										<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.bizType??>${list.bizType!}</#if></div>
									</td>
									<td style='width:144px'>
										<div class="Check_Radio FontDarkBlue" style="width: 86%;"><a onclick="detailPartyIndividual(<#if list.partyId??>${list.partyId!}</#if>)" style="cursor: pointer"><#if list.name??>${list.name!}</#if></a></div>
									</td>
									<td style='width:145px'>
										<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.cardTypeName??>${list.cardTypeName!}</#if></div>
									</td>
									<td style='width:239px'>
										<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.idCard??>${list.idCard!}</#if></div>
									</td>
									<td style='width:239px'>
										<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.tel??>${list.tel!}</#if></div>
									</td>
									<td style='width:100px'>
										<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.sexName??>${list.sexName!}</#if></div>
									</td>
								</tr>
							</#list>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
