var __IS_READY = false;
///字典
(function ($) {
    var gDict = {
        dict: {},
        count:0,
        isReady: false,
        clearId: null,
        add: function (columnName, dataMap) {
            if (!this.getDataMap(columnName)) {
                this.count = this.count+1;
            }
            this.dict[columnName] = dataMap;
        },
        remove: function(columnName){
             if (this.getDataMap(columnName)) {
                delete this.dict[columnName];
                this.count = this.count-1;
                return true;
            }
            return false;
        },
        addColumn: function (columnName, columnValue, columnText) {
            var map = this.getDataMap(columnName);
            if (!map) {
                map = {};
                this.add(columnName, map);
            }
            map[columnValue] = columnText;
        },
        loadData: function(data, valueName, textName){
            var me = this;
            if(data){
			    for(var i=0,len=data.length; i<len; i++){
				    var dic = data[i];
				    if(dic && dic["dictionaryId"]){
					    var dictionaryId = dic["dictionaryId"];
					    var dictionaryData = dic["dictionaryItems"];
                        if(dictionaryData!=null){
					        for(var j=0,len2=dictionaryData.length; j<len2; j++){
						        var dictionaryItem = dictionaryData[j];
                                var value = (valueName && dictionaryItem[valueName])? dictionaryItem[valueName]: (dictionaryItem["value"]? dictionaryItem["value"] : null);
                                var text = (textName && dictionaryItem[textName])? dictionaryItem[textName]: (dictionaryItem["text"]? dictionaryItem["text"] : null);
						        me.addColumn(dictionaryId, value, text);
					        }
                        }
				    }
			    }
			    if(me.isReady) me._initForInnerHtml();
		    }
        },
        getDataMap: function (columnName) {
            return this.dict[columnName];
        },
        getColumnText: function (columnName, value) {
            var map = this.getDataMap(columnName);
            if (!map) return value;
            if (map[value]) //如果不存在则 返回原来的值。
            	return map[value];
            else
            	return value;
        },
        hasInit: false,
        _initForInnerHtml: function () {//翻译字典界面
            var g = this;
            if (g["isReady"]==true && window.jQuery && g.count>0) {
                var dict = g.dict, map;
                for (var o in dict) {
                    map = dict[o];
                    jQuery("*[stringMap='" + o + "']").each(function () {
                        var value = jQuery(this).text();

                        if(value.indexOf(",") > 0){
                           var arr1 = value.split(",");
                           if(arr1.length > 0){
                              var str1 = "";
                              $.each(arr1, function(i,v1){
                                 if(v1!='' && map[v1]){
                                    if(str1 != ""){str1 += ",";}str1 += map[v1];
                                 }
                              });
                              jQuery(this).html(str1);
                              jQuery(this).val(str1);
                           }
                        }else{
                           if(map[value]){
                              jQuery(this).html(map[value]);
                              jQuery(this).val(map[value]);
                           }
                        }
                    });
                }
                //this.hasInit = true;
            }

        },
        _initForSelectHtml: function () {//导入下拉菜单 例如 selectMap="EDUCATION_LEVEL"
            var g = this;
            if (g["isReady"]==true && window.jQuery && g.count>0) {
                var dict = g.dict, map;
                for (var o in dict) {
                    map = dict[o];
                    jQuery("*[selectMap='" + o + "']").each(function () {
                       var j=this;
                       var value = jQuery(this).attr("value");
                       $.each(map,function(k,v){
                       	   var option =  $("<option>").text(v).val(k);
	                       if(k==value){
	                       		option.attr('selected', 'true');
	                       }
                       	   $(j).append(option);
						});
					 $(j).combobox({});
                    });
                }
            }
        },
        parseText: function(){
            var g = this;
            if(g["clearId"]) window.clearTimeout(g["clearId"]);

            if(g["isReady"]==true){
                g._initForInnerHtml();
            }else{
                g["clearId"] = setTimeout(window["globalDictionary"].parseText,100);
            }
        }

    };
    window["globalDictionary"] = gDict;
    if(window["dictionaryData"]) gDict.loadData(window["dictionaryData"], "COLUMN_VALUE", "COLUMN_VALUE_REMARK");
    if(window["newDictionaryData"]) gDict.loadData(window["newDictionaryData"], "value", "name");
    $(function () {
        window["globalDictionary"]["isReady"]=true;
       // window["globalDictionary"]._initForSelectHtml();
        window["globalDictionary"]._initForInnerHtml();

    });
})(jQuery);
//列表映射 例如：  {field:'educationLevel',title:'文化程度',formatter:function(v){return format('EDUCATION_LEVEL',v);}}
function format(columnName,value){
	return window["globalDictionary"] ? window["globalDictionary"].getColumnText(columnName, value) : value;
}

function format2(columnName,value,value2){
	if(window["globalDictionary"].getColumnText(columnName, value) == value){
		return value2;
	}else {
		return window["globalDictionary"] ? window["globalDictionary"].getColumnText(columnName, value) : value;
	}
}


function formatValues(columnName,value){
	var label="";
	if(value){
		$.each(value.split(','),function(index,item){
			label+=format(columnName,item)+",";
		});
		label=label.substring(0, label.length-1);
	}
	return label;
}

$.extend({
	includePath: '',
	include: function(file) {
		var files = typeof file == "string" ? [file]:file;
		for (var i = 0; i < files.length; i++) {
			var name = files[i].replace(/^\s|\s$/g, "");
			var att = name.split('.');
			var ext = att[att.length - 1].toLowerCase();
			var isCSS = ext == "css";
			var tag = isCSS ? "link" : "script";
			var attr = isCSS ? " type='text/css' rel='stylesheet' " : " language='javascript' type='text/javascript' ";
			var link = (isCSS ? "href" : "src") + "='" + $.includePath + name + "'";
			if ($(tag + "[" + link + "]").length == 0) document.write("<" + tag + attr + link + "></" + tag + ">");
		}
	}
});


//datagrid
(function ($) {
	function intGridDataForStaticPage(grid, data){//grid静态前台翻页缓存纠正处理
        
        if(data && data["total"]){//符合数据格式的才做静态分页处理
          var pager = $(grid).datagrid('getPager');
          if(pager[0]){
        	var pageNo = pager.pagination('options').pageNumber;
            if (pageNo == 1){
				$(grid).attr("_lastTotal", data.total);
			}else{
			    if(data.rows.length>0 && (!data.total || data.total<1)){
					data.total = $(grid).attr("_lastTotal");
					if(typeof(data.total)=="string") data.total= parseInt(data.total);
				}
			}
          }
        } else {//不符合数据有可能是传入list集合
            if($.isArray(data)){//判断是否为list集合
                if (data.length) {
                    return {total: data.length, rows: data};
                } else {
                    return {total: 0, rows: []};
                }
            }
        }
		return data;
    }



    if($.fn.datagrid){
        try{
            $.fn.datagrid.defaults["pageSize"]=20;
            $.fn.datagrid.defaults["pageList"]=[10, 20, 30, 40, 50];

            $.fn.datagrid.defaults["loadFilter"] = function(data){
				return intGridDataForStaticPage(this, data);
            };


            $.fn.datagrid.defaults["onLoadSuccess"]=function(data) {
            	try {
            		intGridDataForStaticPage(this, data);

                	var newQueryParams =$.extend($(this).datagrid('options').queryParams,{ismark:0});
            		$(this).datagrid('options').queryParams = newQueryParams;
    			    $(this).datagrid('clearSelections');	//清除掉列表选中记录
     				$('.datagrid-body-inner').eq(0).removeClass("l_elist");
     				
    			    if(data.total==0){
    					$('.datagrid-body-inner').eq(0).addClass("l_elist");
    					$(this).parent().find('.datagrid-body').eq(1).append('<div class="nodata"></div>');
    				}
    			    if(typeof completedData === 'function' ){
    			    	completedData(data);
    			    }
    				//tooptip();
            	} catch(e) {//异常捕获，以使得后续操作能正常进行
            		console.log(e);
            	}
            };
            $.fn.datagrid.defaults["onLoadError"]=function(data){
            	$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="Error500"></div>');
            };
        }catch(e){}

        window["_init_pagination_for_datagrid"] = function(obj, options){
            var p = $(obj).datagrid('getPager');
            var opts = $.extend({},{
			    pageSize: 20,//每页显示的记录条数，默认为
			    pageList: [10, 20,30,40,50],//可以设置每页记录条数的列表
			    beforePageText: '第',//页数文本框前显示的汉字
			    afterPageText: '页    共 {pages} 页',
			    displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			    onBeforeRefresh:function(){
				    $(this).pagination('loading');
				    alert('before refresh');
				    $(this).pagination('loaded');
			    }*/
		    }, options);

            $(p).pagination(opts);
        }

    }
})(jQuery);



function formartDate(value){
	if(value){
		var date = new Date(value);
		return date.formatString("yyyy-MM-dd");
	}else{
		return value;
	}
}

function formartTime(value){
	if(value){
		var date = new Date(value);
		return date.formatString("yyyy-MM-dd HH:mm:ss");
	}else{
		return value;
	}
}

function getBasePath(){
	var scripts = document.getElementsByTagName('script'), i, ln, path, scriptSrc, match;
	for (i = 0, ln = scripts.length; i < ln; i++) {

		scriptSrc = scripts[i].src;

        match = scriptSrc.match(/js\/global\.js$/);

        if (match) {
            path = scriptSrc.substring(0, scriptSrc.length - match[0].length);
            break;
        }
    }
    return path;

}

function formatParameters(str, name, value){
	if(str){
		var funStr = "return '"+str+"'.replace(/\\\$\\\{"+name+"\\\}/ig, '"+value+"')";
		return new Function(funStr)();
	}
	return str;
}

Date.prototype.formatString = function (mask){
	var d = this;
	var zeroize = function (value, length)
	{
		if (!length) length = 2;
		value = String(value);
		for (var i = 0, zeros = ''; i < (length - value.length); i++)
		{
			zeros += '0';
		}
		return zeros + value;
	};

	return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0)
	{
		switch ($0)
		{
			case 'd': return d.getDate();
			case 'dd': return zeroize(d.getDate());
			case 'ddd': return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
			case 'dddd': return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
			case 'M': return d.getMonth() + 1;
			case 'MM': return zeroize(d.getMonth() + 1);
			case 'MMM': return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
			case 'MMMM': return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
			case 'yy': return String(d.getFullYear()).substr(2);
			case 'yyyy': return d.getFullYear();
			case 'h': return d.getHours() % 12 || 12;
			case 'hh': return zeroize(d.getHours() % 12 || 12);
			case 'H': return d.getHours();
			case 'HH': return zeroize(d.getHours());
			case 'm': return d.getMinutes();
			case 'mm': return zeroize(d.getMinutes());
			case 's': return d.getSeconds();
			case 'ss': return zeroize(d.getSeconds());
			case 'l': return zeroize(d.getMilliseconds(), 3);
			case 'L': var m = d.getMilliseconds();
				if (m > 99) m = Math.round(m / 10);
				return zeroize(m);
			case 'tt': return d.getHours() < 12 ? 'am' : 'pm';
			case 'TT': return d.getHours() < 12 ? 'AM' : 'PM';
			case 'Z': return d.toUTCString().match(/[A-Z]+$/);
			// Return quoted strings with the surrounding quotes removed
			default: return $0.substr(1, $0.length - 2);
		}
	});
};

function getWHeight(minHeight,maxHeight){
	var wHeight = $(document).height();
	if(wHeight>= maxHeight+30){
		return maxHeight;
	}
	return minHeight;
}

String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
		return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
	} else {
		return this.replace(reallyDo, replaceWith);
	}
};

function fmtMessage(value,rec,index){
	if(value!=null&&value!=""){
		if(value.length<10){
			return value;
		}else{
			var replaceStr = "\n";
			value = value.replace(new RegExp(replaceStr,'gm'),'<br/>');
			var content =$('<div></div>');
			var a_label = $('<a href="###" style="text-decoration:none;color:#000;" class="note"></a>');
			a_label.attr("title",value);
			a_label.html(value.substring(0,9)+'…');
			content.append(a_label);
			return content.html();
		}
	}else{
		return "";
	}
}


//function tooptip(){
//	$(".note").each(function(i,n){
//		var title = $(n).attr("title");
//			position: 'bottom',
//			content: title,
//			onShow: function(){
//				alert("123");
//				$(this).tooltip('tip').css({
//					borderColor: '#ff0000'
//				});
//			}
//		});
//	});
//}

function trunMap(a){
	var m = new Array();
	$.each(a, function(i, n){
	  m[n.name]=$.trim(n.value);
	});
	return m;
}

function getPhotoSize(obj){
	photoExt=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
	if(photoExt!='.jpg'&&photoExt!='.jpeg'&&photoExt!='.png'&&photoExt!='.bmp'&&photoExt!='.gif'){
		alert("请上传后缀名为jpg,jpeg,png,bmp,gif的照片!");
		obj.value='';
		return false;
	}
	var fileSize = 0;
	var isIE = /msie/i.test(navigator.userAgent) && !window.opera;
	if (isIE && !obj.files) {
		var filePath = obj.value;
		var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
		var file = fileSystem.GetFile (filePath);
		fileSize = file.Size;
	}else {
		fileSize = obj.files[0].size;

	}
	fileSize=Math.round(fileSize/1024*100)/100; //单位为KB
	if(fileSize>=10000){
		alert("照片最大尺寸为10M，请重新上传!");
		obj.value='';
		return false;
	}
}

String.prototype.startWith=function(str){
	var reg=new RegExp("^"+str);
	return reg.test(this);
}

String.prototype.endWith=function(str){
	var reg=new RegExp(str+"$");
	return reg.test(this);
}

function showUnableDiv(appendId) {
	$("<div class='unableDiv'></div>").appendTo(
		appendId).css({
		width: '100%',
		height:'100%',
		position: "absolute",
		display : "block",
		left:0,
		top:0,
		'z-index': 100,
		'background-color':'#000',
		opacity: 0.3
	});
}
function closeUnableDiv() {
	$(".unableDiv").css( {
		display : "none"
	});
	$(".unableDiv").remove();
}


/** 封装弹窗界面 ================================================================================================ */

var windowPanelHelper = (function($){

	var pref = "wd_";
	function _getPanel(name){
		return $("#"+ pref + name)[0];
	}

	var _stackMap = [];
	function _inStack(name){
		var key = pref+name;
		if(_stackMap[key]){//已经存在先移除
			for(var i=_stackMap.length-1; i>-1; i--){
				if(_stackMap[i]==name){
					_stackMap.splice(i, 1);
					break;
				}
			}
		}
		_stackMap.push(name);
		_stackMap[key] = true;
	}

	function _outStack(name){
		var key = pref+name;
		if(_stackMap[key]){//已经存在
			for(var i=_stackMap.length-1; i>-1; i--){
				if(_stackMap[i]==name){
					_stackMap.splice(i, 1);
					break;
				}
			}
		}
		_stackMap[key] = false;
	}

	function _initParams(param){
		var p = param||{};
		if(!p["width"]) {
			var w =$(window).width();
			if(p["minWidth"] && w<p["minWidth"]){
				p["width"] = p["minWidth"];
			}else if(p["maxWidth"] && w>p["maxWidth"]) {
				p["width"] = p["maxWidth"];
			}else{
				p["width"] = w;
			}
		}
		if(!p["height"]) {
			var h =$(window).height();
			if(p["minHeight"] && h<p["minHeight"]){
				p["height"] = p["minHeight"];
			}else if(p["maxHeight"] && h>p["maxHeight"]) {
				p["height"] = p["maxHeight"];
			}else{
				p["height"] = h;
			}
		}

		if(!p["title"]) {
			p["title"] = "信息窗口";
		}

		if(!p["left"]) {
			p["left"] = parseInt(($(window).width()-param["width"])*0.5+$(document).scrollLeft());
		}
		if(!p["top"]) {
			p["top"] = Math.abs(parseInt(($(window).height()-param["height"])*0.5+$(document).scrollTop()));
		}
		if (!p["scrolling"]) {
			p["scrolling"] = 'auto';
		}

		return p
	}

	function _parseOptions(name, param){
		var _onclose = function(){
			if(name)_outStack(name);
			if(param && param["onClose"]) param["onClose"]();
		}

		return $.extend({
		    shadow: true,
		    modal: true,
			onClose:_onclose,
		    closed: true,
		    draggable: false,
		    minimizable: false,
		    maximizable: false,
		    collapsible: false
		}, _initParams(param));
	}

	function _hidePanel(name, isClear){//name空，关闭最后的弹窗
		if(name){
			var t = _getPanel(name);
			if(t){
				var pn = $(t);
				pn.window('close');

				return pn;
			}
		}else{
			if(_stackMap.length>0){
				return _hidePanel(_stackMap[_stackMap.length-1]);
			}
		}
		return null;
	}

	function _hideAllPanel(isClear){
		for(var i=0, l=_stackMap.length; i<l; i++){
			_hidePanel(_stackMap[i], isClear);
		}
	}

	function _showPanel(name, url, param){
		var t = _getPanel(name), pn, opts;
		if(!t){
			pn = $('<div id="'+ pref+name +'"></div>').appendTo(document.body);
			t = pn[0];
			opts = _parseOptions(name,param);
			pn.window(opts);

		}else{
			pn = $(t);
			opts = $.data(t, "window");
			if(param){
				pn.window($.extend(opts, _initParams(param)));
			}
		}
		if(url){
			pn.empty().html("<iframe scrolling='"+opts["scrolling"]+"' frameborder='0' src='"+url+"' style='width:100%;height:100%;'></iframe>");
		}
		pn.window('open');
		_inStack(name);

		return pn;
	}

	function _getOpenWins(){
		var len = _stackMap.length;
		var wins =[];
		for(var i=0; i<len; i++){
			wins[i] = _getPanel(_stackMap[i]);
		}
		return wins;
	}

	return {
		getOpenWins: _getOpenWins,
		getWindow: _getPanel,
		hideWindow: _hidePanel,
		showWindow: _showPanel,
		hideAllWindow: _hideAllPanel
	}

})(jQuery);

//兼容之前的弹窗方法showMaxJqueryWindow
function showMaxJqueryWindow(title, targetUrl,width,height,scrolling){
	windowPanelHelper.showWindow(title, targetUrl, {title: title, width:width, height:height, scrolling: scrolling});
}
//兼容之前的弹窗方法showMaxJqueryWindowForCrossDomain
function showMaxJqueryWindowForCrossDomain(title, targetUrl,width,height){
	var left = parseInt(($(document).width() - width)/2) + $(document).scrollLeft();
	var top = parseInt($(document).scrollTop());
	windowPanelHelper.showWindow(title, targetUrl, {title: title, width:width, height:height, left:left, top: top});
}
//兼容之前的弹窗方法
function closeMaxJqueryWindow() {
	windowPanelHelper.hideWindow();
}
//兼容之前的弹窗方法
function modifyMaxJqueryWindowTitle(title) {
	//$MaxJqueryWindow.panel({title:title});
}

//兼容之前的弹窗方法showMaxJqueryWindows
function showMaxJqueryWindows(title, targetUrl,width,height, maximizable, scroll, callBackOnClose){
	var top = parseInt(($(window).height()-height)*0.5+$(document).scrollTop());
	var left = parseInt(($(window).width()-width)*0.5+$(document).scrollLeft());
	windowPanelHelper.showWindow(title, targetUrl, {title: title, width:width, height:height, left:left, top: top, maximizable: maximizable, scrolling: scrolling, onBeforeClose: callBackOnClose});
}
//兼容之前的弹窗方法closeMaxJqueryWindows
function closeMaxJqueryWindows(){////关闭最后一个窗口
	windowPanelHelper.hideWindow();
}

//兼容之前的弹窗方法closeBeforeMaxJqueryWindow
function closeBeforeMaxJqueryWindow() {//关闭之前所有的窗口 需要在打开窗口方法调用之后使用
	windowPanelHelper.hideAllWindow();
}
//兼容之前的弹窗方法showMaxJqueryWindowTop
function showMaxJqueryWindowTop(title, targetUrl,width,height,scroll) {
	var top = 0;
	var left = parseInt(($(window).width()-width)*0.5+$(document).scrollLeft());
	windowPanelHelper.showWindow(title, targetUrl, {title: title, width:width, height:height, left:left, top: top, scrolling: scrolling});
}

