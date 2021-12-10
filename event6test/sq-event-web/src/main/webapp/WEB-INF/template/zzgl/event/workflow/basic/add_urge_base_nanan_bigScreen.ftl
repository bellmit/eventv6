<@override name="BaseWorkflow4UrgeHandle_initParam">  
	var initObj = {
			initUrge: {
<#if reportType??>
initUrgeUrl: "${rc.getContextPath()}/zhsq/reportFocus/capSmsContent.jhtml?smsTemplateName=remindNote-reportFocus-${reportType}&reportType=${reportType}&formId=${eventId}&instanceId=${instanceId}&operateType=1"
<#else>
initUrgeUrl: "${rc.getContextPath()}/zhsq/workflow/workflowController/eventNoteContent.jhtml?noteType=remindNote-reportFocus&formId=${eventId}&instanceId=${instanceId}&operateType=1&isCapRemindedUser=true"
</#if>						
},
			addUrge: {
<#if reportType??>
addUrgeUrl: '${rc.getContextPath()}/zhsq/workflow4Base/addUrgeOrRemind.jhtml',
<#else>
addUrgeUrl: "${rc.getContextPath()}/zhsq/event/eventDisposalController/addRemind.jhtml",
</#if>	
	    		

	    		addUrgeCallback: function(data){
					var msg = "操作失败！";
		
					if(data.result && data.result == true) {
						var msg = "催办成功！";
						
						if(data.operateType == 1) {
							msg = "督办成功！";
						}
					} else if(data.msgWrong) {
						msg = data.msgWrong;
					}
window.parent.postMessage(' dbTableSubmitBack("'+msg+'")','${backUrl}');
				}
	    	},
	    	sendSms: {
	    		isSendSms: true,
	    		isRemote: false
	    	}
    	};
    initParamOperate(initObj);  
</@override>  


<@override name="closeWinFn">  
	function closeWin(){ 
		window.parent.postMessage('cancelFocusDuban()','${backUrl}');
	}
</@override> 
<@override name="fontColor">  
<#if color?? && color='black'>
<style>
	.LabName span{color: #7aa9ff;}
	.NorForm td{border-bottom: 1px dotted rgba(0, 132, 255, .4);}
.BigTool {background: rgba(0,0,0,0);}
</style>
</#if>
</@override> 
<@extends name="/zzgl/reportFocus/base/add_urge_base.ftl"/> 