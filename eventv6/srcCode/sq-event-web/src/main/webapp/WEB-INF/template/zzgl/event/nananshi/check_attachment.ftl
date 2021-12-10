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
    function checkPicture(toClose, selector, fileType, option) {
    	var isValid = true;
    	
    	if(isBlankString(toClose)) {
			toClose = "0";
		}
		
		switch(parseInt(toClose, 10)) {
			case 1: {
				isValid = checkAttachment4BigFileUpload(3, selector, fileType, option);
			}
			case 0: {
				isValid = isValid && checkAttachment4BigFileUpload(1, selector, fileType, option);
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
		var isAttachmentValid = true,
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
	
	/**
	* 依据事件分类获取附件标签名称信息
	*/
	function capLabelDict(eventType) {
		var radioList = null;
		
		if(eventType === '22') {
			radioList = [{'name':'座谈', 'value':'1'}, {'name':'处理中', 'value':'2', 'isShow': false}, {'name':'入户宣传登记表', 'value':'3'}];
		} else {
			var isUploadHandlingPic = <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>;
			
			radioList = [{'name':'处理前', 'value':'1'}];
			
			if(isUploadHandlingPic) {
				radioList.push({'name':'处理中', 'value':'2'});
			}
			
			radioList.push({'name':'处理后', 'value':'3'});
		}
		
		return radioList;
	}
	
	/**
	* 依据事件分类变更附件标签名称
	*/
	function changeLabelDict(eventType) {
		var radioList = capLabelDict(eventType),
			eventName = $('#eventName').val();
		
		if(eventType === '22') {
			eventName = '反诈宣传';
			$('#content').attr('placeholder', '请填写入户宣传登记表编号');
		} else {
			$('#content').removeAttr('placeholder');
		}
		
		$('#eventName').val(eventName);
		
		$('#bigFileUploadDiv').changeLabelDict(radioList);
		alterLabelDictAfterWidth(eventType);
	}
	
	/**
	* 调整入户宣传登记表标签宽度
	*/
	function alterLabelDictAfterWidth(eventType) {
		var labelWidth = $('#bigFileUploadDiv div.bigFile-upload-box .zt-pxt > a[label-val=1]').eq(0).outerWidth(true);
		
		if(eventType === '22') {
			labelWidth += 35;
		}
		
		$('#bigFileUploadDiv div.bigFile-upload-box .zt-pxt > a[label-val=3]').width(labelWidth);
	}
	
	function changeContactUserLabel(eventType, isRetain) {
		var contactUserLabelSpan = "联系人员：",
			contactTelLabelSpan = "联系电话：";
			contactUser = "${event.contactUser!}",
			tel = "${event.tel!}",
			isShowCloseBtn = true;
		 
		if(eventType == '24') {
			contactUserLabelSpan = '<label class="Asterik">*</label>所有人：';
			contactTelLabelSpan = '<label class="Asterik">*</label>联系电话：';
			isShowCloseBtn = false;
			
			if(isRetain != true) {
				contactUser = "";
				tel = "";
			}
		}
		
		if($('#contactUserLabelSpan').length == 1) {
			$('#contactUserLabelSpan').html(contactUserLabelSpan);
		}
		if($('#contactTelLabelSpan').length == 1) {
			$('#contactTelLabelSpan').html(contactTelLabelSpan);
		}
		if($('#contactUser').length == 1) {
			$('#contactUser').val(contactUser);
		}
		if($('#tel').length == 1) {
			$('#tel').val(tel);
		}
		if($('#archiveButton').length == 1) {
			if(isShowCloseBtn == true) {
				$('#archiveButton').show();
			} else {
				$('#archiveButton').hide();
			}
		}
	}
</script>