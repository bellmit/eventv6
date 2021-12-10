<div class="clear title EventLabelTitle FontDarkBlue disputeMediation hide" id="disputeMediation">矛盾纠纷排查化解</div>
<div class="has-more disputeMediation hide">
	<div class="basic-infor">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
               <td class="LeftTd" style="width:50%">
               		<label class="LabName"><span>发生日期：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                   		<#if disputeMediation?? && disputeMediation.happenTimeStr??>${disputeMediation.happenTimeStr}</#if>
                 	</div>
               </td>
               <td><label class="LabName"><span>受理日期：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                   		<#if disputeMediation?? && disputeMediation.acceptedDateStr??>${disputeMediation.acceptedDateStr}</#if>
                 	</div>
               </td>
            </tr>
			<tr>
               <td class="LeftTd">
               		<label class="LabName"><span>化解时限：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                   		<#if disputeMediation?? && disputeMediation.mediationDeadlineStr??>${disputeMediation.mediationDeadlineStr}</#if>
                 	</div>
               </td>
               <td><label class="LabName"><span>涉及金额：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                   		<#if disputeMediation?? && disputeMediation.involvedAmount??>${disputeMediation.involvedAmount}<label><span>（元）</span></label></#if>
                    </div>
               </td>
            </tr>
			<tr>
               <td class="LeftTd" colspan="2">
               		<label class="LabName"><span>考评日期：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                   		<#if disputeMediation?? && disputeMediation.evaDateStr??>${disputeMediation.evaDateStr}</#if>
                 	</div>
               </td>
			</tr>
			<tr>
               <td class="LeftTd" colspan="2">
               		<label class="LabName"><span>考评信息：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                   		<#if disputeMediation?? && disputeMediation.evaOpn??>${disputeMediation.evaOpn}</#if>  
                    </div>
               </td>
            </tr>
			<tr>
               <td class="LeftTd">
               		<label class="LabName"><span>事件规模：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                   		<#if disputeMediation?? && disputeMediation.disputeScaleStr??>${disputeMediation.disputeScaleStr}</#if>
                 	</div>
               </td>
               <td><label class="LabName"><span>是否化解：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                   		<#if disputeMediation?? && disputeMediation.mediationTypeStr??>是<#else>否</#if>  
                    </div>
               </td>
            </tr>
            <#if disputeMediation?? && disputeMediation.mediationTypeStr??>
			<tr>
               <td class="LeftTd">
               		<label class="LabName"><span>化解方式：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                		<#if disputeMediation?? && disputeMediation.mediationTypeStr??>${disputeMediation.mediationTypeStr}</#if>  
                 	</div>
               </td>
               <td><label class="LabName"><span>最后化解时间：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                   		<#if disputeMediation?? && disputeMediation.mediationDeadlineStr??>${disputeMediation.mediationDeadlineStr}</#if>
                    </div>
               </td>
           </tr>
           
            <tr>
                <td class="LeftTd">
                	<label class="LabName"><span>化解组织：</span></label>
                    <div class="Check_Radio FontDarkBlue" id="mediationOrgName">
                    </div>
                    <div style="display: none;">
                        <input type="text" id="mediationOrgNameExt" name="mediationOrgNameExt"
                               value="<#if disputeMediation.mediationOrgName??>${disputeMediation.mediationOrgName}</#if>"
                               class="inp1 comboselector" style="width:155px;height:32px;"
                               data-options="dType:'legal',panelWidth:300, editable:false, hasDownArrow: false" query-params="orgCode=${orgCode}"/>
                    </div>
                </td>
                <td>
                	<label class="LabName"><span>化解是否成功：</span></label>
                    <div class="Check_Radio FontDarkBlue">
                        <#if disputeMediation?? && disputeMediation.isSuccess?? && disputeMediation.isSuccess=="1">是<#elseif disputeMediation?? && disputeMediation.isSuccess?? && disputeMediation.isSuccess=="0">否</#if>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="LeftTd">
                	<label class="LabName"><span>化解情况：</span></label>
                	<div class="Check_Radio FontDarkBlue" style="width:87%">
                    	<#if disputeMediation?? && disputeMediation.mediationResult??>${disputeMediation.mediationResult}</#if>
                	</div>
                </td>
            </tr>
            </#if>
		</table>

        <#--矛盾纠纷当事人-->
        <div id="partyInfoDiv">
            <div id="con8" class="title EventLabelTitle FontDarkBlue pos-rela" >当事人<div class="title-r-btn"></div></div>
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
                    </tr>
                    </thead>
                    <tbody id="partyInfo_tbody">
                    <#if disputeMediation?? && disputeMediation.suspectList?? && (disputeMediation.suspectList?size > 0)>
                        <#list disputeMediation.suspectList as list>
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
                                <td style='width:144px'>
                                    <div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.peopleTypeName??>${list.peopleTypeName!}</#if></div>
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

        <#--矛盾化解责任人-->
        <div id="peopleInChargeInfoDiv">
            <div id="con8" class="title EventLabelTitle FontDarkBlue pos-rela" >化解责任人<div class="title-r-btn"></div></div>
            <div class="table-normal">
                <table id="peopleInChargeInfo_list" width="100%" border="0" cellspacing="0" cellpadding="0">
                    <thead>
                    <tr>
                        <th style="display: none;"><font color="red">*</font>人员类型</th>
                        <th style="width:144px"><font color="red" >*</font>姓名</th>
                        <th style="width:145px"><font color="red" >*</font>证件类型</th>
                        <th style="width:239px"><font color="red" >*</font>证件号码</th>
                        <th style="width:145px">联系电话</th>
                        <th style="width:100px"><font color="red" ></font>性别</th>
                    </tr>
                    </thead>
                    <tbody id="peopleInChargeInfo_tbody">
                    <#if disputeMediation?? && disputeMediation.peopleInChargeList?? && (disputeMediation.peopleInChargeList?size > 0)>
                        <#list disputeMediation.peopleInChargeList as list>
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
                                <td style='width:144px'> 
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