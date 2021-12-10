<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" > 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>事件信息</title> 
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
</head>
<body>
		<div id="ts" class="easyui-tabs" fit="true" border="false" style="margin: 0; height:450px;">
			<div title="事件信息详情">
				<iframe id="eventDetail" scrolling='no' frameborder='0' src='${rc.getContextPath()}/zzgl/event/requiredEvent/eventDetail.jhtml?eventId=${eventId?c}' style='width:99.9%;height:100%;'></iframe>
			</div>
			<div title="事件办理流程">
				<iframe  id="eventFlow" scrolling='no' frameborder='0' src='${rc.getContextPath()}/zzgl/event/requiredEvent/eventTaskDetail.jhtml?eventId=${eventId?c}' style='width:99.9%;height:100%;'></iframe>
			</div>
	    </div>
	
<#include "/component/BuildingRoomSelector.ftl" />
<#include "/component/GridSelectorZtree.ftl" />
<#include "/component/BuildingSelector.ftl" />
<#include "/component/SelectorAddress.ftl" />

	<script type="text/javascript">

	</script>
</body>
</html>
