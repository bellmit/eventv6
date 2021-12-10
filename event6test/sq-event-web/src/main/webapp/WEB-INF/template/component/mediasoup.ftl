		
<!-- 加载AnyChat for Web SDK库  -->
<script src="${uiDomain!''}/js/anychat/javascript/anychatsdk.js" ></script>  
<script src="${uiDomain!''}/js/anychat/javascript/anychatevent.js" </script>  
<script src="${uiDomain!''}/js/anychat/javascript/advanceset.js" ></script> 
<script src="${uiDomain!''}/js/anychat/javascript/logicfunc.js"></script>

<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/yancheng/css/reset.css"/>
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/yancheng/css/yc-video-voice.css"/>
<style>
.public-content {
    z-index: 9999; 
}
.volume-adjust.active{
	display: block;
}
.volume:hover>.volume-adjust  {
    display: none;
}
.video-name{
	height: 42px;
}
.yc-vs-text1{
line-height: 25px;
}
.yc-vs-text2{
line-height: 25px;
}

</style>  

     		 <!-- 提示框 -->
            <div class="tooltip bs"  style="z-index: 9999">
                <div class="tooltip-top">
                    <p>提示</p>
                </div>
                <p id="tipMsg">确定关闭视频通话？</p> 
                <div class="tooltip-btn clearfix" style="z-index: 101">
                    <a href="javascript:void(0);" class="tb-determine fl">确&nbsp;&nbsp;&nbsp;&nbsp;定</a>
                    <a href="javascript:void(0);" class="tb-cancel fr bs">取&nbsp;&nbsp;&nbsp;&nbsp;消</a>
                </div>
            </div>   
            
            <!-- 最小化 -->
			<div class="minimize video-minimize" style="z-index: 1000;margin-right: 15%;top:32px">
                <i></i>
                <p id="min_time">00:00:00</p>
                <p>通话中</p>
                <a href="javascript:void(0);" class="voice-m-close"></a>  
            </div>
<div id="anychatDiv">
            <!-- 单人视频 -->
            <div class="public-content yc-video-single">
                <div class="public-c-top clearfix">
                    <p>视频呼叫</p>
                    <div class="fr clearfix public-ct-right">
         
                    </div> 
                </div>
                  <!-- 开始呼叫 -->
				<div class="yc-video-content yc-video-content1 bs">
                    <div class="yc-vs-avatar bs">
                        <!-- 默认男头像 -->
                        <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                        <!-- 默认女头像 -->
                        <!-- <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/women.png"> -->
                    </div>
                    <p class="yc-vs-text1" style="line-height: 25px">是否呼叫<br><span id="partyName_sp1"></span></p>
                    <div class="yc-vs-btn clearfix">
                        <a href="javascript:void(0);" class="yc-vsb-determine fl">确&nbsp;&nbsp;&nbsp;&nbsp;定</a>
                        <a href="javascript:void(0);" class="yc-vsb-cancel fr bs">取&nbsp;&nbsp;&nbsp;&nbsp;消</a>
                    </div>
                    <div class="yc-vs-btn clearfix">
                        <a href="javascript:void(0);" class="yc-vsb-determine fl">确&nbsp;&nbsp;&nbsp;&nbsp;定</a>
                        <a href="javascript:void(0);" class="yc-vsb-cancel fr bs">取&nbsp;&nbsp;&nbsp;&nbsp;消</a>
                    </div>
                </div>
                <!-- 呼叫中 -->
                <div class="yc-video-content yc-video-content2">
               
                    <div class="yc-video-box">
                        <div class="yc-vb-avatar">
                            <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                        </div>
                        <p id="partyName_sp2"></p>
                        <!-- 放小的视频窗口 -->
                        <div class="yc-bc-small">
                            <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                        </div>
                    </div>
                    <div class="video-features">
                        <ul class="vf-list clearfix">
                            <li>
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/volume-ct.png">
                            </li>
                            <li>
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/mike-ct.png">
                            </li>
                            <li>
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/recording-ct.png">
                            </li>
                            <li>
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/camera-ct.png">
                            </li>
                            <li>
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/camera-luz.png">
                            </li>
                            <li>
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/conversion-ct.png">
                            </li>
                            <li>
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/comment-ct.png">
                            </li>
                        </ul>
                    </div>
                    <div class="video-hang-up clearfix">
                        <p>视频连接中</p>
                        <a href="javascript:void(0);" class="vhu-btn fl ml20"></a>
                    </div>
                </div>
            </div>
             <!-- 单人视频，视频中 -->
            <div class="public-content yc-video-single1">
            <div  id="LOG_DIV_CONTENT" style="height:20px;display: none;"></div>
                <div class="flex" id="singleFlex" style="min-width: 740px;">
                    <div class="yc-vs1-left flex1">
                        <div class="public-c-top clearfix">
                            <p>视频呼叫</p>
                            <div class="fr clearfix public-ct-right">
                                <a href="javascript:void(0);" class="fl pctr-minimize"></a>
                                 <div class="fl yc-pctr-fn">
                                    <a href="javascript:void(0);" class="pctr-full-screen" style="display: block;"></a>
                                    <a href="javascript:void(0);" class="pctr-narrow"></a>
                                </div>
                                <a href="javascript:void(0);" class="fl pctr-close"></a>
                            </div> 
                        </div>
                       	
                        <!-- 大的视频框                                       -->
                        <div class="yc-vs1l-box">
                  			  <iframe id="vedioIframe" frameborder=0 src=""  allow="microphone;camera;midi;encrypted-media;" scrolling=no style=" z-index: 100; width: 100%; height: 100%; "></iframe>
                        
                    	</div>
     
                    </div>
         
                </div>
            </div>
         <!-- 单人语音 -->
			<div class="public-content yc-voice-single">
			  
				<div class="public-c-top clearfix">
					<p>语音呼叫</p>
					<div class="fr clearfix public-ct-right">
						<a href="javascript:void(0);" class="fl pctr-minimize"></a>
						 <div class="fl yc-pctr-fn">
                                    <a href="javascript:void(0);" class="pctr-full-screen" style="display: block;"></a>
                                    <a href="javascript:void(0);" class="pctr-narrow"></a>
                          </div>
						<a href="javascript:void(0);" class="fl pctr-close"></a>
					</div>
				</div>
				<!-- 是否呼叫网格员 -->
				<div class="yc-vs-content yc-vs-content1 bs" style="display: block;">
					<div class="yc-vs-avatar bs">
						<!-- 默认男头像 -->
						<img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
						<!-- 默认女头像 -->
						<!-- <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/women.png"> -->
					</div>
					<p class="yc-vs-text1">是否呼叫<br><span id="partyName_yp1"></span></p>
					<div class="yc-vs-btn clearfix">
						<a href="javascript:void(0);" class="yc-vsb-determine fl">确&nbsp;&nbsp;&nbsp;&nbsp;定</a>
						<a href="javascript:void(0);" class="yc-vsb-cancel fr bs">取&nbsp;&nbsp;&nbsp;&nbsp;消</a>
					</div>
				</div>
				<!-- 连接中 -->
				<div class="yc-vs-content yc-vs-content2 bs">
					<div class="yc-vs-avatar bs">
						<!-- 默认男头像 -->
						<img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
						<!-- 默认女头像 -->
						<!-- <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/women.png"> -->
					</div>
					<p class="yc-vs-text1" id="partyName_yp2"></p>
					<p class="yc-vs-text2">语音接通中</p>
					<div class="yc-vsc2-btn">
						<a href="javascript:void(0);" class="yc-vsc-hang-up"></a>
					</div>
				</div>
				<!-- 接通 -->
				<div class="yc-vs-content yc-vs-content3 bs" >
					<div class="yc-vs-avatar bs">
						<!-- 默认男头像 -->
						<img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
						<!-- 默认女头像 -->
						<!-- <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/women.png"> -->
					</div>
					<p class="yc-vs-text1"><sapn id="partyName_yp3"></sapn></p>   
					<p class="yc-vs-text2" id="mytime_yp">00:00:00</p>    
					<div class="yc-features-btn clearfix">
						<div class="fl volume-box">
							<a href="javascript:void(0);" class="volume">
								<div class="volume-adjust">
									<div class="va-bar">
										<div class="vab-content"></div>
										<i class="va-btn"></i>
									</div>
								</div>
							</a>
					      <script>
                                  	$('.volume').mouseenter(function(){
                                  		$('.volume-adjust').show();
                                  	});
                                  	$('.volume').mouseleave(function(){
                                  		$('.volume-adjust').hide();
                                  	});
                          </script>
						</div>
						<a href="javascript:void(0);" class="mike fl ml25"></a>
						<div class="recording-box fr">
							<a href="javascript:void(0);" class="recording"></a>
							<p>录音中</p>
						</div>
					</div>
					<div class="yc-vsc2-btn">
						<a href="javascript:void(0);" class="yc-vsc-hang-up"></a>
					</div>
				</div>
			</div>
            
            

        <div id="anychatDiv" class="ycv-container bs" style="z-index: 9999;display: none">
            <!-- 多人视频 -->
            <div class="public-content yc-video-multiplayer clearfix">
                <div class="public-c-top clearfix">
                    <p>多人视频</p>
                    <div class="fr clearfix public-ct-right">
                        <a href="javascript:void(0);" class="fl pctr-minimize"></a>
                        <a href="javascript:void(0);" class="fl pctr-full-screen"></a>
                        <a href="javascript:void(0);" class="fl pctr-close"></a>
                    </div> 
                </div>
                <!-- 选人中 -->
                <div class="yc-vm-content yc-vm-content1 bs">
                    <p class="yc-vnc1-text">请从右侧的人员列表中添加</p>
                    <div class="yc-vs-btn clearfix">
                        <a href="javascript:void(0);" class="yc-vsb-determine fl">确&nbsp;&nbsp;&nbsp;&nbsp;定</a>
                        <a href="javascript:void(0);" class="yc-vsb-cancel fr bs">取&nbsp;&nbsp;&nbsp;&nbsp;消</a>
                    </div>
                </div>
                <!-- 选好人员 -->
                <div class="yc-vm-content yc-vm-content2 bs" style="display: block;">
                    <div class="yc-vmc2-content clearfix">
                        <!-- 有active代表自己 -->
                        <div class="yc-vmc2c-item active">
                            <div class="yc-vmc2-avatar">
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                            </div>
                            <p>李铁军</p>
                        </div>
                        <div class="yc-vmc2c-item">
                            <a href="javascript:void(0);" class="yc-vmc2-del"></a>
                            <div class="yc-vmc2-avatar">
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/women.png">
                            </div>
                            <p>张晓萌</p>
                        </div>
                        <div class="yc-vmc2c-item">
                            <a href="javascript:void(0);" class="yc-vmc2-del"></a>
                            <div class="yc-vmc2-avatar">
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                            </div>
                            <p>程文华 </p>
                        </div>
                        <div class="yc-vmc2c-item">
                            <a href="javascript:void(0);" class="yc-vmc2-del"></a>
                            <div class="yc-vmc2-avatar">
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                            </div>
                            <p>金培文</p>
                        </div>
                        <div class="yc-vmc2c-item">
                            <a href="javascript:void(0);" class="yc-vmc2-del"></a>
                            <div class="yc-vmc2-avatar">
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                            </div>
                            <p>陈向坤</p>
                        </div>
                        <div class="yc-vmc2c-item">
                            <a href="javascript:void(0);" class="yc-vmc2-del"></a>
                            <div class="yc-vmc2-avatar">
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                            </div>
                            <p>林峰</p>
                        </div>
                        <div class="yc-vmc2c-item">
                            <a href="javascript:void(0);" class="yc-vmc2-del"></a>
                            <div class="yc-vmc2-avatar">
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                            </div>
                            <p>李文浩</p>
                        </div>
                        <div class="yc-vmc2c-item">
                            <a href="javascript:void(0);" class="yc-vmc2-del"></a>
                            <div class="yc-vmc2-avatar">
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                            </div>
                            <p>林晓峰</p>
                        </div>
                        <div class="yc-vmc2c-item">
                            <a href="javascript:void(0);" class="yc-vmc2-del"></a>
                            <div class="yc-vmc2-avatar">
                                <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                            </div>
                            <p>张立伟</p>
                        </div>
                    </div>
                    <div class="yc-vs-btn clearfix">
                        <a href="javascript:void(0);" class="yc-vsb-determine fl">确&nbsp;&nbsp;&nbsp;&nbsp;定</a>
                        <a href="javascript:void(0);" class="yc-vsb-cancel fr bs">取&nbsp;&nbsp;&nbsp;&nbsp;消</a>
                    </div>
                </div>
            </div>
            <!-- 多人视频，视频中 -->
            <div class="public-content yc-video-multiplayer1" >
                <div class="flex">
                    <div class="fl yc-ms1-left">
                        <div class="public-c-top clearfix">
                            <p>视频呼叫</p>
                            <div class="fr clearfix public-ct-right">
                                <a href="javascript:void(0);" class="fl pctr-minimize"></a>
                                <a href="javascript:void(0);" class="fl pctr-full-screen"></a>
                                <a href="javascript:void(0);" class="fl pctr-close"></a>
                            </div> 
                        </div>
                        <!-- 多人的视频框 -->
                        <div class="yc-ms1l-box">
                            <ul class="yc-ms1lb-list clearfix">
                                <li>
                                    <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/icon-pic2.png">
                                    <div class="yc-multiplayer-name">
                                        <p>李铁军</p>
                                    </div>
                                </li>
                                <li>
                                    <div class="yc-video-avatar">
                                        <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                                    </div>
                                    <p>张晓萌</p>
                                </li>
                                <li>
                                    <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/icon-pic2.png">
                                    <div class="yc-multiplayer-name">
                                        <p>程文华</p>
                                    </div>
                                </li>
                                <li>
                                    <div class="yc-video-avatar">
                                        <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                                    </div>
                                    <p>张晓萌</p>
                                </li>
                                <li>
                                    <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/icon-pic2.png">
                                    <div class="yc-multiplayer-name">
                                        <p>程文华</p>
                                    </div>
                                </li>
                                <li>
                                    <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/icon-pic2.png">
                                    <div class="yc-multiplayer-name">
                                        <p>程文华</p>
                                    </div>
                                </li>
                                <li>
                                    <div class="yc-video-avatar">
                                        <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                                    </div>
                                    <p>张晓萌</p>
                                </li>
                                <li>
                                    <div class="yc-video-avatar">
                                        <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/men.png">
                                    </div>
                                    <p>张晓萌</p>
                                </li>
                                <li>
                                    <img src="${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/icon-pic2.png">
                                    <div class="yc-multiplayer-name">
                                        <p>程文华</p>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- 功能按钮 -->
                        <div class="yc-features-btn clearfix">
                            <div class="fl volume-box">
                                <a href="javascript:void(0);" class="volume">
                                    <div class="volume-adjust">
                                        <div class="va-bar">
                                            <div class="vab-content"></div>
                                            <i class="va-btn"></i>
                                        </div>
                                         <iframe frameborder=0 scrolling=no style="background-color:transparent; position: absolute; z-index: 100; width: 100%; height: 100%; top: 0;left:0;"></iframe>
                                    </div>
                                </a>
                            </div>
                            <a href="javascript:void(0);" class="mike fl ml16"></a>
                            <div class="recording-box fl ml16">
                                <a href="javascript:void(0);" class="recording"></a>
                                <p>录音中</p>
                            </div>
                            <a href="javascript:void(0);" class="camera fl ml16"></a>
                            <a href="javascript:void(0);" class="conversion fl ml16"></a>
                            <a href="javascript:void(0);" class="comment fl ml16"></a>
                        </div>
                        <!-- 挂断按钮 -->
                        <div class="video-hang-up clearfix">
                            <p class="mytime">00:00:00 </p>
                            <a href="javascript:void(0);" class="vhu-btn fl ml20"></a>
                        </div>
                    </div>
                    <div class="fl yc-vs1-right bs">
                        <div class="public-c-top">
                            <p>评论列表</p>
                            <a href="javascript:void(0);" class="yc-vs1r-close"></a>
                        </div>
                        <div class="yc-vs1r-content bs">
                            <!-- 自己发的添加 active -->
                            <div class="video-chat-item active">
                                <p class="vci-name-time">李铁军 2019/11/25 17:25:36</p>
                                <p class="vci-text">先解决事件</p>
                            </div>
                            <div class="video-chat-item">
                                <p class="vci-name-time">张立伟 2019/11/25 17:26:43</p>
                                <p class="vci-text">好的，那应该如何处理更合适呢？</p>
                            </div>
                            <div class="video-chat-item active">
                                <p class="vci-name-time">李铁军 2019/11/25 17:30:36</p>
                                <p class="vci-text">解决方案需要多思考</p>
                            </div>
                        </div>
                        <div class="yc-vs1r-bottom bs">
                            <div class="video-input-box bs">
                                <input type="text" class="bs" placeholder="请输入内容">
                                <a href="javascript:void(0);" class="vib-send">发&nbsp;&nbsp;送</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
      
        </div>
       </div>
 </div>      
       
	<script src="${uiDomain!''}/web-assets/common/js/basic/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script> 
       <script>      
       var password="123",partyName="${partyName}",userId='${userId}',uiDomain='${uiDomain}',
        serverAddr="${anyChatServerUrl!'192.168.52.44'}",serverPort=8906;  

		//提示
		function showTip(msg,roomid,oper,userId,partyName){//type <4:请求  4：接受
			$(".tooltip").show();
			$("#tipMsg").html(msg);
			$('.tb-determine').click(function(e) {
				if(oper<4){
					showAnychatSelector();
				}else if(oper==4){//接受
					$(".tooltip").hide();
				 	openSupportMedia(roomid,userId,partyName);//视频弹窗
				}
				    
			}) ;
			$('.tb-cancel').click(function(e) {
				$(".tooltip").hide();
			}) ;
			
			//关闭iframe弹窗
			$(".panel-tool-close").click(function(e) {
				$("#MaxJqueryWindowForCross iframe").attr("src","") ;
			}) ;
		}
	    //medioSoup视频页面弹窗
		function openSupportMedia(roomid,userId,partyName){
			var url = 'https://192.168.52.156:3000/?roomId='+roomid+'&userId=${userId}&displayName='+partyName;
			if(getIsSupportMedia()){
				window.parent.closeMaxJqueryWindow();//关闭前一次打开的窗口
				window.parent.showMaxJqueryWindowForCross("视频",url,550,300); 
			}else{
				 window.open(url,'maxwindow','toolbar=no,location=no,directories=no,menubar=no,scrollbars=yes,resizable=no,status=no,width=800,height=500');
			}
		
			
		}
		function closeSupportMedia(){
			if(getIsSupportMedia()){
				window.parent.closeMaxJqueryWindow();//关闭前一次打开的窗口
			}
			
		}
		//获取是否支持视频视频
	   function getIsSupportMedia(){
		   var isSupportMedia=true;
		 	 try
		 	 {
		 		typeof navigator.mediaDevices.getUserMedia=='function';
		 	 }
		 	 catch(err)
		 	 {
		 		isSupportMedia=false;//不支持
		    }
		 	 return isSupportMedia;
	   }
		
		function showAnychatSelector(){
			
			$(".public-content").hide();
			if(oper_==1){
				$(".yc-video-single1").show();
			}else if(oper_==2){
				$(".yc-voice-single").show();
				$(".yc-vs-content").hide();
				$(".yc-vs-content3").show();
			}
			resetTime();
		    startTime();
			  
		}
 
		
		
 
		
		</script>
        <script>
       
			//关闭或者挂断
			$('.pctr-close,.vhu-btn,.yc-vsc-hang-up').click(function(e) {
				//BRAC_SendTextMessage(mTargetUserId, 1, "ffcstest@ffcstest@ffcstestExit"); //调用发送信息函数
				//endTime();
				$(".public-content").hide();
				$("#ReceiveMsgDiv").html('');
				$(".recording-box a").removeClass('active');
			});
	 
			
			// 最小化
			$('.pctr-minimize').click(function(){
				$('.video-minimize').show();
				$("#anychatDiv").hide();
			})
			// 回复原状
			$('.video-minimize').click(function(){
				$(this).hide();
				$("#anychatDiv").show();
			});
		     // 最大化
            $('.pctr-full-screen').click(function(){
                $(this).parents('.public-content').addClass('wh-full');
                $(this).hide().siblings('.pctr-narrow').css('display','block');
                var boxHeight = $(this).parents('.public-content').find('.yc-vs1l-box').height();
                $(this).parents('.public-content').find('.yc-vs1l-box').width(boxHeight/0.58);
            })
		     // 最大化
            $('.pctr-full-screen').click(function(){
                $(this).parents('.public-content').addClass('wh-full');
                $(this).hide().siblings('.pctr-narrow').css('display','block');
                var boxHeight = $(this).parents('.public-content').find('.yc-vs1l-box').height();
                $(this).parents('.public-content').find('.yc-vs1l-box').width(boxHeight/0.58);
               
                
            })
            // 最大化之后回复原状
            $('.pctr-narrow').click(function(){
                $(this).parents('.public-content').removeClass('wh-full');
                $(this).hide().siblings('.pctr-full-screen').css('display','block');
                $(this).parents('.public-content').find('.yc-vs1l-box').width('100%');
                $(this).hide().siblings('.pctr-full-screen').css('display','block');
            })
			
			
        // 滚动条优化
       /*      $(".yc-vs1r-content").niceScroll({
                cursorcolor:"#343e4c",
                cursorwidth: "5px",
                cursorborder: 0,
                cursorborderradius: "5px",
                autohidemode: 'false',
            });   */ 
            // 聊天框关闭
            $('.yc-vs1r-close').click(function(){  
      
              $(this).parent().parent().hide();
              $(this).parent().parent().find(".yc-vs1r-content").getNiceScroll().resize();
              $(this).parent().parent().siblings('div').find('.comment').removeClass('active');
            });
            
			$('.yc-vsb-cancel').click(function(e) {
				$(".public-content").hide();  
			}) ;
	
        </script>
        
        <script type="text/javascript">
  
    (function (window) {
        let socket, session = {}, ID_SEQ = 1;
        let config = {listener: null, log: console};

        let listener = {
            onOpened: function (event) {
                if (config.listener != null) {
                    config.listener.onOpened(event);
                }
                handshake();
            },
            onClosed: function (event) {
                if (config.listener != null) {
                    config.listener.onClosed(event);
                }
                session = {};
                ID_SEQ = 1;
                socket = null;
            },
            onHandshake: function () {
                session.handshakeOk = true;
                if (config.listener != null) {
                    config.listener.onHandshake();
                }
                if (config.userId) {
                    bindUser(config.userId, config.tags);
                }
            },
            onBindUser: function (success) {
                if (config.listener != null) {
                    config.listener.onBindUser(success);
                }
            },
            onReceivePush: function (message, messageId) {
                if (config.listener != null) {
                    config.listener.onReceivePush(message, messageId);
                }
            },
            onKickUser: function (userId, deviceId) {
                if (config.listener != null) {
                    config.listener.onKickUser(userId, deviceId);
                }
                doClose(-1, "kick user");
            }
        };

        const Command = {
            HANDSHAKE: 2,
            BIND: 5,
            UNBIND: 6,
            ERROR: 10,
            OK: 11,
            KICK: 13,
            PUSH: 15,
            ACK: 23,
            UNKNOWN: -1
        };

        function Packet(cmd, body, sessionId) {
            return {
                cmd: cmd,
                flags: 16,
                sessionId: sessionId || ID_SEQ++,
                body: body
            }
        }

        function handshake() {
            send(Packet(Command.HANDSHAKE, {
                    deviceId: config.deviceId,
                    osName: config.osName,
                    osVersion: config.osVersion,
                    clientVersion: config.clientVersion
                })
            );
        }

        function bindUser(userId, tags) {
            if (userId && userId != session.userId) {
                session.userId = userId;
                session.tags = tags;
                send(Packet(Command.BIND, {userId: userId, tags: tags}));
            }
        }
		 function unBindUser(userId, tags) {
            if (userId && userId != session.userId) {
                session.userId = userId;
                session.tags = tags;
                send(Packet(Command.UNBIND, {userId: userId, tags: tags}));
            }
        }

        function ack(sessionId) {
            send(Packet(Command.ACK, null, sessionId));
        }

        function send(message) {
            if (!socket) {
                return;
            }
            if (socket.readyState == WebSocket.OPEN) {
                socket.send(JSON.stringify(message));
            } else {
                config.log.error("The socket is not open.");
            }
        }

        function dispatch(packet) {
            switch (packet.cmd) {
                case Command.HANDSHAKE: {
                    console.log(">>> handshake ok.");
                    listener.onHandshake();
                    break;
                }
                case Command.OK: {
                    if (packet.body.cmd == Command.BIND) {
                        console.log(">>> bind user ok.");
                        listener.onBindUser(true);
                    }
                    break;
                }
                case Command.ERROR: {
                    if (packet.body.cmd == Command.BIND) {
                        console.log(">>> bind user failure.");
                        listener.onBindUser(false);
                    }
                    break;
                }

                case Command.KICK: {
                    if (session.userId == packet.body.userId && config.deviceId == packet.body.deviceId) {
                        console.log(">>> receive kick user.");
                        listener.onKickUser(packet.body.userId, packet.body.deviceId);
                    }
                    break;
                }

                case Command.PUSH: {
                    console.log(">>> receive push, content=" + packet.body.content);
                    let sessionId;
                    if ((packet.flags & 8) != 0) {
                        ack(packet.sessionId);
                    } else {
                        sessionId = packet.sessionId
                    }
                    listener.onReceivePush(packet.body.content, sessionId);
                    break;
                }
            }
        }
        var mywindow;
        //收到消息事件
        function onReceive(event) {
        	var jsonObj = eval('(' + event.data + ')');
        	if(typeof(jsonObj.body)!="undefined" && typeof(jsonObj.body.content)!="undefined"){
            var content=eval('(' + jsonObj.body.content + ')')
             content=eval('(' + content.content + ')')
        	 console.log(">>> roomId==" + content.roomId);
        	 console.log(">>> fromId==" + content.fromId); 
        	 console.log(">>> fromName==" + content.fromName);
        	 console.log(">>> action==" + content.action);
        	 if(content.action=='1'){
        		 showTip("是否接受"+content.fromName+"视频邀请？",content.roomId,4,content.fromId,content.fromName);
        	 }else if(content.action=='6'){
        		// mywindow.close();  
        	 }else{
        		 
        	 }
       	    }
        console.log(">>> receive packet=" + event.data);
            dispatch(JSON.parse(event.data))
        }

        function onOpen(event) {
        	 console.log("Web Socket opened!");
            listener.onOpened(event);
        }

        function onClose(event) {
        	 console.log("Web Socket closed!");
            listener.onClosed(event);
        }

        function onError(event) {
        	console.log("Web Socket receive, error");
            doClose();
        }

        function doClose(code, reason) {
            if (socket) socket.close();
            console.log("try close web socket client, reason=" + reason);
        }

        function doConnect(cfg) {
            config = copy(cfg);
            socket = new WebSocket(config.url);
            socket.onmessage = onReceive;
            socket.onopen = onOpen;
            socket.onclose = onClose;
            socket.onerror = onError;
            console.log("try connect to web socket server, url=" + config.url);
        }

        function copy(cfg) {
            for (let p in cfg) {
                if (cfg.hasOwnProperty(p)) {
                    config[p] = cfg[p];
                }
            }
            return config;
        }

	   
        window.mpush = {
            connect: doConnect,
            close: doClose,
            bindUser: bindUser
			
        }
    })(window);

 

    let log = {
        log: function () {
		//$("#responseText").val( $("#responseText").val()+Array.prototype.join.call(arguments, "") + "\r\n");
  
        }
    };
    log.debug = log.info = log.warn = log.error = log.log;

    function connect() {
    	var url="ws://192.168.52.156:4000";
         mpush.connect({
            url: url,
            userId: '${userInfo.userId}',
            deviceId: "test-1001",  
            osName: "web " + navigator.userAgent,
            osVersion: "55.2",
            clientVersion: "1.0",
            log: log
        }); 
    }

    function bind() {
        mpush.bindUser($("#userId").val())
    }

function sendToUser(userId,userName,action){
	
	
	  $.ajax({  
        type : "post",  
        async:false,  
         url : "http://components.beta.aishequ.org/common/mpush/pushMsg.jhtml?userId="+userId+"&roomId="+userId+"&action="+action+"&fromId=${userInfo.userId}",    
		data:{
			'jsonpcallback':'setData',
			'fromName':userName, 
			
		},
        dataType : "jsonp",//数据类型为jsonp  
        success : function(data){  
            //$("#showcontent").text("Result:"+data.result)  
        },  
        error:function(){  
           // alert('fail');  
        }  
    });
	  

	if(action &&action ==1){//发起请求
		openSupportMedia(userId,userId,userName);
	}


}
   function setData(data){
 
    
   }

$(function(){
	connect();
});
</script>
