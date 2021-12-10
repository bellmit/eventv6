<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<HTML>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>全球眼监控</title>
	<script language="JavaScript" src="${rc.getContextPath()}/globalEyes/js/prototype.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/globalEyes/js/Map.js"></script>
	<script language="JavaScript" src="${rc.getContextPath()}/globalEyes/js/date.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/globalEyes/js/mega_hxht.js"></script>
	<script language=javascript for=MegaWebPlayer event='OnGetFileList()'></script>
</head>
<body style="font-size:12px; margin:0 auto 0 auto; padding:0px; color:#333333;" >
<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td align="center" valign="top">
				<div id="bootom">
					<#if RESOURCE_DOMAIN??>
					<div style="padding-left:10px; line-height:25px; text-align:left;">若无法观看视频，请点击下载：<a href="${RESOURCE_DOMAIN}/public_download/WebPlayer3.0.2-1017.zip" title="点击下载" style="text-decoration:none;">全球眼视频播放控件_V1</a>　<a href="${RESOURCE_DOMAIN}/public_download/telwebplayer.zip" title="点击下载" style="text-decoration:none;">全球眼视频播放控件_V2</a></div>
					</#if>
					<div class="sp" style="text-align:left;">
						<div id="voter" style=" height:351px; width:462px; background:#000000; text-align:center;">
							<div id="mwp">
								<OBJECT ID="MegaWebPlayer"
									CLASSID="CLSID:9581374A-C188-4980-B6D5-2AE4318F694A"
									style="width: 460px; height: 350px">
									<param name="Screen" value="1">
									<param name="HistoryMode" value="0">
								</OBJECT>
							</div>
						</div>
					</div>
				</div>
			</td>
  		</tr>
	</table>
</div>
</BODY>
</HTML>

<script language="javascript">
	var MWP = null;
	var gblEqpId = 0;
	window.onload = function() {
		MWP = new EMStar($('MegaWebPlayer'));
		<#if monitor??>
			showVideo('${monitor.platformIp!''}', '${monitor.platformUsername!''}', '${monitor.platformPassword!''}', '${monitor.channelIp!''}', '${monitor.channelId!''}')
		</#if>
	}
</script>

<script language="javascript">
	function ltrim(str) {
        var pattern = new RegExp("^[\\s]+","gi");
        return str.replace(pattern,"");
	}
	function rtrim(str) {
        var pattern = new RegExp("[\\s]+$","gi");
        return str.replace(pattern,"");
	}
	function trim(str) {
        return rtrim(ltrim(str));
	}
	function login_init(platformIp, platformUsername, platformPassword ) {
		MWP.login(platformIp, 80, '', platformUsername, platformPassword );
	}
	
	function showVideo(platformIp, platformUsername, platformPassword, channelIp, channelId ) {
		platformIp = trim(platformIp);
		channelIp = trim(channelIp);
		channelId = trim(channelId);
		login_init( platformIp, platformUsername, platformPassword );
		MWP.stopVideo();
		var str = "<SourceAddr><IP>"+channelIp+"</IP>";
		str += "<Port>6001</Port>";
		str += "<CameraID>"+channelId+"</CameraID>";
		str += "<SessionID>33333</SessionID></SourceAddr>";
		MegaWebPlayer.SourceAddr = str;
	}
</script>