<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>化解信息-新增</title>

<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/FieldCfg.ftl" />
<#include "/component/ComboBox.ftl">
<style type="text/css">
    .inp2{width: 150px;}
    .Asterik{font-size:12px; color:red; }
</style>
</head>
<body>
<div>
<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/event/dispute/save.jhtml"  method="post" enctype="multipart/form-data">
    <input type="hidden" id="mediationId" name="mediationId" value="<#if disputeMediation.mediationId??>${disputeMediation.mediationId}</#if>">
    <input type="hidden" id="disputeId" name="disputeId" value="<#if disputeMediation.disputeId??>${disputeMediation.disputeId}</#if>">
    <input type="hidden" id="mediationResId" name="mediationResId" value="<#if disputeMediation.mediationResId??>${disputeMediation.mediationResId}</#if>">
    <input type="hidden" id="gridId" name="gridId" value="<#if disputeMediation.gridId??>${disputeMediation.gridId}</#if>">
    <input type="hidden" id="gridCode" name="gridCode" value="">
		<input type="hidden" id="id" name="id" value="<#if disputeMediation.mediationId??>${disputeMediation.mediationId}</#if>"/>
		<input type="hidden" id="module" name="module" value="<#if module??>${module}</#if>"/>
		<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
<!--     <input type="hidden" id="involvedAmount" name="involvedAmount" value=""> -->
    <input type="hidden" id="mediationCode" name="mediationCode" value="<#if disputeMediation.mediationCode??>${disputeMediation.mediationCode}</#if>">
    <input type="hidden" id="disputeStatus" name="disputeStatus" value="<#if disputeMediation.disputeStatus??>${disputeMediation.disputeStatus}</#if>">
    <input type="hidden" id="status" name="status" value="<#if disputeMediation.status??>${disputeMediation.status}</#if>">
	<div id="content-d" class="MC_con content light" style="overflow-x:hidden; position:relative;">
	                <div class="NorForm NorForm2">
                <div class="title FontDarkBlue">化解信息</div>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="LeftTd"><label class="LabName"><span><label class="Asterik">*</label>化解方式：</span></label>
                    <input type="hidden" id="mediationType" name="mediationType" value="<#if disputeMediation.mediationType??>${disputeMediation.mediationType}</#if>">
                    <input name="mediationTypeStr" id="mediationTypeStr" type="text" data-options="required:true" class="inp1 inp2 InpDisable easyui-validatebox" readonly value="" class="inp1" />
                        </td>

                    <td><label class="LabName"><span><label class="Asterik">*</label>化解日期：</span></label>
                        <input id="mediationDateStr" name="mediationDateStr" type="text" data-options="required:true" readonly="readonly" class="inp1 inp2 Wdate easyui-validatebox"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true})" value="<#if disputeMediation.mediationDateStr??>${disputeMediation.mediationDateStr}</#if>"/>
                    </td>

                        </tr>
                	<tr>
                        <td class="LeftTd"><label class="LabName"><span>化解责任人姓名：</span></label>
                    <input name="mediator" id="mediator" type="text" class="inp1 inp2 easyui-validatebox" value="<#if disputeMediation.mediator??>${disputeMediation.mediator}</#if>"  data-options="validType:['maxLength[15]','characterCheck']"/>
                        </td>
                        <td class="LeftTd"><label class="LabName"><span>化解责任人联系方式：</span></label>
                    		<input name="mediationTel" id="mediationTel" type="text" class="inp1 inp2 easyui-validatebox" value="<#if disputeMediation.mediationTel??>${disputeMediation.mediationTel}</#if>"  data-options="validType:'mobileorphone'"/>
                        </td>
                         </tr>
                         <tr>
                        <td class="LeftTd"><label class="LabName"><span>化解组织：</span></label>
                    		<input name="mediationOrgName" id="mediationOrgName" type="text" class="inp1 inp2 easyui-validatebox" value="<#if disputeMediation.mediationOrgName??>${disputeMediation.mediationOrgName}</#if>"  data-options="validType:['maxLength[50]','characterCheck']"/>
                        </td>
                        <td class="LeftTd"><label class="LabName"><span><label class="Asterik">*</label>化解是否成功：</span></label>
                        	<select id="isSuccess" name="isSuccess" class="sel1" style="width: 45px;">
			               <#if disputeMediation.isSuccess??>
			                      <option value="1" <#if ("1"==disputeMediation.isSuccess)>selected="selected"</#if>>是</option>
			                      <option value="0" <#if ("0"==disputeMediation.isSuccess)>selected="selected"</#if>>否</option>
			                <#else>
			                    <option selected="selected" value="1">是</option>
			                  <option value="0">否</option>
			              </#if>
			              </select>
                        </td>
                         </tr>
            		<tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span><label class="Asterik">*</label>化解情况：</span></label>
                        <textarea name="mediationResult" id="mediationResult" style="width: 432px; height: 45px;" cols="45" rows="3" class="area1 easyui-validatebox" maxLength="600" data-options="required:true,tipPosition:'bottom',validType:['maxLength[600]','characterCheck']"><#if disputeMediation.mediationResult??>${disputeMediation.mediationResult}</#if></textarea></td>
            		</tr>
                </table>

                    </div>

                </div>
	</div>

	<div class="BigTool">
    	<div class="BtnList">
     		<!--<a href="#" onclick="tableSubmit('saveAndReport');" class="BigNorToolBtn ShangBaoBtn">上报</a>
     		<a href="#" class="BigNorToolBtn BigJieAnBtn" onclick="tableSubmit('saveAndClose');">结案</a> -->
            <a href="###" class="BigNorToolBtn SaveBtn" onclick="javascript:tableSubmit();">保存</a>
    		<!--<a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancl();">取消</a>-->
        </div>
    </div>
    </form>
</div>
</body>

<script type="text/javascript">

    $(function(){
        AnoleApi.initTreeComboBox("mediationTypeStr", "mediationType", "B417", null<#if disputeMediation.mediationType??>, ["${disputeMediation.mediationType}"]</#if>);
    });

    function tableSubmit(){
        var isValid =  $("#tableForm").form('validate');
        if(isValid){
        	modleopen();
        	
            $("#tableForm").ajaxSubmit(function(data) {
            	modleclose();
            	
                if (data.success) {
                    if(parent.subTaskCallBack) {
                    	parent.subTaskCallBack();
                    }
                } else if(data.tipMsg) {
                	$.messager.alert('错误', data.tipMsg, 'error');
                }
            });
        }
    }
</script>
</html>