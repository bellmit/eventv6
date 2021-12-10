<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>jstree basic demos</title>
	<link rel="stylesheet" href="${rc.getContextPath()}/js/zTree_v3/css/demo.css" />
	<link rel="stylesheet" href="${rc.getContextPath()}/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" />
	<script src="${rc.getContextPath()}/js/zTree_v3/js/jquery-1.4.4.min.js"></script> 
	<script src="${rc.getContextPath()}/js/zTree_v3/js/jquery.ztree.core-3.5.js"></script> 
	<script src="${rc.getContextPath()}/js/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script> 
	<script src="${rc.getContextPath()}/js/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script> 
	<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/menu_conf.js"></script> 
	<SCRIPT type="text/javascript">
		<!--
		var path = "${rc.getContextPath()}";
		//-->
	</SCRIPT>
	<style type="text/css">
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
	</style>
</HEAD>

<BODY>
<div class="content_wrap">
	<div class="zTreeDemoBackground left">
		<ul id="treeDemo" class="ztree"></ul>
	</div>
	<div class="right">
		<ul id="treeDemo2" class="ztree"></ul>
	</div>
</div>
</BODY>
</HTML>