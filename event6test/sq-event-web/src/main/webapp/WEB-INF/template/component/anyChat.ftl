		
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
            <div class="tooltip bs"  style="z-index: 1000">
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
                        <a href="javascript:void(0);" class="fl pctr-minimize"></a>
                        <div class="fl yc-pctr-fn">
                                    <a href="javascript:void(0);" class="pctr-full-screen" style="display: block;"></a>
                                    <a href="javascript:void(0);" class="pctr-narrow"></a>
                          </div>
                        <a href="javascript:void(0);" class="fl pctr-close"></a>
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
                           	 <div class="video-name"  >
                                <p id="sname">当前没有人</p>
                                <iframe class="xxx" frameborder=0 scrolling=no style=" position: absolute; z-index: 100; width: 100%; height: 100%; top: 0;left:0;"></iframe>
                            </div>
                                <!--远程视频-->
                                <div style="height: 100%;width: auto;min-height: 426px;" id="AnyChatRemoteVideoDiv"></div>
	                        <!--     <div style="width:740px; height:4px; text-align:left;">
	                      	  		 <div id="RemoteAudioVolume" style="background: green;"></div>
	                            </div> -->
                            <!-- 小的视频框 -->
                            <div   class="yc-vs1l-box1" style="width:142px;height:142px;">
                        	     <!--本地视频-->
                           		<div id="AnyChatLocalVideoDiv" style="height: 100%;width: auto;"> </div>
                           		          	<!-- 	 	<div style="width:140px; height:4px; text-align:left;">
			                        <div id="LocalAudioVolume" style="background: green;"> <iframe class="xxx" frameborder=0 scrolling=no style=" position: absolute; z-index: 100; width: 100%; height: 4px; top: 0;left:0;"></iframe></div>
			                   		</div> -->
                            </div>
                        
                    	</div>
              
                        <!-- 功能按钮 -->
                        <div class="yc-features-btn clearfix" >
                            <div class="fl volume-box" >
                                <a href="javascript:void(0);" class="volume" style="z-index: 101;">
                                    <div class="volume-adjust" >
                                        <div class="va-bar" style="z-index: 101;">
                                            <div class="vab-content" style="height: 100"></div>
                                            <i class="va-btn"></i>
                                        </div>
                                        <iframe class="xxx" frameborder=0 scrolling=no style=" position: absolute; z-index: 100; width: 100%; height: 129px; top: 0;left:0;"></iframe>
                                    </div>
                                    <script>
                                    	$/* ('.volume').hover(function(){
                                    		$(this).parent().addClass('active');
                                    	},function(){
                                    		$(this).parent().removeClass('active');
                                    	}); */
                                    	$('.volume').mouseenter(function(){
                                    		$('.volume-adjust').show();
                                    	});
                                    	$('.volume').mouseleave(function(){
                                    		$('.volume-adjust').hide();
                                    	});
                                    
                                    	</script>
                                </a>       
                            </div>
                            <a href="javascript:void(0);" class="mike fl ml16"></a>
                            <div class="recording-box fl ml16">
                                <a href="javascript:void(0);" class="recording"></a>
                                <p>录音中</p>
                            </div>
                            <a href="javascript:void(0);" class="camera fl ml16"></a>
                            
                            <div class="recording-box fl ml16">
	                             <a href="javascript:void(0);" class="videotape"></a>
	                             <p>录频中</p>
                            </div>
                            <a href="javascript:void(0);" class="conversion fl ml16"></a>
                            <a href="javascript:void(0);" class="comment fl ml16"></a> 
                        </div>
                        <!-- 挂断按钮 -->
                        <div class="video-hang-up clearfix">
                            <p id="mytime_sp">00:00:00 </p>
                            <a href="javascript:void(0);" class="vhu-btn fl ml20"></a>
                        </div>
                    </div>
                    <div class="fl yc-vs1-right bs">
                        <div class="public-c-top">
                            <p>评论列表</p>
                            <a href="javascript:void(0);" class="yc-vs1r-close"></a>
                        </div>
                        <div id="ReceiveMsgDiv" class="yc-vs1r-content bs"  >
                            <!-- 自己发的添加 active -->
                        </div>
                        <div class="yc-vs1r-bottom bs">
                            <div class="video-input-box bs">
                                <input type="text" id="MessageInput" class="bs" placeholder="请输入内容">
                                <a href="javascript:void(0);" id="SendMsg"  class="vib-send">发&nbsp;&nbsp;送</a>
                            </div>
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
	  	 $(window).load(function() {//ffcsyxm123(30021186)
			 LogicInit(uiDomain,partyName,userId,password,serverAddr,serverPort);//初始化
		 });
       
		// BRAC_TransBufferEx(parseInt(kv),message,444,2,0);   
		// 收到透明通道（扩展）传输数据   
		//lParam: 0:退出房间  1：发起视频邀请    2：发起语音邀请
		//wParam：房间号
		function OnAnyChatTransBufferEx(dwUserId, lpBuf, dwLen, wParam, lParam, dwTaskId) {
			oper_=lParam;
			if(lParam==0 ||lParam==-10086){
				BRAC_LeaveRoom(-1);
				return;
			}
			var roomid=parseInt(wParam);
			resUserId =dwUserId;//发起
		    showTip("是否接受视频邀请？",roomid,4);
		}
		
		
		
		//邀请或者踢出
		function transBuffer_userId(userId,partyName,oper){
			var isOnRoom=false;
			var useridlist = BRAC_GetOnlineUser();
			for (var k=0; k<useridlist.length; k++) {
				if(useridlist[k] == userId)
				{
					isOnRoom=true;
				}
			}
			if(isOnRoom ||( mTargetUserId==userId)) return;
			if(mTargetUserId>0){ 
				alert('正在通话中');
				return;
			}
			$(".public-content").hide();
			if(oper==1){
				$("#anychatDiv").show();
				$(".yc-video-single").show();
				$(".yc-video-content1").show();
				$("#partyName_sp1").html(partyName);   
				$("#partyName_sp2").html(partyName);  
			}else if(oper==2){
				$("#anychatDiv").show();
				$(".yc-voice-single").show();
				$(".yc-vs-content1").show();
				$("#partyName_yp1").html(partyName);   
				$("#partyName_yp2").html(partyName);   
				$("#partyName_yp3").html(partyName);  
			}else if(oper==-10086){
				BRAC_TransBufferEx(userId,partyName,2,oper,0);  
			}
			if(partyName!=''){
				var msg="";
				if(oper==1){
					msg="是否邀请"+partyName+"加入视频通话？";
				}else if(oper==2){
					msg="是否邀请"+partyName+"加入语音通话？";
				}else if(oper==0){
					msg="是否踢出"+partyName+"的视频通话？";
				}
				oper_=oper;
				$(".yc-vsb-determine").unbind("click").click(function(e) {
					$("#sname").html("当前没有人");
						   if(oper<4){
								 if(isInitAnycat!=0){  //是否初始化过Anycat视频插件 
										BRAC_TransBufferEx(userId,partyName,2,oper,0);    
										EnterRoomRequest(2);
								 }
								//设置被点用户ID为全局变量
								mTargetUserId =userId;
								if(oper==1){
									$(".yc-video-content1").hide();
									$(".yc-video-content2").show();  
								}else if(oper==2){
									$(".yc-vs-content").hide();
									$(".yc-vs-content2").show(); 
								
								}
							}
					
				});
		
			}else{
				BRAC_TransBufferEx(userId,"",2,0,0); 
			}
			
		}
		//提示
		function showTip(msg,roomid,oper,userId,partyName){//type <4:请求  4：接受
			$(".tooltip").show();
			$("#tipMsg").html(msg);
			$('.tb-determine').click(function(e) {
				if(oper<4){
					 if(isInitAnycat!=0){  //是否初始化过Anycat视频插件 
							oper_=oper;
							BRAC_TransBufferEx(userId,partyName,2,oper,0);    
							EnterRoomRequest(2);
					 }
					//设置被点用户ID为全局变量
					mTargetUserId =userId;
					showAnychatSelector();
				}else if(oper==4){//接受
					EnterRoomRequest(roomid); 
					showAnychatSelector();
				} 
				
				$(".tooltip").hide();
				 
			}) ;
			$('.tb-cancel').click(function(e) {
				$(".tooltip").hide();
			}) ;
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
			setTimeout(function(){ 
				//解决画中画无法显示问题
			     $("#AnyChatLocalVideoDiv").height($("#AnyChatLocalVideoDiv").height()+1);
			     $("#AnyChatLocalVideoDiv").height($("#AnyChatLocalVideoDiv").height()-1);
			}, 500);
			  
		}
 
		//客户端进入房间，dwRoomId表示所进入房间的ID号，errorcode表示是否进入房间：0成功进入，否则为出错代码
		function OnAnyChatEnterRoom(dwRoomId, errorcode) {
			if(oper_==1){
			        BRAC_UserCameraControl(mSelfUserId, 1); 	// 打开本地视频
			  }
			  BRAC_UserSpeakControl(mSelfUserId, 1); 		// 打开本地语音
			  ShowNotifyMessage("Welcome use AnyChat, successfully enter the room:" + dwRoomId, NOTIFY_TYPE_SYSTEM);
			  // 设置远程视频显示位置（没有关联到用户，只是占位置）
			  BRAC_SetVideoPos(0, GetID("AnyChatRemoteVideoDiv"), "ANYCHAT_VIDEO_REMOTE");
			  GetID("ANYCHAT_VIDEO_REMOTE").SetBkImage("${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/bk.jpg");
			 // 设置本地视频显示位置
		     BRAC_SetVideoPos(mSelfUserId, GetID("AnyChatLocalVideoDiv"), "ANYCHAT_VIDEO_LOCAL");
		     GetID("ANYCHAT_VIDEO_LOCAL").SetBkImage("${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/bk.jpg");

		     /* 		     mRefreshVolumeTimer = setInterval(function () {
		            GetID("LocalAudioVolume").style.width = GetID("AnyChatLocalVideoDiv").offsetHeight * BRAC_QueryUserStateInt(mSelfUserId, BRAC_USERSTATE_SPEAKVOLUME) / 100 + "px";
					if(mTargetUserId != -1)
						GetID("RemoteAudioVolume").style.width = GetID("AnyChatRemoteVideoDiv").offsetHeight * BRAC_QueryUserStateInt(mTargetUserId, BRAC_USERSTATE_SPEAKVOLUME) / 100 + "px";
					else
						GetID("RemoteAudioVolume").style.width = "0px";
		        }, 100);  */
		}
		
		
 
		
		</script>
        <script>
        var n= 0; //计算毫秒
        var s =0;  //计算秒
        var m=0; //计算分钟
        var h=0; //计算小时
        var timer=null;
        var is_onstart=true; //是否点击开始
        var is_onstop=false;  //是否点击暂停
        var count=0;  //点击开始次数
        var sstart=0;   //记录暂停时的次数
        //重置时间
        function resetTime(){
            clearTimeout(timer);
            is_onstart=false;
            count=0;
            n=0;
            s=0;
            m=0;
        	h=0;
            sstart=0;
           // $(".mytime").html("00:00:00");
            $(".mytime_sp").html("00:00:00");
            $(".mytime_yp").html("00:00:00");
            $("#min_time").html("00:00:00");
        	
        }
        //开始计时
        function startTime(){
            var interval=1;
            var start=Date.now();
            count++;
            is_onstart=true;
            //is_onstop=false;
              if(is_onstart && (count==1) ){
                timer=setTimeout(step,interval);
                function step(){
                  var  msecond=(Date.now()-start)+sstart;
        		  n=msecond%1000;
                  s=parseInt(msecond/1000);
                  m=parseInt(msecond/60000);
        		  h=parseInt(msecond/3600000);
        		  var timStr=toDub(h%24)+":"+toDub(m%60)+":"+toDub(s%60);
        		 // $(".mytime").html(timStr);
        		  $("#mytime_yp").html(timStr);
        		  $("#mytime_sp").html(timStr);
                  $("#min_time").html(timStr);
        		  
                  
        		  if(is_onstart){
                    setTimeout(step,interval);
                  }
                  else {
                    sstart=msecond;
                    clearTimeout(timer);
                  }
                }
              }
        }
        //暂停并且清空计时器
        function endTime(){
        	   clearTimeout(timer);
        	   is_onstart=false;
        	   is_onstop=true;
        	   count=0;
        }
        //补零
        function toDub(n){
          return n<10?"0"+n:""+n;
        }
        
        
            // 功能按钮的点击效果
			$('.yc-features-btn').on('click','a',function(){
				$(this).toggleClass('active');
                if($(this).hasClass('comment')){
                    if($(this).hasClass('active')){
                        $(this).parents('.yc-features-btn').parent().siblings('.yc-vs1-right').show();
                        $("#singleFlex").css('min-width','1060px')
                      //  $(this).parents('.yc-features-btn').parent().siblings('.yc-vs1-right').find(".yc-vs1r-content").getNiceScroll().resize();
                    }else{
                        $(this).parents('.yc-features-btn').parent().siblings('.yc-vs1-right').hide();
                        $("#singleFlex").css('min-width','740px')
                        // $(this).parents('.yc-features-btn').parent().siblings('.yc-vs1-right').find(".yc-vs1r-content").getNiceScroll().resize();
                    }
                }
                //是否禁麦
                if($(this).hasClass('mike')){
                	  if($(this).hasClass('active')){
                		  BRAC_UserSpeakControl(mSelfUserId, 0); // 关闭语音 
                      }else{
                    	  BRAC_UserSpeakControl(mSelfUserId, 1); // 打开语音
                      }
                	
                }
                //是否开启摄像头
                if($(this).hasClass('camera')){
                	  if($(this).hasClass('active')){
                		  BRAC_UserCameraControl(mSelfUserId, 0); // 关闭语音 
                      }else{
                    	  BRAC_UserCameraControl(mSelfUserId, 1); // 打开语音
                      }
                	
                }
                //是否禁声音
                if($(this).hasClass('volume')){  
                	  if($(this).hasClass('active')){
                		  //BRAC_AudioSetVolume(BRAC_AD_WAVEOUT,0);//设置音量
                		   BRAC_UserSpeakControl(mTargetUserId, 0); // 关闭语音 
                      }else{
                    	  BRAC_AudioSetVolume(BRAC_AD_WAVEOUT,eLevel);//设置音量
                    	  BRAC_UserSpeakControl(mTargetUserId, 1); // 打开语音
                      }
                	
                }
                
                try {

                	var WshShell = new ActiveXObject("WScript.Shell");
        			var tempdir=WshShell.SpecialFolders("Desktop");
        			 
                    //录像
                    if($(this).hasClass('videotape')){  
                    	  if($(this).hasClass('active')){
                    		 // $(".download").show();
      						  BRAC_SetSDKOption(BRAC_SO_RECORD_FILETYPE, 0);    // 录制为MP4格式
                  		      BRAC_SetSDKOption(BRAC_SO_RECORD_TMPDIR,tempdir+"\\record\\local");
    						  BRAC_StreamRecordCtrl(mTargetUserId,1,ANYCHAT_RECORD_FLAGS_AUDIO+ANYCHAT_RECORD_FLAGS_VIDEO, 0);
                          }else{   
                        	var rs=  BRAC_StreamRecordCtrl(mTargetUserId, 0,ANYCHAT_RECORD_FLAGS_AUDIO+ANYCHAT_RECORD_FLAGS_VIDEO, 0) 
                        	if(rs==0) 
                        	alert("录像文件保存在”"+tempdir+"\\record\\local"+"“");
                          }
                    	
                    }
                    //录音
                    if($(this).hasClass('recording')){  
                    	  if($(this).hasClass('active')){
                    		  BRAC_SetSDKOption(BRAC_SO_RECORD_FILETYPE, 3);    // 录制为MP3格式
                    		  BRAC_SetSDKOption(BRAC_SO_RECORD_TMPDIR,tempdir+"\\record\\local");
      						  BRAC_StreamRecordCtrl(mTargetUserId,1,ANYCHAT_RECORD_FLAGS_AUDIO, 0);
                          }else{
                        	var rs=BRAC_StreamRecordCtrl(mTargetUserId, 0, ANYCHAT_RECORD_FLAGS_AUDIO, 0) 
                        	if(rs==0) 
                        	alert("录音文件保存在”"+tempdir+"\\record\\local"+"“");
                          }
                    	
                    }

               	} catch(err) {
               	  $(this).removeClass('active');
               	  alert('请先启用ActiveX控件！');
               	}
			});
	 
			// 音量调整
			var tag = false,ox=0,bgTop=0,eLevel=0,bTop=0;
			var height = $('.va-bar').height();
			$('.va-btn').on('mousedown',function(e){
				ox=0;
				bTop = parseInt($(this).css('top'))
				ox = e.pageY - bTop;
				tag = true;
				return false;
			})
			$('.va-btn').on('mousemove', function(e){
				if(tag){
					eLevel=0;
					bTop = e.pageY - ox; 
					if (bTop <= 0) {
						bTop = 0;
					}else if (bTop > height) {
						bTop = height;
					}
					$(this).css('top', bTop);
					$(this).siblings('.vab-content').height(100 - bTop);
					eLevel = (100 - bTop);
				}
				return false;
			});
			$('.va-btn').on('mouseup',function(e) {
				tag = false;
				// 音量的数字
				console.log(eLevel);
				return false;
			});
			$('.va-btn').on('mouseout',function(e) {
				tag = false;
				// 音量的数字
				console.log(eLevel);
				return false;
			})
			$('.va-bar').on('mousedown',function(e){
				if (!tag) {
					eLevel=0;
					bgTop =parseInt($(this).offset().top);
					bTop = e.pageY - bgTop;
					if (bTop <= 0) {
						bTop = 0;
					}else if (bTop > height) {
						bTop = height;
					}
					$(this).find('.va-btn').css('top', bTop);
					$(this).find('.vab-content').height(100 - bTop);
					eLevel = (100 - bTop);
				}
				return false;
			})
			$('.va-bar').on('mouseup',function(e) {
				// 音量的数字
				console.log(eLevel)
				return false;
			})
			$('.volume-adjust').click(function(e) {
				$(".volume").removeClass('active');
				BRAC_UserSpeakControl(mTargetUserId, 1); // 打开语音
				BRAC_AudioSetVolume(BRAC_AD_WAVEOUT,eLevel);//设置音量
				return false;
			}) 
			var conversion=0;
			//远程和本地页面切换
			$('.conversion').click(function(e) {
				var userName="";
				 if(conversion==0){
					// 设置远程视频显示位置（没有关联到用户，只是占位置）
					  BRAC_SetVideoPos(mSelfUserId, GetID("AnyChatRemoteVideoDiv"), "ANYCHAT_VIDEO_REMOTE");
					 // 设置本地视频显示位置
				      BRAC_SetVideoPos(mTargetUserId , GetID("AnyChatLocalVideoDiv"), "ANYCHAT_VIDEO_LOCAL");
					 conversion=1;
					 userName=BRAC_GetUserName(mSelfUserId);
					 if(mTargetUserId==-1){
						  userName="当前没有人";
					  }
				}else{
					// 设置远程视频显示位置（没有关联到用户，只是占位置）
					  BRAC_SetVideoPos(mTargetUserId, GetID("AnyChatRemoteVideoDiv"), "ANYCHAT_VIDEO_REMOTE");
					  // 设置本地视频显示位置
				      BRAC_SetVideoPos( mSelfUserId, GetID("AnyChatLocalVideoDiv"), "ANYCHAT_VIDEO_LOCAL");
					  conversion=0;
					  userName=BRAC_GetUserName(mTargetUserId);
					  if(mSelfUserId==-1){
						  userName="当前没有人";
					  }
					  
				}
				$("#sname").html(userName);
				setTimeout(function(){ 
					 $("#AnyChatLocalVideoDiv").height($("#AnyChatLocalVideoDiv").height()+1);
					 $("#AnyChatLocalVideoDiv").height($("#AnyChatLocalVideoDiv").height()-1);
					 $("#AnyChatRemoteVideoDiv").height($("#AnyChatRemoteVideoDiv").height()+1);
					 $("#AnyChatRemoteVideoDiv").height($("#AnyChatRemoteVideoDiv").height()-1);
				}, 500);
		
				//reVideoDivSize();
				return false;
			})
			//关闭或者挂断
			$('.pctr-close,.vhu-btn,.yc-vsc-hang-up').click(function(e) {
				BRAC_SendTextMessage(mTargetUserId, 1, "ffcstest@ffcstest@ffcstestExit"); //调用发送信息函数
				endTime();
				$(".public-content").hide();
				$("#ReceiveMsgDiv").html('');
				$(".recording-box a").removeClass('active');
				mTargetUserId=-1;
				BRAC_LeaveRoom(-1); 
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
