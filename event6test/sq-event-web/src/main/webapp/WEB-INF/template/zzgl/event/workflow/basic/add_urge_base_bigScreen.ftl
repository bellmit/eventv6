<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="x-ua-compatible" content="ie=8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>催办事件通用页面</title>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<#include "/component/commonFiles-1.1.ftl" />

</head>
<body>
	<form id="urgeEventForm" action="" method="post">
    	<input type="hidden" id="userIds" name="userIds" value="${userIds!}">
    	<input type="hidden" id="userNames" name="userNames" value="${userNames!}">
    	<input type="hidden" id="formId" name="formId" value="<#if formId??>${formId?c}</#if>">
    	<input type="hidden" id="taskId" name="taskId" value="<#if taskId??>${taskId?c}</#if>">
    	<input type="hidden" id="instanceId" name="instanceId" value="<#if instanceId??>${instanceId?c}</#if>">
    	<input type="hidden" name="operateType" value="${operateType!}" />
    	
		<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
			<div class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span><label class="Asterik">*</label>办理意见：</span></label><textarea name="remarks" id="remarks" cols="" rows="" class="area1 easyui-validatebox" data-options="required:true,validType:['maxLength[250]','characterCheck'],tipPosition:'bottom'" style="width:350px;height:50px;"></textarea>
						</td>
					</tr>
			    	<tr id="sendSmsTr" class="hide">
						<td class="LeftTd">
							<label class="LabName"><span>发送短信：</span></label>
							<div class="Check_Radio">
								<input type='checkbox' id='sendSms_' onclick="BaseWorkflow4UrgeHandle.showSmsContent();"/>
							</div>
							<div id="otherMobile" class="hide">
								<input type="text" id="otherMobileNums" name="otherMobileNums" class="inp1" style="width:335px;" value="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔" />
							</div>
						</td>
					</tr>
			    	<tr id="sendCont" class="hide">
						<td class="LeftTd">
							<label class="LabName"><span><label class="Asterik">*</label>短信内容：</span></label><textarea rows="3" id="smsContent" name="smsContent" class="area1 easyui-validatebox" data-options="validType:['maxLength[500]','characterCheck'],tipPosition:'top'" style="width:350px;height:75px;"></textarea>
						</td>
					</tr>
			    </table>
			</div>
		</div>
	    	
		<div class="BigTool" <#if color??>style="background:none"</#if>>
        	<div class="BtnList">
		        <a href="###" onclick="BaseWorkflow4UrgeHandle.addUrge();" class="BigNorToolBtn BigShangBaoBtn">提交</a>
		        <a href="###" onclick="BaseWorkflow4UrgeHandle.cancelUrge();" class="BigNorToolBtn CancelBtn">取消</a>
            </div>
        </div>	
	</form>
</body>

<script type="text/javascript">
var defaultMsg='可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔';
		
(function($){ 
	$(function() {
		var options = {
			axis : "yx", 
			theme : "minimal-dark" 
		};
		
		$('#otherMobileNums')
		.focus(function(){  
			if(defaultMsg == $('#otherMobileNums').val()) {
				$('#otherMobileNums').val("");
			}  
		})
		.blur(function(){
			if($('#otherMobileNums').val() == "") {
				$('#otherMobileNums').val(defaultMsg);
			}
		});
		
		enableScrollBar('content-d',options); 
		
		if(typeof BaseWorkflow4UrgeHandle_initParam === 'function') {
			initParamOperate(BaseWorkflow4UrgeHandle_initParam());
		}
	});

	//督办、催办初始化方法
	function BaseWorkflow4UrgeHandle_initParam() {
		var initObj = {
			addUrge: {
	    		addUrgeUrl: '${rc.getContextPath()}/zhsq/workflow4Base/addUrgeOrRemind.jhtml',
	    		addUrgeCallback: remindCallback
	    	},
	    	sendSms: {
	    		smsContentUrl: "${rc.getContextPath()}/zhsq/reportFocus/capSmsContent.jhtml?smsTemplateName=remindNote-reportFocus-" + $('#reportType').val(),
	    		isSendSms: true,
	    		isRemote: true
	    	}
    	};
    	return initObj;
	}

	function remindCallback() {
		console.log("123");
	}
	
    var defaultParam = {
    	addUrge: {
    		addUrgeUrl: '',
    		addUrgeCallback: null
    	},
    	sendSms: {
    		smsContentUrl: null,
    		isSendSms: false,
    		isRemote: false
    	},
    	cancelUrge: {
    		cancelUrgeCallback: null
    	}
    },
    initParamObj = $.extend({}, defaultParam);
	
    //对外开放方法
	BaseWorkflow4UrgeHandle = {
		initParam 		: initParamOperate,
		addUrge			: addUrge,
		showSmsContent	: showSmsContent,
		cancelUrge		: closeWin
	};
	
	//参数初始化设置
	function initParamOperate(option, param) {
		var result = "";
		
		if(!param) {
			param = initParamObj;
		}
		
		for(var index in option) {
			if(typeof option[index] === 'object') {
				param[index] = initParamOperate(option[index], param[index]);
			} else {
				result = (option[index] ? option[index] : defaultParam[index]) || '';
				
				param[index] = result;//通过变量result，防止initParamObj对defaultParam的引用
			}
		}
		
		if(param.sendSms && param.sendSms.isSendSms) {
			$("#sendSmsTr").show();
		}
		
		return param;
	}
	
	function addUrge() {
		var addUrgeUrl = initParamObj.addUrge.addUrgeUrl;
		
		if(addUrgeUrl) {
			var isValid =  $("#urgeEventForm").form('validate');
			
			if(isValid) {
				modleopen();
				
				var otherMobileNums = $('#otherMobileNums').val();
				if(otherMobileNums == defaultMsg) {
					$('#otherMobileNums').removeAttr('name');
				}
				
				$("#urgeEventForm").attr('action', initParamObj.addUrge.addUrgeUrl);
				
				$("#urgeEventForm").ajaxSubmit(function(data) {
					if(data.result){
						window.parent.postMessage('dbTableSubmitBack()','${backUrl}');
	                }else{
	                    modleclose();
	                    $.messager.alert('错误','督办失败！','error');
	                }
				});
			}
		} else {
			$.messager.alert('警告','请设置催办请求，设置属性为[addUrge.addUrgeUrl]！','warning');
		}
	}
	
	function showSmsContent() {
		var isChecked = $("#sendSms_").attr("checked"),
			smsContentUrl = initParamObj.sendSms.smsContentUrl,
			isRemote = initParamObj.sendSms.isRemote;
		
		if(isChecked) {
			if(isRemote) {
				if(isBlankStringTrim(smsContentUrl)) {
					$.messager.alert('警告','请设置获取短信内容请求，设置属性为[sendSms.smsContentUrl]！','warning');
				} else {
					var isValid =  $("#urgeEventForm").form('validate');
					
					if(isValid) {
						modleopen();
						
						var otherMobileNums = $('#otherMobileNums').val();
						if(otherMobileNums == defaultMsg) {
							$('#otherMobileNums').removeAttr('name');
						}
						
						$("#urgeEventForm").attr('action', smsContentUrl);
						
						$("#urgeEventForm").ajaxSubmit(function(data) {
							var smsContent = data.smsContent;
							
							$('#otherMobileNums').attr('name', 'otherMobileNums');
							
							if(smsContent) {
								smsContent = smsContent.replaceAll('@advice', $("#remarks").val());
							}
							
							$('#smsContent').validatebox({
								required: true
							});
							$("#smsContent").val(smsContent);
							$("#sendCont").show();
							$("#otherMobile").show();
							modleclose();
						});
					} else {
						$("#sendSms_").attr("checked", false);
					}
				}
			} else {
				$('#smsContent').validatebox({
					required: true
				});
				$("#sendCont").show();
				$("#otherMobile").show();
			}
		} else {
			$("#sendCont").hide();
			$("#otherMobile").hide();
			$("#sendCont").val('');
			$("#otherMobileNums").val(defaultMsg);
			$('#smsContent').validatebox({
				required: false
			});
		}
	}
	
	function closeWin() {
		/* var cancelUrgeCallback = initParamObj.cancelUrge.cancelUrgeCallback;
		
		if(cancelUrgeCallback && typeof cancelUrgeCallback === 'function') {
			cancelUrgeCallback();
		} else {
			parent.closeMaxJqueryWindow();
		} */
		window.parent.postMessage('cancelFocusDuban()','${backUrl}');
	}
    
})(jQuery);
	
</script>
</html>