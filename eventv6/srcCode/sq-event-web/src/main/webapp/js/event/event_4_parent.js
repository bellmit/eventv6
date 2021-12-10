/**************************************************************************************************************
*引入该文件需要注意的地方
*1. 需要属性SQ_ZZGRID_URL
*2. 当前页需要引入maxJqueryEasyUIWin.ftl或者customEasyWin.ftl，前者优先
*3. 当前页需要引入zzgl_core.js
*4. 列表刷新方法统一为flashData()
*5. 引用页面需要调用eventInit对参数进行初始化
*6. 列表所在页面需要调用columnListSet对列表进行初始化
**************************************************************************************************************/
	
	var eventList = null;
	var eventContextPath = null;
	var eventSqZzgridUrl = null;
	var eventType = "";
	var eventModel = "";
	
	function columnListSet(list){//获取要处理的列表
		eventList = list;
	}
	
	function eventInit(path, sqZzgridUrl, type, model){
		eventContextPath = path;
		//eventSqZzgridUrl = sqZzgridUrl;
		eventSqZzgridUrl = path;
		
		if(type!=undefined && type!=model){
			eventType = type;
		}
		
		if(model!=undefined && model!=null){
			eventModel = model;
		}
	}
	
	function nextColumn(eventId) {//获取下一条事件
		return getNextOrPrevUrl("next", eventId);
	}
	
	function prevColumn(eventId) {//获取上一条事件
		return getNextOrPrevUrl("prev", eventId);
	}
	
	function getNextOrPrevUrl(type, eventId) {
		var index = -1;
		var rows = eventList;
		var length = rows.length;
		$.each(rows, function(i, row) {
			if (row.eventId == eventId) {
				index = i;
				return false;
			}
		});
		if (index != -1) {
			if (index == 0 && type == "prev") {
				return "";
			} else if (index == length - 1 && type == "next") {
				return "";
			} else {
				index = type == "prev" ? index - 1 : index + 1;
				var row = rows[index];
				var url = eventContextPath+"/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType="+ eventType +"&instanceId="+row.instanceId+"&workFlowId="+row.workFlowId+"&eventId="+row.eventId+"&taskId="+row.taskId+"&cachenum=" + Math.random();
				return url;
			}
		} else {
			throw new Error("当前页未找到事件eventId=" + eventId + "的记录！");
		}
	}
	
	
	//-- 供子页调用的提示方法
	function noReloadDataForSubPage(result){
		if(typeof(closeMaxJqueryWindow) != 'undefined'){//关闭当前窗口
			closeMaxJqueryWindow();
		}else if(typeof(closeCustomEasyWin) != 'undefined'){
			closeCustomEasyWin();
		}
		DivShow(result);
		
		if(typeof(flashData) != 'undefined'){
			flashData();//刷新列表
		}
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result, type, isCurrent){
		if(result!=undefined && result!=null && result!=''){//打印提示信息
			$.messager.alert('提示', result, 'info');
		}
		
		if(typeof(closeMaxJqueryWindow) != 'undefined'){//关闭当前窗口
			closeMaxJqueryWindow();
		}else if(typeof(closeCustomEasyWin) != 'undefined'){
			closeCustomEasyWin();
		}
		if(typeof(flashData) != 'undefined'){
			flashData(result, isCurrent);//刷新列表
		}
	}
	
	/**************************************************************************************************************
	*winType 父窗口类型 0为customEasyWin.ftl，空值无效
	*eventDetailUrlExtraParam 在data中 事件详情页面跳转额外参数 格式：'&isCapEventLabelInfo=true'
	*eventDetailWidth 在data中 设置事件详情页面的宽度
	*eventDetailHeight 在data中 设置事件详情页面的高度
	**************************************************************************************************************/
	function startWorkFlow(data, winType){//启动流程
		var formId = data.formId,
			new_workFlowId = data.workflowId,
			wftypeId = data.wftypeId,
			orgCode = data.orgCode,
			orgType = data.orgType,
			toClose = data.toClose,
			json = data.json,
			advice = data.advice,
			isCrossDomain = data.isCrossDomain,
			ajaxDataType = "json",
			ajaxUrl = eventSqZzgridUrl+'/zhsq/workflow/workflowController/startFlow.jhtml',
			eventDetailHeight = data.eventDetailHeight,
			eventDetailWidth = data.eventDetailWidth,
			eventDetailUrlExtraParam = data.eventDetailUrlExtraParam || '';
		
		if(isCrossDomain && isCrossDomain==true) {//跨域操作
			ajaxDataType = "jsonp";
			ajaxUrl = eventSqZzgridUrl+'/zhsq/workflow/workflowController/startFlow4Jsonp.jhtml?jsonpCallback=?';
		}
		
		if(formId == null || formId < 0) {
			$.messager.alert('错误', '缺少有效的事件信息！', 'error');
			return;
		}
		
		//启动流程
		$.ajax({
			//type: "POST",
			url : ajaxUrl,
			data: {'formId': formId ,'workFlowId': new_workFlowId,'wftypeId': wftypeId, 'orgCode': orgCode, 'orgType': orgType, 'toClose': toClose, 'advice':advice, '_t': Math.random()},
			dataType: ajaxDataType,
			success: function(data){
				modleclose();
				
				if(typeof flashData === 'function') {
					flashData();//刷新列表，以防点击取消
				}
				
			    if(data.result){
			    	var instanceId=data.instanceId;
			    	if(isNotBlankStringTrim(instanceId)){
				    	//alert("instanceId==="+instanceId);
						/*if(winType!=undefined && winType!=null && winType=='0' && eventModel=='c'){
							closeMaxJqueryWindow();
						}*/
						
						if(toClose == '0'){
		    				//var url = eventSqZzgridUrl+'/zhsq/workflow/workflowController/jumpPage.jhtml?instanceId='+instanceId+'&workFlowId='+new_workFlowId+'&json='+json+'&formId='+formId+'&eventType=event';
							var url = eventSqZzgridUrl+"/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=todo&instanceId="+instanceId+"&workFlowId="+new_workFlowId+"&json="+json+"&eventId="+formId+"&cachenum=" + Math.random();
							var winWidth = 900, winHeight = 400;
							
							if(eventDetailUrlExtraParam && typeof eventDetailUrlExtraParam === 'string' ){
								url += eventDetailUrlExtraParam;
							}
							
							if(eventDetailHeight){
								winHeight=eventDetailHeight;
								
							} else if(typeof fetchWinHeight == 'function' ) {
								winHeight = fetchWinHeight();
							}
							
							if(eventDetailWidth){
								winWidth=eventDetailWidth;
								
							}else if(typeof fetchWinWidth == 'function' ) {
								winWidth = fetchWinWidth();
							}
							
		    				//showMaxJqueryWindow("办理窗口", url);
		    				showMaxJqueryWindow("办理事件", url, winWidth, winHeight, true);
		    				if(typeof(closeBeforeMaxJqueryWindow) == 'function'){
								closeBeforeMaxJqueryWindow();
							}
	    				}else if(toClose == '1'){
	    					try{
	    						closeMaxJqueryWindow();//新增弹出窗口的关闭方法
	    					}catch(e){
	    					}
	    					$.messager.alert('提示','事件结案成功！','info');
	    				}
			    	}
			    }else{
			    	var resultMsg = data.msgWrong,
			    		msg = "事件启动失败！";
			    	
			    	if(resultMsg) {
			    		msg = resultMsg;
			    	}
			    	
			    	try{
  						closeMaxJqueryWindow();//新增弹出窗口的关闭方法
  					}catch(e){
  					}
  					
			    	$.messager.alert('错误', msg, 'error');
			    }
			},
			error:function(data){
				$.messager.alert('错误', '未启动成功！', 'error');
			}
		});
	}