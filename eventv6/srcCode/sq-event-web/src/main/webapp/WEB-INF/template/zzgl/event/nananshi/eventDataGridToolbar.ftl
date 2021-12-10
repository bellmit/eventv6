<@override name="extendCondition">
	<tr <#if eventType == 'draft' || eventType == 'todo'>class="hide"</#if>>
		<td>
			<label class="LabName width65px"><span>档案采用：</span></label>
			<input id="isAdopted" name="isAdopted" type="text" class="queryParam hide"/>
			<input id="isAdoptedName" type="text" class="inp1 selectWidth" />
		</td>
	</tr>
</@override>

<@override name="extendConditionInit">
	if($('#isAdopted').length > 0) {
		AnoleApi.initListComboBox("isAdoptedName", "isAdopted", null, null, null, {
			DataSrc: [{"name":"是", "value":"1"},{"name":"否", "value":"0"}],
			ShowOptions: {
				EnableToolbar : true
			}
		});
	}
</@override>

<@override name="exclusiveFunction">
	function addSpecifiedEvent(){
		var url = "${rc.getContextPath()}/zhsq/szzg/eventController/toSpecifiedAddPage.jhtml";
	    showMaxJqueryWindow("事件采集", url, 482, 280);
	}
	
	function addGivenEvent(urgencyDegree){
		var event = {
			"urgencyDegree" : urgencyDegree,
			"isReport":false
		};
				
		var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByJson.jhtml?eventJson='+encodeURI(JSON.stringify(event));
	    showMaxJqueryWindow("新增事件信息", url, fetchWinWidth(), defaultShowWindowHeight<document.body.clientHeight?defaultShowWindowHeight:undefined, true);
		
	}

	function showMyPic(url) {

		showMaxJqueryWindow("图片查看", url, null, null, true);

	}
	
	function BaseWorkflow4UrgeHandle_initParam() {//督办、催办初始化方法
		var reportType = $('#reportType').val();
		var initObj = {
			initUrge: {
				initUrgeUrl: "${rc.getContextPath()}/zhsq/workflow/workflowController/eventNoteContent.jhtml?noteType=remindNote-reportFocus&isCapRemindedUser=true"
			},
			addUrge: {
				addUrgeUrl: '${rc.getContextPath()}/zhsq/event/eventDisposalController/addRemind.jhtml',
				addUrgeCallback: remindCallback
			},
			sendSms: {
				isSendSms: true,
				isRemote: false
			}
		};
		
		return initObj;
	}
	
	function remind(operateType) {
		var selectItem = $('#list').datagrid('getSelected'),
			instanceId = null,
			eventId = null;
		
		if(selectItem != null) {
			eventId = selectItem.eventId;
			instanceId = selectItem.instanceId;
		} else {
			$.messager.alert('警告','请选中要操作的记录再执行此操作!','warning');
			return;
		}
		
		var url = "${rc.getContextPath()}/zhsq/reportFocus/toAddUrgeOrRemind.jhtml?formId=" + eventId + "&instanceId=" + instanceId + "&operateType=" + operateType;
		
		openJqueryWindowByParams({
			maxWidth: 480,
			maxHeight: 300,
			title: "督办信息",
			targetUrl: url
		});
	}
	
	function remindCallback(data) {
		var msg = "操作失败！";
		
		if(data.result && data.result == true) {
			var msg = "催办成功！";
			
			if(data.operateType == 1) {
				msg = "督办成功！";
			}
		} else if(data.msgWrong) {
			msg = data.msgWrong;
		}
		
		reloadDataForSubPage(msg, null, true);
	}
</@override>

<@extends name="/zzgl/event/eventDataGridToolbar.ftl" />