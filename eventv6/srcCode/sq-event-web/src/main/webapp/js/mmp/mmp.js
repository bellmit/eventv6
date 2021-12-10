/*
 * *****************************************************************
 * 语音视频
 * 
********************************************************************/
    var maxIndex=4;//默认最大窗口数
    var wndindex=0;//播放窗口号
	var MMPLogicOcx = null;
	var MMPPreviewWndOcx=null;
	
	var userName=null;//当前登录用户
	var talk=null;//被呼叫的用户
	
	var ocxId=null;
	var previewWndOcxId=null;
	
	var is_talk=false;//终端发起的在通话
	
	function initOcx(userName,objId,previewId,max){    
		 maxIndex=max;
		 userName=userName;
		 ocxId=objId;
		 previewWndOcxId=previewId;
		 if(MMPLogicOcx==null){
			 MMPLogicOcx=document.getElementById(ocxId);
		 }
		 if(MMPPreviewWndOcx==null){
			 MMPPreviewWndOcx=document.getElementById(previewId);
		 }
	}

	/**
	 * 登录
	 * @returns
	 */
  function LoginMMP(svrip,mediaurl,userName) {
	 
	   if(MMPLogicOcx==null){
		   MMPLogicOcx=document.getElementById(ocxId);
       } 
	   var loginxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	   loginxml += "<Login userid=\"";
	   loginxml += userName;
	   loginxml += "\" serveruri=\"";
	   loginxml += svrip;
	   loginxml += "\" mediauri=\"";
	   loginxml += mediaurl;
	   loginxml += "\" https=\"1\" /> ";
	   return MMPLogicOcx.LoginMMP(loginxml);
   }
   /**
    * 退出
    */
   function LogoutMMP() {
       if(MMPLogicOcx==null){
    	   MMPLogicOcx=document.getElementById(ocxId);
       }
       MMPLogicOcx.LogoutMMP();
   }
   
 //发起视频通话
   function StartVideoTalk(talkid) {
	   if(MMPLogicOcx==null){
		   MMPLogicOcx=document.getElementById(ocxId);
       }
	   //if(is_talk){
		//	alert("接入新的视频通话失败，请退出当前通话！");
	    //  return false;
		//}
	   talk=talkid;
       var ret = MMPLogicOcx.CreateSession(3);//语音+视频
       MMPLogicOcx.InviteSession(talkid, "videotalk");

   }
  //接受视频通话
   function AcceptVideoTalk(callid) {
	   
	   showMmpSelector(); 
	   if(MMPLogicOcx==null){
		   MMPLogicOcx=document.getElementById(ocxId);
       }
	   //先销毁现有的所有会话
	   StopVideoTalk();
	   wndindex=0;
	   
	   MMPLogicOcx.ConfirmSession(callid,1);

	   
	   var url=GetVideoPlayUrl(callid);
	   StartPreview(url,"");
	   
	   is_talk=true;
	   
	   $(".messager-body").window('close');
   }
   
  function changeIndex(){
	  if(wndindex==3)
		   wndindex=0;
	   else
		   wndindex+=1;  
  }
   
 //拒绝视频通话请求
   function RefuseVideoTalk(callid) {
	   if(MMPLogicOcx==null){
		   MMPLogicOcx=document.getElementById(ocxId);
       }
	   
	   MMPLogicOcx.ConfirmSession(callid, 0);
	   StopVideoTalk();
	   is_talk=false;
	   $(".messager-body").window('close');
   }
   
   //结束视频通话
   function StopVideoTalk() {
	   is_talk=false;
	   if(MMPLogicOcx==null){
		   MMPLogicOcx=document.getElementById(ocxId);
       }
	   MMPLogicOcx.DestroySession();
   }
	//获取终端播放地址
   function GetVideoPlayUrl(talkid) {
	   if(MMPLogicOcx==null){
		   MMPLogicOcx=document.getElementById(ocxId);
       }
       var url = MMPLogicOcx.GetSessionPlayUrl(talkid);
       
      return url;
   }
   
  
 //预览视频
   function StartPreview(playurl,overtext) 
	{   
	   if(MMPPreviewWndOcx==null){
			MMPPreviewWndOcx = document.getElementById(previewWndOcxId);
	    }
		MMPPreviewWndOcx.StartPreview(wndindex, playurl, overtext);
		
	}
   
	//停止视频预览
   function StopPreview() 
   {
	   if(MMPPreviewWndOcx==null){
			MMPPreviewWndOcx = document.getElementById(previewWndOcxId);
	    }
       // -1 则关闭所有画面
	   MMPPreviewWndOcx.StopPreview(-1);
   }
 //设置窗口布局  (有效值：2,3,5)，2:1x1,3:2x2,5:3x3
   function SetLayerMode(index) {
	   if(MMPPreviewWndOcx==null){
			MMPPreviewWndOcx = document.getElementById(previewWndOcxId);
	    }
       MMPPreviewWndOcx.SetWndLayer(index);
   }
   
   //消息事件
   function MMPNotifyMsg(msgtype, msginfo) {
		var a = $.parseXML(msginfo);
				switch(msgtype){
				case 1://用户上下线		
					var userid =  $(a).find('OnLine').attr("userid");
					var status = $(a).find('OnLine').attr("status");
					if(status==1){
						if(userid==userName){
						}	
					}else{
						//alert("登录失败："+status);
					}	 
					break;
				case 2://群组变更
					var groupid = $(a).find('Group').attr("groupid");
					break;
				case 3://会话邀请
					var fromid=$(a).find('Invite').attr("fromid");
					var from=fromid.split("@")[0];
					var callid=$(a).find('Invite').attr("callid");
					var dest = $(a).find('Invite').attr("desc");//videotalk
					if(dest=='videotalk'){
						if(is_talk){
							alert("接入新的视频通话失败，请退出当前通话！");
						}else{
							bottomRightMsg('视频通话请求',from+'邀请您视频通话，是否接受？',callid);    	
						}
					}else if(dest=='audiotalk'){
						bottomRightMsg('语音通话请求',from+'　邀请您语音通话，是否接受？',callid); 
					}
					
					break;
				case 4://终端接受或加入会话
					
					var fromid = $(a).find('InviteRsp').attr("fromid");
					var accept = $(a).find('InviteRsp').attr("accept");
					if(accept==0){
						if(talk==fromid){
							var url=GetVideoPlayUrl(fromid);
							StartPreview(url,fromid);
							is_talk=true;
							talk=null;
							changeIndex();
						}
					}else{
						alert(fromid.split("@")[0]+"对话拒绝视频会话！");
						
					}
					//2001 拒绝
		
					break;
				case 5://离开会话
					var leaveUser = $(a).find('Leave').attr("fromid");
					if(leaveUser==userName && is_talk==true){//is_talk 为true时
						wndindex=0;
						StopPreview(); 
						StopVideoTalk(); 
						is_talk=false;
					}
					break;
				case 6://申请讲话
					var status = $(a).find('Grouptalk').attr("status");
					break;
				default:
					alert('调用错误');
				}
   }  
   
   //右下角弹出窗口
   function showRightFade(message){
		$.messager.show({
			title:'视频语音提示',
			msg:message,
			timeout:5000,
			showType:'slide'
		});
	}	
 //右下角弹出视频确认窗口
   function bottomRightMsg(title,msg,callid){
	   var  content='<div>'+msg+'</div><div style="clear:both;"></div><div class="messager-button"><a href="javascript:void(0)" class="l-btn l-btn-small" group="" id="" style="margin-left: 10px;"><span class="l-btn-left"><span class="l-btn-text" onclick="AcceptVideoTalk('+callid+')" >接受</span></span></a><a href="javascript:void(0)" class="l-btn l-btn-small" group="" id="" style="margin-left: 10px;"><span class="l-btn-left"><span class="l-btn-text" onclick="RefuseVideoTalk('+callid+')">拒绝</span></span></a></div>';
		$.messager.show({
			title:title,
			msg:content,
			showType:'show',
			timeout:10000,
			height:'100px'
		});
	}
	function checkActiveX(){
		if(document.all.MMPLogicOcx.object == null){
			return false;
		}else {
			return true;
		}
	}