/**
 * 默认配置
 * 默认操作类为cn.ffcs.zhsq.servlet.UploadFileServlet
 */
var default_config = {
	upload_url:'/upFileServlet?method=up&moudle=attr',
	download_url:'/upFileServlet?method=down',
	delete_url:'/upFileServlet?method=delete',
	script_path:'/scripts/updown-1.1/swfupload/',
	button_action:'SWFUpload.BUTTON_ACTION.SELECT_FILES',
	multi_file_nums : 'multi'//single:单附件上传，multi:多附件上传
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
 * 上传文件弹出窗口，窗口关闭触发
 * @param numFilesSelected
 * @param numFilesQueued
 */
function fileDialogComplete(numFilesSelected, numFilesQueued) {
	
	try {
		if (numFilesQueued > 0) {
			document.getElementById(this.customSettings.cancel_upload).disabled = "";
			$('#'+this.customSettings.cancel_upload).attr('class', 'can_btn')
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
		//alert("是否支持多附件上传："+this.customSettings.multi_file_nums);
		//alert("已存在文件个数："+$("#fileNum_"+this.customSettings.cancel_upload).val())
		if(this.customSettings.multi_file_nums != 'multi' && ($("#fileNum_"+this.customSettings.cancel_upload).val()==1)){
			alert("文件上传数超过限制");
		}else{
			addFile({
				type:'add',
				fileId:file.id,
				fileName:file.name,
				fileSize:formatFileSize(file.size),
				table:this.customSettings.upload_table,
				cancel_button:this.customSettings.cancel_upload
			},'addFile');
		}
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
		
		var fileType = this.customSettings.fileType;
		_fileState.append("<input type='hidden' id='attachmentId' name='attachmentId' value='").append(file.attachmentId).append("'/>");
		_fileState.append("<input type='hidden' id='filePath' name='filePath' value='").append(file.filePath).append("'/>");
		_fileState.append("<input type='hidden' id='fileType' name='fileType' value='").append(fileType).append("'/>");
		_fileState.append("<input type='hidden' id='fileSize' name='fileSize' value='").append(formatFileSize(file.size)).append("'/>");
		_fileState.append("<font color='green'>文件上传完成</font>");
		
		$(row.cells[3]).html(_fileState.toString());

		//添加下载按钮
		var _downFile = new StringBuffer();
		_downFile.append('<img src="').append(default_config.script_path).append('images/icon/folder_del.gif" width="16" height="16" ')
		   .append('style="cursor:pointer;vertical-align:middle;"')
		   .append('id=img_').append(file.attachmentId)
		   .append(' onclick="deleteFile(').append(file.attachmentId).append(',\'').append(cancel_button).append('\');" alt="删除文件" title="删除"/>&nbsp;');
		
		_downFile.append('<a href="').append(this.customSettings.download_url)
				 .append('&attachmentId=').append(file.attachmentId).append('" target="_blank">');
		_downFile.append('<img src="').append(default_config.script_path);
		_downFile.append('images/icon/down.png" width="16" height="16" style="vertical-align:middle;" alt="查看文件" title="查看"/></a>');
		$(row.cells[4]).append(_downFile.toString());
		
		$("#img_"+file.attachmentId).closest("tr").attr("id", file.attachmentId);//为了使得新增的图片的删除功能和取消上传功能有效  20140705
		$("#img_"+file.attachmentId).closest("tr").attr("path", file.filePath);
		
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
	return fileName.substr(len+1,fileName.length); 
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
 * 		delFileMethod:''
 * }
 */
function addFile(result,type){
	
	var imgStr = "bmp,doc,docx,txt,xls,tif,rtf,xml,ppt,ppx,png,pdf,jpg,gif";
	var suffix = getFileSuffix(result.fileName);
	if(imgStr.indexOf(suffix)<0){
		suffix = "epub";
	}
	
	var row = new StringBuffer();
	row.append('<tr id="').append(result.fileId).append('" type="').append(result.type).append('" path="').append(result.filePath+'">');
	row.append('<td style="width: 20px;" align="center">');
	row.append('<img src="').append(default_config.script_path).append('images/icon/icon_').append(suffix).append('.gif" width="16" height="16" />');
	row.append('</td>');
	row.append('<td align="left">').append(displayFileName(result.fileName)).append('</td>');
	//row.append('<td width="50" align="center">').append(result.fileSize).append('</td>');
	row.append('<td></td>');
	row.append('<td style="width: 150px;" align="center">');
	if(result.type=='add'){
		row.append('<div class="bar"><div class="progress_bar"></div></div>');//新增添加进度条
	}
	row.append('</td>');
	row.append('<td style="width: 100px;" align="center">');
	if(result.type != 'detail' && type==null){//文件详情无下载图片
		row.append('<img src="').append(default_config.script_path).append('images/icon/folder_del.gif" width="16" height="16" ')
		   .append('style="cursor:pointer;vertical-align:middle;"')
		   .append(' onclick="deleteFile(').append(result.fileId).append(',\'').append(result.cancel_button).append('\');" alt="删除文件" title="删除"/>&nbsp;');

	} 
	if('add' != result.type){
		row.append('<a href="').append(result.download_url).append('&attachmentId=').append(result.fileId).append('" target="_blank" >')
		   .append('<img src="').append(default_config.script_path).append('images/icon/down.png" width="16" height="16"')
		   .append(' style="vertical-align:middle;" alt="查看文件" title="查看" /></a>');		 
	}
	row.append('</td>');
	
	$("#"+result.table).append(row.toString());
	
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
					deleteFile_(element.id, _cancel_button);
				}
		    });
		}
	});
}
/**
 * 删除文件
 * @param id 文件ID
 */
function deleteFile(id, _cancel_button){
	$.messager.confirm('提示', '您确定删除该文件吗？', function(r){
		if(r){
			deleteFile_(id, _cancel_button);
		}
	});
}

function deleteFile_(id, _cancel_button){
	
	if(typeof id  == 'object'){
		id = id.id;
	}
	
	var type = $("#"+id).attr("type");

	$.ajax({
		type: "POST",
	   	url: default_config.delete_url,
	   	data: {'attachmentId':id},
	   	success: function(msg){
			if(msg=='-1'){
				alert('删除附件失败');
			}else{
				$("#"+id).remove();

				//删除成功后，文件数-1
				$("#fileNum_"+_cancel_button).val(parseInt($("#fileNum_"+_cancel_button).val())-1);
				if($("#fileNum_"+_cancel_button).val() <= 0){//文件数为0时，
					$("#"+_cancel_button).attr("disabled","disabled");//使“取消上传”按钮不可用
					$("#"+_cancel_button).attr('class', 'can_btn3');
				}
			}
	   	}
	});
	
	var stats =swfUpload.getStats();
	stats.successful_uploads = 0;
	swfUpload.setStats(stats);
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
function getJSessionId() {
	var data = {};
	var host = document.location.href;
	var i = host.indexOf('/');
	i = host.indexOf('/', i + 1);
	i = host.indexOf('/', i + 1);
	i = host.indexOf('/', i + 1);
	host = host.substring(0, i);
	$.ajax({
		type : "POST",
		url : host + "/sys/getSessionId.jhtml",
		dataType : 'json',
		async : false,
		cache : false,
		success : function(result) {
			if (result) {
				data.jSessionId = result.sessionId;
				data.userId = result.userId;
				data.partyName = result.partyName;
			} else {
				data.jSessionId = "";
				data.userId = "";
				data.partyName = "";
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			data.jSessionId = "";
			data.userId = "";
			data.partyName = "";
        }
	});
	if (data.jSessionId && data.jSessionId != "") {
		data.jSessionId = ";JSESSIONID=" + data.jSessionId;
	} else {
		data.jSessionId = "";
	}
	return data;
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
 *      file_num:''默认multi(多附件上传)，单附件上传为:single
 *	}
 */
function fileUpload(_config){
	var _data = getJSessionId();
	var _type = _config.type; 
	var _cancel_button = _config.cancel_button || 'cancel_button';
	var _upload_table = _config.upload_table || 'upload_table';
	if(_config.context_path!=null){
		default_config.upload_url=_config.context_path+'/upFileServlet'+_data.jSessionId+'?method=up&moudle=attr&___userId='+_data.userId+'&___partyName='+_data.partyName;
		default_config.download_url=_config.context_path+'/upFileServlet?method=down';
		default_config.delete_url=_config.context_path+'/upFileServlet?method=delete';
		default_config.script_path=_config.context_path+'/scripts/updown-1.1/swfupload/';
	}
	
	var fileSizeLimit = _config.file_size_limit||"10 MB";
	
	fileSizeLimit = fileSizeLimit.toUpperCase();
	
	if(fileSizeLimit.toLowerCase() == fileSizeLimit) {//添加默认单位
		fileSizeLimit += " KB";
	}
	
	//1、init  div
	var _content = new StringBuffer();
	if(_config.type != 'detail'){
		_content.append('<div class="FontRed" style="color:red; margin-bottom:5px;">')
				.append('	<span class="warning_tips" style="margin-bottom:-2px;"></span>')
				.append('	文件大小不能超过').append(fileSizeLimit)
				.append('</div>');

		_content.append('<div id="upload_file"></div>&nbsp;&nbsp;&nbsp;&nbsp;');
		if(_config.noDiplay){
		_content.append('<input id="').append(_cancel_button).append('"  style="display:none;" type="button" value=""');
		_content.append('onclick="cancelUpload(\'').append(_upload_table).append('\',\'').append(_cancel_button).append('\');" class="can_btn" disabled="disabled"/>');
 		}else{
 		_content.append('<input id="').append(_cancel_button).append('" type="button" value=""');
		_content.append('onclick="cancelUpload(\'').append(_upload_table).append('\',\'').append(_cancel_button).append('\');" class="can_btn3" disabled="disabled"/>');
 		}
			

		_content.append('<input type="hidden" id="fileNum_'+_cancel_button+'" name="fileNum" value="0"/>');
	}
	if(_config.noDiplay){
		_content.append('<table id="').append(_upload_table).append('" style="width: 330px !important;display:none;" class="upload_table">');
	}else{
		_content.append('<table id="').append(_upload_table).append('" style="width: 330px !important;" class="upload_table">');
	}

	/*_content.append('<tr><th colspan="2" width="250" align="left" style="padding-left:20px;">文件名称</th>')
			.append('<th width="50">大小</th><th width="100">状态</th>');
	_content.append('<th width="100">操作</th></tr></table>');*/
	$("#"+_config.positionId).html(_content.toString());
	
	//2、init data
	if(_config.initType=='ajax'){
		//AJAX从Action获取Json Data并加载
		$.ajax({
			type: "POST",
		   	url: _config.ajaxUrl,
		   	data: _config.ajaxData||{},
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
						fileId:this.attachmentId,
						fileName:this.fileName,
						fileSize:formatFileSize(this.fileSize),
						filePath:this.filePath,
						table:_upload_table,
						file_upload_limit:_config.file_upload_limit,
						cancel_button:_cancel_button,
						download_url : _config.download_url || default_config.download_url
					});
				});
				
				
		   	}
		});
	} else if(_config.initType == 'hidden'){
		//从隐藏域获取数据并加载
		var result = $.parseJSON($("#"+_config.hiddenId).val());
		$(result).each(function(e){
			addFile({
				type:_type,
				fileId:this.attachmentId,
				fileName:this.fileName,
				fileSize:this.fileSize,
				filePath:this.filePath,
				file_upload_limit:_config.file_upload_limit,
				table:_upload_table,
				cancel_button:_cancel_button,
				download_url : _config.download_url || default_config.download_url
			});
		});
	}
	
	//3、 init fileupload comp
	if(_type != 'detail'){
		var config  = {
			upload_url: _config.upload_url||default_config.upload_url,
			// File Upload Settings
			//指定要上传的文件的最大体积，可以带单位，合法的单位有:B、KB、MB、GB(大小写均可)，如果省略了单位，则默认为KB。该属性为0时，表示不限制文件的大小。
			//设置值需为正整数，设置小数时，只取整数部分，设置负值及非数字时，设置为0
			file_size_limit : fileSizeLimit,	
			file_types:_config.file_types||"*.doc;*.docx;*.xls;*.xlsx;*.txt;*.jpg;*.gif;*.png;*.rar;*.zip;*.tif;*.pdf",//文件类型
			//file_types_description : "产品LOGO",
			file_upload_limit:_config.file_upload_limit||50,
			//file_queue_limit:10,
			//file_dialog_start_handler:fileDialog,
			file_queue_error_handler : fileQueueError,//上传文件错误时触发
			file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
			file_queued_handler : fileQueued,//选择完文件后就触发
			upload_progress_handler : uploadProgress,
			upload_error_handler : uploadError,//上传错误触发
			upload_success_handler : uploadSuccess,
			upload_complete_handler : uploadComplete,
			// Flash Settings
			flash_url : default_config.script_path+"swfupload.swf",
			//button_action : _config.button_action || default_config.button_action,
			button_placeholder_id : "upload_file",
			button_image_url: default_config.script_path+"images/upload.png",
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
				multi_file_nums : _config.multi_file_nums || default_config.multi_file_nums,
				download_url : _config.download_url || default_config.download_url
			}//,debug:true
		};
		swfUpload = new SWFUpload(config);
		//alert("支持上传文件个数："+_config.file_upload_limit);
		return swfUpload;
	}
}

