<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" > 
<title>图片查看</title>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<#include "/component/ImageView.ftl" />
 <script > 
 var fileUrl = "${COMPONENTS_URL!''}";
 </script>
<#if drag?? && drag == 'true'>
<script type="text/javascript">
	ImageViewApi._loadScript("fbsource/jquery.event.drag-2.2.js");
	ImageViewApi._loadScript("fbsource/jquery.event.drag.live-2.2.js");
	var USE_IMAGE = 'true';
</script>

<style type="text/css">
    .fancybox-wrap{    cursor: move;}
</style>  
</#if>
	<#if background?? && background == 'none'>
<style type="text/css">
    .fancybox-overlay{background:none;}
</style>  
    </#if>

</head>
<body>
    <input type="hidden" id="index" value="${index!''}"/> 
    <input type="hidden" id="fieldId" value="${fieldId!''}"/> 
    <input type="hidden" id="paths" value="${paths!''}"/> 
    <input type="hidden" id="titles" value="${titles!''}"/> 
</body>
<script type="text/javascript">
$(function(){
	var index = $("#index").val();
	var fieldId  =  $("#fieldId").val();
	var paths = new Array();
	var pathStr =  $("#paths").val();
	paths = pathStr.split(",");

	var titles = new Array();
	var titleStr =  $("#titles").val();
	titles = titleStr.split(",");
	
	for( var i=0, len=paths.length; i < len; i++) {
		if(paths[i].indexOf('?') < 0) {
			paths[i]+='?t='+Math.random();
		}
	}
	
	if(paths.length > titles.length) {
		for(var _index = 0, len = paths.length - titles.length; _index < len; _index++) {
			titles.push("");
		}
	}
	
	ImageViewApi.initImageView(fieldId,paths,false,false,titles);
	ffcs_viewImg(fieldId,index);
	<#if overflow?? && overflow == 'none'>
    	$('.fancybox-overlay').css('overflow','hidden');
    </#if>
});
</script>
</html>