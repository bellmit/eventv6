<!DOCTYPE html>
<html lang="en" style="overflow: hidden;">
<head>
    <meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>南昌-代办事件名称</title>
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css"/>
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index-mask.css">
    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
        var winW, scale;
        var imgclsnum=0;
        $(window).on('load resize', function () {
            fullPage();
            setTimeout(fullPage, 10);//二次执行页面缩放，解决全屏浏览时滚动条问题
            function fullPage() {//将页面等比缩放
                winW = $(window.parent.parent).width();
   
                var whdef = 100 / 1920;
                var rem = winW * whdef;// 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
                $('html').css('font-size', rem + "px");


                if(winW<=1200){
                    $("head").append("<link rel='stylesheet' href='${uiDomain!}/web-assets/_big-screen/nanchang-cc/css/small-screen.css'>");
                        
                 
                }
                
                picSwiper();
            }
            
            function picSwiper(){
                var swiper;
            
		        $(".soundcls,.imgcls,.videocls").hide();
		        $(".imgcls").show();//.imgcls-container
		        
		        <#if (imgs?? && imgs?size>0)>
		        if(imgclsnum <1){
		        
			    swiper = new Swiper('.imgcls-container', {
			    navigation: {nextEl: '.imgcls-next',prevEl: '.imgcls-prev'},
		        pagination: {el: '#imgcls-pagination',type: 'fraction'}
		        });
		        
		        imgclsnum=imgclsnum+1;
		        
			    $(".imgcls-next,.imgcls-prev").show();
		        }
		        <#else>
		        $(".imgcls-next,.imgcls-prev").hide();
			    </#if>
			    
			    formatAttrSeq('imgcls',0);
		    
            }
            
        });
    </script>
    <style>
    .maed-cl-bottom {height: 2.1rem;}
    .maed-w60 {width: 55%;}
    .maed-w40 {width: 45%;}
    .maed-clb-list li {margin-bottom: .1rem;}
    .lmcc-mask-box {width: 7.8rem;}
    .aj_items_t_yellow {margin-left: .2rem;text-align: left;}
	.aj_items_t2 {width: 2.7rem;}
	.aj_items_t_result {text-align: left;}
	
	.maed-cl-top {
    	height: 2.6rem;
    }
    </style>
</head>
<body style="background: transparent;">
    <div class="mae-container" style="display:block">
        <div class="maed-content">
            <div class="clearfix">
                <div class="maed-c-left fl">
                    <div class="maed-cl-top bs" style="width:95%" id="maed-cl-top">
                        <p class="maed-clt-title"><#if event.eventClass??>[${event.eventClass}]</#if> <span>${event.eventName!}</span></p>
                        <p class="maed-clt-title">于  <span>${event.happenTimeStr}</span>  在  <span>${event.occurred}</span>  发生：</p>
                        <p class="maed-clt-text">${event.content}</p>
                    </div>
                    <div class="maed-cl-bottom mtr10 bs" style="overflow:hidden;width:95%">
                        <ul class="maed-clb-list bs" style="margin-top:2px;overflow:hidden;padding-top:0px" id="maed-clb-list">
                            <li class="maed-w60 fl clearfix">
                                <p>信息来源：</p>
								<p><code>${event.sourceName!}</code></p>
                            </li>
                            <li class="maed-w40 fr clearfix">
                                <p>采集渠道：</p>
								<p><code>${event.collectWayName!}</code></p>
                            </li>
                            <li class="maed-w60 fl clearfix">
                                <p>紧急程度：</p>
								<p>
								    <#if event.urgencyDegree?? && event.urgencyDegree!='01'>
                                        <code style="color:red">${event.urgencyDegreeName!}</code>
									<#else>
                                        <code>${event.urgencyDegreeName!}</code>
									</#if>
								</p>
                            </li>
                            <li class="maed-w40 fr clearfix">
                                <p>影响范围：</p>
								<p><code>${event.influenceDegreeName!}</code></p>
                            </li>
                            <li class="maed-w60 fl clearfix">
								<p>联系人员：</p>
								<p><code>${event.contactUser!}(${event.tel})</code></p>
							</li>
							<li class="maed-w40 fr clearfix"><p>当前状态：</p><p><code>${event.statusName!}</code></p></li>
							<li class="maed-w60 fl clearfix"><p>事件编号：</p><p><code>${event.code!}</code></p></li>
							<li class="maed-w40 fr clearfix"><p>涉及人员：</p><p><code><#if event.involvedNumName??>(<b>${event.involvedNumName}</b>)</#if>${event.involvedPersion!}</code></p></li>
							<li class="maed-w560 fl clearfix">
								<p>巡访类型：</p>
								<p><code>${patrolTypeName!}</code></p>
							</li>
												
							<li class="maed-w40 fr clearfix">
								<p>责任点位：</p>
								<p><code>${pointSelectionName!}</code></p>
							</li>
							<li  class="maed-w100 fl clearfix"><p>所属网格：</p><p><code>${event.gridPath!}</code></p></li>
                        </ul>
                    </div>
                </div>
                <div class="maed-c-right fr bs" style="">
                    <div class="legal-mcccr-top bs">
                        <ul class="lmcccrt-list clearfix">
                            <li id="li_imgcls" class="active licls">
                                <a href="javascript:attrChange('imgcls');">
                                    <p>图片(<#if (imgs?? && imgs?size>0)>${imgs?size}<#else>0</#if>)</p>
                                </a>
                            </li>
                            <li id="li_videocls" class="licls">
                                <a href="javascript:attrChange('videocls');">
                                    <p>视频(<#if (videos?? && videos?size>0)>${videos?size}<#else>0</#if>)</p>
                                </a>
                            </li>
                            <li id="li_soundcls" class="licls">
                                <a href="javascript:attrChange('soundcls');">
                                    <p>录音(<#if (sounds?? && sounds?size>0)>${sounds?size}<#else>0</#if>)</p>
                                </a>
                            </li>
                        </ul>
                    </div>
                    
                    <div class="legal-mcccr-center bs imgcls">
                        <div class="swiper-container swiper-container-horizontal imgcls-container">
                            <div class="swiper-wrapper" style="transform: translate3d(0px, 0px, 0px);">
                                <#if (imgs?? && imgs?size>0)>
									<#list imgs as r>
										<div class="swiper-slide" >
											<img class="imgSize" id="imgcls${r_index}" attrseq="${r.eventSeq}" style="width:1.92rem" onclick="showImg(${r_index})" src="${IMG_URL}${r.filePath}"/>
										</div>
									</#list>
								<#else>
									<div class="swiper-slide">
										<img class="imgSize" style="width:1.92rem" src="${rc.getContextPath()}/images/nodata.png" />
									</div>
								</#if>
                            </div>
                           <div class="swiper-button-next imgcls-next" onclick="formatAttrSeq('imgcls',1)"></div>
						   <div class="swiper-button-prev imgcls-prev" onclick="formatAttrSeq('imgcls',-1)"></div>
                        </div>
                        
                        <div class="swiper-pagination seqClass" id="imgcls-seq" style="margin-top: 0.2rem;font-size: 0.15rem;color: white;margin-left: 1.18rem;"></div>
                        
                        <div class="swiper-pagination" id="imgcls-pagination" style="margin-bottom: 0.5rem;font-size: 0.2rem;color: white;margin-left:3.1rem"></div>
                    </div>
                    
                    <div class="legal-mcccr-center bs soundcls">
                        <div class="swiper-container swiper-container-horizontal soundcls-container">
                            <div class="swiper-wrapper" style="transform: translate3d(0px, 0px, 0px);">
                                <#if (sounds?? && sounds?size>0)>
									<#list sounds as r>
										<div class="swiper-slide">
											<img class="imgSize" id="soundcls${r_index}" attrseq="${r.eventSeq}" style="width:1.92rem" onclick="showVideo(${r.attachmentId})" src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg"/>
										</div>
									</#list>
								<#else>
									<div class="swiper-slide">
										<img class="imgSize" style="width:1.92rem" src="${rc.getContextPath()}/images/nodata.png" />
									</div>
								</#if>
                            </div>
                            <div class="swiper-button-next soundcls-next" onclick="formatAttrSeq('soundcls',1)"></div>
						    <div class="swiper-button-prev soundcls-prev" onclick="formatAttrSeq('soundcls',-1)"></div>
                        </div>
                        
                        <div class="swiper-pagination seqClass" id="soundcls-seq" style="margin-top: 0.2rem;font-size: 0.15rem;color: white;margin-left: 1.18rem;"></div>
                        
                        <div class="swiper-pagination" id="soundcls-pagination" style="margin-bottom: 0.5rem;font-size: 0.2rem;color: white;margin-left:3.1rem"></div>
                    </div>
                    
                    <div class="legal-mcccr-center bs videocls">
                        <div class="swiper-container swiper-container-horizontal videocls-container">
                            <div class="swiper-wrapper" style="transform: translate3d(0px, 0px, 0px);">
                                <#if (videos?? && videos?size>0)>
									<#list videos as r>
										<div class="swiper-slide">
											<img class="imgSize" id="videocls${r_index}" attrseq="${r.eventSeq}" style="width:1.92rem" onclick="showVideo(${r.attachmentId})" src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg"/>
										</div>
									</#list>
								<#else>
									<div class="swiper-slide">
										<img class="imgSize" style="width:1.92rem" src="${rc.getContextPath()}/images/nodata.png" />
									</div>
								</#if>
                            </div>
                            <div class="swiper-button-next videocls-next" onclick="formatAttrSeq('videocls',1)"></div>
						    <div class="swiper-button-prev videocls-prev" onclick="formatAttrSeq('videocls',-1)"></div>
                        </div>
                        
                        <div class="swiper-pagination seqClass" class="seqClass" id="videocls-seq" style="margin-top: 0.2rem;font-size: 0.15rem;color: white;margin-left: 1.18rem;"></div>
                        
                        <div class="swiper-pagination" id="videocls-pagination" style="margin-bottom: 0.5rem;font-size: 0.2rem;color: white;margin-left:3.1rem"></div>
                    </div>
                    
                </div>
            </div>
        </div>
        <a href="javascript:void(0);" class="lmcc-mask-open" id="lmcc-mask-open-container">
            <i class="lmccm-open"></i>
            <p>处</p>
            <p>理</p>
            <p>记</p>
            <p>录</p>
        </a>
        <div class="lmcc-mask-box" id='lmcc-mask-box'>
            <a href="javascript:void(0);" class="lmcc-mask-open" id="lmcc-mask-open-box">
                <i class="lmccm-close"></i>
                <p>处</p>
                <p>理</p>
                <p>记</p>
                <p>录</p>
            </a>
            <div class="lmcc-mask bs" id="lmcc-mask">
                <div class="lmcc-mask-content">
                    <div class="det-links-des clearfix">
                        <ul class="flex layer_aj_bt_n fr">
                            <li class="clearfix fr">
                                <div class="aj_ks aj_ks_blue fl">
                                    <div class="aj_ks1"></div>
                                    <div class="aj_ks2"></div>
                                </div>
                                <p>当前环节</p>
                            </li>
                            <li class="flex flex-ac fl" style="margin-right:0.4rem">
                                <div class="aj_ks aj_ks_gray">
                                    <div class="aj_ks1"></div>
                                    <div class="aj_ks2"></div>
                                </div>
                                <p>历史环节</p>
                            </li>
                            <li class="flex flex-ac">
					        	<div class="aj_ks aj_ks_gray">
									<div class="aj_ks1" style="background-color: #FFB90F"></div>
									<div class="aj_ks2"></div>
						        </div>
					        	<p>驳回环节</p>
					        </li>
                        </ul>
                    </div>
                    <div class="layer_aj_bt_b bs clearfix">
                        <p class="layer_aj_bt_b1">办理环节</p>
                        <p class="layer_aj_bt_b2">办理人/办理时间</p>
                        <p class="layer_aj_bt_b3">办理意见</p>
                    </div>
                    <div class="layer_aj_bt_s" id="layer_aj_bt_s">
                        <div class="layer_aj_bt_line" style="left:1.52rem" id="layer_aj_bt_line"></div>
                        <ul class="layer_aj_bt_items">
                        <#if (process?? && process?size>0)>
                        <#list process as r>
                            <#if r.HANDLE_PERSON??>
                            <li class="flex flex-ac">
                                <div class="aj_items_h aj_items_h_green" style="width:1.2rem;color: #00fffc;">${r.TASK_NAME}</div>
                            <#else>
                            <li class="flex flex-ac">
                                <div class="aj_items_h aj_items_h_green" style="width:1.2rem">${r.TASK_NAME}<#if r.OPERATE_TYPE??&&r.OPERATE_TYPE==2><br><div class="aj_items_h aj_items_h_green" style="width:1.2rem;color: #FFB90F;font-size: 0.1rem;">(驳回)</div></#if></div>
                            </#if> 
                            	<#if r_index =0>
                                <div class="aj_ks aj_ks_blue">
                                    <div class="aj_ks1"></div>
                                    <div class="aj_ks2"></div>
                                </div>
                                <#elseif r.OPERATE_TYPE??&&r.OPERATE_TYPE==2>
                                <div class="aj_ks aj_ks_gray">
									<div class="aj_ks1" style="background-color: #FFB90F"></div>
									<div class="aj_ks2"></div>
						        </div>
						        <#else>
						        <div class="aj_ks aj_ks_gray">
                                    <div class="aj_ks1"></div>
                                    <div class="aj_ks2"></div>
                                </div>
                                </#if>
                                
                                <#if r.HANDLE_PERSON??>
                                <div>
                                
                                <div class="flex flex-ac">
                                <p class="aj_items_t aj_items_t_yellow" style="display:table">${r.HANDLE_PERSON}</p>
                                </div>
                                
                                <#if (r.subAndReceivedTaskList?? && r.subAndReceivedTaskList?size>0)>
									<#list r.subAndReceivedTaskList as sub>
															
											<#if (sub.timeAndRemarkList?? && sub.timeAndRemarkList?size>0)>
												<#list sub.timeAndRemarkList as s>
										        <div class="flex flex-ac mtr10">
										        <div class="aj_items_t2 flex">
										        
										        <p class="aj_items_t aj_items_t_yellow" style="margin-left:0rem"><#if s_index=0>${sub.TRANSACTOR_NAME}<#if sub.ORG_NAME??>(${sub.ORG_NAME})</#if><br></#if>
												    <#if s_index =0>
													<span>接受时间</span>：${s.RECEIVE_TIME}
													<#else>
													<span>处理时间</span>：${s.END_TIME}
													</#if>
										        </p>
										        </div>
										        <#if s.REMARKS??>
                                					<p class="aj_items_t aj_items_t_result">${s.REMARKS}</p>
                                				</#if>
										        </div>
												</#list>
										    </#if>
										
																
									</#list>
								</#if>
                                </div>
                                
                                <#else>
                                
                                <div class="aj_items_t2 flex">
                                    <p class="aj_items_t aj_items_t_green bs" style="max-width: 2.7rem;">${r.TRANSACTOR_NAME}(${r.ORG_NAME})<#if r.INTER_TIME?? ><br><span>耗时： ${r.INTER_TIME}</span></#if><br><#if r.END_TIME??><span>办理时间</span>：${r.END_TIME}</p></#if>
                                </div>
                                
                                </#if>
                                
                                
                                <#if r.REMARKS??>
                                <p class="aj_items_t aj_items_t_result">${r.REMARKS}</p>
                                </#if>
                            </li>
                            </#list>
                        </#if>    
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script src="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${uiDomain!''}/web-assets/common/js/basic/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
    <script>
    var u = navigator.userAgent, app = navigator.appVersion;   
	var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器	
		if(isAndroid){
        //if(navigator.userAgent.match("ua_ffcs")){
			$(".imgSize").css({"width":"2.8rem","height":"4.4rem"});
			$(".lmcc-mask-open").css("top","3.05rem");
			$(".swiper-pagination").css("margin-left","3.6rem");
			$(".seqClass").css("margin-left","1.66rem");
			$(".seqClass").css("margin-top","0rem");
		}
        
    
        //事件处理记录
        $('#layer_aj_bt_s').niceScroll({
		    cursorcolor:"rgba(0, 160, 233, .4)",
		    cursoropacitymax:1,
		    touchbehavior:false,
		    cursorwidth:"4px",
		    cursorborder:"0",
		    cursorborderradius:"4px",
		    zindex: 25,
		    horizrailenabled: false,
		    autohidemode: false //隐藏式滚动条
		});
		//$('.mae-container>.lmcc-mask-open').click(function(){
		$('#lmcc-mask-open-container').click(function(){
		    $('#event_detail',parent.document).css('z-index','2');
			$(this).hide();
			$("#maed-cl-top").getNiceScroll().hide();
			$("#maed-clb-list").getNiceScroll().hide();
			$('#lmcc-mask-box').show();
			$('#lmcc-mask').animate({"right": "0"},100,function(){
				$('#lmcc-mask-box').css({"overflow":"visible"});
				$('#layer_aj_bt_line').height($('.layer_aj_bt_items').height() + 38);
				$("#layer_aj_bt_s").getNiceScroll().resize();
			})
		})
		$('#lmcc-mask-open-box').click(function(){
		    $('#event_detail',parent.document).css('z-index','0');
			$('#lmcc-mask-box').css({"overflow":"hidden"});
			$('#lmcc-mask').animate({"right": "-100%"},100,function(){
				$('#lmcc-mask-box').hide();
				$("#layer_aj_bt_s").getNiceScroll().resize();
				$("#maed-cl-top").getNiceScroll().show();
				$("#maed-clb-list").getNiceScroll().show();
				$('#lmcc-mask-open-container').show();
			})
		})
        // 事件内容的滚动条优化
        $('#maed-cl-top,#maed-clb-list').niceScroll({
            cursorcolor: "rgba(0,160,233,.4)",//#CC0071 光标颜色
            cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
            touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
            cursorwidth: "6px", //像素光标的宽度
            cursorborder: "0", // 游标边框css定义
            cursorborderradius: "3px",//以像素为光标边界半径
            autohidemode: false, //是否隐藏滚动条
            horizrailenabled: false, 
            background: "rgba(29,32,136,.4)",
        });
		
        var soundclsnum=0,videoclsnum=0;
        function attrChange(cls){
            $(".soundcls,.imgcls,.videocls").hide();
			$(".licls").removeClass("active");
			$("#li_"+cls).addClass("active");
			
			$("."+cls).show();
			
			<#if (videos?? && videos?size>0)>
				if(cls=="videocls" && videoclsnum <1){
					var videocls = new Swiper('.videocls-container', {
					  pagination: {el: '#videocls-pagination',type: 'fraction'},
				      navigation: {nextEl: '.videocls-next',prevEl: '.videocls-prev'}
				    });
				    videoclsnum=videoclsnum+1;
					$(".videocls-next,.videocls-prev").show();
				}
			<#else>
		        $(".videocls-next,.videocls-prev").hide();
			</#if>	
			<#if (sounds?? && sounds?size>0)>
				if(cls=="soundcls" && soundclsnum <1){
					var soundcls = new Swiper('.soundcls-container', {
					  pagination: {el: '#soundcls-pagination',type: 'fraction'},
				      navigation: {nextEl: '.soundcls-next',prevEl: '.soundcls-prev'},
				    });
				    soundclsnum=soundclsnum+1;
					$(".soundcls-next,.soundcls-prev").show();
				}
			<#else>
		        $(".soundcls-next,.soundcls-prev").hide();
			</#if>	
			
			formatAttrSeq(cls,0);
				
            $(".swiper-slide").css({"width": "100%"})
				
		}
		
		function showImg(i){
			var list = [];
			<#if (imgs?? && imgs?size>0)>
			<#list imgs as r>
				list.push("${IMG_URL}${r.filePath}")
			</#list>
			</#if>
			window.open('${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId=playImg&index='+i+'&paths='+list.toString());
		}
		
		function showVideo(id){//eventSeq
			window.open('${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?videoType=2&attachmentId='+id);
		}
		
		function formatAttrSeq(type,inter){
			
			var index=$("#"+type+"-pagination>.swiper-pagination-current").text();
			
			var indexInt=parseInt(index)-1;
			
			var cur=indexInt;
			if(inter){
			
				cur=indexInt+inter;
			}
			
			var seq=$("#"+type+cur.toString()).attr("attrseq");
			var seqStr="";
			if(seq=="1"){
				seqStr="处理前";
			}else if(seq=="2"){
				seqStr="处理中";
			}else if(seq=="3"){
				seqStr="处理后";
			}
			
			$("#"+type+"-seq").html(seqStr);
		}
		
    </script>
</body>
</html>