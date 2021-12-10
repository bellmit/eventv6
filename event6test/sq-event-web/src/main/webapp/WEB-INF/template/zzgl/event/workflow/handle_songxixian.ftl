<@override name="handlePageTitle">
	松溪县办理事件页面
</@override>
<@override name="attachmentCheck">
	if(flag) {
		var curNodeName = $('#curNodeName').val(),
			  nextNodeName = $('#nodeName_').val(),
			  STREET_HANDLE_NODE_NAME = 'task7',//街道专业部门处理
			  DISTRICT_HANDLE_NODE_NAME = 'task8',//区县专业部门处理
			  HANDLING_NODE_NAME = 'task0';//处理中环节
		
		if(nextNodeName != HANDLING_NODE_NAME && (STREET_HANDLE_NODE_NAME == curNodeName || DISTRICT_HANDLE_NODE_NAME == curNodeName)) {
			var _attachmentCheckFlag = checkAttachment4BigFileUpload(2, $('#bigFileUploadDiv div[file-status="complete"]'));
			
			if(!_attachmentCheckFlag) {
				return;
			}
		}
	}
</@override>

<@extends name="/zzgl/event/workflow/handle_event_node.ftl"/>