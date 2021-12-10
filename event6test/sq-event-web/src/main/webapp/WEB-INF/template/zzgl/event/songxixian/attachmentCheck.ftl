<@override name="attachmentCheck">
	var _attachmentCheckFlag = true;
	
	if(typeof isValid !== 'undefined') {
		_attachmentCheckFlag = isValid;
	}
	
	if(_attachmentCheckFlag) {
		if(typeof toClose !== 'undefined') {
			var _toClose = parseInt(toClose, 10);

			if(_toClose == 0 || _toClose == 1) {
				<@block name="attachmentCheckBlock">
				_attachmentCheckFlag = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
				</@block>
				
				if(!_attachmentCheckFlag) {
					return;
				}
			}
		}
	}
</@override>