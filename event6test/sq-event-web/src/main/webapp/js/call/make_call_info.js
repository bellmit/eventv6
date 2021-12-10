var callInNo;

$(function(){
	if (true) {
		// 打开设备
		TV_Initialize();
		if (isInstalled && isInit) {//变量isInstallled,isInit见于qnvfunc.js 20131231
			// 打开MIC
			TV_EnableMic(0,TRUE);
			// 打开耳机
			TV_EnableMicSpk(0,TRUE);
			// 打开喇叭
			TV_OpenDoPlay(0);
			// 接通话机与电话线连接
			TV_EnableRing(0,TRUE);
		}
	}	
});
    
function  T_GetEvent(uID,uEventType,uHandle,uResult,szdata) {
	//var vValueArray=qnviccub.QNV_Event(0,2,0,"","",1024);
	var vValue=" type="+uEventType+" Handle="+uHandle+" Result="+uResult+" szdata="+szdata;
	switch(uEventType)
	{
	/*
	case BriEvent_PhoneHook:// 本地电话机摘机事件
		AppendStatusEx(uID,"本地电话机摘机");
	break;	
	case BriEvent_PhoneDial:// 只有在本地话机摘机，没有调用软摘机时，检测到DTMF拨号
		AppendStatusEx(uID,"本地话机拨号");
	break;	
	case BriEvent_PhoneHang:// 本地电话机挂机事件
		AppendStatusEx(uID,"本地电话机挂机");
	break;
	case BriEvent_CallIn:// 外线通道来电响铃事件
		AppendStatusEx(uID,"外线通道来电响铃事件");
	break;*/
	case BriEvent_GetCallID://得到来电号码
		var isCallIn = $("#isCallIn");
		if(isCallIn!=undefined && isCallIn.val()=='true'){
			var SerialNo=qnviccub.QNV_DevInfo(uID,QNV_DEVINFO_GETSERIAL);
			callInNo = szdata;
			showCallInNo(szdata);
			AppendStatusEx(uID,"得到来电号码");
		}
	break;
	/*case BriEvent_StopCallIn:// 对方停止呼叫(产生一个未接电话)
		AppendStatusEx(uID,"对方停止呼叫");
	break;
	*/
	case BriEvent_DialEnd:// 调用开始拨号后，全部号码拨号结束
		AppendStatusEx(uID,"开始拨号");
	break;
	/*
	case BriEvent_PlayFileEnd:// 播放文件结束事件
		AppendStatusEx(uID,"播放文件结束事件");
	break;
	case BriEvent_PlayMultiFileEnd:// 多文件连播结束事件
		AppendStatusEx(uID,"多文件连播结束事件");
	break;
	case BriEvent_PlayStringEnd://播放字符结束
		AppendStatusEx(uID,"播放字符结束");
	break
	case BriEvent_RepeatPlayFile:// 播放文件结束准备重复播放
		AppendStatusEx(uID,"播放文件结束准备重复播放");
	break;
	case BriEvent_SendCallIDEnd:// 给本地设备发送震铃信号时发送号码结束
		AppendStatusEx(uID,"给本地设备发送震铃信号时发送号码结束");
	break;
	case BriEvent_RingTimeOut://给本地设备发送震铃信号时超时
		AppendStatusEx(uID,"给本地设备发送震铃信号时超时");
	break;	
	case BriEvent_Ringing://正在内线震铃
		AppendStatusEx(uID,"正在内线震铃");
	break;
	case BriEvent_Silence:// 通话时检测到一定时间的静音.默认为5秒
		AppendStatusEx(uID,"通话时检测到一定时间的静音");
	break;
	case BriEvent_GetDTMFChar:// 线路接通时收到DTMF码事件
		AppendStatusEx(uID,"线路接通时收到DTMF码事件");
	break;
	*/
	case BriEvent_RemoteHook:// 拨号后,对方摘机事件
		AppendStatusEx(uID,"等待对方接听");
	break;
	case BriEvent_RemoteHang: //对方挂机事件
		AppendStatusEx(uID,"对方挂机");
		stopCall();
		$("#reCall").css("display", "block");
		$("#endCall").css("display", "none");
	break;
	case BriEvent_Busy:// 检测到忙音事件,表示PSTN线路已经被断开
		AppendStatusEx(uID,"对方正忙");
		stopCall();
		$("#reCall").css("display", "block");
		$("#endCall").css("display", "none");
	break;
	/*
	case BriEvent_DialTone:// 本地摘机后检测到拨号音
		AppendStatusEx(uID,"本地摘机后检测到拨号音");
	break;
	*/
	case BriEvent_RingBack:// 电话机拨号结束呼出事件。
		AppendStatusEx(uID,"接通中...");
	break;
	/*
	case BriEvent_MicIn:// MIC插入状态
		AppendStatusEx(uID,"MIC插入状态");
	break;
	case BriEvent_MicOut:// MIC拔出状态
		AppendStatusEx(uID,"MIC拔出状态");
	break;
	case BriEvent_FlashEnd:// 拍插簧(Flash)完成事件，拍插簧完成后可以检测拨号音后进行二次拨号
		AppendStatusEx(uID,"拍插簧(Flash)完成事件，拍插簧完成后可以检测拨号音后进行二次拨号");
	break;
	case BriEvent_RefuseEnd:// 拒接完成
		AppendStatusEx(uID,"拒接完成");
	break;
	case BriEvent_SpeechResult:// 语音识别完成 
		AppendStatusEx(uID,"语音识别完成");
	break;
	case BriEvent_FaxRecvFinished:// 接收传真完成
		AppendStatusEx(uID,"接收传真完成");
	break;
	case BriEvent_FaxRecvFailed:// 接收传真失败
		AppendStatusEx(uID,"接收传真失败");
	break;
	case BriEvent_FaxSendFinished:// 发送传真完成
		AppendStatusEx(uID,"发送传真完成");
	break;
	case BriEvent_FaxSendFailed:// 发送传真失败
		AppendStatusEx(uID,"发送传真失败");
	break;
	case BriEvent_OpenSoundFailed:// 启动声卡失败
		AppendStatusEx(uID,"启动声卡失败");
	break;
	case BriEvent_UploadSuccess://远程上传成功
		AppendStatusEx(uID,"远程上传成功");
	break;
	case BriEvent_UploadFailed://远程上传失败
		AppendStatusEx(uID,"远程上传失败");
	break;
	*/
	case BriEvent_EnableHook:// 应用层调用软摘机/软挂机成功事件
		AppendStatusEx(uID,"挂断成功");
	break;
	/*
	case BriEvent_EnablePlay:// 喇叭被打开或者/关闭
		AppendStatusEx(uID,"喇叭被打开或者/关闭");
	break;
	case BriEvent_EnableMic:// MIC被打开或者关闭
		AppendStatusEx(uID,"MIC被打开或者关闭");
	break;
	case BriEvent_EnableSpk:// 耳机被打开或者关闭
		AppendStatusEx(uID,"耳机被打开或者关闭");
	break;
	case BriEvent_EnableRing:// 电话机跟电话线(PSTN)断开/接通
		AppendStatusEx(uID,"电话机跟电话线(PSTN)断开/接通");
	break;
	case BriEvent_DoRecSource:// 修改录音源
		AppendStatusEx(uID,"修改录音源");
	break;
	*/
	case BriEvent_DoStartDial:// 开始软件拨号
		AppendStatusEx(uID,"开始软件拨号");
	break;
	/*
	case BriEvent_RecvedFSK:// 接收到FSK信号，包括通话中FSK/来电号码的FSK
		AppendStatusEx(uID,"接收到FSK信号，包括通话中FSK/来电号码的FSK");
	break;
	*/
	case BriEvent_DevErr://设备错误
		AppendStatusEx(uID,"设备错误");
	break;
	case BriEvent_CC_Connecting://呼叫正在连接
		AppendStatusEx(uID,"呼叫正在连接");
	break;
	case BriEvent_CC_Connected://呼叫连通
		AppendStatusEx(uID,"呼叫连通");
	break;
	case BriEvent_CC_ReplyBusy://对方回复忙过来
		AppendStatusEx(uID,"对方忙");
	break;
	case BriEvent_CC_CallFinished://呼叫结束
		AppendStatusEx(uID,"呼叫结束");
	break;
	case BriEvent_CC_CallOutSuccess://呼叫成功，正在呼叫
		AppendStatusEx(uID,"正在呼叫");
	break;
	case BriEvent_CC_CallOutFailed://呼叫失败
		AppendStatusEx(uID,"呼叫失败");
	break;
	default:
		if(uEventType < BriEvent_EndID)
			//AppendStatusEx(uID,"忽略其它事件发生:ID=" + uEventType+ vValue);	
	break;
	}
	
}

function showCallInNo(callInNo){
	//alert("呼入电话："+callInNo);
	cc_callIn(callInNo,null);
}

// 挂断电话
function stopCall() {
	TV_HangUpCtrl(0);
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

