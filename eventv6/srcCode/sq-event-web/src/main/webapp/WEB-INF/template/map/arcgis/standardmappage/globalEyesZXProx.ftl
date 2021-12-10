<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<HTML>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>全球眼监控</title>
	<script language="JavaScript" src="${rc.getContextPath()}/globalEyes/js/prototype.js"></script>
	<script language="JavaScript" src="${rc.getContextPath()}/zte_globalEyes/js/png.js"></script>
	<script language="JavaScript" src="${rc.getContextPath()}/zte_globalEyes/js/sp_monitor.js"></script>
</head>
<body style="font-size:12px; margin:0 auto 0 auto; padding:0px; color:#333333;" >
<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td align="center" valign="top">
				<div id="bootom">
					<div style="padding-left:10px; line-height:25px; text-align:left;"><span id="msg" style="float:left;padding-left:15px"></span>若无法观看视频，请点击下载：<a href="${RESOURCE_DOMAIN}/public_download/MOCPlayer.zip" title="点击下载" style="text-decoration:none;">全球眼视频播放控件_V1</a></div>
					<div class="sp" style="text-align:left;">
						<div id="voter" style=" height:351px; width:462px; background:#000000; text-align:center;">
							<object id="player" CLASSID="clsid:AB70953C-CD32-4CE0-BEA8-07365E319C2B" height="350" width="460">
								<!-- param name="wmode" value="opaque" / -->
								<param name="wmode" value="transparent" />
								<embed width="502px" height="325" wmode="opaque" align="bottom"></embed>
							</object>
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
	window.onload = function() {
		<#if monitor??>
			look('${monitor.channelId!''}', '${monitor.channelIp!''}', '${monitor.platformUsername!''}', '${monitor.platformPassword!''}', '0')
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
function fullScree(){
	var player = document.getElementById("player");
	var reNum= player.SetFullScreen(1);
}  
var num=1;
var results=new Array(num);
function look(channelId,ip,username,pass,index){
		channelId = trim(channelId);
		ip = trim(ip);



	var player = document.getElementById("player");
	if(results[index]==200||results[index]==500){
		 player.OpenGUStreamEx2(channelId, 1, 0);
	}else{
		//显示初始化提示
	msg.innerHTML='<img src="${rc.getContextPath()}/images/load.gif" align="left" style="margin:3px 5px 0 0;"/><div id="waitMsg">正在登录...</div>'
				
		results[index]=player.LoginEx2(ip, '10000', username, pass,0);
		//成功则播放
		if(results[index]==200||results[index]==500){
			player.OpenGUStreamEx2(channelId, 1, 0);
			msg.innerHTML='';
			
		}else{
		//失败则提示
			alert("初始化失败：网络异常或认证失败");
			msg.innerHTML='<font color="red">初始化失败</font>';
		}

	}
}
</script>