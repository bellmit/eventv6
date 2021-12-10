$.extend($.messager.defaults,{
    ok:"确定",
    cancel:"取消"
});

function viewOrDownLoad(fileUrl,type) {
	if (type && type != 'img') {
		fileUrl = base + '/file/downloadFile.jhtml?path='+fileUrl;
	}
	window.open(fileUrl);
}

//ajax请求封装方法
function ajaxPostRequest(url,paramObj,callBackFunc,isShowMask) {
	if (isShowMask)
		modleopen('接收地区数据加载中，请稍后。。。',true);
	$.ajax({
		type: 'post',
		url: base + url,
		data: paramObj,
		beforeSend:function(){},
		complete:function(){},
		success:function(data){
			callBackFunc(data);
		},
		error:function(){
			$.messager.alert('错误', '网络异常','error');
		}
	});
}

function getFilePath(path) {
	path = path.toLowerCase();
	//图片类型
	if (path.endWith('.bmp') || path.endWith('.jpg')
			|| path.endWith('.jpeg') || path.endWith('.png')|| path.endWith('.gif')) {
		return imgDomain + path;
	} else if (path.endWith('.xls') || path.endWith('.xlsx') || path.endWith('.xlsm') || path.endWith('.xltx')) { //excel类型
		return base + '/ui/workorder/img/icon_wl_add_excel.png';
	} else if (path.endWith('.docx') || path.endWith('.docm') || path.endWith('.dotx') || path.endWith('.doc')) { //word类型
		return base + '/ui/workorder/img/icon_wl_add_word.png';
	} else if (path.endWith('.pptx') || path.endWith('.pptm') || path.endWith('.ppsx') || path.endWith('.ppt')) { //ppt类型
		return base + '/ui/workorder/img/icon_wl_add_ppt.png';
	}
	
	return base + '/ui/workorder/img/icon_wl_add_word.png';
}

function getFileObj(path) {
	var fileObj = {'title':'点击下载该附件','fileType':'file'};
	path = path.toLowerCase();
	//图片类型
	if (path.endWith('.bmp') || path.endWith('.jpg')
			|| path.endWith('.jpeg') || path.endWith('.png')|| path.endWith('.gif')) {
		fileObj.path = imgDomain + path;
		fileObj.title = '点击预览该图片';
		fileObj.fileType = 'img';
	} else if (path.endWith('.xls') || path.endWith('.xlsx') || path.endWith('.xlsm') || path.endWith('.xltx')) { //excel类型
		fileObj.path = base + '/ui/workorder/img/icon_wl_add_excel.png';
	} else if (path.endWith('.docx') || path.endWith('.docm') || path.endWith('.dotx') || path.endWith('.doc')) { //word类型
		fileObj.path = base + '/ui/workorder/img/icon_wl_add_word.png';
	} else if (path.endWith('.pptx') || path.endWith('.pptm') || path.endWith('.ppsx') || path.endWith('.ppt')) { //ppt类型
		fileObj.path = base + '/ui/workorder/img/icon_wl_add_ppt.png';
	} else {
		fileObj.path = base + '/ui/workorder/img/icon_wl_add_word.png';
	}
	
	return fileObj;
}

//打开遮罩
function modleopen(info,type) {
	var msg = info || "正在处理，请稍候。。。";
	$("<div class='datagrid-mask'></div>").css( {
		display : "block",
		width : "100%",
		height : $(document).height()
	}).appendTo("body");
	$("<div class='datagrid-mask-msg'></div>").html(msg).appendTo(
			"body").css( {
		display : "block",
		left : ($(document.body).outerWidth(true) - 190) / 2,
		top : type ? ($(document).height() - 50) / 2 : $(document).height() / 2 + 100
	});
	document.body.scroll="no";//除去滚动条
}
//关闭遮罩
function modleclose() {
	$(".datagrid-mask").css( {
		display : "none"
	});
	$(".datagrid-mask-msg").css( {
		display : "none"
	});
	$(".datagrid-mask").remove();
	$(".datagrid-mask-msg").remove();
	document.body.scroll="yes";//开启滚动条
}


/*
 * MAP对象，实现MAP功能
 *
 * 接口：
 * size()     获取MAP元素个数
 * isEmpty()    判断MAP是否为空
 * put(key, value)   向MAP中增加元素（key, value) 
 * get(key)    获取指定KEY的元素值VALUE，失败返回NULL
 * containsKey(key)  判断MAP中是否含有指定KEY的元素
 *
 * var map = new Map();
 * map.put("key", "value");
 * var val = map.get("key")
 */
function Map() {
	this.elements = new Array();

	//获取MAP元素个数
	this.size = function() {
		return this.elements.length;
	};

	//判断MAP是否为空
	this.isEmpty = function() {
		return (this.elements.length < 1);
	};

	//删除MAP所有元素
	this.clear = function() {
		this.elements = new Array();
	};

	//向MAP中增加元素（key, value) 
	this.put = function(_key, _value) {
		this.elements.push({
			key : _key,
			value : _value
		});
	};

	//删除指定KEY的元素，成功返回True，失败返回False
	this.removeByKey = function(_key) {
		var bln = false;
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					this.elements.splice(i, 1);
					return true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	};

	//获取指定KEY的元素值VALUE，失败返回NULL
	this.get = function(_key) {
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					return this.elements[i].value;
				}
			}
		} catch (e) {
			return false;
		}
		return false;
	};

	//判断MAP中是否含有指定KEY的元素
	this.containsKey = function(_key) {
		var bln = false;
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					bln = true;
				}
			}
		} catch (e) {
			bln = false;
		}
		return bln;
	};
}

/*日期格式化
 * time: 时间毫秒数
 * 调用方式: new Date(time).format('yyyy-MM-dd hh:mm:ss')
*/
Date.prototype.format = function(f) {
    var o = {
        "M+" : this.getMonth() + 1, //month
        "d+" : this.getDate(), //day
        "h+" : this.getHours(), //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth() + 3) / 3), //quarter
        "S" : this.getMilliseconds()    //millisecond
    };
    if (/(y+)/.test(f))
        f = f.replace(RegExp.$1, (this.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
    for ( var k in o)
        if (new RegExp("(" + k + ")").test(f))
            f = f.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]: ("00" + o[k]).substr(("" + o[k]).length));
    return f;
};

//两个时间相差天数 
function datedifference(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式  
    var dateSpan, tempDate, iDays;
    sDate1 = Date.parse(sDate1);
    sDate2 = Date.parse(sDate2);
    dateSpan = sDate2 - sDate1;
    dateSpan = Math.abs(dateSpan);
    iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
    return iDays;
};

/* 
 * js 字符串endWith函数
*/
String.prototype.endWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substring(this.length-str.length)==str)
	  return true;
	else
	  return false;
	return true;
};
/* 
 * js 字符串startWith函数
*/
String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
	  return true;
	else
	  return false;
	return true;
};

function checkStrLenAndNull(tipInfo,str,len){
	if (str == null || str=="") {
		$.messager.alert('警告', tipInfo+'不能为空','warning');
		return false;
	} else {
		var strLen = str.match(/[^ -~]/g) == null ? str.length : str.length + str.match(/[^ -~]/g).length;
		var isValid = strLen <= len;
		if (!isValid) {
			$.messager.alert('警告', tipInfo+'不能超过'+len+'个字符','warning');
		}
		return  isValid;
	}
}