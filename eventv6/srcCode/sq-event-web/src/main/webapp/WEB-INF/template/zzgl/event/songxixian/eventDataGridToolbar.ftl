<@override name="exclusiveFunction">
	function turn2scevent() {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示', '请选中要转案件的事件记录!', 'warning');
			return;
		}
		
		modleopen();
		
		$.ajax({
				type: "POST",
	    		url : '${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/fetchEventInfo4Dock.jhtml',
				data: {'eventId': idStr, 'isCapAttachment': true},
				dataType:"json",
				success: function(data){
					modleclose();
					
					var url = '${SC_EVENT_URL}/scEvent/eventCase/toAddEventCaseByJson.jhtml',
						  reportWinId = openJqueryWindowByParams({
						  		title: "事件转案件"
						  }),
						  reportForm = '<form id="_report4SCEventForm" name="_report4SCEventForm" action="" target="'+ reportWinId +'_iframe" method="post"></form>',
						  eventCaseJson = {
						  		"eventId"			: data.eventId,
						  		"eventCode"		: data.code,
						  		"caseOrigin"		: 2,
						  		"caseDateStr"	: data.happenTimeStr,
						  		"caseAddr"		: data.occurred,
						  		"caseIntro"		: data.content,
						  		"infoOrgCode"	: data.gridCode,
						  		"informant"		: {
						  				"perUserName"		: data.contactUser,
						  				"perPhone"			: data.tel
						  		},
						  		"extraParam"	: {
						  				"iframeUrl"			: "${SQ_EVENT_URL}/zhsq/event/eventDisposalController/isDomain.jhtml?",
						  				"iframeCloseCallback"	: "parent.parent.turn2sceventCallback",
						  				"isShowSaveBtn"			: false,
						  				"isShowCancelBtn"		: "false"
						  		},
						  		"isThrowExceptionDirectly"	: false
						  },
						  attachmentMapList = data.attachmentMapList;
						  
					
					if(attachmentMapList && attachmentMapList.length > 0) {
						var attachment = [],
						      attchmentObj = null;
						      
						for(var index in data.attachmentMapList) {
							attchmentObj = {};
							attchmentObj.attachmentName = attachmentMapList[index].attachmentName;
							attchmentObj.attachmentPath = attachmentMapList[index].attachmentPath;
							attchmentObj.attachmentSize = attachmentMapList[index].attachmentSize;
							
							attachment.push(attchmentObj);
						}
						
						eventCaseJson.attachment = attachment;
					}
					
					$("#jqueryToolbar").append($(reportForm));
					$("#_report4SCEventForm").append($('<input type="hidden" id="_reportSCEventJson" name="eventCaseJson" value="" />'));
					
					$("#_reportSCEventJson").val(JSON.stringify(eventCaseJson));
					$("#_report4SCEventForm").attr('action', url);
					
					$("#_report4SCEventForm").submit();
					
					$("#_report4SCEventForm").remove();
				},
				error:function(data){
					modleclose();
					
					$.messager.alert('错误','获取事件信息失败！','error');
				}
	    	});
	}
	
	function turn2sceventCallback(data) {
		var result = data.result;
		
		if(result == "success") {
			var url = '${SC_EVENT_URL}/scEvent/eventCase/initTask.jhtml?isDomain=false&caseId=' + data.caseId;
			openJqueryWindowByParams({
				title			: "案件办理",
				targetUrl	: url			
			});
			
			closeBeforeMaxJqueryWindow();
			
			<#if model?? && model == 'l'>
				loadMessage((pageNum?pageNum:1),$('#pageSize').val());
			<#else>
				flashData(true);
			</#if>
		} else {
			var message = data.message || '事件转案件操作失败！';
			
			closeMaxJqueryWindow();
			
			$.messager.alert('错误', message, 'error');
		}
	}
</@override>

<@extends name="/zzgl/event/eventDataGridToolbar.ftl" />