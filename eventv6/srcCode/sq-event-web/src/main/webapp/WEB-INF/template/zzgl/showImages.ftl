<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>图片查看</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ImageView.ftl" />
</head>
<body>
                
</body>
<script type="text/javascript">
$(function(){
	var fieldId  = "${fieldId?c}";
	var paths = "${paths!''}";
	ImageViewApi.initImageView(fieldId,paths,false,false);
	ffcs_viewImg(fieldId,0);
});
</script>
</html>