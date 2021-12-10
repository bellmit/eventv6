<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事件采集</title>
<#include "/component/commonFiles-1.1.ftl" />
</head>
<body>
	<div id="jqueryToolbar" style="display:none"></div>
	<div style="float:left;cursor: pointer;" onclick="addGivenEvent('02')">
		<img src="${uiDomain}/web-assets/_big-screen/nanAn/images/proruption_event.png"/>
	</div>
	<div style="float:left;cursor: pointer;" onclick="addGivenEvent('03')">
		<img src="${uiDomain}/web-assets/_big-screen/nanAn/images/sensitive_event.png"/>
	</div>
</body>

<script type="text/javascript">

	function addGivenEvent(urgencyDegree){
		parent.addGivenEvent(urgencyDegree);
	}


</script>
</html>