<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="x-ua-compatible" content="ie=8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>南安入格催办事件页面</title>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	 <@block name="fontColor">
	 </@block>  
</head>
<body>
	<form id="urgeEventForm" action="" method="post">
    	<input type="hidden" id="userIds" name="userIds" value="${userIds!}">
    	<input type="hidden" id="userNames" name="userNames" value="${userNames!}">
    	<input type="hidden" id="formId" name="formId" value="${formId!}">
    	<input type="hidden" id="taskId" name="taskId" value="${taskId!}">
    	<input type="hidden" id="instanceId" name="instanceId" value="<#if instanceId??>${instanceId?c}</#if>">
    	<input type="hidden" name="operateType" value="${operateType!}" />
    	<input type="hidden" id="smsContent" name="smsContent" value="" />
    	
		<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
			<div class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span><label class="Asterik">*</label>督办意见：</span></label><textarea name="remarks" id="remarks" cols="" rows="" class="area1 easyui-validatebox" data-options="required:true,validType:['maxLength[250]','characterCheck'],tipPosition:'bottom'" style="width:350px;height:110px;"></textarea>
						</td>
					</tr>
					<tr id="remindedTr">
						<td class="LeftTd">
							<label class="LabName"><span>被督办人员：</span></label>
							<div class="FontDarkBlue fl DealMan Check_Radio" style="width: 75%;"><b id="remindedUserNames"></b></div>
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
			    </table>
			</div>
		</div>
	    	
		<div class="BigTool">
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
		 <@block name="BaseWorkflow4UrgeHandle_initParam">
		if(typeof parent.BaseWorkflow4UrgeHandle_initParam === 'function') {
			initParamOperate(parent.BaseWorkflow4UrgeHandle_initParam());
		}
		</@block>  
		initUrge();
	});
	
    var defaultParam = {
    	initUrge: {
    		initUrgeUrl: '',
    		initUrgeCallback: null
    	},
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
		initUrge		: initUrge,
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
	
	function initUrge() {
		var initUrgeUrl = initParamObj.initUrge.initUrgeUrl;
		
		if(initUrgeUrl) {
			modleopen();
			
			$("#urgeEventForm").attr('action', initParamObj.initUrge.initUrgeUrl);
			
			$("#urgeEventForm").ajaxSubmit(function(data) {
				modleclose();
				var smsContent = '';
				
				if(data) {
					if(data.smsContent) {
						smsContent = data.smsContent;
					}
					
					if(data.remindedUserName) {
						$('#remindedUserNames').html(data.remindedUserName);
					}
				}
				
				$('#remarks').val(smsContent);
			});
		}
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
				
				$('#smsContent').val($('#remarks').val());
				
				$("#urgeEventForm").attr('action', initParamObj.addUrge.addUrgeUrl);
				
				$("#urgeEventForm").ajaxSubmit(function(data) {
					var addUrgeCallback = initParamObj.addUrge.addUrgeCallback;
					
					if(addUrgeCallback && typeof addUrgeCallback === 'function') {
						addUrgeCallback(data);
					} else {
						closeWin();
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
			var isValid =  $("#urgeEventForm").form('validate');
			
			if(isValid) {
				$("#otherMobile").show();
			} else {
				$("#sendSms_").attr("checked", false);
			}
		} else {
			$("#otherMobile").hide();
			$("#otherMobileNums").val(defaultMsg);
		}
	}
	
	 <@block name="closeWinFn"> 
	function closeWin() {
		var cancelUrgeCallback = initParamObj.cancelUrge.cancelUrgeCallback;
		
		if(cancelUrgeCallback && typeof cancelUrgeCallback === 'function') {
			cancelUrgeCallback();
		} else {
			parent.closeMaxJqueryWindow();
		}
	}
    	</@block>  
})(jQuery);
	
</script>
</html>