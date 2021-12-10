<#include "/component/commonVariable.ftl" />
<#if Session.defaultPageAction?exists>
	<#assign defaultPageAction = Session.defaultPageAction>
</#if>
<#assign rootPath = rc.getContextPath() >
<#global ffcs=JspTaglibs["/WEB-INF/tld/RightTag.tld"] >
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/jquery-1.7.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	var rootPath = "${rootPath!''}";
	var _jSessionId = "${JSESSIONID!''}";
	//页面初始化
	$(document).ready(function(){
		//默认执行函数
		var defaultAction = "${defaultPageAction!''}";
		if(window[defaultAction]) window[defaultAction]();	
	});
	
</script>

