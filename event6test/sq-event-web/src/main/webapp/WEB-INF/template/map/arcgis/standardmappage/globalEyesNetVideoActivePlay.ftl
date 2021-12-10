<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>全球眼监控-HIKVISION V2.3</title>
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
</head>
<body style="font-size:12px; margin:0 auto 0 auto; height:100%;overflow:hidden;" >
	<div style="padding-left:10px; line-height:25px; text-align:left;">
	<span id="msg" style="float:left;padding-left:15px"></span>若无法观看视频，请点击下载：<a href="${RESOURCE_DOMAIN}/public_download/WebComponents.exe" title="点击下载" style="text-decoration:none;">全球眼视频播放控件</a>
	</div>
	<div id="NetPlayOCX1" style="height:95%;">
		<object classid="CLSID:CAFCF48D-8E34-4490-8154-026191D73924" codebase="${rc.getContextPath()}/zzgl_map_data/situation/NetVideoActiveX23.cab#version=2,3,19,1" standby="Waiting..." id="HIKOBJECT1" width="100%" height="100%" name="HIKOBJECT1" ></object>
	</div>
</body>
</html>
<script language="javascript">
var m_iNowChanNo = 0;                           //当前通道号
var m_bDVRControl = null;						 //OCX控件对象
var m_iProtocolType = 0;                         //协议类型，0 – TCP， 1 - UDP
var m_iStreamType = 0;                           //码流类型，0 表示主码流， 1 表示子码流
//var m_iIPChanStart = 32; //如果设备有IP通道，IP通道号取值从32开始！
//var m_iIPChanStart = 0;//IP通道个数大于等于64，通道号取值从0开始！
$(function () {
	if(document.getElementById("HIKOBJECT1").object == null){
		alert("请先下载控件并安装！");
		m_bDVRControl = null;
	}else{
		m_bDVRControl = document.getElementById("HIKOBJECT1");
		<#if monitor??>
		loginDev();
	 	</#if>
	}
	
 	<#if type??>
		window.parent.$(".panel-tool .panel-tool-close").click(function(){
			logoutDev();
		});
	<#else>
		// 设置事件
		window.parent.$(".l-dialog-tc .l-dialog-close", window.parent.wingrid.dialog).click(function (){
			logoutDev();
			window.parent.wingrid.close();
		});
	</#if>
});
/*****登录******/
function loginDev(){
	var szDevIp='${monitor.channelIp!''}';
 	var szDevPort='8000';//默认端口为8000
	var szDevUser='${monitor.platformUsername!''}';
 	var szDevPwd='${monitor.platformPassword!''}';
 	var channelId='${monitor.channelId!''}';
	if(!validateAddress(szDevIp)){
		return;
	}
	if(szDevUser==""){
		alert("用户名不能为空！");
		return;
	}if(szDevPwd==""){
		alert("密码不能为空！");
		return;
	}
	var m_iLoginUserId = -1;//注册设备用户ID
	m_iLoginUserId = m_bDVRControl.Login(szDevIp,szDevPort,szDevUser,szDevPwd);
 	if(m_iLoginUserId == -1){
        var dRet = m_bDVRControl.GetLastError();
        alert("登录视频出错，请验证用户名，密码，及服务器IP地址等信息！错误号：" + dRet);
	}else{
		document.getElementById("HIKOBJECT1").SetUserID(m_iLoginUserId);
		startPreview(channelId);
	}
	
}
/*****登出******/
function logoutDev(){
	var ret = m_bDVRControl.Logout();
}

//查看视频 
function startPreview(channelId){
	var m_iNowChanNo = channelId;
	var bRet = m_bDVRControl.StartRealPlay(m_iNowChanNo, m_iProtocolType, m_iStreamType);
    if (!bRet) {
        var dRet = m_bDVRControl.GetLastError();
        alert("预览通道" + m_iNowChanNo + "失败！错误号:" + dRet);
    }
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
function trim(str){
	return (str || "").replace( /^\s+|\s+$/g, "" );
}
</script>