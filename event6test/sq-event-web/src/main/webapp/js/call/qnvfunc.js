/////////////////////////////////////////////////////////////////////////
var uMaxID=64;
var uPlayFileID=new Array(64);
var uRecordID=new Array(64);
var uCCSessID=-1;
var vConfID=0;
var callStatus=0;
var isInit = false;
var isInstalled = false;
////////////////////////////////////////////////////////////////////////

function  T_WaitForWin(vWin)
{
	var vTimeout=5000;	
	var vBegintime=new Date();
	var vEndtime=new Date();
	while(vWin.vInit != 1 && vEndtime.getTime() - vBegintime.getTime() < (vTimeout*2))//ns超时
	{
		if(vEndtime.getTime() - vBegintime.getTime() > vTimeout)//1s还没有完成，使用等待方式
		{
			Sleep(100);//等待100ms
		}
		vEndtime=new Date();
	}	
}

function I_CheckActiveX()
{ 
	try{
	      	var Ole = new ActiveXObject("qnviccub.qnviccub");	      	
	      	AppendStatus("已经注册了ACTIVEX");
	      	alert("已经注册了ACTIVEX");
	  }catch(e)
   	  { 
    	      	AppendStatus("未安装ACTIVEX,请使用regsvr32 qnviccub.dll先注册/或者开发包bin目录'组件注册'");
   		      	alert("未安装ACTIVEX,请使用regsvr32 qnviccub.dll先注册/或者开发包bin目录'组件注册'");
   	  }
}
function TV_Initialize()
{
	try{
	      	var Ole = new ActiveXObject("qnviccub.qnviccub");	      	
	  }catch(e)
   	  { 
    	      	AppendStatus("未安装控件,请联系系统管理员");
    	      	isInstalled = false;
    	      	return;
   	  }

	isInstalled = true;
	if(qnviccub.QNV_DevInfo(0,QNV_DEVINFO_GETCHANNELS) < 0)
	{
 		qnviccub.QNV_OpenDevice(0,0,0);
		//初始化状态控制
		var channels=qnviccub.QNV_DevInfo(0,QNV_DEVINFO_GETCHANNELS);
		if(channels > 0)
 		{ 
			isInit = true;
 			//AppendStatus("打开设备成功 通道数:"+channels+" 序列号:"+qnviccub.QNV_DevInfo(0,QNV_DEVINFO_GETSERIAL)+" 设备类型:"+qnviccub.QNV_DevInfo(0,QNV_DEVINFO_GETTYPE)+" ver:"+qnviccub.QNV_DevInfo(0,QNV_DEVINFO_FILEVERSION));	
 		}else
		{
			AppendStatus("打开设备失败,请检查设备是否已经插入并安装了驱动,并且没有其它程序已经打开设备");		
		}			
		//初始化变量
		for(i=0;i<uMaxID;i=i+1)
		{
			uPlayFileID[i]=-1;
			uRecordID[i]=-1;			
		}
	}else
	{
		if(TV_GetLineStatus(0) > 0){
			isInit = true;
		}else{
	 		AppendStatus("设备已经被打开，不需要重复打开");		
	 	}
 	}
}

function TV_StopConference()
{
	if(vConfID > 0)	
	{
		qnviccub.QNV_Conference(-1,vConfID,QNV_CONFERENCE_DELETECONF,0,0);//删除会议
		vConfID = 0;
		AppendStatus("会议停止");
	}
}
function TV_StartConference()
{
	if(qnviccub.QNV_OpenDevice(ODT_SOUND,0,0) <= 0)//使用声卡模块前先打开声卡模块
		AppendStatus("打开声卡模块失败");
	else
	{
	TV_StopConference();
	vConfID=qnviccub.QNV_Conference(-1,0,QNV_CONFERENCE_CREATE,0,"");//创建一个空会议
	if(vConfID <= 0) AppendStatus("创建会议失败");	
	else
	{
	var vRet=qnviccub.QNV_Conference(SOUND_CHANNELID,vConfID,QNV_CONFERENCE_ADDTOCONF,0,"");//把通道加入到会议中
	AppendStatus("加入会议完成,Ret="+vRet);	
	//把通道0,就是第一个设备的通道加入到会议里,这样设备的声音就会声卡出来，声卡MIC的声音就会到电话线		
	for(i=0;i<qnviccub.QNV_DevInfo(0,QNV_DEVINFO_GETCHANNELS);i=i+1)
	{	
		vRet=qnviccub.QNV_Conference(i,vConfID,QNV_CONFERENCE_ADDTOCONF,0,"");
		AppendStatus("加入会议完成,Ret="+vRet);	
	}
	//避免声卡MIC干扰拨号，我们禁止声卡MIC的声音到电话线。
	//qnviccub.QNV_Conference(SOUND_CHANNELID,vConfID,QNV_CONFERENCE_ENABLEMIC,0,"");
	AppendStatus("会议创建完成,会议ID="+vConfID);	
	}
	}
}


function TV_InitCCModule()
{
	if(qnviccub.QNV_OpenDevice(ODT_CC,0,QNV_CC_LICENSE) > 0)
	{
		AppendStatus("加载CC网络模块成功");
	}
	else
		AppendStatus("加载CC网络模块失败");
}
function TV_Disable()
{
	if (isInstalled && qnviccub != null) {
		qnviccub.QNV_CloseDevice(ODT_ALL,0);//关闭所有设备
		AppendStatus("关闭设备完成.");
	}
}

//---------------------------------------
function TV_EnableHook(uID,bEnable)
{
	if (qnviccub != null) {
		qnviccub.QNV_SetDevCtrl(uID,QNV_CTRL_DOHOOK,bEnable);
		AppendStatusEx(uID,bEnable?"软摘机":"软挂机");
		callStatus = 0;
	}
}

function TV_OffHookCtrl(uID)
{
	TV_EnableHook(uID,TRUE);
}

function TV_HangUpCtrl(uID)
{
	TV_EnableHook(uID,FALSE);
}
//----------------------------------------
function TV_EnableMic2Line(uID,bEnable)
{
	qnviccub.QNV_SetDevCtrl(uID,QNV_CTRL_DOMICTOLINE,bEnable);
}
function TV_EnableMic(uID,bEnable)
{
	TV_EnableMic2Line(uID,bEnable);
}
//----------------------------------------
function TV_EnableDoPhone(uID,bEnable)
{
	qnviccub.QNV_SetDevCtrl(uID,QNV_CTRL_DOPHONE,bEnable);
}
function TV_EnableRing(uID,bEnable)
{
	TV_EnableDoPhone(uID,bEnable);
}
function TV_StartRing(uID,bEnable)
{
	if(bEnable)
		qnviccub.QNV_General(uID,QNV_GENERAL_STARTRING,0,"1234");	
	else
		qnviccub.QNV_General(uID,QNV_GENERAL_STOPRING,0,"");	
}
//--------------------------------------

function TV_EnableDoPlay(uID,bEnable)
{
	qnviccub.QNV_SetDevCtrl(uID,QNV_CTRL_DOPLAY,bEnable);
}
function TV_OpenDoPlay(uID)
{
	TV_EnableDoPlay(uID,TRUE);
}
function TV_CloseDoPlay(uID)
{
	TV_EnableDoPlay(uID,FALSE);
}
//----------------------------------------------
function TV_EnableLine2Spk(uID,bEnable)
{
	qnviccub.QNV_SetDevCtrl(uID,QNV_CTRL_DOLINETOSPK,bEnable);
}
function TV_EnableMicSpk(uID,bEnable)
{
	TV_EnableLine2Spk(uID,bEnable);	
}
//----------------------------------------------

function TV_EnablePlay2Spk(uID,bEnable)
{
	qnviccub.QNV_SetDevCtrl(uID,QNV_CTRL_DOPLAYTOSPK,bEnable);
}

function TV_EnableRingPower(uID,bEnable)
{
	if(qnviccub.QNV_GetDevCtrl(uID,QNV_CTRL_DOPHONE) && bEnable)
	{
		alert("请先断开电话机");
	}else
	{
		qnviccub.QNV_SetDevCtrl(uID,QNV_CTRL_RINGPOWER,bEnable);				
	}
}

function TV_RefuseCallIn(uID)
{
	if(qnviccub.QNV_GetDevCtrl(uID,QNV_CTRL_RINGTIMES) <= 0)
	{
		alert("没有来电,无效的拒接");
	}else
		qnviccub.QNV_General(uID,QNV_GENERAL_STARTREFUSE,0,0);	
}
function TV_StartFlash(uID)
{
	if(qnviccub.QNV_GetDevCtrl(uID,QNV_CTRL_DOHOOK) <= 0
		&& qnviccub.QNV_GetDevCtrl(uID,QNV_CTRL_PHONE) <= 0)
	{
		alert("没有摘机状态，无效的拍插簧");
	}else
	{
		if(qnviccub.QNV_General(uID,QNV_GENERAL_STARTFLASH,FT_ALL,"") <= 0)//1099
			alert("拍插簧失败");
	}
}

function TV_EnablePhoneRing(uID,bEnable)
{
	if(bEnable)
	{
		if(qnviccub.QNV_GetDevCtrl(uID,QNV_CTRL_DOPHONE))
		{
			alert("请先断开电话机");
		}else
		{
			var  szCallID="1234567890";
			qnviccub.QNV_SetParam(uID,QNV_PARAM_RINGCALLIDTYPE,DIALTYPE_FSK);//设置送码方式为一声后FSK模式,默认为一声前dtmf模式//DIALTYPE_DTMF
			qnviccub.QNV_General(uID,QNV_GENERAL_STARTRING,0,szCallID);	
			AppendStatusEx(uID,"开始内线模拟间隔震铃 -> 模拟来电号码："+szCallID);
		}
	}else
	{
	
		qnviccub.QNV_General(uID,QNV_GENERAL_STOPRING,0,0);	
		AppendStatusEx(uID,"停止内线震铃");
	}
}

function TV_StartPlayFile(uID,szFile)
{
	var vFilePath=qnviccub.QNV_Tool(QNV_TOOL_SELECTFILE,0,"wav files|*.wav|all files|*.*||",'',0,0);
	if(vFilePath.length > 0)
	{
	AppendStatus("选择文件:"+vFilePath);
	TV_StopPlayFile(uID);//先停止上次播放的句柄
	var vmask=PLAYFILE_MASK_REPEAT;//循环播放
	uPlayFileID[uID]=qnviccub.QNV_PlayFile(uID,QNV_PLAY_FILE_START,0,vmask,vFilePath);	
	if(uPlayFileID[uID] <= 0)
	{
	 	AppendStatusEx(uID,"播放失败:"+vFilePath);	
	}else
	{
		AppendStatusEx(uID,"开始播放文件:"+vFilePath);
	}
	}else
		AppendStatus("没有选择文件");
}

function TV_StopPlayFile(uID)
{
	if(uPlayFileID[uID] > 0)
	{
	qnviccub.QNV_PlayFile(uID,QNV_PLAY_FILE_STOP,uPlayFileID[uID],0,0);
	AppendStatusEx(uID,"停止播放");
	uPlayFileID[uID] =0;
	}else
	{
	AppendStatusEx(uID,"未播放的句柄");
	}
}

function TV_StartRecordFile(uID,szFile)
{
	var vFilePath=qnviccub.QNV_Tool(QNV_TOOL_SELECTFILE,1,"wav files|*.wav|all files|*.*||",0,0,0);
	if(vFilePath.length > 0)
	{
	AppendStatus("选择文件:"+vFilePath);
	TV_StopRecordFile(uID);
	var vFormatID=BRI_WAV_FORMAT_IMAADPCM8K4B;//选择使用4K/S的ADPCM格式录音
	var vmask=RECORD_MASK_ECHO|RECORD_MASK_AGC;//使用回音抵消后并且自动增益的数据
	//qnviccub.QNV_RecordFile(uID,QNV_RECORD_FILE_SETROOT,0,0,"c:\\recfile\\");
	uRecordID[uID]=qnviccub.QNV_RecordFile(uID,QNV_RECORD_FILE_START,vFormatID,vmask,vFilePath);
	if(uRecordID[uID] <= 0)
	{
	 	AppendStatusEx(uID,"录音失败:"+vFilePath);	
	}else
	{
		AppendStatusEx(uID,"开始录音文件: id="+uRecordID[uID]+"  "+vFilePath);
	}
	}else
		AppendStatus("没有选择文件");
}

function TV_StopRecordFile(uID)
{
	if(uRecordID[uID] > 0)
	{
		//var vRecPath=qnviccub.QNV_GetRecFilePath(uID,uRecordID[uID]);
		var vRecPath=qnviccub.QNV_RecordFile(uID,QNV_RECORD_FILE_PATH,uRecordID[uID],0,0);
		var vElapse=qnviccub.QNV_RecordFile(uID,QNV_RECORD_FILE_ELAPSE,uRecordID[uID],0,0);
		//qnviccub.QNV_RecordFile(uID,QNV_RECORD_FILE_STOP,uRecordID[uID],0,"e:\\a.wav");//保存到e:\\a.wav删除原来路径的录音文件
		qnviccub.QNV_RecordFile(uID,QNV_RECORD_FILE_STOP,uRecordID[uID],0,0);	
		AppendStatusEx(uID,"停止录音:"+vRecPath+"  录音时间:"+vElapse);
		uRecordID[uID]=0;
	}
}


function TV_DeleteRecordFile(uID)
{
	var vRet=qnviccub.QNV_CallLog(uID,QNV_CALLLOG_DELRECFILE,"",0);
	if(vRet <= 0)
	{
		alert('删除失败:'+vRet);
	}else
		alert('删除完成');
}

function TV_StartDial(uID,szCode)
{//正常拨号必须使用 DIALTYPE_DTMF
	if(qnviccub.QNV_General(uID,QNV_GENERAL_STARTDIAL,DIALTYPE_DTMF,szCode) <= 0)
	{
		callStatus = 0;
		AppendStatusEx(uID,"拨号失败");
	}else
	{
		callStatus = 1;
		AppendStatusEx(uID,"开始拨号");
	}
}

function TV_GetDiskList()
{
	var vDiskList=qnviccub.QNV_Tool(QNV_TOOL_DISKLIST,0,0,0,0,0);
	AppendStatus("按逗号分隔的盘符列表:"+vDiskList);	
}
function TV_GetFreeSpace(szDiskname)
{
	var vFreeSpace=qnviccub.QNV_Tool(QNV_TOOL_DISKFREESPACE,0,szDiskname,0,0,0);
	AppendStatus(szDiskname+" 空闲大小为:"+vFreeSpace+"(M)");	
}
function TV_GetTotalSpace(szDiskname)
{
	var vTotalSpace=qnviccub.QNV_Tool(QNV_TOOL_DISKTOTALSPACE,0,szDiskname,0,0,0);
	AppendStatus(szDiskname+" 总共大小为:"+vTotalSpace+"(M)");	
}
function TV_BrowerPath()
{
	var vPath=qnviccub.QNV_Tool(QNV_TOOL_SELECTDIRECTORY,0,"选择目录",0,0,0);
	AppendStatus("选择目录:"+vPath);	
}
function TV_SelectFile()
{
	var vFilePath=qnviccub.QNV_Tool(QNV_TOOL_SELECTFILE,0,"wav files|*.wav|all files|*.*||",0,0,0);
	AppendStatus("选择文件:"+vFilePath);	
}

//登陆CC
function TV_LoginCC(cc,pwd)
{
	if(qnviccub.QNV_CCCtrl(QNV_CCCTRL_ISLOGON,NULL,0) > 0)
         alert('已经登陆,请先离线');
	else
	{
		var v=cc+','+pwd;
		var vret=qnviccub.QNV_CCCtrl(QNV_CCCTRL_LOGIN,v,0);
		if(vret <= 0)//开始登陆
             alert('登陆CC失败:'+vret);
		else
			AppendStatus("开始登陆CC:"+cc);
	}
}
//CC离线
function TV_LogoutCC()
{
	qnviccub.QNV_CCCtrl(QNV_CCCTRL_LOGOUT,NULL,0);//离线
	AppendStatus("已离线");
}

function T_GetMsgValue(vmsg,vkey)
{
	var strs = vmsg.split("\r\n");
	for(var i = 0; i < strs.length; i++)  
  	{  
  		var vline=strs[i];
    		var vindex=vline.indexOf(vkey);
		if(vindex != -1)  
		{
			return vline.slice(vkey.length+1);
		}
  	}
  	return "";
}

function TV_GetLineStatus(uId){
	return qnviccub.QNV_General(uId,QNV_GENERAL_CHECKLINESTATE,0,0);
}
