var map = new Map(),globalIndex = 0;

$.extend($.messager.defaults,{
    ok:"确定",
    cancel:"取消"
});

$(function(){
	//核查内容添加滚动条
	$('.exa-cont').niceScroll({
	    cursorcolor: "rgba(0,0,0,.2)",//#CC0071 光标颜色
	    cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
	    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
	    cursorwidth: "5px", //像素光标的宽度
	    cursorborder: "0", // 游标边框css定义
	    cursorborderradius: "5px",//以像素为光标边界半径
	    autohidemode: false //是否隐藏滚动条
	});
	
	$(document).click(function(e){
		var calendar = $('#my-calendar'),
			$dateIpt = $('#datePicker');
		
		if (!($dateIpt.is(e.target) || $dateIpt.has(e.target).length)) {
			if(!calendar.is(e.target) && calendar.has(e.target).length === 0){
				calendar.css('display','none');
			}
		}
		
	});
	

	
	$('#datePicker').click(function(){
		if ($('#my-calendar').find('table[id="myCalendar"]').length) {
			$('#my-calendar').css('display','block');
		} else {
			var width = $(this).width(),
			top = $(this).offset().top + 40;
			$('#my-calendar').css({'top':top+'px','position':'absolute','z-index':'99999','background':'white','border':'#c5d9e8 1px solid'});
			new MyCendae(document.body,{tableWidth:width+'px',tableHeight:'300px',event:'onclick',callBackFunc:'dateCallBackFunc(this);' });
		}
	});
	
	$('div[id="attas"]').find('div.pic_content').each(function(index) {
		$(this).hover(function() {
			$('.pic_content').eq(index).find('.off_btn').removeClass('dn');
		}, function() {
			$('.pic_content').eq(index).find('.off_btn').addClass('dn');
		});
	});
	


	new AjaxUpload($("#chooeseAtta"),{//异步上传
		action: base + '/file/uploadFile.jhtml?limitSize=5',
		name: 'uploadFile',
		onSubmit : function(file, ext){
			/* var exts = "jpg|png",paths = "|";
			if ( !RegExp( "\.(?:" + exts + ")$$", "i" ).test(file)){
				$.messager.alert("上传失败","只能上传以下类型：" + exts.replace(/\|/g,"或"));
				return false;
			} */
	 	},
	 	onComplete: function(file,response,statusText){
			//var jsonData = $.parseJSON(response);
			if(response.indexOf('<pre')>=0){
				var reg = /<pre.+?>(.+)<\/pre>/g;//去除浏览器自带pre标签,擦
				var result = response.match(reg);
				response = RegExp.$1;
			}
			var jsonData = $.parseJSON(response);
			if(jsonData.result=='success'){
				var att = jsonData.resultList[0];
				//console.log(att);
				var index = (globalIndex++);
				var html = '';
				html +='<div class="pic pic_content" id="pic_'+index+'">';
				html +='	<span class="wl_upload">';
				html +='		<img src="'+getFilePath(att.path)+'" style="width: 56px;height:36px"/>';
				html +='		<p title="'+att.fileName+'">'+att.fileName+'</p>';
				html +='	</span>';
				html +='	<div class="off_btn displ dn"></div>';
				html +='	<input type="hidden" name="attList['+index+'].title" value="'+att.fileName+'" />';
				html +='	<input type="hidden" name="attList['+index+'].fileName" value="'+att.fileName+'" />';
				html +='	<input type="hidden" name="attList['+index+'].filePath" value="'+att.path+'" />';
				html +='</div>';
				$("#attas").append(html);
        	}else if(jsonData.result=='limit'){
        		$.messager.alert("上传失败","原因：文件大于"+jsonData.limit+"M");
        	}else{
        		$.messager.alert("上传失败","原因："+jsonData.result);
        	}
		}   
	});
	
	$('div[id="attas"]').find('div.pic_content').each(function(index) {
		$(this).hover(function() {
			$('.pic_content').eq(index).find('.off_btn').removeClass('dn');
		}, function() {
			$('.pic_content').eq(index).find('.off_btn').addClass('dn');
		});
	});
	
	//删除附件
	$("#attas").on("click",".off_btn",function(){
		$pObj = $(this).parent();
		$.messager.confirm('提示','确定删除该附件?',function(r){
		    if (r){
		    	$pObj.remove();
		    }
		});
	});
	

});



//ajax请求封装方法
function ajaxPostRequest(url,paramObj,callBackFunc) {
	modleopen('数据加载中，请稍后。。。',true);
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

/*function chooseAll(node) {
	var isChecked = $(node).is(':checked');
	$('ul.wl-files-area').find('input').each(function(){
		$(this).prop("checked",isChecked);
	});
}*/



/**
 * 日期选择回调方法
 * @param node
 */
function dateCallBackFunc(node){
	var currDate =  new Date().format('yyyy-MM-dd'),
		dateSelected = node.getAttribute('data-date');
	if (dateSelected <= currDate) {
		$.messager.alert("警告",'完成时限必须大于当前日期',"warning");
	} else {
		var diffDays = datedifference(dateSelected,currDate);
		$('.wl-files-date-c').html('（共'+diffDays+'个工作日）');
		$('#datePicker').val(dateSelected);
		$('#my-calendar').css('display','none');
	}
}

/**
 * WdatePicker.js
 * 日期选择回调方法
 */
function pickedCallBack() {
	var currDate =  new Date().format('yyyy-MM-dd'),
		dateSelected = $('#completeDate').val();
	if (dateSelected <= currDate) {
		$('#datePicker').val('');
		$.messager.alert("警告",'完成时限必须大于当前日期',"warning");
	} else {
		var diffDays = datedifference(dateSelected,currDate);
		$('.wl-files-date-c').html('（共'+diffDays+'个工作日）');
	}
}


function getFilePath(path) {
	path = path.toLowerCase();
	//图片类型
	if (path.endWith('bmp') || path.endWith('.jpg')
			|| path.endWith('.jpeg') || path.endWith('.png')|| path.endWith('.gif')) {
		return path;
	} else if (path.endWith('.xls') || path.endWith('.xlsx') || path.endWith('.xlsm') || path.endWith('.xltx')) { //excel类型
		return base + '/ui/workorder/img/icon_wl_add_excel.png';
	} else if (path.endWith('.docx') || path.endWith('.docm') || path.endWith('.dotx') || path.endWith('.doc')) { //word类型
		return base + '/ui/workorder/img/icon_wl_add_word.png';
	} else if (path.endWith('.pptx') || path.endWith('.pptm') || path.endWith('.ppsx') || path.endWith('.ppt')) { //ppt类型
		return base + '/ui/workorder/img/icon_wl_add_ppt.png';
	}
	
	return base + '/ui/workorder/img/icon_wl_add_word.png';
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
		top : type ? ($(document).height() - 500) / 2 : $(document).height() / 2 + 100
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