<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>拨打电话</title>

<#if Session.defaultPageAction?exists>
	<#assign defaultPageAction = Session.defaultPageAction>
</#if>
<#assign rootPath = rc.getContextPath() >
<#if Session.uiDomain?exists>
	<#assign uiDomain = Session.uiDomain>
</#if>

<link href="${uiDomain!''}/css/normal.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/call/qnviccub.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/call/qnvfunc.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/call/make_call_info.js"></script>
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>

<style type="text/css">
/*---------------------------------语音呼叫 14-9-25 -----------------------------------------*/
.ManPhoto{width:160px; height:160px; position:relative; margin:0 auto 10px;}
.ManPhoto img{width:160px; height:160px;}
.ManName{width:160px; height:28px; text-align:center; color:#fff; line-height:28px; font-size:16px; font-family:Microsoft YaHei; position:absolute; bottom:10px; left:0; z-index:1;}
.AlphaBack{background-color:rgba(0, 0, 0, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c000000',endColorstr='#8c000000');}
.PhotoMask{width:160px; height:160px; background:url(${uiDomain!''}/images/yy_33.png); position:absolute; top:0; left:0; z-index:2;}
.PhoneNumber{font-size: 42px; font-family:Arial, Helvetica, sans-serif; color:#959595; text-align:center;}
.CallWaiting{text-align:center; font-size:18px; color:#959595; font-family:Microsoft YaHei; margin:10px 0 30px;}
.CallWaiting p{margin:10px 0;}
.PhoneBtns{width:256px; margin:0 auto;}
.RingOff{width:256px; height:48px; background:url(${uiDomain!''}/images/yy_34.png); cursor:pointer;}
.RingOff:hover{background:url(${uiDomain!''}/images/yy_35.png);}
.call{width:256px; height:48px; background:url(${uiDomain!''}/images/yy_17.png); cursor:pointer;}
.call:hover{background:url(${uiDomain!''}/images/yy_19.png);}
.ControlDownload{text-align:center; margin-top:15px;}
.ControlDownload img{vertical-align:middle; margin-right:5px; margin-top:-3px; *margin-top:0;}
.fail{font-size:16px; line-height:28px; padding-left:30px; font-family:Microsoft YaHei; border-top:1px solid #d5d5d5; padding-top:5px; margin-top:5px;}
.fail p:first-child{font-size:22px; background:url(${uiDomain!''}/images/yy_error.png) no-repeat 0 13px; padding:5px 0 5px 30px;}
</style>

<script type="text/javascript">
	function initCall(){//为了使该方法在make_call_info.js的初始化方法之后进行，将其转移至onload事件中
		if(isInstalled){
			if (isInit) {
				// 拨打电话
				TV_StartDial(0, '${bCall}');
				displayMsg();
			}else if(TV_GetLineStatus(0)>0){			
				TV_StartDial(0, '${bCall}');
				displayMsg();
			}else {
				$("#successDiv").hide();
				$("#failDiv").show();
			}
		} else {
			$("#successDiv").hide();
			$("#failDiv").show();
		}
	}
	
	// 页面状态显示
	function displayMsg() {
		if (callStatus == 0) {
			$("#reCall").show();
			$("#endCall").hide();
		} else {
			$("#reCall").hide();
			$("#endCall").show();
		}
	}
	
	// 重拨
	function reCallPhone() {
		if (isInit) {
			TV_StartDial(0, '${bCall}');
			displayMsg();
		} else if(TV_GetLineStatus(0)>0){
			TV_StartDial(0, '${bCall}');
			displayMsg();
		} else {
			$("#reCall").show();
			$("#endCall").hide();
		}
	}
	
	// 挂断
	function stopCall() {
	    TV_EnableDoPhone(0,FALSE);
		TV_HangUpCtrl(0);
		//TV_EnableDoPhone(0,TRUE);
		displayMsg();
	}
	
	function AppendStatus(szStatus) {
		//qnviccub.QNV_Tool(QNV_TOOL_WRITELOG,0,szStatus,NULL,NULL,0);//写本地日志到控件注册目录的userlog目录下
		$("#status").html(szStatus);
		//StatusArea.value +=szStatus+"\r\n";
		//StatusArea.scrollTop=StatusArea.scrollHeight;
	}
	function AppendStatusEx(uID,szStatus) {
		uID=uID+1;
		AppendStatus(szStatus);
	}
	
</script>   
</head>
<body onload="initCall()">
	<SCRIPT LANGUAGE="JavaScript" FOR="qnviccub" EVENT="OnQnvEvent(chID,type,handle,result,param,szdata,szdataex)">
		T_GetEvent(chID,type,handle,result,szdata)
	</SCRIPT>
	
	<div id="voiceInterfaceBox_${userName}" style="width:300px;">
		<div class="h_10"></div>
		<div class="ManPhoto">
	    	<div class="PhotoMask"></div>
	    	<div class="ManName AlphaBack">${userName! }</div>
	    	<#if userImg?? && userImg!="">
	    		<img src="${userImg}" />
	    	<#else>
	    		<img src="${uiDomain!''}/images/yy_31.png" />
	    	</#if>
	    	
	    </div>
	    <div class="PhoneNumber">
	    	<#if bCall??>${bCall }</#if>
	    </div>
	    <div id="successDiv" class="success">
	    	<div class="CallWaiting">
		    	<p id="status"></p>
		    </div>
		    <div class="PhoneBtns">
		        <div id="endCall" class="RingOff hide" title="挂断" onclick="stopCall()"></div>
		    	<div id="reCall" class="call hide" title="重拨" onclick="reCallPhone()"></div>
		    </div>
	    	<div class="ControlDownload FontDarkBlue"><img src="${uiDomain!''}/images/control.png"><a href="${RESOURCE_DOMAIN}/public_download/CC301SDK5.8.zip">语音盒控件下载</a></div>
	    </div>
	    <div id="failDiv" class="fail hide">
	    	<p class="FontRed">语音盒设备启动失败！</p>
	        <p>设备未连接电脑？</p>
	        <p>其他页面或程序正在使用设备？</p>
	        <p>控件未安装？<span class="FontDarkBlue"><a href="${RESOURCE_DOMAIN}/public_download/CC301SDK5.8.zip">点击下载</a></span></p>
	    </div>
	    <object style="display:none;" classid="clsid:F44CFA19-6B43-45EE-90A3-29AA08000510" id="qnviccub" data="DATA:application/x-oleobject;BASE64,GfpM9ENr7kWQoymqCAAFEAADAAD7FAAADhEAAA==" width="33" height="33"></object>
	</div>
	
</body>
</html>
