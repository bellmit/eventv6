//删除左右两端的空格
function trim(str){
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
//计算字符串长度
function len(str){ 
	var i,sum=0; 
	for(i=0;i<str.length;i++){ 
	if((str.charCodeAt(i)>=0) && (str.charCodeAt(i)<=255)) 
	sum=sum+1; 
	else 
	sum=sum+2; 
	} 
	return sum; 
}

/**
 * 阻止浏览器默认行为触发的通用方法  
 * @param {} e
 * @return {Boolean}
 */
function stopDefault(e){  
	//防止浏览器默认行为(W3C)  
	if(e && e.preventDefault){  
		e.preventDefault();  
	}  
	//IE中组织浏览器行为  
	else {  
		window.event.returnValue=false;  
	}  
	return false;  
} 

/**
 * 取消元素名称的默认事件
 * @param {String} tagName 元素名称 例：A
 * @param {String} e 事件 例：click
 */
function stopDefaultByTagName(tagName, e) {
	if (tagName != null && tagName != "") {
		tagName = tagName.toUpperCase();
	}
	var objs = document.getElementsByTagName(tagName);
	stopObjsDefaultEvent(objs, e);
}

/**
 * 取消对象的默认事件
 * @param {} objs 对象
 * @param {} e 事件 例：click
 */
function stopObjsDefaultEvent(objs, e) {
	if (objs == null) {
		return;
	}
	if (e != null && e != "") {
		e = e.replace("on", "");
	}
	if(window.attachEvent){ 
		//ie
		for (var i = 0; i < objs.length; i++) {
			objs[i].attachEvent("on" + e, stopDefault);
		}
	} else if(window.addEventListener){
		//ff
		for (var i = 0; i < objs.length; i++) {
			objs[i].addEventListener(e, stopDefault, false);
		}
	}
}


