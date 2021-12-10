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
	function initCall(){
	
	}
	
	// 重拨
	function reCallPhone() {
		var num = '${bCall }';
		if(isNaN(num)){
			return;
		}
		parent.makeCallSoftPhone(num);
	}
	
	// 挂断
	function stopCall() {
		parent.releaseCallSoftPhone();
	}
	
	
</script>   
</head>
<body onload="initCall()">
	
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
	    </div>
	 </div>
	
</body>
</html>
