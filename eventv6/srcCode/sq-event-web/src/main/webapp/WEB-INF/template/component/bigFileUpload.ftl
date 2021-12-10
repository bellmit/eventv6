<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/layui.css" />
<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/big-file-upload.css" />
<script type="text/javascript" src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/layui-v2.4.5/layui/layui.js" charset="utf-8"></script>
<script type="text/javascript" src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/webuploader/webuploader.js" charset="utf-8"></script>
<script type="text/javascript" src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/upload-common.js"></script>
<script type="text/javascript" src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/big-file-upload.js" charset="utf-8"></script>  
<script type="text/javascript" src="${SQ_EVENT_URL}/js/event/eventExtend.js" charset="utf-8"></script>

<script type="text/javascript">
	function bigFileUpload_initFileUploadDiv(divId, option) {
		var bigFileUploadOpt = {
			useType: 'add',
        	componentsDomain: '${COMPONENTS_URL!}',
        	appcode: 'zhsq_event',
        	fileNumLimit: 50,
        	maxSingleFileSize: 10,
        	isDelDiscData: false,
        	duplicate: true,
        	styleType: 'box',
        	isUseLabel: true,
        	useDomainCache:true,
        	videoSize: [$(window).width() + 'px', $(window).height() + 'px'],
        	labelDict: [{'name':'处理前', 'value':'1'},{'name':'处理中', 'value':'2'},{'name':'处理后', 'value':'3'}],
        	individualOpt : {
        		isAutoMarginLeft : true,
        		isUploadHandlingPic : false
        	}
        };
        
        if(option) {
	        var individualOpt = {};
	        
	        $.extend(individualOpt, bigFileUploadOpt.individualOpt, option.individualOpt);
	        $.extend(bigFileUploadOpt, option);
	        $.extend(bigFileUploadOpt.individualOpt, individualOpt);
        }
        
        if(bigFileUploadOpt.individualOpt.isAutoMarginLeft || bigFileUploadOpt.individualOpt.isAutoMarginLeft == 'true') {
        	$('#' + divId).css('margin-left', $('#' + divId).siblings().eq(0).outerWidth(true));
        }
        
        if(isBlankParam(bigFileUploadOpt.individualOpt.isUploadHandlingPic)  || bigFileUploadOpt.individualOpt.isUploadHandlingPic == false || bigFileUploadOpt.individualOpt.isUploadHandlingPic == 'false') {
        	var handlingEventSeq = '2', handlingIndex = 0;
        	
        	if(isNotBlankParam(bigFileUploadOpt.labelDict) && typeof bigFileUploadOpt.labelDict === 'object') {
        		var labelDictArray = bigFileUploadOpt.labelDict;
        		
        		handlingIndex = 0;
        		
        		for(var index in labelDictArray) {
        			if(labelDictArray[index].value == handlingEventSeq) {
        				handlingIndex = index;
        				break;
        			}
        		}
        		
        		labelDictArray.splice(handlingIndex, 1);
        		
        		bigFileUploadOpt.labelDict = labelDictArray;
        	}
        	
        	if(isNotBlankParam(bigFileUploadOpt.attachmentData) && isNotBlankParam(bigFileUploadOpt.attachmentData.eventSeq)) {
        		var eventSeqArray = bigFileUploadOpt.attachmentData.eventSeq.split(',');
        		
        		handlingIndex = 0;
        		
        		for(var index in eventSeqArray) {
        			if(eventSeqArray[index] == handlingEventSeq) {
        				handlingIndex = index;
        				break;
        			}
        		}
        		
        		eventSeqArray.splice(handlingIndex, 1);
        		
        		bigFileUploadOpt.attachmentData.eventSeq = eventSeqArray.toString();
        	}
        }
        
        if(isBlankParam(bigFileUploadOpt.showTip) && bigFileUploadOpt.useType == 'view') {
        	bigFileUploadOpt.showTip = false;
        }
        
        $('#' + divId).bigfileUpload(bigFileUploadOpt);
	}
</script>