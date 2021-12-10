<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	    <title><@block name="entryTypePageName">${reportTypeTitle}详情页</@block></title>
	    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/JiangYinPlatform/css/mask.css"/>
	    <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css"/>
	    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/jiangying-cs-new/css/xxbh-mask.css">
	    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>
		<#include "/component/ComboBox.ftl" />
		<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
		<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
		<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/gray/easyui.css">
		<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
		<script src="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js"></script>
		<script src="${uiDomain!''}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<script type="text/javascript" src="${uiDomain!''}/js/paging/paging.js"></script>
		<link rel="stylesheet" href="${uiDomain!''}/js/paging/paging.css">
		<script type="text/javascript" src="${uiDomain!''}/js/zzgl_core.js"></script>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/jquery.mCustomScrollbar.css" />
		<script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>
		
		<script type="text/javascript">
			var winW, scale,rem
			$(document).ready(function(){
				fullPage();
			})
			$(window).on('load resize', function () {
				fullPage();

			});
			function fullPage() {//将页面等比缩放
				winW = <#if pwidth??>${pwidth}<#else>$(window.parent).width()</#if>
				if (winW < 1000) {
					winW = 1000;
				}
				var whdef =  100 / 1920;
				rem = winW * whdef;// 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
				$('html').css('font-size', rem + "px");
				line();
				$('.mt-tbody').getNiceScroll().resize();
			}
		</script>
		<style>
		.mt-tbody {max-height: 4.5rem;}
		.mask-c-bottom {margin-top: .5rem;}
		.FromTo{margin-top:-0.05rem}
		.TimeControl{font-size:12px;}
		.mf-item>input {background-color: rgba(29, 32, 136, .1) ! important;color: white ! important}
		.ztree{background-color: rgba(7, 44, 86, .9) !important;}
		.ztree *{color:white!important;}
		.TimeControl .current,.TimeControl .TimeSearchBtn{margin-left:0;background-color:#00d8ff !important;}
		.TimeControl *{color:white!important;background-color: rgba(7, 44, 86, .9) !important;}
		.TimeControl .YearNav{background-color: rgba(0, 132, 255, 0) !important;}
		.swiper-pagination {height: 0.12rem;bottom: 1rem !important;}
		.datagrid-mask-msg{font-size:.15rem;border: solid .01rem rgba(0, 255, 252, 0.2);}
		.aj_items_t_yellow {text-align: left;}
		.TimeControl .YearNav ul li,.TimeControl .hide{background-color:transparent !important}
		.mCSB_inside > .mCSB_container{margin-right:.1rem}
		.div-text{
	        position: absolute;
	        top:0;
	        left:0;
	        width: 56px;
	        height: 10px;
	        background: rgba(0,0,0,0.5);
	        padding: 10px;
	        box-sizing: border-box;
	        border-radius: 2px;
	        z-index: 20;
    	}
    	.div-text>p{
        	font-size: 12px;
        	color: #fff;
        	margin-top: -.11rem;
    	}
    	.mdt-item {
		    margin-top: .09rem;
		}
		
		.mcb-left {
		    width: 3.2rem;
		}
		.aj_items_t_result {
	    	text-align: initial;
	    	max-width: 4rem;
	    }
		.span-grid-admin{
			text-decoration:underline;
		}
		.mdt-item p.mdt-icon {
			white-space: nowrap;
		}
		#reportCode{
		 word-break: break-all;
		}
		
		.mcb-r-btn {
	    	bottom: .05rem;
	    }
	    
	    .mbr20 {
		    margin-bottom: 0rem;
		}
		.mlr30 {
		    margin-left: 0rem;
		}
		.mrr30 {
		    margin-right: 0rem;
		}
		.tab-div {
		    font-size: 0;   
		}
		.tab-div>a.active {
		    background-image: linear-gradient( 
		0deg
		 , rgba(0, 168, 255, 0.8) 0%, rgba(0, 168, 255, 1) 50%, rgba(0, 168, 255, 0.8) 100%);
		    background-blend-mode: normal, normal;
		}
		.tab-div>a {
		    width: 1.2rem;
		    display: inline-block;
		    height: 0.3rem;
		    line-height: 0.30rem;
		    background-image: linear-gradient( 
		0deg
		 , rgba(0, 168, 255, 0.5) 0%, rgba(0, 168, 255, 0.29) 50%, rgba(0, 168, 255, 0.08) 100%);
		    font-size: .15rem;
		    font-weight: normal;
		    font-stretch: normal;
		    letter-spacing: 1px;
		    color: #feffff;
		    text-align: center;
		    transform: -webkit-skewX(-15deg);
		    transform: skewX( 
		-15deg
		 );
		    cursor: pointer;
		}
		.hide{
		 display: none;
		}
		.show {
		    display: block !important;
		}
		.dubanIcon{
		  top:20%;
		}
		</style>
	</head>
	<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
	<body style="background-color: transparent !important;">
		<div class="event-mask" id="eventAttr" style="width: 10rem;height: 6rem;margin-top:-3rem;left:53%;z-index:55;display:none">
            <div class="mask-main-top" style="height:0rem;">
                <i class="close" id="eventAddClose"></i>
            </div>
            <div class="mask-main-bottom" style="height:100%;">
                <iframe id="mask-eventAttr" frameborder="0" width="100%" height="100%" style="overflow:hidden"></iframe>
            </div>
        </div>
		<div class="container-fluid">
			<div class="wrap_container">
				<div class="mask-title flex flex-jc mtr30">
					<p><@block name="entryTypeHeadName">${reportTypeTitle}信息</@block></p>
				</div>
				<div class="mask-content mask-content2">
					<!--<a href="javascript:void(0);" class="mc2-back" id="mc2-back">返回</a>-->
					<div class="mc-box flex">
						
						<div class="mcb-left bs">
							<div class="mc-title bs">
								<p>基本信息</p>
							</div>
							<div class="mtr20 mask-device">
								<div class="swiper-container md-swiper-container">
									<div class="swiper-wrapper variable" id="attrDiv">
									
									</div>
									<!-- Add Pagination -->
									<div class="swiper-pagination"></div>
								</div>
								
							</div>
							
							<div class="mdt-box mtr20">
							<!-- 基础信息开始-->
								<div class="md-text mcbl-bg bs">
									<div class="angle top-left"></div>
									<div class="angle top-right"></div>
									<div class="angle bottom-left"></div>
									<div class="angle bottom-right"></div>
									<div class="mdt-item flex flex-ac">
										<div>
											<p class="mdt-icon">报告编号:</p>
										</div>
										<!-- 一般添加general，正常添加normal，紧急添加urgent -->
										<p class="general" id="reportCode"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">上报人员:</p>
										</div>
										<p class="white flex1 variable" id="reporterName"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">报告方式:</p>
										</div>
										<p class="white flex1 variable" id="reportWayName"></p>
									</div>
									<div class="mdt-item flex">
										<div>
											<p class="mdt-icon">所属网格:</p>
										</div>
										<p class="white flex1 variable" id="regionPath"></p>
									</div>
								
								</div>
							<!-- 基础信息结束-->
							<@block name="entryTypePageLeft">
							</@block>
							</div>
						</div>
						
						<div class="mcb-right flex1 mlr10">
							<div class="mcb-r-top" style="height:2.1rem">
								<div class="mc-title bs flex flex-jb">
									<p class="variable" id="eventNameStr" style="max-width: 8rem;line-height: 0.22rem;"></p>
									<div class="mcb-rtc-time clearfix">
										<p>报告时间:</p>
										<p class="variable" id="reportTimeStr"></p>
									</div>
								</div>
								<div class="mcb-rt-content" style="height:1.8rem">
									<div class="clearfix mcb-rtc-item flex">
										<p style="width: 0.41rem;">地点:</p>
										<p class="variable" id="occurred"></p>
									</div>
									<div class="mcb-rtc-title flex flex-ac flex-jb" style="height:0.5rem">
										<p class="mcb-rtc-name variable"  style="max-height: 0.5rem;"></p>
									</div>
									<div class="clearfix mcb-rtc-item flex">
										<p>当前状态:</p>
										<p class="variable" id="reportStatusName"></p>
									</div>
								</div>
								<a href="javascript:void(0);" id="db-btn" class="mcb-r-btn db-btn">督办</a>
								<a href="javascript:void(0);" id="sjhj-btn" class="mcb-r-btn sjhj-btn" style="display: block;">历史环节</a>
							</div>
							<div id="dubanIconDiv" class="dubanIcon hide" ></div>
							<div class="mcb-r-bottom mcb-r-bottom1 bs" style="height:4.2rem">
								<div class="tab-div mlr30 mrr30 mbr20" id="tabdiv" style="padding-top:.1rem">
					                <a class="active"><b>流程详情</b></a>   
					                <a class="mlr20" id="db_bt" style="display: none;"><b>督办记录</b></a>
					             </div>
								<div id="tabdivSub" >
					             	<div style="display: none" class="show"><!--流程详情  --> 
						             	<div class="mc-title bs flex flex-ac flex-jb">
											<!-- <p>流程详情</p> -->
											<div class="det-links-des">
												<ul class="flex layer_aj_bt_n flex-ac flex-je">
													<li class="flex flex-ac">
														<div class="aj_ks aj_ks_blue">
															<div class="aj_ks1"></div>
															<div class="aj_ks2"></div>
														</div>
														<p>当前环节</p>
													</li>
													<li class="flex flex-ac" style="margin-right:0.5rem">
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
										</div>
										<div class="mcb-rb-content mcb-rb-content1" style="height:3.2rem">
											<div class="layer_aj_bt_b bs flex flex-ac">
												<p class="layer_aj_bt_b1">办理环节</p>
												<p class="layer_aj_bt_b2">办理人/办理时间</p>
												<p class="layer_aj_bt_b3">办理意见</p>
											</div>
											<div class="layer_aj_bt_s">
												<div class="layer_aj_bt_line"></div>
												<ul class="layer_aj_bt_items variable" id="flowDetail">
													
												</ul>
											</div>
										</div>
					             	</div>
					             	<div id="remindListContentDiv" style="display: none"><!--督办记录  -->
					             	</div>
					             </div>	
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script>
		
			$(function(){
				initSupervise("${eventId}");
			});
			
			// 滚动条优化
			$('.mt-tbody,.mdt-box,.mcb-rb-content1,#eventContentStr').niceScroll({
				cursorcolor: '#00a0e9',
				cursoropacitymax: '.4',
				cursorwidth: ".05rem",
				cursorborderradius: ".05rem",
				cursorborder: 'none',
				autohidemode: false,
			})
			
			
			$('.close').click(function(){
				$('#mask-eventAttr').attr('src','');
				$('#eventAttr').hide();
			});
			
		
			
			// 历史环节的线
			function line(){
				$('.layer_aj_bt_line').height($('.layer_aj_bt_items').height()+38);
			}
			
			//返回键点击事件
			$('.mc2-back').click(function(){
				$('.mask-content2').hide().siblings('.mask-content1').show();
				$('.mt-tbody,.mdt-box,.mcb-rb-content1').getNiceScroll().resize();
				$('.variable').html('');
				$('#superviseRemark').val('');
				$('#otherMobileNums').val('');
				destroySwiper();
			})
			
			
			// 事件属性的swiper的初始化
			var mdSwiper;
			function eventSwiper(attrflag){
				
				if(mdSwiper!=null){
					mdSwiper.destroy(false);
				}
				if(attrflag>1){
				
				mdSwiper = new Swiper('.md-swiper-container', {
					loop : true,
                    autoplay: {
                        delay: 3000,
                        stopOnLastSlide: false,
                        disableOnInteraction: false,
                    },
					pagination: {
						el: '.swiper-pagination',
						clickable: true
					},
				});
				
				mdSwiper.autoplay.start();
				}
			}
			// 注销swiperd方法
			function destroySwiper(){
				//mdSwiper.destroy(false);
			}
			// 事件环节和督办的切换
			$('#sjhj-btn').click(function(){
				$(this).hide().siblings('#db-btn').show();
				$('.mcb-r-bottom1').show().siblings('.mcb-r-bottom2').hide();
				line();
				$('.mcb-rb-content1').getNiceScroll().resize();
			})
			$('#db-btn').click(function(){
				$(this).hide().siblings('#sjhj-btn').show();
				$('.mcb-r-bottom2').show().siblings('.mcb-r-bottom1').hide();
				$('.mcb-rb-content1').getNiceScroll().resize();
			})
			
			
			window.addEventListener("message",function(e){  
				eval(e.data);
			},false);
			
			var curEventId;
			var taskId;
			var instanceId;
			
			var imgList=[];//图片集合用于展示使用
			var imgTitleList=[];//图片标题集合用于展示使用

			//设置办理人组织层级颜色
			var handleColorArr={
				"1":"",
				"2":"",
				"3":"#e65e7d",
				"4":"#e68f66",
				"5":"#e6dd63",
				"6":"#00d1fe",
				"7":"#00d1fe",
			}
			
			var imgTitleArr={
				"1":"处理前",
				"2":"处理中",
				"3":"处理后"
			}
			
			function initSupervise(eventId,type){
				
				modleopen();
		 		$(".mask-content2").addClass('db-mc').show().siblings('.mask-content1').hide();
		 		$('#sendMessageButton').removeClass('active');
				$('#otherMobileNumsDiv').css("visibility","hidden");
				$('#smsContentDiv').css("visibility","hidden");
			 	//获取事件详情
			 	$('.layer_aj_bt_line').height(0);//清空历史环节线
			 	
			 	
			 	if(type=='list'){
		 			$(".mc2-back").css('display','unset');
		 			//列表入口需要判断是否显示督办按钮（默认显示历史环节页面）
		 			$('#sjhj-btn').hide().siblings('#db-btn').show();
					$('.mcb-r-bottom1').show().siblings('.mcb-r-bottom2').hide();
					$('.mcb-rb-content1').getNiceScroll().resize();
					
					//点击取消则返回列表页面（相当于点击左上角的返回）
					$('#cancelSupervise').click(function(){
						$('#mc2-back').click();
					});
					
					
		 			
		 					
				}else{//督办入口进入，默认显示督办页面
						
					$('#db-btn').hide().siblings('#sjhj-btn').show();
					$('.mcb-r-bottom2').show().siblings('.mcb-r-bottom1').hide();
					$('.mcb-rb-content1').getNiceScroll().resize();
					
					//点击取消则返回大屏页面（相当于点击右上角的×）
					$('#cancelSupervise').click(function(){
						closeIframe();
						window.parent.postMessage('addRemindCallBack()','${biDomain!""}/bigScreen/cityManagement/index.jhtml');
					});
					
				}
				
			 	$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/eventAnalyseController/getNananEntryEventInfo.json',
					data: {"eventId":eventId<@block name="entryTypeDetailParams">,"reportType":'${reportType}',"listType":'5'</@block>},
					dataType:"json",
					success: function(data){
						console.log(data);
						var event=data.event;
						
						//设置全局变量
						curEventId=event.eventId;
						taskId=data.process[0].TASK_ID;
						instanceId=data.instanceId;
						if(!instanceId && data.process.length>1){
							instanceId=data.process[1].INSTANCE_ID
						}
						$('#reportCode').removeClass().addClass('general variable').html(event.reportCode).attr('title',event.reportCode);
						$('#reportCode').parent().removeClass('flex-ac');
						$('#reporterName').html(event.reporterName);
						$('#reportWayName').html(event.reportWayName);
						$('#regionPath').html(event.regionPath);
						$('#reportTimeStr').html(event.reportTimeStr);
						$('#occurred').html(event.occurred);
						$('#reportStatusName').html(event.reportStatusName);
						
						<@block name="entryTypeLeftContentSet">
						</@block>
						$('#remark').html(event.remark||'');
						if(data.process[0].IS_CURRENT_TASK){
							$('#curTask').html(formatStr(data.process[0].TASK_NAME));
						}else{
							$('#curTask').html('结案');
						}
						
								
		 				$('#sjhj-btn').hide().siblings('#db-btn').show();
						$('#db-btn').hide();
						$('.mcb-r-bottom1').show().siblings('.mcb-r-bottom2').hide();
						$('.mdt-box,.mcb-rb-content1').getNiceScroll().resize();
						
						//填充环节信息
						var flow=data.process;
						var flowStr="";
						for(var i=0,j=flow.length;i<j;i++){
							var d=flow[i];
							if(!d.PARENT_PRO_TASK_ID){
							
								//如果是当前环节
								if(d.IS_CURRENT_TASK){
									flowStr+='<li class="flex flex-ac">'+
									'<h5 class="aj_items_h aj_items_h_green">'+d.TASK_NAME+'</h5>'+
									'<div class="aj_ks aj_ks_blue">'+
									'<div class="aj_ks1"></div>'+
									'<div class="aj_ks2"></div>'+
							        '</div>'+
							        '<div>'+
									'<div class="flex flex-ac">';
									<#if canContactGridAdmin??>
										if(d.HANDLE_PERSON_USER_ID){
											var _userIds = d.HANDLE_PERSON_USER_ID.split(';')
											var _userNames = d.HANDLE_PERSON.split(';')
											var _userOrgIds = d.HANDLE_PERSON_ORG_ID.split(';')
											var _lis = '';
											if(Array.isArray(_userIds) && _userIds.length>0){
												for (var k = 0,l=_userIds.length; k <l ; k++) {
													if(_userIds[k]){
														var _onclickStr = ' onclick="showGridAdminDetail('+_userIds[k]+','+_userOrgIds[k]+')" ';
														_lis += '<span style="color:'+handleColorArr[d.ORG_LEVEL]+';CURSOR: pointer; text-underline-position:auto;" onmouseover="this.className=\'span-grid-admin\'" onmouseout="this.className=\'\'" ' + _onclickStr +'>'+_userNames[k]+'<img src="${uiDomain!}/web-assets/_big-screen/nanAn/images/zdry/icon-list-t03b.png" /></span><br/>';
													}
												}
											}
											if(_lis){
												flowStr+='<p class="aj_items_t aj_items_t_yellow" >' + _lis +'</p></div>';
											}
										}
									<#else>
										flowStr+='<p class="aj_items_t aj_items_t_yellow">'+d.HANDLE_PERSON+'</p></div>';
									</#if>
	
							        if(d.subAndReceivedTaskList!=null && d.subAndReceivedTaskList.length>0){
							        
							        	for(var m=0,n=d.subAndReceivedTaskList.length;m<n;m++){
							        		var t=d.subAndReceivedTaskList[m];
							        		
							        		flowStr+='<div class="flex flex-ac mtr10">'+
							        		'<div class="aj_items_t2 flex">'+
							        		'<p class="aj_items_t aj_items_t_yellow" style="margin-left:0rem;text-align: left">'+t.TRANSACTOR_NAME+'('+t.ORG_NAME+')'+'<br>';
							        		if(m==0){
							        			flowStr+='<span>接受时间</span>:'+t.RECEIVE_TIME;
							        		}else{
							        			flowStr+='<span>处理时间</span>:'+t.END_TIME;
							        		}
							        		
							        		if(t.REMARKS){
							        			flowStr+='<p class="aj_items_t aj_items_t_result">'+t.REMARKS+'</p>'
							        		}
							        	
							        	}
							        
							        }
								
							        flowStr+='</div>'+
							        '</li>';
								}else{
								
									flowStr+='<li class="flex flex-ac">'+
									'<h5 class="aj_items_h aj_items_h_green">'+d.TASK_NAME+'</h5>';
									
									if(d.OPERATE_TYPE==2){//驳回环节
										flowStr+='<div class="aj_ks aj_ks_gray">'+
											'<div class="aj_ks1" style="background-color: #FFB90F"></div>'+
											'<div class="aj_ks2"></div>'+
											'</div>';
									}else{
									
										flowStr+='<div class="aj_ks aj_ks_gray">'+
										'<div class="aj_ks1"></div>'+
										'<div class="aj_ks2"></div>'+
										'</div>';
									
									}
									if(d.TRANSACTOR_NAME&&d.ORG_NAME&&d.INTER_TIME&&d.START_TIME){
										flowStr+='<div class="aj_items_t2 flex">'+
										'<p class="aj_items_t aj_items_t_green bs">'+formatStr(d.TRANSACTOR_NAME)+'('+d.ORG_NAME+')'+'<span> 耗时 '+d.INTER_TIME+'</span><br><span>办结时间</span>:'+d.END_TIME;
										if(d.DISTRIBUTE_USER_ORG_NAMES){//分送
											flowStr+='<br><span>分送人员:</span>'+d.DISTRIBUTE_USER_ORG_NAMES;
										}
										if(d.SELECT_USER_ORG_NAMES){//选送
											flowStr+='<br><span>选送人员:</span>'+d.SELECT_USER_ORG_NAMES;
										}
										flowStr+='</p></div>';
									}
									if(d.REMARKS){
										flowStr+='<p class="aj_items_t aj_items_t_result">'+d.REMARKS+'</p>';
									}
									flowStr+='</li>';
								
								}
							}
						
						}
						
						$('#flowDetail').html(flowStr);
						fetchRemindInfo();
						
						line();
						
						if(type=='list'){
		 					$(".mc2-back").css('display','unset');
						}else{//督办入口进入，默认显示督办页面
						
						}
						
						
						// 滚动条优化
						$('.mcb-rb-content1').getNiceScroll().resize();
						
						
						//填充附件轮播信息
						var attrStr="";
						imgList=[];
						imgTitleList=[];
						var imgs=data.imgs;
						
						var sounds=data.sounds;
						var videos=data.videos;
						var attrflag=0;
						if(imgs.length>0){
							for(var i=0,j=imgs.length;i<j;i++){
								var d=imgs[i];
								imgList.push("${IMG_URL}"+d.filePath);
								imgTitleList.push(imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前");
								attrStr+='<div class="swiper-slide">';
								attrStr+='<div class="div-text"><p>'+(imgTitleArr[d.eventSeq]?imgTitleArr[d.eventSeq]:"处理前")+'</p></div>';
								attrStr+='<img style="cursor:pointer;z-index:10" onclick="showImg('+i+')" src="${IMG_URL}'+d.filePath+'" >';
								attrStr+='</div>';
								attrflag+=1;
							}
						}
						if(sounds.length>0){
							for(var i=0,j=sounds.length;i<j;i++){
								var d=sounds[i];
								attrStr+='<div class="swiper-slide">'+
								'<img  style="cursor:pointer;z-index:10" onclick="showVideo('+d.attachmentId+')"  src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg">'+
								'</div>';
								attrflag+=1;
							}
						}
						if(videos.length>0){
							for(var i=0,j=videos.length;i<j;i++){
								var d=videos[i];
								attrStr+='<div class="swiper-slide">'+
								'<img style="cursor:pointer;z-index:10" onclick="showVideo('+d.attachmentId+')"  src="${rc.getContextPath()}/scripts/updown/swfupload/images/thumbnail/audio.jpg">'+
								'</div>';
								attrflag+=1;
							}
						}
						
						if(attrStr==''){
							attrStr+='<div class="swiper-slide">'+
							'<img src="${uiDomain!''}/images/nopic.jpg">'+
							'</div>';
						}
				
						
						$('#attrDiv').html(attrStr);
						
						eventSwiper(attrflag);//初始化轮播
						
					},
					complete:function(){
						modleclose();
					}
				});
				
			}

			<#if canContactGridAdmin??>
			function showGridAdminDetail(userId,orgId){
				modleopen();
				$.ajax({
					type: "POST",
					async:false,
					url: '${rc.getContextPath()}/zhsq/event/eventAnalyseController/findInfoOrgCodesByOrgIds.json',
					data: {"orgId":orgId},
					dataType:"json",
					success: function (data) {
						if(data && data.length>0) {
							modleopen();
							$.ajax({
								type: "post",
								url: "${SQ_ZZGRID_URL}/zzgl/grid/gridAdmin/findGridAdminListByUserIdJsonp.json?jsoncallback=?",
								data: {
									'userId': userId,
									'infoOrgCode': data[0].ORG_CODE
								},
								dataType: "jsonp",//数据类型为jsonp
								success: function (data) {
									if(Array.isArray(data.result) && data.result.length>0){
										var userInfo = data.result[0];
										var paramsData = {
											labelType: 'grider',
											wid: userInfo.gridAdminId,
											name: userInfo.partyName
										};
										var urlDomain = "${GIS_DOMAIN!}/gis/base/arcgis/commonCrossDomain.jhtml?callBackFunction=" + "summaryCallBack(" + encodeURIComponent(JSON.stringify(paramsData).replace(/"/g, "'")) + ")";
										$("#cross_domain_iframe").attr("src", urlDomain);
									}else{
										$.messager.alert('提示','网格员管理模块无有效网格员，请确认网格员是否调整!','info');
									}
								},
								error: function (e) {
									console.log(e);
								},
								complete:function(){
									modleclose();
								}
							});
						}
					},
					complete:function(){
						modleclose();
					}
				});
			}
			</#if>
			
			var begintime;
			var endtime;
			
			function initTime(){
        		var begin=new Date();
        		var end=new Date();
        		new Date(begin.setYear((new Date().getFullYear()-1)));
        		new Date(begin.setMonth((new Date().getMonth()+1)));
        		begintime= begin.format("yyyy-MM")+'-01';
        		endtime=end.format("yyyy-MM-dd");
        	}
        	
        	Date.prototype.format = function(format) {
    			var o = {
        		"M+" : this.getMonth() + 1, // month
        		"d+" : this.getDate(), // day
        		"h+" : this.getHours(), // hour
        		"m+" : this.getMinutes(), // minute
        		"s+" : this.getSeconds(), // second
        		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
        		"S" : this.getMilliseconds()
    			// millisecond
    			}
    			if (/(y+)/.test(format))
        			format = format.replace(RegExp.$1, (this.getFullYear() + "")
                		.substr(4 - RegExp.$1.length));
    				for ( var k in o)
        				if (new RegExp("(" + k + ")").test(format))
            				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    			: ("00" + o[k]).substr(("" + o[k]).length));
    			return format;
			}
			
			var curPage=1;
			var pageSize=10;
			var totalPage=1;
			
			var infoOrgCode;
			var defaultOrgCode;
			var urgencyDegree;
			var defaultGridName;
			var status;
			var startGridId;
			var eventStatus='00,01,02,03';//大屏上的列表暂时都只显示未办结的事件，排除已归档的事件
			
			
			function formatStr(str){
				if(str == null ||str == 'null' || str == undefined ){
					return '';
				}else{
					return str;				
				}
			}
			
			
			
			function showImg(i){
				$('#eventAttr').show();
				$('#mask-eventAttr').attr('src','${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?drag=true&background=none&overflow=none&fieldId=playImg&index='+i+'&titles='+imgTitleList.toString()+'&paths='+imgList.toString());
				//window.open('${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId=playImg&index='+i+'&paths='+imgList.toString());
			}
		
			function showVideo(id){//eventSeq
				$('#eventAttr').show();
				$('#mask-eventAttr').attr('src','${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?isBigScreen=1&videoType=2&attachmentId='+id);
				//window.open('${rc.getContextPath()}/zhsq/att/toSeeVideo.jhtml?videoType=2&attachmentId='+id);
			}
			
			//每隔三位加个逗号
			function formatInteger(nStr){
		
	 			nStr += '';  
        		x = nStr.split('.');  
        		x1 = x[0];  
        		x2 = x.length > 1 ? '.' + x[1] : '';  
        		var rgx = /(\d+)(\d{3})/;  
        		while (rgx.test(x1)) {  
            		x1 = x1.replace(rgx, '$1' + ',' + '$2');  
        		}  
        		return x1 + x2;  
	
			}
	
			//计算百分数保留2位小数并加上百分号
			function formatDouble(double){
	
				var str=Number(double*100).toFixed(2);
    			str+="%";
    			return str;
	
			}
			//督办记录获取初始化
			$(function(){
		        $('#tabdiv').on('click','a',function(){
		            $(this).addClass('active').siblings().removeClass('active');
		             $('#tabdivSub>div').eq($(this).index()).addClass('show').siblings().removeClass('show');
		          var showIndex = $(this).index()
		            if(showIndex==1){
		            	$('.nicescroll-cursors').hide();
		            }else{
		            	$('.nicescroll-cursors').show();
		            	 $('.h-x').niceScroll().resize();
		            }    
		        });
			});		
			
			//获取督办记录
			function fetchRemindInfo() { 
		        $.ajax({
		            type: "POST",
		            url : '/zhsq_event/zhsq/workflow4Base/findUrgeOrRemindList.jhtml',
		            data: {'instanceId': instanceId, 'category': '2'},
		            dataType:"json",
		            success: function(data){
		                var superviseResultList = data.remindMapList;

		                if(superviseResultList && superviseResultList.length > 0) {
		                    var superviseResultContent = "", supervise = null;
		                    $("#dubanIconDiv").show();
		                    $("#db_bt").show();
		                    

		                    for(var index in superviseResultList) {
		                        supervise = superviseResultList[index];
		                        superviseResultContent += '<p class="aj_items_t aj_items_t_green bs" style="max-width:12rem;margin-top:.1rem">';
		                        if(supervise.remindUserName) {
		                            superviseResultContent += '	  <span style="color:#00d1fe;">' + supervise.remindUserName + '</span>';
		                        }
		                        if(supervise.remindDateStr) {
		                            superviseResultContent += '	  <span>于 ' + supervise.remindDateStr+ '</span>';
		                        }
		                        superviseResultContent += '<span style="color: #ffd34c">  督办</span>';
		                        superviseResultContent += '<br>';
		                        if(supervise.remarks) {
		                            superviseResultContent += '			<span>' + supervise.remarks+ '</span>';
		                        }
		                        superviseResultContent += '</p>';
		                    } 
		                    
		                    $("#remindListContentDiv").html(superviseResultContent);
		                }else{
		                	 $("#dubanIconDiv").hide();
		                	 $("#db_bt").hide();
		                }
		                
		            },
		            error:function(data){
		                $.messager.alert("错误", "获取督办信息失败！", "error");
		            }
		        });
		    }	
		 function setValue(event,key,unit){
		 	$('#'+key).html(event[key]+(unit?unit:'')).parent().removeClass('hide');
		 }
		 function setOwnValue(event,key){
		 	$('#'+key).html(event[key]||'');
		 }
		</script>
	</body>
</html>
