<@override name="extendCondition">
	<#if eventType?? && eventType=='all'>
	<tr>
		<td><label class="LabName width65px"><span>举报人员：</span></label><input class="inp1 queryParam" type="text" name="informant" style="width:135px;"></input></td>
	</tr>
	<tr>
		<td><label class="LabName width65px"><span>举报电话：</span></label><input class="inp1 queryParam" type="text" name="informantTel" style="width:135px;"></input></td>
	</tr>
	</#if>
</@override>
<@override name="invalidApplicationExtraAttribute">
	$("#_report4eventForm").append($('<input type="hidden" name="serviceName" value="timeApplicationCallback4SCUMYPQInvalidEventService" />'));
	
	if(invalidType == '61') {
		$("#_report4eventForm").append($('<input type="hidden" name="applicationType" value="61" />'));
	}
</@override>

<@override name="eventListPatternChange"></@override>

<@override name="addTimeApplicationFunctionOpenMethod">
	showMaxJqueryWindow("申请延时", url, 480, 410);
</@override>

<@override name="invalidFunctionOpenMethod">
	var reportWinId = openJqueryWindowByParams({
					  	maxWidth: 480,
					  	maxHeight: 330,
					  	title: operateName + "事件"
					  });
</@override>

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
				data: {'eventId': idStr, 'isCapAttachment': true, 'isCapMapInfo': true},
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
						  		"mapInfo"		: {
						  			"longitude"			: data.longitude,
						  			"latitude"			: data.latitude,
						  			"mapType"			: data.mapType
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
				title		: "案件办理",
				targetUrl	: url			
			});
			
			closeBeforeMaxJqueryWindow();
			
			force2End(data, true);
		} else {
			var message = data.message || '事件转案件操作失败！';
			
			closeMaxJqueryWindow();
			
			$.messager.alert('错误', message, 'error');
		}
	}
	
	function force2End(sceventData, isReload) {
		if(idStr==null || idStr=="") {
			$.messager.alert('提示', '请选中要操作的事件记录!', 'warning');
			return;
		}
		
		var selectedEvent = $("#list").datagrid("getSelected");
		sceventData = sceventData || {};
		
		if(selectedEvent != null) {
			var dataOpt = {
				'eventId': idStr,
				'instanceId': selectedEvent.instanceId,
				'taskId': selectedEvent.taskId,
				'advice': '事件已转案件处理。',
				'nextNode': 'end1',
				'isForce2End': true
			};
			
			dataOpt = $.extend(sceventData, dataOpt);
			
			if(dataOpt.caseId && dataOpt.taskId) {
				modleopen();
				
				$.ajax({
					type: "POST",
					url : '${rc.getContextPath()}/zhsq/workflow/workflowController/submitInstanceForEventPC.jhtml',
					data: dataOpt,
					dataType:"json",
					success: function(data) {
						modleclose();
						
						if(isReload) {
							<#if model?? && model == 'l'>
								loadMessage((pageNum?pageNum:1), $('#pageSize').val());
							<#else>
								flashData(true);
							</#if>
						}
					},
					error: function(data) {
						modleclose();
						
						$.messager.alert('错误', '事件办结失败！', 'error');
					}
				});
			}
		}
	}
</@override>

<@extends name="/zzgl/event/eventDataGridToolbar.ftl" />