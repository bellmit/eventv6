<script type="text/javascript">
	/***********************************************************
	* 1、以下关于附件的验证只在页面进行，没有进行后台交互验证；
	* 2、以下关于附件的验证与页面使用的附件上传组件有关，组件更换，则判断方法也需更换；
	* 3、以下关于附件的验证对应的附件上传组件为swfupload.js、handlers.js
	***********************************************************/
	
	/**
    * toClose 	0 非结案；1 结案；默认为0
    * selector	起始选择的jquery对象
    * fileType 	附件格式，多个附件格式使用英文逗号连接，默认为bmp,tif,png,jpg,gif,webp
    */
    function checkPicture(toClose, selector, fileType) {
    	var isValid = true;
    	
    	if(isBlankString(toClose)) {
			toClose = "0";
		}
		
		switch(parseInt(toClose, 10)) {
			case 1: {
				isValid = checkAttachment4BigFileUpload(3, selector);
			}
			case 0: {
				isValid = isValid && checkAttachment4BigFileUpload(1, selector);
			}
		}
		
		return isValid;
    }
    
    /**
    * type 		1 处理前；3 处理后；默认为1
    * selector	起始选择的jquery对象
    * fileType 	附件格式，多个附件格式使用英文逗号连接，默认为bmp,tif,png,jpg,gif,webp
    */
    function checkAttachment(type, selector, fileType) {
		var isAttachmentValid = false,
			attachmentInpt = null,
			typeName = "处理前",
			fileObj = null,
			filePath = null;
		
		if(selector) {
			attachmentInpt = selector.find('input[name="attachmentId"]');
		}
		type = type || 1;
		fileType = fileType || 'bmp,tif,png,jpg,gif,webp';
		
		if(type == 3) {
			typeName = "处理后";
		}
		
		if(attachmentInpt) {
			attachmentInpt.each(function() {
				//列表模式
				fileObj = $('#' + $(this).val() + ' label:contains('+ typeName +')');
				if(fileObj.length > 0) {
					filePath = $('#' + $(this).val()).attr('path');
				} else {
					//缩略图模式
					fileObj = $('#' + $(this).val() + '_thumbAnchor span:contains('+ typeName +')');
					if(fileObj.length > 0) {
						filePath = fileObj.eq(0).siblings('img').eq(0).attr('src');
					}
				}
				
				if(filePath) {
					var suffix = filePath.substr(filePath.lastIndexOf('.') + 1).toLowerCase();
					if(fileType.indexOf(suffix) > 0) {
						isAttachmentValid = true; 
						return false;
					}
				}
			});
		}
		
		if(!isAttachmentValid) {
			$.messager.alert('警告', "请先上传" + typeName + "图片！", 'warning');
		}
		
		return isAttachmentValid;
	}
</script>