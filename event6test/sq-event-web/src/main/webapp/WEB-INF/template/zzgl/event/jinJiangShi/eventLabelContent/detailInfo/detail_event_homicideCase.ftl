<div class="clear title EventLabelTitle FontDarkBlue homicideCase hide" id="homicideCase">命案防控</div>
<div class="has-more homicideCase hide">
	<div class="basic-infor">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="LeftTd leftTdWidth">
					<label class="LabName"><span>发生开始日期：</span></label>
					<div class="Check_Radio FontDarkBlue"><#if homicideCase??>${homicideCase.occuDateStr!}</#if></div>
				</td>
				<td class="LeftTd">
					<label class="LabName"><span>侦查结束日期：</span></label>
					<div class="Check_Radio FontDarkBlue"><#if homicideCase??>${homicideCase.spyEndDateStr!}</#if></div>
				</td>
			</tr>
			<tr>
				<td class="LeftTd" colspan="2">
					<label class="LabName"><span>简要情况：</span></label>
					<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if homicideCase??>${homicideCase.situation!}</#if></div>
				</td>
			</tr>
		</table>

		<div id="suspectInfoDiv">
			<div id="con8" class="title EventLabelTitle FontDarkBlue pos-rela" >命案犯罪嫌疑人</div>
			<div class="table-normal">
				<table id="suspectInfo_list" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
					<tr>
						<th style="display: none;"><font color="red">*</font>人员类型</th>
						<th style="width:144px"><font color="red" >*</font>姓名</th>
						<th style="width:145px"><font color="red" >*</font>证件类型</th>
						<th style="width:239px"><font color="red" >*</font>证件号码</th>
						<th style="width:239px">联系电话</th>
						<th style="width:239px">人员类型</th>
						<th style="width:100px"><font color="red" ></font>性别</th>
					</tr>
					</thead>
					<tbody>
						<#if homicideCase?? && homicideCase.suspectList?? && (homicideCase.suspectList?size > 0)>
							<#list homicideCase.suspectList as list>
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
									<td style='width:269px'>
										<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.idCard??>${list.idCard!}</#if></div>
									</td>
									<td style='width:209px'>
										<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.tel??>${list.tel!}</#if></div>
									</td>
									<td style='width:150px'>
										<#if list.isMinors?? && list.isMinors == 1>
											<img title="未成年人" src="${rc.getContextPath()}/images/juveniles.png" style="margin:0 10px 0 0; width:28px; height:28px;"/>
										</#if>
										<#if list.isTeenager?? && list.isTeenager == 1>
											<img title="青少年" src="${rc.getContextPath()}/images/youngsters.png" style="margin:0 10px 0 0; width:28px; height:28px;"/>
										</#if>
										<#if list.isMentalDisease?? && list.isMentalDisease == 1>
											<img title="精神病患者" src="${rc.getContextPath()}/images/psychotic.png" style="margin:0 10px 0 0; width:28px; height:28px;"/>
										</#if>
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
		<div id="victimInfoDiv">
			<div id="con8" class="title FontDarkBlue pos-rela" >命案受害人</div>
			<div class="table-normal">
				<table id="victimInfo_list" width="100%" border="0" cellspacing="0" cellpadding="0">
					<thead>
					<tr>
						<th style="display: none;"><font color="red">*</font>人员类型</th>
						<th style="width:144px"><font color="red" >*</font>姓名</th>
						<th style="width:145px"><font color="red" >*</font>证件类型</th>
						<th style="width:239px"><font color="red" >*</font>证件号码</th>
						<th style="width:239px">联系电话</th>
						<th style="width:239px"></th>
						<th style="width:100px"><font color="red" ></font>性别</th>

					</tr>
					</thead>
					<tbody>
						<#if homicideCase?? && homicideCase.victimList?? && (homicideCase.victimList?size > 0)>
							<#list homicideCase.victimList as list>
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
									<td style='width:269px'>
										<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.idCard??>${list.idCard!}</#if></div>
									</td>
									<td style='width:144px'>
										<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.tel??>${list.tel!}</#if></div>
									</td>
									<td style='width:209px'>
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