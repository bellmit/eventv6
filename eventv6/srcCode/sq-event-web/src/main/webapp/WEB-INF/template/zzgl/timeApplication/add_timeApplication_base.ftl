<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><@block name="pageTitle">申请新增页面</@block></title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<@block name="extendHeadInclude"></@block>
</head>
<body>
	<form id="timeApplicationForm" action="" method="post">
		<input type="hidden" name="applicationType" value="${applicationType!}" />
		<input type="hidden" name="businessId" value="<#if businessId??>${businessId?c}</#if>" />
		<input type="hidden" name="businessKeyId" value="<#if businessKeyId??>${businessKeyId?c}</#if>" />
		<input type="hidden" name="auditorId" value="<#if auditorId??>${auditorId?c}</#if>" />
		<input type="hidden" name="auditorOrgId" value="<#if auditorOrgId??>${auditorOrgId?c}</#if>" />
		<input type="hidden" name="auditorType" value="${auditorType!}" />
		<input type="hidden" name="isAutoAudit" value="${isAutoAudit!}" />
		<input type="hidden" name="serviceName" value="${serviceName!}" />
		<input type="hidden" name="timeAppCheckStatus" value="${timeAppCheckStatus!}" />
		<input type="hidden" name="defaultTipMsg" value="${defaultTipMsg!}" />
		<input type="hidden" id="defaultIntervalUnit" value="${defaultIntervalUnit!1}" />
		<!--isDuplicatedCheck 传入的是boolean类型，不是字符串-->
		<input type="hidden" name="isDuplicatedCheck" value="<#if isDuplicatedCheck??>${isDuplicatedCheck?c}</#if>" />
		<!--短信模板-->
		<input type="hidden" id="noteType" name="noteType" value="${noteType!}" />
		
		<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
			<div class="NorForm">
				<table id="timeApplicationTable" width="100%" border="0" cellspacing="0" cellpadding="0">
					<@block name="timeAppIntervalTr"></@block>
					<tr id="timeApplicationAdviceTr">
						<td class="LeftTd" <@block name="timeApplicationAdviceColoumnSpan"></@block>>
							<@block name="reasonLabel"><label class="LabName"><span>办理意见：</span></label></@block><textarea name="reason" id="reason" cols="" rows="" class="area1 easyui-validatebox" data-options="required:true,validType:['maxLength[200]','characterCheck'],tipPosition:'bottom'" style="width:362px;height:90px;"></textarea>
						</td>
					</tr>
					<tr <#if isShowSendMsg?? && isShowSendMsg><#else>class="hide"</#if>>
						<td class="LeftTd">
							<label class="LabName"><span>发送短信：</span></label>
							<div class="Check_Radio"><input type='checkbox' id='sendSms_' onclick="showSmsCont();"/></div>
							<div id="otherMobile" style="display:none;">
								<input type="text" id="otherMobileNums" name="otherMobileNums" class="inp1" style="width:340px;" title="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔" placeholder="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔" />
							</div>
						</td>
					</tr>
					<tr id="sendCont" style="display:none;">
						<td class="LeftTd">
							<label class="LabName"><span>短信内容：</span></label><textarea rows="3"  id="smsContent" name="smsContent" class="area1 easyui-validatebox" data-options="validType:['maxLength[500]','characterCheck'],tipPosition:'top'"  style="width:362px;height:75px;"></textarea>
						</td>
					</tr>
			    </table>
			</div>
		</div>
	    	
		<div class="BigTool">
        	<div class="BtnList">
		        <a href="#" onclick="addInspect();" class="BigNorToolBtn BigShangBaoBtn">提交</a>
		        <a href="#" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
            </div>
        </div>	
	</form>
</body>

<script type="text/javascript">
	$(function() {
		var msgWrong = "${msgWrong!}";
		
		if(msgWrong) {
			$('#timeApplicationForm').html('<div class="FontRed" style="text-align: center;"><b>' + msgWrong + '</b></div>');
			
			return;
		}
		
		var options = {
			axis : "yx", 
			theme : "minimal-dark" 
		};
		
		enableScrollBar('content-d',options); 
		
		<@block name="extendInit"></@block>
	});
	
	function addInspect() {
		var msgWrong = "${msgWrong!}";
		
		if(msgWrong) {
			$.messager.alert('错误', msgWrong, 'error');
		} else {
			var isValid =  $("#timeApplicationForm").form('validate');
	      	
	      	<@block name="extraCheck"></@block>
	      	
	      	if(isValid) {
	      		modleopen();
	      		
	      		$("#timeApplicationForm").attr("action","${rc.getContextPath()}/zhsq/timeApplication/saveTimeApplication.jhtml");
	      		
	      		$("#timeApplicationForm").ajaxSubmit(function(data) {
	      			modleclose();
	      			
	      			if(data.success) {
	      				parent.reloadDataForSubPage(data.tipMsg, true);
	      			} else {
	      				var tipMsg = data.tipMsg || '操作失败！';
	      				
	      				$.messager.alert('错误', tipMsg, 'error');
	      			}
	      		});
	      	}
      	}
	}
	
	function showSmsCont() {
        var isValid =  $("#remindForm").form('validate');
        if(isValid) {
            if($("#sendSms_").attr("checked")) {
                modleopen();
                $.ajax({
                    type: "POST",
                    url : '${rc.getContextPath()}/zhsq/workflow/workflowController/eventNoteContent.jhtml',
                    data: {'noteType': $('#noteType').val()},
                    dataType:"json",
                    success: function(data){
                        if(data.msgWrong) {
                            $.messager.alert('错误', data.msgWrong, 'error');
                            $("#sendSms_").attr("checked", false);
                        } else {
                            var smsContent = data.smsContent;
                            smsContent = smsContent.replace('@advice',$("#remarks").val());
                            $("#smsContent").val(smsContent);
                            $("#sendCont").show();
                            $("#otherMobile").show();
                        }
                        modleclose();
                    },
                    error:function(data){
                        modleclose();
                        $.messager.alert('错误','连接错误！','error');
                        $("#sendSms_").attr("checked", false);
                    }
                });
            } else {
                $("#sendCont").hide();
                $("#otherMobile").hide();
            }
        } else {
            $("#sendSms_").attr("checked", false);
        }
    }
    
	function closeWin() {
		parent.closeMaxJqueryWindow();
	}
	
	<@block name="extraJavaScript"></@block>
</script>
</html>