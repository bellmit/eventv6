<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<HTML>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>全球眼监控-海康威视</title>
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
</head>
<body style="font-size:12px; margin:0 auto 0 auto; padding:0px; color:#333333;" >
<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td align="center" valign="top">
				<div id="bootom">
					<div style="padding-left:10px; line-height:25px; text-align:left;"><span id="msg" style="float:left;padding-left:15px"></span>若无法观看视频，请点击下载：<a href="${RESOURCE_DOMAIN}/public_download/ViSSOcxSetup.zip" title="点击下载" style="text-decoration:none;">全球眼视频播放控件_V1</a>
					<div class="sp" style="text-align:left;">
						<div id="objectid" style=" height:410px; width:462px; background:#000000; text-align:center;">
							<object id='PlayViewOCX' width='700' height='450'
									classid='clsid:D5E14042-7BF6-4E24-8B01-2F453E8154D7'>
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
$(function () {
	<#if monitor??>
	loginCMS();
 	</#if>
 	<#if type??>
		window.parent.$(".panel-tool .panel-tool-close").click(function(){
			logoutCMS();
		});
	<#else>
		// 设置事件
		window.parent.$(".l-dialog-tc .l-dialog-close", window.parent.wingrid.dialog).click(function (){
			logoutCMS();
			window.parent.wingrid.close();
		});
	</#if>
});
/*****登录CMS******/
function loginCMS(){
	var userName='${monitor.platformUsername!''}';
 	var pw='${monitor.platformPassword!''}';
 	var ipAdd='${monitor.channelIp!''}';
 	var port='80';//默认端口为80
	var channelId='${monitor.channelId!''}';
	if(!validateAddress(ipAdd))
	{
		return;
	}
	//if(""==port){
	//	alert("端口号不能为空！");
	//	return;
	//}
	//if(!validateInteger(port,"端口"))
	//{
	//	return;
	//}
	//if(!(parseInt(port)>=0 && parseInt(port)<=2147483647)){
	//	showMethodInvokedInfo("端口号介于0到2147483647之间！");
	//	return;
	//}
	//if(!validateIntegerRange(port,1,65535,"端口"))
	//{
	//	return;
	//}
	if(""==userName){
		alert("用户名不能为空！");
		return;
	}if(""== pw){
		alert("密码不能为空！");
		return;
	}
	var OCXobj = document.getElementById("PlayViewOCX");
	OCXobj.SetWndMode(0);
 	var ret=OCXobj.Login_V11(userName,pw,ipAdd,port,1);
 	switch(ret){
 		case 0:
 			startPreview(channelId);
 			break;
 		case -1:
 			alert("登录视频出错，请验证用户名，密码，及服务器IP地址等信息！");
 			break;
 		default:
 			break;
 	}
	
}
/*****登出CMS******/
function logoutCMS(){
	var OCXobj = document.getElementById("PlayViewOCX");
	var ret = OCXobj.Logout_V11();
	switch(ret){
		case 0:	
		    OCXobj.StopAllPreview();
			//alert("退出成功！");
			//$("#TextInfo").html("Logout_V11,StopAllPreview接口调用成功！\r");
			break;
		case -1:
			alert("退出失败！");
			//showMethodInvokedInfo("Logout_V11接口调用失败！错误码："+ OCXobj.GetLastError_V11());
			break;
		default:
			break;
	}
}
/*****初始化监控点列表**************/
function initCameraList(){
 	var OCXobj = document.getElementById("PlayViewOCX");
 	var xmlStr=OCXobj.GetResourceInfo(4);
 	var htmlStr="";
 	var xmldom=getXmlDomFromStr(xmlStr);
 	$(xmldom).find("Camera").each(function(){
 	htmlStr+="<li ondblclick='startPreview("+$(this).find("CameraId").text()+");'><a href='#' style='text-decoration:none'>"+$(this).find("CameraName").text()+"</a></li>";
 	});
 	$("#tree").html(htmlStr);
 	
}

//查看视频 
function startPreview(channelId){
	var OCXobj = document.getElementById("PlayViewOCX");
	var ret=OCXobj.StartTask_Preview_FreeWnd_V11(channelId);
	//alert('channelId---------->'+channelId);
	switch(ret){
		case 0:
			//alert("StartTask_Preview_FreeWnd_V11接口调用成功！");
			break;
		case -1:
			alert("StartTask_Preview_FreeWnd_V11接口调用失败！错误码："+ OCXobj.GetLastError_V11());
			break;
		default:
			break;
	}
}
function getXmlDomFromStr(xmlStr){
	var xmldom=null;
	if(navigator.userAgent.toLowerCase().indexOf("msie")!=-1){
		xmldom=new ActiveXObject("Microsoft.XMLDOM");
		xmldom.loadXML(xmlStr);
	}else{
		xmldom=new DOMParser().parseFromString(xmlStr,"text/xml");
	}
	return xmldom;
}
function validateAddress(ipAddress) {
	if (ipAddress.length == 0) {
		alert("IP地址不能为空！");
		return false;
	}
	var tokens = trim(ipAddress).split(".");
	if (tokens.length < 4 || tokens.length > 4) {
		alert("IP地址输入不正确！");
		return false;
	}
	if (tokens[0] == 0)
	{
		alert("IP地址输入不正确！");
		return false;
	}
	for (var k=0; k<4; k++) {
		if (isNaN(tokens[k])|| (tokens[k].length ==0) || (tokens[k].length >3) ||tokens[k]>255 || tokens[k]<0) {
			alert("IP地址输入不正确！");
			return false;
		}
		if (tokens[k].length > 1 && tokens[k].indexOf("0")==0) {
			alert("IP地址输入不正确！");
			return false;
		}
		if ((tokens[k].indexOf(" ")>-1)) {
			alert("IP地址输入不正确！");
			return false;
		}
	}
	return true;
}
function trim(str)
{
	return (str || "").replace( /^\s+|\s+$/g, "" );
}
</script>