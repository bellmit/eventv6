<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" > 
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
	<title>音频查看</title>
</head>

<body>
	<object width="200" height="20" data="${path!}" classid="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B" codebase="http://www.apple.com/qtactivex/qtplugin.cab">
		<param name="src" value="${path!}" />
		<param name="controller" value="true" />
		<param name="type" value="video/quicktime" />
		<param name="autoplay" value="true" />
		<param name="target" value="myself" />
		<param name="bgcolor" value="black" />
		<param name="pluginspage" value="http://www.apple.com/quicktime/download/index.html" />
		
		<param name="href" value="${path!}" />
		<param name="autohref" value="true" />
		
		<embed src="${path!}" href="${path!}" autohref="true" width="200" height="20" controller="true" align="middle" bgcolor="black" target="myself" type="video/quicktime" pluginspage="http://www.apple.com/quicktime/download/index.html" />
	</object>
	
	<div id="mainDiv">
		<b><font color="red">如果当前浏览器不支持该格式文件播放，请点击</font><a href="${rc.getContextPath()}/upFileServlet?method=down&attachmentId=<#if attachmentId??>${attachmentId}<#else>0</#if>">下载</a></b>
	</div>
</body>	

</html>
