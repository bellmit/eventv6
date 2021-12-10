/**
 * 默认配置
 */
var default_config = {
	upload_url:'/upFileServlet?method=up&moudle=attr',
	download_url:'/upFileServlet?method=down',
	delete_url:'/upFileServlet?method=delete',
	script_path:'/scripts/updown/swfupload/'
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
			alert("文件大小超过限制");
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
 * 上传文件弹出窗口，窗口关闭触发
 * @param numFilesSelected
 * @param numFilesQueued
 */
function fileDialogComplete(numFilesSelected, numFilesQueued) {
	
	try {
		if (numFilesQueued > 0) {
			if(numFilesQueued > 1){
				alert("文件上传数超过限制");
				var row = document.getElementById(this.customSettings.tr_id);
				$(row.cells[1]).html('&nbsp;');
				$(row.cells[2]).html('&nbsp;');
				$(row.cells[3]).html('&nbsp;');
			} else {
				this.startUpload();
			}
			//document.getElementById(this.customSettings.cancel_upload).disabled = "";
			
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
		
		var row = document.getElementById(this.customSettings.tr_id);
		$(row.cells[1]).html(displayFileName(file.name));
		$(row.cells[2]).html(formatFileSize(file.size));
		$(row.cells[3]).html('<div class="bar"><div class="progress_bar"></div></div>');
		
	} catch (ex) {
		this.debug(ex);
	}
	
}

/**
 * 文件上传中(进度条功能)
 * @param file
 * @param bytesLoaded
 */
function uploadProgress(file, bytesLoaded) {
	try {
		var percent = Math.ceil((bytesLoaded / file.size) * 100);
		
		$("#"+this.customSettings.tr_id).find(".progress_bar").css("width",percent+"%");
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
		
		file.attachmentId = $.parseJSON(serverData).attachmentId;
		var row = document.getElementById(this.customSettings.tr_id);

		$(row.cells[1]).html(['<input type="hidden" id="attachmentId_'+this.customSettings.tr_id+'" value='+file.attachmentId+' /><a href="',default_config.download_url,'&attachmentId=',file.attachmentId,'">',displayFileName(file.name),'</a>'].join(""));
		var _fileState = new StringBuffer();
		
		var fileType = this.customSettings.tr_id;
		_fileState.append("<input type='hidden' id='attachmentId' name='attachmentId' value='").append(file.attachmentId).append("'/>");
		_fileState.append("<input type='hidden' id='fileType' name='fileType' value='").append(fileType).append("'/>");
		_fileState.append("<input type='hidden' id='fileSize' name='fileSize' value='").append(formatFileSize(file.size)).append("'/>");
		_fileState.append("<font color='green'>文件上传完成</font>");

		$(row.cells[3]).html(_fileState.toString());

		//启用删除按钮
		var trId = this.customSettings.tr_id;
		$("#delete"+trId).bind("click", function(){
			$(row.cells[1]).html('&nbsp;');
			$(row.cells[2]).html('&nbsp;');
			$(row.cells[3]).html('&nbsp;');
			$("#attr"+trId).show();

		});

		$("#attr"+this.customSettings.tr_id).hide();
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadComplete(file) {
	
	try {
		//上传文件数+1
		/*  I want the next upload to continue automatically so I'll call startUpload here */
		//$("#saveFile").click();
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
	return fileName.substr(len+1,fileName.length); 
}  

/**
 * 文件名太长...显示
 * @param fileName
 * @returns
 */
function displayFileName(fileName){	
	if(fileName.length > 20){
		var _name = new StringBuffer();
		_name.append("<span style='cursor:pointer' title='").append(fileName).append("'>");
		_name.append(fileName.substring(0,20)).append("……").append("</span>");
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
	return size.toFixed(2)+unit;
}

$(function(){
	$(".can_btn").hover(
	  	function () {
			$(".can_btn").css("background-position","left -21px");
	  	},
	  	function () {
	    	$(".can_btn").css("background-position","left top");
	  	}
	);	
});
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
 *      file_size_limit:'',//默认1024M
 *      file_types:'',//默认*.*
 *	}
 */
function fileUpload(_config){

	var config  = {
		upload_url: default_config.upload_url,
		file_size_limit :"20 MB",	// 1000MB
		file_types:"*.doc;*.docx;*.xls;*.xlsx;*.txt;*.jpg;*.gif;*.png;*.rar;*.zip;*.tif;*.pdf",//文件类型
		file_upload_limit:10,
		file_queue_limit:10,
		file_queue_error_handler : fileQueueError,//上传文件错误时触发
		file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
		file_queued_handler : fileQueued,//选择完文件后就触发
		upload_progress_handler : uploadProgress,
		upload_error_handler : uploadError,//上传错误触发
		upload_success_handler : uploadSuccess,
		upload_complete_handler : uploadComplete,
		flash_url : default_config.script_path+"swfupload.swf",
		button_placeholder_id : _config.upload_file,
		button_image_url: default_config.script_path+"images/btn2.gif",
		button_text : '<span class="theFont">上 传</span>',
		button_width: 58,
		button_height: 21,
		button_text_left_padding: 12,
		custom_settings : {
			tr_id: _config.tr_id
		}//,debug:true
	};

	swfUpload = new SWFUpload(config);
		
}

