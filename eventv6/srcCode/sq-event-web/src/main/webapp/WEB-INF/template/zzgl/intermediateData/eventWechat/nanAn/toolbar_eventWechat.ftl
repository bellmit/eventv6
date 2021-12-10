<!--事件操作审核完某个入格事件后会赋值--> 
<@override name = "reportType">
     var reportType = '';
</@override>

<!--根据事件类型，跳转至不同表单新增入格事件--> 
<@override name = "jumpPageByEventType">

	 var url = '';
	 var eventType = event.eventType;
	 var title = '';
	 
	 if(eventType == '99'){//其他事件
	 
	 	url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByMenu.jhtml';
	 	title = '上报事件信息';
	 	
	 }else if(eventType == '01'){//两违防治（群众举报）
	 
	 	url = '${rc.getContextPath()}/zhsq/reportTwoVioPre/toAdd.jhtml?listType=1&dataSource=05';
	 	title = '上报两违事件信息';
	 
	 }else if(eventType == '02'){//房屋安全隐患（群众举报）
	 
	 	url = '${rc.getContextPath()}/zhsq/reportHHD/toAdd.jhtml?listType=1&dataSource=02';
	 	title = '上报房屋安全隐患信息';
	 
	 }else if(eventType == '03'){//企业安全生产（企业安全管理员）
	 
	 	url = '${rc.getContextPath()}/zhsq/reportEHD/toAdd.jhtml?listType=1&dataSource=02';
	 	title = '上报企业安全生产信息';
	 
	 }else if(eventType == '04'){//流域水质（群众举报）
	 	
	 	url = '${rc.getContextPath()}/zhsq/reportWQ/toAdd.jhtml?listType=1&matterType=1&dataSource=03';
	 	title = '上报流域水质信息';
	 
	 }else if(eventType == '05'){//森林防灭火（群众举报）
	 	
	    url = '${rc.getContextPath()}/zhsq/reportFFP/toAdd.jhtml?listType=1&dataSource=05';
	    title = '上报森林防灭火信息';
	    
	 }else if(eventType == '06'){//营商环境
	 	
	    url = '${rc.getContextPath()}/zhsq/reportBusPro/toAdd.jhtml?listType=1&dataSource=02';
	    title = '上报营商环境信息';
	 }
	 
</@override>

<!--跳转至不同表单新增入格事件 -->
<@override name="reportEventWechatOption">
	
	{
	  		maxWidth: 1000,
	  		maxHeight: 500,
	  		title: title,
	  		targetUrl: url
	}
</@override>

<!--构造入格表单信息  --> 
<@override name="reportEvtParam">

	 //var advice = event.advice;
	 //var contactUser = event.contactUser;
	 //var eventName = event.eventName;
	 //var gridId = event.gridId;
	 //var tel = event.tel;
	 var tipoffContent = event.content;
	 var regionName = event.gridName;
	 var reportTimeStr = event.happenTimeStr;
	 var regionCode = event.infoOrgCode;
	 var occurred = event.occurred;
	 var outerAttachmentIds = event.outerAttachmentIds;
	 var longitude = event.longitude;
	 var latitude = event.latitude;
	 
	 //标识为审核模块跳转至上报页面
	 $("#_report4eventForm").append($('<input type="hidden" id="isEventVerify" name="isEventVerify" value="1" />'));
	 //默认报告方式：微信公众号
	 $("#_report4eventForm").append($('<input type="hidden" id="reportWayName" name="reportWayName" value="微信公众号" />'));
	 $("#_report4eventForm").append($('<input type="hidden" id="reportWay" name="reportWay" value="3" />'));
	 //入格事件上报提交成功后，回调更新审核状态、同步事件Id方法
	 $("#_report4eventForm").append($('<input type="hidden" id="callBack" name="callBack" value="parent.reportCallBack" />'));
	
	 //所属区域
	 $("#_report4eventForm").append($('<input type="hidden" id="regionCode" name="regionCode" value="" />'));
	 $("#regionCode").val(regionCode);
	 
	 //区域名称
	 $("#_report4eventForm").append($('<input type="hidden" id="regionName" name="regionName" value="" />'));
	 $("#regionName").val(regionName);
	 
	 //发生地址
	 $("#_report4eventForm").append($('<input type="hidden" id="occurred" name="occurred" value="" />'));
	 $("#occurred").val(occurred);
	 
	 //内容
	 $("#_report4eventForm").append($('<input type="hidden" id="tipoffContent" name="tipoffContent" value="" />'));
	 $("#tipoffContent").val(tipoffContent);
	 
	 //报告时间
	 $("#_report4eventForm").append($('<input type="hidden" id="reportTimeStr" name="reportTimeStr" value="" />'));
	 $("#reportTimeStr").val(reportTimeStr);
	 
	 //经度
	 $("#_report4eventForm").append($('<input type="hidden" id="longitude" name="longitude" value="" />'));
	 $("#longitude").val(longitude);
	 
	 //纬度
	 $("#_report4eventForm").append($('<input type="hidden" id="latitude" name="latitude" value="" />'));
	 $("#latitude").val(latitude);
	 
	 //附件Id
	 $("#_report4eventForm").append($('<input type="hidden" id="outerAttachmentIds" name="outerAttachmentIds" value="" />'));
	 $("#outerAttachmentIds").val(outerAttachmentIds);
	 
	 
	 //附件类型
	 $("#_report4eventForm").append($('<input type="hidden" id="attachmentType" name="attachmentType" value="" />'));
	
	 if(eventType == '01'){//两违防治
	 
	 	$("#attachmentType").val("TWO_VIO_PRE");
	 	reportType = '1';
	 
	 }else if(eventType == '02'){//房屋安全隐患
	 
	 	$("#attachmentType").val("HOUSE_HIDDEN_DANGER");
	 	reportType = '2';
	 
	 }else if(eventType == '03'){//企业安全生产
	 
	 	$("#attachmentType").val("ENTERPRISE_HIDDEN_DANGER");
	 	reportType = '3';
	 
	 }else if(eventType == '04'){//流域水质
	 	
	 	$("#attachmentType").val("WATER_QUALITY");
	 	reportType = '5';
	 
	 }else if(eventType == '05'){//森林防灭火
	 
	 	$("#attachmentType").val("FOREST_FIRE_PREVENTION");
	 	reportType = '9';
	 	
	 }else if(eventType == '06'){//营商环境
	 
	 	$("#attachmentType").val("BUSINESS_PROBLEM");
	 	reportType = '10';
	 	
	 }
	 
</@override>

<!--详情 --> 
<@override name="detailFunctionBlock">

	//事件审核完某个入格事件后，提交入格表单，跳转至对应详情页面进行办理
	function detail(reportUUID, instanceId, listType) {
		if(reportUUID) {
			
			var url = '';
			var title = '';
			
			if(reportType == '1'){//两违防治
			 
			 	url = '${rc.getContextPath()}/zhsq/reportTwoVioPre/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=' + listType + '&reportType=' + reportType;
			 	title = '查看两违事件信息';
			 
			 }else if(reportType == '2'){//房屋安全隐患
			 
			 	url = '${rc.getContextPath()}/zhsq/reportHHD/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=' + listType + '&reportType=' + reportType;
			 	title = '查看房屋安全隐患信息';
			 
			 }else if(reportType == '3'){//企业安全生产
			 
			 	url = '${rc.getContextPath()}/zhsq/reportEHD/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=' + listType + '&reportType=' + reportType;
			 	title = '查看企业安全生产信息';
			 
			 }else if(reportType == '5'){//流域水质
			 	
			 	url = '${rc.getContextPath()}/zhsq/reportWQ/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=' + listType + '&reportType=' + reportType;
			 	title = '查看流域水质信息';
			 
			 }else if(reportType == '9'){//森林防灭火
			 	
			    url = '${rc.getContextPath()}/zhsq/reportFFP/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=' + listType + '&reportType=' + reportType;
			    title = '查看森林防灭火信息';
			    
			 }else if(reportType == '10'){//营商环境
			 	
			    url = '${rc.getContextPath()}/zhsq/reportBusPro/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=' + listType + '&reportType=' + reportType;
			    title = '查看营商环境信息';
			    
			 }
			
			if(instanceId) {
				url += "&instanceId=" + instanceId;
			}
			
			openJqueryWindowByParams({
				maxWidth: 900,
				maximizable: true,
				title: title,
				targetUrl: url
			});
		} else {
			$.messager.alert('警告','请选择需要查看的记录!','warning');
		}
	}
	
	//列表点击标题，展示审核信息详情
	function detailToEventVerify(eventVerifyHashId) {
		var opt = {
			'maxHeight': 410,
			'maxWidth': 720
		};
		var url = '${rc.getContextPath()}/zhsq/eventWechat/toDetail.jhtml?eventVerifyHashId=' + eventVerifyHashId + '&verifyType',
			verifyType = $('#verifyType').val();
		
		if(isNotBlankStringTrim(verifyType)) {
			url += '&verifyType=' + verifyType;
		}
		
		opt.title = "查看事件信息";
		opt.targetUrl = url;
		
		openJqueryWindowByParams(opt);
	}
</@override>

<!--审核详情页查看事件进度 -->
<@override name="showEvent">

    function showEventWin(eventId , reportUUID ,reportType) {
    
		if(eventId) {
			
			var url = '';
			var title = '查看事件信息';
			
			 if(reportType == '1'){//两违防治
			 
			 	url = '${rc.getContextPath()}/zhsq/reportTwoVioPre/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=5&reportType=' + reportType;
			 	title = '查看两违事件信息';
			 
			 }else if(reportType == '2'){//房屋安全隐患
			 
			 	url = '${rc.getContextPath()}/zhsq/reportHHD/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=5&reportType=' + reportType;
			 	title = '查看房屋安全隐患信息';
			 
			 }else if(reportType == '3'){//企业安全生产
			 
			 	url = '${rc.getContextPath()}/zhsq/reportEHD/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=5&reportType=' + reportType;
			 	title = '查看企业安全生产信息';
			 
			 }else if(reportType == '5'){//流域水质
			 	
			 	url = '${rc.getContextPath()}/zhsq/reportWQ/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=5&reportType=' + reportType;
			 	title = '查看流域水质信息';
			 
			 }else if(reportType == '9'){//森林防灭火
			 	
			    url = '${rc.getContextPath()}/zhsq/reportFFP/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=5&reportType=' + reportType;
			    title = '查看森林防灭火信息';
			    
			 }else if(reportType == '10'){//营商环境
			 	
			    url = '${rc.getContextPath()}/zhsq/reportBusPro/toDetail.jhtml?reportUUID=' + reportUUID + '&listType=5&reportType=' + reportType;
			    title = '查看营商环境信息';
			    
			 }else if(!reportType){
			 	url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId=' + eventId;
			 }
			
			var opt = 
		
			{
				title: title,
				targetUrl: url
			};
			
			openJqueryWindowByParams(opt);
		}
	}

</@override>

<!--回调更新审核记录状态、事件Id-->
<@override name="callbackData">
	if(callbackData.eventId) {
		ajaxData.eventId = callbackData.eventId;
	}else if (callbackData.reportId){
		ajaxData.eventId = callbackData.reportId;		
	}
</@override>

<@extends name="/zzgl/intermediateData/eventWechat/toolbar_eventWechat.ftl" />