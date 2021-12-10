/**
 * 默认配置
 * 默认操作类为cn.ffcs.zhsq.servlet.UploadFileServlet
 */
var default_config = {
	upload_url:'/upFileServlet?method=up&moudle=attr',
	download_url:'/upFileServlet?method=down',
	download_video_url: '/zhsq/att/toSeeVideo.jhtml?path=1',
	delete_url:'/upFileServlet?method=delete',
	script_path:'/scripts/updown/swfupload/',
	column_name:'eventSeq',
	showPattern: 'list'
};

var StringBuffer = function(){
	this._strs = new Array();
};
StringBuffer.prototype.append = function(str){
	this._strs.push(str);
	return this;
};
StringBuffer.prototype.toString = function(){
	return this._strs.join("");
};
StringBuffer.prototype.length = function(){
	return this._strs.length;
};

/**
 * 上传文件错误时触发
 * @param file
 * @param errorCode
 * @param message
 */
function fileQueueError(file, errorCode, message) {
	
	try {
		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			alert("文件大小为0");
			break;
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			alert("文件大小超过"+this.settings.file_size_limit);
			break;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			alert("文件扩展名不符上传规定");
			break;
		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			alert("文件上传数超过限制");
			break;
		default:
			alert('文件上传错误');
			break;
		}
		
	} catch (ex) {
		this.debug(ex);
	}
}

/**
 * 上传文件弹出窗口，窗口打开时触发
 */
function fileDialog(){
	var positionId = this.customSettings.positionId;
	var fileNum = $("#fileNum_"+this.customSettings.cancel_upload).val();//获取当前的已上传的文件总数 为字符串类型
	var stats = this.getStats();//获取当前的队列状态对象
	var radioList = $('input[type="radio"][name="'+ positionId +'_upload_pic_radio"]');
	var radioCheckedLen = radioList.length;
	
	if(radioList!=null && radioList!="" && radioCheckedLen>0){
		var radioCheckedLenChecked = $('input[type="radio"][name="'+ positionId +'_upload_pic_radio"]:checked').length;
		if(radioCheckedLenChecked == 0){//默认设置选择第一项
			var radioId = radioList[0].id;
			$("#"+radioId).attr("checked", true);
			changePostParam(default_config.column_name, $("#"+radioId).val(), this.customSettings.positionId);
		}
	}
	
	stats.successful_uploads = parseInt(fileNum);//更改目前上传队列中的文件数量
	this.setStats(stats);//修改队列的stats_object
	
}

/**
 * 上传文件弹出窗口，窗口关闭触发
 * @param numFilesSelected
 * @param numFilesQueued
 */
function fileDialogComplete(numFilesSelected, numFilesQueued) {
	
	try {
		if (numFilesQueued > 0) {
			document.getElementById(this.customSettings.cancel_upload).disabled = "";
			$('#'+this.customSettings.cancel_upload).attr('class', 'can_btn');
			this.startUpload();
		}
	} catch (ex) {
		this.debug(ex);
	}
}

/**
 * 当文件选择对话框关闭消失时，如果选择的文件成功加入上传队列，
 * 那么针对每个成功加入的文件都会触发一次该事件（N个文件成功加入队列，就触发N次此事件）。
 * @param {} file
 * id : string,			    // SWFUpload控制的文件的id,通过指定该id可启动此文件的上传、退出上传等
 * index : number,			// 文件在选定文件队列（包括出错、退出、排队的文件）中的索引，getFile可使用此索引
 * name : string,			// 文件名，不包括文件的路径。
 * size : number,			// 文件字节数
 * type : string,			// 客户端操作系统设置的文件类型
 * creationdate : Date,		// 文件的创建时间
 * modificationdate : Date,	// 文件的最后修改时间
 * filestatus : number		// 文件的当前状态，对应的状态代码可查看SWFUpload.FILE_STATUS }
 */
function fileQueued(file){

	try {
		addFile({
			type:'add',
			initType: this.customSettings.initType,
			fileId:file.id,
			fileName:file.name,
			fileSize:formatFileSize(file.size),
			table:this.customSettings.upload_table,
			cancel_button:this.customSettings.cancel_upload,
			radioList:this.customSettings.radioList,
			radioListAll:this.customSettings.radioListAll,
			positionId:this.customSettings.positionId,
			showPattern : this.customSettings.showPattern,
			imgDomain : default_config.script_path,
			isFromQueued : true,
			filePath : 'images/loading.gif'
		},'addFile');
	} catch (ex) {
		this.debug(ex);
	}
	
}

/**
 * 文件上传中(进度条功能)
 * @param file
 * @param bytesLoaded 已上传的字节数
 * @param totalBytes 文件总的字节数
 */
function uploadProgress(file, bytesLoaded, totalBytes) {
	try {
		var percent = Math.ceil((bytesLoaded / totalBytes) * 100);
		$("#"+file.id).find(".progress_bar").css("width",percent+"%");
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadError(file, errorCode, message) {

	try {
		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
			alert("文件上传出错,请检查文件上传路径");
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			alert("取消上传!");
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			alert("停止上传!");
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			alert("文件大小超过限制");
			break;
		default:
			alert("文件上传出错");
			break;
		}
	} catch (ex3) {
		this.debug(ex3);
	}

}

/**
 * 文件上传成功
 * @param file
 * @param serverData 服务端返回的数据
 */
function uploadSuccess(file, serverData) {
	
	try {
		var data = $.parseJSON(serverData);
		file.attachmentId = data.attachmentId;
		file.filePath = data.filePath;
		
		var row = document.getElementById(file.id);
		var _fileState = new StringBuffer();
		
		var cancel_button = this.customSettings.cancel_upload;
		var upload_table = this.customSettings.upload_table;
		
		var fileType = this.customSettings.fileType;
		_fileState.append("<input type='hidden' id='attachmentId' name='attachmentId' value='").append(file.attachmentId).append("'/>");
		_fileState.append("<input type='hidden' id='filePath' name='filePath' value='").append(file.filePath).append("'/>");
		_fileState.append("<input type='hidden' id='fileType' name='fileType' value='").append(fileType).append("'/>");
		_fileState.append("<input type='hidden' id='fileSize' name='fileSize' value='").append(formatFileSize(file.size)).append("'/>");
		_fileState.append("文件上传完成");
		
		$(row.cells[3]).html(_fileState.toString());

		//添加下载按钮
		var _downFile = new StringBuffer();
		_downFile.append('<img src="').append(default_config.script_path).append('images/icon/folder_del.gif" width="16" height="16" ')
		   .append('style="cursor:pointer;vertical-align:middle;"')
		   .append('id=img_').append(file.attachmentId)
		   .append(' onclick="deleteFile(').append(file.attachmentId).append(',\'').append(cancel_button).append('\',\'').append(upload_table).append('\');" alt="删除文件" title="删除"/>&nbsp;');
		
		_downFile.append('<a href="').append(this.customSettings.download_url)
				 .append('&attachmentId=').append(file.attachmentId).append('" target="_blank">');
		_downFile.append('<img src="').append(default_config.script_path);
		_downFile.append('images/icon/down.png" width="16" height="16" style="vertical-align:middle;" alt="查看文件" title="查看"/></a>');
		$(row.cells[4]).append(_downFile.toString());
		
		$("#img_"+file.attachmentId).closest("tr").attr("id", file.attachmentId);//为了使得新增的图片的删除功能和取消上传功能有效  20140705
		$("#img_"+file.attachmentId).closest("tr").attr("path", file.filePath);
		
		var imgStr = "png,jpg,gif,jpeg",		//可查看缩略图的附件类型
			amrStr = "amr",
			audioStr = "amr,mp3",				//音频附件类型
			suffix = getFileSuffix(file.name),
			thumbImgSrc = "",
			downloadUrl = this.customSettings.download_url+'&attachmentId='+file.attachmentId;
		
		if(imgStr.indexOf(suffix) >= 0) {
			thumbImgSrc = this.customSettings.imgDomain+"/"+file.filePath;
		} else if(audioStr.indexOf(suffix) >= 0) {
			thumbImgSrc = default_config.script_path + 'images/thumbnail/audio.jpg';
			downloadUrl = this.customSettings.download_video_url +'&attachmentId='+file.attachmentId + '&videoType=2';
		}
		
		$("#"+file.id+"_thumbImg").attr("src", thumbImgSrc);//修改缩略图的文件路径
		$("#"+file.id+"_thumbClose").attr("onclick", "deleteFile("+file.attachmentId+",'"+cancel_button+"','"+upload_table+"')");//修改缩略图删除图片操作
		$("#"+file.id+"_thumbAnchor").attr("href", downloadUrl);//修改缩略图查看原图连接
		$("#"+file.id+"_thumb").attr("id", file.attachmentId+"_thumb");//修改缩略图的a标签的id
		$("#"+file.id+"_thumbSpan").attr("id", file.attachmentId+"_thumbSpan");//修改删除图标的span的id
		
		$("#fileNum_"+cancel_button).val(parseInt($("#fileNum_"+cancel_button).val())+1);//上传成功后 +1
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadComplete(file) {
	
	try {
		//上传文件数+1
		//$("#fileNum").val(parseInt($("#fileNum").val())+1);
		/*  I want the next upload to continue automatically so I'll call startUpload here */
		if (this.getStats().files_queued > 0) {
			this.startUpload();
		} else {
			//alert('所有文件上传完毕!');
		}
	} catch (ex) {
		this.debug(ex);
	}
}

/**
 * 根据文件名截取文件后缀
 * @param fileName
 * @returns
 */
function getFileSuffix(fileName){ 
	var len = fileName.lastIndexOf('.'); 
	return fileName.substr(len+1,fileName.length).toLowerCase(); 
}  

/**
 * 文件名太长...显示
 * @param fileName
 * @returns
 */
function displayFileName(fileName){	
	if(fileName.length > 9){
		var _name = new StringBuffer();
		var index = fileName.lastIndexOf('.'); 
		_name.append("<span style='cursor:pointer' title='").append(fileName).append("'>");
		_name.append(fileName.substring(0,1)).append("...").append(fileName.substring(index-1,index)).append(".").append(getFileSuffix(fileName)).append("</span>");
		return _name.toString();
	}
	return fileName;
}

/**
 * 格式化文件大小
 * @param {Object} fileSize
 */
function formatFileSize(fileSize) {
	var size = fileSize;
	var unit = "B";
	if(size==undefined || size==null || size==""){
		size = 0;
	}
	size = size - 1 + 1;// 转成NUMBER类型
	if (size > (1024 * 1024 * 1024)) {
		unit = "GB";
		size /= (1024 * 1024 * 1024);
	} else if (size > (1024 * 1021)) {
		unit = "MB";
		size /= (1024 * 1024);
	} else if (size > 1024) {
		unit = "KB";
		size /= 1024;
	} 
	return size.toFixed(2) + unit;
}


/**
 * 往表格添加文件信息file, upload_table, type
 * @param result
 * var result = {
 * 		fileId:'',
 * 		fileName:'',
 * 		filePath:'',
 * 		fileSize:'',
 * 		tableName:'',
 * 		type:'',//type add、edit、detail
 * 		delFileMethod:'',
 * 		isFromQueued	//true表示文件来自队列，否则表示文件来自外部
 * }
 */
function addFile(result,type){
	addImgList(result, type);
	addThumbImg(result,type);
}

/**
 * 获取选中的图片类型 处理前 处理中 处理后
 * @param result
 * @returns {String}
 */
function capEventSeqName(result) {
	var eventSeq = result.eventSeq;
	var eventSeqName = "";
	var radioList = result.radioListAll;
	var positionId = result.positionId;
	
	if(eventSeq==undefined || eventSeq==null || eventSeq==""){
		var radioChecked = $('input[type="radio"][name="'+ positionId +'_upload_pic_radio"]:checked');
		var radioCheckedLen = radioChecked.length;
		if(radioCheckedLen == 1){
			eventSeq = $(radioChecked[0]).val();
		}
	}
	
	if(radioList==undefined || radioList==null){
		radioList = result.radioList;
	}
	
	if(radioList != undefined && radioList != null && radioList != ''){
		for(var index = 0, len = radioList.length; index < len; index++){
			if(eventSeq == radioList[index].value){
				eventSeqName = radioList[index].name;
				break;
			}
		}
	}
	
	return eventSeqName;
}

/**
 * 添加图片列表
 * @param result
 * 			isFromQueued	//true表示文件来自队列，否则表示文件来自外部
 * @param type
 */
function addImgList(result, type) {
	var showPattern = result.showPattern;
	var imgStr = "bmp,doc,docx,txt,xls,tif,rtf,xml,ppt,ppx,png,pdf,jpg,gif,amr,mp3";
	var suffix = getFileSuffix(result.fileName);
	if(imgStr.indexOf(suffix)<0){
		suffix = "epub";
	}
	
	var row = new StringBuffer();
	var eventSeqName = capEventSeqName(result);

	if(showPattern == 'list' || showPattern == 'all') {
		row.append('<tr ');
	} else {
		row.append('<tr class="hide" ');
	}
	row.append(' id="').append(result.fileId).append('" type="').append(result.type)
	   .append('" init_type="').append(result.initType).append('"')
	   .append(' path="').append(result.filePath+'">');
	row.append('<td style="width: 30px;" align="center">');
	row.append('<img style="margin-top:3px;" src="').append(default_config.script_path).append('images/icon/icon_').append(suffix).append('.gif" width="16" height="16" />');
	row.append('</td>');
	row.append('<td align="left">');
	if(eventSeqName!=undefined && eventSeqName!=""){
		row.append('<label class="FontDarkBlue">').append('['+eventSeqName+']').append('</label>');
	}
	row.append(displayFileName(result.fileName)).append('</td>');
	//row.append('<td width="50" align="center">').append(result.fileSize).append('</td>');
	row.append('<td></td>');
	row.append('<td align="center">');
	if(result.type=='add'){
		row.append('<div class="bar"><div class="progress_bar"></div></div>');//新增添加进度条
	}
	
	if(!result.isFromQueued) {//为了让传递attachmentId的附件也能正常关联上业务id
		var fileType = 'fileType';
		
		row.append("<input type='hidden' id='attachmentId' name='attachmentId' value='").append(result.fileId).append("'/>");
		row.append("<input type='hidden' id='filePath' name='filePath' value='").append(result.filePath).append("'/>");
		row.append("<input type='hidden' id='fileType' name='fileType' value='").append(fileType).append("'/>");
		row.append("<input type='hidden' id='fileSize' name='fileSize' value='").append(formatFileSize(result.fileSize)).append("'/>");
	}
	
	row.append('</td>');
	row.append('<td style="width: 100px;" align="center">');
	if(result.type != 'detail' && type==null){//文件详情无删除图片
		row.append('<img src="').append(default_config.script_path).append('images/icon/folder_del.gif" width="16" height="16" ')
		   .append('style="cursor:pointer;vertical-align:middle;"')
		   .append(' onclick="deleteFile(').append(result.fileId).append(',\'').append(result.cancel_button).append('\',\'').append(result.table).append('\');" alt="删除文件" title="删除"/>&nbsp;');

	} 
	if('add' != result.type){
		row.append('<a href="').append(result.download_url).append('&attachmentId=').append(result.fileId).append('" target="_blank" >')
		   .append('<img src="').append(default_config.script_path).append('images/icon/down.png" width="16" height="16"')
		   .append(' style="vertical-align:middle;" alt="查看文件" title="查看" /></a>');		 
	}
	row.append('</td>');
	row.append('</tr>');
	
	$("#"+result.table).append(row.toString());
	$("#"+result.table).removeClass("hide");
}

/**
 * 添加图片缩略图展示
 * @param result
 * 			isFromQueued	//true表示文件来自队列，否则表示文件来自外部
 * @param type
 */
function addThumbImg(result, type) {
	var showPattern = result.showPattern,
		imgStr = "png,jpg,gif,jpeg",		//可查看缩略图的附件类型
		amrStr = "amr",
		audioStr = "amr,mp3",				//音频附件类型
		suffix = getFileSuffix(result.fileName),
		thumbImgSrc = "",
		downloadUrl = result.download_url;
	
	if(imgStr.indexOf(suffix) >= 0) {
		thumbImgSrc = result.imgDomain + "/" + result.filePath;
	} else if(audioStr.indexOf(suffix) >= 0) {
		thumbImgSrc = default_config.script_path + 'images/thumbnail/audio.jpg';
		downloadUrl = result.download_video_url + '&videoType=2';
	}
	
	if(thumbImgSrc) {//附件为图片时，才展示图片缩略图
		var row = new StringBuffer();
		var eventSeqName = capEventSeqName(result);
		
		if(showPattern == 'pic' || showPattern == 'all') {
			row.append('<span style="display:inline-block;');
		} else {
			row.append('<span class="hide" style="');
		}
		
		row.append('position:relative; margin-right:20px; margin-top:15px;" id="').append(result.fileId).append('_thumb" >');
		
		if(result.type != 'detail'){//文件详情无删除图片
			row.append('<span id="').append(result.fileId).append('_thumbSpan" class="hide" ').append(' init_type="').append(result.initType).append('"');
			row.append(' onmouseover="showCloseSpanSelf(this, true)"; onmouseout="showCloseSpanSelf(this, false)"; >');
			row.append('<img id="').append(result.fileId).append('_thumbClose" src="').append(default_config.script_path).append('images/icon/close.png"')
			   .append('style="position:absolute; right:-4px; top:-4px;cursor:pointer;vertical-align:middle;"')
			   .append(' onclick="deleteFile(').append(result.fileId).append(',\'').append(result.cancel_button).append('\',\'').append(result.table).append('\');" alt="删除文件" title="删除"/>');
			row.append('</span>');
		}
		
		row.append('<a id="').append(result.fileId).append('_thumbAnchor" href="').append(downloadUrl).append('&attachmentId=').append(result.fileId).append('" target="_blank" ')
		   .append(' onmouseover="showCloseSpan(this, true)"; onmouseout="showCloseSpan(this, false)"; ')
		   .append(' style="text-decoration:none;" >');
		
		row.append('<img id="').append(result.fileId).append('_thumbImg" title="').append(result.fileName).append('" src="').append(thumbImgSrc).append('" width="60" height="60" ')
		   .append('style="cursor:pointer;vertical-align:middle;" />');
		
		if(eventSeqName && eventSeqName!='') {
			row.append('<span style="position:absolute; left:0px; bottom:0px;width:100%; background:url(').append(default_config.script_path).append('images/thumb_bg.png').append(') repeat; text-align:center; color:#fff;">').append(eventSeqName).append('</span>');
		}
		
		row.append('</a>');
		
		row.append('</span>');
		
		$("#"+result.table+"_thumbDiv").append(row.toString());
		
		if(showPattern == 'pic' || showPattern == 'all') {
			$("#"+result.table+"_thumbDiv").show();
		}
	}
}

/**
 * 是否展示删除操作图片
 * @param obj 展示的缩略图的a标签对象
 * @param isShow true时展示；否则不展示
 */
function showCloseSpan(obj, isShow) {
	$(obj).siblings("span").each(function() {
		showCloseSpanSelf($(this), isShow);
	});
}

/**
 * 是否展示删除操作图片
 * @param obj 删除操作的span对象
 * @param isShow isShow true时展示；否则不展示
 */
function showCloseSpanSelf(obj, isShow) {
	if(isShow) {
		$(obj).show();
	} else {
		$(obj).hide();
	}
}

/**
 * 删除全部上传文件
 * @param upload_table
 */
function cancelUpload(upload_table, _cancel_button){
	$.messager.confirm('提示', '您确定删除已上传的文件吗？', function(r){
		if(r){
			$('#'+upload_table+' tr').each(function(index, element) {
				if(element.id){
					deleteFile_(element.id, _cancel_button, upload_table);
				}
		    });
		}
	});
}

/**
 * 删除文件
 * @param id 文件ID
 */
function deleteFile(id, _cancel_button, upload_table){
	$.messager.confirm('提示', '您确定删除该文件吗？', function(r){
		if(r){
			deleteFile_(id, _cancel_button, upload_table);
		}
	});
}

function deleteFile_(id, _cancel_button, upload_table){
	if(typeof id  == 'object'){
		id = id.id;
	}

	var initType = $("#"+ id).attr("init_type");
	var ajaxOpt = {
		type: "POST",
	   	url: default_config.delete_url,
	   	data: {'attachmentId':id},
	   	success: function(msg){
			if(msg=='-1'){
				alert('删除附件失败');
			}else{
				$("#"+id).remove();
				$("#"+id+"_thumb").remove();
				//删除成功后，文件数-1
				$("#fileNum_"+_cancel_button).val(parseInt($("#fileNum_"+_cancel_button).val())-1);
				
				if($("#fileNum_"+_cancel_button).val() <= 0){//文件数为0时；当showPattern为pic，但附件中有非图片附件时，图片逐张均删除时，仍会有不可见的附件，故不会执行以下代码
					$("#"+_cancel_button).attr("disabled","disabled");//使“取消上传”按钮不可用
					$("#"+_cancel_button).attr('class', 'can_btn3');
					$("#"+upload_table).addClass("hide");//由于列表有边框，在兼容模式下需要先隐藏列表，否则会出现一条黑线
					$("#"+upload_table+"_thumbDiv").addClass("hide");//隐藏缩略图展示div
				}
			}
	   	},
	   	error:function(data){
			$.messager.alert('错误','删除附件失败','error');
		}
	};
	
	if(initType == undefined || initType == null || initType == "") {
		initType = $("#"+ id + "_thumb").attr("init_type");
	}
	
	if(initType == "jsonp") {
		ajaxOpt["dataType"] = "jsonp";
		ajaxOpt["jsonp"] = 'callback';
	}
	
	$.ajax(ajaxOpt);
}

/**
 * 更改对应的的swfUpload实例的eventSeq参数
 * @param obj
 * @param positionId
 */
function changeAttachmentSeq(obj, positionId){
	var eventSeq = $(obj).val();
	
	changePostParam(default_config.column_name, eventSeq, positionId);
}

/**
 * 更改对应的swfUpload的指定的参数的值
 * @param value
 * @param paramName
 * @param positionId
 */
function changePostParam(paramName, paramValue, positionId){
	var swfUploadInstance = findSwfUploadByPositionId(positionId);
	
	if(swfUploadInstance != null){
		swfUploadInstance.addPostParam(paramName, paramValue);
	}
}

/**
 * 依据positionId查找到对应的swfUpload实例
 * @param positionId
 * @returns
 */
function findSwfUploadByPositionId(positionId){
	var swfUploadInstance = null;
	
	if(positionId!=undefined && positionId!=null && positionId!=""){
		for(var index = 0; index < SWFUpload.movieCount; index++){
			if(SWFUpload.instances["SWFUpload_"+index].customSettings.positionId == positionId){
				swfUploadInstance = SWFUpload.instances["SWFUpload_"+index];
				break;
			}
		}
	}
	
	return swfUploadInstance;
}

/**
 * 依据单选按钮名称设置指定的选项 使用配置选项
 * @param radioName
 * @param positionId
 */
function setCheckedRadioByName(radioName, positionId){
	var swfUploadInstance = findSwfUploadByPositionId(positionId);
	
	if(swfUploadInstance != null && radioName!=undefined && radioName!=null && radioName!=""){
		var radioList = swfUploadInstance.customSettings.radioList;
		
		if(radioList!=undefined && radioList!=null && radioList!="" && radioList.length > 0){
			for(var index = 0, len = radioList.length; index < len; index++){
				if(radioName == radioList[index].name){
					$("#"+ positionId +"_upload_pic_radio_"+index).attr("checked", true);
					changePostParam(default_config.column_name, $("#"+ positionId +"_upload_pic_radio_"+index).val(), swfUploadInstance.customSettings.positionId);
					break;
				}
			}
		}
	}
}

/**
 * 依据单选按钮名称设置指定的选项
 * @param radioName
 * @param positionId
 */
function setCheckedRadioByNameFromRadioList(radioName, positionId){
	var radioList = $('input[type="radio"][name="'+ positionId +'_upload_pic_radio"]');
	var radioText = "";
	var radioId = "";
	
	for(var index = 0, len = radioList.length; index < len; index++){
		radioId = radioList[index].id;
		radioText = $("#"+radioId).attr('text');
		
		if(radioName == radioText){
			$("#"+radioId).attr("checked", true);
			changePostParam(default_config.column_name, $("#"+radioId).val(), positionId);
			break;
		}
	}
}

/**
 * 动态调整radio的选项
 * @param radioList
 */
function changeRadioItem(radioList, positionId){
	if(radioList!=undefined && radioList!=null && radioList!=""){
		var _content = new StringBuffer();
		var radioId = "";
		
		for(var index = 0, len = radioList.length; index < len; index++){
			radioId = positionId+"_upload_pic_radio_"+index;
			_content.append('<span>');
			_content.append('	<input type="radio" id="'+radioId+'" name="'+ positionId +'_upload_pic_radio" text="'+radioList[index].name+'" value="'+radioList[index].value+'" onclick="changeAttachmentSeq(this, \''+positionId+'\')"> ');
			_content.append('		<label for="'+radioId+'" style="cursor:pointer">'+radioList[index].name+'</label> ');
			_content.append('	</input>');
			_content.append('</span>');
		}
		
		$("#"+ positionId +"_upload_pic_radio_div").html(_content.toString());
	}
}

/**
 * 检验必填项
 * @param _config
 * @returns {Boolean}
 */
function initCfgCheck(_config) {
	var isValid = true;
	
	if(_config) {
		var msgWrong = new StringBuffer(),
			checkItem = ["positionId", "type", "initType", "context_path"],//必填项key
			item = "", itemValue = "",
			showPattern = _config.showPattern,
			_type = _config.type;//具体检测项key
		
		if(showPattern && (showPattern == 'pic' || showPattern == 'all')) {
			checkItem.push("imgDomain");
		}
		
		/*if(_type && _type != 'detail') {
			checkItem.push("createUserId");
		}*/
		
		for(var index in checkItem) {
			item = checkItem[index];
			itemValue = _config[item];
			
			if(itemValue == undefined || itemValue == null || $.trim(itemValue) == "") {
				msgWrong.append("缺少属性[").append(item).append("]！");
				break;
			}
		}
		
		if(msgWrong.length() > 0) {
			isValid = false;
			$.messager.alert('错误', msgWrong, 'error');
		}
	}
	
	return isValid;
}

/**
 * 设置默认值，需要在必填项验证完成后使用
 * @param _config
 * @returns {___anonymous21796_21802}
 */
function initCfgDefault(_config) {
	var fileSizeLimit = _config.file_size_limit,
		positionId = _config.positionId,
		_initType = _config.initType.toLowerCase(),
		_type = _config.type;
	
	_config.cancel_button = _config.cancel_button || 'cancel_button_'+positionId;
	_config.upload_table = _config.upload_table || 'upload_table_'+positionId;
	_config.ajaxData = _config.ajaxData || {};
	_config.showPattern = _config.showPattern || default_config.showPattern;
	_config.file_types = _config.file_types || "*.doc;*.docx;*.xls;*.xlsx;*.txt;*.jpg;*.gif;*.png;*.rar;*.zip;*.tif;*.pdf";//文件类型
	_config.file_upload_limit = _config.file_upload_limit || 50;//文件数量
	_config.appCode = _config.appCode || 'sqfile';
	
	_config.initType = _initType;//去除输入时，大小写的差异
	
	if(fileSizeLimit) {
		fileSizeLimit = fileSizeLimit.toUpperCase();
		
		if(fileSizeLimit.toLowerCase() == fileSizeLimit) {//添加默认单位
			fileSizeLimit += " KB";
		}
	} else {
		fileSizeLimit = "10 MB";
	}
	
	_config.file_size_limit = fileSizeLimit;
	
	if(_config.context_path) {//访问上下文
		default_config.upload_url = _config.context_path+'/upFileServlet?method=up&moudle=attr&___appCode='+_config.appCode;
		default_config.download_url = _config.context_path+'/upFileServlet?method=down';
		default_config.download_video_url = _config.context_path + '/zhsq/att/toSeeVideo.jhtml?path=1';
		default_config.delete_url = _config.context_path+'/upFileServlet?method=delete';
		
		if(_config.createUserId) {
			default_config.upload_url += '&___userId=' + _config.createUserId;
		}
		
		if(_type && _type != 'add') {
			if(_initType == 'ajax') {
				default_config.list_url = _config.context_path + '/file/att/getList.jhtml';
			} else if(_initType == 'jsonp') {
				default_config.list_url = _config.context_path + '/file/att/getListForJsonp.jhtml';
			}
			
			_config.ajaxUrl = _config.ajaxUrl || default_config.list_url;
		}
		
		_config.upload_url = _config.upload_url || default_config.upload_url;
		_config.download_url = _config.download_url || default_config.download_url;
		_config.download_video_url = _config.download_video_url || default_config.download_video_url;
		_config.delete_url = _config.delete_url || default_config.delete_url;
	}
	//由于可能有多个控件使用
	if(_config.script_context_path) {//script访问上下文
		default_config.script_path = _config.script_context_path + '/scripts/updown/swfupload/';
	} else if(_config.context_path) {
		default_config.script_path = _config.context_path + '/scripts/updown/swfupload/';
	}
	//设置上传按钮图片
	_config.button_image_url = _config.button_image_url || default_config.script_path+"images/upload.png";//文件上传按钮图片

	return _config;
}

/**
 * 初始化文件
 * @param _config
 *	var _config = {
 * 		positionId:'附件列表DIV的id值',
 * 		upload_table:'logo',//表格ID
 *		cancel_button:'cancel_button1'//取消全部按钮
 * 	    fileSize:'fileSize1',//自定义文件属性
 *	    filePath:'filePath1',//自定义文件属性
 *	    fileName:'fileName1',//自定义文件属性
 *      type:'',//add edit detail
 *      initType:'',//ajax、hidden()
 *      ajaxUrl:'',
 *      ajaxData:'',
 *      hiddenId:'',
 *      upload_url:'',//文件上传路径
 *      file_upload_limit:''
 *      file_size_limit:'',//默认10M
 *      file_types:'',//默认*.*
 *      radio_list:[],//附件eventSeq设置选项
 *      radio_list_all:[]//附件eventSeq设置选项，所有选项，用于名称转换
 *	}
 */
function fileUpload(_config) {
	var isValid = initCfgCheck(_config);//验证配置必填项
	var swfUpload = null;
	
	if(isValid) {
		_config = initCfgDefault(_config);//设置配置默认值
		swfUpload = initFileUpload(_config);
	}
	
	return swfUpload;
}

/**
 * 初始化上传控件
 * @param _config
 * @returns {SWFUpload}
 */
function initFileUpload(_config) {
	var _type = _config.type,
		_initType = _config.initType,
		positionId = _config.positionId,
		_cancel_button = _config.cancel_button,
		_upload_table = _config.upload_table,
		fileSizeLimit = _config.file_size_limit;
	
	//1、init  div
	var _content = new StringBuffer();
	if(_config.type != 'detail'){
		_content.append('<div class="FontRed" style="margin-bottom:5px;">')
				.append('	<span class="warning_tips" style="margin-bottom:-2px;"></span>')
				.append('	文件大小不能超过').append(fileSizeLimit)
				.append('</div>');
		
		if(_config.radio_list != null && _config.radio_list != ''){//添加事件上传类型 处理前、处理中、处理后
			var radioId = "";
			var radioList = _config.radio_list;
			var len = radioList.length;
			
			_content.append('<div style="height:25px; line-height:12px;">');
			_content.append('	<div id="'+ positionId +'_upload_pic_radio_div" class="Check_Radio">');
			for(var index = 0; index < len; index++){
				radioId = positionId+"_upload_pic_radio_"+index;
				_content.append('		<span>');
				_content.append('			<input type="radio" id="'+radioId+'" name="'+ positionId +'_upload_pic_radio" text="'+radioList[index].name+'" value="'+radioList[index].value+'" onclick="changeAttachmentSeq(this, \''+_config.positionId+'\')"> ');
				_content.append('				<label for="'+radioId+'" style="cursor:pointer">'+radioList[index].name+'</label> ');
				_content.append('			</input>');
				_content.append('		</span>');
			}
			
			_content.append('	</div>');
			_content.append('</div>');
		}
		
		_content.append('<div id="upload_file_'+ positionId +'"></div>&nbsp;&nbsp;&nbsp;&nbsp;');
		if(_config.noDiplay){
			_content.append('<input id="').append(_cancel_button).append('"  style="display:none;" type="button" value=""');
			_content.append('onclick="cancelUpload(\'').append(_upload_table).append('\',\'').append(_cancel_button).append('\');" class="can_btn" disabled="disabled"/>');
 		}else{
	 		_content.append('<input id="').append(_cancel_button).append('" type="button" value=""');
			_content.append('onclick="cancelUpload(\'').append(_upload_table).append('\',\'').append(_cancel_button).append('\');" class="can_btn3" disabled="disabled"/>');
 		}
			

		_content.append('<input type="hidden" id="fileNum_'+_cancel_button+'" name="fileNum" value="0"/>');
	}
	
	//增添缩略图展示div
	_content.append('<div id="'+_upload_table+'_thumbDiv" style="margin-bottom:10px;" class="hide"></div>');
	
	if(_config.noDiplay){
		_content.append('<table id="').append(_upload_table).append('" style="display:none;" class="upload_table hide">');
	}else{
		_content.append('<table id="').append(_upload_table).append('" class="upload_table hide">');
	}

	/*_content.append('<tr><th colspan="2" width="250" align="left" style="padding-left:20px;">文件名称</th>')
			.append('<th width="50">大小</th><th width="100">状态</th>');
	_content.append('<th width="100">操作</th></tr></table>');*/
	$("#"+_config.positionId).html(_content.toString());
	
	//2、init data
	if((_initType == 'ajax' || _initType == 'jsonp') && _type != "add" ) {
		//AJAX从Action获取Json Data并加载
		var ajaxOpt = {
			type: "POST",
			async: false,
		   	url: _config.ajaxUrl,
		   	data: _config.ajaxData,
		   	dataType:'json',
		   	success: function(result){	
		   		var fileNumCount = parseInt($("#fileNum_"+_cancel_button).val()) + result.length;
		   		$("#fileNum_"+_cancel_button).val(fileNumCount);
		   		
		   		if(result.length > 0){
			   		$('#'+_cancel_button).removeAttr('disabled');
					$('#'+_cancel_button).attr('class', 'can_btn');
				}
				$(result).each(function(e){
					addFile({
						type:_type,
						initType: _initType,
						fileId:this.attachmentId,
						fileName:this.fileName,
						fileSize:formatFileSize(this.fileSize),
						filePath:this.filePath,
						eventSeq:this.eventSeq,
						table:_upload_table,
						cancel_button:_cancel_button,
						download_url : _config.download_url,
						download_video_url : _config.download_video_url,
						radioList :  _config.radio_list,
						radioListAll :  _config.radio_list_all,
						positionId : _config.positionId,
						imgDomain: _config.imgDomain,
						showPattern : _config.showPattern
					});
				});
		   	},
		   	error:function(data){
				$.messager.alert('错误','获取附件列表失败','error');
			}
		};
		
		if(_initType == 'jsonp') {
			ajaxOpt["dataType"] = "jsonp";
			ajaxOpt["jsonp"] = "callback";
		}
		
		$.ajax(ajaxOpt);
	} else if(_initType == 'hidden'){
		//从隐藏域获取数据并加载
		var result = $.parseJSON($("#"+_config.hiddenId).val());
		$(result).each(function(e){
			
			addFile({
				type:_type,
				initType: _initType,
				fileId:this.attachmentId,
				fileName:this.fileName,
				fileSize:this.fileSize,
				filePath:this.filePath,
				eventSeq:this.eventSeq,
				table:_upload_table,
				cancel_button:_cancel_button,
				download_url : _config.download_url,
				download_video_url : _config.download_video_url,
				radioList :  _config.radio_list,
				radioListAll :  _config.radio_list_all,
				positionId : _config.positionId,
				imgDomain: _config.imgDomain,
				showPattern : _config.showPattern
			});
		});
	}
	
	//3、 init fileupload comp
	if(_type != 'detail'){
		var config  = {
			upload_url: _config.upload_url,
			// File Upload Settings
			//指定要上传的文件的最大体积，可以带单位，合法的单位有:B、KB、MB、GB(大小写均可)，如果省略了单位，则默认为KB。该属性为0时，表示不限制文件的大小。
			//设置值需为正整数，设置小数时，只取整数部分，设置负值及非数字时，设置为0
			file_size_limit : fileSizeLimit,	
			file_types:_config.file_types,//文件类型
			//file_types_description : "产品LOGO",
			file_upload_limit:_config.file_upload_limit,//文件数量
			//file_queue_limit:10,
			file_dialog_start_handler:fileDialog,//上传文件窗口打开前触发
			file_queue_error_handler : fileQueueError,//上传文件错误时触发
			file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
			file_queued_handler : fileQueued,//选择完文件后就触发
			upload_progress_handler : uploadProgress,
			upload_error_handler : uploadError,//上传错误触发
			upload_success_handler : uploadSuccess,
			upload_complete_handler : uploadComplete,
			// Flash Settings
			flash_url : default_config.script_path+"swfupload.swf",
			button_placeholder_id : "upload_file_"+positionId,
			button_image_url: _config.button_image_url,
			button_text : '',//'<span class="theFont">上 传文件</span>',
			button_width: 81,
			button_height: 21,
			button_text_left_padding: 12,
			button_cursor:-2,
			use_query_string : true,
			post_params :_config.ajaxData||{"a":"1"},
			custom_settings : {
				cancel_upload: _cancel_button,
				upload_table : _upload_table,
				fileType : _config.fileType||'fileType',
				upload_target: 'file_progress',
				download_url : _config.download_url,
				download_video_url : _config.download_video_url,
				radioList : _config.radio_list,
				radioListAll : _config.radio_list_all,
				positionId : _config.positionId,
				imgDomain : _config.imgDomain,
				initType : _initType,
				showPattern : _config.showPattern//附件列表展示模式 pic只展示图片缩略图；list只展示附件列表；all同时展示图片缩略图和附件列表
			}//,debug:true
		};
		
		var swfUpload = new SWFUpload(config);
		
		return swfUpload;
	}
}
