<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<HTML>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>全球眼监控</title>
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<script language="JavaScript" src="${rc.getContextPath()}/globalEyes/js/prototype.js"></script>
</head>
<body style="font-size:12px; margin:0 auto 0 auto; padding:0px; color:#333333;" >
<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td align="center" valign="top">
				<div id="bootom">
					<div style="padding-left:10px; line-height:25px; text-align:left;">若无法观看视频，请点击下载：<a href="${rc.getContextPath()}/globalEyes/installfiles/ViSSOcxSetup.zip" title="点击下载" style="text-decoration:none;">全球眼视频播放控件_V1</a>
					<div class="sp" style="text-align:left;">
						<div id="voter" style=" height:351px; width:462px; background:#000000; text-align:center;">
							<OBJECT id="BabyOnline" name="BabyOnline" 
								classid="clsid:FB199005-28D7-4E5B-B68D-983FC8743D2F" STYLE="width: 460px; height: 350px" 
								<#--codeBase="BabyOnline.cab#version=1,0,0,0"
								align=middle class="style1" VIEWASTEXT -->
								><!--  -->
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
	var MWP = null;
	var gblEqpId = 0;
	var lResult = 1;
	lResult = BabyOnline.CreateInstance();
	if(lResult != 0){
		alert("初始化失败");
	}
	window.onload = function() {
		<#if monitor??>
			showVideo('${monitor.platformIp!''}', '${monitor.platformUsername!''}', '${monitor.platformPassword!''}', '${monitor.channelIp!''}', '${monitor.channelId!''}')
		</#if>
		<#if type??>
			window.parent.$(".panel-tool .panel-tool-close").click(function(){
				logout();
			});
		<#else>
			// 设置事件
			window.parent.$(".l-dialog-tc .l-dialog-close", window.parent.wingrid.dialog).click(function (){
				logout();
				window.parent.wingrid.close();
			});
		</#if>
	}
	
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
var gblIsLogin = false;
function showVideo( platformIp, platformUsername, platformPassword, channelIp, channelId ) {
	platformIp = trim(platformIp);
		channelIp = trim(channelIp);
		channelId = trim(channelId);
	if (!gblIsLogin)
	{
		try{
		var msg    = BabyOnline.Login(platformIp,'18081','viss',platformUsername,platformPassword);	
		if (msg != 0)
		{
			alert("登录视频出错，请验证用户名，密码，及服务器IP地址等信息！");
			return false;
		}
		}catch(e)
		{
			alert("初始化失败！");
		}
	}	
	playVideo(channelId);
	
} 

function playVideo(videoCode){
	try{
		var msg = BabyOnline.StartLiveVideo(videoCode,0);	
		if(msg != 0){
			alert("登录视频失败！");
			return false;
		}
		//BabyOnline.GetPresetPtz(videoCode);
	}catch(e){
		alert("初始化失败！");
	}
}
 
  

//释放ocx控件
function destroyOcx(){
	BabyOnline.Destroy();
}  
//退出
function logout(){ 
	var lResult = BabyOnline.Logout();
	//if(lResult != 0){
	//	resultMSG = "resultCode:" + lResult + " , Logout is failure";
	//}else{
	//	resultMSG = "resultCode:" + lResult + " , Logout is successful";
	//}
}
</script>