<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    
	<title>单条楼宇轮廓编辑跳转页面</title>
</head>
<body >
	<iframe data-iframe="true" name="buildingHSDrawFrame" id="buildingHSDrawFrame" style="margin-left:-8px;margin-top:-8px" frameborder="0" allowtransparency="true"></iframe>
</body>
<script type="text/javascript">
	$(function(){
		var targetURL = '<#if targetURL??>${targetURL!''}</#if>';
		var URL = '${rc.getContextPath()}'+targetURL;
		$("#buildingHSDrawFrame").css("width",$(window).width());
		$("#buildingHSDrawFrame").css("height",$(window).height());
		$("#buildingHSDrawFrame").attr("src",URL);
	});
</script>
</html>