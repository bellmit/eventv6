<@override name="handlePageTitle">南安市办理事件页面</@override>

<@override name="extraJs">
	<script type="text/javascript">
	function delRecord(deleteUrl) {
		deleteUrl = deleteUrl || '${rc.getContextPath()}/zhsq/event/eventDisposalController/delEvent.jhtml?idStr=' + $('#formId').val();
		
		$.messager.confirm('删除提示', '确定删除该记录吗？', function(r) {
			if(r) {
				modleopen();
				$("#flowSaveForm").attr("action", deleteUrl);
				
				$("#flowSaveForm").ajaxSubmit(function(data) {
					modleclose();
					
					var result = data.successTotal,
						msg = data.msgWrong;
					
					if(result && result > 0) {
						msg = msg || '操作成功！';
					} else {
						msg = msg || '操作失败！';
					}
					
					$.messager.alert('提示', msg, 'info', function() {
						cancel();
					});
				});
			}
		});
	}
	</script>
</@override>

<@override name="bigFileUploadOption">
	$('#imageUpload>td>label.LabName>span').html('图片上传：');
	
	$.extend(bigFileUploadOpt, {
		finishEleRander: function() {
			var eventType ="${event.type!}";
			
			if(eventType) {
				changeLabelDict(eventType);
			}
		}
	});
	
	var bigViodeUploadOpt = $.extend({}, bigFileUploadOpt, {
		fileExt: '.mp4,.avi,.amr',
		labelDict:[{'name':'处理前', 'value':'1'}]
	}),
	videoUploadHtml = '<tr>' +
				      	'<td>' +
				      		'<label class="LabName"><span>视频上传：</span></label><div id="bigVideoUploadDiv"></div>' +
				      	'</td>' +
					  '</tr>';
	
	$('#imageUpload').after(videoUploadHtml);
	
	bigFileUpload_initFileUploadDiv('bigVideoUploadDiv', bigViodeUploadOpt);
</@override>

<@override name="attachmentCheck">
	if(flag) {
		var nextNodeName = $('#nodeName_').val(), CLOSE_HANDLE_NODE_NAME = 'task8',//结案
			eventType = $('#type').val() || "${event.type!}",
			option = {},
			isAntiSpoofing = eventType === '22',
			isArchiveCollect = eventType === '24',
			_attachmentCheckFlag = true;
		
		if(isArchiveCollect && $('#isAdoptedRadioDiv').is(':visible') && $('#isAdoptedRadioDiv input[type=radio][name=isAdopted]:checked').length == 0) {
			$.messager.alert('警告','请选择是否采用！','warning');
			return;
		} else if(isAntiSpoofing) {
			var labelDict = $('#bigFileUploadDiv').getInstanceX().labelDict,
				typeNameObj = {};
			
			if(labelDict) {
				for(var index in labelDict) {
					typeNameObj[labelDict[index].value] = labelDict[index].name;
				}
				
				option.typeNameObj = typeNameObj;
			}
		}
		
		if(isAntiSpoofing) {
			_attachmentCheckFlag = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'), null, option)
								&& checkAttachment4BigFileUpload(3, $('#bigFileUploadDiv div[file-status="complete"]'), null, option);
		} else if(nextNodeName == CLOSE_HANDLE_NODE_NAME) {
			_attachmentCheckFlag = checkAttachment4BigFileUpload(3, $('#bigFileUploadDiv div[file-status="complete"]'), null, option);
		}
		
		if(!_attachmentCheckFlag) {
			return;
		}
	}

	var occurred = $('#occurred').val();
	var type = $('#type').val();

	if(isBlankStringTrim(occurred) && isNotBlankStringTrim(type) && type == '23'){
		isValid = false;
		$.messager.alert('警告','事发详址不能为空，请选择楼栋名称！','warning');
	}
</@override>

<@override name="radioCheckSuccessAfter">
	var eventType = $('#type').val() || '${event.type!}';
	
	if(eventType === '22') {
		changeLabelDict(eventType);
	}
</@override>

<@override name="smsContentDataOption">
	{
		'formId'		: $("#formId").val(),
		'taskId'		: $("#taskId").val(),
		'curnodeName'	: $("#curNodeName").val(),
		'nodeName'		: $("#nodeName_").val(), 
		'advice'		: $("#advice").val(), 
		'receiverIds'	: $('#receiverIds').val(),//如果需要发送给当前办理人员，需要更改为$('#userIds').val()
		'nodeId'		: nodeId,
		isCapReceiverId	: false,
		isCapReceiverMobilePhone : false
	}
</@override>

<@extends name="/zzgl/event/workflow/handle_event_node.ftl"/>