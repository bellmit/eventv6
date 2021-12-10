<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" > 
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
	<title><#if videoType?? && videoType=='2'>音频<#else>视频</#if>查看</title>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jPlayer-2.9.2/dist/skin/blue.monday/css/jplayer.blue.monday.min.css" />
	
	<script src="${rc.getContextPath()}/js/jPlayer-2.9.2/lib/jquery.min.js" type="text/javascript"></script>
	<script src="${rc.getContextPath()}/js/jPlayer-2.9.2/dist/jplayer/jquery.jplayer.min.js" type="text/javascript"></script>
	<script src="${rc.getContextPath()}/js/jPlayer-2.9.2/src/javascript/jplayer/jquery.jplayer.js" type="text/javascript"></script>
</head>

<#if isBigScreen?? && isBigScreen == '1'>
<style>
	body{
		overflow:hidden;
	}
	
	#jp_container_1{
		margin-left:2rem;
	}
</style>
</#if>

<body>
	<div id="jp_container_1" class="jp-video jp-video-360p" role="application" aria-label="media player">
		<div class="jp-type-single">
			<div id="jquery_jplayer_1" class="jp-jplayer"></div>
			<div class="jp-gui">
				<div class="jp-video-play">
					<button class="jp-video-play-icon" role="button" tabindex="0" title="播放">play</button>
				</div>
				<div class="jp-interface">
					<div class="jp-progress">
						<div class="jp-seek-bar">
							<div class="jp-play-bar"></div>
						</div>
					</div>
					<div class="jp-current-time" role="timer" aria-label="time">&nbsp;</div>
					<div class="jp-duration" role="timer" aria-label="duration">&nbsp;</div>
					<div class="jp-controls-holder">
						<div class="jp-controls">
							<button class="jp-play" role="button" tabindex="0">play</button>
							<button class="jp-stop" role="button" tabindex="0" title="停止">stop</button>
						</div>
						<div class="jp-volume-controls">
							<button class="jp-mute" role="button" tabindex="0" title="静音">mute</button>
							<button class="jp-volume-max" role="button" tabindex="0">max volume</button>
							<div class="jp-volume-bar">
								<div class="jp-volume-bar-value" title="音量"></div>
							</div>
						</div>
						<div class="jp-toggles">
							<button class="jp-repeat" role="button" tabindex="0" title="循环播放">repeat</button>
							<button id="fullScreenBtn" class="jp-full-screen" role="button" tabindex="0" title="全屏">full screen</button>
						</div>
					</div>
					<div class="jp-details">
						<div class="jp-title" aria-label="title">如果<#if videoType?? && videoType=='2'>音频<#else>视频</#if>不能正常播放，请点击<a href="#" onclick="_skipTo()">重试</a></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<form id="videoForm" name="videoForm" action="${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml" method="post">
		<input type="hidden" name="path" value="${path!}" />
		<input type="hidden" name="videoType" value="${videoType!}" />
		<input type="hidden" name="isRetry" value="true" />
		<input type="hidden" name="attachmentId" value="<#if attachmentId??>${attachmentId?c}</#if>" />
	</form>
</body>	

<script>
	$(function(){
		var winHeight =  $(window).height()>360 ? 360 : $(window).height();
		var winWidth = $(window).width()>640 ? 640 : $(window).width();
		
		if (/Chrome/.test(navigator.userAgent)){//chrome 浏览器
			$("#fullScreenBtn").remove();
		}
		
		$("#jquery_jplayer_1").bind('contextmenu', function() { return false; });
		
		$("#jquery_jplayer_1").jPlayer( {
			ready: function () {
	            $(this).jPlayer("setMedia", {
	                m4v: "${path!}",
	                m4a: "${path!}",
	                poster:"${rc.getContextPath()}/js/jPlayer-2.9.2/src/skin/poster_${videoType!'1'}.jpg"
	            }).jPlayer("play");
	        },
	        swfPath:"${rc.getContextPath()}/js/jPlayer-2.9.2/dist/jplayer",
	        supplied: "m4v, m4a",
	        size: {
	            width: winWidth,
	            height: winHeight,
	            cssClass: "jp-video-360p"
	        },
	        useStateClassSkin: true,
			autoBlur: false,
			smoothPlayBar: true,
			remainingDuration: true,
			toggleDuration: true
		});
   
	});
	
	function _skipTo() {
		$("#videoForm").submit();
	}
	
</script>

</html>