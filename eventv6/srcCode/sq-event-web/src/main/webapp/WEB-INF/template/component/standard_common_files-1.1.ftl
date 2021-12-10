<#include "/component/commonVariable.ftl" />
<#if Session.defaultPageAction?exists>
	<#assign defaultPageAction = Session.defaultPageAction>
</#if>
<#assign rootPath = rc.getContextPath() >
<#if Session.uiDomain?exists>
	<#assign uiDomain = Session.uiDomain>
</#if>

<#global ffcs=JspTaglibs["/WEB-INF/tld/RightTag.tld"] >
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/icon.css">

<#if UI_STYLE?? && UI_STYLE=='/oldstyle'>
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/themes${UI_STYLE!''}/easyui.css">
<#else>
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/jquery-easyui-1.4/themes/blue/easyui.css">
</#if>
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/jquery.mCustomScrollbar.css" />
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/themes${UI_STYLE!''}/css/normal.css">
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/themes${UI_STYLE!''}/css/easyuiExtend.css">
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/themes${UI_STYLE!''}/plat-extend.css">


<script type="text/javascript" src="${uiDomain!''}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-7.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery.easyui.patch.js"></script><!--用于修复easyui-1.4中easyui-numberbox失去焦点后不能输入小数点的问题-->
<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery.form.js" ></script>
<script type="text/javascript" src="${uiDomain!''}/js/zzgl_core.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/function.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/layer/layer.js"></script>
<script type="text/javascript" src="${SQ_EVENT_URL}/js/global.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/themes${UI_STYLE!''}/js/plat-extend.js"></script>

<script type="text/javascript">
	var rootPath = "${rootPath!''}";
	var uiPath = "${uiDomain!''}";
	var _jSessionId = "${JSESSIONID!''}";
	//页面初始化
	$(document).ready(function(){
		//默认执行函数
		var defaultAction = "${defaultPageAction!''}";
		if(window[defaultAction]) window[defaultAction]();	
		
		//初始化
		
	});
	
	 (function($){

        /**
         * 表单序列化成json对象
         *
         */
        $.fn.serializeJson = function(){  
            var serializeObj = {};
            var array=this.serializeArray();
            var str=this.serialize();
            $(array).each(function(){
                if(serializeObj[this.name]){
                    if($.isArray(serializeObj[this.name])){
                        serializeObj[this.name].push(this.value);  
                    }else{  
                        serializeObj[this.name]=[serializeObj[this.name],this.value];  
                    }
                }else{
                    serializeObj[this.name]=this.value;   
                }  
            });
            return serializeObj;  
        };
    })(jQuery);
	
	
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
	

	
</script>




