<#include "/component/commonVariable.ftl" />
<#if Session.uiDomain?exists>
	<#assign uiDomain = Session.uiDomain>
</#if>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/icon.css">
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<link href="${uiDomain!''}/css/normal.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/css/easyuiExtend.css" rel="stylesheet" type="text/css" />

<link href="${uiDomain!''}/css/jquery.mCustomScrollbar.css" rel="stylesheet"  type="text/css">
<script src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/function.js"></script>
<script type="text/javascript">
	var _jSessionId = "${JSESSIONID!''}";
</script>

