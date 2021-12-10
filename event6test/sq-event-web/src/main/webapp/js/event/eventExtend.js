/**
 * 附件上传必填验证
 * 该验证只适用于上传附件组件为 $SQ_FILE中的handlers.js
 * @param type					附件类别，1 处理前；2 处理中；3 处理后
 * @param attachmentInpt	附件集合
 * @param fileType				附件类别，多个值使用英文逗号连接
 * @param option				扩展参数，{}
 * @returns
 */
function checkAttachment(type, attachmentInpt, fileType, option) {
	var isAttachmentValid = false,
		  typeName = null,
		  typeNameObj = {1: "处理前", 2: "处理中", 3: "处理后"},
		  filePath = null;
	
	type = type || 1;
	fileType = fileType || 'bmp,tif,png,jpg,jpeg,gif,webp';
	
	typeName = typeNameObj[type];
	
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

/**
 * 附件上传必填验证
 * 该验证只适用于上传附件组件为 $COMPONENTS_DOMAIN中的big-file-upload.js
 * @param type					附件类别，1 处理前；2 处理中；3 处理后
 * @param attachmentInpt	附件集合
 * @param fileType				附件类别，多个值使用英文逗号连接
 * @param option				扩展参数，{}
 * 				isUseOption  是否启用该参数
 * 				typeName  类型中文名称
 * @returns
 */
function checkAttachment4BigFileUpload(type, attachmentInpt, fileType, option) {
	var isAttachmentValid = false,
		  typeName = null,
		  typeNameObj = {1: "处理前", 2: "处理中", 3: "处理后"},
		  filePath = null;
	
	option = option || {};
	type = type || 1;
	fileType = fileType || 'bmp,tif,png,jpg,jpeg,gif,webp';
	$.extend(typeNameObj, option.typeNameObj);
	
	typeName = typeNameObj[type];
	
	if(attachmentInpt) {
		attachmentInpt.each(function() {
			var labelName = null,
			      fileName = '',
			      suffix = null,
			      labelNameObj = $(this).find('div.top-pxt > p');
			
			if(labelNameObj.length > 0) {
				//box模式
				labelName = labelNameObj.eq(0).html();
				fileName = $(this).find('p.file-text').eq(0).attr('title');
			} else {
				//list模式
				labelName = $(this).find('p.file-text > span.cl-tb').eq(0).html();
				fileName = $(this).find('p.file-text > span.list-file-name').eq(0).attr('title');
			}
			
			if(fileName) {
				suffix = fileName.substr(fileName.lastIndexOf('.') + 1).toLowerCase();
			}
			
			if(labelName == typeName && fileType.indexOf(suffix) > 0) {
				isAttachmentValid = true; 
				return false;
			}
		
		});
	}

	if(undefined != option && option.isUseOption){
		if(undefined != option.typeName){
			typeName = option.typeName;
		}
	}
	if(!isAttachmentValid) {
		$.messager.alert('警告', "请先上传" + typeName + "图片！", 'warning');
	}

	return isAttachmentValid;
}

/**
 * 验证附件上传状态
 * 该验证只适用于上传附件组件为 $COMPONENTS_DOMAIN中的big-file-upload.js
 * @param divId		附件组件初始化使用divId
 * @param option	扩展参数，{}
 * @returns
 */
function checkAttachmentStatus4BigFileUpload(divId, option) {
	var isValid = true;
	
	if(divId && typeof divId === 'string') {
		if($('#' + divId) && typeof $('#' + divId).submitValidate === 'function') {
			var fileUploadResultObj = {}, progress = 0;
			
			/*{
			 * "incomplete" : 0, //未上传完成的所有附件，包含队列中的附件数、progress和error，如果这个为0，则说明所有文件都已成功上传
			 * "progress":0, //上传过程中的附件，用于特殊的处理
			 * "error":0 ,//上传出错的附件，用于特殊的处理
			 * }*/
			fileUploadResultObj = $('#' + divId).submitValidate();
			
			progress = fileUploadResultObj.incomplete - fileUploadResultObj.error;
			
			if(progress > 0) {
				isValid = false;
				$.messager.alert('警告', '有' + progress + '个附件正在上传中，请稍后操作！', 'warning');
			}
		}
		
	}
	
	return isValid;
}
