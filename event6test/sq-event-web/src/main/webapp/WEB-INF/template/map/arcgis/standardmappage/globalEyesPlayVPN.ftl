<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<HTML>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>全球眼监控</title>
	<script language="JavaScript" charset="utf-8" src="${rc.getContextPath()}/globalEyes/VPN/js/jmegaeyes-1.0.js"></script>
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<script language="JavaScript" src="${rc.getContextPath()}/globalEyes/VPN/js/jquery.json-2.4.min.js"></script>
<script>
var theApp = {};

$(function(){
	//创建Session对像
	theApp.Session = new JMegaeyes.Session();
	theApp.Session.createPlugin("sessionPlugin");
	//创建RealPlayer对像
	theApp.Player = new JMegaeyes.RealPlayer();
	theApp.Player.createPlugin("playBox", 352, 288);
});

	//登录
	function login(){
		if(theApp.Session.isLogin()){
			logMessage("已经登录，请调用注销功能后重新登录！");
			return;
		}
		var ipPort = $("#ip").val();
		if(ipPort){
			var ipAddr = ipPort.split(":")[0];
			var port = ipPort.split(":")[1];
			var user = $("#user").val();
			var password = $("#password").val();
			if( theApp.Session.loginToPlatform(ipAddr, port, user, password) )
			{
				logMessage("登录成功！会话标识：" + theApp.Session.getSessionID() +"。");
			}
			else
			{
				logMessage("登录失败！");
			}
		}else{
			logMessage("平台接入IP不能为空！");
		}
	};
		//播放
	function realPlay(){
		var cameraNaming = $("#camera_Naming").val();
		var cameraName = $("#camera_Name").val();
		if( !theApp.Session.isLogin() || cameraNaming == "" ){
			logMessage("登录并获取设备列表后才能运行示例！");
			login();
		}
		//平台相关参数，登录时获取
		var setting = theApp.Session.getSysSetting();
		theApp.Player.setSysSetting(setting);
		var ret = theApp.Player.play(cameraNaming, cameraName);
		if(ret){
			logMessage("播放实时视频成功！");
		}
		else{
			logMessage("播放实时视频失败！");
		}
	};
	//停止
	function stopPlay(){
		theApp.Player.stop();
		logMessage("停止播放实时视频！");
	};
$(window).unload(function() {
	theApp.Session.removePlugin();
});

function logMessage(msg){
	$("#log").append("<p>"+msg+"</p>");
}
</script>
</head>
<body style="font-size:12px; margin:0 auto 0 auto; padding:0px; color:#333333;" >
<div id="sessionPlugin" style="height:0px;"></div>
<div style="display:none">
<form>
<label for="ip">平台接入IP&nbsp;</label><input type="text" id="ip" value="${monitor.platformIp!''}"></input><br />
<label for="user">用户名&nbsp;</label><input type="text" id="user" value="${monitor.platformUsername!''}" ></input><br />
<label for="password">密码&nbsp;</label><input type="text" id="password" value="${monitor.platformPassword!''}" ></input><br />
<label for="camera_Name">通道名称&nbsp;</label><input type="text" id="camera_Name" value="${monitor.platformName!''}" ><br />
<label for="camera_Naming">通道ID&nbsp;</label><input type="text" id="camera_Naming" value="${monitor.channelId!''}" style="width:300px"></input><br />
</form>
</div>
<div>
<!--<img id="img1" src="${rc.getContextPath()}/globalEyes/VPN/images/player_02.jpg" onclick="realPlay()"/>
<img id="img2" src="${rc.getContextPath()}/globalEyes/VPN/images/player_06.jpg" onclick="fullScreen()"/>-->

<!--<input type="button" value="播放" id="btnPlay" onclick="realPlay()"/>
<input type="button" value="停止" id="btnStop" onclick="stopPlay()"/>
<input type="button" value="全屏" id="btnFullScreen" onclick="fullScreen()"/>
<input type="button" value="退出全屏" id="btnExtScreen" onclick="extScreen()"/>-->
<b id="impStat"><font color="red"><#if message??>${message}</#if></font></b>
若无法观看视频，请点击下载全球眼播放控件：<a href="${rc.getContextPath()}/zzgl/res/globalEyes/downloadQQY.jhtml" title="政务网全球眼播放控件">政务网全球眼播放控件</a>
<div id="playBox" style="width:462;height:348"></div>
</div>
<!--
<h1>运行日志</h1><hr />
<div id="log"></div>
-->
</BODY>
</HTML>
<script language="javascript">
$(function(){
	$("#playBox_ibox").css({"position": "absolute", "text-align": "center", "top": "15px", "left": "0px", "right": "0px", "bottom": "0px"});	
	$("#playBox_ibox").css({height: function () {
                        return $(document).height()-15;
                    },
                    width:"462"});
	realPlay();
});
//全屏
function fullScreen(){
   $("#img2").attr("src","${rc.getContextPath()}/globalEyes/VPN/images/player_08.jpg"); 
   $("#img2").attr("onclick","extScreen()"); 
   var width = screen.width;
   var height = screen.height;
	 $("#playBox_ibox").css({"position": "absolute", "text-align": "center", "top": "35px", "left": "0px", "right": "0px", "bottom": "0px"});	
	 $("#playBox_ibox").css({height: function () {
                        return $(document).height()-35;
                    },
                    width:"100%"});
}
//退出全屏
function extScreen(){
   $("#img2").attr("src","${rc.getContextPath()}/globalEyes/VPN/images/player_06.jpg"); 
   $("#img2").attr("onclick","fullScreen()");
	 $("#playBox_ibox").css({height: function () {
                        return 298;
                    },
                    width:"362"});
}
</script>