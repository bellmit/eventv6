<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"  style="background-color: rgba(0, 0, 0, 0);">
<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>平安智慧</title>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/yancheng/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/yancheng/css/screen-yanduqu.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/yancheng/css/screen-yanduqu-new.css"/>
		<!--引入 重置默认样式 statics/basic -->
		<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
		<script src="${uiDomain}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js" type="text/javascript" charset="utf-8"></script>
		<script src="${uiDomain}/web-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
		<style>
		.ydq-mask{display:block; background-color: rgba(0, 0, 0, 0);}
		.ydq-mci-back{left:42px;}
		.legal-mcccr-center{height: 560px;padding: 20px 0;}
		.ydq-mc-item{display:block;}
		.soundcls{display:none;}
		.videocls{display:none;}
		.swiper-pagination{    font-size: .18rem;    color: #b2cdff;}
		.swiper-button-next{display:none;}
		.swiper-button-prev{display:none;}
		.swiper-slide img{    cursor: pointer;}
			.wrap-container{background:none;}

			.ydq-mask-content{background:none;}
		.legal-mccclb-list li{margin-top: 25px;}
		.legal-mccclb-list li p code{color: #05c5f4;}
		.lmccclt-title {
    margin-top: 15px;
}.aj_items_t_result{word-wrap: break-word;}

		</style>
		<script type="text/javascript">
			//设置页面分辨率自适应
			$(function(){
				var scale, winW, winH, pageW, pageH, scaleW, scaleH;
				$(window).on('load resize', function(){
						fullPage();
				});
				function fullPage(){//将页面等比缩放
					winW = $(window).width();
					winH = $(window).height();
					scale = winW/1920;
					pageW = $('.wrap-container').width();
					pageH = $('.wrap-container').height();
					scaleW = pageW*scale;
					scaleH = pageH*scale;
					if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){//判断浏览器是否为火狐浏览器
						$('.wrap-container').css({'height': 1080*scale,'WebkitTransform': 'scale('+scale+')', 'MozTransform': 'scale('+scale+')', 'MsTransform': 'scale('+scale+')', 'transform': 'scale('+scale+')', 'top': (scaleH - 1080)/2, 'left': (scaleW - 1920)/2});
					}else{
						$('.wrap-container').css({'height': 1080*scale,'zoom': scale});
						$('.ydq-mask').css({'height': 1080*scale});
					}
					//$('.container-fluid').css({'width': 1920*scale, 'height': 1080*scale, 'overflow': 'hidden'})
				}
				
				//事件处理记录
				$('#open_div').click(function(){
					$(this).hide();
					$('.lmcc-mask-box').show();
					$('.lmcc-mask').animate({"right": "0"},100,function(){
						$('.lmcc-mask-box').css({"overflow":"unset"});
						$('.layer_aj_bt_line').height($('.layer_aj_bt_items').height() + 38);
						$(".layer_aj_bt_s").getNiceScroll().resize();
					})
				})
				$('#close_div').click(function(){
					$('.lmcc-mask-box').css({"overflow":"hidden"});
					$('.lmcc-mask').animate({"right": "-100%"},100,function(){
						$('.lmcc-mask-box').hide();
						$(".layer_aj_bt_s").getNiceScroll().resize();
						$('#open_div').show();
					})
				});
				<#if (imgs?? && imgs?size>0)>
				var imgcls = new Swiper('.imgcls-container', {
					pagination: {el: '#imgcls-pagination',type: 'fraction'},
				    navigation: {nextEl: '.imgcls-next',prevEl: '.imgcls-prev'}
				}); 		
				$(".imgcls-next,.imgcls-prev").show();
				</#if>		
					$(".legal-mcccl-top>div").niceScroll({cursorcolor:"rgba(0, 160, 233, .4)",cursoropacitymax:1,touchbehavior:false,cursorwidth:"4px",cursorborder:"0",cursorborderradius:"4px",autohidemode: false});

                $('.layer_aj_bt_s').niceScroll({cursorcolor:"rgba(0, 160, 233, .4)",cursoropacitymax:1,touchbehavior:false,cursorwidth:"4px",cursorborder:"0",cursorborderradius:"4px",zindex: 25,autohidemode: false});
			})
			var soundclsnum=0,videoclsnum=0;
			function attrChange(cls){
				$(".soundcls,.imgcls,.videocls").hide();
				$(".licls").removeClass("active");
				$("#li_"+cls).addClass("active");
				$("."+cls).show();
				<#if (videos?? && videos?size>0)>
				if(cls=="videocls" && videoclsnum++ <1){
					var videocls = new Swiper('.videocls-container', {
					  pagination: {el: '#videocls-pagination',type: 'fraction'},
				      navigation: {nextEl: '.videocls-next',prevEl: '.videocls-prev'},
				    });
					$(".videocls-next,.videocls-prev").show();
				}</#if>	
				<#if (sounds?? && sounds?size>0)>
				if(cls=="soundcls" && soundclsnum++ <1){
					var soundcls = new Swiper('.soundcls-container', {
					  pagination: {el: '#soundcls-pagination',type: 'fraction'},
				      navigation: {nextEl: '.soundcls-next',prevEl: '.soundcls-prev'}
				    });
					$(".soundcls-next,.soundcls-prev").show();
				}</#if>	
				
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
			function showVideo(id){
				window.open('${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?videoType=2&attachmentId='+id);
			}
		</script>
	</head>
	<body style="background-color: rgba(0, 0, 0, 0);overflow:hidden;">
		<div class="wrap-container">
			
				<div class="ydq-mask" >
					<div class="ydq-mask-content" style="top: 0px;">
					<!--弹窗层-->
						<div class="ydq-mc-item ydq-mc-item2">
							<#if nofull??><#else>
							<a href="javascript:parent.closeInfo();" class="ydq-mci-back">
								<p>返回</p>
							</a>
							</#if>
							<div class="ydq-mci-title flex flex-jc mt40">
								<p>事件详情</p>
								<i></i>
							</div>
							<div class="ydq-mci-event-details" >
								
								<div class="legal-mcc-center clearfix">
									<div class="legal-mccc-left fl ">
										<div class="legal-mcccl-top bs">
											<div style="overflow: hidden;">
												<p class="lmccclt-title"><#if event.eventClass??>[${event.eventClass}]</#if> <span>${event.eventName!}</span></p></p>
												<p class="lmccclt-title">于  <span>${event.happenTimeStr}</span>  在  <span>${event.occurred}</span>  发生：</p>
												<p class="lmccclt-text">
													${event.content}</p>
											</div>
										</div>
										<div class="legal-mcccl-bottom bs">
											<ul class="legal-mccclb-list clearfix">
												<li>
													<p>信息来源：</p>
													<p><code>${event.sourceName!}</code></p>
												</li>
												
												<li>
													<p>采集渠道：</p>
													<p><code>${event.collectWayName!}</code></p>
												</li>
												<li>
													<p>紧急程度：</p>
													<p><#if event.urgencyDegree?? && event.urgencyDegree!='01'>
                                        <code style="color:red">${event.urgencyDegreeName!}</code>
									<#else>
                                        <code>${event.urgencyDegreeName!}</code>
									</#if></p>
												</li>
												<li>
													<p>影响范围：</p>
													<p><code>${event.influenceDegreeName!}</code></p>
												</li>
												<li>
													<p>联系人员：</p>
													<p><code>${event.contactUser!}(${event.tel})</code></p>
												</li>
												<li><p>当前状态：</p><p><code>${event.statusName!}</code></p></li>
												<li><p>事件编号：</p><p><code>${event.code!}</code></p></li>
												<li><p>涉及人员：</p><p><code><#if event.involvedNumName??>(<b>${event.involvedNumName}</b>)</#if>${event.involvedPersion!}</code></p></li>
												<li style="width: 100%;"><p>所属网格：<code>${event.gridPath!}</code></p></li>
												
											</ul>
										</div>
									</div>
									<div class="legal-mccc-right fr">
										<div class="legal-mcccr-top bs">
											<ul class="lmcccrt-list clearfix">
												<li class="active licls" id="li_imgcls">
													<a href="javascript:attrChange('imgcls');">
														<p>图片(<#if (imgs?? && imgs?size>0)>${imgs?size}<#else>0</#if>)</p>
													</a>
												</li>
												<li  id="li_videocls"  class=" licls" >
													<a href="javascript:attrChange('videocls');">
														<p>视频(<#if (videos?? && videos?size>0)>${videos?size}<#else>0</#if>)</p>
													</a>
												</li>
												<li  id="li_soundcls"  class=" licls" >
													<a href="javascript:attrChange('soundcls');">
														<p>录音(<#if (sounds?? && sounds?size>0)>${sounds?size}<#else>0</#if>)</p>
													</a>
												</li>
												
											</ul>
										</div>
										
										
										<div class="legal-mcccr-center bs soundcls">
											<div class="swiper-container  soundcls-container" >
												<div class="swiper-wrapper">
												<#if (sounds?? && sounds?size>0)>
													<#list sounds as r>
														<div class="swiper-slide">
															<img onclick="showVideo(${r.attachmentId})"  src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg" />
														</div>
													</#list>
													<#else>
													<div class="swiper-slide">
														<img  src="${rc.getContextPath()}/images/notbuilding.gif" />
													</div>
												</#if>
												</div>
												 <div class="swiper-pagination" id="soundcls-pagination"></div>
												<div class="swiper-button-next soundcls-next"></div>
												<div class="swiper-button-prev soundcls-prev"></div>
											</div>
										</div> 
										
										<div class="legal-mcccr-center bs imgcls">
											<div class="swiper-container  imgcls-container" >
												<div class="swiper-wrapper">
												<#if (imgs?? && imgs?size>0)>
													<#list imgs as r>
														<div class="swiper-slide">
															<img  onclick="showImg(${r_index})" src="${IMG_URL}${r.filePath}"/>
														</div>
													</#list>
														<#else>
													<div class="swiper-slide">
														<img  src="${rc.getContextPath()}/images/notbuilding.gif" />
													</div>
													</#if>
												</div>
												 <div class="swiper-pagination" id="imgcls-pagination"></div>
												<div class="swiper-button-next imgcls-next"></div>
												<div class="swiper-button-prev imgcls-prev"></div>
											</div>
										</div>
										
										<div class="legal-mcccr-center bs videocls">
											<div class="swiper-container videocls-container">
												<div class="swiper-wrapper">
												<#if (videos?? && videos?size>0)>
													<#list videos as r>
														<div class="swiper-slide">
															<img onclick="showVideo(${r.attachmentId})" src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/video.jpg" />
														</div>
													</#list>
														<#else>
													<div class="swiper-slide">
														<img  src="${rc.getContextPath()}/images/notbuilding.gif" />
													</div>
													</#if>
												</div>
												 <div class="swiper-pagination " id="videocls-pagination"></div>
												<div class="swiper-button-next videocls-next"></div>
												<div class="swiper-button-prev videocls-prev"></div>
											</div>
										</div>
										
									</div>
								</div>
							</div>
							<div class="lmcc-mask-box">
								<a href="javascript:void(0);" class="lmcc-mask-open"  id="close_div">
									<i class="lmccm-close"></i>
									<p>处</p>
									<p>理</p>
									<p>记</p>
									<p>录</p>
								</a>
								<div class="lmcc-mask bs">
									<div class="lmcc-mask-content">
										<div class="det-links-des">
											<ul class="flex layer_aj_bt_n flex-ac flex-je">
												<li class="flex flex-ac">
					        						<div class="aj_ks aj_ks_blue">
														<div class="aj_ks1"></div>
														<div class="aj_ks2"></div>
														<div class="aj_ks3"></div>
						        					</div>
					        						<p>当前环节</p>
					        					</li>
					        					<li class="flex flex-ac" style="margin-right:58px">
					        						<div class="aj_ks aj_ks_gray">
														<div class="aj_ks1"></div>
														<div class="aj_ks2"></div>
														<div class="aj_ks3"></div>
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
										<div class="layer_aj_bt_b bs flex flex-ac">
											<p class="layer_aj_bt_b1">办理环节</p>
						    				<p class="layer_aj_bt_b2">办理人/办理时间</p>
						    				<p class="layer_aj_bt_b3">办理意见</p>
										</div>
										<div class="layer_aj_bt_s">
											<div class="layer_aj_bt_line"></div>
											<ul class="layer_aj_bt_items">
											<#if (process?? && process?size>0)>
											<#list process as r>
												<#if r.HANDLE_PERSON??>
												<li class="flex">
					        						<h5 class="aj_items_h aj_items_h_green" style="line-height: .34rem;">${r.TASK_NAME}</h5>
												<#else>
												<li class="flex flex-ac">
					        						<div>
					        						<div><h5 class="aj_items_h aj_items_h_green">${r.TASK_NAME}</h5></div>
					        						<#if r.OPERATE_TYPE??&&r.OPERATE_TYPE==2><div style="color: #FFB90F;margin-left: 85px; transform: scale(0.833333)">(驳回)</div></#if>
					        						</div>
												</#if>
													<#if r_index =0>
					        						<div class="aj_ks aj_ks_blue">
														<div class="aj_ks1"></div>
														<div class="aj_ks2"></div>
														<div class="aj_ks3"></div>
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
														<div class="aj_ks3"></div>
						        					</div>
													</#if>

													
													 <#if r.HANDLE_PERSON??>
														<div>
															<div class="aj_items_t2 flex">
																<p class="aj_items_t aj_items_t_green bs">${r.HANDLE_PERSON}</p>
															</div>
															<#if (r.subAndReceivedTaskList?? && r.subAndReceivedTaskList?size>0)>
															<#list r.subAndReceivedTaskList as sub>
																<div class="aj_items_t2 flex  mt10">
																<p class="aj_items_t aj_items_t_green bs">${sub.TRANSACTOR_NAME}<#if sub.ORG_NAME??>(${sub.ORG_NAME})</#if><br>
																	<#if (sub.timeAndRemarkList?? && sub.timeAndRemarkList?size>0)>
																		<#list sub.timeAndRemarkList as s>
																			<span>接受时间</span>：${s.RECEIVE_TIME}
																		</#list>
																	</#if></p>
																</div>
															</#list>
															</#if>
															</div>
													<#else>
															<div class="aj_items_t2 flex">
															<p class="aj_items_t aj_items_t_green bs">${r.TRANSACTOR_NAME}(${r.ORG_NAME})<#if r.INTER_TIME?? ><br><span> 耗时 ${r.INTER_TIME}</span></#if><br>
																<#if r.END_TIME??>
																<span>办理时间</span>：${r.END_TIME}</p>
																</#if>
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
							<a href="javascript:void(0);" class="lmcc-mask-open" id="open_div">
								<i class="lmccm-open"></i>
								<p>处</p>
								<p>理</p>
								<p>记</p>
								<p>录</p>
							</a>
						</div>
					</div>
				</div><!--container-->
			</div> <!--wrap_container-->
		</div><!--container-fluid-->
	</body>
</html>
