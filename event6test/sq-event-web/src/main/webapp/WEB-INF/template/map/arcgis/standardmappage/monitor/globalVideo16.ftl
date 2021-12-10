<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>大华视频播放</title>
 <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
 <script type="text/javascript" src="${SQ_ZZGRID_URL}/component/msgClient.jhtml"></script>

    <style type="text/css">
        *{margin: 0;padding: 0;}
    </style>

    <script language="javascript" for="DPSDK_OCX" event="OnWndLBtnClick(nWndId, nWndNo, xPos, yPos)">
        currWndNo = nWndNo;
    </script>
    <script type="text/javascript">
        var currWndNo=0;
        var status = 0;
        var gWndId =0;
        var wndNum = 1;
        var obj;
        var channelList=new Array(wndNum)
        function beforeClose(){
            currWndNo = status=0;
            wndNum=1;
            channelList=new Array(wndNum);
        }

        function closeVideo(){
            beforeClose();
            logOut();
        }
        function receive(msg){
            try{
                if(msg.type==0){ //登录
                    login_play(msg.data);
                }else if(msg.type==1){ //制定窗口播放
                    wndNum = msg.num;
                    createWnd(msg.num);
                }else if(msg.type==2){ //关闭
                    closeVideo();
                }else if(msg.type==3){ //截图
                    var path = capturePicture();
                    send({type:3,path:path});
                }
            }catch (e){
                $(document.body).html('若无法观看视频，请点击下载：<a  href="${APP_URL_RES}/public_download/DPSDK_OCX_Win32.exe" title="点击下载" style="text-decoration:none;color:blue;">视频播放控件</a>');
            }
        }

        $(function(){
        
            gmMsgClient.addObserver(window.parent, receive, "videoMsg");
            var width = $(window).width();
            var height = $(window).height();
            $("#videoBox").css({width:width,height:height});
            initLoadWnd();
        });
        function send(msg){
            gmMsgClient.sent(window.parent, "videoMsg", msg);
        }

        function initLoadWnd(){
            try{
                obj = document.getElementById("DPSDK_OCX");
                gWndId = obj.DPSDK_CreateSmartWnd(0, 0, 100, 100);
                createWnd(1)
            }catch(e){
                $(document.body).html('若无法观看视频，请点击下载：<a  href="${APP_URL_RES}/public_download/DPSDK_OCX_Win32.exe" title="点击下载" style="text-decoration:none;color:blue;">视频播放控件</a>');
            }
        }

        function createWnd(num){
            try{
                var nWndCount = num;
                obj.DPSDK_SetWndCount(gWndId, nWndCount);
                obj.DPSDK_SetSelWnd(gWndId, 0);
            }catch(e){
                $(document.body).html('若无法观看视频，请点击下载：<a  href="${APP_URL_RES}/public_download/DPSDK_OCX_Win32.exe" title="点击下载" style="text-decoration:none;color:blue;">视频播放控件</a>');
            }
        }
        var oldData;

        function isSame(newData){
            if(!oldData)return false;
            if(oldData.platformIp == newData.platformIp &&  oldData.port == newData.port &&   oldData.platformUsername == newData.platformUsername &&  oldData.platformPassword == newData.platformPassword)
            {
                return true;
            }else{
                false;
            }
        }

        function ShowCallRetInfo(nRet, strInfo){
            var str = "";
            if(nRet == 0){
                str = " ";
            }else{
                str = " (" + strInfo + "失败！错误码：" + nRet+")";
            }
            send({type:1,note:str});
            return nRet;
        }

        function login_play(data){
            //先登陆 或者 和之前账户不一样，重新登陆
            if(status==0 || !isSame(data)){
                var n = ShowCallRetInfo(obj.DPSDK_Login(data.platformIp, data.port, data.platformUsername, data.platformPassword),"登录");
                if(n==0){
                    send({type:1,note:"(请稍等！正在加载中...)"});
                    status=1;
                    oldData = data;
                }
            }
            setTimeout(function(){
                var nWndNo = obj.DPSDK_GetSelWnd(gWndId);
                ShowCallRetInfo(obj.DPSDK_DirectRealplayByWndNo(gWndId, nWndNo, data.channelId, 1, 1, 1), "实时播放");
            },500);
//            channelList[currWndNo]=true;
//            var b = false;
//            for(var i=0;i<wndNum;i++){
//                if(!channelList[i]){
//                    b=true;
//                    currWndNo=i;
//                    break;
//                }
//            }
//            if(!b){
//                currWndNo=0;
//            }
        }

        function logOut(){
            var obj = document.getElementById("DPSDK_OCX");
            var nWndNo = obj.DPSDK_GetSelWnd(gWndId);
            for(var i=0;i<wndNum;i++){
                obj.DPSDK_StopRealplayByWndNo(gWndId, nWndNo);
            }
            obj.DPSDK_Logout();
        }

        //抓图功能
        function capturePicture(){
           var  tempdir="";//document.getElementById("tempdir").value;
           try {
    			 var WshShell = new ActiveXObject("WScript.Shell");
    			 tempdir=WshShell.SpecialFolders("Desktop");
    		}
    		catch(err) {
    		    alert("请在Internet选项中启用ActiveX控件");
    		    return ;
    		}
    		if(tempdir==""){
    			 alert("该功能只支持IE");
     		    return ;
    		}
            // 获取ocx实例
            var obj = document.getElementById("DPSDK_OCX");
            //存放路径 可以放在 "默认的临时文件路径" 通过 System.getProperty("java.io.tmpdir") 获取
            //再用流打开上传到文件系统做关联
           var picName="20190402203801.jpg";
            //picName=Date.parse(new Date())+".png"; 
            picName=getNowFormatDate()+".png"; 
            tempdir=tempdir+"\\南平网格监控截图\\";
           var strPicPathName =tempdir+picName;
            // 抓图
            var nResult =obj.DPSDK_CapturePictureByWndNo(gWndId, currWndNo,strPicPathName);
            if(nResult == 0){
            	alert("图片已保存在："+"桌面\\南平网格监控截图文件夹");
          /*
            	var szIp = '192.168.52.40';
            	var szName = 'kaifa01';
            	var szPwd = 'kaifa2017@ffcs';
            	var timestamp = Date.parse(new Date())/1000;
            	alert(timestamp);
            	alert(getNowFormatDate());
            	var nRet = obj.DPSDK_UploadFtpPic('$1$0$1', timestamp, strPicPathName, "ftp://" + szIp, szName, szPwd);
            	if(nRet != 0)
            	    ShowCallRetInfo( nRet, "上传");
            	else{
            		alert(getNowFormatDate());
            		// alert("已上传至ftp://" + szIp); 
            	}
            	*/   
                // 成功
                return picName;
            }
            else{
                return null;
            }
        }
        String.prototype.replaceAll = function(s1,s2){ 
        	return this.replace(new RegExp(s1,"gm"),s2); 
        }
        function getCurWndNo(){
        	return currWndNo;
        }
        function getNowFormatDate() {//获取当前时间
        	var date = new Date();
        	var seperator1 = "-";
        	var seperator2 = ":";
        	var month = date.getMonth() + 1<10? "0"+(date.getMonth() + 1):date.getMonth() + 1;
        	var strDate = date.getDate()<10? "0" + date.getDate():date.getDate();
			var strHours = date.getHours()<10? "0" + date.getHours():date.getHours();
			var strMinutes = date.getMinutes()<10? "0" + date.getMinutes():date.getMinutes();
			var strSeconds = date.getSeconds()<10? "0" + date.getSeconds():date.getSeconds();
        	var currentdate = date.getFullYear() + seperator1  + month  + seperator1  + strDate
        			+ "-"  + strHours    + strMinutes + strSeconds;
        	return currentdate;
        }
    </script>
</head>
<body>
<input type="hidden" id="tempdir"  name="tempdir" value="${tempdir!}" />
<div id="videoBox" onunload="closeVideo()">
    <object id="DPSDK_OCX" classid="CLSID:D3E383B6-765D-448D-9476-DFD8B499926D" style="width: 100%;height: 100%;"></object>
</div>
</body>
</html>