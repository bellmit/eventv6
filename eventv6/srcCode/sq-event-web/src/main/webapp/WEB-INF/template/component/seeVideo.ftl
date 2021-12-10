<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" > 
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
	<title><#if videoType?? && videoType=='2'>音频<#else>视频</#if>查看</title>
	
	<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/jquery-1.7.js"></script>
</head>

<body>
	<embed class="pic" src="${path!}" width="560px" height="340px" id="embedPlayer" style="display:none" />
	
	<video src="${path!}" poster="${rc.getContextPath()}/js/jPlayer-2.9.2/src/skin/poster_${videoType!'1'}.jpg" width="560px" height="340px" controls="controls" id="videoPlayer" style="display:none" ></video>
	
	<div id="mainDiv">
		<b><font color="red">如果当前浏览器不支持该格式文件播放，请点击</font><a href="${rc.getContextPath()}/upFileServlet?method=down&attachmentId=<#if attachmentId??>${attachmentId}<#else>0</#if>">下载</a></b>
	</div>
</body>	

<script>
	$(function(){
		if (navigator.appName.indexOf('Microsoft') != -1){//IE浏览器
			playVideo("embedPlayer");
		}else if (navigator.appName.indexOf('Netscape') != -1){//chrome 浏览器
			playVideo("videoPlayer");
		}else{
			playVideo("videoPlayer");
		}
	});
	
	function playVideo(playerId) {
		var winHeight = $(window).height()>360 ? 360 : $(window).height(),
			winWidth = $(window).width()>640 ? 640 : $(window).width();
		
		$("#" + playerId).bind('contextmenu', function() { return false; }); 
		
		$("#" + playerId).attr("height", winHeight)
						 .attr("width", winWidth)
						 .attr("autoplay", true)
						 .show();
	}
</script>
</html>
