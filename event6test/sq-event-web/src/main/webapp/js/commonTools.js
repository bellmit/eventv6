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

/* 
 * js 去除所有的标签函数
*/
String.prototype.delHtmlTag=function(){
	return this.replace(/<[^>]+>/g,"");
};
/* 
 * js 去除所有的空格,包括换行与\t
 */
String.prototype.trimAll=function(){
	return this.replace(/\ +/g, "").replace(/[ ]/g, "").replace(/[\r\n|\n|\t]/g, "");
};
/* 
 * 半角转换为全角函数,全角空格为12288，半角空格为32 ,其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248 
*/
String.prototype.ToDBC = function() {
	var tmp = "";
	for ( var i = 0; i < this.length; i++) {
		if (this.charCodeAt(i) == 32) {
			tmp = tmp + String.fromCharCode(12288);
		}
		if (this.charCodeAt(i) < 127) {
			tmp = tmp + String.fromCharCode(this.charCodeAt(i) + 65248);
		}
	}
	return temp;
};
/* 
 * 全角转半角
*/
String.prototype.ToCDB = function() {
	var tmp = "";
	for ( var i = 0; i < str.length; i++) {
		if (str.charCodeAt(i) > 65248 && str.charCodeAt(i) < 65375) {
			tmp += String.fromCharCode(str.charCodeAt(i) - 65248);
		} else {
			tmp += String.fromCharCode(str.charCodeAt(i));
		}
	}
	return temp;
};

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
/*js日期加减日期*/
Date.prototype.addDay = function(val){ 
	var date = this;
	date.setDate(date.getDate() + val);
    return date;
};
Date.prototype.addMonth = function(val){ 
	var date = this;
	date.setMonth(date.getMonth() + val);
	return date; 
};
Date.prototype.addHour = function(val){ 
	var date = this;
	date.setHours(date.getHours() + val);
	return date; 
};
/*数组相关的方法*/
Array.prototype.indexOf = function(val) {
	for ( var i = 0; i < this.length; i++) {
		if (this[i] == val)
			return i;
	}
	return -1;
};
/**
 * 去除数组的重复元素
 * */
Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};
/**
 * 根据索引去除数组的重复元素(此时数组长度不变,其值为undefined)
 * */
Array.prototype.removeByIndex = function(index) {
	//this.splice(index, 1);
	delete this[index];
};
/**
 * 数组的contains方法
 * */
Array.prototype.contains = function(ss) {
	   return this.indexOf(ss) >= 0;
	 },
	 
/**
* each是一个集合迭代函数，它接受一个函数作为参数和一组可选的参数
* 这个迭代函数依次将集合的每一个元素和可选参数用函数进行计算，并将计算得的结果集返回
{%example
<script>
     var a = [1,2,3,4].each(function(x){return x > 2 ? x : null});
     var b = [1,2,3,4].each(function(x){return x < 0 ? x : null});
     alert(a);
     alert(b);
</script>
%}
* @param {Function} fn 进行迭代判定的函数
* @param more ... 零个或多个可选的用户自定义参数
* @returns {Array} 结果集，如果没有结果，返回空集
*/
Array.prototype.each = function(fn){
    fn = fn || Function.K;
     var a = [];
     var args = Array.prototype.slice.call(arguments, 1);
     for(var i = 0; i < this.length; i++){
         var res = fn.apply(this,[this[i],i].concat(args));
         if(res != null) a.push(res);
     }
     return a;
};

/**
* 得到一个数组不重复的元素集合<br/>
* 唯一化一个数组
* @returns {Array} 由不重复元素构成的数组
*/
Array.prototype.uniquelize = function(){
     var ra = new Array();
     for(var i = 0; i < this.length; i ++){
         if(!ra.contains(this[i])){
            ra.push(this[i]);
         }
     }
     return ra;
};

/**
* 求两个集合的补集
{%example
<script>
     var a = [1,2,3,4];
     var b = [3,4,5,6];
     alert(Array.complement(a,b));
</script>
%}
* @param {Array} a 集合A
* @param {Array} b 集合B
* @returns {Array} 两个集合的补集
*/
Array.complement = function(a, b){
     return Array.minus(Array.union(a, b),Array.intersect(a, b));
};

/**
* 求两个集合的交集
{%example
<script>
     var a = [1,2,3,4];
     var b = [3,4,5,6];
     alert(Array.intersect(a,b));
</script>
%}
* @param {Array} a 集合A
* @param {Array} b 集合B
* @returns {Array} 两个集合的交集
*/
Array.intersect = function(a, b){
     return a.uniquelize().each(function(o){return b.contains(o) ? o : null;});
};

/**
* 求两个集合的差集
{%example
<script>
     var a = [1,2,3,4];
     var b = [3,4,5,6];
     alert(Array.minus(a,b));
</script>
%}
* @param {Array} a 集合A
* @param {Array} b 集合B
* @returns {Array} 两个集合的差集
*/
Array.minus = function(a, b){
     return a.uniquelize().each(function(o){return b.contains(o) ? null : o;});
};

/**
* 求两个集合的并集
{%example
<script>
     var a = [1,2,3,4];
     var b = [3,4,5,6];
     alert(Array.union(a,b));
</script>
%}
* @param {Array} a 集合A
* @param {Array} b 集合B
* @returns {Array} 两个集合的并集
*/
Array.union = function(a, b){
     return a.concat(b).uniquelize();
};

/**
* 获取Cookie的value值
* @author zkongbai
* @param {String} name Cookie Name
* @returns
*/
window.Get_Cookie = function(name) {  
   var start = document.cookie.indexOf(name+"="); 
   var len = start+name.length+1; 
   if ((!start) && (name != document.cookie.substring(0,name.length))) return null; 
   if (start == -1) return null; 
   var end = document.cookie.indexOf(";",len); 
   if (end == -1) end = document.cookie.length; 
   return decodeURI(document.cookie.substring(len,end));   
};
/**
* 设置Cookie
* @author zkongbai
* @param {String} name: Cookie Name
* @param {String} value: Cookie value
* @param {String} expires: expires time (单位:天)
* @returns {String} Cookie Value
*/
window.Set_Cookie = function(name,value,expires,path,domain,secure) {
    expires = expires * 60*60*24*1000; 
    var today = new Date(); 
    var expires_date = new Date( today.getTime() + (expires) ); 
    var cookieString = name + "=" +escape(value) + 
       ( (expires) ? ";expires=" + expires_date.toGMTString() : "") + 
       ( (path) ? ";path=" + path : "") + 
       ( (domain) ? ";domain=" + domain : "") + 
       ( (secure) ? ";secure" : ""); 
    document.cookie = cookieString; 
};

var fileSuffixs={
		acc : "acc",
		ai : "ai",
		aif : "aif",
		app : "app",
		atom : "atom",
		avi : "avi",
		bmp : "bmp",
		cdr : "cdr",
		css : "css",
		doc : "doc",
		docx : "docx",
		eps : "eps",
		exe : "exe",
		file : "file",
		fla : "fla",
		flv : "flv",
		gzip : "gzip",
		html : "html",
		ico : "ico",
		indd : "indd",
		jpeg : "jpeg",
		jpg : "jpg",
		js : "js",
		keynote : "keynote",
		log : "log",
		mov : "mov",
		mp3 : "mp3",
		mp4 : "mp4",
		numbers : "numbers",
		otf : "otf",
		pages : "pages",
		pdf : "pdf",
		php : "php",
		png : "png",
		ppt : "ppt",
		pptx : "pptx",
		psd : "psd",
		rar : "rar",
		raw : "raw",
		rss : "rss",
		rtf : "rtf",
		sql : "sql",
		svg : "svg",
		swf : "swf",
		tar : "tar",
		tiff : "tiff",
		ttf : "ttf",
		txt : "txt",
		wav : "wav",
		wmv : "wmv",
		xls : "xls",
		xlsx : "xlsx",
		xml : "xml",
		zip : "zip"
}; 
var canPreview = {
		"doc" : "/plugins/unitegallery/images/suffix/doc.png",
		"docx" : "/plugins/unitegallery/images/suffix/docx.png",
		"xls" : "/plugins/unitegallery/images/suffix/xls.png",
		"xlsx" : "/plugins/unitegallery/images/suffix/xls.png",
		"pdf" : "/plugins/unitegallery/images/suffix/pdf.png",
		"txt" : "/plugins/unitegallery/images/suffix/txt.png",
		"jpg" : "/plugins/unitegallery/images/suffix/jpg.png",
		"jpeg" : "/plugins/unitegallery/images/suffix/jpeg.png",
		"bmp" : "/plugins/unitegallery/images/suffix/bmp.png",
		"gif" : "/plugins/unitegallery/images/suffix/png.png",
		"png" : "/plugins/unitegallery/images/suffix/png.png"
};
var dictCodes = {
		/**
		 * 任务状态进行中
		 */
		"F-OA001-001" : "进行中",
		
		/**
		 * 任务状态待接收
		 */
		"F-OA001-002" : "待接收",
		
		/**
		 * 任务状态待审核
		 */
		"F-OA001-003" : "待审核",
		
		/**
		 * 任务状态已完成
		 */
		"F-OA001-004" : "已完成",
		
		/**
		 * 任务状态已拒绝
		 */
		"F-OA001-005" : "已完成",
		
		/**
		 * 文件文档分类(共享文档)
		 */
		"F-OA-SORT-TYPE-001" : "共享文档",
		
		/**
		 * 文件文档分类(办公文档)
		 */
		"F-OA-SORT-TYPE-002" : "办公文档",
		
		/**
		 * 文件文档分类(我的文档)
		 */
		"F-OA-SORT-TYPE-003" : "我的文档",
		
		/**
		 * 文件类型(文件夹)
		 */
		"F-OA-FILE-TYPE-001" : "文件夹",
		
		/**
		 * 文件类型(文件)
		 */
		"F-OA-FILE-TYPE-002" : "文件",
		
		/**
		 * 视频维稳状态
		 */
		"F-VIDEO-SECURITY-001" : "已提醒"
};