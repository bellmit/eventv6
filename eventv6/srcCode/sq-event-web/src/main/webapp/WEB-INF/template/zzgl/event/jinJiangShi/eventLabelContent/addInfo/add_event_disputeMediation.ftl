<div class="clear title FontDarkBlue disputeMediation hide" id="eventLabel_disputeMediation">矛盾纠纷排查化解</div>
<div class="has-more disputeMediation hide">
	<div class="basic-infor">
		 <input type="hidden" id="isSaveDisputeMediation" name="isSaveDisputeMediation" value="false" />
		 <input type="hidden" id="disputeMediation_mediationId" name="disputeMediation.mediationId" value="<#if disputeMediation?? && disputeMediation.mediationId??>${disputeMediation.mediationId?c}</#if>" />

        <!-- 矛盾纠纷当事人 -->
        <input type="hidden" id="disputeMediation_person" name="disputeMediation.personJson" />

		 <table width="100%" border="0" cellspacing="0" cellpadding="0">
		 		<tr>
                    <td class="LeftTd leftTdWidth">
                    	<label class="LabName"><span><label class="Asterik">*</label>发生日期：</span></label>
                    	<input id="disputeMediation_happenTimeStrr" name="disputeMediation.happenTimeStr" type="text" class="inp1 inp2 Wdate"  onclick="WdatePicker( {maxDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59',dateFmt:'yyyy-MM-dd HH:mm:ss', readOnly:true, alwaysUseStartDate:true})" value="<#if disputeMediation?? && disputeMediation.happenTimeStr??>${disputeMediation.happenTimeStr}<#else>${maxHappenTime}</#if>"/>
                    </td>
                    <td>
                    	<label class="LabName"><span>受理日期：</span></label>
                    	<input id="disputeMediation_acceptedDateStr" name="disputeMediation.acceptedDateStr" type="text" class="inp1 inp2 Wdate"  onclick="WdatePicker({maxDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)}',dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true})" value="<#if disputeMediation?? && disputeMediation.acceptedDateStr??>${disputeMediation.acceptedDateStr}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd"><label class="LabName"><span><label class="Asterik">*</label>化解时限：</span></label>
                    	<input id="disputeMediation_mediationDeadlineStr" name="disputeMediation.mediationDeadlineStr" type="text" class="inp1 inp2 Wdate requestParam"  onclick="WdatePicker({minDate:new Date().format('yyyy-MM-dd'),dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true})" value="<#if disputeMediation?? && disputeMediation.mediationDeadlineStr??>${disputeMediation.mediationDeadlineStr}</#if>"/>
                    </td>
                    <td><label class="LabName"><span>涉及金额：</span></label>
                        <input name="disputeMediation.involvedAmount" id="disputeMediation_involvedAmount" style="height: 28px;" type="text" value="<#if disputeMediation?? && disputeMediation.involvedAmount??>${disputeMediation.involvedAmount?string('0.00')}</#if>" class="inp1 easyui-numberbox easyui-validatebox" max="9999999.99" size="8" maxlength="10" data-options="tipPosition:'bottom'"/>
						<span class="Check_Radio" style="float:none;">（元）</span>
                    </td>
                </tr>
                <tr>
					<td class="LeftTd" colspan="2">
				    	<label class="LabName"><span>考评日期：</span></label>
						<input id="disputeMediation_evaDateStr" name="disputeMediation.evaDateStr" type="text" readonly="readonly" class="inp1 inp2 Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true})" value="<#if disputeMediation?? && disputeMediation.evaDateStr??>${disputeMediation.evaDateStr}</#if>"/>					
					</td>
                </tr>
                <tr>
					<td class="LeftTd" colspan="2">
				    	<label class="LabName"><span>考评信息：</span></label>
                    	<textarea name="disputeMediation.evaOpn" id="disputeMediation_evaOpn" style="height: 45px;resize:none" cols="45" rows="3" class="area1 autoCellWidth easyui-validatebox" maxLength="600" data-options="tipPosition:'bottom',validType:['maxLength[600]','characterCheck']"><#if disputeMediation?? && disputeMediation.evaOpn??>${disputeMediation.evaOpn}</#if></textarea>
					</td>
				</tr>
				
				<tr>
					<td class="LeftTd">
				    	<label class="LabName"><span><label class="Asterik">*</label>事件规模：</span></label>
				    	<input type="hidden" id="disputeMediation_disputeScale" name="disputeMediation.disputeScale" value="" />
						<input type="text" class="inp1 easyui-validatebox requestParam" id="disputeMediation_disputeScaleStr" value="" />
					</td>
					<td>
				    	<label class="LabName"><span>是否化解：</span></label>
                    	<div class="zz-form" zz-form-filter="disputeMediationIsDetection">
							<input type="checkbox" zz-form-filter="_disputeMediationIsDetection" id="disputeMediation_isDetection" value="<#if disputeMediation?? && disputeMediation.mediationType??>1<#else>0</#if>" <#if (disputeMediation?? && disputeMediation.mediationTypeStr??)>checked</#if>>
						</div>
					</td>
				</tr>
            </table>
            
            
            <table width="100%" border="0" cellspacing="0" cellpadding="0" id="disputeMediationIsDetection" <#if !(disputeMediation?? && disputeMediation.mediationType??)>style="display:none"</#if>>
            	<tr>
                    <td class="LeftTd"><label class="LabName"><span><label class="Asterik">*</label>化解方式：</span></label>
                    	<input type="hidden" id="disputeMediation_mediationType" name="disputeMediation.mediationType" value="<#if disputeMediation?? && disputeMediation.mediationType??>${disputeMediation.mediationType}</#if>">
                    	<input name="disputeMediation.mediationTypeStr" id="disputeMediation_mediationTypeStr" type="text" class="inp1 inp2 InpDisable easyui-validatebox requestParam" readonly value="<#if disputeMediation?? && disputeMediation.mediationTypeStr??>${disputeMediation.mediationTypeStr}</#if>" />
                    </td>
                    <td><label class="LabName"><span><label class="Asterik">*</label>最后化解时间：</span></label>
                        <input type="text" id="disputeMediation_mediationDeadline" name="disputeMediation.mediationDeadlineStr" class="inp1 Wdate easyui-validatebox requestParam" style="cursor:pointer;" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd', isShowClear:false, isShowToday:true})" value="<#if disputeMediation?? && disputeMediation.mediationDeadlineStr??>${disputeMediation.mediationDeadlineStr!}</#if>" readonly="readonly"></input>
                    </td>
                </tr>
              <#--  <tr>
                    <td class="LeftTd"><label class="LabName"><span>责任人姓名：</span></label>
                    	<input name="disputeMediation.mediator" id="disputeMediation_mediator" type="text" class="inp1 inp2 easyui-validatebox" value="<#if disputeMediation?? && disputeMediation.mediator??>${disputeMediation.mediator}</#if>"  data-options="validType:['maxLength[15]','characterCheck']"/>
                    </td>
                    <td class="LeftTd"><label class="LabName"><span><label class="Asterik">*</label>责任人电话：</span></label>
                    	<input name="disputeMediation.mediationTel" id="disputeMediation_mediationTel" type="text" class="inp1 inp2 easyui-validatebox requestParam" value="<#if disputeMediation?? && disputeMediation.mediationTel??>${disputeMediation.mediationTel}</#if>"  data-options="validType:'mobileorphone'"/>
                    </td>
                </tr>-->
                <tr>
                    <td class="LeftTd"><label class="LabName"><span><label class="Asterik">*</label>化解组织：</span></label>
                    	<input type="text" id="disputeMediation_mediationOrgName" name="disputeMediation.mediationOrgName"
                               value="<#if disputeMediation?? && disputeMediation.mediationOrgName??>${disputeMediation.mediationOrgName}</#if>"
                               class="inp1 comboselector requestParam" style="width:205px;height:32px;"
							    data-options="dType:'legal',panelWidth:300, editable:true, hasDownArrow: false" query-params="orgCode=<#if orgCode??>${orgCode}</#if>"/>

						<#--<a href="javascript:void(0)" id="newHtml" style="float:inherit;height: 38px;margin: 0 10px;padding: 0px 10px 0 15px;" class="BigNorToolBtn add-addr" onclick="addRecOrg();">
						<i class="icon-add-w"></i></a>-->
                    </td>
                	<td><label class="LabName"><span><label class="Asterik">*</label>化解是否成功：</span></label>
			            <input type="hidden" id="disputeMediation_isSuccess"  name="disputeMediation.isSuccess" value="<#if disputeMediation?? && disputeMediation.isSuccess??>${disputeMediation.isSuccess!''}</#if>">
                        <input id="disputeMediation_isSuccessStr" type="text" class="inp1 easyui-validatebox requestParam" readonly value="<#if disputeMediation?? && disputeMediation.isSuccess?? && disputeMediation.isSuccess==1>是<#else>否</#if>" />
                    </td>
                </tr>
            	<tr>
                    <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span><label class="Asterik">*</label>化解情况：</span></label>
                    <textarea name="disputeMediation.mediationResult" id="disputeMediation_mediationResult" style="height: 45px;resize:none" cols="45" rows="3" class="area1 autoCellWidth easyui-validatebox requestParam" maxLength="600" data-options="tipPosition:'bottom',validType:['maxLength[600]','characterCheck']"><#if disputeMediation?? && disputeMediation.mediationResult??>${disputeMediation.mediationResult}</#if></textarea></td>
            	</tr>
            </table>

        <#--矛盾纠纷当事人-->
        <div id="partyInfoDiv">
            <div id="con8" class="title FontDarkBlue pos-rela" >当事人<div class="title-r-btn"><a href="###" class="BigNorToolBtn add-addr" onclick="addPartyIndividual('06')"><i class="icon-add-w"></i>新 增</a></div></div>
            <div class="table-normal">
                <table id="partyInfo_list" width="100%" border="0" cellspacing="0" cellpadding="0">
                    <thead>
                    <tr>
                        <th style="display: none;"><font color="red">*</font>人员业务类型</th>
                        <th style="width:144px"><font color="red" >*</font>姓名</th>
                        <th style="width:145px"><font color="red" >*</font>证件类型</th>
                        <th style="width:239px"><font color="red" >*</font>证件号码</th>
                        <th style="width:145px"><font color="red" >*</font>人员类型</th>
                        <th style="width:100px"><font color="red" ></font>性别</th>
                        <th style="width:92px;"></th>
                    </tr>
                    </thead>
                    <tbody id="partyInfo_tbody">
                    </tbody>
                </table>
            </div>
        </div>
        <#--矛盾纠纷化解责任人-->
        <div id="peopleInChargeInfoDiv">
            <div id="con8" class="title FontDarkBlue pos-rela" >化解责任人<div class="title-r-btn"><a href="###" class="BigNorToolBtn add-addr" onclick="addPartyIndividual('14')"><i class="icon-add-w"></i>新 增</a></div></div>
            <div class="table-normal">
                <table id="peopleInChargeInfo_list" width="100%" border="0" cellspacing="0" cellpadding="0">
                    <thead>
                    <tr>
                        <th style="display: none;"><font color="red">*</font>人员业务类型</th>
                        <th style="width:144px"><font color="red" >*</font>姓名</th>
                        <th style="width:145px"><font color="red" >*</font>证件类型</th>
                        <th style="width:239px"><font color="red" >*</font>证件号码</th>
                        <th style="width:145px">联系电话</th>
                        <th style="width:100px"><font color="red" ></font>性别</th>
                        <th style="width:92px;"></th>
                    </tr>
                    </thead>
                    <tbody id="peopleInChargeInfo_tbody">
                    </tbody>
                </table>
            </div>
        </div>
	</div>
</div>