<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<LINK rel=stylesheet type=text/css href="${rc.getContextPath()}/theme/frame/css/public.css">
<LINK rel=stylesheet type=text/css href="${rc.getContextPath()}/theme/frame/css/frame.css">
<STYLE type=text/css>
BODY {
	BACKGROUND: #bee2e7;
}
</STYLE>

<script language=javascript>
var COOKIE_NAME = "x";
var CACHE_DATA_KEY = "cData";
var CACHE_DATA_NAME = "cDataName";
var flag = false;

//获取cookie值
function getCookie(sName) {
	var aCookie = document.cookie.split("; ");
	for (var i=0; i < aCookie.length; i++) {
		var aCrumb = aCookie[i].split("=");
		if (sName == aCrumb[0])
		return unescape(aCrumb[1]);
	}
	return "";
}

//从缓存中导入数据
function loadDataFromCache() {
	var cacheData = document.getElementById("cacheData");
	if(cacheData.getAttribute(CACHE_DATA_NAME)!=null)
		return cacheData.getAttribute(CACHE_DATA_NAME);
	else
		return "";
}

//从缓存中删除数据
function removeDataFromCache() {
	try {
		var cacheData = document.getElementById("cacheData");
		cacheData.removeAttribute(CACHE_DATA_NAME);
		cacheData.save(CACHE_DATA_KEY);
	} catch(e) {};
}

//恢复本地的数据
function restoreLocale() {
	var cv = getCookie(COOKIE_NAME);
	var str = loadDataFromCache();
	if (cv && str && str.length>0) {
		var frame_obj = str.split(",");

		var layoutObj = new Array();
		for (i=0; i<frame_obj.length; i++) {
			var piece = frame_obj[i].split("^");
			if (piece[0]==window.name) continue;
			eval("top." + piece[0] + ".location = '" + piece[1] + "'");
		}
	}
	removeDataFromCache();
}

//点击头部页面隐藏或打开
function shift_status(id){
	var rightFrame = top.document.getElementsByName("myFrame")[0];
	if(flag){
		id.className="open";
		id.title="关闭菜单栏目";
		if(screen.height>768)
			rightFrame.cols = "210,11,*";
		else if(screen.height>600)
			rightFrame.cols = "210,11,*";
		else
			rightFrame.cols = "210,11,*";
	}
	else{
	id.className="close";
	id.title="打开菜单栏目";
		rightFrame.cols = "0,11,*";
	}
	flag = !flag;
	id.blur();
}
</script>

</head>

<body>
<div class="switch"><a class="open" title="关闭菜单栏目" onclick="shift_status(this);" href="#">左侧栏目开关</a> 
</div>
</body>
</html>
